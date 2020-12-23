package com.gitlab.amirmehdi.service.sahra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OMS;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.TelegramMessageSender;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.service.dto.sahra.*;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderCreditSource;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderType;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderValidity;
import com.gitlab.amirmehdi.service.dto.sahra.exception.CodeException;
import com.gitlab.amirmehdi.util.JalaliCalendar;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class SahraRequestService implements CommandLineRunner {
    public static final String HUB = "omsclienthub";
    private final TokenRepository tokenRepository;
    private final BourseCodeRepository bourseCodeRepository;
    private final NegotiateManager negotiateManager;
    private final RestTemplate restTemplate;
    private final RestTemplate longPollRestTemplate;
    private final TaskScheduler executor;
    private final ObjectMapper objectMapper;
    private final MessageHandler handler;
    private final TelegramMessageSender telegramMessageSender;
    private final ApplicationProperties applicationProperties;
    private final Map<Long, SecurityFields> securityFieldsMap = new HashMap<>();

    private final String connectUrl = "%s/realtime/connect?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String pollUrl = "%s/realtime/poll?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String sendUrl = "%s/realtime/send?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";


    public SahraRequestService(TokenRepository tokenRepository, BourseCodeRepository bourseCodeRepository, RestTemplate restTemplate, NegotiateManager negotiateManager, @Qualifier("longPollRestTemplate") RestTemplate longPollRestTemplate, TaskScheduler executor, ObjectMapper objectMapper, MessageHandler handler, TelegramMessageSender telegramMessageSender, ApplicationProperties applicationProperties) {
        this.tokenRepository = tokenRepository;
        this.bourseCodeRepository = bourseCodeRepository;
        this.restTemplate = restTemplate;
        this.negotiateManager = negotiateManager;
        this.longPollRestTemplate = longPollRestTemplate;
        this.executor = executor;
        this.objectMapper = objectMapper;
        this.handler = handler;
        this.telegramMessageSender = telegramMessageSender;
        this.applicationProperties = applicationProperties;
    }

    /*
        @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(value = 3000))
            */
    @Scheduled(cron = "0 1 8 * * *")
    public void connectAndStart() {
        List<BourseCode> bourseCodes = bourseCodeRepository.findAllByBrokerIn(Broker.byOms(OMS.SAHRA));
        for (BourseCode bourseCode : bourseCodes) {
            try {
                connectAndStart(bourseCode);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == 401) {
                    clearConnection(bourseCode);
                    // message to telegram
                }
            } catch (ResourceAccessException e) {
                if (securityFieldsMap.get(bourseCode.getId()).getGroupToken() == null) {
                    clearConnection(bourseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void connectAndStart(BourseCode bourseCode) {
        SecurityFields securityFields;
        if (securityFieldsMap.containsKey(bourseCode.getId())) {
            securityFields = securityFieldsMap.get(bourseCode.getId());
            securityFields.clear();
        } else {
            securityFields = new SecurityFields();
        }
        securityFieldsMap.put(bourseCode.getId(), securityFields);
        String tokenString;
        if (bourseCode.getToken()==null){
            tokenString = login(bourseCode);
            Token token = new Token()
                .bourseCode(bourseCode)
                .broker(bourseCode.getBroker())
                .token(tokenString);
            tokenRepository.save(token);
            bourseCode.setToken(token);
            bourseCodeRepository.save(bourseCode);
        }else if (ChronoUnit.HOURS.between(bourseCode.getToken().getCreatedAt().toInstant(), new Date().toInstant()) > 6) {
            tokenString = login(bourseCode);
            bourseCode.getToken().setToken(tokenString);
            tokenRepository.save(bourseCode.getToken());
        }
        securityFields.setToken(bourseCode.getToken());

        NegotiateResponse negotiate = negotiateManager.negotiate(securityFields.getToken());
        securityFields.setConnectionToken(negotiate.getConnectionToken());

        ConnectResponse connect = connect(securityFields);
        securityFields.setMessageId(connect.getMessageId());

        negotiateManager.start(securityFields);

        FirstPollResponse firstPollResponse = firstPoll(securityFields);
        securityFields.setMessageId(firstPollResponse.getMessageId());
        securityFields.setGroupToken(firstPollResponse.getGroupsToken());

        firstPollResponse.getM().forEach(pollMessageResponse -> {
            handler.handle(bourseCode, pollMessageResponse);
        });

        securityFields.getSchedules().add(executor.scheduleWithFixedDelay(() -> {
            if (!isHeadlineTime()) {
                handler.handle(bourseCode, poll(securityFields));
            }
        }, 500));
        securityFields.getSchedules().add(executor.scheduleWithFixedDelay(() -> {
            getTime(securityFields.getToken().getBourseCode().getId());
        }, 60000));
    }

    private String login(BourseCode bourseCode) {
        String tokenString;
        try {
            tokenString = negotiateManager.login(bourseCode, 3);
        } catch (LoginFailedException e) {
            telegramMessageSender.sendMessage(new TelegramMessageDto(applicationProperties.getTelegram().getHealthCheckChat(), "sahra can't login. number of attempts reached"));
            throw e;
        }
        return tokenString;
    }

    private boolean isHeadlineTime() {
        String[] cron = applicationProperties.getHeadline().getCron().split(" ");
        int millis = (int) (applicationProperties.getHeadline().getRepeat() * applicationProperties.getHeadline().getSleep());

        LocalTime from = LocalTime.of(Integer.parseInt(cron[2]), Integer.parseInt(cron[1]), Integer.parseInt(cron[0]));
        LocalTime to = from.plus(millis, ChronoUnit.MILLIS);

        return !LocalTime.now().isBefore(from) && !LocalTime.now().isAfter(to);
    }

    //"{\"H\":\"omsclienthub\",\"M\":\"GetAssetsReport\",\"A\":[],\"I\":7}"
    public ObjectNode getAssetReport(long userId) {
        SendRequest sendRequest = new SendRequest("GetAssetsReport", Collections.emptyList());
        return send(userId, sendRequest);
    }

    // "{\"H\":\"omsclienthub\",\"M\":\"GetAcountRemainReport\",\"A\":[1,1399,9,4,1399,9,4],\"I\":6}"
    public ObjectNode getAccountRemainReport(long userId, LocalDate localDate) {
        JalaliCalendar j = new JalaliCalendar(localDate);
        SendRequest sendRequest = new SendRequest("GetAcountRemainReport"
            , Arrays.asList(1, j.getYear(), j.getMonth(), j.getDay(), j.getYear(), j.getMonth(), j.getDay()));
        return send(userId, sendRequest);
    }

    //"{\"H\":\"omsclienthub\",\"M\":\"GetTime\",\"A\":[],\"I\":5}";
    //{"R":"19:35:42","I":"7"}
    public LocalTime getTime(long userId) {
        SendRequest sendRequest = new SendRequest("GetTime", Collections.emptyList());
        return LocalTime.parse(send(userId, sendRequest).get("R").asText());
    }

    //    {"H":"omsclienthub","M":"CancelOrder","A":[1170000000356607],"I":6}
    public void cancelOrder(Order order) {
        SendRequest sendRequest = new SendRequest("CancelOrder", Collections.singletonList(Long.valueOf(order.getOmsId())));
        send(order.getBourseCode().getId(), sendRequest);
    }

    // "{\"H\":\"omsclienthub\",\"M\":\"AddOrder\",\"A\":[[1,\"IRO1MAPN0001\",1481,18950,1,null,null,null,null,null,null,null]],\"I\":1}";
    //{"R":{"ex":{"i":-2006,"m":"اعتبار کافی نیست.(اعتبار مورد نیاز 330,608,884 ریال)"}},"I":"9"}
    public void sendOrder(Order order) throws CodeException {
        if (order.getId() == null) {
            return;
        }
        SendRequest sendRequest = new SendRequest("AddOrder", Collections.singletonList(Arrays.asList(
            OrderType.toOrderType(order.getSide()).getValue()
            , order.getIsin()
            , order.getQuantity()
            , order.getPrice()
            , OrderCreditSource.Broker.getValue()
            , null
            , null
            , OrderValidity.fromOrderValidity(order.getValidity()).getValue()
            , null
            , null
            , null
            , null
            , String.valueOf(order.getId())

        )));
        send(order.getBourseCode().getId(), sendRequest);
    }

    private ConnectResponse connect(SecurityFields securityFields) {
        ResponseEntity<ConnectResponse> connectResponse =
            restTemplate.exchange(
                URI.create(String.format(connectUrl, securityFields.getToken().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>(getConnectOrSendHeaders(securityFields))
                , ConnectResponse.class);
        log.info("connect, response:{}", connectResponse);
        return connectResponse.getBody();
    }

    private ObjectNode send(long userId, SendRequest data) {
        SecurityFields securityFields = securityFieldsMap.get(userId);
        if (securityFields == null) {
            return null;
        }
        if (securityFields.getGroupToken() == null)
            return null;
        data.setI(securityFields.incAndGet());
        log.info("send request {}", data);
        ResponseEntity<ObjectNode> sendResponse;
        try {
            sendResponse = restTemplate.exchange(
                URI.create(String.format(sendUrl, securityFields.getToken().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>("data=" + getEncode(objectMapper.writeValueAsString(data)), getConnectOrSendHeaders(securityFields))
                , ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        log.info("send, response:{}", sendResponse);
        ObjectNode node = sendResponse.getBody();
        if (node != null && node.has("R") && node.get("R").has("ex")) {
            long errorCode = node.get("R").get("ex").get("i").asLong();
            String errorDesc = node.get("R").get("ex").get("m").asText();
            if (errorCode == -3005) {
                BourseCode bourseCode = bourseCodeRepository.findById(userId).get();
                clearConnection(bourseCode);
                connectAndStart(bourseCode);
                return send(userId, data);
            }
            throw new CodeException(errorCode, errorDesc);
        }
        return node;
    }

    public FirstPollResponse firstPoll(SecurityFields securityFields) {
        ResponseEntity<FirstPollResponse> firstPollResponse =
            longPollRestTemplate.exchange(
                URI.create(String.format(pollUrl, securityFields.getToken().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.POST
                , new HttpEntity<>("messageId=" + getEncode(securityFields.getMessageId()), getConnectOrSendHeaders(securityFields))
                , FirstPollResponse.class);
        log.info("firstPoll, response:{}", firstPollResponse);
        return firstPollResponse.getBody();
    }

    public PollResponse poll(SecurityFields securityFields) {
        if (securityFields.getGroupToken() == null)
            return null;
        try {
            ResponseEntity<PollResponse> pollResponse =
                longPollRestTemplate.exchange(
                    URI.create(String.format(pollUrl, securityFields.getToken().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                    , HttpMethod.POST
                    , new HttpEntity<>("messageId=" + getEncode(securityFields.getMessageId()) + "&groupsToken=" + getEncode(securityFields.getGroupToken()), getConnectOrSendHeaders(securityFields))
                    , PollResponse.class);
            log.debug("poll, response:{}", pollResponse);
            securityFields.setMessageId(pollResponse.getBody().getMessageId());
            return pollResponse.getBody();
        } catch (ResourceAccessException e) {
            log.debug("poll nochange");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                clearConnection(securityFields.getToken().getBourseCode());
            }
        }
        return null;
    }


    private LinkedMultiValueMap<String, String> getConnectOrSendHeaders(SecurityFields securityFields) {
        String[] s = securityFields.getToken().getToken().split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "text/plain, */*; q=0.01");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Origin", "https://firouzex.ephoenix.ir");
        headers.add("Sec-Fetch-Site", "same-origin");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://firouzex.ephoenix.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }

    @Override
    public void run(String... args) throws Exception {
        connectAndStart();
    }

    private void clearConnection(BourseCode bourseCode) {
        if (securityFieldsMap.containsKey(bourseCode.getId())) {
            securityFieldsMap.get(bourseCode.getId()).clear();
            securityFieldsMap.remove(bourseCode.getId());
        }
        telegramMessageSender.sendMessage(new TelegramMessageDto(applicationProperties.getTelegram().getHealthCheckChat(),
            String.format("%s in %s token is expired", bourseCode.getName(), bourseCode.getBroker())));
    }
}

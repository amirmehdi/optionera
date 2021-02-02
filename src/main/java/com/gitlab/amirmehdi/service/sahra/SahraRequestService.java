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
import com.gitlab.amirmehdi.service.TokenUpdater;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.service.dto.sahra.*;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderCreditSource;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderType;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderValidity;
import com.gitlab.amirmehdi.service.dto.sahra.exception.CodeException;
import com.gitlab.amirmehdi.util.JalaliCalendar;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import java.util.concurrent.atomic.AtomicInteger;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class SahraRequestService implements TokenUpdater {
    public static final String HUB = "omsclienthub";
    private final TokenRepository tokenRepository;
    private final BourseCodeRepository bourseCodeRepository;
    private final NegotiateManager negotiateManager;
    private final RestTemplate restTemplate;
    private final RestTemplate longPollRestTemplate;
    private final ObjectMapper objectMapper;
    private final MessageHandler handler;
    private final TelegramMessageSender telegramMessageSender;
    private final ApplicationProperties applicationProperties;
    private final HashMap<Long, AtomicInteger> requestsNumber = new HashMap<>();
    private final String connectUrl = "%s/realtime/connect?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String pollUrl = "%s/realtime/poll?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String sendUrl = "%s/realtime/send?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";


    public SahraRequestService(TokenRepository tokenRepository, BourseCodeRepository bourseCodeRepository, RestTemplate restTemplate, NegotiateManager negotiateManager, @Qualifier("longPollRestTemplate") RestTemplate longPollRestTemplate, ObjectMapper objectMapper, MessageHandler handler, TelegramMessageSender telegramMessageSender, ApplicationProperties applicationProperties) {
        this.tokenRepository = tokenRepository;
        this.bourseCodeRepository = bourseCodeRepository;
        this.restTemplate = restTemplate;
        this.negotiateManager = negotiateManager;
        this.longPollRestTemplate = longPollRestTemplate;
        this.objectMapper = objectMapper;
        this.handler = handler;
        this.telegramMessageSender = telegramMessageSender;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public OMS getOMS() {
        return OMS.SAHRA;
    }

    @Override
    public void updateAllTokens() {
        if (!applicationProperties.getBrokers().isSahraEnable()) {
            return;
        }
        List<BourseCode> bourseCodes = bourseCodeRepository.findAllByBrokerIn(Broker.byOms(OMS.SAHRA));
        for (BourseCode bourseCode : bourseCodes) {
            try {
                updateToken(bourseCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("finish initial sahra tokens, connectAndStart");
    }

    @Override
    public void updateToken(BourseCode bourseCode) {
        if (!bourseCode.getConditions().contains("login")) return;
        String loginCount = bourseCode.getConditions().substring(bourseCode.getConditions().indexOf("login") + 5, bourseCode.getConditions().indexOf(","));
        int tokenNeeded = 1;
        if (!loginCount.isEmpty()) {
            tokenNeeded = Integer.parseInt(loginCount);
        }
        if (tokenNeeded <= bourseCode.getTokens().size()) {
            return;
        }
        tokenNeeded -= bourseCode.getTokens().size();
        for (int i = 0; i < tokenNeeded; i++) {
            try {
                getNewToken(bourseCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getNewToken(BourseCode bourseCode) {
        String tokenString = login(bourseCode);
        Token token = new Token()
            .bourseCode(bourseCode)
            .token(tokenString);
        bourseCode.addToken(token);
        NegotiateResponse negotiate = negotiateManager.negotiate(token);
        ConnectResponse connect = connect(token, negotiate.getConnectionToken());
        negotiateManager.start(token, negotiate.getConnectionToken());
        FirstPollResponse firstPollResponse = firstPoll(token, negotiate.getConnectionToken(), connect.getMessageId());

        firstPollResponse.getM().forEach(pollMessageResponse -> {
            handler.handle(bourseCode, pollMessageResponse);
        });

        String securityFields = negotiate.getConnectionToken() +
            "__" +
            firstPollResponse.getMessageId() +
            "__" +
            firstPollResponse.getGroupsToken();
        token.securityFields(securityFields);
        tokenRepository.save(token);
        bourseCodeRepository.save(bourseCode);
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
    public ObjectNode getAssetReport(BourseCode bourseCode) {
        SendRequest sendRequest = new SendRequest("GetAssetsReport", Collections.emptyList());
        return send(bourseCode, sendRequest, false);
    }

    // "{\"H\":\"omsclienthub\",\"M\":\"GetAcountRemainReport\",\"A\":[1,1399,9,4,1399,9,4],\"I\":6}"
    public ObjectNode getAccountRemainReport(BourseCode bourseCode, LocalDate localDate) {
        JalaliCalendar j = new JalaliCalendar(localDate);
        SendRequest sendRequest = new SendRequest("GetAcountRemainReport"
            , Arrays.asList(1, j.getYear(), j.getMonth(), j.getDay(), j.getYear(), j.getMonth(), j.getDay()));
        return send(bourseCode, sendRequest, false);
    }

    //"{\"H\":\"omsclienthub\",\"M\":\"GetTime\",\"A\":[],\"I\":5}";
    //{"R":"19:35:42","I":"7"}
    public LocalTime getTime(BourseCode bourseCode) {
        SendRequest sendRequest = new SendRequest("GetTime", Collections.emptyList());
        return LocalTime.parse(send(bourseCode, sendRequest, false).get("R").asText());
    }

    //    {"H":"omsclienthub","M":"CancelOrder","A":[1170000000356607],"I":6}
    public void cancelOrder(Order order) {
        SendRequest sendRequest = new SendRequest("CancelOrder", Collections.singletonList(Long.valueOf(order.getOmsId())));
        send(order.getBourseCode(), sendRequest, true);
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
        send(order.getBourseCode(), sendRequest, true);
    }

    private ConnectResponse connect(Token token, String connectionToken) {
        ResponseEntity<ConnectResponse> connectResponse =
            restTemplate.exchange(
                URI.create(String.format(connectUrl, token.getBourseCode().getBroker().url, getEncode(connectionToken), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>(getConnectOrSendHeaders(token))
                , ConnectResponse.class);
        log.info("connect, response:{}", connectResponse);
        return connectResponse.getBody();
    }

    private ObjectNode send(BourseCode bourseCode, SendRequest data, boolean retry) {
        Optional<Token> tokenOptional = bourseCode.getRandomToken();
        if (!tokenOptional.isPresent()) {
            updateToken(bourseCode);
        }
        Token token = bourseCode.getRandomToken().get();
        SahraSecurityObject securityFields = token.toSecurityFields();
        if (securityFields == null) {
            return null;
        }
        if (securityFields.getGroupToken() == null)
            return null;
        data.setI(getI(bourseCode));
        LocalTime requestStart = LocalTime.now();
        ResponseEntity<ObjectNode> sendResponse;
        try {
            sendResponse = restTemplate.exchange(
                URI.create(String.format(sendUrl, bourseCode.getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>("data=" + getEncode(objectMapper.writeValueAsString(data)), getConnectOrSendHeaders(token))
                , ObjectNode.class);
        } catch (JsonProcessingException e) {
            log.error("send request startTime: {} userId: {} data: {}", requestStart, bourseCode.getId(), data);
            e.printStackTrace();
            return null;
        }
        LocalTime requestFinish = LocalTime.now();
        log.info("send request startTime: {} endTime: {} userId: {} data: {} response: {}"
            , requestStart, requestFinish, bourseCode.getId(), data, sendResponse.getBody());
        ObjectNode node = sendResponse.getBody();
        if (node != null && node.has("R") && node.get("R").has("ex")) {
            long errorCode = node.get("R").get("ex").get("i").asLong();
            String errorDesc = node.get("R").get("ex").get("m").asText();
            if (errorCode == -3005) {
                clearConnection(token);
                if (retry) {
                    return send(bourseCode, data, false);
                }
            }
            throw new CodeException(errorCode, errorDesc);
        }
        return node;
    }

    private int getI(BourseCode bourseCode) {
        if (!requestsNumber.containsKey(bourseCode.getId())) {
            requestsNumber.put(bourseCode.getId(), new AtomicInteger(0));
        }
        return requestsNumber.get(bourseCode.getId()).incrementAndGet();
    }

    public FirstPollResponse firstPoll(Token token, String connectionToken, String messageId) {
        ResponseEntity<FirstPollResponse> firstPollResponse =
            longPollRestTemplate.exchange(
                URI.create(String.format(pollUrl, token.getBourseCode().getBroker().url, getEncode(connectionToken), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.POST
                , new HttpEntity<>("messageId=" + getEncode(messageId), getConnectOrSendHeaders(token))
                , FirstPollResponse.class);
        log.info("firstPoll, response:{}", firstPollResponse);
        return firstPollResponse.getBody();
    }

    public PollResponse poll(Token token) {
        SahraSecurityObject securityFields = token.toSecurityFields();
        try {
            ResponseEntity<PollResponse> pollResponse =
                longPollRestTemplate.exchange(
                    URI.create(String.format(pollUrl, token.getBourseCode().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                    , HttpMethod.POST
                    , new HttpEntity<>("messageId=" + getEncode(securityFields.getMessageId()) + "&groupsToken=" + getEncode(securityFields.getGroupToken()), getConnectOrSendHeaders(token))
                    , PollResponse.class);
            log.debug("poll, response:{}", pollResponse);
//            securityFields.setMessageId(pollResponse.getBody().getMessageId());
            return pollResponse.getBody();
        } catch (ResourceAccessException e) {
            log.debug("poll nochange");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                clearConnection(token);
            }
        }
        return null;
    }


    private LinkedMultiValueMap<String, String> getConnectOrSendHeaders(Token token) {
        String[] s = token.getToken().split("__");
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

    private void clearConnection(Token token) {
        tokenRepository.delete(token);
        BourseCode bourseCode = bourseCodeRepository.findById(token.getBourseCode().getId()).get();
        if (bourseCode.getTokens().isEmpty()) {
            updateToken(bourseCode);
        }
        telegramMessageSender.sendMessage(new TelegramMessageDto(applicationProperties.getTelegram().getHealthCheckChat(),
            String.format("%s in %s token is expired", bourseCode.getName(), bourseCode.getBroker())));
    }

    @Scheduled(fixedDelay = 1000)
    public void pollForBourseCodes() {
        if (isHeadlineTime()) {
            return;
        }
        bourseCodeRepository.findAllByBrokerIn(Broker.byOms(OMS.SAHRA))
            .stream().filter(bourseCode -> !bourseCode.getTokens().isEmpty())
            .forEach(bourseCode -> {
                Optional<Token> randomToken = bourseCode.getRandomToken();
                randomToken.ifPresent(token -> handler.handle(bourseCode, poll(token)));
            });
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void healthCheckForBourseCodes() {
        if (isHeadlineTime()) {
            return;
        }
        bourseCodeRepository.findAllByBrokerIn(Broker.byOms(OMS.SAHRA))
            .stream().filter(bourseCode -> !bourseCode.getTokens().isEmpty())
            .forEach(this::getTime);
    }
}

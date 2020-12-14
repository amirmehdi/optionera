package com.gitlab.amirmehdi.service.sahra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class SahraRequestService implements CommandLineRunner {
    public static final String HUB = "omsclienthub";
    private final TokenRepository tokenRepository;
    private final NegotiateManager negotiateManager;
    private final RestTemplate restTemplate;
    private final RestTemplate longPollRestTemplate;
    private final TaskScheduler executor;
    private final ObjectMapper objectMapper;
    private final MessageHandler handler;
    private final TelegramMessageSender telegramMessageSender;

    private final SecurityFields securityFields = new SecurityFields();
    private final String connectUrl = "https://firouzex.ephoenix.ir/realtime/connect?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String pollUrl = "https://firouzex.ephoenix.ir/realtime/poll?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String sendUrl = "https://firouzex.ephoenix.ir/realtime/send?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";

    @Value("${application.telegram.healthCheckChat}")
    private String healthCheckChannelId;

    public SahraRequestService(TokenRepository tokenRepository, RestTemplate restTemplate, NegotiateManager negotiateManager, @Qualifier("longPollRestTemplate") RestTemplate longPollRestTemplate, TaskScheduler executor, ObjectMapper objectMapper, MessageHandler handler, TelegramMessageSender telegramMessageSender) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = restTemplate;
        this.negotiateManager = negotiateManager;
        this.longPollRestTemplate = longPollRestTemplate;
        this.executor = executor;
        this.objectMapper = objectMapper;
        this.handler = handler;
        this.telegramMessageSender = telegramMessageSender;
    }

    @Retryable(
        value = {ResourceAccessException.class},
        maxAttempts = 3,
        backoff = @Backoff(value = 3000))
    public void connectAndStart() {
        Token token = tokenRepository.findTopByBrokerOrderByCreatedAtDesc(Broker.FIROOZE_ASIA).get();
        if (ChronoUnit.HOURS.between(token.getCreatedAt().toInstant(), new Date().toInstant()) > 6) {
            try {
                token = negotiateManager.login(3);
            } catch (LoginFailedException e) {
                telegramMessageSender.sendMessage(new TelegramMessageDto(healthCheckChannelId, "sahra can't login. number of attempts reached"));
                return;
            }
        }
        this.securityFields.setToken(token.getToken());

        NegotiateResponse negotiate = negotiateManager.negotiate(token);
        this.securityFields.setConnectionToken(negotiate.getConnectionToken());
        this.securityFields.setMessageId(connect().getMessageId());
        negotiateManager.start(securityFields.getToken(), securityFields.getConnectionToken());

        FirstPollResponse firstPollResponse = firstPoll();
        this.securityFields.setMessageId(firstPollResponse.getMessageId());
        this.securityFields.setGroupToken(firstPollResponse.getGroupsToken());
        firstPollResponse.getM().forEach(handler::handle);

        securityFields.getSchedules().add(executor.scheduleWithFixedDelay(() -> handler.handle(poll()), 100));
        securityFields.getSchedules().add(executor.scheduleWithFixedDelay(this::getTime, 60000));
    }

    //"{\"H\":\"omsclienthub\",\"M\":\"GetAssetsReport\",\"A\":[],\"I\":7}"
    public ObjectNode getAssetReport() {
        SendRequest sendRequest = new SendRequest("GetAssetsReport", Collections.emptyList());
        return send(sendRequest);
    }

    // "{\"H\":\"omsclienthub\",\"M\":\"GetAcountRemainReport\",\"A\":[1,1399,9,4,1399,9,4],\"I\":6}"
    public ObjectNode getAccountRemainReport(LocalDate localDate) {
        JalaliCalendar j = new JalaliCalendar(localDate);
        SendRequest sendRequest = new SendRequest("GetAcountRemainReport"
            , Arrays.asList(1, j.getYear(), j.getMonth(), j.getDay(), j.getYear(), j.getMonth(), j.getDay()));
        return send(sendRequest);
    }

    //"{\"H\":\"omsclienthub\",\"M\":\"GetTime\",\"A\":[],\"I\":5}";
    //{"R":"19:35:42","I":"7"}
    public LocalTime getTime() {
        SendRequest sendRequest = new SendRequest("GetTime", Collections.emptyList());
        return LocalTime.parse(send(sendRequest).get("R").asText());
    }

    //    {"H":"omsclienthub","M":"CancelOrder","A":[1170000000356607],"I":6}
    public void cancelOrder(Order order) {
        SendRequest sendRequest = new SendRequest("CancelOrder", Collections.singletonList(Long.valueOf(order.getOmsId())));
        send(sendRequest);
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
        send(sendRequest);
    }

    private ConnectResponse connect() {
        ResponseEntity<ConnectResponse> connectResponse =
            restTemplate.exchange(
                URI.create(String.format(connectUrl, getEncode(securityFields.getConnectionToken()), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>(getConnectOrSendHeaders())
                , ConnectResponse.class);
        log.info("connect, response:{}", connectResponse);
        return connectResponse.getBody();
    }

    private ObjectNode send(SendRequest data) {
        if (securityFields.getGroupToken() == null)
            return null;
        data.setI(securityFields.incAndGet());
        log.info("send request {}", data);
        ResponseEntity<ObjectNode> sendResponse;
        try {
            sendResponse = restTemplate.exchange(
                URI.create(String.format(sendUrl, getEncode(securityFields.getConnectionToken()), getEncode(connectionData)))
                , HttpMethod.POST
                , new HttpEntity<>("data=" + getEncode(objectMapper.writeValueAsString(data)), getConnectOrSendHeaders())
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
                clearConnection();
                connectAndStart();
                return send(data);
            }
            throw new CodeException(errorCode, errorDesc);
        }
        return node;
    }

    public FirstPollResponse firstPoll() {
        ResponseEntity<FirstPollResponse> firstPollResponse =
            longPollRestTemplate.exchange(
                URI.create(String.format(pollUrl, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.POST
                , new HttpEntity<>("messageId=" + getEncode(securityFields.getMessageId()), getConnectOrSendHeaders())
                , FirstPollResponse.class);
        log.info("firstPoll, response:{}", firstPollResponse);
        return firstPollResponse.getBody();
    }

    public PollResponse poll() {
        if (securityFields.getGroupToken() == null)
            return null;
        try {
            ResponseEntity<PollResponse> pollResponse =
                longPollRestTemplate.exchange(
                    URI.create(String.format(pollUrl, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                    , HttpMethod.POST
                    , new HttpEntity<>("messageId=" + getEncode(securityFields.getMessageId()) + "&groupsToken=" + getEncode(securityFields.getGroupToken()), getConnectOrSendHeaders())
                    , PollResponse.class);
            log.debug("poll, response:{}", pollResponse);
            securityFields.setMessageId(pollResponse.getBody().getMessageId());
            return pollResponse.getBody();
        } catch (ResourceAccessException e) {
            log.debug("poll nochange");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                clearConnection();
                // message to telegram
            }
        }
        return null;
    }


    private LinkedMultiValueMap<String, String> getConnectOrSendHeaders() {
        String[] s = securityFields.getToken().split("__");
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
        try {
            connectAndStart();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                clearConnection();
                // message to telegram
            }
        } catch (ResourceAccessException e) {
            if (securityFields.getGroupToken() == null) {
                clearConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearConnection() {
        securityFields.clear();
        telegramMessageSender.sendMessage(new TelegramMessageDto(healthCheckChannelId, "sahra token is expired"));
    }
}

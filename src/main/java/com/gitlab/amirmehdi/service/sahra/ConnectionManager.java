package com.gitlab.amirmehdi.service.sahra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.repository.TokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Service
@Log4j2
public class ConnectionManager extends TextWebSocketHandler {
    private final TokenRepository tokenRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final NegotiateManager negotiateManager;
    private final StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
    private String connectionToken;

    private final String wsUrl = "wss://firouzex.ephoenix.ir/realtime/connect?transport=webSockets&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&tid=10";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";

    //can be hashmap for keep all sessions for other users
    private WebSocketSession session;

    public ConnectionManager(TokenRepository tokenRepository, RestTemplate restTemplate, ObjectMapper objectMapper, NegotiateManager negotiateManager) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.negotiateManager = negotiateManager;
    }

    /*@Retryable(
        value = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(value = 3000))
    public void connect() throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        Token token = tokenRepository.findTopByBrokerOrderByCreatedAtDesc(Broker.FIROOZE_ASIA).get();
        this.connectionToken = negotiateManager.negotiate(token).getConnectionToken();
        this.connectionToken = "AQAAANCMnd8BFdERjHoAwE/Cl+sBAAAA/DY7XwkOiUCggvzVTSs4gwAAAAACAAAAAAAQZgAAAAEAACAAAACcWJ6e3lRFEkRkc+kz523/ObC42fRXyt3oM8BHgTtYQAAAAAAOgAAAAAIAACAAAADDokuqv1jtgPqB1X5EP9ornhYkWxjy9oPe9S3FE4/KrzAAAACTfGvq39bzEGztM8V70Fr6C/o+hsATS98WZWpubDfwstkgi7VhKGv8NZDFkM8TJilAAAAACsNaxN9xZmeGq7EdBjojMfPPNT6mx/3rNv4x6QZ2nQucTx4q96o2HKdUJo61zxnETrKU2GRedZgCjY7rqqP8Jw==";

        WebSocketHttpHeaders headers = getWebSocketHttpHeaders(token);
        this.session = webSocketClient.doHandshake(this, headers, URI.create("wss://firouzex.ephoenix.ir/realtime/connect?transport=webSockets&clientProtocol=1.5&token=&connectionToken=AQAAANCMnd8BFdERjHoAwE%2FCl%2BsBAAAA%2FDY7XwkOiUCggvzVTSs4gwAAAAACAAAAAAAQZgAAAAEAACAAAACcWJ6e3lRFEkRkc%2Bkz523%2FObC42fRXyt3oM8BHgTtYQAAAAAAOgAAAAAIAACAAAADDokuqv1jtgPqB1X5EP9ornhYkWxjy9oPe9S3FE4%2FKrzAAAACTfGvq39bzEGztM8V70Fr6C%2Fo%2BhsATS98WZWpubDfwstkgi7VhKGv8NZDFkM8TJilAAAAACsNaxN9xZmeGq7EdBjojMfPPNT6mx%2F3rNv4x6QZ2nQucTx4q96o2HKdUJo61zxnETrKU2GRedZgCjY7rqqP8Jw%3D%3D&connectionData=%5B%7B%22name%22%3A%22omsclienthub%22%7D%5D&tid=2")).get();
//        this.session = webSocketClient.doHandshake(this, headers, URI.create(String.format(wsUrl, getEncode(connectionToken), getEncode(connectionData)))).get();

        negotiateManager.start(token.getToken(), connectionToken);
    }*/

    public void sendMessage(Object object) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(object)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished, session:{}", session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage({}, {})", session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("handlePongMessage({}, {})", session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError({}, {})", session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed({}, {})", session, status);
    }

    private WebSocketHttpHeaders getWebSocketHttpHeaders(Token token) {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.setAccessControlRequestMethod(HttpMethod.GET);
        String[] s = token.getToken().split("__");
        headers.add("Pragma", "no-cache");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Origin", "https://firouzex.ephoenix.ir");
        headers.add("Sec-WebSocket-Key", "Y/fjjtpQnNuQq13sN5BhLQ==");
        headers.add("Host", "firouzex.ephoenix.ir");
        headers.add("Upgrade", "websocket");
        headers.add("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits");
        headers.add("Cache-Control", "no-cache");
        headers.add("Connection", "Upgrade");
        headers.add("Sec-WebSocket-Version", "13");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.add("Cookie", "cookiesession1=06D9A6D3W8HKKJUOS2MCPMEAIUXYE883; PHXsessions=1f77002f6519437b9059fbdea128e40f; PHX=AQAAANCMnd8BFdERjHoAwE_Cl-sBAAAA_DY7XwkOiUCggvzVTSs4gwAAAAACAAAAAAAQZgAAAAEAACAAAACw14mO32495DZ9dqXl50j3VAWEy3Z9bemOlGMtTpt-2QAAAAAOgAAAAAIAACAAAACRCtfBOPiZyNMInotHETGfYlK4lsqj4JUwOL_JJw_GwnAAAAB8nvn6SMP1fGJuuZVnJ7wgZww2HjCID0J3lBpmh1NbFwbLgxrZf8G0m_0kYo9-Ty_m2m32UI2SiYY-SlZjS4PRNmWDLPbYc4cUKsIjalcGmSnMyGNnODrnpF3I-2c_f0m0EO3qWy0KuqOeuQqavMtkQAAAAPBhRZKLKyNdjOpj2h7hLPBw7ryXoO-dPntIYF1LLvyWBaiT6HbkNUR_JoHM-OABXKSg6kKou5StaWKUgwysSr0");
//        headers.add("User-Agent", s[0]);
//        headers.add("Cookie", s[1]);
        return headers;
    }
}

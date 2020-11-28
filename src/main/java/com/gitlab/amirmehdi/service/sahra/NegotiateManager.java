package com.gitlab.amirmehdi.service.sahra;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.service.dto.sahra.NegotiateResponse;
import com.gitlab.amirmehdi.service.dto.sahra.StartSocketResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class NegotiateManager {
    private final RestTemplate restTemplate;
    private final String negotiateUrl = "https://firouzex.ephoenix.ir/realtime/negotiate?clientProtocol=1.5&token=&connectionData={connectionData}&_={nano}";
    private final String startUrl = "https://firouzex.ephoenix.ir/realtime/start?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";

    public NegotiateManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NegotiateResponse negotiate(Token token) {
        ResponseEntity<NegotiateResponse> negotiateResponse =
            restTemplate.exchange(negotiateUrl
                , HttpMethod.GET
                , new HttpEntity<>(getNegotiateHeaders(token))
                , NegotiateResponse.class
                , connectionData, System.currentTimeMillis());
        log.info("negotiate, response:{}", negotiateResponse);
        return negotiateResponse.getBody();
    }

    public void start(String token,String connectionToken) {
        ResponseEntity<StartSocketResponse> startResponse =
            restTemplate.exchange(
                URI.create(String.format(startUrl, getEncode(connectionToken), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.GET
                , new HttpEntity<>(getStartHeaders(token))
                , StartSocketResponse.class);
        log.info("start, response:{}", startResponse);
        if (!startResponse.getBody().getResponse().equals("started")){
            throw new RuntimeException("not start");
        }
    }

    private LinkedMultiValueMap<String, String> getNegotiateHeaders(Token token) {
        String[] s = token.getToken().split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "text/plain, */*; q=0.01");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Sec-Fetch-Site", "same-origin");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://firouzex.ephoenix.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }


    private LinkedMultiValueMap<String, String> getStartHeaders(String token) {
        String[] s = token.split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "text/plain, */*; q=0.01");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Sec-Fetch-Site", "same-origin");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://firouzex.ephoenix.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }
}

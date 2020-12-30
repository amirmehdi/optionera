package com.gitlab.amirmehdi.service.asa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.service.dto.asa.InstrumentInfoResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class AsaRLCConsumer {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String instrumentInfoUrl = "https://online.agah.com/Watch/GetInstrumentInfo?isin=%s";

    public AsaRLCConsumer(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public CompletableFuture<InstrumentInfoResponse> getInstrumentInfo(String isin, String token) {
        StopWatch watch = new StopWatch("getInstrumentInfo");
        watch.start();
        InstrumentInfoResponse response = null;
        try {
            response = objectMapper.readValue(restTemplate.exchange(
                String.format(instrumentInfoUrl, isin)
                , HttpMethod.GET
                , new HttpEntity<>(getHeader(token))
                , String.class)
                .getBody(), InstrumentInfoResponse.class);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        watch.stop();
        log.debug(watch.shortSummary());
        return CompletableFuture.completedFuture(response);
    }


    private LinkedMultiValueMap<String, String> getHeader(String token) {
        String[] s = token.split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        headers.add("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }

}

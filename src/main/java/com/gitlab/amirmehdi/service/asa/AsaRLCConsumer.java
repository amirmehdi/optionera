package com.gitlab.amirmehdi.service.asa;

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

    private final String instrumentInfoUrl = "https://online.agah.com/Watch/GetInstrumentInfo?isin=%s";

    public AsaRLCConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<InstrumentInfoResponse> getInstrumentInfo(String isin, String token) {
        StopWatch watch = new StopWatch("getInstrumentInfo");
        watch.start();
        InstrumentInfoResponse response = restTemplate.exchange(
            String.format(instrumentInfoUrl, isin)
            , HttpMethod.GET
            , new HttpEntity<>(getHeader(token))
            , InstrumentInfoResponse.class)
            .getBody();
        watch.stop();
        log.debug(watch.shortSummary());
        return CompletableFuture.completedFuture(response);
    }


    private LinkedMultiValueMap<String, String> getHeader(String token) {
        String[] s = token.split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }

}

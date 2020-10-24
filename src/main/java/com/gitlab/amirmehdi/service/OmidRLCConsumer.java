package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.Instrument;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class OmidRLCConsumer {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public OmidRLCConsumer(ObjectMapper objectMapper, @Qualifier("trustedRestTemplate") RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Timed(value = "omid.rlc")
    public List<Instrument> searchInstrument(String name) {
        log.debug("searchInstrument for name :{}", name);
        return restTemplate.exchange(
            applicationProperties.getOaBaseUrl() + "/core/search/" + name,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Instrument>>() {
            }).getBody();
    }

    @Timed(value = "omid.rlc")
    public List<Instrument> getInstruments(List<String> isins) throws JsonProcessingException {
        log.debug("getInstruments for isins :{}", isins);
        ResponseEntity<List<Instrument>> response = restTemplate.exchange(
            applicationProperties.getOaBaseUrl() + "/core/instrument-bulk",
            HttpMethod.POST,
            getRequestBodyForBulkRequest(isins),
            new ParameterizedTypeReference<List<Instrument>>() {
            });
        return response.getBody();
    }

    @Async
    @Timed(value = "omid.rlc")
    public CompletableFuture<List<BidAsk>> getBulkBidAsk(List<String> isins) throws JsonProcessingException {
        log.debug("getBulkBidAsk for isins :{}", isins);
        ResponseEntity<List<BidAsk>> response = restTemplate.exchange(
            applicationProperties.getOaBaseUrl() + "/core/bidask-bulk",
            HttpMethod.POST,
            getRequestBodyForBulkRequest(isins),
            new ParameterizedTypeReference<List<BidAsk>>() {
            });
        return CompletableFuture.completedFuture(response.getBody());
    }

    @Async
    @Timed(value = "omid.rlc")
    public CompletableFuture<List<StockWatch>> getBulkStockWatch(List<String> isins) throws JsonProcessingException {
        log.debug("getBulkStockWatch for isins :{}", isins);
        ResponseEntity<List<StockWatch>> response = restTemplate.exchange(
            applicationProperties.getOaBaseUrl() + "/core/stockwatch-bulk",
            HttpMethod.POST,
            getRequestBodyForBulkRequest(isins),
            new ParameterizedTypeReference<List<StockWatch>>() {
            });
        return CompletableFuture.completedFuture(response.getBody());
    }


    private HttpEntity<String> getRequestBodyForBulkRequest(List<String> isins) throws JsonProcessingException {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("isins", isins);
        return new HttpEntity<>(objectMapper.writeValueAsString(hashMap), headers);
    }

}

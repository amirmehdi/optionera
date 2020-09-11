package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
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
public class OmidRLCConsumer {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public OmidRLCConsumer(ObjectMapper objectMapper, RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Async
    public CompletableFuture<List<BidAsk>> getBulkBidAsk(List<String> isins) throws JsonProcessingException {
        ResponseEntity<List<BidAsk>> response = restTemplate.exchange(
            applicationProperties.getOaBaseUrl() + "/core/bidask-bulk",
            HttpMethod.POST,
            getRequestBodyForBulkRequest(isins),
            new ParameterizedTypeReference<List<BidAsk>>() {
            });
        return CompletableFuture.completedFuture(response.getBody());
    }

    @Async
    public CompletableFuture<List<StockWatch>> getBulkStockWatch(List<String> isins) throws JsonProcessingException {
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

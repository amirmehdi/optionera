package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.dto.tadbir.SendOrderRequest;
import com.gitlab.amirmehdi.service.dto.tadbir.SendOrderResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TadbirService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final TokenRepository tokenRepository;

    public TadbirService(@Qualifier("trustedRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper, TokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.tokenRepository = tokenRepository;
    }

    public String sendOrder(Order order) {
        SendOrderRequest sendOrderRequest = SendOrderRequest.builder()
            .isSymbolCautionAgreement(false)
            .cautionAgreementSelected(false)
            .isSymbolSepahAgreement(false)
            .sepahAgreementSelected(false)
            .orderCount(order.getQuantity())
            .orderPrice(order.getPrice())
            .financialProviderId(1)
            .minimumQuantity(0)
            .maxShow(0)
            .orderId(0)
            .isin(order.getIsin())
            .orderSide(order.getSide().equals(Side.BUY) ? 65 : 86)
            .orderValidity(order.getValidity().equals(Validity.DAY) ? 74 : 69)
            .orderValiditydate(null)
            .shortSellIsEnabled(false)
            .shortSellIncentivePercent(0)
            .build();

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "BasicAuthentication " + tokenRepository.findTopByBrokerOrderByCreatedAtDesc(order.getBroker()).orElseThrow(RuntimeException::new));
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        headers.add("sec-ch-ua", "\"\\Not;A\"Brand\";v=\"99\", \"Google Chrome\";v=\"85\", \"Chromium\";v=\"85\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        headers.add("Accept", "*/*");
        headers.add("Origin", "https://silver.refahbroker.ir");
        headers.add("Sec-Fetch-Site", "same-site");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://silver.refahbroker.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        try {
            ResponseEntity<SendOrderResponse> response = restTemplate.exchange("https://api.refahbroker.ir/Web/V1/Order/Post", HttpMethod.POST, new HttpEntity<>(objectMapper.writeValueAsString(sendOrderRequest), headers), SendOrderResponse.class);
            log.info("order:{} response:{}", order, response);
            return response.getBody().getMessageDesc();
        } catch (JsonProcessingException e) {
            log.error("order:{} response:{}", order, "JsonProcessingException", e);
            return "parsing error";
        } catch (HttpStatusCodeException e) {
            log.error("order:{} response:{}", order, e.getResponseBodyAsString(), e);
            return e.getResponseBodyAsString();
        }
    }

}

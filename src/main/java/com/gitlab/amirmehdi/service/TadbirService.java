package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.dto.tadbir.*;
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

        try {
            ResponseEntity<SendOrderResponse> response =
                restTemplate.exchange("https://api.refahbroker.ir/Web/V1/Order/Post"
                    , HttpMethod.POST
                    , new HttpEntity<>(objectMapper.writeValueAsString(sendOrderRequest), getHeaders(order.getBroker()))
                    , SendOrderResponse.class);
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

    public UserOpenInterestResponse getUserOpenInterest() {
        try {
            ResponseEntity<UserOpenInterestResponse> response =
                restTemplate.exchange("https://silver.refahbroker.ir/Customer/GetCustomerSummaryList"
                    , HttpMethod.POST
                    , new HttpEntity<>("{}", getHeaders2(Broker.REFAH))
                    , UserOpenInterestResponse.class);
            log.debug("getUserOpenInterest response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getUserOpenInterest response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public DailyPortfolioResponse getDailyPortfolio() {
        try {
            ResponseEntity<DailyPortfolioResponse> response =
                restTemplate.exchange("https://api.refahbroker.ir/Web/V1/DailyPortfolio/Get/DailyPortfolio?symbolIsin="
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(Broker.REFAH))
                    , DailyPortfolioResponse.class);
            log.debug("getDailyPortfolio response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getDailyPortfolio response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public RemainResponse getRemain() {
        try {
            ResponseEntity<RemainResponse> response =
                restTemplate.exchange("https://api.refahbroker.ir/Web/V1/Accounting/Remain"
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(Broker.REFAH))
                    , RemainResponse.class);
            log.debug("getRemain response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getRemain response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public OpenOrderResponse getOpenOrders() {
        try {
            ResponseEntity<OpenOrderResponse> response =
                restTemplate.exchange("https://api.refahbroker.ir/Web/V1/Order/GetOpenOrder/OpenOrder"
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(Broker.REFAH))
                    , OpenOrderResponse.class);
            log.debug("getOpenOrders response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getOpenOrders response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public OpenOrderResponse getTodayOrder() {
        try {
            ResponseEntity<OpenOrderResponse> response =
                restTemplate.exchange("https://api.refahbroker.ir/Web/V1/Order/GetTodayOrders/Customer/GetCustomerTodayOrders"
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(Broker.REFAH))
                    , OpenOrderResponse.class);
            log.debug("getTodayOrder response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getTodayOrder response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    protected LinkedMultiValueMap<String, String> getHeaders(Broker broker) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String[] token = tokenRepository.findTopByBrokerOrderByIdDesc(broker)
            .orElseThrow(RuntimeException::new)
            .getToken().split("__");
        addCommonHeader(headers, token[1]);
        headers.add("Authorization", "BasicAuthentication " + token[0]);
        headers.add("Referer", "https://silver.refahbroker.ir/");
        return headers;
    }

    protected LinkedMultiValueMap<String, String> getHeaders2(Broker broker) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String[] token = tokenRepository.findTopByBrokerOrderByIdDesc(broker)
            .orElseThrow(RuntimeException::new)
            .getToken().split("__");
        addCommonHeader(headers, token[1]);
        headers.add("Referer", "https://silver.refahbroker.ir/Home/Default/page-1");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Cookie", token[2]);
        return headers;
    }

    private void addCommonHeader(LinkedMultiValueMap<String, String> headers, String userAgent) {
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        headers.add("sec-ch-ua", "\"\\Not;A\"Brand\";v=\"99\", \"Google Chrome\";v=\"85\", \"Chromium\";v=\"85\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("User-Agent", userAgent);
        headers.add("Accept", "*/*");
        headers.add("Origin", "https://silver.refahbroker.ir");
        headers.add("Sec-Fetch-Site", "same-site");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
    }

}

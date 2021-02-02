package com.gitlab.amirmehdi.service.tadbir;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
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

    public TadbirService(@Qualifier("trustedRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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
                restTemplate.exchange(order.getBourseCode().getBroker().apiUrl + "/Order/Post"
                    , HttpMethod.POST
                    , new HttpEntity<>(objectMapper.writeValueAsString(sendOrderRequest), getHeaders(order.getBourseCode()))
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

    public UserOpenInterestResponse getUserOpenInterest(BourseCode bourseCode) {
        try {
            ResponseEntity<UserOpenInterestResponse> response =
                restTemplate.exchange(bourseCode.getBroker().url + "/Customer/GetCustomerSummaryList"
                    , HttpMethod.POST
                    , new HttpEntity<>("{}", getHeaders2(bourseCode))
                    , UserOpenInterestResponse.class);
            log.debug("getUserOpenInterest response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getUserOpenInterest response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public DailyPortfolioResponse getDailyPortfolio(BourseCode bourseCode) {
        try {
            ResponseEntity<DailyPortfolioResponse> response =
                restTemplate.exchange(String.format("%s/DailyPortfolio/Get/DailyPortfolio?symbolIsin=", bourseCode.getBroker().apiUrl)
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(bourseCode))
                    , DailyPortfolioResponse.class);
            log.debug("getDailyPortfolio response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getDailyPortfolio response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public RemainResponse getRemain(BourseCode bourseCode) {
        try {
            ResponseEntity<RemainResponse> response =
                restTemplate.exchange(String.format("%s/Accounting/Remain", bourseCode.getBroker().apiUrl)
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(bourseCode))
                    , RemainResponse.class);
            log.debug("getRemain response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getRemain response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public OpenOrderResponse getOpenOrders(BourseCode bourseCode) {
        try {
            ResponseEntity<OpenOrderResponse> response =
                restTemplate.exchange(String.format("%s/Order/GetOpenOrder/OpenOrder", bourseCode.getBroker().apiUrl)
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(bourseCode))
                    , OpenOrderResponse.class);
            log.debug("getOpenOrders response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getOpenOrders response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    public OpenOrderResponse getTodayOrder(BourseCode bourseCode) {
        try {
            ResponseEntity<OpenOrderResponse> response =
                restTemplate.exchange(String.format("%s/Order/GetTodayOrders/Customer/GetCustomerTodayOrders", bourseCode.getBroker().apiUrl)
                    , HttpMethod.GET
                    , new HttpEntity<>(getHeaders(bourseCode))
                    , OpenOrderResponse.class);
            log.debug("getTodayOrder response {}", response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("getTodayOrder response:{}", e.getResponseBodyAsString(), e);
            return null;
        }
    }

    protected LinkedMultiValueMap<String, String> getHeaders(BourseCode bourseCode) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String[] tokenStrings = bourseCode.getRandomToken().get().getToken().split("__");
        addCommonHeader(headers, tokenStrings[1]);
        headers.add("Authorization", "BasicAuthentication " + tokenStrings[0]);
        headers.add("Referer", bourseCode.getBroker().url);
        headers.add("Origin", bourseCode.getBroker().url);
        return headers;
    }

    protected LinkedMultiValueMap<String, String> getHeaders2(BourseCode bourseCode) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String[] tokenStrings = bourseCode.getRandomToken().get().getToken().split("__");
        addCommonHeader(headers, tokenStrings[1]);
        headers.add("Referer", bourseCode.getBroker().url + "/Home/Default/page-1");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Cookie", tokenStrings[2]);
        headers.add("Origin", bourseCode.getBroker().url);
        return headers;
    }

    private void addCommonHeader(LinkedMultiValueMap<String, String> headers, String userAgent) {
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        headers.add("sec-ch-ua", "\"\\Not;A\"Brand\";v=\"99\", \"Google Chrome\";v=\"85\", \"Chromium\";v=\"85\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("User-Agent", userAgent);
        headers.add("Accept", "*/*");
        headers.add("Sec-Fetch-Site", "same-site");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
    }

}

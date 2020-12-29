package com.gitlab.amirmehdi.service.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.service.dto.tadbirrlc.IndInstTradeResponse;
import com.gitlab.amirmehdi.service.dto.tadbirrlc.LightSymbolInfoAndQueue;
import com.gitlab.amirmehdi.service.dto.tadbirrlc.SymbolInfoRequestBody;
import com.gitlab.amirmehdi.util.UrlEncodingUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class TadbirRlcConsumer {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String stockInfoUrl = "https://core.tadbirrlc.com//StockFutureInfoHandler.ashx?%s";
    private final String clientsInfoUrl = "https://core.tadbirrlc.com//AlmasDataHandler.ashx?%s";

    //index
    //https://core.tadbirrlc.com//AlmasDataHandler.ashx?%7B%22Type%22:%22IndexInfoLast%22,%22la%22:%22fa%22,%22isin%22:%5B%22IRX6XTPI0006%22,%22IRXZXOCI0006%22,%22IRX6XSLC0006%22,%22IRX6XS300006%22,%22IRXYXTPI0026%22%5D%7D&jsoncallback=
    //{"Type":"IndexInfoLast","la":"fa","isin":["IRX6XTPI0006","IRXZXOCI0006","IRX6XSLC0006","IRX6XS300006","IRXYXTPI0026"]}

    //intraday candle
    //https://core.tadbirrlc.com//StocksHandler.ashx?%7B%22Type%22:%22compactintradaychart%22,%22la%22:%22Fa%22,%22isin%22:%22IRO1SIPA0001%22%7D&jsoncallback=
    //{"Type":"compactintradaychart","la":"Fa","isin":"IRO1SIPA0001"}

    // clientInfo
    //https://core.tadbirrlc.com//AlmasDataHandler.ashx?%7B%22Type%22:%22getIndInstTrade%22,%22la%22:%22Fa%22,%22nscCode%22:%22IRO1SIPA0001%22,%22ZeroIfMarketIsCloesed%22:true%7D&jsoncallback=
    //{"Type":"getIndInstTrade","la":"Fa","nscCode":"IRO1SIPA0001","ZeroIfMarketIsCloesed":true}:

    //fundumental
    //https://core.tadbirrlc.com//AlmasDataHandler.ashx?%7B%22Type%22:%22getSymbolFundamentalInfo%22,%22la%22:%22Fa%22,%22nscCode%22:%22IRO1SIPA0001%22%7D&jsoncallback=
    //{"Type":"getSymbolFundamentalInfo","la":"Fa","nscCode":"IRO1SIPA0001"}

    //etf
    //https://core.tadbirrlc.com//StockFutureInfoHandler.ashx?%7B%22Type%22:%22etf%22,%22la%22:%22fa%22,%22nscCode%22:%22IRT1SARV0001%22%7D&jsoncallback=
    //{"Type":"etf","la":"fa","nscCode":"IRT1SARV0001"}
    public TadbirRlcConsumer(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<LightSymbolInfoAndQueue> stockFutureInfoHandler(String isin) {
        StopWatch watch = new StopWatch("stockFutureInfoHandler");
        watch.start();
        SymbolInfoRequestBody body = new SymbolInfoRequestBody();
        body.setLa("fa");
        body.setType("getLightSymbolInfoAndQueue");
        body.setNscCode(isin);
        try {
            LightSymbolInfoAndQueue response = objectMapper.readValue(restTemplate.exchange(
                URI.create(String.format(stockInfoUrl, UrlEncodingUtil.getEncode(objectMapper.writeValueAsString(body))))
                , HttpMethod.GET
                , null
                , String.class)
                .getBody(), LightSymbolInfoAndQueue.class);
            watch.stop();
            log.debug(watch.shortSummary());
            return CompletableFuture.completedFuture(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IndInstTradeResponse clientsInfoHandler(String isin) {
        log.debug("clientsInfoHandler for isin {}", isin);
        SymbolInfoRequestBody body = new SymbolInfoRequestBody();
        body.setLa("fa");
        body.setType("getIndInstTrade");
        body.setNscCode(isin);
        try {
            IndInstTradeResponse response = objectMapper.readValue(restTemplate.exchange(
                URI.create(String.format(clientsInfoUrl, UrlEncodingUtil.getEncode(objectMapper.writeValueAsString(body))))
                , HttpMethod.GET
                , null
                , String.class)
                .getBody(), IndInstTradeResponse.class);
            return response;
//            return CompletableFuture.completedFuture(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.MessageEventEnum;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Market {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Market(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public StockWatch getStockWatch(String isin) {
        try {
            return objectMapper.readValue((String) (redisTemplate.opsForHash().get(isin, MessageEventEnum.STOCKWATCH.toString())), StockWatch.class);
        } catch (Exception e) {
            return null;
        }
    }

    public BidAsk getBidAsk(String isin) {
        try {
            return objectMapper.readValue((String) (redisTemplate.opsForHash().get(isin, MessageEventEnum.BIDASK.toString())), BidAsk.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveBidAsk(BidAsk bidAsk) {
        try {
            redisTemplate.opsForHash().put(bidAsk.getIsin(), MessageEventEnum.BIDASK.toString(), objectMapper.writeValueAsString(bidAsk));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void saveStockWatch(StockWatch stockWatch) {
        try {
            redisTemplate.opsForHash().put(stockWatch.getIsin(), MessageEventEnum.STOCKWATCH.toString(), objectMapper.writeValueAsString(stockWatch));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void saveAllBidAsk(List<BidAsk> bidAsks){
        for (BidAsk bidAsk : bidAsks) {
            saveBidAsk(bidAsk);
        }
    }
    public void saveAllStockWatch(List<StockWatch> stockWatches){
        for (StockWatch stockWatch : stockWatches) {
            saveStockWatch(stockWatch);
        }
    }
}

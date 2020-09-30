package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.MessageEventEnum;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
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
            if (redisTemplate.opsForHash().hasKey(isin, MessageEventEnum.BIDASK.toString()).equals(true)) {
                return objectMapper.readValue((String) (redisTemplate.opsForHash().get(isin, MessageEventEnum.BIDASK.toString())), BidAsk.class);
            }else {
                log.warn("BidAsk for ISIN: " + isin + " is not available. Returning default BidAsk...");
                return defaultBidAsk(isin);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private BidAsk defaultBidAsk(String isin) {
        BidAsk bidAsk = new BidAsk();
        bidAsk.setIsin(isin);
        bidAsk.setDateTime(new Date());
        ArrayList<BidAskItem> items = new ArrayList<>(Arrays.asList(
            new BidAskItem(),
            new BidAskItem(),
            new BidAskItem(),
            new BidAskItem(),
            new BidAskItem()
        ));
        bidAsk.setItems(items);
        return bidAsk;
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

    public void saveAllBidAsk(List<BidAsk> bidAsks) {
        for (BidAsk bidAsk : bidAsks) {
            saveBidAsk(bidAsk);
        }
    }

    public void saveAllStockWatch(List<StockWatch> stockWatches) {
        for (StockWatch stockWatch : stockWatches) {
            saveStockWatch(stockWatch);
        }
    }
}

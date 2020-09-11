package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class OptionStatService {
    //key : instrument.id + strike + date
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OptionRepository optionRepository;
    private final String key = "OPTION_STAT";

    public OptionStatService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper, OptionRepository optionRepository) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.optionRepository = optionRepository;
    }

    public OptionStats findById(String isin, int strike, LocalDate localDate) {
        try {
            return objectMapper.readValue((String) redisTemplate.opsForHash().get(key, getId(isin, strike, localDate)), OptionStats.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<OptionStats> findAll() {
        try {
            return objectMapper.readValue(redisTemplate.opsForHash().entries(key).values().toString(), new TypeReference<List<OptionStats>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void save(OptionStats optionStats) {
        try {
            redisTemplate.opsForHash()
                .put(key,
                    getId(
                        optionStats.getOption().getInstrument().getIsin()
                        , optionStats.getOption().getStrikePrice()
                        , optionStats.getOption().getExpDate())
                    , objectMapper.writeValueAsString(optionStats)
                );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<OptionStats> stats) {
        for (OptionStats stat : stats) {
            save(stat);
        }
    }

    private String getId(String isin, int strike, LocalDate localDate) {
        return isin +
            "-" +
            strike +
            "-" +
            localDate.toString();
    }

    public void saveAllBidAsk(List<BidAsk> bidAsks) {
        for (BidAsk bidAsk : bidAsks) {
            Option option = optionRepository.findByCallIsinOrPutIsin(bidAsk.getIsin()).get();
            OptionStats optionStats = findById(option.getInstrument().getIsin(), option.getStrikePrice(), option.getExpDate());
            if (bidAsk.getIsin().equals(option.getCallIsin())) {
                optionStats.setCallBidAsk(getBestBidAsk(bidAsk));
            } else {
                optionStats.setPutBidAsk(getBestBidAsk(bidAsk));
            }
            save(optionStats);
        }
    }

    public void saveAllStockWatch(List<StockWatch> stockWatches) {
        for (StockWatch stockWatch : stockWatches) {
            Option option = optionRepository.findByCallIsinOrPutIsin(stockWatch.getIsin()).get();
            OptionStats optionStats = findById(option.getInstrument().getIsin(), option.getStrikePrice(), option.getExpDate());
            if (stockWatch.getIsin().equals(option.getCallIsin())) {
                optionStats.setCallStockWatch(getOptionStockWatch(optionStats.getCallStockWatch(), stockWatch));
            } else {
                optionStats.setPutStockWatch(getOptionStockWatch(optionStats.getPutStockWatch(), stockWatch));
            }
            save(optionStats);
        }
    }

    private OptionStockWatch getOptionStockWatch(OptionStockWatch optionStockWatch, StockWatch stockWatch) {
        optionStockWatch.setClose(stockWatch.getClosing());
        optionStockWatch.setLast(stockWatch.getLast());
        optionStockWatch.setTradeCount(stockWatch.getTradesCount());
        optionStockWatch.setTradeVolume(stockWatch.getTradeVolume());
        optionStockWatch.setTradeValue(stockWatch.getTradeVolume());
        return optionStockWatch;
    }

    private BestBidAsk getBestBidAsk(BidAsk bidAsk) {
        BidAskItem bestBidAsk = bidAsk.getItems().get(0);
        return BestBidAsk
            .builder()
            .askPrice(bestBidAsk.getAskPrice())
            .askVolume(bestBidAsk.getAskQuantity())
            .bidPrice(bestBidAsk.getBidPrice())
            .bidVolume(bestBidAsk.getBidQuantity())
            .build();
    }
}

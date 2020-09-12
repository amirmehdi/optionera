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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OptionStatService {
    //key : instrument.id + strike + date
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OptionRepository optionRepository;
    private final String key = "OPTION_STAT";
    public static final double RISK_FREE = 0.35;

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
            Optional<Option> optional = optionRepository.findByCallIsinOrPutIsin(bidAsk.getIsin());
            if (!optional.isPresent()) {
                log.error("option:{} not exist in db", bidAsk.getIsin());
                return;
            }
            Option option = optional.get();
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
            Optional<Option> optional = optionRepository.findByCallIsinOrPutIsin(stockWatch.getIsin());
            if (!optional.isPresent()) {
                log.error("option:{} not exist in db", stockWatch.getIsin());
                return;
            }
            Option option = optional.get();
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

    public Page<OptionStats> findAll(Pageable pageable) {
        Page<Option> options = optionRepository.findAll(pageable);
        List<Object> objects = redisTemplate
            .opsForHash()
            .multiGet(key
                , options.get()
                    .map(option -> getId(option.getInstrument().getIsin(), option.getStrikePrice(), option.getExpDate()))
                    .collect(Collectors.toList()));
        List<OptionStats> optionStats;
        try {
            optionStats = objectMapper.readValue(objects.toString(), new TypeReference<List<OptionStats>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            optionStats = Collections.emptyList();
        }
        return new PageImpl<>(optionStats);
    }
}

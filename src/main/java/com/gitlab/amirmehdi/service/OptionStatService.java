package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.OptionStats;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OptionStatService {
    //key : instrument.id + strike + date
    private final RedisTemplate<String, OptionStats> redisTemplate;

    public OptionStatService(RedisTemplate<String, OptionStats> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public OptionStats findById(String isin, int strike, LocalDate localDate){
        return redisTemplate.opsForValue().get(getId(isin, strike, localDate));
    }

    public void save(OptionStats optionStats){
        redisTemplate.opsForValue()
            .set(
                getId(
                    optionStats.getOption().getInstrument().getIsin()
                    ,optionStats.getOption().getStrikePrice()
                    ,optionStats.getOption().getExpDate())
                ,optionStats
            );
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
}

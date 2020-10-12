package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UnusualPrices1Strategy extends Strategy {
    private final Map<Long, Float> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(5, TimeUnit.MINUTES)
        .build();

    protected UnusualPrices1Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService) {
        super(optionRepository, optionStatsService);
    }

    @Override
    public List<TelegramMessageDto> getSignals() {
        return optionRepository.findAllByCallBreakEvenIsLessThanEqual(-10)
            .stream()
            .filter(option -> !cachedIsin.containsKey(option.getId()) || cachedIsin.get(option.getId()) > option.getCallBreakEven())
            .peek(option -> cachedIsin.put(option.getId(), option.getCallBreakEven()))
            .map(option -> getTelegramMessageDto(getMessageTemplate(optionStatsService.findOne(option), "قیمت های غیرمعمول۱", "متوسط")))
            .collect(Collectors.toList());
    }

    @Override
    public String getCron() {
        return "*/10 * 9-12 * * *";
    }
}

package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.BourseCodeService;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UnusualPrices2Strategy extends Strategy {
    private final Map<Long, Float> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(30, TimeUnit.MINUTES)
        .build();

    protected UnusualPrices2Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market, BourseCodeService bourseCodeService) {
        super(optionRepository, optionStatsService, market, bourseCodeService);
    }

    @Override
    public StrategyResponse getSignals() {
        return StrategyResponse.builder()
            .callSignals(optionRepository.findAllByCallAskToBSLessThanEqualAndExpDateGreaterThanEqual(-15, LocalDate.now().plusDays(30))
                .stream()
                .filter(option -> !cachedIsin.containsKey(option.getId()) || cachedIsin.get(option.getId()) - 3 > option.getCallAskToBS())
                .peek(option -> cachedIsin.put(option.getId(), option.getCallAskToBS()))
                .map(option -> getSignal(option.getCallIsin()))
                .collect(Collectors.toList()))
            .publicChatId(optionEraChatId)
            .sendOrderType(StrategyResponse.SendOrderType.NONE)
            .build();
    }

    @Override
    public String getCron() {
        return "*/10 * 9-12 * * *";
    }

    @Override
    protected String getStrategyDesc() {
        return "قیمت_پایین قیمت اختیار بسیار کمتر از قیمت تئوری است.";
    }

    @Override
    protected String getStrategyRisk() {
        return "متوسط";
    }
}

package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class OptionWithPriceLowerThanBSStrategy extends Strategy {
    protected OptionWithPriceLowerThanBSStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market) {
        super(optionRepository, optionStatsService, market);
    }

    @Override
    public StrategyResponse getSignals() {
        Option option = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now(), PageRequest.of(0, 1, Sort.by(Sort.Order.asc("callAskToBS")))).getContent().get(0);
        if (option.getCallAskToBS() > 0) {
            return null;
        }
        return StrategyResponse.builder()
            .callSignals(Collections.singletonList(getSignal(option.getCallIsin())))
            .publicChatId(optionEraChatId)
            .sendOrderType(StrategyResponse.SendOrderType.NONE)
            .build();
    }

    @Override
    public String getCron() {
        return "0 0,29 9-12 * * *";
    }

    @Override
    protected String getStrategyDesc() {
        return "قیمت_پایین قیمت اختیار کمتر از قیمت تئوری است.";
    }

    @Override
    protected String getStrategyRisk() {
        return "متوسط";
    }
}

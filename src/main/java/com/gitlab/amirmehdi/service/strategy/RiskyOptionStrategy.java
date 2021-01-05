package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.BourseCodeService;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RiskyOptionStrategy extends Strategy {
    protected RiskyOptionStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market, BourseCodeService bourseCodeService) {
        super(optionRepository, optionStatsService, market, bourseCodeService);
    }

    @Override
    public StrategyResponse getSignals() {
        List<Option> options = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now().plusDays(30));
        Map<String, Double> values = options.stream()
            .collect(Collectors.toMap(Option::getCallIsin, option -> getRiskyParam(option.getCallLeverage(), option.getCallAskToBS(), option.getCallBreakEven())));

        return StrategyResponse.builder()
            .callSignals(values
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(callIsin -> getSignal(callIsin.getKey()))
                .collect(Collectors.toList()))
            .publicChatId(optionEraChatId)
            .sendOrderType(StrategyResponse.SendOrderType.NONE)
            .build();
    }

    private Double getRiskyParam(double callLeverage, double callAskToBS, double callBreakEven) {
        return callLeverage / Math.pow((1 + callAskToBS / 100), 3) / Math.pow((1 + callBreakEven / 100), 2);
    }

    @Override
    public String getCron() {
        return "0 0,15,28,45 9-12 * * *";
    }

    @Override
    protected String getStrategyDesc() {
        return "اهرم_خوب درصورتی که دارایی پایه ارزنده است، اختیار بالا می تواند مورد بررسی قرار بگیرد";
    }

    @Override
    protected String getStrategyRisk() {
        return "زیاد";
    }
}

package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.service.errors.OptionStatsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RiskyOptionStrategy extends Strategy {
    protected RiskyOptionStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService) {
        super(optionRepository, optionStatsService);
    }

    @Override
    public List<TelegramMessageDto> getSignals() {
        List<Option> options = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now().plusDays(30));
        Map<Long, Double> values = options.stream()
            .collect(Collectors.toMap(Option::getId, option -> getRiskyParam(option.getCallLeverage(), option.getCallAskToBS(), option.getCallBreakEven())));

        return values
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(3)
            .map(longDoubleEntry -> getTelegramMessageDto(
                getMessageTemplate(
                    optionStatsService.findOne(longDoubleEntry.getKey()).orElseThrow(OptionStatsNotFoundException::new)
                    , " درصورتی که دارایی پایه ارزنده است، اختیار بالا می تواند مورد بررسی قرار بگیرد"
                    , "زیاد")))
            .collect(Collectors.toList());
    }

    private Double getRiskyParam(double callLeverage, double callAskToBS, double callBreakEven) {
        return callLeverage / Math.pow((1 + callAskToBS / 100), 3) / Math.pow((1 + callBreakEven / 100), 2);
    }

    @Override
    public String getCron() {
        return "0 0,15,28,45 9-12 * * *";
    }
}

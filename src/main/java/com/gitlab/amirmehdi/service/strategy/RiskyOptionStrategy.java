package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.service.errors.OptionStatsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        Date date = new Date(System.currentTimeMillis() - (15 * 60 * 1000));
        List<Option> options = optionRepository.findAllByUpdatedAtGreaterThanEqual(date);
        Map<Long, Double> values = options.stream()
            .collect(Collectors.toMap(Option::getId, option -> getRiskyParam(option.getCallLeverage(), option.getCallAskToBS(), option.getCallBreakEven())));

        return values
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
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
        return "0 */15 9-12 * * *";
    }
}

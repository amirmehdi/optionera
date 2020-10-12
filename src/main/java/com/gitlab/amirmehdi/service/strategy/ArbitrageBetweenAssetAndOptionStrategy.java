package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class ArbitrageBetweenAssetAndOptionStrategy extends Strategy {

    protected ArbitrageBetweenAssetAndOptionStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService) {
        super(optionRepository, optionStatsService);
    }

    @Override
    public List<TelegramMessageDto> getSignals() {
        Option option = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now(), PageRequest.of(0, 1, Sort.by(Sort.Order.asc("callBreakEven")))).getContent().get(0);
        if (option.getCallBreakEven() > 0) {
            return null;
        }
        String text = getMessageTemplate(optionStatsService.findOne(option), "آربیتراژ روی سهم و اختیار خرید به قصد اعمال اختیار", "کم");
        return Collections.singletonList(getTelegramMessageDto(text));
    }

    @Override
    public String getCron() {
        return "0 0,29 9-12 * * *";
    }
}

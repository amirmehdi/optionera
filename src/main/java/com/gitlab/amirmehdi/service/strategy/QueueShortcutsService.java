package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class QueueShortcutsService extends Strategy {

    private final InstrumentRepository instrumentRepository;
    private final Market market;
    private final Map<String, Option> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(20, TimeUnit.MINUTES)
        .build();

    protected QueueShortcutsService(OptionRepository optionRepository, OptionStatsService optionStatsService, InstrumentRepository instrumentRepository, Market market) {
        super(optionRepository, optionStatsService);
        this.instrumentRepository = instrumentRepository;
        this.market = market;
    }

    @Override
    public List<TelegramMessageDto> getSignals() {
        List<String> isins= instrumentRepository
            .findAll().stream()
            .filter(instrument -> market.getStockWatch(instrument.getIsin()).isBuyQueue())
            .map(Instrument::getIsin)
            .collect(Collectors.toList());

        List<Option> optionList = new ArrayList<>();
        isins.removeAll(cachedIsin.keySet());

        optionRepository
            .findAllByInstrumentIsinIsIn(isins)
            .stream()
            .collect(Collectors.groupingBy(option -> option.getInstrument().getIsin()))
            .forEach((s, options) -> {
                options.sort(Comparator.comparing(Option::getCallBreakEven));
                optionList.add(options.get(0));
            });

        return optionList
            .stream()
            .peek(option -> cachedIsin.put(option.getInstrument().getIsin(), option))
            .map(option -> getTelegramMessageDto(getMessageTemplate(optionStatsService.findOne(option), "میان بر صف(دارایی پایه صف خرید است ولی اختیار داده شده قابل خرید است)", "متوسط")))
            .collect(Collectors.toList());

    }

    @Override
    public String getCron() {
        return "*/10 * 9-12 * * *";
    }
}

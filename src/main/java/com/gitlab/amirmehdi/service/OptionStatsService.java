package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.service.dto.OptionCriteria;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OptionStatsService {
    private final OptionService optionService;
    private final OptionQueryService optionQueryService;
    private final Market market;

    public OptionStatsService(OptionService optionService, OptionQueryService optionQueryService, Market market) {
        this.optionQueryService = optionQueryService;
        this.market = market;
        this.optionService = optionService;
    }

    public Optional<OptionStats> findOne(long id) {
        Optional<Option> option = optionService.findOne(id);
        return option.map(this::toOptionStats);
    }

    public OptionStats findOne(Option option) {
        return toOptionStats(option);
    }

    public Page<OptionStats> findAll(OptionCriteria criteria, Pageable pageable) {
        Page<Option> options = optionQueryService.findByCriteria(criteria,pageable);
        List<OptionStats> optionStats = options.get()
            .map(this::toOptionStats)
            .collect(Collectors.toList());
        return new PageImpl<>(optionStats, pageable, options.getTotalElements());
    }

    private OptionStats toOptionStats(Option option) {
        return new OptionStats()
            .option(option)
            .callStockWatch(market.getStockWatch(option.getCallIsin()))
            .callBidAsk(market.getBidAsk(option.getCallIsin()).getBestBidAsk())
            .putStockWatch(market.getStockWatch(option.getPutIsin()))
            .putBidAsk(market.getBidAsk(option.getPutIsin()).getBestBidAsk())
            .baseStockWatch(market.getStockWatch(option.getInstrument().getIsin()))
            .baseBidAsk(market.getBidAsk(option.getInstrument().getIsin()).getBestBidAsk());
    }
}

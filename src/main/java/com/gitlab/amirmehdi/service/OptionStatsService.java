package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OptionStatsService {
    private final OptionService optionService;
    private final Market market;

    public OptionStatsService(OptionService optionService, Market market) {
        this.market = market;
        this.optionService = optionService;
    }

    public Page<OptionStats> findAll(Pageable pageable) {
        Page<Option> options = optionService.findAll(pageable);
        List<OptionStats> optionStats = options.get()
            .map(option -> new OptionStats()
            .option(option)
            .callStockWatch(market.getStockWatch(option.getCallIsin()))
            .callBidAsk(market.getBidAsk(option.getCallIsin()).getBestBidAsk())
            .putStockWatch(market.getStockWatch(option.getPutIsin()))
            .putBidAsk(market.getBidAsk(option.getPutIsin()).getBestBidAsk())
            .baseStockWatch(market.getStockWatch(option.getInstrument().getIsin()))
            .baseBidAsk(market.getBidAsk(option.getInstrument().getIsin()).getBestBidAsk())).collect(Collectors.toList());
        return new PageImpl<>(optionStats);
    }
}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class MetricService {
    private final MeterRegistry registry;
    private final InstrumentRepository instrumentRepository;
    private final Market market;

    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;

    public MetricService(MeterRegistry registry, InstrumentRepository instrumentRepository, Market market) {
        this.registry = registry;
        this.instrumentRepository = instrumentRepository;
        this.market = market;
    }

    @Scheduled(cron = "0,30 * * * * *")
    public void publishMetricsOfBeingRealTime() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        instrumentRepository.findAll().forEach(instrument -> {
            StockWatch stockWatch = market.getStockWatch(instrument.getIsin());
            if (stockWatch != null) {
                registry.gauge("omid.rlc.stockwatch", Collections.singleton(new ImmutableTag("isin", stockWatch.getIsin())), (stockWatch.getDateTime().getTime() - new Date().getTime()) / 1000);
            }
            BidAsk bidAsk = market.getBidAsk(instrument.getIsin());
            if (bidAsk != null) {
                registry.gauge("omid.rlc.bidask", Collections.singleton(new ImmutableTag("isin", bidAsk.getIsin())), (bidAsk.getDateTime().getTime() - new Date().getTime()) / 1000);
            }
        });
    }
}

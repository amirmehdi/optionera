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
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricService {
    private final MeterRegistry registry;
    private final InstrumentRepository instrumentRepository;
    private final Market market;
    HashMap<String, AtomicLong> metricValues = new HashMap<>();
    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;

    public MetricService(MeterRegistry registry, InstrumentRepository instrumentRepository, Market market) {
        this.registry = registry;
        this.instrumentRepository = instrumentRepository;
        this.market = market;
    }

    @Scheduled(cron = "0/15 * * * * *")
    public void publishMetricsOfBeingRealTime() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        instrumentRepository.findAll().forEach(instrument -> {
            StockWatch stockWatch = market.getStockWatch(instrument.getIsin());
            if (stockWatch != null) {
                long value = (new Date().getTime() - stockWatch.getDateTime().getTime()) / 1000;
                reportMetric("omid.rlc.stockwatch", new ImmutableTag("isin", stockWatch.getIsin()), value);
            }
            BidAsk bidAsk = market.getBidAsk(instrument.getIsin());
            if (bidAsk != null) {
                long value = (new Date().getTime() - bidAsk.getDateTime().getTime()) / 1000;
                reportMetric("omid.rlc.bidask", new ImmutableTag("isin", bidAsk.getIsin()), value);
            }
        });
    }

    public void reportMetric(String name, ImmutableTag immutableTag, long value) {
        String mapKey = name + immutableTag.getValue();
        if (metricValues.containsKey(mapKey)) {
            metricValues.get(mapKey).set(value);
        } else {
            metricValues.put(mapKey, registry.gauge(name, Collections.singleton(immutableTag), new AtomicLong(value)));
        }
    }
}

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

    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;

    public MetricService(MeterRegistry registry, InstrumentRepository instrumentRepository, Market market) {
        this.registry = registry;
        this.instrumentRepository = instrumentRepository;
        this.market = market;
    }

    HashMap<String,AtomicLong> metricValues = new HashMap<>();

    @Scheduled(cron = "0,30 * * * * *")
    public void publishMetricsOfBeingRealTime() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        instrumentRepository.findAll().forEach(instrument -> {
            StockWatch stockWatch = market.getStockWatch(instrument.getIsin());
            if (stockWatch != null) {
                long value = (new Date().getTime() - stockWatch.getDateTime().getTime()) / 1000;
                reportValue("omid.rlc.stockwatch", stockWatch.getIsin(), value);
            }
            BidAsk bidAsk = market.getBidAsk(instrument.getIsin());
            if (bidAsk != null) {
                long value = (new Date().getTime() - bidAsk.getDateTime().getTime()) / 1000;
                reportValue("omid.rlc.bidask", bidAsk.getIsin(), value);
            }
        });
    }

    protected void reportValue(String metricName, String isin, long value) {
        if (metricValues.containsKey(metricName + isin)) {
            metricValues.get(metricName + isin).set(value);
        } else {
            registry.gauge(metricName, Collections.singleton(new ImmutableTag("isin", isin)), new AtomicLong(value));
        }
    }
}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduledJobs {
    private final CrawlerJobs crawlerJobs;

    @Value("${application.market-time-check}")
    private boolean marketTimeCheck;

    public ScheduledJobs(CrawlerJobs crawlerJobs) {
        this.crawlerJobs = crawlerJobs;
    }

    @Scheduled(fixedRateString = "${application.market-updater-fixed-rate}")
    public void marketUpdater() throws Exception {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen(new Date()))
            return;
        crawlerJobs.marketUpdater();
    }

    @Scheduled(fixedRateString = "${application.open-interest-updater-fixed-rate}")
    public void openInterestUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen(new Date()))
            return;
        crawlerJobs.openInterestUpdater();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void optionCrawler() {
        crawlerJobs.optionCrawler();
    }
}

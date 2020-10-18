package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledJobs {
    private final CrawlerJobs crawlerJobs;

    @Value("${application.schedules.timecheck}")
    private boolean marketTimeCheck;

    public ScheduledJobs(CrawlerJobs crawlerJobs) {
        this.crawlerJobs = crawlerJobs;
    }

    @Scheduled(fixedDelayString = "${application.schedules.arbitrage}")
    public void updateImportantOptions() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.arbitrageOptionsUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedules.market}")
    public void marketUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.marketUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedules.interest}")
    public void openInterestUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.openInterestUpdater();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void optionCrawler() {
        crawlerJobs.optionCrawler();
    }
}

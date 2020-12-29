package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.service.crawler.CrawlerBox;
import com.gitlab.amirmehdi.service.crawler.TseCrawler;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ScheduledJobs {
    private final TseCrawler tseCrawler;
    private final CrawlerBox crawlerBox;
    private final Market market;
    private final TelegramMessageSender telegramMessageSender;
    private final ApplicationProperties properties;

    public ScheduledJobs(TseCrawler tseCrawler, CrawlerBox crawlerBox, Market market, TelegramMessageSender telegramMessageSender, ApplicationProperties applicationProperties) {
        this.tseCrawler = tseCrawler;
        this.crawlerBox = crawlerBox;
        this.market = market;
        this.telegramMessageSender = telegramMessageSender;
        this.properties = applicationProperties;
    }

    @Scheduled(fixedDelayString = "${application.schedule.arbitrage}")
    public void updateImportantOptions() {
        if (properties.getSchedule().isTimeCheck() && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerBox.getBestMarketUpdater().arbitrageOptionsUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.market}")
    public void marketUpdater() {
        if (properties.getSchedule().isTimeCheck() && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerBox.highAvailableBoardUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.clientsInfo}")
    public void clientsInfoUpdater() {
        if (properties.getSchedule().isTimeCheck() && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerBox.getBestMarketUpdater().clientsInfoUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.interest}")
    public void openInterestUpdater() {
        if (properties.getSchedule().isTimeCheck() && !MarketTimeUtil.isMarketOpen())
            return;
        tseCrawler.openInterestUpdater();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void optionCrawler() {
        tseCrawler.optionCrawler();
        crawlerBox.getBestMarketUpdater().instrumentUpdater();
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void checkNullStockWatch() {
        if (market.getStockWatch("IRO1FOLD0001") == null) {
            tseCrawler.optionCrawler();
            tseCrawler.openInterestUpdater();
            crawlerBox.getBestMarketUpdater().boardUpdater(null);
            crawlerBox.getBestMarketUpdater().instrumentUpdater();
            telegramMessageSender.sendMessage(new TelegramMessageDto(properties.getTelegram().getHealthCheckChat(), "Why redis is empty!"));
        }
    }
}

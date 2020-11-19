package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledJobs {
    private final CrawlerJobs crawlerJobs;
    private final BoardService boardService;
    private final Market market;
    private final TelegramMessageSender telegramMessageSender;
    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;

    @Value("${application.telegram.token}")
    private String apiToken;
    @Value("${application.telegram.privateChat}")
    private String privateChannelId;

    public ScheduledJobs(CrawlerJobs crawlerJobs, BoardService boardService, Market market, TelegramMessageSender telegramMessageSender) {
        this.crawlerJobs = crawlerJobs;
        this.boardService = boardService;
        this.market = market;
        this.telegramMessageSender = telegramMessageSender;
    }

    @Scheduled(fixedDelayString = "${application.schedule.arbitrage}")
    public void updateImportantOptions() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.arbitrageOptionsUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.market}")
    public void marketUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.marketUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.clientsInfo}")
    public void clientsInfoUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.clientsInfoUpdater();
    }

    @Scheduled(fixedRateString = "${application.schedule.board}")
    public void boardUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        boardService.updateAllBoard();
    }

    @Scheduled(fixedRateString = "${application.schedule.interest}")
    public void openInterestUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        crawlerJobs.openInterestUpdater();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void optionCrawler() {
        crawlerJobs.optionCrawler();
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void checkNullStockWatch() {
        if (market.getStockWatch("IRO1FOLD0001") == null) {
            crawlerJobs.optionCrawler();
            crawlerJobs.openInterestUpdater();
            crawlerJobs.marketUpdater();
            telegramMessageSender.sendMessage(new TelegramMessageDto(apiToken,privateChannelId,"Why redis is empty!"));
        }
    }
}

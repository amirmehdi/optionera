package com.gitlab.amirmehdi.web.rest;


import com.gitlab.amirmehdi.batch.BatchConfiguration;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.*;
import com.gitlab.amirmehdi.service.algorithm.HeadLineAlgorithm;
import com.gitlab.amirmehdi.service.crawler.CrawlerBox;
import com.gitlab.amirmehdi.service.crawler.TseCrawler;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/job")
public class ManualJobResource {
    private final BatchConfiguration batchConfiguration;
    private final TseCrawler tseCrawler;
    private final CrawlerBox crawlerBox;
    private final BoardService boardService;
    private final StrategyService strategyService;
    private final OptionService optionService;
    private final InstrumentService instrumentService;
    private final HeadLineAlgorithm algorithm;
    private final TelegramMessageSender telegramMessageSender;

    public ManualJobResource(BatchConfiguration batchConfiguration, TseCrawler tseCrawler, CrawlerBox crawlerBox, BoardService boardService, StrategyService strategyService, OptionService optionService, InstrumentService instrumentService, HeadLineAlgorithm algorithm, TelegramMessageSender telegramMessageSender) {
        this.batchConfiguration = batchConfiguration;
        this.tseCrawler = tseCrawler;
        this.crawlerBox = crawlerBox;
        this.boardService = boardService;
        this.strategyService = strategyService;
        this.optionService = optionService;
        this.instrumentService = instrumentService;
        this.algorithm = algorithm;
        this.telegramMessageSender = telegramMessageSender;
    }

    @PostMapping(value = "volatility")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> updateVolatility() {
        batchConfiguration.updateVolatility();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "arbitrage")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> importantOptionsUpdater() {
        crawlerBox.getBestMarketUpdater().arbitrageOptionsUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "market")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> marketUpdater() {
        crawlerBox.highAvailableBoardUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "whole-market")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> wholeMarketUpdater(String marketUpdater) {
        crawlerBox.getMarketUpdater(marketUpdater).boardUpdater(optionService.findAllCallAndPutIsins());
        crawlerBox.getMarketUpdater(marketUpdater).boardUpdater(instrumentService.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList()));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "clients-info")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> clientsInfoUpdater() {
        crawlerBox.getBestMarketUpdater().clientsInfoUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "board")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> updateAllBoard() {
        boardService.updateAllBoard();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "open-interest")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> openInterestUpdater() {
        tseCrawler.openInterestUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "option-crawler")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> optionCrawler() {
        tseCrawler.optionCrawler();
        tseCrawler.embeddedOptionCrawler();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "tse-ids")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> tseIdUpdater() {
        crawlerBox.getBestMarketUpdater().instrumentUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "strategy-runner")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> strategyRunner() {
        strategyService.run();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "headline-algorithm/{sleep}/{repeat}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> headlineAlgorithm(@PathVariable long sleep, @PathVariable int repeat) {
        algorithm.retrieveHeadLineOrdersAndSend(sleep, repeat);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "tg-send")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> telegramSender(@RequestBody TelegramMessageDto dto) {
        telegramMessageSender.sendMessage(dto);
        return ResponseEntity.ok().build();
    }
}

package com.gitlab.amirmehdi.web.rest;


import com.gitlab.amirmehdi.batch.BatchConfiguration;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.BoardService;
import com.gitlab.amirmehdi.service.crawler.CrawlerBox;
import com.gitlab.amirmehdi.service.crawler.TseCrawler;
import com.gitlab.amirmehdi.service.StrategyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/job")
public class ManualJobResource {
    private final BatchConfiguration batchConfiguration;
    private final TseCrawler tseCrawler;
    private final CrawlerBox crawlerBox;
    private final BoardService boardService;
    private final StrategyService strategyService;

    public ManualJobResource(BatchConfiguration batchConfiguration, TseCrawler tseCrawler, CrawlerBox crawlerBox, BoardService boardService, StrategyService strategyService) {
        this.batchConfiguration = batchConfiguration;
        this.tseCrawler = tseCrawler;
        this.crawlerBox = crawlerBox;
        this.boardService = boardService;
        this.strategyService = strategyService;
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
        crawlerBox.getBestMarketUpdater().boardUpdater();
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
}

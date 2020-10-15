package com.gitlab.amirmehdi.web.rest;


import com.gitlab.amirmehdi.batch.BatchConfiguration;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.CrawlerJobs;
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
    private final CrawlerJobs crawlerJobs;
    private final StrategyService strategyService;

    public ManualJobResource(BatchConfiguration batchConfiguration, CrawlerJobs crawlerJobs, StrategyService strategyService) {
        this.batchConfiguration = batchConfiguration;
        this.crawlerJobs = crawlerJobs;
        this.strategyService = strategyService;
    }

    @PostMapping(value = "volatility")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> updateVolatility() {
        batchConfiguration.updateVolatility();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "market")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> marketUpdater() throws Exception {
        crawlerJobs.marketUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "open-interest")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> openInterestUpdater() {
        crawlerJobs.openInterestUpdater();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "option-crawler")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> optionCrawler() {
        crawlerJobs.optionCrawler();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "tse-ids")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> tseIdUpdater() {
        crawlerJobs.updateTseIds();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "strategy-runner")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> strategyRunner() {
        strategyService.run();
        return ResponseEntity.ok().build();
    }
}

package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.service.OptionStatsService;
import io.github.jhipster.web.util.PaginationUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@Log4j2
public class OptionStatsResource {
    private final OptionStatsService optionStatsService;

    public OptionStatsResource(OptionStatsService optionStatsService) {
        this.optionStatsService = optionStatsService;
    }

    @GetMapping("/option-stats")
    public ResponseEntity<List<OptionStats>> getAllOptions(Pageable pageable) {
        log.debug("REST request to get a page of OptionStats");
        Page<OptionStats> page = optionStatsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.OptionCriteria;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Log4j2
public class OptionStatsResource {
    private final OptionStatsService optionStatsService;

    public OptionStatsResource(OptionStatsService optionStatsService) {
        this.optionStatsService = optionStatsService;
    }

    @GetMapping("/option-stats/{id}")
    public ResponseEntity<OptionStats> getOptionState(@PathVariable Long id) {
        log.debug("REST request to get a OptionStats");
        Optional<OptionStats> optionStats = optionStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionStats);
    }

    @GetMapping("/option-stats")
    public ResponseEntity<List<OptionStats>> getAllOptions(OptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get a page of OptionStats");
        /*if (SecurityUtils.isCurrentUserInRoleJustUser()) {
            if (criteria.getExpDate() == null) {
                criteria.setExpDate(new LocalDateFilter().setLessThan(LocalDate.now().plusDays(30)));
            } else if (criteria.getExpDate().getLessThan() == null || criteria.getExpDate().getLessThan().isAfter(LocalDate.now().plusDays(30))) {
                criteria.getExpDate().setLessThan(LocalDate.now().plusDays(30));
            }
        }*/
        if (pageable.getSort().isSorted()) {
            if (pageable.getSort().get().count() == 1) {
                List<Sort.Order> orders = pageable.getSort().get().collect(Collectors.toList());
                if (orders.get(0).getProperty().equals("id")) {
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders.get(0).withProperty("callBreakEven")));
                } else if (orders.get(0).getProperty().equals("expDate")) {
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders.get(0), Sort.Order.asc("instrument.isin"), Sort.Order.asc("strikePrice")));
                }
            }
        }
        Page<OptionStats> page = optionStatsService.findAll(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

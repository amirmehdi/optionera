package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.service.InstrumentHistoryService;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.InstrumentHistory}.
 */
@RestController
@RequestMapping("/api")
public class InstrumentHistoryResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentHistoryResource.class);

    private final InstrumentHistoryService instrumentHistoryService;

    public InstrumentHistoryResource(InstrumentHistoryService instrumentHistoryService) {
        this.instrumentHistoryService = instrumentHistoryService;
    }

    /**
     * {@code GET  /instrument-histories} : get all the instrumentHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instrumentHistories in body.
     */
    @GetMapping("/instrument-histories")
    public ResponseEntity<List<InstrumentHistory>> getAllInstrumentHistories(Pageable pageable) {
        log.debug("REST request to get a page of InstrumentHistories");
        Page<InstrumentHistory> page = instrumentHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /instrument-histories/:id} : get the "id" instrumentHistory.
     *
     * @param id the id of the instrumentHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrumentHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instrument-histories/{id}")
    public ResponseEntity<InstrumentHistory> getInstrumentHistory(@PathVariable Long id) {
        log.debug("REST request to get InstrumentHistory : {}", id);
        Optional<InstrumentHistory> instrumentHistory = instrumentHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instrumentHistory);
    }
}

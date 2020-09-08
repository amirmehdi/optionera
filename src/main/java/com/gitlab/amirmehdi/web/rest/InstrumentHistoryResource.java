package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.service.InstrumentHistoryService;
import com.gitlab.amirmehdi.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.InstrumentHistory}.
 */
@RestController
@RequestMapping("/api")
public class InstrumentHistoryResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentHistoryResource.class);

    private static final String ENTITY_NAME = "instrumentHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentHistoryService instrumentHistoryService;

    public InstrumentHistoryResource(InstrumentHistoryService instrumentHistoryService) {
        this.instrumentHistoryService = instrumentHistoryService;
    }

    /**
     * {@code POST  /instrument-histories} : Create a new instrumentHistory.
     *
     * @param instrumentHistory the instrumentHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrumentHistory, or with status {@code 400 (Bad Request)} if the instrumentHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instrument-histories")
    public ResponseEntity<InstrumentHistory> createInstrumentHistory(@Valid @RequestBody InstrumentHistory instrumentHistory) throws URISyntaxException {
        log.debug("REST request to save InstrumentHistory : {}", instrumentHistory);
        if (instrumentHistory.getId() != null) {
            throw new BadRequestAlertException("A new instrumentHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstrumentHistory result = instrumentHistoryService.save(instrumentHistory);
        return ResponseEntity.created(new URI("/api/instrument-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instrument-histories} : Updates an existing instrumentHistory.
     *
     * @param instrumentHistory the instrumentHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrumentHistory,
     * or with status {@code 400 (Bad Request)} if the instrumentHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instrumentHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instrument-histories")
    public ResponseEntity<InstrumentHistory> updateInstrumentHistory(@Valid @RequestBody InstrumentHistory instrumentHistory) throws URISyntaxException {
        log.debug("REST request to update InstrumentHistory : {}", instrumentHistory);
        if (instrumentHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InstrumentHistory result = instrumentHistoryService.save(instrumentHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instrumentHistory.getId().toString()))
            .body(result);
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

    /**
     * {@code DELETE  /instrument-histories/:id} : delete the "id" instrumentHistory.
     *
     * @param id the id of the instrumentHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instrument-histories/{id}")
    public ResponseEntity<Void> deleteInstrumentHistory(@PathVariable Long id) {
        log.debug("REST request to delete InstrumentHistory : {}", id);
        instrumentHistoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

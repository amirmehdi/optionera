package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.security.SecurityUtils;
import com.gitlab.amirmehdi.service.SignalQueryService;
import com.gitlab.amirmehdi.service.SignalService;
import com.gitlab.amirmehdi.service.dto.SignalCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Signal}.
 */
@RestController
@RequestMapping("/api")
public class SignalResource {

    private static final String ENTITY_NAME = "signal";
    private final Logger log = LoggerFactory.getLogger(SignalResource.class);
    private final SignalService signalService;
    private final SignalQueryService signalQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SignalResource(SignalService signalService, SignalQueryService signalQueryService) {
        this.signalService = signalService;
        this.signalQueryService = signalQueryService;
    }

    /**
     * {@code POST  /signals} : Create a new signal.
     *
     * @param signal the signal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signal, or with status {@code 400 (Bad Request)} if the signal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Signal> createSignal(@Valid @RequestBody Signal signal) throws URISyntaxException {
        log.debug("REST request to save Signal : {}", signal);
        if (signal.getId() != null) {
            throw new BadRequestAlertException("A new signal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Signal result = signalService.save(signal);
        return ResponseEntity.created(new URI("/api/signals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signals} : Updates an existing signal.
     *
     * @param signal the signal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signal,
     * or with status {@code 400 (Bad Request)} if the signal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Signal> updateSignal(@Valid @RequestBody Signal signal) throws URISyntaxException {
        log.debug("REST request to update Signal : {}", signal);
        if (signal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Signal result = signalService.save(signal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /signals} : get all the signals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signals in body.
     */
    @GetMapping("/signals")
    public ResponseEntity<List<Signal>> getAllSignals(SignalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Signals by criteria: {}", criteria);
        if (SecurityUtils.isCurrentUserInRoleJustUser(AuthoritiesConstants.USER) || SecurityUtils.isCurrentUserInRoleJustUser(AuthoritiesConstants.BRONZE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Page<Signal> page = signalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /signals/count} : count all the signals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/signals/count")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Long> countSignals(SignalCriteria criteria) {
        log.debug("REST request to count Signals by criteria: {}", criteria);
        return ResponseEntity.ok().body(signalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /signals/:id} : get the "id" signal.
     *
     * @param id the id of the signal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Signal> getSignal(@PathVariable Long id) {
        log.debug("REST request to get Signal : {}", id);
        Optional<Signal> signal = signalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signal);
    }

    /**
     * {@code DELETE  /signals/:id} : delete the "id" signal.
     *
     * @param id the id of the signal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteSignal(@PathVariable Long id) {
        log.debug("REST request to delete Signal : {}", id);
        signalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/signals/tg-order/{signalId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<Order>> sendOrderForStrategy(@PathVariable Long signalId) {
        log.debug("REST request to sendOrderForStrategy : {}", signalId);
        return ResponseEntity.ok().body(signalService.sendOrder(signalId));

    }
}

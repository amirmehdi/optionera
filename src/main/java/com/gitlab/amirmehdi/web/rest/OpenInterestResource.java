package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.OpenInterest;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.OpenInterestService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.OpenInterest}.
 */
@RestController
@RequestMapping("/api")
public class OpenInterestResource {

    private final Logger log = LoggerFactory.getLogger(OpenInterestResource.class);

    private static final String ENTITY_NAME = "openInterest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpenInterestService openInterestService;

    public OpenInterestResource(OpenInterestService openInterestService) {
        this.openInterestService = openInterestService;
    }

    /**
     * {@code POST  /open-interests} : Create a new openInterest.
     *
     * @param openInterest the openInterest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new openInterest, or with status {@code 400 (Bad Request)} if the openInterest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/open-interests")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenInterest> createOpenInterest(@RequestBody @Valid OpenInterest openInterest) throws URISyntaxException {
        log.debug("REST request to save OpenInterest : {}", openInterest);
        if (openInterest.getId() != null) {
            throw new BadRequestAlertException("A new openInterest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpenInterest result = openInterestService.save(openInterest);
        return ResponseEntity.created(new URI("/api/open-interests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /open-interests} : Updates an existing openInterest.
     *
     * @param openInterest the openInterest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated openInterest,
     * or with status {@code 400 (Bad Request)} if the openInterest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the openInterest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/open-interests")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenInterest> updateOpenInterest(@RequestBody @Valid OpenInterest openInterest) throws URISyntaxException {
        log.debug("REST request to update OpenInterest : {}", openInterest);
        if (openInterest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OpenInterest result = openInterestService.save(openInterest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, openInterest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /open-interests} : get all the openInterests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of openInterests in body.
     */
    @GetMapping("/open-interests")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<OpenInterest>> getAllOpenInterests(Pageable pageable) {
        log.debug("REST request to get a page of OpenInterests");
        Page<OpenInterest> page = openInterestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /open-interests/:id} : get the "id" openInterest.
     *
     * @param id the id of the openInterest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the openInterest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/open-interests/find-one")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenInterest> getOpenInterest(@RequestBody AssetCompositeKey id) {
        log.debug("REST request to get OpenInterest : {}", id);
        Optional<OpenInterest> openInterest = openInterestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(openInterest);
    }
}

package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.EmbeddedOption;
import com.gitlab.amirmehdi.service.EmbeddedOptionQueryService;
import com.gitlab.amirmehdi.service.EmbeddedOptionService;
import com.gitlab.amirmehdi.service.dto.EmbeddedOptionCriteria;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.EmbeddedOption}.
 */
@RestController
@RequestMapping("/api")
public class EmbeddedOptionResource {

    private final Logger log = LoggerFactory.getLogger(EmbeddedOptionResource.class);

    private static final String ENTITY_NAME = "embeddedOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmbeddedOptionService embeddedOptionService;

    private final EmbeddedOptionQueryService embeddedOptionQueryService;

    public EmbeddedOptionResource(EmbeddedOptionService embeddedOptionService, EmbeddedOptionQueryService embeddedOptionQueryService) {
        this.embeddedOptionService = embeddedOptionService;
        this.embeddedOptionQueryService = embeddedOptionQueryService;
    }

    /**
     * {@code POST  /embedded-options} : Create a new embeddedOption.
     *
     * @param embeddedOption the embeddedOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new embeddedOption, or with status {@code 400 (Bad Request)} if the embeddedOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/embedded-options")
    public ResponseEntity<EmbeddedOption> createEmbeddedOption(@Valid @RequestBody EmbeddedOption embeddedOption) throws URISyntaxException {
        log.debug("REST request to save EmbeddedOption : {}", embeddedOption);
        if (embeddedOption.getId() != null) {
            throw new BadRequestAlertException("A new embeddedOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmbeddedOption result = embeddedOptionService.save(embeddedOption);
        return ResponseEntity.created(new URI("/api/embedded-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /embedded-options} : Updates an existing embeddedOption.
     *
     * @param embeddedOption the embeddedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated embeddedOption,
     * or with status {@code 400 (Bad Request)} if the embeddedOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the embeddedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/embedded-options")
    public ResponseEntity<EmbeddedOption> updateEmbeddedOption(@Valid @RequestBody EmbeddedOption embeddedOption) throws URISyntaxException {
        log.debug("REST request to update EmbeddedOption : {}", embeddedOption);
        if (embeddedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmbeddedOption result = embeddedOptionService.save(embeddedOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, embeddedOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /embedded-options} : get all the embeddedOptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of embeddedOptions in body.
     */
    @GetMapping("/embedded-options")
    public ResponseEntity<List<EmbeddedOption>> getAllEmbeddedOptions(EmbeddedOptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmbeddedOptions by criteria: {}", criteria);
        Page<EmbeddedOption> page = embeddedOptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /embedded-options/count} : count all the embeddedOptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/embedded-options/count")
    public ResponseEntity<Long> countEmbeddedOptions(EmbeddedOptionCriteria criteria) {
        log.debug("REST request to count EmbeddedOptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(embeddedOptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /embedded-options/:id} : get the "id" embeddedOption.
     *
     * @param id the id of the embeddedOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the embeddedOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/embedded-options/{id}")
    public ResponseEntity<EmbeddedOption> getEmbeddedOption(@PathVariable Long id) {
        log.debug("REST request to get EmbeddedOption : {}", id);
        Optional<EmbeddedOption> embeddedOption = embeddedOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(embeddedOption);
    }

    /**
     * {@code DELETE  /embedded-options/:id} : delete the "id" embeddedOption.
     *
     * @param id the id of the embeddedOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/embedded-options/{id}")
    public ResponseEntity<Void> deleteEmbeddedOption(@PathVariable Long id) {
        log.debug("REST request to delete EmbeddedOption : {}", id);
        embeddedOptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

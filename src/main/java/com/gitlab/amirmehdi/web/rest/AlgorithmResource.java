package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.service.AlgorithmQueryService;
import com.gitlab.amirmehdi.service.AlgorithmService;
import com.gitlab.amirmehdi.service.dto.AlgorithmCriteria;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
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
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Algorithm}.
 */
@RestController
@RequestMapping("/api")
public class AlgorithmResource {

    private final Logger log = LoggerFactory.getLogger(AlgorithmResource.class);

    private static final String ENTITY_NAME = "algorithm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlgorithmService algorithmService;

    private final AlgorithmQueryService algorithmQueryService;

    public AlgorithmResource(AlgorithmService algorithmService, AlgorithmQueryService algorithmQueryService) {
        this.algorithmService = algorithmService;
        this.algorithmQueryService = algorithmQueryService;
    }

    /**
     * {@code POST  /algorithms} : Create a new algorithm.
     *
     * @param algorithmDTO the algorithmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new algorithmDTO, or with status {@code 400 (Bad Request)} if the algorithm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/algorithms")
    public ResponseEntity<AlgorithmDTO> createAlgorithm(@Valid @RequestBody AlgorithmDTO algorithmDTO) throws URISyntaxException {
        log.debug("REST request to save Algorithm : {}", algorithmDTO);
        if (algorithmDTO.getId() != null) {
            throw new BadRequestAlertException("A new algorithm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity.created(new URI("/api/algorithms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /algorithms} : Updates an existing algorithm.
     *
     * @param algorithmDTO the algorithmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated algorithmDTO,
     * or with status {@code 400 (Bad Request)} if the algorithmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the algorithmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/algorithms")
    public ResponseEntity<AlgorithmDTO> updateAlgorithm(@Valid @RequestBody AlgorithmDTO algorithmDTO) throws URISyntaxException {
        log.debug("REST request to update Algorithm : {}", algorithmDTO);
        if (algorithmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, algorithmDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /algorithms} : get all the algorithms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of algorithms in body.
     */
    @GetMapping("/algorithms")
    public ResponseEntity<List<AlgorithmDTO>> getAllAlgorithms(AlgorithmCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Algorithms by criteria: {}", criteria);
        Page<AlgorithmDTO> page = algorithmQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /algorithms/count} : count all the algorithms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/algorithms/count")
    public ResponseEntity<Long> countAlgorithms(AlgorithmCriteria criteria) {
        log.debug("REST request to count Algorithms by criteria: {}", criteria);
        return ResponseEntity.ok().body(algorithmQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /algorithms/:id} : get the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the algorithmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/algorithms/{id}")
    public ResponseEntity<AlgorithmDTO> getAlgorithm(@PathVariable Long id) {
        log.debug("REST request to get Algorithm : {}", id);
        Optional<AlgorithmDTO> algorithmDTO = algorithmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(algorithmDTO);
    }

    /**
     * {@code DELETE  /algorithms/:id} : delete the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/algorithms/{id}")
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable Long id) {
        log.debug("REST request to delete Algorithm : {}", id);
        algorithmService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

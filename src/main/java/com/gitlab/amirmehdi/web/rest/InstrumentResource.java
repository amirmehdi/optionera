package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Instrument}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);

    private static final String ENTITY_NAME = "instrument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentRepository instrumentRepository;

    public InstrumentResource(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    /**
     * {@code POST  /instruments} : Create a new instrument.
     *
     * @param instrument the instrument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrument, or with status {@code 400 (Bad Request)} if the instrument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instruments")
    public ResponseEntity<Instrument> createInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrument);
        if (instrument.getId() != null) {
            throw new BadRequestAlertException("A new instrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.created(new URI("/api/instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instruments} : Updates an existing instrument.
     *
     * @param instrument the instrument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrument,
     * or with status {@code 400 (Bad Request)} if the instrument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instrument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instruments")
    public ResponseEntity<Instrument> updateInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}", instrument);
        if (instrument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instrument.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /instruments} : get all the instruments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instruments in body.
     */
    @GetMapping("/instruments")
    public ResponseEntity<List<Instrument>> getAllInstruments(Pageable pageable) {
        log.debug("REST request to get a page of Instruments");
        Page<Instrument> page = instrumentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /instruments/:id} : get the "id" instrument.
     *
     * @param id the id of the instrument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instruments/{id}")
    public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Optional<Instrument> instrument = instrumentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(instrument);
    }

    /**
     * {@code DELETE  /instruments/:id} : delete the "id" instrument.
     *
     * @param id the id of the instrument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instruments/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

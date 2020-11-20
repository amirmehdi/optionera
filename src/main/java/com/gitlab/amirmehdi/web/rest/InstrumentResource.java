package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.dto.InstrumentSearchDTO;
import com.gitlab.amirmehdi.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Instrument}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InstrumentResource {

    private static final String ENTITY_NAME = "instrument";
    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);
    private final InstrumentRepository instrumentRepository;
    private final OptionRepository optionRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public InstrumentResource(InstrumentRepository instrumentRepository, OptionRepository optionRepository) {
        this.instrumentRepository = instrumentRepository;
        this.optionRepository = optionRepository;
    }

    /**
     * {@code POST  /instruments} : Create a new instrument.
     *
     * @param instrument the instrument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrument, or with status {@code 400 (Bad Request)} if the instrument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instruments")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Instrument> createInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrument);
        if (instrumentRepository.findById(instrument.getIsin()).isPresent()) {
            throw new BadRequestAlertException("A new instrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.created(new URI("/api/instruments/" + result.getIsin()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getIsin().toString()))
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
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Instrument> updateInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}", instrument);
        if (instrument.getIsin() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instrument.getIsin().toString()))
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
    public ResponseEntity<Instrument> getInstrument(@PathVariable String id) {
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
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteInstrument(@PathVariable String id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/instruments/search/{name}")
    public ResponseEntity<List<InstrumentSearchDTO>> searchByName(@PathVariable String name) {
        List<InstrumentSearchDTO> list;
        if (name.charAt(0) == 'ض' || name.charAt(0) == 'ط'){
            list= optionRepository.findAllByNameLike("%" + name.substring(1) + "%",PageRequest.of(0, 10))
                .stream().map(option -> new InstrumentSearchDTO(option.getInstrument().getIsin(), option.getInstrument().getName()))
                .distinct()
                .collect(Collectors.toList());
        }else {
            list = instrumentRepository.findAllByNameLike("%" + name + "%", PageRequest.of(0, 10))
                .stream()
                .map(instrument -> new InstrumentSearchDTO(instrument.getIsin(), instrument.getName()))
                .collect(Collectors.toList());
            list.addAll(optionRepository.findAllByNameLike("%" + name + "%",PageRequest.of(0, 10))
                .stream().map(option -> new InstrumentSearchDTO(option.getInstrument().getIsin(), option.getInstrument().getName()))
                .distinct()
                .collect(Collectors.toList()));
        }
        list = list.stream().distinct().collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), new PageImpl<>(list));
        return ResponseEntity.ok()
            .headers(headers)
            .body(list);
    }
}

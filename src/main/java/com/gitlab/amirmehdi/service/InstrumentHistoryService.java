package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.repository.InstrumentHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InstrumentHistory}.
 */
@Service
@Transactional
public class InstrumentHistoryService {

    private final Logger log = LoggerFactory.getLogger(InstrumentHistoryService.class);

    private final InstrumentHistoryRepository instrumentHistoryRepository;

    public InstrumentHistoryService(InstrumentHistoryRepository instrumentHistoryRepository) {
        this.instrumentHistoryRepository = instrumentHistoryRepository;
    }

    /**
     * Save a instrumentHistory.
     *
     * @param instrumentHistory the entity to save.
     * @return the persisted entity.
     */
    public InstrumentHistory save(InstrumentHistory instrumentHistory) {
        log.debug("Request to save InstrumentHistory : {}", instrumentHistory);
        return instrumentHistoryRepository.save(instrumentHistory);
    }

    /**
     * Get all the instrumentHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InstrumentHistory> findAll(Pageable pageable) {
        log.debug("Request to get all InstrumentHistories");
        return instrumentHistoryRepository.findAll(pageable);
    }

    /**
     * Get one instrumentHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InstrumentHistory> findOne(Long id) {
        log.debug("Request to get InstrumentHistory : {}", id);
        return instrumentHistoryRepository.findById(id);
    }

    /**
     * Delete the instrumentHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InstrumentHistory : {}", id);
        instrumentHistoryRepository.deleteById(id);
    }
}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.InstrumentHistoryCompositeKey;
import com.gitlab.amirmehdi.domain.InstrumentTradeHistory;
import com.gitlab.amirmehdi.repository.InstrumentTradeHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InstrumentTradeHistory}.
 */
@Service
@Transactional
public class InstrumentTradeHistoryService {

    private final Logger log = LoggerFactory.getLogger(InstrumentTradeHistoryService.class);

    private final InstrumentTradeHistoryRepository instrumentTradeHistoryRepository;

    public InstrumentTradeHistoryService(InstrumentTradeHistoryRepository instrumentTradeHistoryRepository) {
        this.instrumentTradeHistoryRepository = instrumentTradeHistoryRepository;
    }

    /**
     * Save a instrumentHistory.
     *
     * @param instrumentTradeHistory the entity to save.
     * @return the persisted entity.
     */
    public InstrumentTradeHistory save(InstrumentTradeHistory instrumentTradeHistory) {
        log.debug("Request to save InstrumentHistory : {}", instrumentTradeHistory);
        return instrumentTradeHistoryRepository.save(instrumentTradeHistory);
    }

    /**
     * Get all the instrumentHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InstrumentTradeHistory> findAll(Pageable pageable) {
        log.debug("Request to get all InstrumentHistories");
        return instrumentTradeHistoryRepository.findAll(pageable);
    }

    /**
     * Get one instrumentHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InstrumentTradeHistory> findOne(InstrumentHistoryCompositeKey id) {
        log.debug("Request to get InstrumentHistory : {}", id);
        return instrumentTradeHistoryRepository.findById(id);
    }

    /**
     * Delete the instrumentHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(InstrumentHistoryCompositeKey id) {
        log.debug("Request to delete InstrumentHistory : {}", id);
        instrumentTradeHistoryRepository.deleteById(id);
    }
}

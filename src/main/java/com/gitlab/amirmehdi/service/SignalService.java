package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.repository.SignalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Signal}.
 */
@Service
@Transactional
public class SignalService {

    private final Logger log = LoggerFactory.getLogger(SignalService.class);

    private final SignalRepository signalRepository;
    private final StrategyService strategyService;

    public SignalService(SignalRepository signalRepository, StrategyService strategyService) {
        this.signalRepository = signalRepository;
        this.strategyService = strategyService;
    }

    /**
     * Save a signal.
     *
     * @param signal the entity to save.
     * @return the persisted entity.
     */
    public Signal save(Signal signal) {
        log.debug("Request to save Signal : {}", signal);
        return signalRepository.save(signal);
    }

    /**
     * Get all the signals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Signal> findAll(Pageable pageable) {
        log.debug("Request to get all Signals");
        return signalRepository.findAll(pageable);
    }

    /**
     * Get one signal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Signal> findOne(Long id) {
        log.debug("Request to get Signal : {}", id);
        return signalRepository.findById(id);
    }

    /**
     * Delete the signal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Signal : {}", id);
        signalRepository.deleteById(id);
    }

    public List<Order> sendOrder(Long signalId) {
        Signal signal = findOne(signalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return strategyService.sendOrder(signal);
    }
}

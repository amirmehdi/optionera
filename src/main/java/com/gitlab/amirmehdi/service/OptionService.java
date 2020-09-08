package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Option}.
 */
@Service
@Transactional
public class OptionService {

    private final Logger log = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * Save a option.
     *
     * @param option the entity to save.
     * @return the persisted entity.
     */
    public Option save(Option option) {
        log.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    /**
     * Get all the options.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Option> findAll(Pageable pageable) {
        log.debug("Request to get all Options");
        return optionRepository.findAll(pageable);
    }

    /**
     * Get one option by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Option> findOne(Long id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }

    /**
     * Delete the option by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}

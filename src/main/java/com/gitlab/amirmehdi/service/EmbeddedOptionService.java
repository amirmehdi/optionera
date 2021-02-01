package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.EmbeddedOption;
import com.gitlab.amirmehdi.repository.EmbeddedOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmbeddedOption}.
 */
@Service
@Transactional
public class EmbeddedOptionService {

    private final Logger log = LoggerFactory.getLogger(EmbeddedOptionService.class);

    private final EmbeddedOptionRepository embeddedOptionRepository;

    public EmbeddedOptionService(EmbeddedOptionRepository embeddedOptionRepository) {
        this.embeddedOptionRepository = embeddedOptionRepository;
    }

    /**
     * Save a embeddedOption.
     *
     * @param embeddedOption the entity to save.
     * @return the persisted entity.
     */
    public EmbeddedOption save(EmbeddedOption embeddedOption) {
        log.debug("Request to save EmbeddedOption : {}", embeddedOption);
        return embeddedOptionRepository.save(embeddedOption);
    }

    /**
     * Get all the embeddedOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmbeddedOption> findAll(Pageable pageable) {
        log.debug("Request to get all EmbeddedOptions");
        return embeddedOptionRepository.findAll(pageable);
    }

    /**
     * Get one embeddedOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmbeddedOption> findOne(Long id) {
        log.debug("Request to get EmbeddedOption : {}", id);
        return embeddedOptionRepository.findById(id);
    }

    /**
     * Delete the embeddedOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmbeddedOption : {}", id);
        embeddedOptionRepository.deleteById(id);
    }
}

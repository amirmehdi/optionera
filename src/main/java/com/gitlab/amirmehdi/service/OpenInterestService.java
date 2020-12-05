package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.OpenInterest;
import com.gitlab.amirmehdi.repository.OpenInterestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OpenInterest}.
 */
@Service
@Transactional
public class OpenInterestService {

    private final Logger log = LoggerFactory.getLogger(OpenInterestService.class);

    private final OpenInterestRepository openInterestRepository;

    public OpenInterestService(OpenInterestRepository openInterestRepository) {
        this.openInterestRepository = openInterestRepository;
    }

    /**
     * Save a openInterest.
     *
     * @param openInterest the entity to save.
     * @return the persisted entity.
     */
    public OpenInterest save(OpenInterest openInterest) {
        log.debug("Request to save OpenInterest : {}", openInterest);
        return openInterestRepository.save(openInterest);
    }

    /**
     * Get all the openInterests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OpenInterest> findAll(Pageable pageable) {
        log.debug("Request to get all OpenInterests");
        return openInterestRepository.findAll(pageable);
    }

    /**
     * Get one openInterest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OpenInterest> findOne(AssetCompositeKey id) {
        log.debug("Request to get OpenInterest : {}", id);
        return openInterestRepository.findById(id);
    }

}

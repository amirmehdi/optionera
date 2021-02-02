package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BourseCode}.
 */
@Service
@Transactional
public class BourseCodeService {

    private final Logger log = LoggerFactory.getLogger(BourseCodeService.class);

    private final TokenService tokenService;
    private final BourseCodeRepository bourseCodeRepository;

    public BourseCodeService(TokenService tokenService, BourseCodeRepository bourseCodeRepository) {
        this.tokenService = tokenService;
        this.bourseCodeRepository = bourseCodeRepository;
    }

    /**
     * Save a bourseCode.
     *
     * @param bourseCode the entity to save.
     * @return the persisted entity.
     */
    public BourseCode save(BourseCode bourseCode) {
        log.debug("Request to save BourseCode : {}", bourseCode);
        bourseCode = bourseCodeRepository.save(bourseCode);
        tokenService.getTokenUpdaters().get(bourseCode.getBroker().oms).updateToken(bourseCode);
        return bourseCode;
    }

    /**
     * Get all the bourseCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BourseCode> findAll(Pageable pageable) {
        log.debug("Request to get all BourseCodes");
        return bourseCodeRepository.findAll(pageable);
    }

    /**
     * Get one bourseCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BourseCode> findOne(Long id) {
        log.debug("Request to get BourseCode : {}", id);
        return bourseCodeRepository.findById(id);
    }

    /**
     * Delete the bourseCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BourseCode : {}", id);
        bourseCodeRepository.deleteById(id);
    }
}

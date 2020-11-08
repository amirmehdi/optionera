package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Algorithm;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;
import com.gitlab.amirmehdi.repository.AlgorithmRepository;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import com.gitlab.amirmehdi.service.mapper.AlgorithmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Algorithm}.
 */
@Service
@Transactional
public class AlgorithmService {

    private final Logger log = LoggerFactory.getLogger(AlgorithmService.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    public AlgorithmService(AlgorithmRepository algorithmRepository, AlgorithmMapper algorithmMapper) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
    }

    /**
     * Save a algorithm.
     *
     * @param algorithmDTO the entity to save.
     * @return the persisted entity.
     */
    public AlgorithmDTO save(AlgorithmDTO algorithmDTO) {
        log.debug("Request to save Algorithm : {}", algorithmDTO);
        Algorithm algorithm = algorithmMapper.toEntity(algorithmDTO);
        algorithm = algorithmRepository.save(algorithm);
        return algorithmMapper.toDto(algorithm);
    }

    /**
     * Get all the algorithms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlgorithmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Algorithms");
        return algorithmRepository.findAll(pageable)
            .map(algorithmMapper::toDto);
    }

    /**
     * Get one algorithm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlgorithmDTO> findOne(Long id) {
        log.debug("Request to get Algorithm : {}", id);
        return algorithmRepository.findById(id)
            .map(algorithmMapper::toDto);
    }

    /**
     * Delete the algorithm by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Algorithm : {}", id);
        algorithmRepository.deleteById(id);
    }

    public List<AlgorithmDTO> getAllEnabledAlgorithmFromType(String type) {
        return algorithmMapper.toDto(algorithmRepository.findAllByTypeAndState(type, AlgorithmState.ENABLE));
    }
}

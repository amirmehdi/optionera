package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Algorithm;
import com.gitlab.amirmehdi.domain.Algorithm_;
import com.gitlab.amirmehdi.domain.Order_;
import com.gitlab.amirmehdi.repository.AlgorithmRepository;
import com.gitlab.amirmehdi.service.dto.AlgorithmCriteria;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import com.gitlab.amirmehdi.service.mapper.AlgorithmMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link Algorithm} entities in the database.
 * The main input is a {@link AlgorithmCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AlgorithmDTO} or a {@link Page} of {@link AlgorithmDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlgorithmQueryService extends QueryService<Algorithm> {

    private final Logger log = LoggerFactory.getLogger(AlgorithmQueryService.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    public AlgorithmQueryService(AlgorithmRepository algorithmRepository, AlgorithmMapper algorithmMapper) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
    }

    /**
     * Return a {@link List} of {@link AlgorithmDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AlgorithmDTO> findByCriteria(AlgorithmCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmMapper.toDto(algorithmRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AlgorithmDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlgorithmDTO> findByCriteria(AlgorithmCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmRepository.findAll(specification, page)
            .map(algorithmMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlgorithmCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmRepository.count(specification);
    }

    /**
     * Function to convert {@link AlgorithmCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Algorithm> createSpecification(AlgorithmCriteria criteria) {
        Specification<Algorithm> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Algorithm_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Algorithm_.type));
            }
            if (criteria.getSide() != null) {
                specification = specification.and(buildSpecification(criteria.getSide(), Algorithm_.side));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Algorithm_.state));
            }
            if (criteria.getInput() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInput(), Algorithm_.input));
            }
            if (criteria.getTradeVolumeLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeVolumeLimit(), Algorithm_.tradeVolumeLimit));
            }
            if (criteria.getTradeValueLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeValueLimit(), Algorithm_.tradeValueLimit));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Algorithm_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Algorithm_.updatedAt));
            }
            if (criteria.getIsins() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsins(), Algorithm_.isins));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(Algorithm_.orders, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}

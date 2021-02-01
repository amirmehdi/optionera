package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.EmbeddedOption;
import com.gitlab.amirmehdi.domain.EmbeddedOption_;
import com.gitlab.amirmehdi.domain.Instrument_;
import com.gitlab.amirmehdi.repository.EmbeddedOptionRepository;
import com.gitlab.amirmehdi.service.dto.EmbeddedOptionCriteria;
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
 * Service for executing complex queries for {@link EmbeddedOption} entities in the database.
 * The main input is a {@link EmbeddedOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmbeddedOption} or a {@link Page} of {@link EmbeddedOption} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmbeddedOptionQueryService extends QueryService<EmbeddedOption> {

    private final Logger log = LoggerFactory.getLogger(EmbeddedOptionQueryService.class);

    private final EmbeddedOptionRepository embeddedOptionRepository;

    public EmbeddedOptionQueryService(EmbeddedOptionRepository embeddedOptionRepository) {
        this.embeddedOptionRepository = embeddedOptionRepository;
    }

    /**
     * Return a {@link List} of {@link EmbeddedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmbeddedOption> findByCriteria(EmbeddedOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmbeddedOption> specification = createSpecification(criteria);
        return embeddedOptionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmbeddedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmbeddedOption> findByCriteria(EmbeddedOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmbeddedOption> specification = createSpecification(criteria);
        return embeddedOptionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmbeddedOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmbeddedOption> specification = createSpecification(criteria);
        return embeddedOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link EmbeddedOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmbeddedOption> createSpecification(EmbeddedOptionCriteria criteria) {
        Specification<EmbeddedOption> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmbeddedOption_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EmbeddedOption_.name));
            }
            if (criteria.getIsin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsin(), EmbeddedOption_.isin));
            }
            if (criteria.getExpDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpDate(), EmbeddedOption_.expDate));
            }
            if (criteria.getStrikePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStrikePrice(), EmbeddedOption_.strikePrice));
            }
            if (criteria.getTseId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTseId(), EmbeddedOption_.tseId));
            }
            if (criteria.getUnderlyingInstrumentId() != null) {
                specification = specification.and(buildSpecification(criteria.getUnderlyingInstrumentId(),
                    root -> root.join(EmbeddedOption_.underlyingInstrument, JoinType.LEFT).get(Instrument_.isin)));
            }
        }
        return specification;
    }
}

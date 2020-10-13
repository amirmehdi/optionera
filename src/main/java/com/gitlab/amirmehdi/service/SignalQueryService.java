package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Order_;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.domain.Signal_;
import com.gitlab.amirmehdi.repository.SignalRepository;
import com.gitlab.amirmehdi.service.dto.SignalCriteria;
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
 * Service for executing complex queries for {@link Signal} entities in the database.
 * The main input is a {@link SignalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Signal} or a {@link Page} of {@link Signal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SignalQueryService extends QueryService<Signal> {

    private final Logger log = LoggerFactory.getLogger(SignalQueryService.class);

    private final SignalRepository signalRepository;

    public SignalQueryService(SignalRepository signalRepository) {
        this.signalRepository = signalRepository;
    }

    /**
     * Return a {@link List} of {@link Signal} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Signal> findByCriteria(SignalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Signal> specification = createSpecification(criteria);
        return signalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Signal} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Signal> findByCriteria(SignalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Signal> specification = createSpecification(criteria);
        return signalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SignalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Signal> specification = createSpecification(criteria);
        return signalRepository.count(specification);
    }

    /**
     * Function to convert {@link SignalCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Signal> createSpecification(SignalCriteria criteria) {
        Specification<Signal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Signal_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Signal_.type));
            }
            if (criteria.getIsin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsin(), Signal_.isin));
            }
            if (criteria.getLast() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLast(), Signal_.last));
            }
            if (criteria.getTradeVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeVolume(), Signal_.tradeVolume));
            }
            if (criteria.getBidVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBidVolume(), Signal_.bidVolume));
            }
            if (criteria.getBidPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBidPrice(), Signal_.bidPrice));
            }
            if (criteria.getAskPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAskPrice(), Signal_.askPrice));
            }
            if (criteria.getAskVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAskVolume(), Signal_.askVolume));
            }
            if (criteria.getBaseInstrumentLast() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBaseInstrumentLast(), Signal_.baseInstrumentLast));
            }
//            if (criteria.getCreatedAt() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Signal_.createdAt));
//            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(Signal_.orders, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}

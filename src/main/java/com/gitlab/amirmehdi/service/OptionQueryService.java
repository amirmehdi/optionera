package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Instrument_;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.Option_;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.OptionCriteria;
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
 * Service for executing complex queries for {@link Option} entities in the database.
 * The main input is a {@link OptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Option} or a {@link Page} of {@link Option} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionQueryService extends QueryService<Option> {

    private final Logger log = LoggerFactory.getLogger(OptionQueryService.class);

    private final OptionRepository optionRepository;

    public OptionQueryService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * Return a {@link List} of {@link Option} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Option> findByCriteria(OptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Option} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Option> findByCriteria(OptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.count(specification);
    }

    /**
     * Function to convert {@link OptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Option> createSpecification(OptionCriteria criteria) {
        Specification<Option> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Option_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Option_.name));
            }
            if (criteria.getCallIsin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCallIsin(), Option_.callIsin));
            }
            if (criteria.getPutIsin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPutIsin(), Option_.putIsin));
            }
            if (criteria.getExpDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpDate(), Option_.expDate));
            }
            if (criteria.getStrikePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStrikePrice(), Option_.strikePrice));
            }
            if (criteria.getContractSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractSize(), Option_.contractSize));
            }
            if (criteria.getCallInTheMoney() != null) {
                specification = specification.and(buildSpecification(criteria.getCallInTheMoney(), Option_.callInTheMoney));
            }
            if (criteria.getCallBreakEven() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallBreakEven(), Option_.callBreakEven));
            }
            if (criteria.getPutBreakEven() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPutBreakEven(), Option_.putBreakEven));
            }
            if (criteria.getCallAskToBS() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallAskToBS(), Option_.callAskToBS));
            }
            if (criteria.getPutAskToBS() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPutAskToBS(), Option_.putAskToBS));
            }
            if (criteria.getCallLeverage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallLeverage(), Option_.callLeverage));
            }
            if (criteria.getPutLeverage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPutLeverage(), Option_.putLeverage));
            }
            if (criteria.getCallHedge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallHedge(), Option_.callHedge));
            }
            if (criteria.getCallIndifference() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallIndifference(), Option_.callIndifference));
            }
            if (criteria.getCallGain() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallGain(), Option_.callGain));
            }
            if (criteria.getCallGainMonthly() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallGainMonthly(), Option_.callGainMonthly));
            }
            if (criteria.getCallMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCallMargin(), Option_.callMargin));
            }
            if (criteria.getPutMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPutMargin(), Option_.putMargin));
            }
            if (criteria.getInstrumentId() != null) {
                specification = specification.and(buildSpecification(criteria.getInstrumentId(),
                    root -> root.join(Option_.instrument, JoinType.LEFT).get(Instrument_.isin)));
            }
        }
        return specification;
    }
}

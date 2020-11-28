package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Board;
import com.gitlab.amirmehdi.domain.Board_;
import com.gitlab.amirmehdi.repository.BoardRepository;
import com.gitlab.amirmehdi.service.dto.BoardCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for {@link Board} entities in the database.
 * The main input is a {@link BoardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Board} or a {@link Page} of {@link Board} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoardQueryService extends QueryService<Board> {

    private final Logger log = LoggerFactory.getLogger(BoardQueryService.class);

    private final BoardRepository boardRepository;

    public BoardQueryService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Return a {@link List} of {@link Board} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Board> findByCriteria(BoardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Board> specification = createSpecification(criteria);
        return boardRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Board} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Board> findByCriteria(BoardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Board> specification = createSpecification(criteria);
        return boardRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Board> specification = createSpecification(criteria);
        return boardRepository.count(specification);
    }

    /**
     * Function to convert {@link BoardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Board> createSpecification(BoardCriteria criteria) {
        Specification<Board> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getIsin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsin(), Board_.isin));
            }
            if (criteria.getLast() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLast(), Board_.last));
            }
            if (criteria.getClose() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClose(), Board_.close));
            }
            if (criteria.getFirst() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFirst(), Board_.first));
            }
            if (criteria.getLow() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLow(), Board_.low));
            }
            if (criteria.getHigh() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHigh(), Board_.high));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), Board_.min));
            }
            if (criteria.getMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax(), Board_.max));
            }
            if (criteria.getTradeCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeCount(), Board_.tradeCount));
            }
            if (criteria.getTradeVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeVolume(), Board_.tradeVolume));
            }
            if (criteria.getTradeValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeValue(), Board_.tradeValue));
            }
            if (criteria.getAskPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAskPrice(), Board_.askPrice));
            }
            if (criteria.getAskVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAskVolume(), Board_.askVolume));
            }
            if (criteria.getBidPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBidPrice(), Board_.bidPrice));
            }
            if (criteria.getBidVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBidVolume(), Board_.bidVolume));
            }
            if (criteria.getIndividualBuyVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndividualBuyVolume(), Board_.individualBuyVolume));
            }
            if (criteria.getIndividualSellVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndividualSellVolume(), Board_.individualSellVolume));
            }
            if (criteria.getLegalBuyVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLegalBuyVolume(), Board_.legalBuyVolume));
            }
            if (criteria.getLegalSellVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLegalSellVolume(), Board_.legalSellVolume));
            }
            if (criteria.getReferencePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReferencePrice(), Board_.referencePrice));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Board_.state));
            }
        }
        return specification;
    }
}

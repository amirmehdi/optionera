package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.Portfolio;
import com.gitlab.amirmehdi.repository.PortfolioRepository;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Portfolio}.
 */
@Service
@Transactional
public class PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    private final PortfolioRepository portfolioRepository;
    private final Market market;

    public PortfolioService(PortfolioRepository portfolioRepository, Market market) {
        this.portfolioRepository = portfolioRepository;
        this.market = market;
    }

    /**
     * Save a portfolio.
     *
     * @param portfolio the entity to save.
     * @return the persisted entity.
     */
    public Portfolio save(Portfolio portfolio) {
        log.debug("Request to save Portfolio : {}", portfolio);
        StockWatch stockWatch = market.getStockWatch(portfolio.getIsin());
        portfolio.setClosePrice(stockWatch.getClosing());
        portfolio.setLastPrice(stockWatch.getLast());
        return portfolioRepository.save(portfolio);
    }

    /**
     * Get all the portfolios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Portfolio> findAll(Pageable pageable) {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll(pageable);
    }

    /**
     * Get one portfolio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Portfolio> findOne(AssetCompositeKey id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findById(id);
    }

}

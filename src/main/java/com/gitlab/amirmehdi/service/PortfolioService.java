package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.Portfolio;
import com.gitlab.amirmehdi.repository.PortfolioRepository;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
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

    @Scheduled(cron = "0 31 12 * * *")
    public void updatePortfolioPrices() {
        StopWatch stopWatch = new StopWatch("updatePortfolioPrices");
        stopWatch.start();
        List<Portfolio> todayPortfolios = portfolioRepository.findAllByDate(LocalDate.now());
        todayPortfolios.forEach(portfolio -> {
            StockWatch stockWatch = market.getStockWatch(portfolio.getIsin());
            if (stockWatch == null) {
                return;
            }
            portfolio.setLastPrice(stockWatch.getLast());
            portfolio.setClosingPrice(stockWatch.getClosing());
        });
        portfolioRepository.saveAll(todayPortfolios);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

}

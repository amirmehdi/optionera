package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.OpenInterest;
import com.gitlab.amirmehdi.repository.OpenInterestRepository;
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
 * Service Implementation for managing {@link OpenInterest}.
 */
@Service
@Transactional
public class OpenInterestService {

    private final Logger log = LoggerFactory.getLogger(OpenInterestService.class);

    private final OpenInterestRepository openInterestRepository;
    private final Market market;

    public OpenInterestService(OpenInterestRepository openInterestRepository, Market market) {
        this.openInterestRepository = openInterestRepository;
        this.market = market;
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


    @Scheduled(cron = "0 31 12 * * *")
    public void updateOpenInterestPrices() {
        StopWatch stopWatch = new StopWatch("updateOpenInterestPrices");
        stopWatch.start();
        List<OpenInterest> todayOpenInterests = openInterestRepository.findAllByDate(LocalDate.now());
        todayOpenInterests.forEach(openInterest -> {
            StockWatch stockWatch = market.getStockWatch(openInterest.getIsin());
            if (stockWatch == null) {
                return;
            }
            openInterest.setLastPrice(stockWatch.getLast());
            openInterest.setClosingPrice(stockWatch.getClosing());
        });
        openInterestRepository.saveAll(todayOpenInterests);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }
}

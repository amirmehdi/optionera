package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Option}.
 */
@Service
@Transactional
public class OptionService {

    public static final double RISK_FREE = 0.35;
    private final Logger log = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;
    private final Market market;

    public OptionService(OptionRepository optionRepository, Market market) {
        this.optionRepository = optionRepository;
        this.market = market;
    }

    /**
     * Save a option.
     *
     * @param option the entity to save.
     * @return the persisted entity.
     */
    public Option save(Option option) {
        log.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    public void saveAll(List<Option> options) {
        log.debug("Request to saveAll Options : {}", options);
        optionRepository.saveAll(options);
    }

    @Transactional(readOnly = true)
    public List<Option> findAll() {
        log.debug("Request to get all Options");
        return optionRepository.findAll();
    }

    /**
     * Get all the options.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Option> findAll(Pageable pageable) {
        log.debug("Request to get all Options");
        return optionRepository.findAll(pageable);
    }

    /**
     * Get one option by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Option> findOne(Long id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }

    /**
     * Delete the option by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }

    public Optional<Option> findByCallIsinOrPutIsin(String isin) {
        log.debug("Request to get Option by call or put isin : {}", isin);
        return optionRepository.findByCallIsinOrPutIsin(isin);
    }

    public Optional<Option> findByCallIsinAndPutIsin(String callIsin, String putIsin) {
        return optionRepository.findByCallIsinAndPutIsin(callIsin, putIsin);
    }

    public void updateParams(List<StockWatch> stockWatches) {
        log.debug("updateParams for isins :{}", stockWatches.stream().map(StockWatch::getIsin).collect(Collectors.toList()));
        Map<String, StockWatch> map = stockWatches.stream()
            .collect(Collectors.toMap(StockWatch::getIsin, stockWatch -> stockWatch));

        List<Option> options = optionRepository.findAll();
        options
            .parallelStream()
            .forEach(option -> {
                updateParams(
                    option, map.get(option.getInstrument().getIsin())
                    , market.getBidAsk(option.getCallIsin())
                    , market.getBidAsk(option.getPutIsin()));
                optionRepository.save(option);
            });
    }

    private void updateParams(Option option, StockWatch stockWatch, BidAsk callBidAsk, BidAsk putBidAsk) {
        OptionStats optionStats = new OptionStats().option(option)
            .baseStockWatch(stockWatch)
            .callBidAsk(callBidAsk.getBestBidAsk())
            .putBidAsk(putBidAsk.getBestBidAsk());

        option
            .callAskToBS(optionStats.getCallAskPriceToBS())
            .putAskToBS(optionStats.getPutAskPriceToBS())
            .callBreakEven(optionStats.getCallBreakEven())
            .putBreakEven(optionStats.getPutBreakEven())
            .callLeverage(optionStats.getCallLeverage())
            .putLeverage(optionStats.getPutLeverage())
            .callInTheMoney(optionStats.getCallInTheMoney());
    }
}

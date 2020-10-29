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

import java.time.LocalDate;
import java.util.ArrayList;
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
        return optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now());
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
        return optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now(), pageable);
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
            });
    }

    public void updateParams(String isin) {
        List<Option> options = optionRepository.findAllByInstrumentIsin(isin);
        StockWatch stockWatch = market.getStockWatch(isin);
        options
            .forEach(option -> updateParams(
                option
                , stockWatch
                , market.getBidAsk(option.getCallIsin())
                , market.getBidAsk(option.getPutIsin())));
    }

    private void updateParams(Option option, StockWatch stockWatch, BidAsk callBidAsk, BidAsk putBidAsk) {
        OptionStats optionStats = new OptionStats()
            .option(option)
            .baseStockWatch(stockWatch)
            .callBidAsk(callBidAsk.getBestBidAsk())
            .putBidAsk(putBidAsk.getBestBidAsk());
        updateOption(optionStats);
    }

    public void updateOption(OptionStats optionStats) {
        optionStats.getOption()
            .callAskToBS(optionStats.getCallAskPriceToBS())
            .putAskToBS(optionStats.getPutAskPriceToBS())
            .callBreakEven(optionStats.getCallBreakEven())
            .putBreakEven(optionStats.getPutBreakEven())
            .callLeverage(optionStats.getCallLeverage())
            .putLeverage(optionStats.getPutLeverage())
            .callInTheMoney(optionStats.getCallInTheMoney())
            .callMargin(optionStats.getCallMargin())
            .putMargin(optionStats.getPutMargin());
        optionRepository.save(optionStats.getOption());
    }

    public Page<Option> findAllOptionsWithoutTseIds(Pageable pageable) {
        return optionRepository.findAllByCallTseIdIsNullOrPutTseIdIsNull(pageable);
    }

    public void updateTseId(String isin, String name, String tseId) {
        if (isin.startsWith("IRO9")) {
            optionRepository.updateCallTseId(isin, name.substring(1), tseId);
        } else {
            optionRepository.updatePutTseId(isin, name.substring(1), tseId);
        }
    }

    public void deleteAllExpiredOption() {
        optionRepository.deleteAllByExpDateBefore(LocalDate.now());
    }

    public List<Option> findAllOptionsByLocalDateAndCallInTheMoney(LocalDate localdate, boolean callInTheMoney) {
        return optionRepository.findAllByExpDateAndCallInTheMoney(localdate, callInTheMoney);
    }

    public List<String> findAllCallAndPutIsins() {
        List<String> isins = new ArrayList<>();
        for (Object[] isin : optionRepository.findAllCallAndPutIsins()) {
            if (isin[0] == null || !((String) isin[0]).isEmpty()) {
                isins.add((String) isin[0]);
            }
            if (isin[1] == null || !((String) isin[1]).isEmpty()) {
                isins.add((String) isin[1]);
            }
        }
        return isins;
    }
}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.BoardRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private final BoardRepository boardRepository;
    private final InstrumentService instrumentService;
    private final Market market;

    public OptionService(OptionRepository optionRepository, BoardRepository boardRepository, InstrumentService instrumentService, Market market) {
        this.optionRepository = optionRepository;
        this.boardRepository = boardRepository;
        this.instrumentService = instrumentService;
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

    public void updateOption(List<String> isins) {
        isins.forEach(this::updateOption);
    }

    public void updateOption(String optionIsin) {
        Option option = findByCallIsinOrPutIsin(optionIsin).orElse(null);
        if (option == null) return;
        StockWatch stockWatch = market.getStockWatch(option.getInstrument().getIsin());
        updateParams(option, stockWatch);
    }

    private void updateParams(Option option, StockWatch stockWatch) {
        OptionStats optionStats = new OptionStats()
            .option(option)
            .baseStockWatch(stockWatch)
            .callBidAsk(market.getBidAsk(option.getCallIsin()).getBestBidAsk())
            .putBidAsk(market.getBidAsk(option.getPutIsin()).getBestBidAsk())
            .callStockWatch(market.getStockWatch(option.getCallIsin()))
            .putStockWatch(market.getStockWatch(option.getPutIsin()));
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
            .callHedge(optionStats.getCallHedge())
            .callIndifference(optionStats.getCallIndifference())
            .callGain(optionStats.getCallGain())
            .callGainMonthly(optionStats.getCallGainMonthly())
            .callMargin(optionStats.getCallMargin())
            .putMargin(optionStats.getPutMargin())
            .callTradeVolume(optionStats.getCallStockWatch().getTradeVolume())
            .putTradeVolume(optionStats.getPutStockWatch().getTradeVolume());
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

    @Transactional
    public void deleteAllExpiredOption() {
        boardRepository.deleteAllByExpDateBefore();
        optionRepository.deleteAllByExpDateBefore();
    }

    public List<Option> findAllOptionsByLocalDateAndCallInTheMoney(LocalDate localdate, boolean callInTheMoney) {
        return optionRepository.findAllByExpDateAndCallInTheMoney(localdate, callInTheMoney);
    }

    public List<String> findAllCallAndPutIsins() {
        List<Object[]> allCallAndPutIsins = optionRepository.findAllCallAndPutIsins();
        return getCallIsinAndPutIsin(allCallAndPutIsins);
    }

    public List<String> findAllCallIsins() {
        List<Object[]> allCallAndPutIsins = optionRepository.findAllCallIsins();
        return getCallIsinAndPutIsin(allCallAndPutIsins);
    }

    public List<String> findAllCallAndPutIsinsByInstrumentIsin(String isin) {
        List<Object[]> allCallAndPutIsins = optionRepository.findAllCallAndPutIsinsByInstrumentIsin(isin);
        return getCallIsinAndPutIsin(allCallAndPutIsins);
    }

    private List<String> getCallIsinAndPutIsin(List<Object[]> allCallAndPutIsins) {
        List<String> isins = new ArrayList<>();
        for (Object[] isin : allCallAndPutIsins) {
            if (isin.length > 0 && !((String) isin[0]).isEmpty()) {
                isins.add((String) isin[0]);
            }
            if (isin.length > 1 && !((String) isin[1]).isEmpty()) {
                isins.add((String) isin[1]);
            }
        }
        return isins;
    }

    public List<String> getCallAndBaseIsins() {
        List<String> isins = findAllCallIsins();
        List<Instrument> instruments = instrumentService.findAll();
        isins.addAll(instruments.stream().map(Instrument::getIsin).collect(Collectors.toList()));
        return isins;
    }

    public List<List<String>> getPartitionedOptions() {
        List<Option> options = optionRepository.findAll(Sort.by(Sort.Order.desc("instrument.isin"), Sort.Order.desc("id")));
        List<List<String>> lists = new ArrayList<>();
        options.stream().collect(Collectors.groupingBy(option -> option.getInstrument().getIsin()))
            .forEach((s, options1) -> {
                List<String> isins = new ArrayList<>();
                isins.add(s);
                isins.addAll(options1.stream().map(Option::getCallIsin).collect(Collectors.toList()));
                lists.add(isins);
            });
        return lists;
    }


}

package com.gitlab.amirmehdi.service.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.service.*;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OmidCrawler implements MarketUpdater {
    private final Market market;
    private final OptionService optionService;
    private final OmidRLCConsumer omidRLCConsumer;
    private final BoardService boardService;
    private final InstrumentService instrumentService;
    private final OptionStatsService optionStatsService;
    private final StrategyService strategyService;
    private final ApplicationProperties properties;

    public OmidCrawler(Market market, OptionService optionService, OmidRLCConsumer omidRLCConsumer, BoardService boardService, InstrumentService instrumentService, OptionStatsService optionStatsService, StrategyService strategyService, ApplicationProperties properties) {
        this.market = market;
        this.optionService = optionService;
        this.omidRLCConsumer = omidRLCConsumer;
        this.boardService = boardService;
        this.instrumentService = instrumentService;
        this.optionStatsService = optionStatsService;
        this.strategyService = strategyService;
        this.properties = properties;
    }


    public void arbitrageOptionsUpdater() {
        Map<String, Long> callIsins = optionService.findAllOptionsByLocalDateAndCallInTheMoney(LocalDate.now(), true)
            .stream()
            .collect(Collectors.toMap(Option::getCallIsin, Option::getId));
        if (!callIsins.isEmpty()) {
            try {
                omidRLCConsumer.getBulkBidAsk(new ArrayList<>(callIsins.keySet()))
                    .whenComplete((bidAsks, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        } else {
                            log.debug("update bidask in arbitrageOptionsUpdater");
                            market.saveAllBidAsk(bidAsks);
                            for (BidAsk bidAsk : bidAsks) {
                                optionService.updateOption(bidAsk.getIsin());
                            }
                            strategyService.run("ExerciseOptionsStrategy");
                        }
                    });
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        }
    }

    @Override
    public void boardUpdater(@Nullable List<String> isins) {
        if (isins == null) {
            isins = optionService.getCallAndBaseIsins();
        }
        List<List<String>> partition = Lists.partition(isins, properties.getCrawler().getOmidChunk());
        for (List<String> instruments : partition) {
            try {
                omidRLCConsumer.getBulkBidAsk(instruments).whenComplete((bidAsks, throwable) -> {
                    if (throwable != null) {
                        log.error("omid get bidask got error {}", throwable.toString());
                        if (!(throwable instanceof ResourceAccessException)) {
                            throwable.printStackTrace();
                        }
                    } else {
                        market.saveAllBidAsk(bidAsks);
                    }
                });
                omidRLCConsumer.getBulkStockWatch(instruments).whenComplete((stockWatches, throwable) -> {
                    if (throwable != null) {
                        log.error("omid get stockwatch got error {}", throwable.toString());
                        if (!(throwable instanceof ResourceAccessException)) {
                            throwable.printStackTrace();
                        }
                    } else {
                        StopWatch watch = new StopWatch("save omid response");
                        watch.start("redis");
                        market.saveAllStockWatch(stockWatches);
                        watch.stop();
                        watch.start("board");
                        boardService.updateBoardForIsins(instruments);
                        watch.stop();
                        /*watch.start("updateOption");
                        optionService.updateOption(instruments);
                        watch.stop();*/
                        log.debug(watch.prettyPrint());
                    }
                });
            } catch (Exception e) {
                log.error(e);
            }
        }
    }


    public void clientsInfoUpdater() {
        List<String> isins = instrumentService.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList());
        isins.addAll(optionService.findAllCallAndPutIsins());
        List<List<String>> partition = Lists.partition(isins, 50);
        for (List<String> strings : partition) {
            try {
                omidRLCConsumer.getBulkClientsInfo(strings).whenComplete((clientsInfos, throwable) -> {
                    if (throwable != null) {
                        if (!(throwable instanceof ResourceAccessException)) {
                            throwable.printStackTrace();
                        }
                    } else {
                        log.debug("update clientsInfos option stat {}", strings.size());
                        market.saveAllClientsInfo(clientsInfos);
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void instrumentUpdater() {

    }


    public void updateTseIds() {
        StopWatch stopwatch = new StopWatch("update tseIds");
        stopwatch.start();
        while (true) {
            Page<Option> optionPage = optionService.findAllOptionsWithoutTseIds(PageRequest.of(0, 20));
            List<String> isins = new ArrayList<>();
            for (Option option : optionPage.getContent()) {
                isins.add(option.getCallIsin());
                isins.add(option.getPutIsin());
            }
            try {
                for (com.gitlab.amirmehdi.service.dto.core.Instrument instrument : omidRLCConsumer.getInstruments(isins)) {
                    optionService.updateTseId(instrument.getId(), instrument.getName(), instrument.getTseId());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (optionPage.getContent().size() < 20) {
                break;
            }
        }
        stopwatch.stop();
        log.info(stopwatch.prettyPrint());
    }
}

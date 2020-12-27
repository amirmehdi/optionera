package com.gitlab.amirmehdi.service.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.service.*;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.ImmutableTag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OmidCrawler implements MarketUpdater {
    private final Market market;
    private final OptionService optionService;
    private final OmidRLCConsumer omidRLCConsumer;
    private final MetricService metricService;
    private final BoardService boardService;
    private final InstrumentService instrumentService;
    private final OptionStatsService optionStatsService;
    private final StrategyService strategyService;

    private final AtomicLong bidAskSuccessCount = new AtomicLong(0);
    private final AtomicLong bidAskErrorCount = new AtomicLong(0);
    private final AtomicLong stockWatchSuccessCount = new AtomicLong(0);
    private final AtomicLong stockWatchErrorCount = new AtomicLong(0);
    private final AtomicLong clientsInfoSuccessCount = new AtomicLong(0);
    private final AtomicLong clientsInfoErrorCount = new AtomicLong(0);
    private Date firstUpdate;
    private Date bidAskLastUpdate;
    private Date stockWatchLastUpdate;
    private Date clientsInfoFirstUpdate;
    private Date clientsInfoLastUpdate;

    public OmidCrawler(Market market, OptionService optionService, OmidRLCConsumer omidRLCConsumer, MetricService metricService, BoardService boardService, InstrumentService instrumentService, OptionStatsService optionStatsService, StrategyService strategyService) {
        this.market = market;
        this.optionService = optionService;
        this.omidRLCConsumer = omidRLCConsumer;
        this.metricService = metricService;
        this.boardService = boardService;
        this.instrumentService = instrumentService;
        this.optionStatsService = optionStatsService;
        this.strategyService = strategyService;
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
                                optionStatsService.findOne(callIsins.get(bidAsk.getIsin())).ifPresent(optionService::updateOption);
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
    public void boardUpdater() {
        if (bidAskLastUpdate != null && stockWatchLastUpdate != null) {
            metricService.reportMetric("crawler.updater.bidask.seconds", new ImmutableTag("status", "all"), (bidAskLastUpdate.getTime() - firstUpdate.getTime()) / 1000);
            metricService.reportMetric("crawler.updater.bidask.count", new ImmutableTag("status", "success"), bidAskSuccessCount.get());
            metricService.reportMetric("crawler.updater.bidask.count", new ImmutableTag("status", "error"), bidAskErrorCount.get());
            metricService.reportMetric("crawler.updater.stockwatch.seconds", new ImmutableTag("status", "all"), (stockWatchLastUpdate.getTime() - firstUpdate.getTime()) / 1000);
            metricService.reportMetric("crawler.updater.stockwatch.count", new ImmutableTag("status", "success"), stockWatchSuccessCount.get());
            metricService.reportMetric("crawler.updater.stockwatch.count", new ImmutableTag("status", "error"), stockWatchErrorCount.get());

        }
        firstUpdate = new Date();
        bidAskSuccessCount.set(0);
        stockWatchSuccessCount.set(0);
        bidAskErrorCount.set(0);
        stockWatchErrorCount.set(0);
        updateOptionsMarket();
        instrumentHasNotOptionUpdater();
    }

    private void updateOptionsMarket() {
        optionService.findAll()
            .stream()
            .collect(Collectors.groupingBy(option -> option.getInstrument().getIsin()))
            .forEach((s, optionStats) -> {
                StockWatch stockWatch = market.getStockWatch(s);
                if (stockWatch != null && stockWatch.getState() != null && !stockWatch.getState().equals("A")) {
                    if (Math.abs((new Date().getTime() - stockWatch.getDateTime().getTime()) / 1000) < 60) {
                        return;
                    }
                }
                List<String> optionsIsin = new ArrayList<>();
                optionsIsin.add(s);
                for (Option option : optionStats) {
                    optionsIsin.add(option.getCallIsin());
                    optionsIsin.add(option.getPutIsin());
                }
                try {
                    omidRLCConsumer.getBulkBidAsk(optionsIsin).whenComplete((bidAsks, throwable) -> {
                        if (throwable != null) {
                            if (!(throwable instanceof ResourceAccessException)) {
                                throwable.printStackTrace();
                            }
                            bidAskErrorCount.incrementAndGet();
                        } else {
                            log.debug("update bidask option stat {}", s);
                            market.saveAllBidAsk(bidAsks);
                            bidAskSuccessCount.incrementAndGet();
                            bidAskLastUpdate = new Date();
                        }
                    });
                    omidRLCConsumer.getBulkStockWatch(optionsIsin).whenComplete((stockWatches, throwable) -> {
                        if (throwable != null) {
                            if (!(throwable instanceof ResourceAccessException)) {
                                throwable.printStackTrace();
                            }
                            stockWatchErrorCount.incrementAndGet();
                        } else {
                            log.debug("update stockwatch option stat {}", s);
                            market.saveAllStockWatch(stockWatches);
                            boardService.updateAllBoard(s);
                            optionService.updateParams(s);
                            stockWatchSuccessCount.incrementAndGet();
                            stockWatchLastUpdate = new Date();
                        }
                    });
                } catch (Exception e) {
                    log.error(e);
                }
            });
    }

    public void instrumentHasNotOptionUpdater() {
        List<String> isins = instrumentService.findAllInstrumentHasNotOption().stream().map(Instrument::getIsin).collect(Collectors.toList());
        try {
            omidRLCConsumer.getBulkBidAsk(isins).whenComplete((bidAsks, throwable) -> {
                if (throwable != null) {
                    if (!(throwable instanceof ResourceAccessException)) {
                        throwable.printStackTrace();
                    }
                    bidAskErrorCount.incrementAndGet();
                } else {
                    log.debug("update bidask instruments {}", isins);
                    market.saveAllBidAsk(bidAsks);
                    bidAskSuccessCount.incrementAndGet();
//                    bidAskLastUpdate = new Date();
                }
            });
            omidRLCConsumer.getBulkStockWatch(isins).whenComplete((stockWatches, throwable) -> {
                if (throwable != null) {
                    if (!(throwable instanceof ResourceAccessException)) {
                        throwable.printStackTrace();
                    }
                    stockWatchErrorCount.incrementAndGet();
                } else {
                    log.debug("update stockwatch instruments {}", isins);
                    market.saveAllStockWatch(stockWatches);
                    stockWatchSuccessCount.incrementAndGet();
//                    stockWatchLastUpdate = new Date();
                }
            });
        } catch (Exception e) {
            log.error(e);
        }
    }


    public void clientsInfoUpdater() {
        if (clientsInfoLastUpdate != null) {
            metricService.reportMetric("crawler.updater.clientsInfo.seconds", new ImmutableTag("status", "all"), (clientsInfoLastUpdate.getTime() - clientsInfoFirstUpdate.getTime()) / 1000);
            metricService.reportMetric("crawler.updater.clientsInfo.count", new ImmutableTag("status", "success"), clientsInfoSuccessCount.get());
            metricService.reportMetric("crawler.updater.clientsInfo.count", new ImmutableTag("status", "error"), clientsInfoErrorCount.get());

        }
        clientsInfoFirstUpdate = new Date();
        clientsInfoSuccessCount.set(0);
        clientsInfoErrorCount.set(0);
        List<String> isins = instrumentService.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList());
        isins.addAll(optionService.findAllCallAndPutIsins());

        List<List<String>> partition = Lists.partition(isins, 50);
        updateClientsInfos(partition);
    }

    @Override
    public void instrumentUpdater() {

    }

    private void updateClientsInfos(List<List<String>> partition) {
        for (List<String> strings : partition) {
            try {
                omidRLCConsumer.getBulkClientsInfo(strings).whenComplete((clientsInfos, throwable) -> {
                    if (throwable != null) {
                        if (!(throwable instanceof ResourceAccessException)) {
                            throwable.printStackTrace();
                        }
                        clientsInfoErrorCount.incrementAndGet();
                    } else {
                        log.debug("update clientsInfos option stat {}", strings.size());
                        market.saveAllClientsInfo(clientsInfos);
                        clientsInfoSuccessCount.incrementAndGet();
                        clientsInfoLastUpdate = new Date();
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
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

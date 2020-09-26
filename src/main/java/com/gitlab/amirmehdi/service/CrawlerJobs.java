package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.service.dto.tsemodels.BDatum;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionResponse;
import com.gitlab.amirmehdi.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class CrawlerJobs implements CommandLineRunner {
    private final RestTemplate restTemplate;
    private final OptionRepository optionRepository;
    private final InstrumentService instrumentRepository;
    private final OptionStatsService optionStatsService;
    private final OmidRLCConsumer omidRLCConsumer;
    private final Market market;


    public CrawlerJobs(RestTemplate restTemplate, OptionRepository optionRepository, InstrumentService instrumentRepository, OptionStatsService optionStatsService, OmidRLCConsumer omidRLCConsumer, Market market) {
        this.restTemplate = restTemplate;
        this.optionRepository = optionRepository;
        this.instrumentRepository = instrumentRepository;
        this.optionStatsService = optionStatsService;
        this.omidRLCConsumer = omidRLCConsumer;
        this.market = market;
    }


    public void marketUpdater() throws Exception {

        StopWatch stopWatch = new StopWatch("market listener");
        stopWatch.start("updateInstrumentMarket");
        updateInstrumentMarket();
        stopWatch.stop();

        stopWatch.start("updateOptionStats");
        updateOptionStats();
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
    }

    private void updateOptionStats() {
        optionStatsService.findAll()
            .stream()
            .collect(Collectors.groupingBy(optionStats -> optionStats.getOption().getInstrument().getIsin()))
            .forEach((s, optionStats) -> {
                StockWatch stockWatch = market.getStockWatch(s);
                if (stockWatch == null || stockWatch.getState() == null || !stockWatch.getIsin().equals("A")) {
                    return;
                }
                List<String> optionsIsin = new ArrayList<>();
                for (OptionStats optionStat : optionStats) {
                    optionsIsin.add(optionStat.getOption().getCallIsin());
                    optionsIsin.add(optionStat.getOption().getPutIsin());
                }
                try {
                    omidRLCConsumer.getBulkBidAsk(optionsIsin).whenCompleteAsync((bidAsks, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        } else {
                            optionStatsService.saveAllBidAsk(bidAsks);
                        }
                    });
                    omidRLCConsumer.getBulkStockWatch(optionsIsin).whenCompleteAsync((stockWatches, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        } else {
                            optionStatsService.saveAllStockWatch(stockWatches);
                        }
                    });
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
    }

    private void updateInstrumentMarket() throws JsonProcessingException {
        List<String> instruments = instrumentRepository.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList());
        omidRLCConsumer.getBulkStockWatch(instruments).whenCompleteAsync((stockWatches, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            } else {
                market.saveAllStockWatch(stockWatches);
                Map<String, StockWatch> map = stockWatches.stream()
                    .collect(Collectors.toMap(StockWatch::getIsin, stockWatch -> stockWatch));
                optionStatsService.findAll().parallelStream().forEach(optionStats -> {
                    optionStats.setBaseStockWatch(map.get(optionStats.getOption().getInstrument().getIsin()));
                    optionStatsService.save(optionStats);
                });
            }
        });
        omidRLCConsumer.getBulkBidAsk(instruments).whenCompleteAsync((bidAsks, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            } else {
                market.saveAllBidAsk(bidAsks);
                Map<String, BidAsk> map = bidAsks.stream()
                    .collect(Collectors.toMap(BidAsk::getIsin, bidAsk -> bidAsk));
                optionStatsService.findAll().parallelStream().forEach(optionStats -> {
                    optionStats.setBaseBidAsk(map.get(optionStats.getOption().getInstrument().getIsin()));
                    optionStatsService.save(optionStats);
                });
            }
        });
    }


    /**
     * redis initialization or update openInterest and settlementPrice fields
     */
    public void openInterestUpdater() {
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        optionResponse.getBData()
            .stream()
            .collect(Collectors.groupingBy(BDatum::getDarayi))
            .forEach((darayi, bData) -> {
                Optional<Instrument> optionalInstrument = instrumentRepository.findOneByName(darayi);
                if (!optionalInstrument.isPresent()) {
                    log.warn("instrument {} not found", darayi);
                    return;
                }
                Instrument instrument = optionalInstrument.get();
                List<OptionStats> optionStats = bData
                    .stream()
                    .map(bDatum -> {
                        OptionStats optionStat = optionStatsService.findById(bDatum.getI(), bDatum.getI2());
                        if (optionStat == null) {
                            Optional<Option> optionalOption = optionRepository.findByCallIsinAndPutIsin(bDatum.getI(), bDatum.getI2());
                            if (!optionalOption.isPresent()) {
                                log.warn("findByCallIsinAndPutIsin does not exist for {} ,{} {} ", bDatum.getVal().get(0).getV(), bDatum.getI(), bDatum.getI());
                                return null;
                            }
                            optionStat = new OptionStats()
                                .option(optionalOption.get())
                                .callStockWatch(OptionStockWatch.builder()
                                    .settlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(2).getV())))
                                    .openInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(3).getV())))
                                    .tradeVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(4).getV())))
                                    .tradeValue(Long.parseLong(numberNormalizer(bDatum.getVal().get(5).getV())))
                                    .tradeCount(Integer.parseInt(numberNormalizer(bDatum.getVal().get(6).getV())))
                                    .last(Integer.parseInt(numberNormalizer(bDatum.getVal().get(7).getV())))
                                    .build())
                                .callBidAsk(BestBidAsk.builder()
                                    .askVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(8).getV())))
                                    .askPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(9).getV())))
                                    .bidPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(10).getV())))
                                    .bidVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(11).getV())))
                                    .build())
                                .putStockWatch(OptionStockWatch.builder()
                                    .settlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(25).getV())))
                                    .openInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(24).getV())))
                                    .tradeVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(23).getV())))
                                    .tradeValue(Long.parseLong(numberNormalizer(bDatum.getVal().get(22).getV())))
                                    .tradeCount(Integer.parseInt(numberNormalizer(bDatum.getVal().get(21).getV())))
                                    .last(Integer.parseInt(numberNormalizer(bDatum.getVal().get(20).getV())))
                                    .build())
                                .putBidAsk(BestBidAsk.builder()
                                    .askVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(16).getV())))
                                    .askPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(17).getV())))
                                    .bidPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(18).getV())))
                                    .bidVolume(Integer.parseInt(numberNormalizer(bDatum.getVal().get(19).getV())))
                                    .build());
                        } else {
                            optionStat.getOption().setInstrument(instrument);
                            optionStat.getCallStockWatch().setSettlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(2).getV())));
                            optionStat.getCallStockWatch().setOpenInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(3).getV())));

                            optionStat.getPutStockWatch().setSettlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(25).getV())));
                            optionStat.getPutStockWatch().setOpenInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(24).getV())));
                        }
                        return optionStat;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                optionStatsService.saveAll(optionStats);
            });
    }

    public void optionCrawler() {
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        List<Option> options = optionResponse.getBData()
            .stream()
            .map(bDatum -> {
                String[] dateInfo = bDatum.getVal().get(1).getV().split("-")[2].split("/");

                Optional<Instrument> instrument = instrumentRepository.findOneByName(bDatum.getDarayi());
                if (!instrument.isPresent()) {
                    log.warn("instrument {} not found", bDatum.getDarayi());
                    return null;
                }
                String callIsin = bDatum.getI() == null ? "" : bDatum.getI();
                String putIsin = bDatum.getI2() == null ? "" : bDatum.getI2();
                Optional<Option> option = optionRepository.findByCallIsinAndPutIsin(callIsin, putIsin);
                if (option.isPresent()) {
                    return option.get()
                        .instrument(instrument.get())
                        .name(bDatum.getVal().get(0).getV().substring(1))
                        .callIsin(callIsin)
                        .putIsin(putIsin)
                        .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                        .strikePrice(Integer.valueOf(bDatum.getVal().get(1).getV().split("-")[1]))
                        .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV().replace(",", "")));
                }
                return new Option()
                    .instrument(instrument.get())
                    .name(bDatum.getVal().get(0).getV().substring(1))
                    .callIsin(callIsin)
                    .putIsin(putIsin)
                    .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                    .strikePrice(Integer.valueOf(bDatum.getVal().get(1).getV().split("-")[1]))
                    .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV().replace(",", "")));
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        optionRepository.saveAll(options);
    }

    private String numberNormalizer(String number) {
        if (number == null) {
            return "0";
        }
        if (number.isEmpty())
            return "0";
        number = number.replace(" ", "").replace(",", "").replace(".0", "");
        int zeroNum = 0;
        if (number.contains("K")) {
            zeroNum += 3;
            number = number.replace("K", "");
        } else if (number.contains("M")) {
            zeroNum += 6;
            number = number.replace("M", "");
        } else if (number.contains("B")) {
            zeroNum += 9;
            number = number.replace("B", "");
        }
        if (number.contains(".")) {
            zeroNum--;
            number = number.replace(".", "");
        }
        StringBuilder numberBuilder = new StringBuilder(number);
        for (int i = 0; i < zeroNum; i++) {
            numberBuilder.append("0");
        }
        number = numberBuilder.toString();
        return number;
    }

    private String yearNormalizer(String year) {
        if (year.length() == 4)
            return year;
        if (year.length() == 2) {
            if (Integer.parseInt(year) > 10) {
                return "13" + year;
            } else
                return "14" + year;
        }
        throw new IllegalArgumentException(year + " undefined");
    }

    @Override
    public void run(String... args) {
        try {
            optionCrawler();
            openInterestUpdater();
            marketUpdater();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

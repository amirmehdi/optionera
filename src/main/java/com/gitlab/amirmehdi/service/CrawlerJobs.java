package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.service.dto.tsemodels.BDatum;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionResponse;
import com.gitlab.amirmehdi.util.DateUtil;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Log4j2
public class CrawlerJobs {
    private final RestTemplate restTemplate;
    private final OptionRepository optionRepository;
    private final InstrumentRepository instrumentRepository;
    private final OptionStatsService optionStatsService;
    private final OmidRLCConsumer omidRLCConsumer;
    private final Market market;

    @Value("${application.market-time-check}")
    private boolean marketTimeCheck;


    public CrawlerJobs(RestTemplate restTemplate, OptionRepository optionRepository, InstrumentRepository instrumentRepository, OptionStatsService optionStatsService, OmidRLCConsumer omidRLCConsumer, Market market) {
        this.restTemplate = restTemplate;
        this.optionRepository = optionRepository;
        this.instrumentRepository = instrumentRepository;
        this.optionStatsService = optionStatsService;
        this.omidRLCConsumer = omidRLCConsumer;
        this.market = market;
    }

    //    @Retryable(
//        value = {Exception.class},
//        maxAttempts = 5,
//        backoff = @Backoff(delay = 5 * 1000))
    @Scheduled(fixedRateString = "${application.market-updater-fixed-rate}")
    public void marketUpdater() throws Exception {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen(new Date()))
            return;

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

    @Scheduled(fixedRateString = "${application.open-interest-updater-fixed-rate}")
    public void openInterestUpdater() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen(new Date()))
            return;
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        optionResponse.getBData()
            .stream()
            .collect(Collectors.groupingBy(BDatum::getDarayi))
            .forEach((darayi, bData) -> {
                Instrument instrument = instrumentRepository.findOneByName(darayi);
                List<OptionStats> optionStats = bData
                    .stream()
                    .map(bDatum -> {
                        String[] dateInfo = bDatum.getVal().get(1).getV().split("-")[2].split("/");
                        LocalDate expDate = DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2])));
                        int strike = Integer.parseInt(bDatum.getVal().get(1).getV().split("-")[1]);
                        OptionStats optionStat = optionStatsService.findById(instrument.getIsin(), strike, expDate);
                        if (optionStat == null) {
                            optionStat = OptionStats.builder()
                                .option(new Option()
                                    .instrument(instrument)
                                    .name(bDatum.getVal().get(0).getV().substring(1))
                                    .callIsin(bDatum.getI())
                                    .putIsin(bDatum.getI2())
                                    .expDate(expDate)
                                    .strikePrice(strike)
                                    .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV().replace(",", ""))))
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
                                    .build())
                                .build();
                        } else {
                            optionStat.getCallStockWatch().setSettlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(2).getV())));
                            optionStat.getCallStockWatch().setOpenInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(3).getV())));

                            optionStat.getPutStockWatch().setSettlementPrice(Integer.parseInt(numberNormalizer(bDatum.getVal().get(25).getV())));
                            optionStat.getPutStockWatch().setOpenInterest(Integer.parseInt(numberNormalizer(bDatum.getVal().get(24).getV())));
                        }
                        return optionStat;
                    })
                    .collect(Collectors.toList());
                optionStatsService.saveAll(optionStats);
            });
    }

    @Scheduled(cron = "0 0 8 * * *")
    @PostConstruct
    public void optionCrawler() {
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        List<Option> options = optionResponse.getBData()
            .stream()
            .map(bDatum -> {
                String[] dateInfo = bDatum.getVal().get(1).getV().split("-")[2].split("/");

                return new Option()
                    .instrument(instrumentRepository.findOneByName(bDatum.getDarayi()))
                    .name(bDatum.getVal().get(0).getV().substring(1))
                    .callIsin(bDatum.getI())
                    .putIsin(bDatum.getI2())
                    .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                    .strikePrice(Integer.valueOf(bDatum.getVal().get(1).getV().split("-")[1]))
                    .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV().replace(",", "")));
            })
            .collect(Collectors.toList());

        options.forEach(option ->
            optionRepository.findByInstrumentIsinAndStrikePriceAndExpDate(option.getInstrument().getIsin(), option.getStrikePrice(), option.getExpDate())
                .ifPresent(option1 -> option.setId(option1.getId())));
        optionRepository.saveAll(options);
    }

    private String numberNormalizer(String number) {
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
        for (int i = 0; i < zeroNum; i++) {
            number += "0";
        }
        return number;
    }

    private String yearNormalizer(String year) {
        if (year.length() == 4)
            return year;
        if (year.length() == 2) {
            if (Integer.valueOf(year) > 10) {
                return "13" + year;
            } else
                return "14" + year;
        }
        throw new IllegalArgumentException(year + " undefined");
    }
}

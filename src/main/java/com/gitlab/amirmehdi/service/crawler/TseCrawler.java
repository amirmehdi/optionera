package com.gitlab.amirmehdi.service.crawler;

import com.gitlab.amirmehdi.domain.EmbeddedOption;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.service.*;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.service.dto.tsemodels.EmbeddedOptionResponse;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionData;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionResponse;
import com.gitlab.amirmehdi.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
public class TseCrawler {
    private final RestTemplate restTemplate;
    private final OptionService optionService;
    private final EmbeddedOptionService embeddedOptionService;
    private final InstrumentService instrumentService;
    private final Market market;


    public TseCrawler(@Qualifier("trustedRestTemplate") RestTemplate restTemplate, OptionService optionService, OptionStatsService optionStatsService, EmbeddedOptionService embeddedOptionService, InstrumentService instrumentService, Market market) {
        this.restTemplate = restTemplate;
        this.optionService = optionService;
        this.embeddedOptionService = embeddedOptionService;
        this.instrumentService = instrumentService;
        this.market = market;
    }

    /**
     * redis initialization or update openInterest and settlementPrice fields
     */
    public void openInterestUpdater() {
        OptionResponse optionResponse = null;
        try {
            optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        } catch (Exception e) {
            log.error("req to tse.ir got error, ", e);
            return;
        }

        optionResponse.getBData()
            .forEach(optionData -> {
                String callIsin = optionData.getI() == null ? "" : optionData.getI();
                String putIsin = optionData.getI2() == null ? "" : optionData.getI2();

                Optional<Option> optionalOption = optionService.findByCallIsinAndPutIsin(callIsin, putIsin);
                if (!optionalOption.isPresent()) {
                    log.warn("findByCallIsinAndPutIsin does not exist for {} ,{} {} ", optionData.getVal().get(0).getV(), callIsin, putIsin);
                    return;
                }
                Option option = optionalOption.get();
                updateOpenInterest(optionData, option.getCallIsin(), 2, 3, 4, 5, 6, 7);
                updateOpenInterest(optionData, option.getPutIsin(), 25, 24, 23, 22, 21, 20);
            });
    }

    private void updateOpenInterest(OptionData optionData, String isin, int i, int i2, int i3, int i4, int i5, int i6) {
        StockWatch callStockWatch = market.getStockWatch(isin);
        if (callStockWatch == null) {
            callStockWatch = StockWatch.builder()
                .isin(isin)
                .settlementPrice(Integer.parseInt(numberNormalizer(optionData.getVal().get(i).getV())))
                .openInterest(Integer.parseInt(numberNormalizer(optionData.getVal().get(i2).getV())))
                .tradeVolume(Integer.parseInt(numberNormalizer(optionData.getVal().get(i3).getV())))
                .tradeValue(Long.parseLong(numberNormalizer(optionData.getVal().get(i4).getV())))
                .tradesCount(Integer.parseInt(numberNormalizer(optionData.getVal().get(i5).getV())))
                .last(Integer.parseInt(numberNormalizer(optionData.getVal().get(i6).getV())))
                .build();
        } else {
            callStockWatch.setSettlementPrice(Integer.parseInt(numberNormalizer(optionData.getVal().get(i).getV())));
            callStockWatch.setOpenInterest(Integer.parseInt(numberNormalizer(optionData.getVal().get(i2).getV())));
        }
        market.saveStockWatch(callStockWatch);
    }

    public void embeddedOptionCrawler() {
        embeddedOptionService.deleteAllExpiredOption();

        EmbeddedOptionResponse response = null;
        try {
            response = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_3.json?1612183632060?1612183632060", EmbeddedOptionResponse.class).getBody();
        } catch (Exception e) {
            log.error("req to tse.ir for embedded options got error, ", e);
            return;
        }

        List<EmbeddedOption> embeddedOptions = response
            .getBData().stream()
            .map(data -> {
                String[] fullName = data.getName().split("-");
                String[] dateInfo;
                dateInfo = fullName[fullName.length - 1].split("/");
                String underlyingAssetName = fullName[0].replace("اختيارف ت ", "").replaceAll("\\d", "");
                LocalDate expDate = DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2])));
                Optional<Instrument> instrument = instrumentService.findOneByName(underlyingAssetName);
                if (!instrument.isPresent()) {
                    log.warn("instrument {} not found", underlyingAssetName);
                    return null;
                }
                Optional<EmbeddedOption> embeddedOptionOptional = embeddedOptionService.findByIsin(data.getI());
                EmbeddedOption embeddedOption;
                embeddedOption = embeddedOptionOptional.orElseGet(EmbeddedOption::new);
                embeddedOption
                    .isin(data.getI())
                    .name(data.getNamad())
                    .strikePrice(Integer.parseInt(numberNormalizer(data.getVal().get(3).getV())))
                    .underlyingInstrument(instrument.get())
                    .expDate(expDate);
                return embeddedOption;
            })
            .filter(Objects::nonNull)
            .filter(option -> !option.getExpDate().isBefore(LocalDate.now()))
            .collect(Collectors.toList());
        embeddedOptionService.saveAll(embeddedOptions);
    }

    public void optionCrawler() {
        optionService.deleteAllExpiredOption();
        StopWatch stopWatch = new StopWatch("option crawler");

        stopWatch.start("tse request");
        OptionResponse optionResponse = null;
        try {
            optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        } catch (Exception e) {
            log.error("req to tse.ir got error, ", e);
            return;
        }
        stopWatch.stop();

        stopWatch.start("mapping");
        List<Option> options = optionResponse.getBData()
            .stream()
            .map(optionData -> {
                String[] dateInfo = optionData.getVal().get(1).getV().split("-")[2].split("/");

                Optional<Instrument> instrument = instrumentService.findOneByName(optionData.getDarayi());
                if (!instrument.isPresent()) {
                    log.warn("instrument {} not found", optionData.getDarayi());
                    return null;
                }
                String callIsin = optionData.getI() == null ? "" : optionData.getI();
                String putIsin = optionData.getI2() == null ? "" : optionData.getI2();
                Optional<Option> option = optionService.findByCallIsinAndPutIsin(callIsin, putIsin);
                if (option.isPresent()) {
                    return option.get()
                        .instrument(instrument.get())
                        .name(optionData.getVal().get(0).getV().substring(1))
                        .callIsin(callIsin)
                        .putIsin(putIsin)
                        .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                        .strikePrice(Integer.valueOf(optionData.getVal().get(1).getV().split("-")[1]))
                        .contractSize(Integer.valueOf(optionData.getVal().get(12).getV().replace(",", "")));
                }
                return new Option()
                    .instrument(instrument.get())
                    .name(optionData.getVal().get(0).getV().substring(1))
                    .callIsin(callIsin)
                    .putIsin(putIsin)
                    .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                    .strikePrice(Integer.valueOf(optionData.getVal().get(1).getV().split("-")[1]))
                    .contractSize(Integer.valueOf(optionData.getVal().get(12).getV().replace(",", "")));
            })
            .filter(Objects::nonNull)
            .filter(option -> !option.getExpDate().isBefore(LocalDate.now()))
            .collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("saving");
        optionService.saveAll(options);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
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

}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionResponse;
import com.gitlab.amirmehdi.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CrawlerJobs {
    private final RestTemplate restTemplate;
    private final OptionRepository optionRepository;
    private final InstrumentRepository instrumentRepository;
    private final OptionStatService optionStatService;


    public CrawlerJobs(RestTemplate restTemplate, OptionRepository optionRepository, InstrumentRepository instrumentRepository, OptionStatService optionStatService) {
        this.restTemplate = restTemplate;
        this.optionRepository = optionRepository;
        this.instrumentRepository = instrumentRepository;
        this.optionStatService = optionStatService;
    }


    @Scheduled(fixedRate = 60000)
    public void realTimeDataUpdater() {
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        List<OptionStats> stats = optionResponse.getBData()
            .stream()
            .map(bDatum -> {
                String[] dateInfo = bDatum.getVal().get(1).getV().split("-")[2].split("/");
                return OptionStats.builder()
                    .option(new Option()
                        .instrument(instrumentRepository.findOneByName(bDatum.getDarayi()))
                        .name(bDatum.getVal().get(0).getV().substring(1))
                        .callIsin(bDatum.getI())
                        .putIsin(bDatum.getI2())
                        .expDate(DateUtil.convertToLocalDateViaInstant(DateUtil.jalaliToGregorian(Integer.parseInt(yearNormalizer(dateInfo[0])), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]))))
                        .strikePrice(Integer.valueOf(bDatum.getVal().get(1).getV().split("-")[1]))
                        .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV().replace(",", ""))))
                    .callStockWatch(OptionStockWatch.builder()
                        .settlementPrice(bDatum.getVal)
                        .openInterest()
                        .tradeVolume()
                        .tradeValue()
                        .tradeCount()
                        .last()
                        .close()
                        .build())
                    .callBidAsk(BestBidAsk.builder()
                        .askVolume()
                        .askPrice()
                        .bidPrice()
                        .bidVolume()
                        .build())
                    .putStockWatch(OptionStockWatch.builder()
                        .settlementPrice()
                        .openInterest()
                        .tradeVolume()
                        .tradeValue()
                        .tradeCount()
                        .last()
                        .close()
                        .build())
                    .putBidAsk(BestBidAsk.builder()
                        .askVolume()
                        .askPrice()
                        .bidPrice()
                        .bidVolume()
                        .build())
                    .build();
            })
            .collect(Collectors.toList());

        optionStatService.saveAll(stats);
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

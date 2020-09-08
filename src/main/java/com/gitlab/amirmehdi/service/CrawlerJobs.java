package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.service.dto.tsemodels.OptionResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class CrawlerJobs {
    private final RestTemplate restTemplate;
    private final OptionService optionService;
    private final InstrumentRepository instrumentRepository;


    public CrawlerJobs(RestTemplate restTemplate, OptionService optionService, InstrumentRepository instrumentRepository) {
        this.restTemplate = restTemplate;
        this.optionService = optionService;
        this.instrumentRepository = instrumentRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void optionCrawler() {
        OptionResponse optionResponse = restTemplate.getForEntity("https://tse.ir/json/MarketWatch/data_7.json?1599569952420?1599569952420", OptionResponse.class).getBody();
        optionResponse.getBData()
            .stream()
            .filter(bDatum -> bDatum.getDarayi().equals("وتجارت"))
            .map(bDatum -> {
                Option option = new Option()
                    .instrument(instrumentRepository.findOneByName(bDatum.getDarayi()))
                    .name(bDatum.getVal().get(0).getV().substring(1))
                    .callIsin(bDatum.getI())
                    .putIsin(bDatum.getI2())
                    .expDate(LocalDate.parse(bDatum.getVal().get(1).getV().split("-")[2],DateTimeFormatter.ISO_LOCAL_DATE))
                    .strikePrice(Integer.valueOf(bDatum.getVal().get(1).getV().split("-")[1]))
                    .contractSize(Integer.valueOf(bDatum.getVal().get(12).getV()));
//                optionService.findOne(option.getInstrument().getId(), option.getExpDate(),option.getStrikePrice());
                return option;
            });
    }
}

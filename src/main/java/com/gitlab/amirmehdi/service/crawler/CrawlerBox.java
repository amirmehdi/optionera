package com.gitlab.amirmehdi.service.crawler;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.service.InstrumentService;
import com.gitlab.amirmehdi.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerBox {
    private final ApplicationProperties applicationProperties;
    private final InstrumentService instrumentService;
    private final OptionService optionService;
    private final HashMap<String, MarketUpdater> marketUpdaters = new HashMap<>();

    public CrawlerBox(ApplicationProperties applicationProperties, InstrumentService instrumentService, OptionService optionService) {
        this.applicationProperties = applicationProperties;
        this.instrumentService = instrumentService;
        this.optionService = optionService;
    }

    @Autowired
    public void setMarketUpdaters(List<MarketUpdater> updaters) {
        for (MarketUpdater updater : updaters) {
            marketUpdaters.put(updater.getClass().getSimpleName(), updater);
        }
    }

    public MarketUpdater getMarketUpdater(String marketUpdater) {
        return marketUpdaters.get(marketUpdater);
    }

    public MarketUpdater getBestMarketUpdater() {
        return marketUpdaters.get(applicationProperties.getCrawler().getBestCrawler());
    }

    public void highAvailableBoardUpdater() {
        List<String> isins = optionService.findAllCallIsins();
        List<String> instrumentIds = instrumentService.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList());

        //TODO if market.getStockWatch(isin).state is not active -> not get options
        List<String> availableCrawler = Arrays.asList(applicationProperties.getCrawler().getAvailableCrawler().split(","));
        if (availableCrawler.stream().anyMatch(s -> s.contains("OmidCrawler"))) {
            marketUpdaters.get("OmidCrawler").boardUpdater(instrumentIds);
        } else {
            isins.addAll(instrumentIds);
        }

        for (String s : availableCrawler) {
            String[] conf = s.split(";");

            marketUpdaters.get(conf[0]).boardUpdater(isins.subList(Integer.parseInt(conf[1]), Math.min(Integer.parseInt(conf[2]), isins.size())));
        }
    }
}

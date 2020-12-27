package com.gitlab.amirmehdi.service.crawler;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CrawlerBox {
    private final ApplicationProperties applicationProperties;
    private final HashMap<String, MarketUpdater> marketUpdaters = new HashMap<>();

    public CrawlerBox(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Autowired
    public void setMarketUpdaters(List<MarketUpdater> updaters) {
        for (MarketUpdater updater : updaters) {
            marketUpdaters.put(updater.getClass().getSimpleName(), updater);
        }
    }

    public MarketUpdater getBestMarketUpdater(){
        return marketUpdaters.get(applicationProperties.getSchedule().getBestCrawler());
    }
}

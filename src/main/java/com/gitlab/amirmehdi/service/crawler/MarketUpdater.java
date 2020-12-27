package com.gitlab.amirmehdi.service.crawler;

public interface MarketUpdater {
    void arbitrageOptionsUpdater();

    void boardUpdater();

    void clientsInfoUpdater();

    void instrumentUpdater();
}

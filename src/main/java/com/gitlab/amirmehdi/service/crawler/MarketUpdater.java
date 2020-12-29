package com.gitlab.amirmehdi.service.crawler;

import javax.annotation.Nullable;
import java.util.List;

public interface MarketUpdater {
    void arbitrageOptionsUpdater();

    void boardUpdater(@Nullable List<String> isins);

    void clientsInfoUpdater();

    void instrumentUpdater();
}

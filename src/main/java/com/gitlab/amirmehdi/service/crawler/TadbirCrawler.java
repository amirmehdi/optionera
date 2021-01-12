package com.gitlab.amirmehdi.service.crawler;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.service.BoardService;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionService;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.service.dto.tadbirrlc.IndInstTradeResponse;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TadbirCrawler implements MarketUpdater {
    private final OptionService optionService;
    private final TadbirRlcConsumer rlcConsumer;
    private final TaskScheduler executor;
    private final Market market;
    private final BoardService boardService;
    private final ApplicationProperties applicationProperties;

    public TadbirCrawler(OptionService optionService, TadbirRlcConsumer rlcConsumer, TaskScheduler executor, Market market, BoardService boardService, ApplicationProperties applicationProperties) {
        this.optionService = optionService;
        this.rlcConsumer = rlcConsumer;
        this.executor = executor;
        this.market = market;
        this.boardService = boardService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void arbitrageOptionsUpdater() {
        List<String> callIsins = optionService
            .findAllOptionsByLocalDateAndCallInTheMoney(LocalDate.now(), true)
            .stream()
            .map(Option::getCallIsin)
            .collect(Collectors.toList());
        updateRlc(callIsins);
    }


    @Override
    public void boardUpdater(@Nullable List<String> isins) {
        if (isins == null) {
            isins = optionService.getCallAndBaseIsins();
        }
        List<List<String>> partition = Lists.partition(isins, applicationProperties.getCrawler().getTadbirChunk());
        for (int i = 0; i < partition.size(); i++) {
            int finalI = i;
            executor.schedule(() -> updateRlc(partition.get(finalI)),
                new Date()
                    .toInstant()
                    .plus(i, ChronoUnit.SECONDS));
        }
    }

    private void updateRlc(List<String> isins) {
        for (String isin : isins) {
            rlcConsumer.stockFutureInfoHandler(isin)
                .whenComplete((res, throwable) -> {
                    if (throwable != null) {
                        log.error("tadbir crawler get error: {} ", throwable.toString());
                        return;
                    }
                    StopWatch watch = new StopWatch("save tadbir response");
                    watch.start("redis");
                    StockWatch stockWatch = res.toStockWatch();
                    stockWatch.setCrawledDate(new Date());
                    BidAsk bidAsk = res.toBidAsk();
                    market.saveBidAsk(bidAsk);
                    market.saveStockWatch(stockWatch);
                    watch.stop();
                    watch.start("db");
                    boardService.save(isin);
                    // optionService.updateOption(isin);
                    watch.stop();
                    log.debug("{} finish {}", isin, watch.shortSummary());
                });
        }
    }

    @Override
    public void clientsInfoUpdater() {
        StopWatch stopWatch = new StopWatch("tadbir clientInfo updater");
        stopWatch.start("fetch isins");
        List<String> isins = optionService.getCallAndBaseIsins();
        stopWatch.stop();
        stopWatch.start("requests");
        isins.parallelStream().forEach(s -> {
            try {
                IndInstTradeResponse response = rlcConsumer.clientsInfoHandler(s);
                if (response != null && response.getIsin() != null)
                    market.saveClientsInfo(response.toClientsInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint());
    }

    @Override
    public void instrumentUpdater() {
        for (Option option : optionService.findAll()) {
            rlcConsumer.stockFutureInfoHandler(option.getCallIsin()).whenComplete((info, throwable) -> {
                option.name(info.getSymbolinfo().getEst().substring(1));
                option.setCallTseId(info.getSymbolinfo().getIc());
                optionService.save(option);
            });
        }
    }
}

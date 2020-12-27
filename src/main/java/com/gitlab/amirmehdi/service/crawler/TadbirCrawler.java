package com.gitlab.amirmehdi.service.crawler;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.service.BoardService;
import com.gitlab.amirmehdi.service.InstrumentService;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TadbirCrawler implements MarketUpdater {
    private final OptionService optionService;
    private final InstrumentService instrumentService;
    private final TadbirRlcConsumer rlcConsumer;
    private final TaskScheduler executor;
    private final Market market;
    private final BoardService boardService;
    private final ApplicationProperties applicationProperties;

    public TadbirCrawler(OptionService optionService, InstrumentService instrumentService, TadbirRlcConsumer rlcConsumer, TaskScheduler executor, Market market, BoardService boardService, ApplicationProperties applicationProperties) {
        this.optionService = optionService;
        this.instrumentService = instrumentService;
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
    public void boardUpdater() {
        List<String> isins = getCallAndBaseIsins();
        List<List<String>> partition = Lists.partition(isins, applicationProperties.getSchedule().getTadbirChunk());
        for (int i = 0; i < partition.size(); i++) {
            int finalI = i;
            executor.schedule(() -> updateRlc(partition.get(finalI)),
                new Date()
                    .toInstant()
                    .plus(i, ChronoUnit.SECONDS));
        }
    }

    private List<String> getCallAndBaseIsins() {
        List<String> isins = optionService.findAllCallIsins();
        List<Instrument> instruments = instrumentService.findAll();
        isins.addAll(instruments.stream().map(Instrument::getIsin).collect(Collectors.toList()));
        return isins;
    }

    private void updateRlc(List<String> isins) {
        for (String isin : isins) {
            rlcConsumer.stockFutureInfoHandler(isin)
                .whenComplete((res, throwable) -> {
                    StockWatch stockWatch = res.toStockWatch();
                    BidAsk bidAsk = res.toBidAsk();
                    market.saveBidAsk(bidAsk);
                    market.saveStockWatch(stockWatch);
                    optionService.updateOption(isin);
                    boardService.save(isin);
                    log.debug("{} finish", isin);
                });
        }
    }

    @Override
    public void clientsInfoUpdater() {
        StopWatch stopWatch = new StopWatch("tadbir clientInfo updater");
        stopWatch.start("fetch isins");
        List<String> isins = getCallAndBaseIsins();
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
        log.info(stopWatch.prettyPrint());
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

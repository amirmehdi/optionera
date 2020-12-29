package com.gitlab.amirmehdi.service.asa;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.service.BoardService;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionService;
import com.gitlab.amirmehdi.service.TokenService;
import com.gitlab.amirmehdi.service.crawler.MarketUpdater;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.ClientsInfo;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.Nullable;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
public class AsaCrawler implements MarketUpdater {
    private final OptionService optionService;
    private final AsaRLCConsumer rlcConsumer;
    private final TokenService tokenService;
    private final TaskScheduler executor;
    private final Market market;
    private final BoardService boardService;
    private final ApplicationProperties properties;

    public AsaCrawler(OptionService optionService, AsaRLCConsumer rlcConsumer, TokenService tokenService, TaskScheduler executor, Market market, BoardService boardService, ApplicationProperties properties) {
        this.optionService = optionService;
        this.rlcConsumer = rlcConsumer;
        this.tokenService = tokenService;
        this.executor = executor;
        this.market = market;
        this.boardService = boardService;
        this.properties = properties;
    }

    @Override
    public void arbitrageOptionsUpdater() {

    }

    @Override
    public void boardUpdater(@Nullable List<String> isins) {
        if (isins == null) {
            isins = optionService.getCallAndBaseIsins();
        }
        List<List<String>> partition = Lists.partition(isins, properties.getCrawler().getAsaChunk());
        for (int i = 0; i < partition.size(); i++) {
            int finalI = i;
            executor.schedule(() -> updateRlc(partition.get(finalI)),
                new Date()
                    .toInstant()
                    .plus(i, ChronoUnit.SECONDS));
        }
    }

    private void updateRlc(List<String> isins) {
        List<Token> availableTokens = tokenService.getAvailableTokens(Broker.AGAH);
        if (availableTokens.isEmpty()) log.error("agah token in not available");
        for (String isin : isins) {
            rlcConsumer.getInstrumentInfo(isin, availableTokens.get(ThreadLocalRandom.current().nextInt(availableTokens.size())).getToken())
                .whenComplete((res, throwable) -> {
                    if (throwable != null) {
                        log.error("asa crawler get error: {} ", throwable.toString());
                        if (throwable instanceof ResourceAccessException) {
                            //can sleep
                        } else if (throwable instanceof HttpStatusCodeException) {
                            if (((HttpStatusCodeException) throwable).getStatusCode().is3xxRedirection() || ((HttpStatusCodeException) throwable).getStatusCode().is4xxClientError()) {
                                //TODO login but once!
                            }
                        }
                        return;
                    }
                    StopWatch watch = new StopWatch("save asa response");
                    watch.start("redis");
                    StockWatch stockWatch = res.toStockWatch();
                    BidAsk bidAsk = res.toBidAsk();
                    ClientsInfo clientsInfo = res.toClientsInfo();
                    market.saveStockWatch(stockWatch);
                    market.saveClientsInfo(clientsInfo);
                    market.saveBidAsk(bidAsk);
                    watch.stop();
                    watch.start("db");
                    optionService.updateOption(isin);
                    boardService.save(isin);
                    watch.stop();
                    log.debug("{} finish {}", isin, watch.shortSummary());
                });
        }
    }


    @Override
    public void clientsInfoUpdater() {
        //TODO not needed!
    }

    @Override
    public void instrumentUpdater() {

    }
}

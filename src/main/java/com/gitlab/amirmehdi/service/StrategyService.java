package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.strategy.Strategy;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class StrategyService {
    private final TelegramMessageSender telegramMessageSender;

    private final TaskScheduler executor;

    @Value("${application.market-time-check}")
    private boolean marketTimeCheck;

    private HashMap<String, Strategy> strategies = new HashMap<>();


    public StrategyService(TelegramMessageSender telegramMessageSender, OptionRepository optionRepository, OptionStatsService optionStatsService, TaskScheduler executor) {
        this.telegramMessageSender = telegramMessageSender;
        this.executor = executor;
    }

    @Autowired
    public void setOrderSenders(List<Strategy> strategies) {
        for (Strategy strategy : strategies) {
            this.strategies.put(strategy.getClass().getSimpleName(), strategy);
            if (strategy.getCron() != null) {
                executor.schedule(() -> runSignalAndSendToTelegram(strategy, marketTimeCheck), new CronTrigger(strategy.getCron()));
            }
        }
    }

    public void run() {
        strategies
            .forEach((s, strategy) -> {
                try {
                    runSignalAndSendToTelegram(strategy, false);
                } catch (Exception e) {
                    log.error("strategy: {} got error ", s, e);
                }
            });
    }

    private void runSignalAndSendToTelegram(Strategy strategy, boolean marketTimeCheck) {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        Optional
            .of(strategy.getSignals())
            .ifPresent(s -> s.stream()
                .filter(Objects::nonNull)
                .forEach(telegramMessageSender::sendMessage));
    }
}

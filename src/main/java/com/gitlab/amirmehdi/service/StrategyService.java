package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.strategy.Strategy;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
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
            if (strategy.getCron()!=null){
                executor.schedule(() -> runSignalAndSendToTelegram(strategy,marketTimeCheck),new CronTrigger(strategy.getCron()));
            }
        }
    }

    public void run() {
        strategies.forEach((s, strategy) -> runSignalAndSendToTelegram(strategy,false));
    }

    private void runSignalAndSendToTelegram(Strategy strategy,boolean marketTimeCheck) {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        strategy.getSignals().stream().filter(Objects::nonNull).forEach(telegramMessageSender::sendMessage);
    }
}

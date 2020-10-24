package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.repository.SignalRepository;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.service.strategy.Strategy;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class StrategyService {
    private final TelegramMessageSender telegramMessageSender;
    private final SignalRepository signalRepository;
    private final OrderService orderService;
    private final OptionRepository optionRepository;

    private final TaskScheduler executor;
    private final HashMap<String, Strategy> strategies = new HashMap<>();
    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;
    @Value("${application.telegram.token}")
    private String apiToken;
    @Value("${application.telegram.privateChat}")
    private String privateChannelId;

    public StrategyService(TelegramMessageSender telegramMessageSender, SignalRepository signalRepository, OrderService orderService, OptionRepository optionRepository, TaskScheduler executor) {
        this.telegramMessageSender = telegramMessageSender;
        this.signalRepository = signalRepository;
        this.orderService = orderService;
        this.optionRepository = optionRepository;
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

    public void run(String strategy) {
        try {
            runSignalAndSendToTelegram(strategies.get(strategy), marketTimeCheck);
        } catch (Exception e) {
            log.error("strategy: {} got error ", strategy, e);
        }
    }

    private void runSignalAndSendToTelegram(Strategy strategy, boolean marketTimeCheck) {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        Optional
            .ofNullable(strategy.getSignals())
            .ifPresent(s -> {
                s.getCallSignals().forEach(signalRepository::save);
                String privateChatId = s.getPrivateChatId() == null ? privateChannelId : s.getPrivateChatId();

                if (StrategyResponse.SendOrderType.NEED_ALLOW.equals(s.getSendOrderType())) {
                    s.getCallSignals()
                        .stream()
                        .map(signal -> new TelegramMessageDto(apiToken
                            , privateChatId
                            , strategy.getMessageTemplateWithOrderLink(signal)))
                        .forEach(telegramMessageSender::sendMessage);
                }
                if (s.getPublicChatId() != null && !s.getPublicChatId().isEmpty()) {
                    s.getCallSignals()
                        .stream()
                        .map(signal -> new TelegramMessageDto(apiToken
                            , s.getPublicChatId()
                            , strategy.getMessageTemplate(signal.getIsin())))
                        .forEach(telegramMessageSender::sendMessage);
                }
            });
    }

    public List<Order> sendOrder(Signal signal) {
        if (strategies.containsKey(signal.getType())) {
            List<Order> orders = strategies.get(signal.getType()).getOrder(signal);
            if (orders == null) {
                return Collections.emptyList();
            }
            orders.forEach(orderService::send);
            return orders;
        }
        throw new RuntimeException("Strategy not active");
    }
}

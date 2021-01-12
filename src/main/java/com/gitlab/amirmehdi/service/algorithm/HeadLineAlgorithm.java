package com.gitlab.amirmehdi.service.algorithm;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OrderService;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class HeadLineAlgorithm {
    private final OrderService orderService;
    private final ApplicationProperties properties;
    private final Market market;
    private final TaskScheduler executor;

    public HeadLineAlgorithm(OrderService orderService, ApplicationProperties properties, Market market, TaskScheduler executor) {
        this.orderService = orderService;
        this.properties = properties;
        this.market = market;
        this.executor = executor;
    }


    @Scheduled(cron = "${application.headline.cron}")
    public void headLineOrder() {
        log.info("headLineOrder fired");
        retrieveHeadLineOrdersAndSend(properties.getHeadline().getSleep(), properties.getHeadline().getRepeat());
    }

    public void retrieveHeadLineOrdersAndSend(long sleep, int repeat) {
        List<Order> orders = orderService.findAllByState(OrderState.HEADLINE);
        orders.stream()
            .collect(Collectors.groupingBy(order -> order.getBourseCode().getId()))
            .forEach((aLong, orders1) -> {
                sendHeadLineOrdersPerBourseCode(orders1, sleep, repeat);
            });
    }

    private void sendHeadLineOrdersPerBourseCode(List<Order> orders, long sleep, int repeat) {
        for (int j = 0, ordersSize = orders.size(); j < ordersSize; j++) {
            Order order = orders.get(j);
            StockWatch stockWatch = market.getStockWatch(order.getIsin());
            int price = stockWatch == null ? order.getPrice() : stockWatch.getMax();
            int quantity = (int) (order.getBourseCode().getBuyingPower() * 0.99 / (ordersSize * price));
            List<String> sentIsins = new ArrayList<>();
            Instant marketOpen = new Date().toInstant().atZone(ZoneId.of("Asia/Tehran")).withHour(8)
                .withMinute(45)
                .withSecond(0).toInstant();
            log.info("market open is {}", marketOpen);
            scheduleOrder(order, price, quantity, sentIsins, marketOpen);
            for (int i = 0; i < repeat; i++) {
                scheduleOrder(order, price, quantity, sentIsins, new Date()
                    .toInstant()
                    .plus((i * sleep) + (j * sleep / ordersSize), ChronoUnit.MILLIS));
            }
        }
    }

    private void scheduleOrder(Order order, int price, int quantity, List<String> sentIsins, Instant instant) {
        executor.schedule(() -> {
            if (sentIsins.contains(order.getIsin())) {
                log.info("order is sent before: {}", order);
                return;
            }
            StopWatch stopWatch = new StopWatch("send order");
            stopWatch.start();
            Order res = orderService.sendOrder(
                new Order()
                    .isin(order.getIsin())
                    .price(price)
                    .quantity(quantity)
                    .validity(order.getValidity())
                    .side(order.getSide())
                    .bourseCode(order.getBourseCode()));
            stopWatch.stop();
            log.info(stopWatch.shortSummary());
            log.debug(res);
            if (res.getDescription() != null && res.getDescription().contains("-2006") && res.getState().equals(OrderState.ERROR)) {
                sentIsins.add(order.getIsin());
            }
        }, instant);
    }
}

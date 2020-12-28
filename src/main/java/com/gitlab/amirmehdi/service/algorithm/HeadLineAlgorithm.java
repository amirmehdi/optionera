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

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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
        List<Order> allByState = orderService.findAllByState(OrderState.HEADLINE);
        for (Order order : allByState) {
            StockWatch stockWatch = market.getStockWatch(order.getIsin());
            int price = stockWatch == null ? order.getPrice() : stockWatch.getMax();
            int quantity = (int) (order.getBourseCode().getBuyingPower() / (2 * price));
            for (int i = 0; i < properties.getHeadline().getRepeat(); i++) {
                executor.schedule(() -> orderService.sendOrder(
                    new Order()
                        .broker(order.getBroker())
                        .isin(order.getIsin())
                        .price(price)
                        .quantity(quantity)
                        .validity(order.getValidity())
                        .side(order.getSide())
                        .bourseCode(order.getBourseCode()))
                    , new Date()
                        .toInstant()
                        .plus(i * properties.getHeadline().getSleep(), ChronoUnit.MILLIS));
            }
        }
    }
}

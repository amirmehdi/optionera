package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.repository.OrderRepository;
import com.gitlab.amirmehdi.service.dto.sahra.exception.CodeException;
import com.gitlab.amirmehdi.service.sahra.SahraRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final TadbirService tadbirService;
    private final SahraRequestService sahraRequestService;
    private final List<OrderState> activeStates = Arrays.asList(OrderState.NONE, OrderState.ACTIVE, OrderState.PARTIALLY_EXECUTED);

    public OrderService(OrderRepository orderRepository, TadbirService tadbirService, SahraRequestService sahraRequestService) {
        this.orderRepository = orderRepository;
        this.tadbirService = tadbirService;
        this.sahraRequestService = sahraRequestService;
    }

    /**
     * Save a order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Order save(Order order) {

        log.debug("Request to save Order : {}", order);
        order = orderRepository.save(order);
        sendOrder(order);
        return order;
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
//        orderRepository.deleteById(id);
        cancelOrder(findOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public void sendOrder(Order order) {
        if (order.getId() == null || order.getId() == 0) {
            save(order);
        }
        if (Broker.FIROOZE_ASIA.equals(order.getBroker())) {
            try {
                sahraRequestService.sendOrder(order);
            } catch (CodeException e) {
                order.setState(OrderState.ERROR);
                order.setDescription(e.getCode() + " " + e.getDesc());
                orderRepository.save(order);
            }
        } else {
            tadbirService.sendOrder(order);
        }
    }

    public void cancelOrder(Order order) {
        if (order.getOmsId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "جواب از سمت کارگزاری نیامده");
        } else if (!OrderState.ACTIVE.equals(order.getState()) && !OrderState.NONE.equals(order.getState())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "سفارش فعال نیست");
        }
        if (Broker.FIROOZE_ASIA.equals(order.getBroker())) {
            sahraRequestService.cancelOrder(order);
        }
    }

    @Scheduled(cron = "0 0 17-20 * * *")
    public void cancelDayOrders() {
        List<Order> activeOrders = orderRepository.findAllByStateIn(activeStates);
        for (Order order : activeOrders) {
            order.setState(OrderState.CANCELLED);
            order.setDescription("سفارش بصورت خودکار لغو شد");
        }
        orderRepository.saveAll(activeOrders);
    }
}

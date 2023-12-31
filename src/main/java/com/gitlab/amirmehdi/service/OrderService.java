package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.OMS;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import com.gitlab.amirmehdi.repository.OrderRepository;
import com.gitlab.amirmehdi.service.dto.sahra.exception.CodeException;
import com.gitlab.amirmehdi.service.sahra.SahraRequestService;
import com.gitlab.amirmehdi.service.tadbir.TadbirService;
import com.gitlab.amirmehdi.web.rest.vm.IsinExecuted;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final BourseCodeRepository bourseCodeRepository;
    private final TadbirService tadbirService;
    private final SahraRequestService sahraRequestService;
    private final List<OrderState> activeStates = Arrays.asList(OrderState.NONE, OrderState.ACTIVE, OrderState.PARTIALLY_EXECUTED);

    public OrderService(OrderRepository orderRepository, BourseCodeRepository bourseCodeRepository, TadbirService tadbirService, SahraRequestService sahraRequestService) {
        this.orderRepository = orderRepository;
        this.bourseCodeRepository = bourseCodeRepository;
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
        order = sendOrder(order);
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

    public Order sendOrder(Order order) {
        if (order.getId() == null || order.getId() == 0) {
            order = orderRepository.save(order);
        }
        if (order.getState() == null || order.getState().equals(OrderState.NONE)) {
            // send order
            if (order.getBourseCode().getBroker() == null) {
                order.setBourseCode(bourseCodeRepository.findById(order.getBourseCode().getId()).get());
            }
            if (OMS.SAHRA.equals(order.getBourseCode().getBroker().oms)) {
                try {
                    sahraRequestService.sendOrder(order);
                } catch (CodeException e) {
                    order.setState(OrderState.ERROR);
                    order.setDescription(e.getCode() + " " + e.getDesc());
                    orderRepository.save(order);
                    return order;
                }
            } else {
                tadbirService.sendOrder(order);
            }
        }
        return order;
    }

    public void cancelOrder(Order order) {
        if (order.getOmsId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "جواب از سمت کارگزاری نیامده");
        } else if (!OrderState.ACTIVE.equals(order.getState()) && !OrderState.NONE.equals(order.getState())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "سفارش فعال نیست");
        }
        if (order.getBourseCode().getBroker() == null) {
            order.setBourseCode(bourseCodeRepository.findById(order.getBourseCode().getId()).get());
        }
        if (OMS.SAHRA.equals(order.getBourseCode().getBroker().oms)) {
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

    public List<IsinExecuted> getIsinExecuted() {
        List<IsinExecuted> isinExecuteds = new ArrayList<>();
        orderRepository.findAllByExecutedGreaterThanAndCreatedAtGreaterThan(0, DateUtils.truncate(new Date(), Calendar.DATE)).stream().collect(Collectors.groupingBy(Order::getIsin))
            .forEach((s, orders) -> {
                isinExecuteds.add(new IsinExecuted(s, orders.stream().mapToInt(Order::getExecuted).sum()));
            });
        return isinExecuteds;
    }

    public List<Order> findAllByState(OrderState orderState) {
        return orderRepository.findAllByState(orderState);
    }
}

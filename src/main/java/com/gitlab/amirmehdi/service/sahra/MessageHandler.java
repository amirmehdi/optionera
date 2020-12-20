package com.gitlab.amirmehdi.service.sahra;

import com.gitlab.amirmehdi.domain.OpenInterest;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Portfolio;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.repository.OrderRepository;
import com.gitlab.amirmehdi.service.OpenInterestService;
import com.gitlab.amirmehdi.service.PortfolioService;
import com.gitlab.amirmehdi.service.dto.sahra.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Log4j2
public class MessageHandler {
    private final OrderRepository orderRepository;
    private final PortfolioService portfolioService;
    private final OpenInterestService openInterestService;

    public MessageHandler(OrderRepository orderRepository, PortfolioService portfolioService, OpenInterestService openInterestService) {
        this.orderRepository = orderRepository;
        this.portfolioService = portfolioService;
        this.openInterestService = openInterestService;
    }

    public void handle(PollResponse pollResponse) {
        if (pollResponse != null && pollResponse.getM() != null)
            pollResponse.getM().forEach(this::handle);
    }

    public void handle(PollMessageResponse pollMessageResponse) {
        switch (pollMessageResponse.getMethod()) {
            case "initUI":
                // قدرت خرید
                ArrayList credit = (ArrayList) ((ArrayList) ((ArrayList) ((ArrayList) pollMessageResponse.getVal().get(0)).get(1)).get(0)).get(3);
                CreditInfoUpdate creditInfoUpdate1 = new CreditInfoUpdate(credit);
                // پورتفوی
                ArrayList<ArrayList> portfo = (ArrayList) ((ArrayList) ((ArrayList) pollMessageResponse.getVal().get(0)).get(1)).get(3);
                portfo.stream()
                    .forEach(s -> assetChangeHandler(new AssetData(s)));
                //سفارشات باز
                //((ArrayList) ((ArrayList) ((ArrayList) ((ArrayList) firstPollResponse.getM().get(0).getVal().get(0)).get(1)).get(2)).get(0)).toString()
                //    [1170000000364703, IRO1PKLJ0001, 1399/09/05 10:11:34, 4000, 12000, 0, 1, 1, null, 2, 1, 009845, false, 1, 0, 0, true, 48178176, 4000, null, 1, null]
                //موقعیت باز
                ArrayList<ArrayList> opens = (ArrayList) ((ArrayList) ((ArrayList) pollMessageResponse.getVal().get(0)).get(1)).get(8);
                opens.forEach(s -> positionChangeHandler(new PositionData(s)));
                break;
            case "marketMessageRecived":
                break;
            case "InstrumentFirstBestLimitChange":
                //val=[[IRO1BMLT0001, [1, 74, 5310, 3508475, 41, 5310, 2073603]]]
                break;
            case "InstrumentClosingPriceChange":
                //PollMessageResponse(hub=OmsClientHub, method=InstrumentClosingPriceChange, val=[[IRO9MAPN4141, 18, 130, 0.0, 2602, 3510, 3510, 0]])
                break;
            case "InstrumentTradePercentChage":
                //PollMessageResponse(hub=OmsClientHub, method=InstrumentTradePercentChage, val=[[IRO1MAPN0001, 1359, [492, 3730230, 645, 4543423, 4, 1285703, 3, 472510]]])
                break;
            case "InstrumentTrade":
                //[[IRO1PTAP0001, 14000, 12960, 0, 13620, 10:42:57, 6287, 35885122]]
                break;
            case "AssetPriceChange":
                //PollMessageResponse(hub=OmsClientHub, method=AssetPriceChange, val=[[IRO1PKOD0001, 7970]])
                break;
            case "OverallStatisticsChange":
                //[[1363271.6, 6162.63, 984040.0, 9.3223558858793E13, 9.229306083E9, 66]]
                break;
            case "creditInfoUpdate":
                //PollMessageResponse(hub=OmsClientHub, method=creditInfoUpdate, val=[[8877109, 255205989, 0, 246328880, 1, 1]])
                CreditInfoUpdate creditInfoUpdate = new CreditInfoUpdate((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                break;
            case "orderAdded":
                //PollMessageResponse(hub=OmsClientHub, method=orderAdded, val=[[1170000000364934, IRO9MAPN4141, 1399/09/05 11:21:55, 1, 2480, 0, 2, 1, null, 1, 1, 000000, false, 2, 0, 0, true, 9276665, 1, null, 1, null]])
                OrderData orderData = new OrderData((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                orderAddedHandler(orderData);
                break;
            case "orderStateChange":
                //PollMessageResponse(hub=OmsClientHub, method=orderStateChange, val=[[1170000000364932, 3, 003078, false, 1, 28169130, 1481, 1]])
                StateChangeData stateChangeData = new StateChangeData((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                stateChangeDataHandler(stateChangeData);
                break;
            case "orderEdited":
                    OrderEdit orderEdit = new OrderEdit((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                    orderEditHandler(orderEdit);
                break;
            case "orderError":
                    OrderError orderError = new OrderError((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                    orderErrorHandler(orderError);
                break;
            case "orderExecution":
                //PollMessageResponse(hub=OmsClientHub, method=orderExecution, val=[[1170000000364932, 1481, 3, 1, 0, 0, 1481, 18900, 28094805]])
                OrderExecution orderExecution = new OrderExecution((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                orderExecutionHandler(orderExecution);
                break;
            case "AssetChange":
                //PollMessageResponse(hub=OmsClientHub, method=AssetChange, val=[[IRO1MAPN0001, 32582, 19103, 18900]])
                AssetData assetData = new AssetData((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                assetChangeHandler(assetData);
                break;
            case "PositionChange":
                //PollMessageResponse(hub=OmsClientHub, method=PositionChange, val=[[IRO9MAPN4141, -1, 0, 0, 0, 0, 9272880, 1399/10/08, 1399/10/09]])
                PositionData positionData = new PositionData((ArrayList<Object>) pollMessageResponse.getVal().get(0));
                positionChangeHandler(positionData);
                break;
            default:
                //InstrumentStateChange
                log.warn("Unexpected value: {} {}", pollMessageResponse.getMethod(), pollMessageResponse.getVal());
        }
    }

    private void assetChangeHandler(AssetData assetData) {
        try {
            portfolioService.save(Portfolio.builder()
                .userId(Broker.FIROOZE_ASIA.name())
                .isin(assetData.getId())
                .quantity(assetData.getQuantity())
                .avgPrice(assetData.getAvgPrice())
                .date(LocalDate.now())
                .build());
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void positionChangeHandler(PositionData positionData) {
        try {
            openInterestService.save(OpenInterest.builder()
                .userId(Broker.FIROOZE_ASIA.name())
                .isin(positionData.getIsin())
                .date(LocalDate.now())
                .quantity(positionData.getAsset())
                .marginAmount(positionData.getMarginAmount())
                .build());
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void orderAddedHandler(OrderData message) {
        Order order;
        if (message.getExtraData() == null || message.getExtraData().equals("null") || message.getExtraData().isEmpty()) {
            log.info("message ( order ) is from other source : {}", message);
            order = new Order();
            order.setSide(message.getOrderSide().toOrderSide());
            order.setValidity(message.getValidity().toValidity());
            order.setIsin(message.getInstrumentId());
            order.setPrice(message.getPrice());
            order.setQuantity(message.getQuantity());
            order.setBroker(Broker.FIROOZE_ASIA);
        } else {
            Optional<Order> optionalOrder = orderRepository.findById(Long.valueOf(message.getExtraData()));
            if (!optionalOrder.isPresent()) {
                log.info("message ( order ) is from other source but have extra: {}", message);
                return;
            }
            order = optionalOrder.get();
        }
        order.setOmsId(message.getId());
        order.setExecuted(message.getExecutedQuantity());
        order.setState(message.getOrderStatus().toOrderState());
        if (message.getError() != null) {
            log.info("order got error: {} {}", order, message.getError());
        }
        orderRepository.save(order);
    }

    private void orderExecutionHandler(OrderExecution message) {
        Optional<Order> orderOptional = orderRepository.findByBrokerAndOmsId(Broker.FIROOZE_ASIA, message.getId());
        if (!orderOptional.isPresent()) {
            log.info("order not found : {}", message);
            return;
        }
        Order order = orderOptional.get();
        order.setState(message.getOrderStatus().toOrderState());
        order.setExecuted(message.getExecutedQuantity());
        orderRepository.save(order);
    }

    private void orderEditHandler(OrderEdit orderEdit) {
        Optional<Order> orderOptional = orderRepository.findByBrokerAndOmsId(Broker.FIROOZE_ASIA, orderEdit.getId());
        if (!orderOptional.isPresent()) {
            log.info("order not found : {}", orderEdit);
            return;
        }
        Order order = orderOptional.get();
        order.setQuantity(order.getQuantity());
        order.setPrice(orderEdit.getPrice());
        order.setDescription("سفارش ویرایش شد");
        order.setValidity(orderEdit.getValidity().toValidity());
        orderRepository.save(order);
    }

    private void stateChangeDataHandler(StateChangeData stateChangeData) {
        Optional<Order> orderOptional = orderRepository.findByBrokerAndOmsId(Broker.FIROOZE_ASIA, stateChangeData.getId());
        if (!orderOptional.isPresent()) {
            log.info("order not found : {}", stateChangeData);
            return;
        }
        Order order = orderOptional.get();
//        order.setExecuted(order.getQuantity() - stateChangeData.getRemain());
        order.setState(stateChangeData.getOrderStatus().toOrderState());
        orderRepository.save(order);
    }

    private void orderErrorHandler(OrderError orderError) {
        Optional<Order> orderOptional = orderRepository.findByBrokerAndOmsId(Broker.FIROOZE_ASIA, orderError.getStateChangeData().getId());
        if (!orderOptional.isPresent()) {
            log.info("order not found : {}", orderError);
            return;
        }
        Order order = orderOptional.get();
        order.setState(orderError.getStateChangeData().getOrderStatus().toOrderState());
        order.setDescription(orderError.getErrorMessage());
        orderRepository.save(order);
    }

}

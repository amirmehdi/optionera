package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import com.gitlab.amirmehdi.service.logic.AlgorithmBox;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AlgorithmRunnerService {
    private final AlgorithmService algorithmService;
    private final OrderService orderService;
    private final HashMap<String, AlgorithmBox> algorithms = new HashMap<>();
    private final TaskScheduler executor;
    @Value("${application.schedule.timeCheck}")
    private boolean marketTimeCheck;

    public AlgorithmRunnerService(AlgorithmService algorithmService, OrderService orderService, TaskScheduler executor) {
        this.algorithmService = algorithmService;
        this.orderService = orderService;
        this.executor = executor;
    }

    @Autowired
    public void setOrderSenders(List<AlgorithmBox> algorithms) {
        for (AlgorithmBox algorithm : algorithms) {
            this.algorithms.put(algorithm.getClass().getSimpleName(), algorithm);
            Trigger trigger = algorithm.getTrigger();
            if (trigger != null) {
                executor.schedule(() -> runAlgorithm(algorithm, marketTimeCheck, AlgorithmEvent.TRIGGER), trigger);
            }
        }
    }

    private void runAlgorithm(AlgorithmBox algorithmBox, boolean marketTimeCheck, AlgorithmEvent event) {
        if (marketTimeCheck && !MarketTimeUtil.isTradingOpen())
            return;
        for (AlgorithmDTO algorithm : algorithmService.getAllEnabledAlgorithmFromType(algorithmBox.getType())) {
            algorithmBox.onEvent(algorithm, event);
        }
    }

    public enum AlgorithmEvent {
        TRIGGER, ON_CREATE, MANUAL
    }
}

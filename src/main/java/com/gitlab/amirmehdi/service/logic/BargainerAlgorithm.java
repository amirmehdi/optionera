package com.gitlab.amirmehdi.service.logic;

import com.gitlab.amirmehdi.service.AlgorithmRunnerService;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

@Service
public class BargainerAlgorithm extends AlgorithmBox {
    @Override
    public Trigger getTrigger() {
        return null;
    }

    @Override
    public void onEvent(AlgorithmDTO algorithm, AlgorithmRunnerService.AlgorithmEvent event) {

    }
}

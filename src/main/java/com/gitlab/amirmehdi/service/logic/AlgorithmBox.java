package com.gitlab.amirmehdi.service.logic;

import com.gitlab.amirmehdi.service.AlgorithmRunnerService;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

@Service
public abstract class AlgorithmBox {
    public final String type = getClass().getSimpleName();

    public final String getType() {
        return type;
    }

    public abstract Trigger getTrigger();

    public abstract void onEvent(AlgorithmDTO algorithm, AlgorithmRunnerService.AlgorithmEvent event);
}

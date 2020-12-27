package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SymbolQueue {

    @JsonProperty("Value")
    public List<SymbolQueueValue> value = null;
}

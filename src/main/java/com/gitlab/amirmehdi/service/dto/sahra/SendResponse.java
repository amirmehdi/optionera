package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendResponse {

    @JsonProperty("R")
    private SendErrorResponse error;
    @JsonProperty("I")
    private int i;

}

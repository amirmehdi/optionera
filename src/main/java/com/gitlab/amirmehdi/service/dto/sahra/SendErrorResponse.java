package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendErrorResponse {

    @JsonProperty("i")
    private long i;
    @JsonProperty("m")
    private String m;

}

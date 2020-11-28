package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ConnectResponse {
    @JsonProperty("C")
    private String messageId;
    @JsonProperty("S")
    private long s;
    @JsonProperty("M")
    private List<Object> m = null;
}

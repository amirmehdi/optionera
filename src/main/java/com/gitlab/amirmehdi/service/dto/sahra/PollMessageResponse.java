package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PollMessageResponse {

    @JsonProperty("H")
    private String hub;
    @JsonProperty("M")
    private String method;
    @JsonProperty("A")
    private List<Object> val = null;
}

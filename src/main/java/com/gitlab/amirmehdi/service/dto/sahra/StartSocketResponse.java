package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StartSocketResponse {
    @JsonProperty(value = "Response")
    private String response;
}

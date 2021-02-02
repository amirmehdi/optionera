package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

import static com.gitlab.amirmehdi.service.sahra.SahraRequestService.HUB;

@Data
@NoArgsConstructor
public class SendRequest {

    @JsonProperty("H")
    private String hub = HUB;
    @JsonProperty("M")
    private String method;
    @JsonProperty("A")
    private List<Object> args = null;
    @JsonProperty("I")
    private int i;

    public SendRequest(String method, List<Object> args) {
        this.method = method;
        this.args = args;
    }
}

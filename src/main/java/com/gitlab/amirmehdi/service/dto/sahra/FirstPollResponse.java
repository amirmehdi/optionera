package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FirstPollResponse {

    @JsonProperty("C")
    private String messageId;
    @JsonProperty("G")
    private String groupsToken;
    @JsonProperty("M")
    private List<PollMessageResponse> m = null;
}

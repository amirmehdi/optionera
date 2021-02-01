package com.gitlab.amirmehdi.service.dto.tsemodels;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddedOption {

    @JsonProperty("time")
    private Time time;
    @JsonProperty("bData")
    private List<EmbeddedData> bData = null;
    @JsonProperty("sum")
    private List<Sum> sum = null;
    @JsonProperty("sresid")
    private List<Sresid> sresid = null;

}

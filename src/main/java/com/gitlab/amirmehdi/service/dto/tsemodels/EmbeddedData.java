package com.gitlab.amirmehdi.service.dto.tsemodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddedData {

    @JsonProperty("bi")
    private String bi;
    @JsonProperty("i")
    private String i;
    @JsonProperty("bat")
    private String bat;
    @JsonProperty("btt")
    private String btt;
    @JsonProperty("namad")
    private String namad;
    @JsonProperty("name")
    private String name;
    @JsonProperty("zvta")
    private long zvta;
    @JsonProperty("emdate")
    private Object emdate;
    @JsonProperty("val")
    private List<Val> val = null;

}

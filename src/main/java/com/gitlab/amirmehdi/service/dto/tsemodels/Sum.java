
package com.gitlab.amirmehdi.service.dto.tsemodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sumT",
    "sumH",
    "sumA"
})
public class Sum {

    @JsonProperty("sumT")
    private String sumT;
    @JsonProperty("sumH")
    private String sumH;
    @JsonProperty("sumA")
    private String sumA;

    @JsonProperty("sumT")
    public String getSumT() {
        return sumT;
    }

    @JsonProperty("sumT")
    public void setSumT(String sumT) {
        this.sumT = sumT;
    }

    @JsonProperty("sumH")
    public String getSumH() {
        return sumH;
    }

    @JsonProperty("sumH")
    public void setSumH(String sumH) {
        this.sumH = sumH;
    }

    @JsonProperty("sumA")
    public String getSumA() {
        return sumA;
    }

    @JsonProperty("sumA")
    public void setSumA(String sumA) {
        this.sumA = sumA;
    }

}

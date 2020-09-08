
package com.gitlab.amirmehdi.service.dto.tsemodels;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "time",
    "sum",
    "bData",
    "sresid",
    "darayis"
})
public class OptionResponse {

    @JsonProperty("time")
    private Time time;
    @JsonProperty("sum")
    private List<Sum> sum = null;
    @JsonProperty("bData")
    private List<BDatum> bData = null;
    @JsonProperty("sresid")
    private List<Sresid> sresid = null;
    @JsonProperty("darayis")
    private List<Darayi> darayis = null;

    @JsonProperty("time")
    public Time getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Time time) {
        this.time = time;
    }

    @JsonProperty("sum")
    public List<Sum> getSum() {
        return sum;
    }

    @JsonProperty("sum")
    public void setSum(List<Sum> sum) {
        this.sum = sum;
    }

    @JsonProperty("bData")
    public List<BDatum> getBData() {
        return bData;
    }

    @JsonProperty("bData")
    public void setBData(List<BDatum> bData) {
        this.bData = bData;
    }

    @JsonProperty("sresid")
    public List<Sresid> getSresid() {
        return sresid;
    }

    @JsonProperty("sresid")
    public void setSresid(List<Sresid> sresid) {
        this.sresid = sresid;
    }

    @JsonProperty("darayis")
    public List<Darayi> getDarayis() {
        return darayis;
    }

    @JsonProperty("darayis")
    public void setDarayis(List<Darayi> darayis) {
        this.darayis = darayis;
    }

}

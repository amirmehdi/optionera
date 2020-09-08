
package com.gitlab.amirmehdi.service.dto.tsemodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hour",
    "min",
    "sec"
})
public class Time {

    @JsonProperty("hour")
    private String hour;
    @JsonProperty("min")
    private String min;
    @JsonProperty("sec")
    private String sec;

    @JsonProperty("hour")
    public String getHour() {
        return hour;
    }

    @JsonProperty("hour")
    public void setHour(String hour) {
        this.hour = hour;
    }

    @JsonProperty("min")
    public String getMin() {
        return min;
    }

    @JsonProperty("min")
    public void setMin(String min) {
        this.min = min;
    }

    @JsonProperty("sec")
    public String getSec() {
        return sec;
    }

    @JsonProperty("sec")
    public void setSec(String sec) {
        this.sec = sec;
    }

}

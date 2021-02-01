
package com.gitlab.amirmehdi.service.dto.tsemodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "d_s_fo",
    "d_s_kh",
    "em_da",
    "darayi",
    "i",
    "i2",
    "val"
})
public class OptionData {

    @JsonProperty("d_s_fo")
    private long dSFo;
    @JsonProperty("d_s_kh")
    private long dSKh;
    @JsonProperty("em_da")
    private String emDa;
    @JsonProperty("darayi")
    private String darayi;
    @JsonProperty("i")
    private String i;
    @JsonProperty("i2")
    private String i2;
    @JsonProperty("val")
    private List<Val> val = null;

    @JsonProperty("d_s_fo")
    public long getDSFo() {
        return dSFo;
    }

    @JsonProperty("d_s_fo")
    public void setDSFo(long dSFo) {
        this.dSFo = dSFo;
    }

    @JsonProperty("d_s_kh")
    public long getDSKh() {
        return dSKh;
    }

    @JsonProperty("d_s_kh")
    public void setDSKh(long dSKh) {
        this.dSKh = dSKh;
    }

    @JsonProperty("em_da")
    public String getEmDa() {
        return emDa;
    }

    @JsonProperty("em_da")
    public void setEmDa(String emDa) {
        this.emDa = emDa;
    }

    @JsonProperty("darayi")
    public String getDarayi() {
        return darayi;
    }

    @JsonProperty("darayi")
    public void setDarayi(String darayi) {
        this.darayi = darayi;
    }

    @JsonProperty("i")
    public String getI() {
        return i;
    }

    @JsonProperty("i")
    public void setI(String i) {
        this.i = i;
    }

    @JsonProperty("i2")
    public String getI2() {
        return i2;
    }

    @JsonProperty("i2")
    public void setI2(String i2) {
        this.i2 = i2;
    }

    @JsonProperty("val")
    public List<Val> getVal() {
        return val;
    }

    @JsonProperty("val")
    public void setVal(List<Val> val) {
        this.val = val;
    }

}

package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemainData {

    @JsonProperty("RealBalance")
    public long realBalance;
    @JsonProperty("BlockedBalance")
    public long blockedBalance;
    @JsonProperty("AccountBalance")
    public long accountBalance;
    @JsonProperty("HasHamiContract")
    public long hasHamiContract;
    @JsonProperty("MarginCustomerStatus")
    public long marginCustomerStatus;
    @JsonProperty("BourseCode")
    public String bourseCode;
    @JsonProperty("email")
    public Object email;
    @JsonProperty("Title")
    public String title;
    @JsonProperty("RealBalanceDescription")
    public Object realBalanceDescription;
    @JsonProperty("HamiBalance")
    public long hamiBalance;
    @JsonProperty("Credit")
    public long credit;
}

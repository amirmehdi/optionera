package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserOpenInterestResponse {

    @JsonProperty("IsSuccessfull")
    public boolean isSuccessfull;
    @JsonProperty("MessageDesc")
    public Object messageDesc;
    @JsonProperty("MessageCode")
    public Object messageCode;
    @JsonProperty("Data")
    public List<UserOpenInterestData> data = null;
    @JsonProperty("Version")
    public long version;
}

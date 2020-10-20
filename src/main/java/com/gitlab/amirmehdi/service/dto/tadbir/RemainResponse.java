package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RemainResponse {

    @JsonProperty("Data")
    public RemainData data;
    @JsonProperty("MessageDesc")
    public Object messageDesc;
    @JsonProperty("IsSuccessfull")
    public boolean isSuccessfull;
    @JsonProperty("MessageCode")
    public Object messageCode;
    @JsonProperty("Version")
    public long version;
}

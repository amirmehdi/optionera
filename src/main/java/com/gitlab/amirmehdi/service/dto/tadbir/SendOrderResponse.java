package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendOrderResponse {

    @JsonProperty("Data")
    public Object data;
    @JsonProperty("MessageDesc")
    public String messageDesc;
    @JsonProperty("IsSuccessfull")
    public boolean isSuccessfull;
    @JsonProperty("MessageCode")
    public Object messageCode;
    @JsonProperty("Version")
    public int version;
}

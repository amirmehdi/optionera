package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DailyPortfolioResponse {
    @JsonProperty("Data")
    public List<DailyPortfolio> data = null;
    @JsonProperty("MessageDesc")
    public Object messageDesc;
    @JsonProperty("IsSuccessfull")
    public boolean isSuccessfull;
    @JsonProperty("MessageCode")
    public Object messageCode;
    @JsonProperty("Version")
    public long version;
}

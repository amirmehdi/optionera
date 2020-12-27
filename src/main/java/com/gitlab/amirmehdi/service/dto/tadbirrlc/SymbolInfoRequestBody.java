package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SymbolInfoRequestBody {

    @JsonProperty("Type")
    public String type;
    @JsonProperty("la")
    public String la;
    @JsonProperty("nscCode")
    public String nscCode;
}

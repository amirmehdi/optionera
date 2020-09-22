package com.gitlab.amirmehdi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionStockWatch {
    @JsonProperty("Last")
    private int last;
    @JsonProperty("Close")
    private int close;
    @JsonProperty("ReferencePrice")
    private int referencePrice;
    @JsonProperty("SettlementPrice")
    private int settlementPrice;
    @JsonProperty("BSPrice")
    private int bsPrice;
    @JsonProperty("TradeVolume")
    private long tradeVolume;
    @JsonProperty("TradeCount")
    private int tradeCount;
    @JsonProperty("TradeValue")
    private long tradeValue;
    @JsonProperty("OpenInterest")
    private int openInterest;

}


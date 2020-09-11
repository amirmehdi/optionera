package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionStats {
    private Option option;
    @JsonUnwrapped(prefix = "put")
    private OptionStockWatch putStockWatch;
    @JsonUnwrapped(prefix = "put")
    private BestBidAsk putBidAsk;
    @JsonUnwrapped(prefix = "call")
    private OptionStockWatch callStockWatch;
    @JsonUnwrapped(prefix = "call")
    private BestBidAsk callBidAsk;
}

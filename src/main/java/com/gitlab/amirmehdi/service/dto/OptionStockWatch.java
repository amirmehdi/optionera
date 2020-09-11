package com.gitlab.amirmehdi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionStockWatch {
    private int last;
    private int close;
    private int settlementPrice;
    private int bsPrice;
    private int tradeVolume;
    private int tradeCount;
    private long tradeValue;
    private int openInterest;

}


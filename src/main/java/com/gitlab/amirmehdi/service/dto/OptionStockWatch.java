package com.gitlab.amirmehdi.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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


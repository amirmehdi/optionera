package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockWatch {

    private String isin;

    private int last;
    private int closing;

    private int first;
    private int high;
    private int low;
    private int min;
    private int max;
    private String state;
    private long tradeValue;
    private long tradeVolume;
    private int tradesCount;
    private int referencePrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Tehran")
    @JsonProperty("lastTrade")
    private Date lastTrade;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Tehran")
    private Date crawledDate;

    private int openInterest;
    private int settlementPrice;

    @JsonIgnore
    public boolean isBuyQueue() {
        return last == max;
    }
}

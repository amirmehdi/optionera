package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * File Created by mojtabye on 4/24/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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
    private int tradeVolume;
    private int tradesCount;
    private int referencePrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateTime;
}

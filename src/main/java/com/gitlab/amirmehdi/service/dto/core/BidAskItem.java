package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * File Created by mojtabye on 7/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BidAskItem {

    private int bidNumber;
    private int bidPrice;
    private int bidQuantity;

    private int askNumber;
    private int askPrice;
    private int askQuantity;

}

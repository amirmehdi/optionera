package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidAskItem {

    private int bidNumber;
    private int bidPrice;
    private int bidQuantity;

    private int askNumber;
    private int askPrice;
    private int askQuantity;

}

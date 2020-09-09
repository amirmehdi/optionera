package com.gitlab.amirmehdi.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class BestBidAsk {
    private int bidPrice;
    private int askPrice;
    private int bidVolume;
    private int askVolume;
}

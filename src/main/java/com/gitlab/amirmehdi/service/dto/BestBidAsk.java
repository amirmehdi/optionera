package com.gitlab.amirmehdi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestBidAsk {
    @JsonProperty("BidPrice")
    private int bidPrice;
    @JsonProperty("AskPrice")
    private int askPrice;
    @JsonProperty("BidVolume")
    private int bidVolume;
    @JsonProperty("AskVolume")
    private int askVolume;
}

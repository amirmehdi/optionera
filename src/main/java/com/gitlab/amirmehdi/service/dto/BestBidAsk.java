package com.gitlab.amirmehdi.service.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestBidAsk {
    private int bidPrice;
    private int askPrice;
    private int bidVolume;
    private int askVolume;
}

package com.gitlab.amirmehdi.service.dto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {
    private String startAuctionTime;
    private String endAuctionTime;
    private String openingTime;
    private String closingTime;
}

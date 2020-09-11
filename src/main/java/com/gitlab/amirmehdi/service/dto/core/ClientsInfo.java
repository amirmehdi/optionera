package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ClientsInfo {

    private int individualBuyCount;
    private int individualSellCount;
    private long individualBuyVolume;
    private long individualSellVolume;

    private long naturalBuyVolume;
    private long naturalSellVolume;
    private int naturalBuyCount;
    private int naturalSellCount;

    private String isin;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateTime;
    private double buyerDensityValue;
    private double sellerDensityValue;
    private double minimumQuickDetectSmartMoney;
    private double quickDetectSmartMoney;
    private double dsm;
    private long dsmCounter;
}

package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.amirmehdi.service.dto.core.ClientsInfo;
import lombok.Data;

import java.util.Date;

@Data
public class IndInstTradeResponse {
    @JsonProperty("isin")
    private String isin;
    @JsonProperty("tickerFa")
    private String tickerFa;
    @JsonProperty("IndBuyVolume")
    private double indBuyVolume;
    @JsonProperty("IndBuyNumber")
    private double indBuyNumber;
    @JsonProperty("IndBuyValue")
    private double indBuyValue;
    @JsonProperty("IndSellVolume")
    private double indSellVolume;
    @JsonProperty("IndSellNumber")
    private double indSellNumber;
    @JsonProperty("IndSellValue")
    private double indSellValue;
    @JsonProperty("InsBuyVolume")
    private double insBuyVolume;
    @JsonProperty("InsBuyNumber")
    private double insBuyNumber;
    @JsonProperty("InsBuyValue")
    private double insBuyValue;
    @JsonProperty("InsSellVolume")
    private double insSellVolume;
    @JsonProperty("InsSellNumber")
    private double insSellNumber;
    @JsonProperty("InsSellValue")
    private double insSellValue;
    @JsonProperty("day")
    private String day;
    @JsonProperty("tickerKey")
    private long tickerKey;
    @JsonProperty("tradeKey")
    private long tradeKey;

    public ClientsInfo toClientsInfo(){
        return ClientsInfo.builder()
            .dateTime(new Date())
            .isin(isin)
            .individualBuyCount((int) indBuyNumber)
            .individualBuyVolume((long) indBuyVolume)
            .individualSellCount((int) indSellNumber)
            .individualSellVolume((long) indSellVolume)
            .naturalBuyCount((int) insBuyNumber)
            .naturalBuyVolume((long) insBuyVolume)
            .naturalSellCount((int) insSellNumber)
            .naturalSellVolume((long) insSellVolume)
            .build();
    }
}

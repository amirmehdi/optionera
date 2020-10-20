package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOpenInterestData {
    @JsonProperty("Symbol")
    public String symbol;
    @JsonProperty("NSCCode")
    public String nSCCode;
    @JsonProperty("CustomerTitle")
    public String customerTitle;
    @JsonProperty("PSDate")
    public String pSDate;
    @JsonProperty("MarketType")
    public long marketType;
    @JsonProperty("SymbolId")
    public long symbolId;
    @JsonProperty("CustomerId")
    public long customerId;
    @JsonProperty("SellPositionCount")
    public long sellPositionCount;
    @JsonProperty("BuyPositionCount")
    public long buyPositionCount;
    @JsonProperty("SellOrderCount")
    public long sellOrderCount;
    @JsonProperty("BuyOrderCount")
    public long buyOrderCount;
    @JsonProperty("MarginValue")
    public float marginValue;
    @JsonProperty("MarginCount")
    public long marginCount;
    @JsonProperty("SellTotalCount")
    public long sellTotalCount;
    @JsonProperty("BuyTotalCount")
    public long buyTotalCount;
    @JsonProperty("BlockedMarginCount")
    public long blockedMarginCount;
}

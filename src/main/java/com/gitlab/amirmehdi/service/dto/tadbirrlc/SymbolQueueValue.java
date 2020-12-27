package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SymbolQueueValue {
    @JsonProperty("BestBuyPrice")
    public int bestBuyPrice;
    @JsonProperty("BestSellPrice")
    public int bestSellPrice;
    @JsonProperty("BestSellQuantity")
    public int bestSellQuantity;
    @JsonProperty("BestBuyQuantity")
    public int bestBuyQuantity;
    @JsonProperty("NoBestBuy")
    public int noBestBuy;
    @JsonProperty("NoBestSell")
    public int noBestSell;
    @JsonProperty("NSCCode")
    public String nSCCode;
    @JsonProperty("Place")
    public int place;
}

package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyPortfolio {
    @JsonProperty("SymbolState")
    public String symbolState;
    @JsonProperty("RunTimeShareCount")
    public long runTimeShareCount;
    @JsonProperty("OnBoardSell")
    public long onBoardSell;
    @JsonProperty("OnBoardBuy")
    public long onBoardBuy;
    @JsonProperty("BuyPriceAvg")
    public long buyPriceAvg;
    @JsonProperty("SellPriceAvg")
    public long sellPriceAvg;
    @JsonProperty("InsCode")
    public String insCode;
    @JsonProperty("CSDPortfolioCount")
    public long cSDPortfolioCount;
    @JsonProperty("ISIN")
    public String iSIN;
    @JsonProperty("SymbolId")
    public long symbolId;
    @JsonProperty("IntradayTotalBuy")
    public long intradayTotalBuy;
    @JsonProperty("IntradayTotalSell")
    public long intradayTotalSell;
    @JsonProperty("IntradayExecuteSell")
    public long intradayExecuteSell;
    @JsonProperty("IntradayExecutedBuy")
    public long intradayExecutedBuy;
    @JsonProperty("TotalAveragePrice")
    public long totalAveragePrice;
}

package com.gitlab.amirmehdi.service.dto.asa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.ClientsInfo;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InstrumentInfoResponse {

    @JsonProperty("Id")
    private long id;
    @JsonProperty("WatchId")
    private long watchId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("CompanyName")
    private String companyName;
    @JsonProperty("TradeNumber")
    private long tradeNumber;
    @JsonProperty("TradesQuantity")
    private long tradesQuantity;
    @JsonProperty("TotalTradeValue")
    private long totalTradeValue;
    @JsonProperty("FirstTradePrice")
    private long firstTradePrice;
    @JsonProperty("LowestTradePrice")
    private long lowestTradePrice;
    @JsonProperty("HighestTradePrice")
    private long highestTradePrice;
    @JsonProperty("PreviousDayPrice")
    private long previousDayPrice;
    @JsonProperty("LastTradePrice")
    private long lastTradePrice;
    @JsonProperty("LastTradeChange")
    private long lastTradeChange;
    @JsonProperty("LastTradePercentChange")
    private double lastTradePercentChange;
    @JsonProperty("FinalPrice")
    private long finalPrice;
    @JsonProperty("FinalPriceChange")
    private long finalPriceChange;
    @JsonProperty("FinalPricePercentChange")
    private double finalPricePercentChange;
    @JsonProperty("UpperPriceThreshold")
    private long upperPriceThreshold;
    @JsonProperty("LowerPriceThreshold")
    private long lowerPriceThreshold;
    @JsonProperty("Eps")
    private long eps;
    @JsonProperty("Pe")
    private long pe;
    @JsonProperty("BestBidNumber")
    private long bestBidNumber;
    @JsonProperty("BestBidQuantity")
    private long bestBidQuantity;
    @JsonProperty("BestBidPrice")
    private long bestBidPrice;
    @JsonProperty("BestAskNumber")
    private long bestAskNumber;
    @JsonProperty("BestAskQuantity")
    private long bestAskQuantity;
    @JsonProperty("BestAskPrice")
    private long bestAskPrice;
    @JsonProperty("GroupStateId")
    private long groupStateId;
    @JsonProperty("GroupStateTitle")
    private Object groupStateTitle;
    @JsonProperty("InstrumentGroupCode")
    private String instrumentGroupCode;
    @JsonProperty("StateId")
    private long stateId;
    @JsonProperty("StateTitle")
    private String stateTitle;
    @JsonProperty("FirstTradeDateTime")
    private String firstTradeDateTime;
    @JsonProperty("LastTradeDateTime")
    private String lastTradeDateTime;
    @JsonProperty("PreviousPriceVariationPercentage")
    private long previousPriceVariationPercentage;
    @JsonProperty("ReferencePriceVariationPercentage")
    private long referencePriceVariationPercentage;
    @JsonProperty("InstrumentId")
    private long instrumentId;
    @JsonProperty("MinimumOrderQuantity")
    private long minimumOrderQuantity;
    @JsonProperty("MaximumOrderQuantity")
    private long maximumOrderQuantity;
    @JsonProperty("BaseQuantity")
    private double baseQuantity;
    @JsonProperty("AmountOfParValue")
    private long amountOfParValue;
    @JsonProperty("MinimumPriceThreshold")
    private long minimumPriceThreshold;
    @JsonProperty("MaximumPriceThreshold")
    private long maximumPriceThreshold;
    @JsonProperty("FixedPriceTick")
    private double fixedPriceTick;
    @JsonProperty("Isin")
    private String isin;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("LastPriceVariationSignId")
    private long lastPriceVariationSignId;
    @JsonProperty("ReferencePriceVariationSignId")
    private long referencePriceVariationSignId;
    @JsonProperty("Asset")
    private long asset;
    @JsonProperty("BidAskList")
    private List<BidAskList> bidAskList = null;
    @JsonProperty("Exchange")
    private long exchange;
    @JsonProperty("CorporationBuyCount")
    private long corporationBuyCount;
    @JsonProperty("CorporationBuyQuantity")
    private long corporationBuyQuantity;
    @JsonProperty("CorporationSellCount")
    private long corporationSellCount;
    @JsonProperty("CorporationSellQuantity")
    private long corporationSellQuantity;
    @JsonProperty("IndividualBuyCount")
    private long individualBuyCount;
    @JsonProperty("IndividualBuyQuantity")
    private long individualBuyQuantity;
    @JsonProperty("IndividualSellCount")
    private long individualSellCount;
    @JsonProperty("IndividualSellQuantity")
    private long individualSellQuantity;
    @JsonProperty("Value")
    private long value;

    public StockWatch toStockWatch() {
        return StockWatch.builder()
            .last((int) lastTradePrice)
            .closing((int) finalPrice)
            .first((int) firstTradePrice)
            .high((int) highestTradePrice)
            .low((int) lowestTradePrice)
            .min((int) lowerPriceThreshold)
            .max((int) upperPriceThreshold)
            .tradesCount((int) tradeNumber)
            .tradeVolume(tradesQuantity)
            .tradeValue(totalTradeValue)
            .referencePrice((int) previousDayPrice)
            .state(stateTitle)
            .isin(isin)
            .dateTime(new Date()).build();
    }

    public BidAsk toBidAsk() {
        BidAsk bidAsk = new BidAsk();
        bidAsk.setIsin(isin);
        bidAsk.setItems(bidAskList.stream().map(item -> BidAskItem.builder()
            .askNumber((int) item.getAskNumber())
            .askQuantity((int) item.getAskQuantity())
            .askPrice((int) item.getAskPrice())
            .bidPrice((int) item.getBidPrice())
            .bidQuantity((int) item.getBidQuantity())
            .bidNumber((int) item.getBidNumber())
            .build()).collect(Collectors.toList()));
        //TODO fix data
        bidAsk.setDateTime(new Date());
        return bidAsk;
    }

    public ClientsInfo toClientsInfo() {
        return ClientsInfo.builder()
            .naturalSellCount((int) corporationSellCount)
            .naturalBuyCount((int) corporationBuyCount)
            .individualSellCount((int) individualSellCount)
            .individualBuyCount((int) individualBuyCount)
            .naturalSellVolume(corporationSellQuantity)
            .naturalBuyVolume(corporationBuyQuantity)
            .individualSellVolume(individualSellQuantity)
            .individualBuyVolume(individualBuyQuantity)
            .isin(isin)
            .dateTime(new Date())
            .build();
    }
}

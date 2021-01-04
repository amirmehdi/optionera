package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LightSymbolInfoAndQueue {

    @JsonProperty("symbolinfo")
    private SymbolInfo symbolinfo;
    @JsonProperty("symbolqueue")
    private SymbolQueue symbolqueue;


    public StockWatch toStockWatch() {
        return StockWatch.builder()
            .lastTrade(new Date())
            .isin(symbolinfo.getNc())
            .last((int) symbolinfo.getLtp())
            .closing((int) symbolinfo.getCp())
            .first((int) symbolinfo.getHp())
            .referencePrice((int) symbolinfo.getRp())
            .max((int) symbolinfo.getHt())
            .min((int) symbolinfo.getLt())
            .high((int) symbolinfo.getHp())
            .low((int) symbolinfo.getLp())
            .tradesCount((int) symbolinfo.getNt())
            .tradeVolume(symbolinfo.getNst())
            .tradeValue(symbolinfo.getTv())
            .build();
    }

    public BidAsk toBidAsk() {
        List<BidAskItem> items = symbolqueue.getValue().stream()
            .map(value -> BidAskItem.builder()
                .bidNumber(value.noBestBuy)
                .bidQuantity(value.bestBuyQuantity)
                .bidPrice(value.bestBuyPrice)
                .askPrice(value.bestSellPrice)
                .askQuantity(value.bestSellQuantity)
                .askNumber(value.noBestSell)
                .build())
            .collect(Collectors.toList());
        BidAsk bidAsk = new BidAsk();
        bidAsk.setIsin(symbolinfo.getNc());
        bidAsk.setItems(items);
        //TODO fix data
        bidAsk.setDateTime(new Date());
        return bidAsk;
    }
}

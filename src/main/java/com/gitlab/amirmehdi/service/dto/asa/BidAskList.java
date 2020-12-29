package com.gitlab.amirmehdi.service.dto.asa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BidAskList {
    @JsonProperty("Id")
    private long id;
    @JsonProperty("Isin")
    private String isin;
    @JsonProperty("BidQuantity")
    private long bidQuantity;
    @JsonProperty("BidNumber")
    private long bidNumber;
    @JsonProperty("BidPrice")
    private long bidPrice;
    @JsonProperty("AskPrice")
    private long askPrice;
    @JsonProperty("AskNumber")
    private long askNumber;
    @JsonProperty("AskQuantity")
    private long askQuantity;
    @JsonProperty("RowPlace")
    private long rowPlace;
    @JsonProperty("BidOrderCount")
    private long bidOrderCount;
    @JsonProperty("AskOrderCount")
    private long askOrderCount;
}

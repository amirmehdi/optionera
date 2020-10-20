package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("nsccode")
    public String nsccode;
    @JsonProperty("symbolid")
    public long symbolid;
    @JsonProperty("lasttradeprice")
    public long lasttradeprice;
    @JsonProperty("time")
    public String time;
    @JsonProperty("dtime")
    public String dtime;
    @JsonProperty("orderprice")
    public long orderprice;
    @JsonProperty("customerid")
    public long customerid;
    @JsonProperty("ProviderName")
    public String providerName;
    @JsonProperty("Providerid")
    public long providerid;
    @JsonProperty("orderid")
    public String orderid;
    @JsonProperty("ordervl")
    public String ordervl;
    @JsonProperty("ordervlid")
    public long ordervlid;
    @JsonProperty("gtdate")
    public Object gtdate;
    @JsonProperty("gtdateMiladi")
    public Object gtdateMiladi;
    @JsonProperty("orderside")
    public String orderside;
    @JsonProperty("ordersideid")
    public long ordersideid;
    @JsonProperty("qunatity")
    public long qunatity;
    @JsonProperty("ExpectedQuantity")
    public long expectedQuantity;
    @JsonProperty("excuted")
    public long excuted;
    @JsonProperty("status")
    public String status;
    @JsonProperty("visible")
    public long visible;
    @JsonProperty("customername")
    public String customername;
    @JsonProperty("orderFrom")
    public String orderFrom;
    @JsonProperty("minimumQuantity")
    public long minimumQuantity;
    @JsonProperty("maxShow")
    public long maxShow;
    @JsonProperty("HostOrderId")
    public long hostOrderId;
    @JsonProperty("OrderEntryDate")
    public String orderEntryDate;
    @JsonProperty("orderDateTime")
    public Object orderDateTime;
    @JsonProperty("state")
    public String state;
    @JsonProperty("errorcode")
    public Object errorcode;
}

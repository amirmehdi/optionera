package com.gitlab.amirmehdi.service.dto.tadbir;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendOrderRequest {

    @JsonProperty("IsSymbolCautionAgreement")
    public boolean isSymbolCautionAgreement;
    @JsonProperty("CautionAgreementSelected")
    public boolean cautionAgreementSelected;
    @JsonProperty("IsSymbolSepahAgreement")
    public boolean isSymbolSepahAgreement;
    @JsonProperty("SepahAgreementSelected")
    public boolean sepahAgreementSelected;
    @JsonProperty("orderCount")
    public int orderCount;
    @JsonProperty("orderPrice")
    public int orderPrice;
    @JsonProperty("FinancialProviderId")
    public int financialProviderId;
    @JsonProperty("minimumQuantity")
    public int minimumQuantity;
    @JsonProperty("maxShow")
    public int maxShow;
    @JsonProperty("orderId")
    public int orderId;
    @JsonProperty("isin")
    public String isin;
    @JsonProperty("orderSide")
    public int orderSide;
    @JsonProperty("orderValidity")
    public int orderValidity;
    @JsonProperty("orderValiditydate")
    public Object orderValiditydate;
    @JsonProperty("shortSellIsEnabled")
    public boolean shortSellIsEnabled;
    @JsonProperty("shortSellIncentivePercent")
    public int shortSellIncentivePercent;
}

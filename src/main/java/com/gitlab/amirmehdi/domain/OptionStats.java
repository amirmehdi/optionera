package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.amirmehdi.service.calculator.BlackScholes;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.gitlab.amirmehdi.service.OptionService.RISK_FREE;

@Getter
@Setter
public class OptionStats {
    private Option option;

    private StockWatch putStockWatch;
    private BidAskItem putBidAsk;

    private StockWatch callStockWatch;
    private BidAskItem callBidAsk;

    private StockWatch baseStockWatch;
    private BidAskItem baseBidAsk;

    public OptionStats option(Option option) {
        this.option = option;
        return this;
    }

    public OptionStats putStockWatch(StockWatch putStockWatch) {
        this.putStockWatch = putStockWatch;
        return this;
    }

    public OptionStats putBidAsk(BidAskItem putBidAsk) {
        this.putBidAsk = putBidAsk;
        return this;
    }

    public OptionStats callStockWatch(StockWatch callStockWatch) {
        this.callStockWatch = callStockWatch;
        return this;
    }

    public OptionStats callBidAsk(BidAskItem callBidAsk) {
        this.callBidAsk = callBidAsk;
        return this;
    }

    public OptionStats baseStockWatch(StockWatch baseStockWatch) {
        this.baseStockWatch = baseStockWatch;
        return this;
    }

    public OptionStats baseBidAsk(BidAskItem baseBidAsk) {
        this.baseBidAsk = baseBidAsk;
        return this;
    }

    public long getId() {
        return option.getId();
    }

    @JsonProperty("callBS")
    public int getCallBlackScholes() {
        if (checkForNull()) {
            return 0;
        }
        return (int) (option.getInstrument().getVolatilities()
            .stream()
            .mapToDouble(value -> BlackScholes.getBSCall(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, value, ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0))
            .sum() / 3.0);
    }

    @JsonProperty("callBS30")
    public int getCallBlackScholes30() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSCall(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility30(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    @JsonProperty("callBS60")
    public int getCallBlackScholes60() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSCall(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility60(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    @JsonProperty("callBS90")
    public int getCallBlackScholes90() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSCall(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility90(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    @JsonProperty("putBS")
    public int getPutBlackScholes() {
        if (checkForNull()) {
            return 0;
        }
        return (int) (option.getInstrument().getVolatilities()
            .stream()
            .mapToDouble(value -> BlackScholes.getBSPut(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, value, ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0))
            .sum() / 3.0);
    }

    @JsonProperty("putBS30")
    public int getPutBlackScholes30() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSPut(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility30(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    @JsonProperty("putBS60")
    public int getPutBlackScholes60() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSPut(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility60(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    @JsonProperty("putBS90")
    public int getPutBlackScholes90() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.getBSPut(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility90(), ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()) / 365.0);
    }

    public Integer getCallEffectivePrice() {
        if (checkForNull()) {
            return 0;
        }
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return option.getStrikePrice() + callBidAsk.getAskPrice();
    }

    public Integer getPutEffectivePrice() {
        if (checkForNull()) {
            return 0;
        }
        if (putBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return option.getStrikePrice() - putBidAsk.getAskPrice();
    }

    @JsonIgnore
    public float getCallAskPriceToBS() {
        if (checkForNull()) {
            return 0;
        }
        int callBlackScholes30 = getCallBlackScholes30();
        if (callBidAsk.getAskPrice() == 0 || callBlackScholes30 == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((callBidAsk.getAskPrice() * 1.0 / callBlackScholes30 - 1) * 10000.0) / 100.0);
    }

    @JsonIgnore
    public float getPutAskPriceToBS() {
        if (checkForNull()) {
            return 0;
        }
        int putBlackScholes30 = getPutBlackScholes30();
        if (putBidAsk.getAskPrice() == 0 || putBlackScholes30 == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((putBidAsk.getAskPrice() * 1.0 / putBlackScholes30 - 1) * 10000.0) / 100.0);
    }

    @JsonIgnore
    public float getCallBreakEven() {
        if (checkForNull()) {
            return 0;
        }
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((getCallEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 10000.0) / 100.0);
    }

    @JsonIgnore
    public float getPutBreakEven() {
        if (checkForNull()) {
            return 0;
        }
        if (putBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((getPutEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 10000.0) / 100.0);
    }

    @JsonIgnore
    public float getCallLeverage() {
        if (checkForNull()) {
            return 0;
        }
        if (callBidAsk.getAskPrice() == 0) {
            return 0;
        }
        return (float) (Math.round((baseStockWatch.getLast() * 1.0 / callBidAsk.getAskPrice()) * 100.0) / 100.0);
    }

    @JsonIgnore
    public float getPutLeverage() {
        if (checkForNull()) {
            return 0;
        }
        if (putBidAsk.getAskPrice() == 0) {
            return 0;
        }
        return (float) (Math.round((baseStockWatch.getLast() * 1.0 / putBidAsk.getAskPrice()) * 100.0) / 100.0);
    }

    @JsonIgnore
    public int getCallMargin() {
        if (checkForNull() || callStockWatch == null) {
            return 0;
        }
        int OTM = Math.max(option.getStrikePrice() - baseStockWatch.getClosing(), 0) * option.getContractSize();
        int I1 = (int) (0.2 * baseStockWatch.getClosing() * option.getContractSize() - OTM);
        int I2 = (int) (0.1 * option.getStrikePrice() * option.getContractSize());
        int V1 = 100_000 * (Math.max(I1, I2) / 100_000 + 1);
        int V2 = callStockWatch.getSettlementPrice() * option.getContractSize();
        return V1 + V2;
    }

    @JsonIgnore
    public int getPutMargin() {
        if (checkForNull() || putStockWatch == null) {
            return 0;
        }
        int OTM = Math.max(baseStockWatch.getClosing() - option.getStrikePrice(), 0) * option.getContractSize();
        int I1 = (int) (0.2 * baseStockWatch.getClosing() * option.getContractSize() - OTM);
        int I2 = (int) (0.1 * option.getStrikePrice() * option.getContractSize());
        int V1 = 100_000 * (Math.max(I1, I2) / 100_000 + 1);
        int V2 = putStockWatch.getSettlementPrice() * option.getContractSize();
        return V1 + V2;
    }

    @JsonIgnore
    public float getCallHedge() {
        if (checkForNull()) {
            return 0;
        }
        return (float) (Math.round((callBidAsk.getBidPrice() * 1.0 / baseStockWatch.getLast()) * 10000.0) / 100.0);
    }

    public Integer getCallFinalPrice() {
        if (checkForNull()) {
            return Integer.MAX_VALUE;
        }
        return baseStockWatch.getLast() - callBidAsk.getBidPrice();
    }

    @JsonIgnore
    public float getCallIndifference() {
        if (checkForNull()) {
            return 0;
        }
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round(((float) (Math.round((option.getStrikePrice() * 1.0 / (baseStockWatch.getLast() - callBidAsk.getAskPrice())) * 10000.0) / 100.0) - 100) * 100.0) / 100.0);
    }

    @JsonIgnore
    public float getCallGain() {
        if (checkForNull()) {
            return 0;
        }
        return (float) (Math.round((option.getStrikePrice() * 1.0 / getCallFinalPrice() - 1) * 10000.0) / 100.0);
    }

    @JsonIgnore
    public float getCallGainMonthly() {
        if (checkForNull()) {
            return 0;
        }
        return (float) (Math.round(((float) (getCallGain() * 30.0 / ChronoUnit.DAYS.between(LocalDate.now(), option.getExpDate()))) * 100.0) / 100.0);
    }

    @JsonIgnore
    public Boolean getCallInTheMoney() {
        if (checkForNull()) {
            return false;
        }
        return option.getStrikePrice() < baseStockWatch.getLast();
    }

    private boolean checkForNull() {
        return option == null || putBidAsk == null || baseStockWatch == null || callBidAsk == null || LocalDate.now().isAfter(option.getExpDate());
    }
}

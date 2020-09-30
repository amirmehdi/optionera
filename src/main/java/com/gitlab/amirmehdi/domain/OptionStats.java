package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public int getBlackScholes30() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility30(), ChronoUnit.DAYS.between(LocalDate.now(),option.getExpDate()));
    }

    public int getBlackScholes60() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility60(), ChronoUnit.DAYS.between(LocalDate.now(),option.getExpDate()));
    }

    public int getBlackScholes90() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility90(), ChronoUnit.DAYS.between(LocalDate.now(),option.getExpDate()));
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
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((callBidAsk.getAskPrice() * 1.0 / getBlackScholes30() - 1) * 1000.0) / 100.0);
    }

    @JsonIgnore
    public float getPutAskPriceToBS() {
        if (checkForNull()) {
            return 0;
        }
        if (putBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((putBidAsk.getAskPrice() * 1.0 / getBlackScholes30() - 1) * 1000.0) / 100.0);
    }

    @JsonIgnore
    public float getCallBreakEven() {
        if (checkForNull()) {
            return 0;
        }
        return (float) (Math.round((getCallEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 1000.0) / 100.0);
    }

    @JsonIgnore
    public float getPutBreakEven() {
        if (checkForNull()) {
            return 0;
        }
        return (float) (Math.round((getPutEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 1000.0) / 100.0);
    }

    @JsonIgnore
    public float getCallLeverage() {
        if (checkForNull()) {
            return 0;
        }
        if (callBidAsk.getAskPrice() == 0) {
            return 0;
        }
        return (float) (Math.round((baseStockWatch.getLast() * 1.0 / callBidAsk.getAskPrice()) * 1000.0) / 100.0);
    }

    @JsonIgnore
    public float getPutLeverage() {
        if (checkForNull()) {
            return 0;
        }
        if (putBidAsk.getAskPrice() == 0) {
            return 0;
        }
        return (float) (Math.round((baseStockWatch.getLast() * 1.0 / putBidAsk.getAskPrice()) * 1000.0) / 100.0);
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

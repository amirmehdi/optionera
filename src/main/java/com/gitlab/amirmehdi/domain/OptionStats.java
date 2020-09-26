package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gitlab.amirmehdi.service.calculator.BlackScholes;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.gitlab.amirmehdi.service.OptionStatsService.RISK_FREE;

@Getter
@Setter
public class OptionStats {
    private Option option;
    @JsonUnwrapped(prefix = "put")
    private OptionStockWatch putStockWatch;
    @JsonUnwrapped(prefix = "put")
    private BestBidAsk putBidAsk;
    @JsonUnwrapped(prefix = "call")
    private OptionStockWatch callStockWatch;
    @JsonUnwrapped(prefix = "call")
    private BestBidAsk callBidAsk;
    private StockWatch baseStockWatch;
    private BidAsk baseBidAsk;

    public OptionStats option(Option option) {
        this.option = option;
        return this;
    }

    public OptionStats putStockWatch(OptionStockWatch putStockWatch) {
        this.putStockWatch = putStockWatch;
        return this;
    }

    public OptionStats putBidAsk(BestBidAsk putBidAsk) {
        this.putBidAsk = putBidAsk;
        return this;
    }

    public OptionStats callStockWatch(OptionStockWatch callStockWatch) {
        this.callStockWatch = callStockWatch;
        return this;
    }

    public OptionStats callBidAsk(BestBidAsk callBidAsk) {
        this.callBidAsk = callBidAsk;
        return this;
    }

    public OptionStats baseStockWatch(StockWatch baseStockWatch) {
        this.baseStockWatch = baseStockWatch;
        return this;
    }

    public OptionStats baseBidAsk(BidAsk baseBidAsk) {
        this.baseBidAsk = baseBidAsk;
        return this;
    }

    public int getBlackScholes30() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility30(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
    }

    public int getBlackScholes60() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility60(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
    }

    public int getBlackScholes90() {
        if (checkForNull()) {
            return 0;
        }
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility90(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
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
        return option == null || putBidAsk == null || baseStockWatch == null || callBidAsk == null || callStockWatch == null || putStockWatch == null;
    }
}

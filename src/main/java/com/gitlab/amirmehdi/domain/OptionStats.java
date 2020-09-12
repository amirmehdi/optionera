package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gitlab.amirmehdi.service.calculator.BlackScholes;
import com.gitlab.amirmehdi.service.dto.BestBidAsk;
import com.gitlab.amirmehdi.service.dto.OptionStockWatch;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.gitlab.amirmehdi.service.OptionStatService.RISK_FREE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public int getBlackScholes30() {
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility30(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
    }

    public int getBlackScholes60() {
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility60(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
    }

    public int getBlackScholes90() {
        return (int) BlackScholes.callPrice(baseStockWatch.getLast(), option.getStrikePrice(), RISK_FREE, option.getInstrument().getVolatility90(), ChronoUnit.DAYS.between(option.getExpDate(), LocalDate.now()));
    }

    public float getPutAskPriceToBS() {
        if (putBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((putBidAsk.getAskPrice() * 1.0 / getBlackScholes30() - 1) * 100.0) / 100.0);
    }

    public float getCallAskPriceToBS() {
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return (float) (Math.round((callBidAsk.getAskPrice() * 1.0 / getBlackScholes30() - 1) * 100.0) / 100.0);
    }

    public Integer getCallEffectivePrice() {
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return option.getStrikePrice() + callBidAsk.getAskPrice();
    }

    public Integer getPutEffectivePrice() {
        if (putBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return option.getStrikePrice() - putBidAsk.getAskPrice();
    }

    public float getCallBreakEven() {
        return (float) (Math.round((getCallEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 100.0) / 100.0);
    }

    public float getPutBreakEven() {
        return (float) (Math.round((getPutEffectivePrice() * 1.0 / baseStockWatch.getLast() - 1) * 100.0) / 100.0);
    }


}

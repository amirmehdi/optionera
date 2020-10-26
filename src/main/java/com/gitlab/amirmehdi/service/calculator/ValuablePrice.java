package com.gitlab.amirmehdi.service.calculator;

public class ValuablePrice {
    private static final double staticThreshold1 = -5.5;
    private static final double staticThreshold2 = -1;
    private static final double lastPriceWeight = 2.5;
    private static final double closePriceWeight = 1.5;
    private static final double lastToCloseWeight = 0.2;

    public static long calc(int closePrice, int lastPrice, int refPrice, int maxPrice) {
        double dynamicThreshold = staticThreshold1 + (lastPriceWeight * Math.pow(1.0 * lastPrice / refPrice - 1, 5) + closePriceWeight * (1.0 * closePrice / refPrice - 1) * 5) /
            ((1.0 * maxPrice / refPrice - 1) * 5);
        double valuablePrice1 = Math.round((closePrice + lastToCloseWeight * lastPrice) / ((lastToCloseWeight + 1) * (1 + dynamicThreshold / 100)));
        double valuablePrice2 = Math.round(lastPrice * (1 + staticThreshold2 / 100));
        return (long) Math.min(valuablePrice1, valuablePrice2);
    }
}

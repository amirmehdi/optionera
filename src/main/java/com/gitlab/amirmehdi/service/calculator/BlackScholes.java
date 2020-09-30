package com.gitlab.amirmehdi.service.calculator;

public class BlackScholes {

    static double vHigh = 100;
    static double vvHigh = 1E+30;

    public static double getBSCall(double underlyingAssetPrice, double strikePrice, double riskFree, double volatility, double totalUntilExpPerYear) {
        // Black Scholes call price
        double d1 = BSSafeD1(underlyingAssetPrice, strikePrice, riskFree, volatility, totalUntilExpPerYear);
        double d2 = getD2(d1, volatility, totalUntilExpPerYear);
        return Math.exp(-riskFree * totalUntilExpPerYear) * (underlyingAssetPrice * Math.exp(riskFree * totalUntilExpPerYear) * normalCDF(d1) - strikePrice * normalCDF(d2));
    }


    public static double getBSPut(double underlyingAssetPrice, double strikePrice, double riskFree, double volatility, double totalUntilExpPerYear) {
        // Black Scholes put price
        double d1 = BSSafeD1(underlyingAssetPrice, strikePrice, riskFree, volatility, totalUntilExpPerYear);
        double d2 = getD2(d1, volatility, totalUntilExpPerYear);
        return Math.exp(-riskFree * totalUntilExpPerYear) * (-underlyingAssetPrice * Math.exp(riskFree * totalUntilExpPerYear) * normalCDF(-d1) + strikePrice * normalCDF(-d2));
    }

    private static double BSSafeD1(double underlyingAssetPrice, double strikePrice, double riskFree, double volatility, double totalUntilExpPerYear) {
        // This computes the BlackScholes quantity d1 safely i.e.
        // no division by zero and no log of zero
        double s0;
        if (volatility == 0 || totalUntilExpPerYear == 0) {
            s0 = underlyingAssetPrice * Math.exp((riskFree + volatility * volatility / 2) * totalUntilExpPerYear);
            if (s0 > strikePrice) {
                return vHigh;
            }
            if (s0 < strikePrice) {
                return -vHigh;
            }
            return 0.0; // if (s0 == X)
        } else {
            if (strikePrice == 0) {
                return vHigh;
            } else {
                //Below is the BlackScholes formula for d1
                return (Math.log(underlyingAssetPrice / strikePrice) + (riskFree + volatility * volatility / 2) * totalUntilExpPerYear) / (volatility * Math.sqrt(totalUntilExpPerYear));
            }
        }
    }

    private static double getD2(double d1, double volatility, double totalUntilExpPerYear) {
        return d1 - volatility * Math.sqrt(totalUntilExpPerYear);
    }

    private static double normalCDF(double x) {
        double a1 = 0.319381530;
        double a2 = -0.356563782;
        double a3 = 1.781477937;
        double a4 = -1.821255978;
        double a5 = 1.330274429;

        double gamma = 0.2316419;
        double precision = 1e-6;

        if (x >= 0.0) {
            double k = 1.0 / (1.0 + gamma * x);
            double temp = NormOrdinate(x) * k *
                (a1 + k * (a2 + k * (a3 + k * (a4 + k * a5))));
            if (temp < precision)
                return 1.0;
            temp = 1.0 - temp;
            if (temp < precision)
                return 0.0;
            return temp;
        } else { // if(x < 0.0)
            return 1.0 - normalCDF(-x);
        }
    }


    private static double NormOrdinate(double z) {
        // The normal ordinate (probability density function)
        return Math.exp(-0.5 * z * z) / Math.sqrt(2 * Math.PI);
    }

}

package com.gitlab.amirmehdi.service.calculator;

public class BlackScholes {

/*
*  % java BlackScholes 23.75 15.00 0.01 0.35 0.5

*      Microsoft:   share price:             23.75
*                   strike price:            15.00
*                   risk-free interest rate:  1%
*                   volatility:              35%          (historical estimate)
*                   time until expiration:    0.5 years
*/
    // Black-Scholes formula
    public static double callPrice(double sharePrice, double strikePrice, double riskFree, double volatility, double totalUntilExp) {
        double a = (Math.log(sharePrice / strikePrice) + (riskFree + volatility * volatility / 2) * totalUntilExp) / (volatility * Math.sqrt(totalUntilExp));
        double b = a - volatility * Math.sqrt(totalUntilExp);
        return sharePrice * Gaussian.Phi(a) - strikePrice * Math.exp(-riskFree * totalUntilExp) * Gaussian.Phi(b);
    }
}

package com.gitlab.amirmehdi.service.calculator;//_________________________________________________________________________
//
//    Copyright (C) 2011  Prof. Jayanth R. Varma, jrvarma@iima.ac.in,
//    Indian Institute of Management, Ahmedabad 380 015, INDIA
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program (see file COPYING); if not, write to the
//    Free Software Foundation, Inc., 59 Temple Place, Suite 330,
//    Boston, MA  02111-1307  USA
//
//_________________________________________________________________________

// This defines a number of functions related to the Black-Scholes
// option pricing formula. This includes Black-Scholes call and put
// prices, Black-Scholes call and put implied volatilities and the various
// option greeks - delta, gamma, vega, theta and rho


/**
 * https://github.com/jrvarma/black-scholes-java/blob/master/src/BlackScholes.java
 */
@Deprecated
public class BlackScholesNonStatic {
    static double vHigh = 100;
    static double vvHigh = 1E+30;
    // The constant pi
    static double Pi = 3.141592653589793;
    double d1, d2, g;
    private double underlyingAssetPrice, strikePrice, riskFree, volatility, totalUntilExpPerYear;
    private double div_yield = 0;

    BlackScholesNonStatic() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    BlackScholesNonStatic(double s_, double X_, double r_, double Sigma_, double t_) {
        this(s_, X_, r_, Sigma_, t_, 0);
    }

    BlackScholesNonStatic(double s_, double X_, double r_, double Sigma_, double t_, double div_yield_) {
        underlyingAssetPrice = s_;
        strikePrice = X_;
        riskFree = r_;
        volatility = Sigma_;
        totalUntilExpPerYear = t_;
        div_yield = div_yield_;
        g = riskFree - div_yield;
        d1 = BSSafeD1();
        d2 = d1 - volatility * Math.sqrt(totalUntilExpPerYear);
    }

    private double BSSafeD1() {
        // This computes the BlackScholes quantity d1 safely i.e.
        // no division by zero and no log of zero
        double s0;
        if (volatility == 0 || totalUntilExpPerYear == 0) {
            s0 = underlyingAssetPrice * Math.exp((g + volatility * volatility / 2) * totalUntilExpPerYear);
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
                return (Math.log(underlyingAssetPrice / strikePrice) + (g + volatility * volatility / 2) * totalUntilExpPerYear) / (volatility * Math.sqrt(totalUntilExpPerYear));
            }
        }
    }

    void reset(double s_, double X_, double r_, double Sigma_, double t_) {
        reset(s_, X_, r_, Sigma_, t_, 0);
    }

    void reset(double s_, double X_, double r_, double Sigma_, double t_, double div_yield_) {
        underlyingAssetPrice = s_;
        strikePrice = X_;
        riskFree = r_;
        volatility = Sigma_;
        totalUntilExpPerYear = t_;
        div_yield = div_yield_;
        g = riskFree - div_yield;
        d1 = BSSafeD1();
        d2 = d1 - volatility * Math.sqrt(totalUntilExpPerYear);
    }

    void reset(double Sigma_) {
        volatility = Sigma_;
        d1 = BSSafeD1();
        d2 = d1 - volatility * Math.sqrt(totalUntilExpPerYear);
    }

    double BSCall() {
        // Black Scholes call price
        return Math.exp(-riskFree * totalUntilExpPerYear) * (underlyingAssetPrice * Math.exp(g * totalUntilExpPerYear) * NormalCDF(d1) - strikePrice * NormalCDF(d2));
    }

    double BSPut() {
        // Black Scholes put price
        return Math.exp(-riskFree * totalUntilExpPerYear) * (-underlyingAssetPrice * Math.exp(g * totalUntilExpPerYear) * NormalCDF(-d1) + strikePrice * NormalCDF(-d2));
    }

    double BSCallDelta() {
        // Black Scholes call delta
        return Math.exp(-div_yield * totalUntilExpPerYear) * NormalCDF(d1);
    }

    double BSPutDelta() {
        return -Math.exp(-div_yield * totalUntilExpPerYear) * NormalCDF(-d1);
    }

    double BSCallTheta() {
        // Black Scholes call theta
        double a, b, c;
        if (totalUntilExpPerYear == 0) {
            if (Math.abs(d1) == vHigh || volatility == 0) {
                a = 0;
            } else {
                a = -vvHigh;
            }
        } else {
            a = -underlyingAssetPrice * NormOrdinate(d1) * volatility * Math.exp(-div_yield * totalUntilExpPerYear) / (2 * Math.sqrt(totalUntilExpPerYear));
        }
        b = riskFree * strikePrice * Math.exp(-riskFree * totalUntilExpPerYear) * NormalCDF(d2);
        c = div_yield * underlyingAssetPrice * NormalCDF(d1) * Math.exp(-div_yield * totalUntilExpPerYear);
        return a - b + c;
    }

    double BSPutTheta() {
        // Black Scholes put theta
        double a, b, c;
        if (totalUntilExpPerYear == 0) {
            if (Math.abs(d1) == vHigh || volatility == 0) {
                a = 0;
            } else {
                a = -vvHigh;
            }
        } else {
            a = -underlyingAssetPrice * NormOrdinate(d1) * volatility * Math.exp(-div_yield * totalUntilExpPerYear) / (2 * Math.sqrt(totalUntilExpPerYear));
        }
        b = riskFree * strikePrice * Math.exp(-riskFree * totalUntilExpPerYear) * NormalCDF(-d2);
        c = div_yield * underlyingAssetPrice * NormalCDF(-d1) * Math.exp(-div_yield * totalUntilExpPerYear);
        return a + b - c;
    }

    double BSGamma() {
        // Black Scholes call/put gamma
        if (volatility == 0 || totalUntilExpPerYear == 0) {
            if (Math.abs(d1) == vHigh) {
                return 0;
            } else {
                return vvHigh;
            }
        } else {
            return NormOrdinate(d1) * Math.exp(-div_yield * totalUntilExpPerYear) / (underlyingAssetPrice * volatility * Math.sqrt(totalUntilExpPerYear));
        }
    }


    double BSVega() {
        // Black Scholes call/put vega
        return underlyingAssetPrice * Math.sqrt(totalUntilExpPerYear) * NormOrdinate(d1) * Math.exp(-div_yield * totalUntilExpPerYear);
    }

    double BSCallRho() {
        return strikePrice * totalUntilExpPerYear * Math.exp(-riskFree * totalUntilExpPerYear) * NormalCDF(d2);
    }

    double BSFuturesCallRho() {
        return -totalUntilExpPerYear * BSCall();
    }

    double BSFuturesPutRho() {
        return -totalUntilExpPerYear * BSPut();
    }

    double BSPutRho() {
        // Black Scholes put rho
        return -strikePrice * totalUntilExpPerYear * Math.exp(-riskFree * totalUntilExpPerYear) * NormalCDF(-d2);
    }

    double BSCallProb() {
        // Black Scholes risk neutral probability of put exercise
        return NormalCDF(d2);
    }

    double BSPutProb() {
        // Black Scholes risk neutral probability of put exercise
        return NormalCDF(-d2);
    }


    double NormOrdinate(double z) {
        // The normal ordinate (probability density function)
        return Math.exp(-0.5 * z * z) / Math.sqrt(2 * Pi);
    }

    double NormalCDF_old(double y) {
        // Computes normal integral by using a rational polynomial approximation
        // This approximation is from Example 9.7.3 of
        // Fike, C.T. (1968), Computer Evaluation of Mathematical Functions
        // Englewood Cliffs, N.J., Prentice Hall
        // Let P(x) be the integral of the normal density from 0 to x. {,
        // the best minimax approximation R(x) to P(x) in the range [0,infinity)
        // among the class of rational functions V5,5[0,infinity) satisfying
        // R(0) = P(0) = 0, and
        // lim x tends to infinity R(x) = lim x tends to infinity P(x) = 0.5
        // is the function:
        //       a1 + a2*x + a3*x^2 + a4*x^3  + a5*x^4  + a6*x^5
        //     ----------------------------------------------------
        //       b1 + b2*x + b3*x^2 + b4*x^3  + b5*x^4  + b6*x^5
        // where the constants a1, a2, ..., a6 and b1, b2, ..., b6
        // are as defined below
        // The maximum absolute error of this approximation is 0.46x10^-4
        // i.e. 0.000046. Therefore, this approximation has the same accuracy
        // as the 4 place tables commonly found in statistics test books

        final double a1 = 0, a2 = 9.050508, a3 = 0.767742, a4 = 1.666902, a5 = -0.624298, a6 = 0.5,
            b1 = 22.601228, B2 = 2.776898, b3 = 5.148169, b4 = 2.995582, b5 = -1.238661, b6 = 1;

        // We now compute R(abs(y)) as an approximation to P(abs(y))

        double X = Math.abs(y);
        double temp1 = ((((a6 * X + a5) * X + a4) * X + a3) * X + a2) * X + a1;
        double temp2 = ((((b6 * X + b5) * X + b4) * X + b3) * X + B2) * X + b1;
        double Temp = temp1 / temp2;

        // We now compute the normal integral N(y) from P(abs(y))

        if (y < 0) {
            return 0.5 - Temp;
        } else {
            return 0.5 + Temp;
        }
    }

    double NormalCDF(double x) {
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
            return 1.0 - NormalCDF(-x);
        }
    }

    // replicates Sgn as in visual basic, the signum of a real number
    double sgn(double x) {
        if (x > 0)
            return 1.0;
        else if (x < 0)
            return -1.0;
        else
            return 0.0;
    }

}

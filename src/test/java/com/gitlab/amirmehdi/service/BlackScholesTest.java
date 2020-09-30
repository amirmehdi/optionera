package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.service.calculator.BlackScholes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.offset;

public class BlackScholesTest {
    @Test
    public void test1(){
        Assertions
            .assertThat(BlackScholes.getBSCall(1000,1100,0.35,0.6,100/365.0))
            .isCloseTo(125.03,offset(0.01));

        Assertions
            .assertThat(BlackScholes.getBSPut(1000,1100,0.35,0.6,100/365.0))
            .isCloseTo(124.45,offset(0.01));
    }
}

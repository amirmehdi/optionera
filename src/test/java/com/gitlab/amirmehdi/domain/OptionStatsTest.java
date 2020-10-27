package com.gitlab.amirmehdi.domain;

import com.gitlab.amirmehdi.service.calculator.ValuablePrice;
import com.gitlab.amirmehdi.web.rest.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionStatsTest {
    @Test
    public void testITMCall() throws Exception {
//        stike = 2_500
//        contractSize= 1_000
//        baseInstrumnetClose = 3_000
//        callOptionSettlement = 1_000
//
//        callMargin = 1_700_000
    }

    @Test
    public void testOTMCall() throws Exception {
//        stike = 3_500
//        contractSize= 1_000
//        baseInstrumnetClose = 3_000
//        callOptionSettlement = 500
//
//        callMargin = 900_000
    }

    @Test
    public void testITMPut() throws Exception {
//        stike = 3_500
//        contractSize= 1_000
//        baseInstrumnetClose = 3_000
//        callOptionSettlement = 1_000
//
//        callMargin = 1_700_000
    }

    @Test
    public void testOTMPut() throws Exception {
//        stike = 2_500
//        contractSize= 1_000
//        baseInstrumnetClose = 3_000
//        callOptionSettlement = 500
//
//        callMargin = 1_300_000
    }
}

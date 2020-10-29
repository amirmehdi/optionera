package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionStatsTest {

    @Autowired
    private ObjectMapper objectMapper;
    private OptionStats optionStats = null;

    @BeforeEach
    public void initOption() throws JsonProcessingException {
        optionStats = objectMapper.readValue("{\"option\":{\"id\":142590,\"name\":\"ترا80301\",\"callIsin\":\"IRO9PTEH2631\",\"putIsin\":\"IROFPTEH3631\",\"callTseId\":\"63926015041995281\",\"putTseId\":\"37471562331957752\",\"expDate\":\"2020-11-01\",\"strikePrice\":8113,\"contractSize\":1705,\"callInTheMoney\":true,\"callBreakEven\":-10.29,\"putBreakEven\":2.14748365E9,\"callAskToBS\":-33.3,\"putAskToBS\":2.14748365E9,\"callLeverage\":4.73,\"putLeverage\":0.0,\"createdAt\":\"2020-10-13T12:08:39\",\"updatedAt\":\"2020-10-28T11:48:47\",\"instrument\":{\"isin\":\"IRO1PTEH0001\",\"name\":\"شتران\",\"tseId\":\"51617145873056483\",\"volatility30\":0.6053854205608282,\"volatility60\":0.7184390486345438,\"volatility90\":0.659736400266889,\"updatedAt\":\"2020-09-27\"}},\"putStockWatch\":{\"isin\":\"IROFPTEH3631\",\"last\":1,\"closing\":1,\"first\":0,\"high\":0,\"low\":0,\"min\":1,\"max\":100000,\"state\":\"A\",\"tradeValue\":0,\"tradeVolume\":0,\"tradesCount\":0,\"referencePrice\":1,\"lastTrade\":null,\"openInterest\":0,\"settlementPrice\":0},\"putBidAsk\":{\"bidNumber\":0,\"bidPrice\":0,\"bidQuantity\":0,\"askNumber\":0,\"askPrice\":0,\"askQuantity\":0},\"callStockWatch\":{\"isin\":\"IRO9PTEH2631\",\"last\":2000,\"closing\":1691,\"first\":1800,\"high\":2000,\"low\":1500,\"min\":1,\"max\":100000,\"state\":\"A\",\"tradeValue\":654534155,\"tradeVolume\":227,\"tradesCount\":18,\"referencePrice\":2308,\"lastTrade\":\"2020-10-28T11:51:11\",\"openInterest\":0,\"settlementPrice\":0},\"callBidAsk\":{\"bidNumber\":1,\"bidPrice\":1801,\"bidQuantity\":7,\"askNumber\":1,\"askPrice\":2500,\"askQuantity\":11},\"baseStockWatch\":{\"isin\":\"IRO1PTEH0001\",\"last\":11830,\"closing\":11830,\"first\":11830,\"high\":11830,\"low\":11830,\"min\":11830,\"max\":13070,\"state\":\"A\",\"tradeValue\":531610447550,\"tradeVolume\":44937485,\"tradesCount\":6138,\"referencePrice\":12450,\"lastTrade\":\"2020-10-28T12:29:55\",\"openInterest\":0,\"settlementPrice\":0},\"baseBidAsk\":{\"bidNumber\":0,\"bidPrice\":0,\"bidQuantity\":0,\"askNumber\":8166,\"askPrice\":11830,\"askQuantity\":59185503},\"callEffectivePrice\":10613,\"putEffectivePrice\":2147483647,\"id\":142590,\"callBS30\":3748,\"callBS60\":3748,\"callBS90\":3748,\"putBS30\":0,\"putBS60\":0,\"putBS90\":0}"
        ,OptionStats.class);
    }

    @Test
    public void testITMCall() {
        optionStats.getOption().setStrikePrice(2500);
        optionStats.getOption().setContractSize(1000);
        optionStats.getBaseStockWatch().setClosing(3000);
        optionStats.getCallStockWatch().setSettlementPrice(1000);
        Assertions.assertThat(optionStats.getCallMargin()).isEqualTo(1_700_000);
    }

    @Test
    public void testOTMCall() {
        optionStats.getOption().setStrikePrice(3_500);
        optionStats.getOption().setContractSize(1_000);
        optionStats.getBaseStockWatch().setClosing(3_000);
        optionStats.getCallStockWatch().setSettlementPrice(500);
        Assertions.assertThat(optionStats.getCallMargin()).isEqualTo(900_000);
    }

    @Test
    public void testITMPut() {
        optionStats.getOption().setStrikePrice(3_500);
        optionStats.getOption().setContractSize(1_000);
        optionStats.getBaseStockWatch().setClosing(3_000);
        optionStats.getPutStockWatch().setSettlementPrice(1_000);
        Assertions.assertThat(optionStats.getPutMargin()).isEqualTo(1_700_000);
    }

    @Test
    public void testOTMPut() {
        optionStats.getOption().setStrikePrice(2_500);
        optionStats.getOption().setContractSize(1_000);
        optionStats.getBaseStockWatch().setClosing(3_000);
        optionStats.getPutStockWatch().setSettlementPrice(500);
        Assertions.assertThat(optionStats.getPutMargin()).isEqualTo(800_000);
    }
}

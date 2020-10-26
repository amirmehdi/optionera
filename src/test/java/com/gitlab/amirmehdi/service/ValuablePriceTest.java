package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.service.calculator.ValuablePrice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValuablePriceTest {
    @Test
    public void test1() {
        Assertions.assertThat(ValuablePrice.calc(1050,1000,1000,1050))
        .isEqualTo(990);
    }

    @Test
    public void test2() {
        Assertions.assertThat(ValuablePrice.calc(1020,950,1000,1050))
            .isEqualTo(928);
    }
}

package com.gitlab.amirmehdi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gitlab.amirmehdi.web.rest.TestUtil;

public class InstrumentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instrument.class);
        Instrument instrument1 = new Instrument();
        instrument1.setIsin("1");
        Instrument instrument2 = new Instrument();
        instrument2.setIsin(instrument1.getIsin());
        assertThat(instrument1).isEqualTo(instrument2);
        instrument2.setIsin("2");
        assertThat(instrument1).isNotEqualTo(instrument2);
        instrument1.setIsin(null);
        assertThat(instrument1).isNotEqualTo(instrument2);
    }
}

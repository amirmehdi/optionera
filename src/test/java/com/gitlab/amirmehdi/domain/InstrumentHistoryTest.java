package com.gitlab.amirmehdi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gitlab.amirmehdi.web.rest.TestUtil;

public class InstrumentHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstrumentHistory.class);
        InstrumentHistory instrumentHistory1 = new InstrumentHistory();
        instrumentHistory1.setId(1L);
        InstrumentHistory instrumentHistory2 = new InstrumentHistory();
        instrumentHistory2.setId(instrumentHistory1.getId());
        assertThat(instrumentHistory1).isEqualTo(instrumentHistory2);
        instrumentHistory2.setId(2L);
        assertThat(instrumentHistory1).isNotEqualTo(instrumentHistory2);
        instrumentHistory1.setId(null);
        assertThat(instrumentHistory1).isNotEqualTo(instrumentHistory2);
    }
}

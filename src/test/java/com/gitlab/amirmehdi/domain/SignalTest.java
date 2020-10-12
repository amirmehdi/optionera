package com.gitlab.amirmehdi.domain;

import com.gitlab.amirmehdi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signal.class);
        Signal signal1 = new Signal();
        signal1.setId(1L);
        Signal signal2 = new Signal();
        signal2.setId(signal1.getId());
        assertThat(signal1).isEqualTo(signal2);
        signal2.setId(2L);
        assertThat(signal1).isNotEqualTo(signal2);
        signal1.setId(null);
        assertThat(signal1).isNotEqualTo(signal2);
    }
}

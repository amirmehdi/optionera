package com.gitlab.amirmehdi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gitlab.amirmehdi.web.rest.TestUtil;

public class EmbeddedOptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmbeddedOption.class);
        EmbeddedOption embeddedOption1 = new EmbeddedOption();
        embeddedOption1.setId(1L);
        EmbeddedOption embeddedOption2 = new EmbeddedOption();
        embeddedOption2.setId(embeddedOption1.getId());
        assertThat(embeddedOption1).isEqualTo(embeddedOption2);
        embeddedOption2.setId(2L);
        assertThat(embeddedOption1).isNotEqualTo(embeddedOption2);
        embeddedOption1.setId(null);
        assertThat(embeddedOption1).isNotEqualTo(embeddedOption2);
    }
}

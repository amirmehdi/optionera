package com.gitlab.amirmehdi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gitlab.amirmehdi.web.rest.TestUtil;

public class BourseCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BourseCode.class);
        BourseCode bourseCode1 = new BourseCode();
        bourseCode1.setId(1L);
        BourseCode bourseCode2 = new BourseCode();
        bourseCode2.setId(bourseCode1.getId());
        assertThat(bourseCode1).isEqualTo(bourseCode2);
        bourseCode2.setId(2L);
        assertThat(bourseCode1).isNotEqualTo(bourseCode2);
        bourseCode1.setId(null);
        assertThat(bourseCode1).isNotEqualTo(bourseCode2);
    }
}

package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgorithmDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlgorithmDTO.class);
        AlgorithmDTO algorithmDTO1 = new AlgorithmDTO();
        algorithmDTO1.setId(1L);
        AlgorithmDTO algorithmDTO2 = new AlgorithmDTO();
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
        algorithmDTO2.setId(algorithmDTO1.getId());
        assertThat(algorithmDTO1).isEqualTo(algorithmDTO2);
        algorithmDTO2.setId(2L);
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
        algorithmDTO1.setId(null);
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
    }
}

package com.gitlab.amirmehdi.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgorithmMapperTest {

    private AlgorithmMapper algorithmMapper;

    @BeforeEach
    public void setUp() {
        algorithmMapper = new AlgorithmMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(algorithmMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(algorithmMapper.fromId(null)).isNull();
    }
}

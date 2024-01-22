package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CancellationManagementMapperTest {

    private CancellationManagementMapper cancellationManagementMapper;

    @BeforeEach
    public void setUp() {
        cancellationManagementMapper = new CancellationManagementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cancellationManagementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cancellationManagementMapper.fromId(null)).isNull();
    }
}

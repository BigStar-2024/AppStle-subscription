package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncUpdateEventProcessingMapperTest {

    private AsyncUpdateEventProcessingMapper asyncUpdateEventProcessingMapper;

    @BeforeEach
    public void setUp() {
        asyncUpdateEventProcessingMapper = new AsyncUpdateEventProcessingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(asyncUpdateEventProcessingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(asyncUpdateEventProcessingMapper.fromId(null)).isNull();
    }
}

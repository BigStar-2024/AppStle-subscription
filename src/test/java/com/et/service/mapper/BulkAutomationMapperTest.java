package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BulkAutomationMapperTest {

    private BulkAutomationMapper bulkAutomationMapper;

    @BeforeEach
    public void setUp() {
        bulkAutomationMapper = new BulkAutomationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bulkAutomationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bulkAutomationMapper.fromId(null)).isNull();
    }
}

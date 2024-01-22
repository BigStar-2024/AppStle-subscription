package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ActivityLogMapperTest {

    private ActivityLogMapper activityLogMapper;

    @BeforeEach
    public void setUp() {
        activityLogMapper = new ActivityLogMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(activityLogMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(activityLogMapper.fromId(null)).isNull();
    }
}

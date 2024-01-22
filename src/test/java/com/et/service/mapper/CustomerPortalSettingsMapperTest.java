package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerPortalSettingsMapperTest {

    private CustomerPortalSettingsMapper customerPortalSettingsMapper;

    @BeforeEach
    public void setUp() {
        customerPortalSettingsMapper = new CustomerPortalSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(customerPortalSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customerPortalSettingsMapper.fromId(null)).isNull();
    }
}

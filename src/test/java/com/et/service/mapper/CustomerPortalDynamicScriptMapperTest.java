package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerPortalDynamicScriptMapperTest {

    private CustomerPortalDynamicScriptMapper customerPortalDynamicScriptMapper;

    @BeforeEach
    public void setUp() {
        customerPortalDynamicScriptMapper = new CustomerPortalDynamicScriptMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(customerPortalDynamicScriptMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customerPortalDynamicScriptMapper.fromId(null)).isNull();
    }
}

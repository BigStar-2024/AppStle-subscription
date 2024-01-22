package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AsyncUpdateEventProcessingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsyncUpdateEventProcessingDTO.class);
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO1 = new AsyncUpdateEventProcessingDTO();
        asyncUpdateEventProcessingDTO1.setId(1L);
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO2 = new AsyncUpdateEventProcessingDTO();
        assertThat(asyncUpdateEventProcessingDTO1).isNotEqualTo(asyncUpdateEventProcessingDTO2);
        asyncUpdateEventProcessingDTO2.setId(asyncUpdateEventProcessingDTO1.getId());
        assertThat(asyncUpdateEventProcessingDTO1).isEqualTo(asyncUpdateEventProcessingDTO2);
        asyncUpdateEventProcessingDTO2.setId(2L);
        assertThat(asyncUpdateEventProcessingDTO1).isNotEqualTo(asyncUpdateEventProcessingDTO2);
        asyncUpdateEventProcessingDTO1.setId(null);
        assertThat(asyncUpdateEventProcessingDTO1).isNotEqualTo(asyncUpdateEventProcessingDTO2);
    }
}

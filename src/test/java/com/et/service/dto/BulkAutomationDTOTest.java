package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BulkAutomationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BulkAutomationDTO.class);
        BulkAutomationDTO bulkAutomationDTO1 = new BulkAutomationDTO();
        bulkAutomationDTO1.setId(1L);
        BulkAutomationDTO bulkAutomationDTO2 = new BulkAutomationDTO();
        assertThat(bulkAutomationDTO1).isNotEqualTo(bulkAutomationDTO2);
        bulkAutomationDTO2.setId(bulkAutomationDTO1.getId());
        assertThat(bulkAutomationDTO1).isEqualTo(bulkAutomationDTO2);
        bulkAutomationDTO2.setId(2L);
        assertThat(bulkAutomationDTO1).isNotEqualTo(bulkAutomationDTO2);
        bulkAutomationDTO1.setId(null);
        assertThat(bulkAutomationDTO1).isNotEqualTo(bulkAutomationDTO2);
    }
}

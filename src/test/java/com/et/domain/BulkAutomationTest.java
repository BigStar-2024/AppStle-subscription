package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BulkAutomationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BulkAutomation.class);
        BulkAutomation bulkAutomation1 = new BulkAutomation();
        bulkAutomation1.setId(1L);
        BulkAutomation bulkAutomation2 = new BulkAutomation();
        bulkAutomation2.setId(bulkAutomation1.getId());
        assertThat(bulkAutomation1).isEqualTo(bulkAutomation2);
        bulkAutomation2.setId(2L);
        assertThat(bulkAutomation1).isNotEqualTo(bulkAutomation2);
        bulkAutomation1.setId(null);
        assertThat(bulkAutomation1).isNotEqualTo(bulkAutomation2);
    }
}

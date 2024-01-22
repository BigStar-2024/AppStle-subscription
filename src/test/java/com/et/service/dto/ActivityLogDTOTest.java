package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ActivityLogDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityLogDTO.class);
        ActivityLogDTO activityLogDTO1 = new ActivityLogDTO();
        activityLogDTO1.setId(1L);
        ActivityLogDTO activityLogDTO2 = new ActivityLogDTO();
        assertThat(activityLogDTO1).isNotEqualTo(activityLogDTO2);
        activityLogDTO2.setId(activityLogDTO1.getId());
        assertThat(activityLogDTO1).isEqualTo(activityLogDTO2);
        activityLogDTO2.setId(2L);
        assertThat(activityLogDTO1).isNotEqualTo(activityLogDTO2);
        activityLogDTO1.setId(null);
        assertThat(activityLogDTO1).isNotEqualTo(activityLogDTO2);
    }
}

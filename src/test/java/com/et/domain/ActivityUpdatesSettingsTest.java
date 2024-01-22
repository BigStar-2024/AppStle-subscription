package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ActivityUpdatesSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityUpdatesSettings.class);
        ActivityUpdatesSettings activityUpdatesSettings1 = new ActivityUpdatesSettings();
        activityUpdatesSettings1.setId(1L);
        ActivityUpdatesSettings activityUpdatesSettings2 = new ActivityUpdatesSettings();
        activityUpdatesSettings2.setId(activityUpdatesSettings1.getId());
        assertThat(activityUpdatesSettings1).isEqualTo(activityUpdatesSettings2);
        activityUpdatesSettings2.setId(2L);
        assertThat(activityUpdatesSettings1).isNotEqualTo(activityUpdatesSettings2);
        activityUpdatesSettings1.setId(null);
        assertThat(activityUpdatesSettings1).isNotEqualTo(activityUpdatesSettings2);
    }
}

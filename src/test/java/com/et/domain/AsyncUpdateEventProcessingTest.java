package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AsyncUpdateEventProcessingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsyncUpdateEventProcessing.class);
        AsyncUpdateEventProcessing asyncUpdateEventProcessing1 = new AsyncUpdateEventProcessing();
        asyncUpdateEventProcessing1.setId(1L);
        AsyncUpdateEventProcessing asyncUpdateEventProcessing2 = new AsyncUpdateEventProcessing();
        asyncUpdateEventProcessing2.setId(asyncUpdateEventProcessing1.getId());
        assertThat(asyncUpdateEventProcessing1).isEqualTo(asyncUpdateEventProcessing2);
        asyncUpdateEventProcessing2.setId(2L);
        assertThat(asyncUpdateEventProcessing1).isNotEqualTo(asyncUpdateEventProcessing2);
        asyncUpdateEventProcessing1.setId(null);
        assertThat(asyncUpdateEventProcessing1).isNotEqualTo(asyncUpdateEventProcessing2);
    }
}

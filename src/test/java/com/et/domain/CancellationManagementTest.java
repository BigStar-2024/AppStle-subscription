package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CancellationManagementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancellationManagement.class);
        CancellationManagement cancellationManagement1 = new CancellationManagement();
        cancellationManagement1.setId(1L);
        CancellationManagement cancellationManagement2 = new CancellationManagement();
        cancellationManagement2.setId(cancellationManagement1.getId());
        assertThat(cancellationManagement1).isEqualTo(cancellationManagement2);
        cancellationManagement2.setId(2L);
        assertThat(cancellationManagement1).isNotEqualTo(cancellationManagement2);
        cancellationManagement1.setId(null);
        assertThat(cancellationManagement1).isNotEqualTo(cancellationManagement2);
    }
}

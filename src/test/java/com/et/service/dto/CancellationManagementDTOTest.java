package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CancellationManagementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancellationManagementDTO.class);
        CancellationManagementDTO cancellationManagementDTO1 = new CancellationManagementDTO();
        cancellationManagementDTO1.setId(1L);
        CancellationManagementDTO cancellationManagementDTO2 = new CancellationManagementDTO();
        assertThat(cancellationManagementDTO1).isNotEqualTo(cancellationManagementDTO2);
        cancellationManagementDTO2.setId(cancellationManagementDTO1.getId());
        assertThat(cancellationManagementDTO1).isEqualTo(cancellationManagementDTO2);
        cancellationManagementDTO2.setId(2L);
        assertThat(cancellationManagementDTO1).isNotEqualTo(cancellationManagementDTO2);
        cancellationManagementDTO1.setId(null);
        assertThat(cancellationManagementDTO1).isNotEqualTo(cancellationManagementDTO2);
    }
}

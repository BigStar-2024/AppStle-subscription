package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class EmailTemplateSettingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailTemplateSetting.class);
        EmailTemplateSetting emailTemplateSetting1 = new EmailTemplateSetting();
        emailTemplateSetting1.setId(1L);
        EmailTemplateSetting emailTemplateSetting2 = new EmailTemplateSetting();
        emailTemplateSetting2.setId(emailTemplateSetting1.getId());
        assertThat(emailTemplateSetting1).isEqualTo(emailTemplateSetting2);
        emailTemplateSetting2.setId(2L);
        assertThat(emailTemplateSetting1).isNotEqualTo(emailTemplateSetting2);
        emailTemplateSetting1.setId(null);
        assertThat(emailTemplateSetting1).isNotEqualTo(emailTemplateSetting2);
    }
}

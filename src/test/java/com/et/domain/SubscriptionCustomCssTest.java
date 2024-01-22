package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionCustomCssTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionCustomCss.class);
        SubscriptionCustomCss subscriptionCustomCss1 = new SubscriptionCustomCss();
        subscriptionCustomCss1.setId(1L);
        SubscriptionCustomCss subscriptionCustomCss2 = new SubscriptionCustomCss();
        subscriptionCustomCss2.setId(subscriptionCustomCss1.getId());
        assertThat(subscriptionCustomCss1).isEqualTo(subscriptionCustomCss2);
        subscriptionCustomCss2.setId(2L);
        assertThat(subscriptionCustomCss1).isNotEqualTo(subscriptionCustomCss2);
        subscriptionCustomCss1.setId(null);
        assertThat(subscriptionCustomCss1).isNotEqualTo(subscriptionCustomCss2);
    }
}

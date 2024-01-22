package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class DeliveryProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryProfile.class);
        DeliveryProfile deliveryProfile1 = new DeliveryProfile();
        deliveryProfile1.setId(1L);
        DeliveryProfile deliveryProfile2 = new DeliveryProfile();
        deliveryProfile2.setId(deliveryProfile1.getId());
        assertThat(deliveryProfile1).isEqualTo(deliveryProfile2);
        deliveryProfile2.setId(2L);
        assertThat(deliveryProfile1).isNotEqualTo(deliveryProfile2);
        deliveryProfile1.setId(null);
        assertThat(deliveryProfile1).isNotEqualTo(deliveryProfile2);
    }
}

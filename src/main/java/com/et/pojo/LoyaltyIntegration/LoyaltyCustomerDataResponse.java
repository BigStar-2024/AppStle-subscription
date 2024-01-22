package com.et.pojo.LoyaltyIntegration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoyaltyCustomerDataResponse {
    private Long availablePointsBalance;

    public Long getAvailablePointsBalance() {
        return availablePointsBalance;
    }

    public void setAvailablePointsBalance(Long availablePointsBalance) {
        this.availablePointsBalance = availablePointsBalance;
    }
}

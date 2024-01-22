package com.et.api.appstle.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerLoyalty {

    private Long availablePoints;

    private Long pendingPoints;

    private List<CustomerReward> rewards;

    public Long getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Long availablePoints) {
        this.availablePoints = availablePoints;
    }

    public Long getPendingPoints() {
        return pendingPoints;
    }

    public void setPendingPoints(Long pendingPoints) {
        this.pendingPoints = pendingPoints;
    }

    public List<CustomerReward> getRewards() {
        return rewards;
    }

    public void setRewards(List<CustomerReward> rewards) {
        this.rewards = rewards;
    }
}

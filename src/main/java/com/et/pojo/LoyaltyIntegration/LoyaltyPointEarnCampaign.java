package com.et.pojo.LoyaltyIntegration;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoyaltyPointEarnCampaign {
    private Long id;
    private String type;
    private String name;
    private ZonedDateTime createAt;
    private ZonedDateTime updateAt;
    private String details;
    private String reward_text;
    private String productData;
    private Integer basePoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReward_text() {
        return reward_text;
    }

    public void setReward_text(String reward_text) {
        this.reward_text = reward_text;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public Integer getBasePoints() {
        return basePoints;
    }

    public void setBasePoints(Integer basePoints) {
        this.basePoints = basePoints;
    }
}

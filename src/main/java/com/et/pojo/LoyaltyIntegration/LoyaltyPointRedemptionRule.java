package com.et.pojo.LoyaltyIntegration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoyaltyPointRedemptionRule {
    private Long id;
    private String icon;
    private String pointsCostText;
    private String description;
    private String type;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPointsCostText() {
        return pointsCostText;
    }

    public void setPointsCostText(String pointsCostText) {
        this.pointsCostText = pointsCostText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

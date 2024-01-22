package com.et.service.dto;

import java.io.Serializable;

public class ContractAnalytics implements Serializable {

    private Long totalOrderCount;
    private Double totalOrderAmount;
    private String totalOrderRevenue;

    public ContractAnalytics(Long totalOrderCount, Double totalOrderAmount) {
        this.totalOrderCount = totalOrderCount;
        this.totalOrderAmount = totalOrderAmount;
    }

    public Long getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Long totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(Double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getTotalOrderRevenue() {
        return totalOrderRevenue;
    }

    public void setTotalOrderRevenue(String totalOrderRevenue) {
        this.totalOrderRevenue = totalOrderRevenue;
    }

}

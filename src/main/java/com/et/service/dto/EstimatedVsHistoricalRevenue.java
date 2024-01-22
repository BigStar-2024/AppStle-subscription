package com.et.service.dto;

import java.io.Serializable;

public class EstimatedVsHistoricalRevenue implements Serializable {

    private String estimatedRevenueTotal;

    private String historicalRevenueTotal;

    private Double estimatedRevenueTotalNumerical;

    private Double historicalRevenueTotalNumerical;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstimatedRevenueTotal() {
        return estimatedRevenueTotal;
    }

    public void setEstimatedRevenueTotal(String estimatedRevenueTotal) {
        this.estimatedRevenueTotal = estimatedRevenueTotal;
    }

    public String getHistoricalRevenueTotal() {
        return historicalRevenueTotal;
    }

    public void setHistoricalRevenueTotal(String historicalRevenueTotal) {
        this.historicalRevenueTotal = historicalRevenueTotal;
    }

    public Double getEstimatedRevenueTotalNumerical() {
        return estimatedRevenueTotalNumerical;
    }

    public void setEstimatedRevenueTotalNumerical(Double estimatedRevenueTotalNumerical) {
        this.estimatedRevenueTotalNumerical = estimatedRevenueTotalNumerical;
    }

    public Double getHistoricalRevenueTotalNumerical() {
        return historicalRevenueTotalNumerical;
    }

    public void setHistoricalRevenueTotalNumerical(Double historicalRevenueTotalNumerical) {
        this.historicalRevenueTotalNumerical = historicalRevenueTotalNumerical;
    }
}

package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.Analytics} entity.
 */
public class AnalyticsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private Long totalSubscriptions;

    private Long totalOrders;

    private Double totalOrderAmount;

    private Long firstTimeOrders;

    private Long recurringOrders;

    private Long totalCustomers;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getTotalSubscriptions() {
        return totalSubscriptions;
    }

    public void setTotalSubscriptions(Long totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(Double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public Long getFirstTimeOrders() {
        return firstTimeOrders;
    }

    public void setFirstTimeOrders(Long firstTimeOrders) {
        this.firstTimeOrders = firstTimeOrders;
    }

    public Long getRecurringOrders() {
        return recurringOrders;
    }

    public void setRecurringOrders(Long recurringOrders) {
        this.recurringOrders = recurringOrders;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnalyticsDTO)) {
            return false;
        }

        return id != null && id.equals(((AnalyticsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnalyticsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", totalSubscriptions=" + getTotalSubscriptions() +
            ", totalOrders=" + getTotalOrders() +
            ", totalOrderAmount=" + getTotalOrderAmount() +
            ", firstTimeOrders=" + getFirstTimeOrders() +
            ", recurringOrders=" + getRecurringOrders() +
            ", totalCustomers=" + getTotalCustomers() +
            "}";
    }
}

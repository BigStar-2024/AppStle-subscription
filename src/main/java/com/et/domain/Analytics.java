package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Analytics.
 */
@Entity
@Table(name = "analytics")
public class Analytics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "total_subscriptions")
    private Long totalSubscriptions;

    @Column(name = "total_orders")
    private Long totalOrders;

    @Column(name = "total_order_amount")
    private Double totalOrderAmount;

    @Column(name = "first_time_orders")
    private Long firstTimeOrders;

    @Column(name = "recurring_orders")
    private Long recurringOrders;

    @Column(name = "total_customers")
    private Long totalCustomers;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public Analytics shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getTotalSubscriptions() {
        return totalSubscriptions;
    }

    public Analytics totalSubscriptions(Long totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
        return this;
    }

    public void setTotalSubscriptions(Long totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public Analytics totalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
        return this;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public Analytics totalOrderAmount(Double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
        return this;
    }

    public void setTotalOrderAmount(Double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public Long getFirstTimeOrders() {
        return firstTimeOrders;
    }

    public Analytics firstTimeOrders(Long firstTimeOrders) {
        this.firstTimeOrders = firstTimeOrders;
        return this;
    }

    public void setFirstTimeOrders(Long firstTimeOrders) {
        this.firstTimeOrders = firstTimeOrders;
    }

    public Long getRecurringOrders() {
        return recurringOrders;
    }

    public Analytics recurringOrders(Long recurringOrders) {
        this.recurringOrders = recurringOrders;
        return this;
    }

    public void setRecurringOrders(Long recurringOrders) {
        this.recurringOrders = recurringOrders;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public Analytics totalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
        return this;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Analytics)) {
            return false;
        }
        return id != null && id.equals(((Analytics) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Analytics{" +
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

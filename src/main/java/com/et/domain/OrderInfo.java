package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A OrderInfo.
 */
@Entity
@Table(name = "order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Lob
    @Column(name = "lines_json")
    private String linesJson;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public OrderInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderInfo orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLinesJson() {
        return linesJson;
    }

    public OrderInfo linesJson(String linesJson) {
        this.linesJson = linesJson;
        return this;
    }

    public void setLinesJson(String linesJson) {
        this.linesJson = linesJson;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderInfo)) {
            return false;
        }
        return id != null && id.equals(((OrderInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", orderId=" + getOrderId() +
            ", linesJson='" + getLinesJson() + "'" +
            "}";
    }
}

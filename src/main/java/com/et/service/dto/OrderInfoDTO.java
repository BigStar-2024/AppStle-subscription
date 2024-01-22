package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.OrderInfo} entity.
 */
public class OrderInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private Long orderId;

    @Lob
    private String linesJson;


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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLinesJson() {
        return linesJson;
    }

    public void setLinesJson(String linesJson) {
        this.linesJson = linesJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderInfoDTO orderInfoDTO = (OrderInfoDTO) o;
        if (orderInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", orderId=" + getOrderId() +
            ", linesJson='" + getLinesJson() + "'" +
            "}";
    }
}

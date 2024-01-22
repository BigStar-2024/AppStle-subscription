package com.et.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderCountVM implements Serializable {

    private Long orderCount;

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    @Override
    public String toString() {
        return "OrderCountVM{" +
            "orderCount=" + orderCount +
            '}';
    }
}

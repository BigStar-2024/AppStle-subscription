package com.et.pojo;

import com.et.domain.enumeration.OrderStatus;

public class TaggingOrderInfo {

    private String order;
    private OrderStatus orderStatus;
    private String shop;
    private String topicName;
    private Long processOrderInfoId;
    private Long jobId;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getProcessOrderInfoId() {
        return processOrderInfoId;
    }

    public void setProcessOrderInfoId(Long processOrderInfoId) {
        this.processOrderInfoId = processOrderInfoId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}

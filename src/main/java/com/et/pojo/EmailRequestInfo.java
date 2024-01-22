package com.et.pojo;

import com.et.domain.enumeration.EmailSettingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailRequestInfo {
    private String shop;
    private Long contractId;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private EmailSettingType emailSettingType;
    private int waitTimeInSeconds;

    private boolean addActivityLog = true;
    Map<String, Object> additionalAttributes = new HashMap<String, Object>();

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public EmailSettingType getEmailSettingType() {
        return emailSettingType;
    }

    public void setEmailSettingType(EmailSettingType emailSettingType) {
        this.emailSettingType = emailSettingType;
    }

    public int getWaitTimeInSeconds() {
        return waitTimeInSeconds;
    }

    public void setWaitTimeInSeconds(int waitTimeInSeconds) {
        this.waitTimeInSeconds = waitTimeInSeconds;
    }

    public boolean isAddActivityLog() {
        return addActivityLog;
    }

    public void setAddActivityLog(boolean addActivityLog) {
        this.addActivityLog = addActivityLog;
    }

    public Map<String, Object> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Map<String, Object> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    @Override
    public String toString() {
        return "EmailRequestInfo{" +
            "shop='" + shop + '\'' +
            ", contractId=" + contractId +
            ", orderItemList=" + orderItemList +
            ", emailSettingType=" + emailSettingType +
            ", waitTimeInSeconds=" + waitTimeInSeconds +
            ", addActivityLog=" + addActivityLog +
            ", additionalAttributes=" + additionalAttributes +
            '}';
    }
}














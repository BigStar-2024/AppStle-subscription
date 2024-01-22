package com.et.web.rest.vm;

import java.util.List;

public class UpdateAttributesRequest {

    private Long subscriptionContractId;
    private List<OrderNoteAttribute> customAttributesList;

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public List<OrderNoteAttribute> getCustomAttributesList() {
        return customAttributesList;
    }

    public void setCustomAttributesList(List<OrderNoteAttribute> customAttributesList) {
        this.customAttributesList = customAttributesList;
    }
}

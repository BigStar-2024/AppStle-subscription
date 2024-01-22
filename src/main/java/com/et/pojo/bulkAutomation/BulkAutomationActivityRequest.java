package com.et.pojo.bulkAutomation;

import com.et.domain.enumeration.BulkAutomationType;

import java.util.List;
import java.util.Map;

public class BulkAutomationActivityRequest {

    private String shop;
    private BulkAutomationType bulkAutomationType;
    private List<Long> contractIds;
    private Boolean allSubscriptions;
    private int pageSize;
    private int pageNumber;
    private Map<String,Object> processAttributes;

    public BulkAutomationActivityRequest() {
    }

    public BulkAutomationActivityRequest(String shop, BulkAutomationType bulkAutomationType, int pageSize, int pageNumber, Map<String, Object> processAttributes) {
        this.shop = shop;
        this.bulkAutomationType = bulkAutomationType;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.processAttributes = processAttributes;
    }

    public BulkAutomationActivityRequest(String shop, BulkAutomationType bulkAutomationType, List<Long> contractIds, Boolean allSubscriptions, int pageSize, int pageNumber, Map<String, Object> processAttributes) {
        this.shop = shop;
        this.bulkAutomationType = bulkAutomationType;
        this.contractIds = contractIds;
        this.allSubscriptions = allSubscriptions;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.processAttributes = processAttributes;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public BulkAutomationType getBulkAutomationType() {
        return bulkAutomationType;
    }

    public void setBulkAutomationType(BulkAutomationType bulkAutomationType) {
        this.bulkAutomationType = bulkAutomationType;
    }

    public List<Long> getContractIds() {
        return contractIds;
    }

    public void setContractIds(List<Long> contractIds) {
        this.contractIds = contractIds;
    }

    public Boolean getAllSubscriptions() {
        return allSubscriptions;
    }

    public void setAllSubscriptions(Boolean allSubscriptions) {
        this.allSubscriptions = allSubscriptions;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Map<String, Object> getProcessAttributes() {
        return processAttributes;
    }

    public void setProcessAttributes(Map<String, Object> processAttributes) {
        this.processAttributes = processAttributes;
    }
}

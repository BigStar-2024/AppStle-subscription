package com.et.pojo.bulkAutomation;

public class UpdateMinMaxCyclesRequest {

    private String shop;
    private Long contractId;
    private Integer minCycles;
    private Integer maxCycles;
    private Boolean updateMinCycles;
    private Boolean updateMaxCycles;
    private boolean isLast;

    public UpdateMinMaxCyclesRequest() {
    }

    public UpdateMinMaxCyclesRequest(String shop, Long contractId, Integer minCycles, Integer maxCycles, Boolean updateMinCycles, Boolean updateMaxCycles) {
        this.shop = shop;
        this.contractId = contractId;
        this.minCycles = minCycles;
        this.maxCycles = maxCycles;
        this.updateMinCycles = updateMinCycles;
        this.updateMaxCycles = updateMaxCycles;
    }

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

    public Integer getMinCycles() {
        return minCycles;
    }

    public void setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
    }

    public Integer getMaxCycles() {
        return maxCycles;
    }

    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
    }

    public Boolean getUpdateMinCycles() {
        return updateMinCycles;
    }

    public void setUpdateMinCycles(Boolean updateMinCycles) {
        this.updateMinCycles = updateMinCycles;
    }

    public Boolean getUpdateMaxCycles() {
        return updateMaxCycles;
    }

    public void setUpdateMaxCycles(Boolean updateMaxCycles) {
        this.updateMaxCycles = updateMaxCycles;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}

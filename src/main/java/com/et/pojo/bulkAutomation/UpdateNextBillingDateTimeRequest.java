package com.et.pojo.bulkAutomation;

public class UpdateNextBillingDateTimeRequest {

    private String shop;
    private Long contractId;
    private Integer hour;
    private Integer minute;
    private Integer zonedOffHours;
    private boolean isLast ;

    public UpdateNextBillingDateTimeRequest() {
    }

    public UpdateNextBillingDateTimeRequest(String shop, Long contractId, Integer hour, Integer minute, Integer zonedOffHours) {
        this.shop = shop;
        this.contractId = contractId;
        this.hour = hour;
        this.minute = minute;
        this.zonedOffHours = zonedOffHours;
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

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getZonedOffHours() {
        return zonedOffHours;
    }

    public void setZonedOffHours(Integer zonedOffHours) {
        this.zonedOffHours = zonedOffHours;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}

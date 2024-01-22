package com.et.api.eber.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionData {
    private Long id;
    private Long business_id;
    private Long points;
    private String currency;
    private String amount;
    private Long store_id;
    private String note;
    private String type;
    private Long transaction_no;
    private Long store_staff_id;
    private Long staff_id;
    private Long user_id;
    private String created_at;
    private String updated_at;
    private String friendly_created_at;
    private String friendly_updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(Long transaction_no) {
        this.transaction_no = transaction_no;
    }

    public Long getStore_staff_id() {
        return store_staff_id;
    }

    public void setStore_staff_id(Long store_staff_id) {
        this.store_staff_id = store_staff_id;
    }

    public Long getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Long staff_id) {
        this.staff_id = staff_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFriendly_created_at() {
        return friendly_created_at;
    }

    public void setFriendly_created_at(String friendly_created_at) {
        this.friendly_created_at = friendly_created_at;
    }

    public String getFriendly_updated_at() {
        return friendly_updated_at;
    }

    public void setFriendly_updated_at(String friendly_updated_at) {
        this.friendly_updated_at = friendly_updated_at;
    }
}

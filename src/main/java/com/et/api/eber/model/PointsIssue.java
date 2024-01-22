package com.et.api.eber.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsIssue {
    private Boolean status;
    private Long points;
    private Long base_points;
    private Long additional_points;
    private Long transaction_id;
    private TransactionData transaction;
    private List<AdditionalTransactions> additional_transactions;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getBase_points() {
        return base_points;
    }

    public void setBase_points(Long base_points) {
        this.base_points = base_points;
    }

    public Long getAdditional_points() {
        return additional_points;
    }

    public void setAdditional_points(Long additional_points) {
        this.additional_points = additional_points;
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public TransactionData getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionData transaction) {
        this.transaction = transaction;
    }

    public List<AdditionalTransactions> getAdditional_transactions() {
        return additional_transactions;
    }

    public void setAdditional_transactions(List<AdditionalTransactions> additional_transactions) {
        this.additional_transactions = additional_transactions;
    }
}

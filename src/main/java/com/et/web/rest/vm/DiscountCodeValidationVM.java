package com.et.web.rest.vm;

import java.io.Serializable;

public class DiscountCodeValidationVM implements Serializable {
    private String discountCode;
    private Boolean valid;
    private String error;

    public String getDiscountCode() {
        return discountCode;
    }

    public DiscountCodeValidationVM setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public DiscountCodeValidationVM setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public String getError() {
        return error;
    }

    public DiscountCodeValidationVM setError(String error) {
        this.error = error;
        return this;
    }
}

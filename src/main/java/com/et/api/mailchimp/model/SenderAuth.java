package com.et.api.mailchimp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SenderAuth {
    private Boolean valid;
    private String valid_after;
    private String error;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getValid_after() {
        return valid_after;
    }

    public void setValid_after(String valid_after) {
        this.valid_after = valid_after;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

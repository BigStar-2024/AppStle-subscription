package com.et.api.mailchimp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipientInformation {
    private String email;
    private String name;
    private String type;

    public RecipientInformation(String email) {
        this.email = email;
    }

    public RecipientInformation(String email, String name, String type) {
        this.email = email;
        this.name = name;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

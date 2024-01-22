package com.et.liquid;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    private Long id;
    private String unsubscribe_url;
    private String display_name;
    private String first_name;
    private String last_name;
    private String token;
    private String email;

    public Customer(String displayName, String firstName, String lastName, String token, String email) {
        this.display_name = displayName;
        this.first_name = firstName;
        this.last_name = lastName;
        this.token = token;
        this.email = email;
    }

    public Customer() {
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUnsubscribe_url() {
        return unsubscribe_url;
    }

    public void setUnsubscribe_url(String unsubscribe_url) {
        this.unsubscribe_url = unsubscribe_url;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", unsubscribe_url='" + unsubscribe_url + '\'' +
                ", display_name='" + display_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

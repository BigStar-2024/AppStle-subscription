package com.et.api.klaviyo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProperties {
    public String $id;
    public String $email;
    public String $firstName;
    public String $lastName;
    public String $phoneNumber;
    public String $title;
    public String $city;
    public String $organization;
    public String $region;
    public String $country;
    public String $zip;

    public CustomerProperties(String $email) {
        this.$email = $email;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String get$email() {
        return $email;
    }

    public void set$email(String $email) {
        this.$email = $email;
    }

    public String get$firstName() {
        return $firstName;
    }

    public void set$firstName(String $firstName) {
        this.$firstName = $firstName;
    }

    public String get$lastName() {
        return $lastName;
    }

    public void set$lastName(String $lastName) {
        this.$lastName = $lastName;
    }

    public String get$phoneNumber() {
        return $phoneNumber;
    }

    public void set$phoneNumber(String $phoneNumber) {
        this.$phoneNumber = $phoneNumber;
    }

    public String get$title() {
        return $title;
    }

    public void set$title(String $title) {
        this.$title = $title;
    }

    public String get$city() {
        return $city;
    }

    public void set$city(String $city) {
        this.$city = $city;
    }

    public String get$organization() {
        return $organization;
    }

    public void set$organization(String $organization) {
        this.$organization = $organization;
    }

    public String get$region() {
        return $region;
    }

    public void set$region(String $region) {
        this.$region = $region;
    }

    public String get$country() {
        return $country;
    }

    public void set$country(String $country) {
        this.$country = $country;
    }

    public String get$zip() {
        return $zip;
    }

    public void set$zip(String $zip) {
        this.$zip = $zip;
    }

}

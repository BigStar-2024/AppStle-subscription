package com.et.liquid;

public class BillingAddressModel {
    private String billing_address1;
    private String billing_city;
    private String billing_province_code;
    private String billing_zip;
    private String billing_full_name;
    private String billing_country;
    private String billing_country_code;
    private String billing_province;

    public BillingAddressModel(String billing_address1, String billing_city, String billing_province_code,
                               String billing_zip, String billing_full_name, String billing_country, String billing_country_code, String billing_province) {
        this.billing_address1 = billing_address1;
        this.billing_city = billing_city;
        this.billing_province_code = billing_province_code;
        this.billing_zip = billing_zip;
        this.billing_full_name = billing_full_name;
        this.billing_country = billing_country;
        this.billing_country_code = billing_country_code;
        this.billing_province = billing_province;
    }

    public String getBilling_address1() {
        return billing_address1;
    }

    public void setBilling_address1(String billing_address1) {
        this.billing_address1 = billing_address1;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public String getBilling_province_code() {
        return billing_province_code;
    }

    public void setBilling_province_code(String billing_province_code) {
        this.billing_province_code = billing_province_code;
    }

    public String getBilling_zip() {
        return billing_zip;
    }

    public void setBilling_zip(String billing_zip) {
        this.billing_zip = billing_zip;
    }

    public String getBilling_full_name() {
        return billing_full_name;
    }

    public void setBilling_full_name(String billing_full_name) {
        this.billing_full_name = billing_full_name;
    }

    public String getBilling_country() {
        return billing_country;
    }

    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    public String getBilling_country_code() {
        return billing_country_code;
    }

    public void setBilling_country_code(String billing_country_code) {
        this.billing_country_code = billing_country_code;
    }

    public String getBilling_province() {
        return billing_province;
    }

    public void setBilling_province(String billing_province) {
        this.billing_province = billing_province;
    }
}

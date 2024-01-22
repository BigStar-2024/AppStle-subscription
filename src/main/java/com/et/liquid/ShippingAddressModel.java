package com.et.liquid;

public class ShippingAddressModel {
    private String shipping_address2;
    private String shipping_address1;
    private String shipping_city;
    private String shipping_province_code;
    private String shipping_zip;
    private String shipping_first_name;
    private String shipping_last_name;
    private String shipping_country;
    private String shipping_country_code;
    private String shipping_type;
    private String shipping_phone;
    private String shipping_company;
    private String pick_up_location_id;
    private String shipping_province;
    private ShippingOptionModel shippingOption;

    public ShippingAddressModel() {
    }

    public ShippingAddressModel(String shipping_address1, String shipping_city, String shipping_province_code,
                                String shipping_zip, String shipping_first_name, String shipping_last_name,
                                String shipping_address2, String shipping_country, String shipping_country_code,
                                String shipping_phone, String shipping_company, String pick_up_location_id, String shipping_province, String shipping_type) {
        this.shipping_address1 = shipping_address1;
        this.shipping_city = shipping_city;
        this.shipping_province_code = shipping_province_code;
        this.shipping_zip = shipping_zip;
        this.shipping_first_name = shipping_first_name;
        this.shipping_last_name = shipping_last_name;
        this.shipping_address2 = shipping_address2;
        this.shipping_country = shipping_country;
        this.shipping_country_code = shipping_country_code;
        this.shipping_phone = shipping_phone;
        this.shipping_company = shipping_company;
        this.pick_up_location_id = pick_up_location_id;
        this.shipping_province = shipping_province;
        this.shipping_type = shipping_type;
    }

    public String getShipping_address1() {
        return shipping_address1;
    }

    public void setShipping_address1(String shipping_address1) {
        this.shipping_address1 = shipping_address1;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public String getShipping_province_code() {
        return shipping_province_code;
    }

    public void setShipping_province_code(String shipping_province_code) {
        this.shipping_province_code = shipping_province_code;
    }

    public String getShipping_zip() {
        return shipping_zip;
    }

    public void setShipping_zip(String shipping_zip) {
        this.shipping_zip = shipping_zip;
    }

    public String getShipping_first_name() {
        return shipping_first_name;
    }

    public void setShipping_first_name(String shipping_first_name) {
        this.shipping_first_name = shipping_first_name;
    }

    public String getShipping_last_name() {
        return shipping_last_name;
    }

    public void setShipping_last_name(String shipping_last_name) {
        this.shipping_last_name = shipping_last_name;
    }

    public String getShipping_address2() {
        return shipping_address2;
    }

    public void setShipping_address2(String shipping_address2) {
        this.shipping_address2 = shipping_address2;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public String getShipping_country_code() {
        return shipping_country_code;
    }

    public void setShipping_country_code(String shipping_country_code) {
        this.shipping_country_code = shipping_country_code;
    }

    public String getShipping_phone() {
        return shipping_phone;
    }

    public void setShipping_phone(String shipping_phone) {
        this.shipping_phone = shipping_phone;
    }

    public String getShipping_company() {
        return shipping_company;
    }

    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }

    public String getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(String shipping_type) {
        this.shipping_type = shipping_type;
    }

    public String getPick_up_location_id() {
        return pick_up_location_id;
    }

    public void setPick_up_location_id(String pick_up_location_id) {
        this.pick_up_location_id = pick_up_location_id;
    }

    public String getShipping_province() {
        return shipping_province;
    }

    public void setShipping_province(String shipping_province) {
        this.shipping_province = shipping_province;
    }

    public ShippingOptionModel getShippingOption() {
        return shippingOption;
    }

    public void setShippingOption(ShippingOptionModel shippingOption) {
        this.shippingOption = shippingOption;
    }
}

package com.et.api.eber.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String display_name;
    private String created_at;
    private String updated_at;
    private String country;
    private String phone;
    private Long birth_year;
    private Long birth_month;
    private Long birth_day;
    private String phone_code;
    private String phone_format;
    private Long business_id;
    private String address;
    private String city;
    private String state;
    private String postal_code;
    private String nationality;
    private String unit;
    private String block;
    private String floor;
    private String external_member_id;
    private String customer_view_url;
    private List<UserPoints> points;
    private UserMember member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(Long birth_year) {
        this.birth_year = birth_year;
    }

    public Long getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(Long birth_month) {
        this.birth_month = birth_month;
    }

    public Long getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(Long birth_day) {
        this.birth_day = birth_day;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone_format() {
        return phone_format;
    }

    public void setPhone_format(String phone_format) {
        this.phone_format = phone_format;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getExternal_member_id() {
        return external_member_id;
    }

    public void setExternal_member_id(String external_member_id) {
        this.external_member_id = external_member_id;
    }

    public String getCustomer_view_url() {
        return customer_view_url;
    }

    public void setCustomer_view_url(String customer_view_url) {
        this.customer_view_url = customer_view_url;
    }

    public List<UserPoints> getPoints() {
        return points;
    }

    public void setPoints(List<UserPoints> points) {
        this.points = points;
    }

    public UserMember getMember() {
        return member;
    }

    public void setMember(UserMember member) {
        this.member = member;
    }
}

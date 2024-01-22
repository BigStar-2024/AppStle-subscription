package com.et.liquid;

public class ShippingOptionModel {
    private String title;
    private String presentmentTitle;
    private String description;
    private String code;
    private String phone;

    public ShippingOptionModel(String title, String presentmentTitle, String description, String code, String phone) {
        this.title = title;
        this.presentmentTitle = presentmentTitle;
        this.description = description;
        this.code = code;
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPresentmentTitle() {
        return presentmentTitle;
    }

    public void setPresentmentTitle(String presentmentTitle) {
        this.presentmentTitle = presentmentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

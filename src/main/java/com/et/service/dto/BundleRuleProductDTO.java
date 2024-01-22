package com.et.service.dto;

public class BundleRuleProductDTO {

    private Long id;
    private String title;
    private String type;
    private String imageSrc;
    private String productHandle;
    private String price;
    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getProductHandle() {
        return productHandle;
    }

    public void setProductHandle(String productHandle) {
        this.productHandle = productHandle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BundleRuleProductDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", type='" + type + '\'' +
            ", imageSrc='" + imageSrc + '\'' +
            ", productHandle='" + productHandle + '\'' +
            ", price='" + price + '\'' +
            '}';
    }
}

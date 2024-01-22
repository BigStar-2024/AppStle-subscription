package com.et.pojo;

public class UpdateShopCustomizationRequest {
    private Long id;
    private Long labelId;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UpdateShopCustomizationRequest{" +
            "id=" + id +
            ", labelId=" + labelId +
            ", value='" + value + '\'' +
            '}';
    }
}

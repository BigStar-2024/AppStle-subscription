package com.et.service.dto;

public class FilterOptionDTO {

    private String filterOptionTitle;
    private String filterOptionIcon;
    private String filterOptionValue;

    public String getFilterOptionTitle() {
        return filterOptionTitle;
    }

    public void setFilterOptionTitle(String filterOptionTitle) {
        this.filterOptionTitle = filterOptionTitle;
    }

    public String getFilterOptionIcon() {
        return filterOptionIcon;
    }

    public void setFilterOptionIcon(String filterOptionIcon) {
        this.filterOptionIcon = filterOptionIcon;
    }

    public String getFilterOptionValue() {
        return filterOptionValue;
    }

    public void setFilterOptionValue(String filterOptionValue) {
        this.filterOptionValue = filterOptionValue;
    }

    @Override
    public String toString() {
        return "FilterOptionDTO{" +
            "filterOptionTitle='" + filterOptionTitle + '\'' +
            ", filterOptionIcon='" + filterOptionIcon + '\'' +
            ", filterOptionValue='" + filterOptionValue + '\'' +
            '}';
    }
}

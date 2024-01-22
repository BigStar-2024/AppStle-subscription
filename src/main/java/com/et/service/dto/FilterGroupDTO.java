package com.et.service.dto;

import java.util.List;

public class FilterGroupDTO {

    private String filterGroupTitle;
    private String filterGroupIcon;
    private Boolean filterGroupFieldInValid;
    private Boolean filterGroupFieldNameInValid;
    private String filterGroupFieldNameErrorText;
    private Integer filterGroupFieldOpen;
    private String filterGroupType;
    private List<FilterOptionDTO> filterOptions;


    public String getFilterGroupTitle() {
        return filterGroupTitle;
    }

    public void setFilterGroupTitle(String filterGroupTitle) {
        this.filterGroupTitle = filterGroupTitle;
    }

    public String getFilterGroupIcon() {
        return filterGroupIcon;
    }

    public void setFilterGroupIcon(String filterGroupIcon) {
        this.filterGroupIcon = filterGroupIcon;
    }

    public Boolean getFilterGroupFieldInValid() {
        return filterGroupFieldInValid;
    }

    public void setFilterGroupFieldInValid(Boolean filterGroupFieldInValid) {
        this.filterGroupFieldInValid = filterGroupFieldInValid;
    }

    public Boolean getFilterGroupFieldNameInValid() {
        return filterGroupFieldNameInValid;
    }

    public void setFilterGroupFieldNameInValid(Boolean filterGroupFieldNameInValid) {
        this.filterGroupFieldNameInValid = filterGroupFieldNameInValid;
    }

    public String getFilterGroupFieldNameErrorText() {
        return filterGroupFieldNameErrorText;
    }

    public void setFilterGroupFieldNameErrorText(String filterGroupFieldNameErrorText) {
        this.filterGroupFieldNameErrorText = filterGroupFieldNameErrorText;
    }

    public Integer getFilterGroupFieldOpen() {
        return filterGroupFieldOpen;
    }

    public void setFilterGroupFieldOpen(Integer filterGroupFieldOpen) {
        this.filterGroupFieldOpen = filterGroupFieldOpen;
    }

    public String getFilterGroupType() {
        return filterGroupType;
    }

    public void setFilterGroupType(String filterGroupType) {
        this.filterGroupType = filterGroupType;
    }

    public List<FilterOptionDTO> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(List<FilterOptionDTO> filterOptions) {
        this.filterOptions = filterOptions;
    }

    @Override
    public String toString() {
        return "FilterGroupDTO{" +
            "filterGroupTitle='" + filterGroupTitle + '\'' +
            ", filterGroupIcon='" + filterGroupIcon + '\'' +
            ", filterGroupFieldInValid=" + filterGroupFieldInValid +
            ", filterGroupFieldNameInValid=" + filterGroupFieldNameInValid +
            ", filterGroupFieldNameErrorText='" + filterGroupFieldNameErrorText + '\'' +
            ", filterGroupFieldOpen=" + filterGroupFieldOpen +
            ", filterGroupType='" + filterGroupType + '\'' +
            ", filterOptions=" + filterOptions +
            '}';
    }
}

package com.et.service.dto;

import java.util.List;

public class AppstleMenuTypeDTO {

    private String menuType;
    private String menuTitle;
    private String menuIcon;
    private Long sourceCollection;
    private Long subscriptionGroup;
    private String sourceCollectionTitle;
    private List<FilterGroupDTO> filterGroups;


    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Long getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(Long sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public Long getSubscriptionGroup() {
        return subscriptionGroup;
    }

    public void setSubscriptionGroup(Long subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
    }

    public String getSourceCollectionTitle() {
        return sourceCollectionTitle;
    }

    public void setSourceCollectionTitle(String sourceCollectionTitle) {
        this.sourceCollectionTitle = sourceCollectionTitle;
    }

    public List<FilterGroupDTO> getFilterGroups() {
        return filterGroups;
    }

    public void setFilterGroups(List<FilterGroupDTO> filterGroups) {
        this.filterGroups = filterGroups;
    }

    @Override
    public String toString() {
        return "AppstleMenuTypeDTO{" +
            "menuType='" + menuType + '\'' +
            ", menuTitle='" + menuTitle + '\'' +
            ", menuIcon='" + menuIcon + '\'' +
            ", sourceCollection=" + sourceCollection +
            ", subscriptionGroup=" + subscriptionGroup +
            ", sourceCollectionTitle='" + sourceCollectionTitle + '\'' +
            ", filterGroups=" + filterGroups +
            '}';
    }
}

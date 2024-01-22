package com.et.service.dto;

public class ThemeCodeDTO {

    private Long id;
    private String themeName;
    private String themeNameFriendly;
    private Integer themeStoreId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public ThemeCodeDTO themeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeNameFriendly() {
        return themeNameFriendly;
    }

    public ThemeCodeDTO themeNameFriendly(String themeNameFriendly) {
        this.themeNameFriendly = themeNameFriendly;
        return this;
    }

    public void setThemeNameFriendly(String themeNameFriendly) {
        this.themeNameFriendly = themeNameFriendly;
    }

    public Integer getThemeStoreId() {
        return themeStoreId;
    }

    public ThemeCodeDTO themeStoreId(Integer themeStoreId) {
        this.themeStoreId = themeStoreId;
        return this;
    }

    public void setThemeStoreId(Integer themeStoreId) {
        this.themeStoreId = themeStoreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeCodeDTO)) {
            return false;
        }
        return id != null && id.equals(((ThemeCodeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ThemeCodeDTO{" +
            "id=" + getId() +
            ", themeName='" + getThemeName() + "'" +
            ", themeNameFriendly='" + getThemeNameFriendly() + "'" +
            ", themeStoreId=" + getThemeStoreId() +
            "}";
    }
}

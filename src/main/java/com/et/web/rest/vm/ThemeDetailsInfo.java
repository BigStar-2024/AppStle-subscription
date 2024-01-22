package com.et.web.rest.vm;

public class ThemeDetailsInfo {

    private String themeName;
    private Long themeStoreId;
    private Long publishedThemeId;
    private Long themeId;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Long getThemeStoreId() {
        return themeStoreId;
    }

    public void setThemeStoreId(Long themeStoreId) {
        this.themeStoreId = themeStoreId;
    }

    public void setPublishedThemeId(Long publishedThemeId) {
        this.publishedThemeId = publishedThemeId;
    }

    public Long getPublishedThemeId() {
        return publishedThemeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}

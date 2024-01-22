package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.ProductViewSettings;

/**
 * A DTO for the {@link com.et.domain.AppstleMenuSettings} entity.
 */
public class AppstleMenuSettingsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String filterMenu;

    private String menuUrl;

    @Lob
    private String menuStyle;

    private Boolean active;

    private String handle;

    private ProductViewSettings productViewStyle;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getFilterMenu() {
        return filterMenu;
    }

    public void setFilterMenu(String filterMenu) {
        this.filterMenu = filterMenu;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuStyle() {
        return menuStyle;
    }

    public void setMenuStyle(String menuStyle) {
        this.menuStyle = menuStyle;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public ProductViewSettings getProductViewStyle() {
        return productViewStyle;
    }

    public void setProductViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppstleMenuSettingsDTO)) {
            return false;
        }

        return id != null && id.equals(((AppstleMenuSettingsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppstleMenuSettingsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", filterMenu='" + getFilterMenu() + "'" +
            ", menuUrl='" + getMenuUrl() + "'" +
            ", menuStyle='" + getMenuStyle() + "'" +
            ", active='" + isActive() + "'" +
            ", handle='" + getHandle() + "'" +
            ", productViewStyle='" + getProductViewStyle() + "'" +
            "}";
    }
}

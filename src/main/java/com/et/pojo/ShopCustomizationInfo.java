package com.et.pojo;

import com.et.domain.enumeration.CustomizationCategory;
import com.et.domain.enumeration.CustomizationType;
import com.et.service.dto.CustomizationDTO;

public interface ShopCustomizationInfo {
    Long getShopCustomizationId();
    String getValue();
    Long getId();
    String getLabel();
    CustomizationType getType();
    String getCss();
    CustomizationCategory getCategory();
}

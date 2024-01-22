package com.et.utils;

import com.et.service.ShopInfoService;
import com.et.service.dto.ShopInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopInfoUtils {

    @Autowired
    private ShopInfoService shopInfoService;

    public String getManageSubscriptionUrl(String shop) {
        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
        if (shopInfoDTO.isAdvancedCustomerPortal()) {
            return "pages/subscriptions";
        } else {
            return shopInfoDTO.getManageSubscriptionsUrl();
        }
    }
}

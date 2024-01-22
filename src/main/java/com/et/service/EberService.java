package com.et.service;

import com.et.api.eber.EberLoyaltyApi;
import com.et.api.eber.model.PointsIssue;
import com.et.api.eber.model.UserInfo;
import com.et.service.dto.ShopInfoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EberService {
    private final Logger log = LoggerFactory.getLogger(EberService.class);

    @Autowired
    private ShopInfoService shopInfoService;

    public UserInfo fetchUserInfo(String customerEmail, String shop) throws JsonProcessingException {

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        log.info("Fetching eber customer detail for customer={}", customerEmail);

        if (BooleanUtils.isTrue(shopInfo.isEberLoyaltyEnabled()) && !StringUtils.isEmpty(shopInfo.getEberLoyaltyApiKey())) {

            EberLoyaltyApi eberLoyaltyApi = new EberLoyaltyApi();

            return eberLoyaltyApi.getUserInfo(shopInfo.getEberLoyaltyApiKey(), customerEmail);
        }
        return new UserInfo();

    }

    public PointsIssue issuePoints(String shop, String customerEmail, Double amount, Long points, String note) throws JsonProcessingException {

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        log.info("Issuing eber loyalty points for customer={}", customerEmail);

        if (BooleanUtils.isTrue(shopInfo.isEberLoyaltyEnabled()) && !StringUtils.isEmpty(shopInfo.getEberLoyaltyApiKey())) {

            EberLoyaltyApi eberLoyaltyApi = new EberLoyaltyApi();

            return eberLoyaltyApi.issuePoints(shopInfo.getEberLoyaltyApiKey(), customerEmail, amount, points, note);
        }
        return new PointsIssue();
    }
}

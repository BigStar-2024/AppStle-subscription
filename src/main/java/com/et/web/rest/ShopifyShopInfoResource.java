package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.shop.Shop;
import com.et.constant.Constants;
import com.et.security.SecurityUtils;
import com.et.service.ShopInfoService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.shopify.java.graphql.client.queries.AccessScopesQuery;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.ShopInfo}.
 */
@RestController
public class ShopifyShopInfoResource {

    private final Logger log = LoggerFactory.getLogger(ShopifyShopInfoResource.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @GetMapping(value = {"/api/shopify-shop-info"})
    public ResponseEntity<Shop> getShopifyShopInfo() {
        log.debug("REST request to get a Shopify info of shop");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shopName);

        Shop shopifyShopInfo = api.getShopInfo().getShop();

        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shopName);
        if (shopInfoDTOOptional.isPresent()) {
            ShopInfoDTO shopInfoDTO = shopInfoDTOOptional.get();
            if (!shopifyShopInfo.getPasswordEnabled().equals(shopInfoDTO.isPasswordEnabled())) {
                shopInfoDTO.setPasswordEnabled(shopifyShopInfo.getPasswordEnabled());
                shopInfoService.save(shopInfoDTO);
            }
        }
        return ResponseEntity.ok().body(shopifyShopInfo);
    }


    @GetMapping(value = {"/api/shop-infos-by-current-login", "/subscriptions/cp/api/shop-infos-by-current-login", "/subscriptions/bb/api/shop-infos-by-current-login"})
    @CrossOrigin
    public ResponseEntity<ShopInfoDTO> getShopInfoByCurrentLogin() {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shop);

        if (shopInfoDTOOptional.isPresent()) {
            ShopInfoDTO shopInfoDTOOriginal = shopInfoDTOOptional.get();
            ShopInfoDTO shopInfoDTO = new ShopInfoDTO();
            shopInfoDTO.setShop(shopInfoDTOOriginal.getShop());
            shopInfoDTO.setPasswordEnabled(shopInfoDTOOriginal.isPasswordEnabled());
            shopInfoDTO.setBuildBoxVersion(shopInfoDTOOriginal.getBuildBoxVersion());
            shopInfoDTO.setAllowLocalDelivery(Optional.ofNullable(shopInfoDTOOriginal.isAllowLocalDelivery()).orElse(Boolean.TRUE));
            shopInfoDTO.setAllowLocalPickup(Optional.ofNullable(shopInfoDTOOriginal.isAllowLocalPickup()).orElse(Boolean.TRUE));
            return ResponseUtil.wrapOrNotFound(Optional.of(shopInfoDTO));
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(new ShopInfoDTO()));
    }

    @GetMapping("/api/shop-access-scopes")
    public Set<String> getShopAccessScopes() throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        AccessScopesQuery accessScopesQuery = new AccessScopesQuery();
        Response<Optional<AccessScopesQuery.Data>> accessScopesResponse = shopifyGraphqlClient.getOptionalQueryResponse(accessScopesQuery);

        Set<String> accessScopes = Objects.requireNonNull(accessScopesResponse.getData())
            .flatMap(AccessScopesQuery.Data::getAppInstallation)
            .map(AccessScopesQuery.AppInstallation::getAccessScopes).orElse(new ArrayList<>())
            .stream()
            .map(ac -> ac.getHandle())
            .collect(Collectors.toSet());

        return accessScopes;
    }

    @GetMapping("/api/shop-missing-access-scopes")
    public Set<String> getMissingAccessScopes() throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        AccessScopesQuery accessScopesQuery = new AccessScopesQuery();
        Response<Optional<AccessScopesQuery.Data>> accessScopesResponse = shopifyGraphqlClient.getOptionalQueryResponse(accessScopesQuery);

        Set<String> shopAccessScopes = Objects.requireNonNull(accessScopesResponse.getData())
            .flatMap(AccessScopesQuery.Data::getAppInstallation)
            .map(AccessScopesQuery.AppInstallation::getAccessScopes).orElse(new ArrayList<>())
            .stream()
            .map(ac -> ac.getHandle())
            .collect(Collectors.toSet());

        Map<String, OAuth2ClientProperties.Registration> registrationMap = oAuth2ClientProperties.getRegistration();
        OAuth2ClientProperties.Registration registration = registrationMap.get(Constants.SUBSCRIPTION_PROVIDER);


        Set<String> missingScopes = registration.getScope().stream().map(String::trim).collect(Collectors.toSet());
        missingScopes.removeAll(shopAccessScopes);

        return missingScopes;
    }
}

package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.security.SecurityUtils;
import com.et.service.ShopInfoService;
import com.et.utils.CommonUtils;
import com.shopify.java.graphql.client.queries.ShopQuery;
import com.shopify.java.graphql.client.type.PaypalExpressSubscriptionsGatewayStatus;
import tech.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ShopInfo}.
 */
@RestController
@RequestMapping("/api")
public class ShopPaymentResource {

    private final Logger log = LoggerFactory.getLogger(ShopPaymentResource.class);

    private static final String ENTITY_NAME = "shopInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopInfoService shopInfoService;

    public ShopPaymentResource(ShopInfoService shopInfoService) {
        this.shopInfoService = shopInfoService;
    }

    @Autowired
    private CommonUtils commonUtils;

    @GetMapping("/shop-payment-info")
    public ResponseEntity<com.et.web.rest.vm.payment.Shop> getShopPaymentInfo() throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopQuery shopQuery = new ShopQuery();
        Response<Optional<ShopQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopQuery);

        Optional<com.et.web.rest.vm.payment.Shop> shop1 = optionalQueryResponse
            .getData()
            .map(ShopQuery.Data::getShop)
            .map(s -> {
                com.et.web.rest.vm.payment.Shop shop3 = new com.et.web.rest.vm.payment.Shop();
                com.et.web.rest.vm.payment.PaymentSettings paymentSettings = new com.et.web.rest.vm.payment.PaymentSettings();

                if (BooleanUtils.isTrue(s.getFeatures().isEligibleForSubscriptions())) {
                    paymentSettings.getSupportedDigitalWallets().add(com.et.web.rest.vm.payment.DigitalWallet.SHOPIFY_PAY);
                }

                boolean legacySubscriptionGatewayEnabled = s.getFeatures().isLegacySubscriptionGatewayEnabled();

                boolean eligibleForSubscriptionMigration = s.getFeatures().isEligibleForSubscriptionMigration();

                PaypalExpressSubscriptionsGatewayStatus paypalExpressSubscriptionGatewayStatus = s.getFeatures().getPaypalExpressSubscriptionGatewayStatus();

                shop3.setPaymentSettings(paymentSettings);
                shop3.setLegacySubscriptionGatewayEnabled(legacySubscriptionGatewayEnabled);
                shop3.setEligibleForSubscriptionMigration(eligibleForSubscriptionMigration);
                shop3.setPaypalExpressSubscriptionGatewayStatus(paypalExpressSubscriptionGatewayStatus);
                return shop3;
            });


        return ResponseUtil.wrapOrNotFound(shop1);
    }
}

package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.constant.Constants;
import com.et.domain.BundleRule;
import com.et.domain.enumeration.*;
import com.et.security.jwt.TokenProvider;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.utils.FTLUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.vm.CustomerTokenInfo;
import com.et.web.rest.vm.bundling.Cart;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import com.et.web.rest.vm.bundling.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.DiscountCodeBasicCreateMutation;
import com.shopify.java.graphql.client.queries.OrderCustomerIdQuery;
import com.shopify.java.graphql.client.type.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.et.constant.Constants.*;
import static com.et.utils.CommonUtils.appendVersionParams;

@Controller
public class WebPageResource {

    public static final String SHOP = "shop";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(WebPageResource.class);

    @GetMapping("/privacy-policy")
    public ModelAndView privacyPolicy() {
        return new ModelAndView("privacy-policy");
    }

    @GetMapping("/affiliates")
    public ModelAndView affiliatesTracking() {
        return new ModelAndView("first-promotion-tracking");
    }

    @Autowired
    private FTLUtils ftlUtils;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private SubscriptionCustomCssService subscriptionCustomCssService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private SubscriptionBundleSettingsService subscriptionBundleSettingsService;

    @Autowired
    private CustomerPortalDynamicScriptService customerPortalDynamicScriptService;

    @Autowired
    private BundleDynamicScriptService bundleDynamicScriptService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private BundleRuleService bundleRuleService;
    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopAssetUrlsService shopAssetUrlsService;

    @Autowired
    SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private Environment env;

    @Autowired
    SubscriptionBundlingService subscriptionBundlingService;

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/subscriptions",
        produces = {"application/liquid", "application/json", "text/html"}
    )
    @ResponseBody
    public ResponseEntity<String> getSubscriptions(@RequestParam(value = "action", defaultValue = "portal") String action,
                                                   @RequestParam(value = "renderType", defaultValue = "liquid") String renderType,
                                                   @RequestParam(value = "portalVersion", required = false) String portalVersion,
                                                   HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {

        long totalStartTime = System.currentTimeMillis();

        String token = "";

        Map<String, Object> templateData = new HashMap<>();

        templateData.put("version", LATEST_BUILD_TIME);
        String templateName = null;

        if (!httpServletRequest.getParameterMap().containsKey(SHOP)) {
            return ResponseEntity.ok().header("Content-Type", "application/json").body(null);
        }

        String shop = httpServletRequest.getParameterMap().get(SHOP)[0];

        UserDetails userDetails = userDetailsService.loadUserByUsername(shop);

        if (action.equals("customer_payment_token")) {
            if (httpServletRequest.getParameterMap().containsKey("customer_id")) {
                long customerTokenStartTime = System.currentTimeMillis();
                String[] customerIdTokens = httpServletRequest.getParameterMap().get("customer_id");
                if (customerIdTokens.length > 0) {
                    Long customerId = null;

                    try {
                        customerId = Long.parseLong(customerIdTokens[0]);
                    } catch (Exception ex) {
                        //log.info("Number not found. ex=" + ExceptionUtils.getStackTrace(ex) + " customerIdTokens[0]=" + customerIdTokens[0]);
                        return ResponseEntity.ok().header("Content-Type", "application/json").body(null);
                    }

                    CustomerTokenInfo customerTokenInfo = customerPaymentService.getCustomerTokenInfo(customerId, shop);
                    token = customerTokenInfo.getToken();

                    String externalPrincipal = shop + "|" + "external" + "|" + token;
                    String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);

                    customerTokenInfo.setAppstleExternalToken(externalJWT);
                    String customerPaymentTokenJson = OBJECT_MAPPER.writeValueAsString(customerTokenInfo);
                    long customerTokenEndTime = System.currentTimeMillis();

                    log.info("Customer Id = {} token time taken : {}", customerId, (customerTokenEndTime - customerTokenStartTime));

                    return ResponseEntity.ok().header("Content-Type", "application/json").body(customerPaymentTokenJson);
                }
            } else if (httpServletRequest.getParameterMap().containsKey("customer_token") && httpServletRequest.getParameterMap().get("customer_token").length > 0) {
                long customerTokenStartTime = System.currentTimeMillis();
                Optional<Long> optionalCustomerId = customerPaymentService.getCustomerIdFromCustomerUid(httpServletRequest.getParameterMap().get("customer_token")[0]);

                if (optionalCustomerId.isPresent()) {
                    Long customerId = optionalCustomerId.get();
                    CustomerTokenInfo customerTokenInfo = customerPaymentService.getCustomerTokenInfo(customerId, shop);
                    token = customerTokenInfo.getToken();

                    String externalPrincipal = shop + "|" + "external" + "|" + token;
                    String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);

                    customerTokenInfo.setAppstleExternalToken(externalJWT);
                    String customerPaymentTokenJson = OBJECT_MAPPER.writeValueAsString(customerTokenInfo);

                    long customerTokenEndTime = System.currentTimeMillis();

                    log.info("Customer Token based customer Id = {} token time taken : {}", customerId, (customerTokenEndTime - customerTokenStartTime));

                    return ResponseEntity.ok().header("Content-Type", "application/json").body(customerPaymentTokenJson);
                }
            } else if (httpServletRequest.getParameterMap().containsKey("order_id")) {
                long customerTokenStartTime = System.currentTimeMillis();
                String orderId = httpServletRequest.getParameter("order_id");
                if (StringUtils.isNotBlank(orderId)) {
                    OrderCustomerIdQuery orderCustomerIdQuery = new OrderCustomerIdQuery(ShopifyIdPrefix.ORDER_ID_PREFIX + orderId);
                    ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                    Response<Optional<OrderCustomerIdQuery.Data>> orderCustomerIdQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderCustomerIdQuery);

                    String graphqlCustomerId = orderCustomerIdQueryResponse.getData()
                        .flatMap(d -> d.getOrder()
                            .flatMap(e -> e.getCustomer()
                                .map(OrderCustomerIdQuery.Customer::getId)))
                        .orElse(null);

                    long customerId = Long.parseLong(graphqlCustomerId.replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));

                    CustomerTokenInfo customerTokenInfo = customerPaymentService.getCustomerTokenInfo(customerId, shop);
                    token = customerTokenInfo.getToken();

                    String externalPrincipal = shop + "|" + "external" + "|" + token;
                    String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);

                    customerTokenInfo.setAppstleExternalToken(externalJWT);
                    String customerPaymentTokenJson = OBJECT_MAPPER.writeValueAsString(customerTokenInfo);
                    long customerTokenEndTime = System.currentTimeMillis();

                    log.info("Customer Id = {} token time taken : {}", customerId, (customerTokenEndTime - customerTokenStartTime));

                    return ResponseEntity.ok().header("Content-Type", "application/json").body(customerPaymentTokenJson);
                }
            }
            return ResponseEntity.ok().header("Content-Type", "application/json").body(null);
        }

        if (httpServletRequest.getParameterMap().containsKey("token")) {
            String[] tokenArray = httpServletRequest.getParameterMap().get("token");
            if (tokenArray.length > 0) {
                token = tokenArray[0];
            }
        }

        long shopInfoServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shop);
        log.info("shopInfoServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - shopInfoServiceFindByShopStartTime));

        ShopInfoDTO shopInfoDTOConcrete = shopInfoDTOOptional.get();

        long subscriptionCustomCssServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<SubscriptionCustomCssDTO> subscriptionCustomCssDTOOptional = subscriptionCustomCssService.findByShop(shop);
        log.info("subscriptionCustomCssServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - subscriptionCustomCssServiceFindByShopStartTime));

        long subscriptionBundleSettingsServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTO = subscriptionBundleSettingsService.findByShop(shop);
        log.info("subscriptionBundleSettingsServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - subscriptionBundleSettingsServiceFindByShopStartTime));

        long customerPortalDynamicScriptServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScriptDTO = customerPortalDynamicScriptService.findByShop(shop);
        log.info("customerPortalDynamicScriptServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalDynamicScriptServiceFindByShopStartTime));

        long bundleDynamicScriptServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<BundleDynamicScriptDTO> optionalBundleDynamicScriptDTO = bundleDynamicScriptService.findByShop(shop);
        log.info("bundleDynamicScriptServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - bundleDynamicScriptServiceFindByShopStartTime));

        long customerPortalSettingsServiceFindByShopStartTime = System.currentTimeMillis();
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
        log.info("customerPortalSettingsServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalSettingsServiceFindByShopStartTime));

        String customerPortalDynamicScript = optionalCustomerPortalDynamicScriptDTO.map(CustomerPortalDynamicScriptDTO::getDynamicScript).orElse(StringUtils.EMPTY);
        String bundleDynamicScript = optionalBundleDynamicScriptDTO.map(BundleDynamicScriptDTO::getDynamicScript).orElse(StringUtils.EMPTY);

        templateData.put("appProxyPathPrefix", Optional.ofNullable(shopInfoDTOConcrete.getManageSubscriptionsUrl()).orElse("apps/subscriptions"));
        templateData.put("useUrlWithCustomerId", String.valueOf(customerPortalSettingsDTO.isUseUrlWithCustomerId()));
        templateData.put("apiKey", shopInfoDTOConcrete.getApiKey());
        templateData.put("moneyFormat", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getMoneyFormat()));
        templateData.put("publicDomain", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getPublicDomain()));
        templateData.put("bundleTopHtml", "");
        templateData.put("bundleBottomHtml", "");
        templateData.put("customerPortalTopHtml", Optional.ofNullable(customerPortalSettingsDTO.getTopHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalBottomHtml", Optional.ofNullable(customerPortalSettingsDTO.getBottomHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalDynamicScript", StringEscapeUtils.escapeEcmaScript(customerPortalDynamicScript));
        templateData.put("bundleDynamicScript", StringEscapeUtils.escapeEcmaScript(bundleDynamicScript));
        templateData.put("browserType", "NOT_FOUND");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(shop,
            userDetails.getPassword(), userDetails.getAuthorities());
        //SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, false);
        templateData.put("appstleSubscriptionsToken", jwt);

        if (StringUtils.isBlank(token)) {
            log.info("token is blank. action=" + action + " shop=" + shop);
        }

        String tokenType = action.equals("bundle") ? "external_bundle" : "external";
        String externalPrincipal = shop + "|" + tokenType + "|" + token;
        String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);
        templateData.put("appstleSubscriptionsExternalToken", externalJWT);

        if (subscriptionBundleSettingsDTO.isPresent()) {
            String bundleTopHtml = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleTopHtml()).orElse("");
            String bundleBottomHtml = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleBottomHtml()).orElse("");
            Boolean bundleWithoutScroll = Optional.ofNullable(subscriptionBundleSettingsDTO.get().isIsBundleWithoutScroll()).orElse(false);
            String bundleRedirectOnCartPage = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleRedirect()).orElse(BundleRedirect.CHECKOUT).toString();
            String customBundleRedirectUrl = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getCustomRedirectURL()).orElse("");

            templateData.put("bundleTopHtml", bundleTopHtml);
            templateData.put("bundleBottomHtml", bundleBottomHtml);
            templateData.put("bundleWithoutScroll", bundleWithoutScroll);
            templateData.put("bundleRedirectOnCartPage", bundleRedirectOnCartPage);
            templateData.put("customBundleRedirectUrl", customBundleRedirectUrl);
            templateData.put("token", token);
        }

        templateData.put("renderType", renderType);

        String contentType = "html".equals(renderType) ? "text/html" : "application/liquid";
        log.info("contentType=" + contentType);

        if (!action.equals("portal")) {
            templateData.put("bundle_iframe_css", "");
            if (subscriptionCustomCssDTOOptional.isPresent()) {
                String iframeCss = Optional.ofNullable(subscriptionCustomCssDTOOptional.get().getBundlingIframeCSS()).orElse("");
                templateData.put("bundle_iframe_css", iframeCss);
            }

            if (BuildBoxVersion.V2.equals(shopInfoDTOConcrete.getBuildBoxVersion())) {
                buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V2, shopInfoDTOConcrete);
                templateName = "bundles-v2.ftl";
            } else if (BuildBoxVersion.V2_IFRAME.equals(shopInfoDTOConcrete.getBuildBoxVersion())) {
                buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V2, shopInfoDTOConcrete);
                templateName = "bundles-iframe-v2.ftl";
            } else {
                buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V1, shopInfoDTOConcrete);
                templateName = "bundles-iframe.ftl";
            }

            log.info("shop=" + shop + " bundleTemplateData=" + templateData);
            String json = ftlUtils.generateContentFor("UNKNOWN_SHOP", templateData, templateName);
            return ResponseEntity.ok().header("Content-Type", contentType).body(json);
        }

        if (StringUtils.isNotBlank(portalVersion)) {
            if (portalVersion.equalsIgnoreCase(CustomerPortalMode.V3.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3, shop);
                templateName = "customer-subscriptions-v3.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.V3_IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3_IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v3.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v2.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.NO_IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.NO_IFRAME, shop);
                templateName = "customer-subscriptions-v2.ftl";
            }
        } else {
            if (CustomerPortalMode.V3.equals(shopInfoDTOConcrete.getCustomerPortalMode())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3, shop);
                templateName = "customer-subscriptions-v3.ftl";
            } else if (CustomerPortalMode.V3_IFRAME.equals(shopInfoDTOConcrete.getCustomerPortalMode())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3_IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v3.ftl";
            } else if (shopInfoDTOConcrete.getCustomerPortalMode().equals(CustomerPortalMode.IFRAME)) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v2.ftl";
            } else {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.NO_IFRAME, shop);
                templateName = "customer-subscriptions-v2.ftl";
            }
        }


        ///response.addCookie(getSocialAuthenticationCookie(jwt));

        //log.info("shop=" + shop + " customerPortalTemplateData=" + templateData);

        String json = ftlUtils.generateContentFor("UNKNOWN_SHOP", templateData, templateName);

        long totalEndTime = System.currentTimeMillis();

        log.info("totalTimeTaken=" + (totalEndTime - totalStartTime) + " shop=" + shop + " action=" + Optional.of(action).orElse("CUSTOMER_PORTAL"));

        return ResponseEntity.ok().header("Content-Type", contentType).body(json);
    }

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/subscriptions/bb/{token}",
        produces = {"application/liquid", "application/json", "text/html"}
    )
    @ResponseBody
    public ResponseEntity<String> getBuildABox(@PathVariable("token") String token,
                                                   @RequestParam(value = "renderType", defaultValue = "liquid") String renderType,
                                                   HttpServletRequest httpServletRequest) throws Exception {

        Map<String, Object> templateData = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        templateData.put("version", String.valueOf(currentTime));
        String templateName = null;

        if (!httpServletRequest.getParameterMap().containsKey(SHOP)) {
            return ResponseEntity.ok().header("Content-Type", "application/json").body(null);
        }

        String shop = httpServletRequest.getParameterMap().get(SHOP)[0];

        UserDetails userDetails = userDetailsService.loadUserByUsername(shop);

        long shopInfoServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shop);
        log.info("shopInfoServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - shopInfoServiceFindByShopStartTime));

        ShopInfoDTO shopInfoDTOConcrete = shopInfoDTOOptional.get();

        long subscriptionCustomCssServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<SubscriptionCustomCssDTO> subscriptionCustomCssDTOOptional = subscriptionCustomCssService.findByShop(shop);
        log.info("subscriptionCustomCssServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - subscriptionCustomCssServiceFindByShopStartTime));

        long subscriptionBundleSettingsServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTO = subscriptionBundleSettingsService.findByShop(shop);
        log.info("subscriptionBundleSettingsServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - subscriptionBundleSettingsServiceFindByShopStartTime));

        long customerPortalDynamicScriptServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScriptDTO = customerPortalDynamicScriptService.findByShop(shop);
        log.info("customerPortalDynamicScriptServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalDynamicScriptServiceFindByShopStartTime));

        long bundleDynamicScriptServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<BundleDynamicScriptDTO> optionalBundleDynamicScriptDTO = bundleDynamicScriptService.findByShop(shop);
        log.info("bundleDynamicScriptServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - bundleDynamicScriptServiceFindByShopStartTime));

        long customerPortalSettingsServiceFindByShopStartTime = System.currentTimeMillis();
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
        log.info("customerPortalSettingsServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalSettingsServiceFindByShopStartTime));

        String customerPortalDynamicScript = optionalCustomerPortalDynamicScriptDTO.map(CustomerPortalDynamicScriptDTO::getDynamicScript).orElse(StringUtils.EMPTY);
        String bundleDynamicScript = optionalBundleDynamicScriptDTO.map(BundleDynamicScriptDTO::getDynamicScript).orElse(StringUtils.EMPTY);

        templateData.put("appProxyPathPrefix", Optional.ofNullable(shopInfoDTOConcrete.getManageSubscriptionsUrl()).orElse("apps/subscriptions"));
        templateData.put("useUrlWithCustomerId", String.valueOf(customerPortalSettingsDTO.isUseUrlWithCustomerId()));
        templateData.put("apiKey", shopInfoDTOConcrete.getApiKey());
        templateData.put("moneyFormat", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getMoneyFormat()));
        templateData.put("publicDomain", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getPublicDomain()));
        templateData.put("bundleTopHtml", "");
        templateData.put("bundleBottomHtml", "");
        templateData.put("customerPortalTopHtml", Optional.ofNullable(customerPortalSettingsDTO.getTopHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalBottomHtml", Optional.ofNullable(customerPortalSettingsDTO.getBottomHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalDynamicScript", StringEscapeUtils.escapeEcmaScript(customerPortalDynamicScript));
        templateData.put("bundleDynamicScript", StringEscapeUtils.escapeEcmaScript(bundleDynamicScript));
        templateData.put("browserType", "NOT_FOUND");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(shop,
            userDetails.getPassword(), userDetails.getAuthorities());
        //SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, false);
        templateData.put("appstleSubscriptionsToken", jwt);

        String tokenType = "external_bundle";
        String externalPrincipal = shop + "|" + tokenType + "|" + token;
        String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);
        templateData.put("appstleSubscriptionsExternalToken", externalJWT);

        if (subscriptionBundleSettingsDTO.isPresent()) {
            Optional<SubscriptionBundlingDTO> subscriptionBundlingDTO = subscriptionBundlingService.findOneByToken(shop, token);
            String bundleTopHtml = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleTopHtml()).orElse("");
            String bundleBottomHtml = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleBottomHtml()).orElse("");
            Boolean bundleWithoutScroll = Optional.ofNullable(subscriptionBundleSettingsDTO.get().isIsBundleWithoutScroll()).orElse(false);
            String bundleRedirectOnCartPage = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getBundleRedirect()).orElse(BundleRedirect.CHECKOUT).toString();
            String customBundleRedirectUrl = Optional.ofNullable(subscriptionBundleSettingsDTO.get().getCustomRedirectURL()).orElse("");

            templateData.put("bundleTopHtml", bundleTopHtml);
            templateData.put("bundleBottomHtml", bundleBottomHtml);
            templateData.put("bundleWithoutScroll", bundleWithoutScroll);
            templateData.put("bundleRedirectOnCartPage", bundleRedirectOnCartPage);
            templateData.put("customBundleRedirectUrl", customBundleRedirectUrl);
            templateData.put("token", token);

            if (subscriptionBundlingDTO.isPresent()) {
                templateData.put("bundleTopHtml", StringUtils.isNotBlank(subscriptionBundlingDTO.get().getBundleTopHtml()) ? subscriptionBundlingDTO.get().getBundleTopHtml() : bundleTopHtml);
                templateData.put("bundleBottomHtml", StringUtils.isNotBlank(subscriptionBundlingDTO.get().getBundleBottomHtml()) ? subscriptionBundlingDTO.get().getBundleBottomHtml() : bundleBottomHtml);
            }
        }

        templateData.put("renderType", renderType);

        String contentType = "html".equals(renderType) ? "text/html" : "application/liquid";
        log.info("contentType=" + contentType);

        templateData.put("bundle_iframe_css", "");
        if (subscriptionCustomCssDTOOptional.isPresent()) {
            String iframeCss = Optional.ofNullable(subscriptionCustomCssDTOOptional.get().getBundlingIframeCSS()).orElse("");
            templateData.put("bundle_iframe_css", iframeCss);
        }

        if (BuildBoxVersion.V2.equals(shopInfoDTOConcrete.getBuildBoxVersion())) {
            buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V2, shopInfoDTOConcrete);
            templateName = "bundles-v2.ftl";
        } else if (BuildBoxVersion.V2_IFRAME.equals(shopInfoDTOConcrete.getBuildBoxVersion())) {
            buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V2, shopInfoDTOConcrete);
            templateName = "bundles-iframe-v2.ftl";
        } else {
            buildBundleAssetUrls(templateData, LATEST_BUILD_TIME, BuildBoxVersion.V1, shopInfoDTOConcrete);
            templateName = "bundles-iframe.ftl";
        }

        log.info("shop=" + shop + " bundleTemplateData=" + templateData);
        String json = ftlUtils.generateContentFor("UNKNOWN_SHOP", templateData, templateName);
        return ResponseEntity.ok().header("Content-Type", contentType).body(json);
    }

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/subscriptions/cp/{token}",
        produces = {"application/liquid", "application/json", "text/html"}
    )
    @ResponseBody
    public ResponseEntity<String> getCustomerPortal(@PathVariable("token") String token,
                                                    @RequestParam(value = "renderType", defaultValue = "liquid") String renderType,
                                                    @RequestParam(value = "portalVersion", required = false) String portalVersion,
                                                    HttpServletRequest httpServletRequest) throws Exception {

        long totalStartTime = System.currentTimeMillis();

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("version", String.valueOf(LATEST_BUILD_TIME));
        String templateName = null;

        if (!httpServletRequest.getParameterMap().containsKey(SHOP)) {
            return ResponseEntity.ok().header("Content-Type", "application/json").body(null);
        }

        String shop = httpServletRequest.getParameterMap().get(SHOP)[0];

        UserDetails userDetails = userDetailsService.loadUserByUsername(shop);


        long shopInfoServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shop);
        log.info("shopInfoServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - shopInfoServiceFindByShopStartTime));

        ShopInfoDTO shopInfoDTOConcrete = shopInfoDTOOptional.get();

        long customerPortalDynamicScriptServiceFindByShopStartTime = System.currentTimeMillis();
        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScriptDTO = customerPortalDynamicScriptService.findByShop(shop);
        log.info("customerPortalDynamicScriptServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalDynamicScriptServiceFindByShopStartTime));

        long customerPortalSettingsServiceFindByShopStartTime = System.currentTimeMillis();
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
        log.info("customerPortalSettingsServiceFindByShopTimeTaken=" + (System.currentTimeMillis() - customerPortalSettingsServiceFindByShopStartTime));

        String customerPortalDynamicScript = optionalCustomerPortalDynamicScriptDTO.map(CustomerPortalDynamicScriptDTO::getDynamicScript).orElse(StringUtils.EMPTY);

        templateData.put("appProxyPathPrefix", Optional.ofNullable(shopInfoDTOConcrete.getManageSubscriptionsUrl()).orElse("apps/subscriptions"));
        templateData.put("useUrlWithCustomerId", String.valueOf(customerPortalSettingsDTO.isUseUrlWithCustomerId()));
        templateData.put("apiKey", shopInfoDTOConcrete.getApiKey());
        templateData.put("moneyFormat", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getMoneyFormat()));
        templateData.put("publicDomain", StringEscapeUtils.escapeEcmaScript(shopInfoDTOConcrete.getPublicDomain()));
        templateData.put("customerPortalTopHtml", Optional.ofNullable(customerPortalSettingsDTO.getTopHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalBottomHtml", Optional.ofNullable(customerPortalSettingsDTO.getBottomHtml()).orElse(StringUtils.EMPTY));
        templateData.put("customerPortalDynamicScript", StringEscapeUtils.escapeEcmaScript(customerPortalDynamicScript));
        templateData.put("browserType", "NOT_FOUND");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(shop,
            userDetails.getPassword(), userDetails.getAuthorities());
        String jwt = tokenProvider.createToken(authenticationToken, false);
        templateData.put("appstleSubscriptionsToken", jwt);

        String tokenType = "external";
        String externalPrincipal = shop + "|" + tokenType + "|" + token;
        String externalJWT = tokenProvider.createToken(new UsernamePasswordAuthenticationToken(externalPrincipal, userDetails.getPassword(), userDetails.getAuthorities()), false);
        templateData.put("appstleSubscriptionsExternalToken", externalJWT);

        templateData.put("renderType", renderType);

        String contentType = "html".equals(renderType) ? "text/html" : "application/liquid";
        log.info("contentType=" + contentType);

        if (StringUtils.isNotBlank(portalVersion)) {
            if (portalVersion.equalsIgnoreCase(CustomerPortalMode.V3.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3, shop);
                templateName = "customer-subscriptions-v3.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.V3_IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3_IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v3.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v2.ftl";
            } else if (portalVersion.equalsIgnoreCase(CustomerPortalMode.NO_IFRAME.toString())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.NO_IFRAME, shop);
                templateName = "customer-subscriptions-v2.ftl";
            }
        } else {
            if (CustomerPortalMode.V3.equals(shopInfoDTOConcrete.getCustomerPortalMode())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3, shop);
                templateName = "customer-subscriptions-v3.ftl";
            } else if (CustomerPortalMode.V3_IFRAME.equals(shopInfoDTOConcrete.getCustomerPortalMode())) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.V3_IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v3.ftl";
            } else if (shopInfoDTOConcrete.getCustomerPortalMode().equals(CustomerPortalMode.IFRAME)) {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.IFRAME, shop);
                templateName = "customer-subscriptions-iframe-v2.ftl";
            } else {
                buildCustomerPortalAssetUrls(templateData, LATEST_BUILD_TIME, CustomerPortalMode.NO_IFRAME, shop);
                templateName = "customer-subscriptions-v2.ftl";
            }
        }

        String json = ftlUtils.generateContentFor("UNKNOWN_SHOP", templateData, templateName);

        long totalEndTime = System.currentTimeMillis();

        log.info("totalTimeTaken=" + (totalEndTime - totalStartTime) + " shop=" + shop);

        return ResponseEntity.ok().header("Content-Type", contentType).body(json);
    }

    private void buildCustomerPortalAssetUrls(Map<String, Object> templateData, String latestBuildTime, CustomerPortalMode customerPortalMode, String shop) {
        if (customerPortalMode == CustomerPortalMode.V3 || customerPortalMode == CustomerPortalMode.V3_IFRAME) {
            templateData.put("customerJavascript", appendVersionParams(JS_CUSTOMER, shop, latestBuildTime));
            templateData.put("customerCss", appendVersionParams(CSS_CUSTOMER, shop, latestBuildTime));
        } else if (customerPortalMode == CustomerPortalMode.IFRAME) {
            templateData.put("vendorJavascript", appendVersionParams(JS_VENDORS, shop, latestBuildTime));
            templateData.put("customerJavascript", appendVersionParams("https://subscription-admin.appstle.com/app/customer.bundle.js", shop, latestBuildTime));
            templateData.put("vendorCss", appendVersionParams(CSS_VENDORS, shop, latestBuildTime));
            templateData.put("customerCss", appendVersionParams("https://subscription-admin.appstle.com/content/customer.css", shop, latestBuildTime));
        } else if (customerPortalMode == CustomerPortalMode.NO_IFRAME) {
            templateData.put("vendorJavascript", appendVersionParams(JS_VENDORS, shop, latestBuildTime));
            templateData.put("customerJavascript", appendVersionParams("https://subscription-admin.appstle.com/app/customer.bundle.js", shop, latestBuildTime));
            templateData.put("vendorCss", appendVersionParams(CSS_VENDORS, shop, latestBuildTime));
            templateData.put("customerCss", appendVersionParams("https://subscription-admin.appstle.com/content/customer.css", shop, latestBuildTime
            ));
        }
    }

    @Autowired
    @Qualifier("taskExecutor")
    private Executor asyncExecutor;

    private void buildBundleAssetUrls(Map<String, Object> templateData, String latestBuildTime, BuildBoxVersion buildBoxVersion, ShopInfoDTO shopInfoDTO) {
        String shop = shopInfoDTO.getShop();
        if (buildBoxVersion == BuildBoxVersion.V2) {
            templateData.put("bundleJavascript", appendVersionParams(JS_BUNDLE, shop, latestBuildTime));
            templateData.put("bundleCss", appendVersionParams(CSS_BUNDLE, shop, latestBuildTime));
        } else if (buildBoxVersion == BuildBoxVersion.V1) {
            templateData.put("vendorJavascript", appendVersionParams(JS_VENDORS, shop, latestBuildTime));
            templateData.put("bundleJavascript", appendVersionParams("https://subscription-admin.appstle.com/app/bundles.bundle.js", shop, latestBuildTime));
            templateData.put("vendorCss", appendVersionParams(CSS_VENDORS, shop, latestBuildTime));
            templateData.put("bundleCss", appendVersionParams("https://subscription-admin.appstle.com/content/customer.css", shop, latestBuildTime));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/subscriptions/cart-discount", produces = {"application/json", "text/html", "application/x-www-form-urlencoded"})
    @ResponseBody
    public DiscountCodeResponse createCartDiscount(@RequestBody Cart cart, HttpServletRequest httpServletRequest) throws Exception {
        String shop = httpServletRequest.getParameterMap().get(SHOP)[0];
        return getDiscountCodeResponse(shop, cart);
    }

    @NotNull
    private DiscountCodeResponse getDiscountCodeResponse(String shop, Cart discountCodeRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DiscountCodeResponse discountCodeResponse = new DiscountCodeResponse();

        List<BundleRule> rules = bundleRuleService.findAllByShopAndStatus(shop, BundleStatus.ACTIVE);


        BundleRule applicableBundleRule = null;
        List<BundleRuleProductDTO> applicableProducts = null;

        boolean haveSubscriptionProduct = discountCodeRequest.getItems().stream().anyMatch(item -> item.getAdditionalProperties().containsKey("selling_plan_allocation"));

        List<Long> cartProductIds = discountCodeRequest.getItems().stream()
            .map(Item::getProductId).collect(Collectors.toList());

        List<Long> cartVariantIds = discountCodeRequest.getItems().stream()
            .map(Item::getVariantId)
            .collect(Collectors.toList());

        int totalQuantity = discountCodeRequest.getItems().stream().map(Item::getQuantity).mapToInt(Long::intValue).sum();

        if (rules.size() > 0) {
            for (var i = 0; i < rules.size(); i++) {
                BundleRule bundleRule = rules.get(i);
                if (bundleRule.getBundleLevel().equals(BundleLevel.PRODUCT)) {
                    List<BundleRuleProductDTO> bundleRuleProducts = mapper.readValue(bundleRule.getProducts(), new TypeReference<List<BundleRuleProductDTO>>() {
                    });

                    List<Long> rulesProducts = bundleRuleProducts.stream().map(BundleRuleProductDTO::getId).collect(Collectors.toList());
                    if ((bundleRule.getBundleType().equals(BundleType.CLASSIC) && cartProductIds.containsAll(rulesProducts)) || (bundleRule.getBundleType().equals(BundleType.MIX_AND_MATCH) && rulesProducts.containsAll(cartProductIds))) {
                        applicableBundleRule = bundleRule;
                        applicableProducts = bundleRuleProducts;
                        break;
                    }
                } else if (bundleRule.getBundleLevel().equals(BundleLevel.VARIANT)) {
                    List<BundleRuleProductDTO> bundleRuleVariants = mapper.readValue(bundleRule.getVariants(), new TypeReference<List<BundleRuleProductDTO>>() {
                    });

                    List<Long> rulesVariants = bundleRuleVariants.stream().map(BundleRuleProductDTO::getId).collect(Collectors.toList());
                    if ((bundleRule.getBundleType().equals(BundleType.CLASSIC) && cartVariantIds.containsAll(rulesVariants)) || (bundleRule.getBundleType().equals(BundleType.MIX_AND_MATCH) && rulesVariants.containsAll(cartVariantIds))) {
                        applicableBundleRule = bundleRule;
                        applicableProducts = bundleRuleVariants;
                        break;
                    }
                }
            }

        }


        if (applicableBundleRule != null && applicableBundleRule.getDiscountValue() != null && applicableBundleRule.getDiscountValue() > 0) {
            if (applicableBundleRule.getBundleType().equals(BundleType.CLASSIC) ||
                (applicableBundleRule.getBundleType().equals(BundleType.MIX_AND_MATCH) &&
                    ((applicableBundleRule.getMinimumNumberOfItems() != null && applicableBundleRule.getMaximumNumberOfItems() != null) && (totalQuantity >= applicableBundleRule.getMinimumNumberOfItems() && totalQuantity <= applicableBundleRule.getMaximumNumberOfItems())) ||
                    ((applicableBundleRule.getMaximumNumberOfItems() == null && applicableBundleRule.getMinimumNumberOfItems() != null) && totalQuantity >= applicableBundleRule.getMinimumNumberOfItems()) ||
                    ((applicableBundleRule.getMinimumNumberOfItems() == null && applicableBundleRule.getMaximumNumberOfItems() != null) && totalQuantity <= applicableBundleRule.getMaximumNumberOfItems())
                )
            ) {
                Map<Long, Set<Long>> variantIdsByProductIds = new HashMap<>();

                for (Item item : discountCodeRequest.getItems()) {
                    Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(item.getProductId(), new HashSet<>());
                    productVariantIds.add(item.getVariantId());
                    variantIdsByProductIds.put(item.getProductId(), productVariantIds);
                }

                List<String> productsToAdd = new ArrayList<>();
                if (applicableBundleRule.getBundleLevel().equals(BundleLevel.PRODUCT)) {
                    List<BundleRuleProductDTO> bundleRuleProducts = mapper.readValue(applicableBundleRule.getProducts(), new TypeReference<List<BundleRuleProductDTO>>() {
                    });
                    productsToAdd.addAll(bundleRuleProducts.stream().filter(bundleRuleProductDTO -> {
                            boolean found = cartProductIds.contains(bundleRuleProductDTO.getId());
                            if (found) {
                                Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(bundleRuleProductDTO.getId(), new HashSet<>());
                                cartVariantIds.removeAll(productVariantIds);
                            }
                            return found;
                        }).map(p -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + p.getId())
                        .collect(Collectors.toList()));
                }

                List<String> variantsToAdd = new ArrayList<>();
                if (applicableBundleRule.getBundleLevel().equals(BundleLevel.VARIANT)) {
                    List<BundleRuleProductDTO> bundleRuleVariants = mapper.readValue(applicableBundleRule.getVariants(), new TypeReference<List<BundleRuleProductDTO>>() {
                    });
                    variantsToAdd.addAll(bundleRuleVariants.stream().filter(bundleRuleVariantDTO -> {
                            return cartVariantIds.contains(bundleRuleVariantDTO.getId());
                        }).map(p -> ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + p.getId())
                        .collect(Collectors.toList()));
                }


                DiscountProductsInput discountProductsInput = DiscountProductsInput
                    .builder()
                    .productsToAdd(productsToAdd)
                    .productVariantsToAdd(variantsToAdd)
                    .build();

                DiscountItemsInput discountItemsInput = DiscountItemsInput
                    .builder()
                    .products(discountProductsInput)
                    .build();

                DiscountCustomerGetsValueInput.Builder customerDiscount = DiscountCustomerGetsValueInput.builder();
                if (applicableBundleRule.getDiscountType().equals(BundleDiscountType.PERCENTAGE)) {
                    customerDiscount.percentage(((applicableBundleRule.getDiscountValue() != null ? applicableBundleRule.getDiscountValue() : new Double(0)) / 100));
                } else if (applicableBundleRule.getDiscountType().equals(BundleDiscountType.FIXED_AMOUNT)) {
                    customerDiscount.discountAmount(
                        DiscountAmountInput.builder()
                            .amount(applicableBundleRule.getDiscountValue())
                            .appliesOnEachItem(false)
                            .build()
                    );
                }

                DiscountCustomerGetsInput.Builder discountCustomerGetInputBuilder = DiscountCustomerGetsInput
                    .builder()
                    .items(discountItemsInput)
                    .value(
                        customerDiscount.build()
                    );

                if (haveSubscriptionProduct) {
                    discountCustomerGetInputBuilder.appliesOnSubscription(true);
                    discountCustomerGetInputBuilder.appliesOnOneTimePurchase(true);
                }

                DiscountCustomerGetsInput discountCustomerGetsInput = discountCustomerGetInputBuilder.build();

                DiscountCustomerSelectionInput discountCustomerSelectionInput = DiscountCustomerSelectionInput
                    .builder()
                    .all(true)
                    .build();

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
                String formattedDate = dateTimeFormatter.format(ZonedDateTime.now());

                DiscountMinimumQuantityInput discountMinimumQuantityInput =
                    DiscountMinimumQuantityInput
                        .builder()
                        .greaterThanOrEqualToQuantity(applicableBundleRule.getBundleType().equals(BundleType.CLASSIC) ? String.valueOf(applicableProducts.size()): String.valueOf(applicableBundleRule.getMinimumNumberOfItems()))
                        .build();

                DiscountMinimumRequirementInput discountMinimumRequirementInput =
                    DiscountMinimumRequirementInput
                        .builder()
                        .quantity(discountMinimumQuantityInput)
                        .build();

                DiscountCombinesWithInput discountCombinesWithInput = DiscountCombinesWithInput.builder()
                    .productDiscounts(true)
                    .orderDiscounts(true)
                    .shippingDiscounts(true)
                    .build();

                DiscountCodeBasicInput discountCodeBasicInput = DiscountCodeBasicInput
                    .builder()
                    .code("BUNDLE_DISCOUNT_" + RandomStringUtils.randomAlphabetic(10))
                    .customerGets(discountCustomerGetsInput)
                    .title("BUNDLE_DISCOUNT")
                    .minimumRequirement(discountMinimumRequirementInput)
                    .customerSelection(discountCustomerSelectionInput)
                    .combinesWith(discountCombinesWithInput)
                    .startsAt(formattedDate)
                    .build();

                DiscountCodeBasicCreateMutation discountCodeBasicCreateMutation = new DiscountCodeBasicCreateMutation(discountCodeBasicInput);

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                Response<Optional<DiscountCodeBasicCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(discountCodeBasicCreateMutation);

                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("errors=" + optionalMutationResponse.getErrors().get(0).getMessage() + " shop=" + shop);
                    discountCodeResponse.setDiscountNeeded(false);
                    return discountCodeResponse;
                }

                List<DiscountCodeBasicCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                    .map(d -> d.getDiscountCodeBasicCreate().map(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getUserErrors)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.info("errors=" + userErrors.toString() + " shop=" + shop);
                    discountCodeResponse.setErrorMessage(userErrors.get(0).getMessage());
                    discountCodeResponse.setDiscountNeeded(false);
                    return discountCodeResponse;
                }

                DiscountCodeBasicCreateMutation.CodeDiscountNode codeDiscountNode = optionalMutationResponse
                    .getData()
                    .flatMap(e -> e.getDiscountCodeBasicCreate()
                        .flatMap(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getCodeDiscountNode))
                    .orElse(null);

                String discountCodeString = null;
                if (codeDiscountNode != null) {
                    discountCodeString = ((DiscountCodeBasicCreateMutation.AsDiscountCodeBasic) codeDiscountNode.getCodeDiscount())
                        .getCodes().getEdges().get(0).getNode().getCode();
                }

                discountCodeResponse.setDiscountCode(discountCodeString);
                discountCodeResponse.setDiscountNeeded(true);
            } else {
                discountCodeResponse.setDiscountNeeded(false);
            }
        } else {
            discountCodeResponse.setDiscountNeeded(false);
        }

        return discountCodeResponse;
    }
}

package com.et.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.asset.Asset;
import com.et.api.shopify.asset.GetAssetResponse;
import com.et.api.shopify.asset.UpdateAssetRequest;
import com.et.api.shopify.asset.UpdateAssetResponse;
import com.et.api.shopify.page.*;
import com.et.api.shopify.scripttag.CreateScriptTagRequest;
import com.et.api.shopify.scripttag.CreateScriptTagResponse;
import com.et.api.shopify.scripttag.GetScriptTagsResponse;
import com.et.api.shopify.scripttag.ScriptTag;
import com.et.api.shopify.theme.GetThemesResponse;
import com.et.api.utils.MetafieldUtils;
import com.et.domain.PaymentPlan;
import com.et.domain.ThemeCode;
import com.et.domain.ThemeSettings;
import com.et.domain.WidgetTemplate;
import com.et.domain.enumeration.*;
import com.et.liquid.LiquidUtils;
import com.et.pojo.LabelValueInfo;
import com.et.repository.ThemeCodeRepository;
import com.et.repository.ThemeSettingsRepository;
import com.et.repository.WidgetTemplateRepository;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.shopify.java.graphql.client.queries.ShopQuery;
import com.shopify.java.graphql.client.type.MetafieldsSetInput;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static com.et.constant.Constants.*;
import static com.et.utils.CommonUtils.appendVersionParams;

@Component
public class SubscribeItScriptUtils {

 /*   public static final List<String> HARD_WHITELISTED_SHOP_NAMES = List.of("rabraham-dev-01.myshopify.com", "bingalls-dev-02.myshopify.com", "jayua-dev.myshopify.com", "eric-dev01.myshopify.com",
        "bingalls-uaudio.myshopify.com", "uaudio-appstle.myshopify.com", "uaudio-stage.myshopify.com", "uaudio-prod.myshopify.com", "uaudio-dev.myshopify.com",
        "sgrona-dev-01.myshopify.com");*/

    @Value("${hard_whitelisted_shop_names}")
    private List<String> HARD_WHITELISTED_SHOP_NAMES;
    public static final List<String> WHITELISTED_SHOP_NAMES = List.of("inspiredgo.myshopify.com");
    private final Logger log = LoggerFactory.getLogger(SubscribeItScriptUtils.class);

    @Autowired
    private LiquidUtils liquidUtils;

    @Autowired
    private FTLUtils ftlUtils;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private ThemeCodeRepository themeCodeRepository;

    @Autowired
    private ThemeSettingsRepository themeSettingsRepository;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private BundleRuleService bundleRuleService;

    @Autowired
    private BundleSettingService bundleSettingService;

    @Autowired
    private ShopLabelService shopLabelService;

    @Value("classpath:templates/appstle-menu.ftl")
    Resource appstleMenuLiquid;

    @Value("classpath:templates/appstle-subscription-helper-v1.liquid")
    Resource appstleSubscriptionHelperV1;

    @Value("${oauth.base-uri}")
    private String oauthBaseUri;

    @Autowired
    private SubscriptionCustomCssService subscriptionCustomCssService;

    @Autowired
    private ShopCustomizationService shopCustomizationService;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private WidgetTemplateRepository widgetTemplateRepository;

    @Autowired
    private SubscriptionGroupService subscriptionGroupService;

    @Autowired
    private AppstleMenuSettingsService appstleMenuSettingsService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private AppstleMenuLabelsService appstleMenuLabelsService;

    @Autowired
    private MetafieldUtils metafieldUtils;

    @Autowired
    private ShopAssetUrlsService shopAssetUrlsService;

    @Autowired
    private SellingPlanMemberInfoService sellingPlanMemberInfoService;


    public static final String PUBLISHED_ROLE = "main";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final Gson gson = new Gson();

    public SubscribeItScriptUtils() {
        super();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public void createOrUpdateFileInCloud(String shopName) {
        createOrUpdateFileInCloud(shopName, false, null);
    }

    public void createOrUpdateFileInCloud(String shopName, Long themeId) {
        createOrUpdateFileInCloud(shopName, false, themeId);
    }

    @Async
    public void createOrUpdateFileInCloudAsync(String shopName) {
        createOrUpdateFileInCloud(shopName);
    }

    @Async
    public void createOrUpdateFileInCloudAsync(String shopName, Long themeId) {
        createOrUpdateFileInCloud(shopName, themeId);
    }

    public void createOrUpdateFileInCloud(String shopName, boolean generatePage, Long themeId) {

        System.out.println("\n\n\n\nupdating store-front------------\n\n\n\n");
        try {

            ShopInfoDTO shopInfo = shopInfoService.findByShop(shopName).get();

            ThemeSettings themeSettings = themeSettingsRepository.findByShop(shopName);

            Optional<PaymentPlan> optionalPaymentPlan = paymentPlanService.findByShop(shopName);

            Map<String, Object> templateData = buildTemplateData(shopName, shopInfo, themeSettings, optionalPaymentPlan);

            if (Optional.ofNullable(themeSettings.getShopifyThemeInstallationVersion()).orElse(ShopifyThemeInstallationVersion.V1) == ShopifyThemeInstallationVersion.V1) {
                throw new BadRequestAlertException("Shop is on deprecated V1 version. Please update to V2 version.", "", "v1");
            }

            boolean isCustomJs = StringUtils.isNotBlank(themeSettings.getCustomJavascript());

            boolean isBundleEnabled = false;

            List<BundleRuleDTO> bundleRuleDTOList = bundleRuleService.findAllByShop(shopName);
            Optional<BundleSettingDTO> bundleSettingDTOOptional = bundleSettingService.findByShop(shopName);

            Map<String, Object> bundleTemplateData = null;
            if (bundleSettingDTOOptional.isPresent() && !CollectionUtils.isEmpty(bundleRuleDTOList)) {
                isBundleEnabled = true;
                templateData.put("bundleEnabled", true);

                bundleTemplateData = buildBundleTemplateData(shopName, bundleSettingDTOOptional.get(), bundleRuleDTOList);
            } else {
                templateData.put("bundleEnabled", false);
            }

            templateData.put("bundle", OBJECT_MAPPER.writeValueAsString(Optional.ofNullable(bundleTemplateData).orElse(new HashMap<>())));

            //Add Appstle menu settings

            ShopifyAPI api = commonUtils.prepareShopifyResClient(shopName);

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shopName);

            List<GetThemesResponse.BasicThemeInfo> themeInfos = api.getThemes().getThemes();

            Optional<GetThemesResponse.BasicThemeInfo> selectedThemeOptional = Optional.ofNullable(null);

            if(themeId != null) {
                selectedThemeOptional = themeInfos.stream().filter(t -> t.getId().equals(themeId)).findFirst();
            } else {
                selectedThemeOptional = themeInfos.stream().filter(t -> t.getRole().equalsIgnoreCase(PUBLISHED_ROLE)).findFirst();
            }

            if (selectedThemeOptional.isPresent() && !HARD_WHITELISTED_SHOP_NAMES.contains(shopName)) {
                GetThemesResponse.BasicThemeInfo theme = selectedThemeOptional.get();

                String appstleSubscriptionAssetUrl = "";
                String appstleBundleAssetUrl = "";
                String appstleInitUrl = "";

                if (WHITELISTED_SHOP_NAMES.contains(shopName)) {
                    appstleSubscriptionAssetUrl = api.getAsset("assets/appstle-subscription.js", theme.getId()).getAsset().getPublic_url();
                } else if(isCustomJs) {
                    appstleSubscriptionAssetUrl = appendVersionParams(APPSTLE_SUBSCRIPTION_BASE_URL + "/assets/custom/appstle-subscription.js", shopName, System.currentTimeMillis());
                } else if (ScriptVersion.V2.equals(shopInfo.getScriptVersion())) {
                    appstleSubscriptionAssetUrl = BooleanUtils.isTrue(shopInfo.isDevEnabled()) ? JS_APPSTLE_SUBSCRIPTION_V2 : JS_APPSTLE_SUBSCRIPTION_V2_MIN;
                    appstleSubscriptionAssetUrl = appendVersionParams(appstleSubscriptionAssetUrl, shopName, LATEST_BUILD_TIME);
                } else {
                    appstleSubscriptionAssetUrl = BooleanUtils.isTrue(shopInfo.isDevEnabled()) ? JS_APPSTLE_SUBSCRIPTION : JS_APPSTLE_SUBSCRIPTION_MIN;
                    appstleSubscriptionAssetUrl = appendVersionParams(appstleSubscriptionAssetUrl, shopName, LATEST_BUILD_TIME);
                }


                if (isBundleEnabled) {
                    appstleBundleAssetUrl = BooleanUtils.isTrue(shopInfo.isDevEnabled()) ? JS_APPSTLE_BUNDLE_V1 : JS_APPSTLE_BUNDLE_V1_MIN;
                    appstleBundleAssetUrl = appendVersionParams(appstleBundleAssetUrl, shopName, LATEST_BUILD_TIME);
                }

                if (Optional.ofNullable(themeSettings.getShopifyThemeInstallationVersion()).orElse(ShopifyThemeInstallationVersion.V1) == ShopifyThemeInstallationVersion.V1) {

                    writeSubscriptionHelperToTheme(api, templateData, theme);

                } else {
                    // removeHelperSnippetFromTheme(api, theme);
                }

                deleteExistingScriptTags(api);

                templateData.put("appstle_subscription_path", appstleSubscriptionAssetUrl);
                templateData.put("appstle_bundle_path", appstleBundleAssetUrl);

                templateData.put("moneyFormat", StringEscapeUtils.escapeJson(shopInfo.getMoneyFormat()));
                updateShopMetafieldsForSettings(api, templateData, shopifyGraphqlClient);
            } else if (HARD_WHITELISTED_SHOP_NAMES.contains(shopName)) {
                deleteExistingScriptTags(api);
            }

            if (generatePage) {
                createSubscriptionPage(api, shopInfo, templateData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static final String METAFIELD_NAMESPACE = "appstle_subscription";

    private void updateShopMetafieldsForSettings(ShopifyAPI api, Map<String, Object> templateData, ShopifyGraphqlClient shopifyGraphqlClient) throws JsonProcessingException {
        try {

            String widgetTemplateHtml = "";
            if (templateData.containsKey("widgetTemplateHtml")) {
                widgetTemplateHtml = templateData.get("widgetTemplateHtml").toString();
                templateData.remove("widgetTemplateHtml");
            }

            String bundleSettings = "";
            if (templateData.containsKey("bundle")) {
                bundleSettings = templateData.get("bundle").toString();
                templateData.remove("bundle");
            }

            String labelJson = "";
            if (templateData.containsKey("labels")) {
                labelJson = templateData.get("labels").toString();
                templateData.remove("labels");
            }

            String settingsJsonString = OBJECT_MAPPER.writeValueAsString(templateData);

            ShopQuery shopQuery = new ShopQuery();
            Response<Optional<ShopQuery.Data>> shopQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopQuery);
            String shopId = Objects.requireNonNull(Objects.requireNonNull(shopQueryResponse.getData())).map(ShopQuery.Data::getShop).map(ShopQuery.Shop::getId).orElse(null);

            List<MetafieldsSetInput> metafields = new ArrayList<>();


            metafields.add(MetafieldUtils.buildMetafieldInput(shopId, METAFIELD_NAMESPACE, "setting", settingsJsonString, "json"));
            metafields.add(MetafieldUtils.buildMetafieldInput(shopId, METAFIELD_NAMESPACE, "widget_template_html", widgetTemplateHtml, "multi_line_text_field"));
            metafields.add(MetafieldUtils.buildMetafieldInput(shopId, METAFIELD_NAMESPACE, "bundle", bundleSettings, "json"));
            metafields.add(MetafieldUtils.buildMetafieldInput(shopId, METAFIELD_NAMESPACE, "labels", labelJson, "multi_line_text_field"));

            metafieldUtils.createOrUpdateMetafields(shopifyGraphqlClient, metafields);

            String a = "b";

        } catch (Exception ex) {
            log.error("An error occurred while creating metafields.", ex);
        }

    }

    private void createSubscriptionPage(ShopifyAPI api, ShopInfoDTO shopInfo, Map<String, Object> templateData) throws IOException {

        String bodyHtml = ftlUtils.generateContentFor(shopInfo.getShop(), templateData, "customer-subscriptions.ftl");

        Long subscriptionPageId = shopInfo.getSearchResultPageId();

        if (subscriptionPageId == null) {
            List<Page> pages = api.getPages().getPages();
            for (Page page : pages) {
                if (page.getHandle().equals("subscriptions")) {
                    subscriptionPageId = page.getId();
                    break;
                }
            }

            if (subscriptionPageId != null) {
                shopInfo.setSearchResultPageId(subscriptionPageId);
                shopInfoService.save(shopInfo);
            }
        }

        if (subscriptionPageId == null) {

            CreatePageRequest.Page page = new CreatePageRequest.Page("Subscriptions", bodyHtml, true);
            CreatePageRequest createPageRequest = new CreatePageRequest(page);

            CreatePageResponse createPageResponse = api.createPage(createPageRequest);

            shopInfo.setSearchResultPageId(createPageResponse.getPage().getId());

            shopInfoService.save(shopInfo);
        } else {

            UpdatePageRequest updatePageRequest = new UpdatePageRequest();
            Page page = new Page();
            page.setId(subscriptionPageId);
            page.setBodyHtml(bodyHtml);
            updatePageRequest.setPage(page);
            UpdatePageResponse updatePageResponse = api.updatePage(updatePageRequest);
        }
    }

    private Map<String, Object> buildTemplateData(String shopName, ShopInfoDTO shopInfo, ThemeSettings themeSettings, Optional<PaymentPlan> optionalPaymentPlan) throws IOException {

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("shop", shopName);
        templateData.put("visible", "true");
        templateData.put("widgetButtonEnabled", "true");
        templateData.put("moneyFormat", StringEscapeUtils.escapeJson("{% raw %}" + shopInfo.getMoneyFormat() + "{% endraw %}"));
        templateData.put("appstlePlanId", optionalPaymentPlan.map(paymentPlan -> paymentPlan.getPlanInfo().getId().toString()).orElse("-1"));
        templateData.put("appstlePlanName", optionalPaymentPlan.map(paymentPlan -> paymentPlan.getPlanInfo().getName()).orElse("FREE"));
        templateData.put("appstlePlanFeatures", optionalPaymentPlan.map(paymentPlan -> paymentPlan.getAdditionalDetails()).orElse("{}"));
        templateData.put("shopifyThemeInstallationVersion", Optional.ofNullable(themeSettings.getShopifyThemeInstallationVersion()).orElse(ShopifyThemeInstallationVersion.V1).toString());
        templateData.put("disableLoadingJquery", Optional.ofNullable(themeSettings.isDisableLoadingJquery()).orElse(Boolean.FALSE).toString());
        templateData.put("enableSlowScriptLoad", Optional.ofNullable(themeSettings.isEnableSlowScriptLoad()).orElse(Boolean.FALSE).toString());
        templateData.put("scriptLoadDelay", Optional.ofNullable(themeSettings.getScriptLoadDelay()).orElse(0).toString());
        templateData.put("scriptAttributes", Optional.ofNullable(themeSettings.getScriptAttributes()).orElse("").toString());
        templateData.put("formatMoneyOverride", Optional.ofNullable(themeSettings.isFormatMoneyOverride()).orElse(Boolean.FALSE).toString());
        templateData.put("appProxyPathPrefix", StringEscapeUtils.escapeJson(Optional.ofNullable(shopInfo.getManageSubscriptionsUrl()).orElse("apps/subscriptions")));
        templateData.put("reBuyEnabled", Optional.ofNullable(shopInfo.isReBuyEnabled()).orElse(Boolean.FALSE).toString());
        templateData.put("enableAddJSInterceptor", Optional.ofNullable(shopInfo.isEnableAddJSInterceptor()).orElse(Boolean.FALSE).toString());

        if (StringUtils.isNotBlank(themeSettings.getWidgetTemplateHtml())) {
            templateData.put("widgetTemplateHtml", "{% raw %}" + themeSettings.getWidgetTemplateHtml() + "{% endraw %}");
            templateData.put("widgetType", "");
        } else if (themeSettings.getWidgetTemplateType() != null) {
            WidgetTemplate widgetTemplate = widgetTemplateRepository.findByType(themeSettings.getWidgetTemplateType());
            templateData.put("widgetTemplateHtml", "{% raw %}" + widgetTemplate.getHtml() + "{% endraw %}");
            templateData.put("widgetType", widgetTemplate.getType() != null ? widgetTemplate.getType().name() : "");
        } else {
            templateData.put("widgetTemplateHtml", "");
            templateData.put("widgetType", "");
        }

        String atcButtonSelector = "";
        String subscriptionLinkSelector = "";
        String cartHiddenAttributesSelector="";
        String priceSelector = "";
        String quickViewClickSelector = "";
        String landingPagePriceSelector = "";

        String cartRowSelector = "";
        String cartLineItemSelector = "";
        String cartLineItemPerQuantityPriceSelector = "";
        String cartLineItemTotalPriceSelector = "";
        String cartLineItemSellingPlanNameSelector = "";
        String cartSubTotalSelector = "";
        String cartLineItemPriceSelector = "";

        PlacementPosition pricePlacement = PlacementPosition.BEFORE;
        PlacementPosition atcButtonPlacement = PlacementPosition.BEFORE;
        PlacementPosition subscriptionLinkPlacement = PlacementPosition.BEFORE;

        List<FrequencyInfoDTO> allSellingPlans = subscriptionGroupService.getAllSellingPlans(shopName);
        templateData.put("sellingPlansJson", gson.toJson(allSellingPlans));

        List<SellingPlanMemberInfoDTO> sellingPlanMemberInfoList = sellingPlanMemberInfoService.findByShop(shopName);

        Map<Long, SellingPlanMemberInfoDTO> memberOnlySellingPlansMap = new HashMap<>();
        Map<Long, SellingPlanMemberInfoDTO> nonMemberOnlySellingPlansMap = new HashMap<>();

        for (SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO : sellingPlanMemberInfoList) {
            if (sellingPlanMemberInfoDTO.isEnableMemberInclusiveTag()) {
                memberOnlySellingPlansMap.put(sellingPlanMemberInfoDTO.getSellingPlanId(), sellingPlanMemberInfoDTO);
            }
            if (sellingPlanMemberInfoDTO.isEnableMemberExclusiveTag()) {
                nonMemberOnlySellingPlansMap.put(sellingPlanMemberInfoDTO.getSellingPlanId(), sellingPlanMemberInfoDTO);
            }
        }

        try {
            allSellingPlans.stream().filter(s -> BooleanUtils.isTrue(s.getMemberOnly())).forEach(s -> {
                long sellingPlanId = Long.parseLong(s.getId().replace(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX, ""));
                SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = new SellingPlanMemberInfoDTO();
                sellingPlanMemberInfoDTO.setSellingPlanId(sellingPlanId);
                sellingPlanMemberInfoDTO.setEnableMemberExclusiveTag(false);
                sellingPlanMemberInfoDTO.setEnableMemberInclusiveTag(true);
                sellingPlanMemberInfoDTO.setMemberInclusiveTags(s.getMemberInclusiveTags());
                sellingPlanMemberInfoDTO.setMemberExclusiveTags(s.getMemberExclusiveTags());
                sellingPlanMemberInfoDTO.setShop(shopName);
                memberOnlySellingPlansMap.put(sellingPlanId, sellingPlanMemberInfoDTO);
            });

            allSellingPlans.stream().filter(s -> BooleanUtils.isTrue(s.getNonMemberOnly())).forEach(s -> {
                long sellingPlanId = Long.parseLong(s.getId().replace(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX, ""));
                SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = new SellingPlanMemberInfoDTO();
                sellingPlanMemberInfoDTO.setSellingPlanId(sellingPlanId);
                sellingPlanMemberInfoDTO.setEnableMemberExclusiveTag(true);
                sellingPlanMemberInfoDTO.setEnableMemberInclusiveTag(false);
                sellingPlanMemberInfoDTO.setMemberInclusiveTags(s.getMemberInclusiveTags());
                sellingPlanMemberInfoDTO.setMemberExclusiveTags(s.getMemberExclusiveTags());
                sellingPlanMemberInfoDTO.setShop(shopName);
                memberOnlySellingPlansMap.put(sellingPlanId, sellingPlanMemberInfoDTO);
            });
        } catch (Exception ex) {
            log.error("An error occurred while setting new member only tags. ex=" + ExceptionUtils.getStackTrace(ex));
        }

        String memberOnlySellingPlansJson = gson.toJson(memberOnlySellingPlansMap);
        String nonMemberOnlySellingPlansJson = gson.toJson(nonMemberOnlySellingPlansMap);
        templateData.put("memberOnlySellingPlansJson", memberOnlySellingPlansJson);
        templateData.put("nonMemberOnlySellingPlansJson", nonMemberOnlySellingPlansJson);


        String badgeTop = "";

        if (themeSettings != null) {
            Optional<ThemeCode> themeCodeOptional = themeCodeRepository.findByThemeName(themeSettings.getThemeName());

            if (StringUtils.isNotBlank(themeSettings.getSelectedSelector())) {
                atcButtonSelector = themeSettings.getSelectedSelector();
            } else if (themeCodeOptional.isPresent() && StringUtils.isNotBlank(themeCodeOptional.get().getAddToCartSelector())) {
                atcButtonSelector = themeCodeOptional.get().getAddToCartSelector();
            }

            if (StringUtils.isNotBlank(themeSettings.getSubscriptionLinkSelector())) {
                subscriptionLinkSelector = themeSettings.getSubscriptionLinkSelector();
            } else if (themeCodeOptional.isPresent() && StringUtils.isNotBlank(themeCodeOptional.get().getSubscriptionLinkSelector())) {
                subscriptionLinkSelector = themeCodeOptional.get().getSubscriptionLinkSelector();
            }
            if (StringUtils.isNotBlank(themeSettings.getCartHiddenAttributesSelector())) {
                cartHiddenAttributesSelector=themeSettings.getCartHiddenAttributesSelector();
            }

            if (themeSettings.getPlacement() != null) {
                atcButtonPlacement = themeSettings.getPlacement();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getAddToCartPlacement() != null) {
                atcButtonPlacement = themeCodeOptional.get().getAddToCartPlacement();
            }

            if (themeSettings.getSubscriptionLinkPlacement() != null) {
                subscriptionLinkPlacement = themeSettings.getSubscriptionLinkPlacement();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getSubscriptionLinkPlacement() != null) {
                subscriptionLinkPlacement = themeCodeOptional.get().getSubscriptionLinkPlacement();
            }

            if (themeSettings.getBadgeTop() != null) {
                badgeTop = themeSettings.getBadgeTop();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getBadgeTop() != null) {
                badgeTop = themeCodeOptional.get().getBadgeTop();
            }

            if (themeSettings.getPriceSelector() != null) {
                priceSelector = themeSettings.getPriceSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getPriceSelector() != null) {
                priceSelector = themeCodeOptional.get().getPriceSelector();
            }

            if (themeSettings.getQuickViewClickSelector() != null) {
                quickViewClickSelector = themeSettings.getQuickViewClickSelector();
            }

            if (themeSettings.getLandingPagePriceSelector() != null) {
                landingPagePriceSelector = themeSettings.getLandingPagePriceSelector();
            }

            if (themeSettings.getPricePlacement() != null) {
                pricePlacement = themeSettings.getPricePlacement();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getPricePlacement() != null) {
                pricePlacement = themeCodeOptional.get().getPricePlacement();
            }

            if (themeSettings.getCartRowSelector() != null) {
                cartRowSelector = themeSettings.getCartRowSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartRowSelector() != null) {
                cartRowSelector = themeCodeOptional.get().getCartRowSelector();
            }

            if (themeSettings.getCartLineItemSelector() != null) {
                cartLineItemSelector = themeSettings.getCartLineItemSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartLineItemSelector() != null) {
                cartLineItemSelector = themeCodeOptional.get().getCartLineItemSelector();
            }

            if (themeSettings.getCartLineItemPerQuantityPriceSelector() != null) {
                cartLineItemPerQuantityPriceSelector = themeSettings.getCartLineItemPerQuantityPriceSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartLineItemPerQuantityPriceSelector() != null) {
                cartLineItemPerQuantityPriceSelector = themeCodeOptional.get().getCartLineItemPerQuantityPriceSelector();
            }

            if (themeSettings.getCartLineItemTotalPriceSelector() != null) {
                cartLineItemTotalPriceSelector = themeSettings.getCartLineItemTotalPriceSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartLineItemTotalPriceSelector() != null) {
                cartLineItemTotalPriceSelector = themeCodeOptional.get().getCartLineItemTotalPriceSelector();
            }

            if (themeSettings.getCartLineItemSellingPlanNameSelector() != null) {
                cartLineItemSellingPlanNameSelector = themeSettings.getCartLineItemSellingPlanNameSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartLineItemSellingPlanNameSelector() != null) {
                cartLineItemSellingPlanNameSelector = themeCodeOptional.get().getCartLineItemSellingPlanNameSelector();
            }

            if (themeSettings.getCartSubTotalSelector() != null) {
                cartSubTotalSelector = themeSettings.getCartSubTotalSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartSubTotalSelector() != null) {
                cartSubTotalSelector = themeCodeOptional.get().getCartSubTotalSelector();
            }

            if (themeSettings.getCartLineItemPriceSelector() != null) {
                cartLineItemPriceSelector = themeSettings.getCartLineItemPriceSelector();
            } else if (themeCodeOptional.isPresent() && themeCodeOptional.get().getCartLineItemPriceSelector() != null) {
                cartLineItemPriceSelector = themeCodeOptional.get().getCartLineItemPriceSelector();
            }
        }

        templateData.put("atcButtonSelector", StringEscapeUtils.escapeJson(atcButtonSelector));
        templateData.put("subscriptionLinkSelector", StringEscapeUtils.escapeJson(subscriptionLinkSelector));
        templateData.put("cartHiddenAttributesSelector",StringEscapeUtils.escapeJson(cartHiddenAttributesSelector));
        templateData.put("atcButtonPlacement", StringEscapeUtils.escapeJson(atcButtonPlacement.toString()));
        templateData.put("subscriptionLinkPlacement", StringEscapeUtils.escapeJson(subscriptionLinkPlacement.toString()));
        templateData.put("priceSelector", StringEscapeUtils.escapeJson(Optional.ofNullable(priceSelector).orElse(StringUtils.EMPTY)));
        templateData.put("quickViewClickSelector", StringEscapeUtils.escapeJava(Optional.ofNullable(quickViewClickSelector).orElse(StringUtils.EMPTY)));
        templateData.put("landingPagePriceSelector", StringEscapeUtils.escapeJava(Optional.ofNullable(landingPagePriceSelector).orElse(StringUtils.EMPTY)));
        templateData.put("badgeTop", StringEscapeUtils.escapeJson(Optional.ofNullable(badgeTop).orElse(StringUtils.EMPTY)));
        templateData.put("pricePlacement", StringEscapeUtils.escapeJson(pricePlacement.toString()));
        templateData.put("cartRowSelector", StringEscapeUtils.escapeJson(cartRowSelector));
        templateData.put("cartLineItemSelector", StringEscapeUtils.escapeJson(cartLineItemSelector));
        templateData.put("cartLineItemPerQuantityPriceSelector", StringEscapeUtils.escapeJson(cartLineItemPerQuantityPriceSelector));
        templateData.put("cartLineItemTotalPriceSelector", StringEscapeUtils.escapeJson(cartLineItemTotalPriceSelector));
        templateData.put("cartLineItemSellingPlanNameSelector", StringEscapeUtils.escapeJson(cartLineItemSellingPlanNameSelector));
        templateData.put("cartSubTotalSelector", StringEscapeUtils.escapeJson(cartSubTotalSelector));
        templateData.put("cartLineItemPriceSelector", StringEscapeUtils.escapeJson(cartLineItemPriceSelector));

        Optional<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettings = subscriptionWidgetSettingsService.findByShop(shopName);
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shopName).get();

        templateData.put("oneTimePurchaseText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getOneTimePurchaseText).orElse("")));
        templateData.put("deliveryText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getDeliveryText).orElse("")));
        templateData.put("purchaseOptionsText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getPurchaseOptionsText).orElse("")));
        templateData.put("subscriptionOptionText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSubscriptionOptionText).orElse("")));
        templateData.put("sellingPlanSelectTitle", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSellingPlanSelectTitle).orElse("")));
        templateData.put("tooltipTitle", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getTooltipTitle).orElse("")));
        templateData.put("apiKey", StringEscapeUtils.escapeJson(""));
        templateData.put("sellingPlanTitleText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSellingPlanTitleText).orElse("")));
        templateData.put("oneTimePriceText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getOneTimePriceText).orElse("")));
        templateData.put("selectedPayAsYouGoSellingPlanPriceText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSelectedPayAsYouGoSellingPlanPriceText).orElse("")));
        templateData.put("selectedPrepaidSellingPlanPriceText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSelectedPrepaidSellingPlanPriceText).orElse("")));
        templateData.put("selectedDiscountFormat", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getSelectedDiscountFormat).orElse("")));
        templateData.put("showTooltipOnClick", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowTooltipOnClick).orElse(Boolean.FALSE).toString()));
        templateData.put("widgetEnabledOnSoldVariant", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isWidgetEnabledOnSoldVariant).orElse(Boolean.FALSE).toString()));
        templateData.put("switchRadioButtonWidget", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isSwitchRadioButtonWidget).orElse(Boolean.FALSE).toString()));
        templateData.put("subscriptionOptionSelectedByDefault", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isSubscriptionOptionSelectedByDefault).orElse(Boolean.FALSE).toString()));
        templateData.put("widgetEnabled", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isWidgetEnabled).orElse(Boolean.TRUE).toString()));
        templateData.put("showTooltip", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowTooltip).orElse(Boolean.TRUE).toString()));
        templateData.put("showCheckoutSubscriptionBtn", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowCheckoutSubscriptionBtn).orElse(Boolean.FALSE).toString()));
        templateData.put("tooltipDesctiption", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getTooltipDesctiption).orElse("<strong>Seamless subscription your way</strong><br>Skip, reschedule, edit, or cancel deliveries anytime, based on your needs.")));
        templateData.put("orderStatusManageSubscriptionTitle", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getOrderStatusManageSubscriptionTitle).orElse("Subscription")));
        templateData.put("orderStatusManageSubscriptionDescription", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getOrderStatusManageSubscriptionDescription).orElse("Continue to your account to view and manage your subscriptions.")));
        templateData.put("orderStatusManageSubscriptionButtonText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getOrderStatusManageSubscriptionButtonText).orElse("Manage your subscription")));
        String manageSubscription = StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getManageSubscriptionBtnFormat).orElse("<a href='" + shopInfoUtils.getManageSubscriptionUrl(shopName) + "' class='appstle_manageSubBtn' ><button class='btn' style='padding: 2px 20px'>Manage Subscription</button><a><br><br>"));
        templateData.put("manageSubscriptionBtnFormat", manageSubscription.replace("Manage Subscription", customerPortalSettingsDTO.getManageSubscriptionButtonText()));
        templateData.put("tooltipDescriptionOnPrepaidPlan", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getTooltipDescriptionOnPrepaidPlan).orElse("<b>Prepaid Plan Details</b></br> Total you are charged for {{totalPrice}} ( At every order {{pricePerDelivery}})")));
        templateData.put("tooltipDescriptionOnMultipleDiscount", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getTooltipDescriptionOnMultipleDiscount).orElse("<b>Discount Details</b></br> Initial Discount is {{discountOne}} and then {{discountTwo}}")));
        templateData.put("tooltipDescriptionCustomization", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getTooltipDescriptionCustomization).orElse("")));
        templateData.put("formMappingAttributeName", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getFormMappingAttributeName).orElse("")));
        templateData.put("formMappingAttributeSelector", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getFormMappingAttributeSelector).orElse("")));
        templateData.put("quickViewModalPollingSelector", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getQuickViewModalPollingSelector).orElse("")));
        templateData.put("updatePriceOnQuantityChange", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getUpdatePriceOnQuantityChange).orElse("")));
        templateData.put("widgetParentSelector", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getWidgetParentSelector).orElse("")));
        templateData.put("quantitySelector", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getQuantitySelector).orElse("")));
        templateData.put("loyaltyDetailsLabelText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getLoyaltyDetailsLabelText).orElse("")));
        templateData.put("loyaltyPerkDescriptionText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::getLoyaltyPerkDescriptionText).orElse("")));

        templateData.put("enableCartWidgetFeature", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isEnableCartWidgetFeature).orElse(false).toString()));
        templateData.put("showStaticTooltip", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowStaticTooltip).orElse(false).toString()));
        templateData.put("showAppstleLink", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowAppstleLink).orElse(false).toString()));
        templateData.put("sortByDefaultSequence", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isSortByDefaultSequence).orElse(false).toString()));
        templateData.put("showSubOptionBeforeOneTime", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isShowSubOptionBeforeOneTime).orElse(false).toString()));
        templateData.put("detectVariantFromURLParams", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isDetectVariantFromURLParams).orElse(false).toString()));
        templateData.put("manageSubscriptionUrl", StringEscapeUtils.escapeJson(shopInfoUtils.getManageSubscriptionUrl(shopName)));

        templateData.put("subscriptionWidgetMarginTop", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionWidgetMarginTop()).map(mt -> mt + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("subscriptionWidgetMarginBottom", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionWidgetMarginBottom()).map(mb -> mb + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("subscriptionWrapperBorderWidth", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionWrapperBorderWidth()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("subscriptionWrapperBorderColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionWrapperBorderColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("circleBorderColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getCircleBorderColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("dotBackgroundColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getDotBackgroundColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));


        templateData.put("selectPaddingTop", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectPaddingTop()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectPaddingBottom", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectPaddingBottom()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectPaddingLeft", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectPaddingLeft()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectPaddingRight", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectPaddingRight()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("selectBorderWidth", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectBorderWidth()).map(p -> p + "px").orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectBorderStyle", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectBorderStyle()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectBorderColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectBorderColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("selectBorderRadius", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSelectBorderRadius()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("tooltipSubscriptionSvgFill", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getTooltipSubscriptionSvgFill()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("tooltipColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getTooltipColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("tooltipBackgroundColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getTooltipBackgroundColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("tooltipBorderTopColorBorderTopColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getTooltipBorderTopColorBorderTopColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("subscriptionFinalPriceColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionFinalPriceColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("subscriptionWidgetTextColor", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionWidgetTextColor()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));
        templateData.put("subscriptionPriceDisplayText", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(subscriptionWidgetSettingsDTO -> Optional.ofNullable(subscriptionWidgetSettingsDTO.getSubscriptionPriceDisplayText()).orElse(StringUtils.EMPTY)).orElse(StringUtils.EMPTY)));

        templateData.put("disableQueryParamsUpdate", StringEscapeUtils.escapeJson(subscriptionWidgetSettings.map(SubscriptionWidgetSettingsDTO::isDisableQueryParamsUpdate).orElse(false).toString()));

        SubscriptionCustomCssDTO subscriptionCustomCss = subscriptionCustomCssService.findByShop(shopName).get();

        templateData.put("customCss", StringUtils.isNotBlank(subscriptionCustomCss.getCustomCss()) ? StringEscapeUtils.escapeJson(subscriptionCustomCss.getCustomCss()) : "");
        templateData.put("customerPortalCss", StringUtils.isNotBlank(subscriptionCustomCss.getCustomerPoratlCSS()) ? StringEscapeUtils.escapeJson(subscriptionCustomCss.getCustomerPoratlCSS()) : "");
        templateData.put("elementCSS" ,   StringEscapeUtils.escapeJson(gson.toJson(shopCustomizationService.getShopCustomizationCss(shopName, CustomizationCategory.PRODUCT_PAGE_WIDGET))));

        templateData.put("useUrlWithCustomerId", Optional.ofNullable(customerPortalSettingsDTO.isUseUrlWithCustomerId()).orElse(false).toString());
        templateData.put("cancelSubscriptionBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelSubscriptionBtnText()));
        templateData.put("nextOrderText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getNextOrderText()));
        templateData.put("noSubscriptionMessage", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getNoSubscriptionMessage()));
        templateData.put("subscriptionNoText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getSubscriptionNoText()));
        templateData.put("orderFrequencyText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getOrderFrequencyText()));
        templateData.put("statusText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getStatusText()));
        templateData.put("totalProductsText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getTotalProductsText()));
        templateData.put("updatePaymentMessage", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpdatePaymentMessage()));
        templateData.put("cardLastFourDigitText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCardLastFourDigitText()));
        templateData.put("cardExpiryText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCardExpiryText()));
        templateData.put("cardHolderNameText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCardHolderNameText()));
        templateData.put("cardTypeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCardTypeText()));
        templateData.put("paymentMethodTypeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getPaymentMethodTypeText()));
        templateData.put("cancelAccordionTitle", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelAccordionTitle()));
        templateData.put("paymentDetailAccordionTitle", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getPaymentDetailAccordionTitle()));
        templateData.put("upcomingOrderAccordionTitle", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpcomingOrderAccordionTitle()));
        templateData.put("paymentInfoText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getPaymentInfoText()));
        templateData.put("updatePaymentBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpdatePaymentBtnText()));
        templateData.put("nextOrderDateLbl", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getNextOrderDateLbl()));
        templateData.put("statusLbl", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getStatusLbl()));
        templateData.put("quantityLbl", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getQuantityLbl()));
        templateData.put("amountLbl", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getAmountLbl()));
        templateData.put("orderNoLbl", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getOrderNoLbl()));
        templateData.put("editFrequencyBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getEditFrequencyBtnText()));
        templateData.put("cancelFreqBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelFreqBtnText()));
        templateData.put("updateFreqBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpdateFreqBtnText()));
        templateData.put("pauseResumeSubscriptionFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isPauseResumeSub()).orElse(Boolean.FALSE).toString()));
        templateData.put("changeNextOrderDateFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isChangeNextOrderDate()).orElse(Boolean.FALSE).toString()));
        templateData.put("cancelSubscriptionFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isCancelSub()).orElse(Boolean.FALSE).toString()));
        templateData.put("changeOrderFrequencyFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isChangeOrderFrequency()).orElse(Boolean.FALSE).toString()));
        templateData.put("createAdditionalOrderFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isCreateAdditionalOrder()).orElse(Boolean.FALSE).toString()));
        templateData.put("orderNowFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isAllowOrderNow()).orElse(Boolean.FALSE).toString()));
        templateData.put("manageSubscriptionButtonText", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.getManageSubscriptionButtonText()).orElse("Manage Subscription")));
        templateData.put("editChangeOrderBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getEditChangeOrderBtnText()));
        templateData.put("cancelChangeOrderBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelChangeOrderBtnText()));
        templateData.put("updateChangeOrderBtnText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpdateChangeOrderBtnText()));
        templateData.put("editProductButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getEditProductButtonText()));
        templateData.put("deleteButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getDeleteButtonText()));
        templateData.put("updateButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpdateButtonText()));
        templateData.put("cancelButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelButtonText()));
        templateData.put("addProductButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getAddProductButtonText()));
        templateData.put("addProductLabelText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getAddProductLabelText()));
        templateData.put("activeBadgeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getActiveBadgeText()));
        templateData.put("closedBadgeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCloseBadgeText()));
        templateData.put("skipOrderButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getSkipOrderButtonText()));
        templateData.put("productLabelText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getProductLabelText()));
        templateData.put("seeMoreDetailsText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getSeeMoreDetailsText()));
        templateData.put("hideDetailsText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getHideDetailsText()));
        templateData.put("productInSubscriptionText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getProductInSubscriptionText()));
        templateData.put("EditQuantityLabelText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getEditQuantityLabelText()));
        templateData.put("subTotalLabelText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getSubTotalLabelText()));
        templateData.put("paymentNotificationText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getPaymentNotificationText()));
        templateData.put("editProductFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isEditProductFlag()).orElse(Boolean.TRUE).toString()));
        templateData.put("deleteProductFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isDeleteProductFlag()).orElse(Boolean.TRUE).toString()));
        templateData.put("showShipment", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isShowShipment()).orElse(Boolean.TRUE).toString()));
        templateData.put("addAdditionalProductFlag", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.isAddAdditionalProduct()).orElse(Boolean.TRUE).toString()));
        templateData.put("minQtyToAllowDuringAddProduct", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.getMinQtyToAllowDuringAddProduct()).orElse(1).toString()));
        templateData.put("successText", StringEscapeUtils.escapeJson(Optional.ofNullable(customerPortalSettingsDTO.getSuccessText()).orElse(StringUtils.EMPTY)));
        templateData.put("cancelSubscriptionConfirmPrepaidText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelSubscriptionConfirmPrepaidText()));
        templateData.put("cancelSubscriptionConfirmPayAsYouGoText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelSubscriptionConfirmPayAsYouGoText()));
        templateData.put("cancelSubscriptionPrepaidButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelSubscriptionPrepaidButtonText()));
        templateData.put("cancelSubscriptionPayAsYouGoButtonText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCancelSubscriptionPayAsYouGoButtonText()));
        templateData.put("upcomingFulfillmentText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getUpcomingFulfillmentText()));
        templateData.put("creditCardText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getCreditCardText()));
        templateData.put("endingWithText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getEndingWithText()));
        templateData.put("weekText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getWeekText()));
        templateData.put("dayText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getDayText()));
        templateData.put("monthText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getMonthText()));
        templateData.put("yearText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getYearText()));
        templateData.put("skipBadgeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getSkipBadgeText()));
        templateData.put("queueBadgeText", StringEscapeUtils.escapeJson(customerPortalSettingsDTO.getQueueBadgeText()));
        templateData.put("totalPricePerDeliveryText", StringEscapeUtils.escapeJson(Optional.ofNullable(subscriptionWidgetSettings.get().getTotalPricePerDeliveryText()).orElse("")));

        CustomerPortalSettingsV2DTO customerPortalSettingsV2DTO = new CustomerPortalSettingsV2DTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            customerPortalSettingsV2DTO = objectMapper.readValue(customerPortalSettingsDTO.getCustomerPortalSettingJson(), CustomerPortalSettingsV2DTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        templateData.put("shippingLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getShippingLabelText()).orElse("Product Shipping Info"));
        templateData.put("failureText", Optional.ofNullable(customerPortalSettingsV2DTO.getFailureText()).orElse("Failure"));
        templateData.put("sendEmailText", Optional.ofNullable(customerPortalSettingsV2DTO.getSendEmailText()).orElse("Send Email"));
        templateData.put("chooseDifferentProductText", Optional.ofNullable(customerPortalSettingsV2DTO.getChooseDifferentProductText()).orElse("Choose different product"));
        templateData.put("chooseDifferentProductActionText", Optional.ofNullable(customerPortalSettingsV2DTO.getChooseDifferentProductActionText()).orElse("Choose different product Action"));
        templateData.put("confirmSkipFulfillmentBtnText", Optional.ofNullable(customerPortalSettingsV2DTO.getConfirmSkipFulfillmentBtnText()).orElse("Confirm Skip Fulfillment"));
        templateData.put("confirmSkipOrder", Optional.ofNullable(customerPortalSettingsV2DTO.getConfirmSkipOrder()).orElse("Confirm Skip Order"));
        templateData.put("skipFulfillmentButtonText", Optional.ofNullable(customerPortalSettingsV2DTO.getSkipFulfillmentButtonText()).orElse("Skip Fulfillment"));
        templateData.put("confirmCommonText", Optional.ofNullable(customerPortalSettingsV2DTO.getConfirmCommonText()).orElse("Confirm"));
        templateData.put("orderNowdescriptionText", Optional.ofNullable(customerPortalSettingsV2DTO.getOrderNowDescriptionText()).orElse("Please note, once you confirm Order Now. The Subscription details will be updated within a minute or you may refresh the page after sometime. It is requested that multiple attempts of Order Now should not be placed"));
        templateData.put("discountDetailsTitleText", Optional.ofNullable(customerPortalSettingsV2DTO.getDiscountDetailsTitleText()).orElse("Discount Details"));
        templateData.put("emailAddressText", Optional.ofNullable(customerPortalSettingsV2DTO.getEmailAddressText()).orElse("Email address"));
        templateData.put("emailMagicLinkText", Optional.ofNullable(customerPortalSettingsV2DTO.getEmailMagicLinkText()).orElse("Email Magic Link"));
        templateData.put("retriveMagicLinkText", Optional.ofNullable(customerPortalSettingsV2DTO.getRetriveMagicLinkText()).orElse("Retrieve Magic Link"));
        templateData.put("validEmailMessage", Optional.ofNullable(customerPortalSettingsV2DTO.getValidEmailMessage()).orElse("Please enter valid email"));
        templateData.put("saveButtonText", Optional.ofNullable(customerPortalSettingsV2DTO.getSaveButtonText()).orElse("Save"));
        templateData.put("orderDateText", Optional.ofNullable(customerPortalSettingsV2DTO.getOrderDateText()).orElse("Order Date"));
        templateData.put("address1LabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getAddress1LabelText()).orElse("Address Line 1"));
        templateData.put("address2LabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getAddress2LabelText()).orElse("Address Line 2"));
        templateData.put("companyLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getCompanyLabelText()).orElse("Company"));
        templateData.put("cityLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getCityLabelText()).orElse("City"));
        templateData.put("countryLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getCountryLabelText()).orElse("Country"));
        templateData.put("firstNameLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getFirstNameLabelText()).orElse("First Name"));
        templateData.put("lastNameLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getLastNameLabelText()).orElse("Last Name"));
        templateData.put("phoneLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getPhoneLabelText()).orElse("Phone Number"));
        templateData.put("provinceLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getProvinceLabelText()).orElse("Province/State"));
        templateData.put("zipLabelText", Optional.ofNullable(customerPortalSettingsV2DTO.getZipLabelText()).orElse("Zip"));
        templateData.put("addressHeaderTitleText", Optional.ofNullable(customerPortalSettingsV2DTO.getAddressHeaderTitleText()).orElse("Shipping Address"));
        templateData.put("changeShippingAddressFlag", Optional.ofNullable(customerPortalSettingsV2DTO.getChangeShippingAddressFlag()).orElse(Boolean.TRUE).toString());
        templateData.put("updateEditShippingButtonText", Optional.ofNullable(customerPortalSettingsV2DTO.getUpdateEditShippingButtonText()).orElse("Update"));
        templateData.put("cancelEditShippingButtonText", Optional.ofNullable(customerPortalSettingsV2DTO.getCancelEditShippingButtonText()).orElse("Cancel"));
        templateData.put("pauseSubscriptionText", Optional.ofNullable(customerPortalSettingsV2DTO.getPauseSubscriptionText()).orElse("Pause Subscription"));
        templateData.put("resumeSubscriptionText", Optional.ofNullable(customerPortalSettingsV2DTO.getResumeSubscriptionText()).orElse("Resume Subscription"));
        templateData.put("pauseBadgeText", Optional.ofNullable(customerPortalSettingsV2DTO.getPauseBadgeText()).orElse("PAUSED"));

        // Add labels
        Map<String, String> labelMap = getShopLabels(shopName);
        templateData.put("labels", StringEscapeUtils.escapeJson(OBJECT_MAPPER.writeValueAsString(labelMap)));

        return templateData;
    }

    private Map<String, String> getShopLabels(String shop) {
        Map<String, LabelValueInfo> labels = new LinkedHashMap<>();

        Optional<ShopLabelDTO> shopLabelDTO = shopLabelService.findFirstByShop(shop);

        if (shopLabelDTO.isPresent()) {
            labels = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
            }, shopLabelDTO.get().getLabels());
        }

        Map<String, String> widget = labels.entrySet().stream().filter(e -> e.getValue().getGroups().contains("WIDGET")).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));

        return widget;
    }

    private Map<String, Object> buildBundleTemplateData(String shopName, BundleSettingDTO bundleSettingDTO, List<BundleRuleDTO> bundleRuleDTOList) throws JsonProcessingException {
        Map<String, Object> bundleTemplateData = new HashMap<>();

        bundleTemplateData.put("bundleRules", OBJECT_MAPPER.writeValueAsString(bundleRuleDTOList));
        bundleTemplateData.put("showOnProductPage", Optional.ofNullable(bundleSettingDTO.isShowOnProductPage()).orElse(Boolean.FALSE).toString());
        bundleTemplateData.put("showMultipleOnProductPage", Optional.ofNullable(bundleSettingDTO.isShowMultipleOnProductPage()).orElse(Boolean.FALSE).toString());
        bundleTemplateData.put("showProductPrice", Optional.ofNullable(bundleSettingDTO.isShowProductPrice()).orElse(Boolean.FALSE).toString());
        bundleTemplateData.put("oneTimeDiscount", Optional.ofNullable(bundleSettingDTO.isOneTimeDiscount()).orElse(Boolean.FALSE).toString());
        bundleTemplateData.put("showDiscountInCart", Optional.ofNullable(bundleSettingDTO.isShowDiscountInCart()).orElse(Boolean.FALSE).toString());
        bundleTemplateData.put("redirectTo", Optional.ofNullable(bundleSettingDTO.getRedirectTo()).orElse(RedirectOption.CART).toString());
        bundleTemplateData.put("actionButtonColor", Optional.ofNullable(bundleSettingDTO.getActionButtonColor()).orElse(""));
        bundleTemplateData.put("actionButtonFontColor", Optional.ofNullable(bundleSettingDTO.getActionButtonFontColor()).orElse(""));
        bundleTemplateData.put("productTitleColor", Optional.ofNullable(bundleSettingDTO.getProductTitleColor()).orElse(""));
        bundleTemplateData.put("productPriceColor", Optional.ofNullable(bundleSettingDTO.getProductPriceColor()).orElse(""));
        bundleTemplateData.put("selector", Optional.ofNullable(bundleSettingDTO.getSelector()).orElse(""));
        bundleTemplateData.put("placement", Optional.ofNullable(bundleSettingDTO.getPlacement()).orElse(PlacementPosition.AFTER));

        bundleTemplateData.put("customCss", Optional.ofNullable(bundleSettingDTO.getCustomCss()).orElse(""));
        bundleTemplateData.put("elementCSS" ,  gson.toJson(shopCustomizationService.getShopCustomizationCss(shopName, CustomizationCategory.BUNDLING)));
        bundleTemplateData.put("variant", Optional.ofNullable(bundleSettingDTO.getVariant()).orElse(""));
        bundleTemplateData.put("deliveryFrequency", Optional.ofNullable(bundleSettingDTO.getDeliveryFrequency()).orElse(""));
        bundleTemplateData.put("perDelivery", Optional.ofNullable(bundleSettingDTO.getPerDelivery()).orElse(""));
        bundleTemplateData.put("discountPopupHeader", Optional.ofNullable(bundleSettingDTO.getDiscountPopupHeader()).orElse(""));
        bundleTemplateData.put("discountPopupAmount", Optional.ofNullable(bundleSettingDTO.getDiscountPopupAmount()).orElse(""));
        bundleTemplateData.put("discountPopupCheckoutMessage", Optional.ofNullable(bundleSettingDTO.getDiscountPopupCheckoutMessage()).orElse(""));
        bundleTemplateData.put("discountPopupBuy", Optional.ofNullable(bundleSettingDTO.getDiscountPopupBuy()).orElse(""));
        bundleTemplateData.put("discountPopupNo", Optional.ofNullable(bundleSettingDTO.getDiscountPopupNo()).orElse(""));
        bundleTemplateData.put("showDiscountPopup", Optional.ofNullable(bundleSettingDTO.isShowDiscountPopup()).orElse(false));

        return bundleTemplateData;
    }

    private Map<String, Object> buildAppstleMenuSettingsTemplateData(AppstleMenuSettingsDTO appstleMenuSettingsDTO) throws JsonProcessingException {
        Map<String, Object> appstleMenuSettingsTemplateData = new HashMap<>();
        appstleMenuSettingsTemplateData.put("filterMenu", StringEscapeUtils.escapeJson(appstleMenuSettingsDTO.getFilterMenu()));
        appstleMenuSettingsTemplateData.put("menuUrl", StringEscapeUtils.escapeJson(appstleMenuSettingsDTO.getMenuUrl()));
        appstleMenuSettingsTemplateData.put("menuStyle", StringEscapeUtils.escapeJson(appstleMenuSettingsDTO.getMenuUrl()));

        return appstleMenuSettingsTemplateData;
    }

    public String writeSubscriptionFileToThemeAsset(ShopifyAPI api, String fileContent, String filename, GetThemesResponse.BasicThemeInfo publishedTheme) {
        log.info("uploading js file {} to Shop's Theme Asset", filename);
        UpdateAssetRequest updateAssetRequest = new UpdateAssetRequest();
        Asset asset = new Asset();
        asset.setKey("assets/" + filename);
        asset.setValue(fileContent);
        updateAssetRequest.setAsset(asset);
        UpdateAssetResponse updateAssetResponse = api.updateAsset(publishedTheme.getId(), updateAssetRequest);
        return updateAssetResponse.getAsset().getPublic_url();
    }

    private void generateScriptTags(ShopifyAPI api, ThemeSettings themeSettings, String... scriptUrls) {
        try {

            deleteExistingScriptTags(api);

            if (WHITELISTED_SHOP_NAMES.contains(api.getShopName())) {
                return;
            }

            for (String scriptUrl : scriptUrls) {
                CreateScriptTagRequest createScriptTagRequest = new CreateScriptTagRequest();
                CreateScriptTagRequest.ScriptTagRequest scriptTag = new CreateScriptTagRequest.ScriptTagRequest();
                scriptTag.setSrc(scriptUrl);
                scriptTag.setEvent("onload");
                createScriptTagRequest.setScript_tag(scriptTag);
                CreateScriptTagResponse scriptTagResponse = api.createScriptTag(createScriptTagRequest);
                String a = "b";
            }
        } catch (Exception ex) {
            log.error("An error occurred while creating scriptTag. shop=" + api.getShopName() + " ex=" + ExceptionUtils.getStackTrace(ex), ex);
        }
    }

    @Autowired
    private AmazonS3 amazonS3;

    private PutObjectRequest buildPutRequestObject(String key, ObjectMetadata metadata, String subscribeItBucket, byte[] compressed) {
        InputStream input = new ByteArrayInputStream(compressed);
        PutObjectRequest putObjectRequest = new PutObjectRequest(subscribeItBucket, key, input, metadata);

        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        putObjectRequest.withAccessControlList(accessControlList);
        return putObjectRequest;
    }

    private void deleteExistingScriptTags(ShopifyAPI api) {
        GetScriptTagsResponse scriptTags = api.getScriptTags();

        for (ScriptTag tag : scriptTags.getScript_tags()) {
            api.deleteScriptTag(tag.getId());
        }
    }

    public void writeSubscriptionHelperToTheme(ShopifyAPI api, Map<String, Object> templateData, GetThemesResponse.BasicThemeInfo publishedTheme) {
        try {
            String fileContent = IOUtils.toString(appstleSubscriptionHelperV1.getInputStream());

            UpdateAssetRequest updateAssetRequest = new UpdateAssetRequest();
            Asset asset = new Asset();
            asset.setKey("snippets/appstle-subscription-helper.liquid");
            asset.setValue(fileContent);
            updateAssetRequest.setAsset(asset);
            api.updateAsset(publishedTheme.getId(), updateAssetRequest);

            updateLayoutTheme(api, publishedTheme);

        } catch (Exception ex) {
            log.error("An error occurred while creating snippets/appstle-subscription-helper.liquid. shop=" + api.getShopName(),
                ex);
        }
    }

    private void createBuildABoxPagePage(ShopifyAPI api, String shop) throws IOException {
        try {
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("product_list_header", "Build A Box");
            Long buildABoxPageId = null;


            String existingPageContent = null;

            List<Page> pages = api.getPages().getPages();
            for (Page page : pages) {
                if (page.getHandle().equals("build-a-box")) {
                    buildABoxPageId = page.getId();
                    existingPageContent = page.getBodyHtml();
                    break;
                }
            }

            if (existingPageContent != null && existingPageContent.contains("appstle-subscription-build-a-box-v1")) {
                return;
            }

            existingPageContent = "<div id='appstle-subscription-build-a-box-widget'> {% render 'appstle-subscription-build-a-box-v1' %} </div>";

            if (buildABoxPageId == null) {
                CreatePageRequest.Page page = new CreatePageRequest.Page(templateData.get("product_list_header").toString(), existingPageContent, true);//TODO: need to add handle in request
                CreatePageRequest createPageRequest = new CreatePageRequest(page);
                CreatePageResponse createPageResponse = api.createPage(createPageRequest);
            } else {

                UpdatePageRequest updatePageRequest = new UpdatePageRequest();
                Page page = new Page();
                page.setId(buildABoxPageId);
                page.setHandle("build-a-box");
                page.setTitle(templateData.get("product_list_header").toString());
                page.setBodyHtml(existingPageContent);
                updatePageRequest.setPage(page);
                UpdatePageResponse updatePageResponse = api.updatePage(updatePageRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLayoutTheme(ShopifyAPI api, GetThemesResponse.BasicThemeInfo theme) {
        GetAssetResponse layoutTheme = api.getAsset("layout/theme.liquid", theme.getId());
        String value = layoutTheme.getAsset().getValue();

        int index = value.lastIndexOf("</body>");
        if (index == -1) {
            return;
        }

        if (value.contains("appstle-subscription-helper")) {
            return;
        }

        String updatedValue = new StringBuilder(value).insert(index, "{% include 'appstle-subscription-helper' %}").toString();

        UpdateAssetRequest updateAssetRequest = new UpdateAssetRequest();
        Asset updateAsset = new Asset();
        updateAsset.setKey("layout/theme.liquid");
        updateAsset.setValue(updatedValue);

        updateAssetRequest.setAsset(updateAsset);

        api.updateAsset(theme.getId(), updateAssetRequest);
    }

    private void removeHelperSnippetFromTheme(ShopifyAPI api, GetThemesResponse.BasicThemeInfo theme) {
        GetAssetResponse layoutTheme = api.getAsset("layout/theme.liquid", theme.getId());
        String value = layoutTheme.getAsset().getValue();


        if (!value.contains("appstle-subscription-helper")) {
            return;
        }

        String updatedValue = value.replace("{% include 'appstle-subscription-helper' %}", "");

        UpdateAssetRequest updateAssetRequest = new UpdateAssetRequest();
        Asset updateAsset = new Asset();
        updateAsset.setKey("layout/theme.liquid");
        updateAsset.setValue(updatedValue);

        updateAssetRequest.setAsset(updateAsset);

        api.updateAsset(theme.getId(), updateAssetRequest);
    }

    @Async
    public void createOrUpdateAppstleMenuPageAsync(String shopName) {
        try {
            ShopifyAPI api = commonUtils.prepareShopifyResClient(shopName);
            Map<String, Object> templateData = new HashMap<>();

            Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByShop(shopName);
            Optional<AppstleMenuSettingsDTO> appstleMenuSettingsOptional = appstleMenuSettingsService.findByShop(shopName);
            if (shopInfoOptional.isPresent() && appstleMenuSettingsOptional.isPresent()) {
                ShopInfoDTO shopInfoDTO = shopInfoOptional.get();
                AppstleMenuSettingsDTO appstleMenuSettings = appstleMenuSettingsOptional.get();
                templateData.put("appstleMenuSettings", OBJECT_MAPPER.writeValueAsString(Optional.of(appstleMenuSettings).orElse(new AppstleMenuSettingsDTO())));
                templateData.put("storeFrontAccessKey", Optional.ofNullable(shopInfoDTO.getStoreFrontAccessToken()).orElse(""));
                createAppstleMenuPage(api, shopName, templateData, appstleMenuSettings);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void createAppstleMenuPage(ShopifyAPI api, String shop, Map<String, Object> templateData, AppstleMenuSettingsDTO appstleMenuSettingsDTO) {
        try {
            Long appstleMenuPageId = null;
            String pageContent = null;
            List<Page> pages = api.getPages().getPages();
            for (Page page : pages) {
                if (page.getHandle().equals("appstle-menu") || page.getHandle().equals(appstleMenuSettingsDTO.getHandle())) {
                    appstleMenuPageId = page.getId();
                    break;
                }
            }
            if (appstleMenuSettingsDTO.isActive()) {

                Optional<AppstleMenuLabelsDTO> appstleMenuLabelsOptional = appstleMenuLabelsService.findOneByShop(shop);
                templateData.put("appstleMenuLabels", OBJECT_MAPPER.writeValueAsString(appstleMenuLabelsOptional.orElse(new AppstleMenuLabelsDTO())));

                String appstleMenuJs = appendVersionParams(JS_APPSTLE_MENU, shop, LATEST_BUILD_TIME);
                templateData.put("appstleMenuJavascript", appstleMenuJs);

                String appstleMenuCss = appendVersionParams(CSS_APPSTLE_MENU, shop, LATEST_BUILD_TIME);
                templateData.put("appstleMenuCss", appstleMenuCss);

                pageContent = ftlUtils.generateContentFor(shop, templateData, "appstle-menu.ftl");
                if (appstleMenuPageId == null) {
                    CreatePageRequest.Page page = new CreatePageRequest.Page("Appstle Menu", pageContent, true);
                    CreatePageRequest createPageRequest = new CreatePageRequest(page);
                    CreatePageResponse result = api.createPage(createPageRequest);

                    if (appstleMenuSettingsDTO.getHandle() != null) {
                        updatePage(api, result.getPage().getId(), pageContent, appstleMenuSettingsDTO.getHandle());
                    }
                } else {
                    updatePage(api, appstleMenuPageId, pageContent, appstleMenuSettingsDTO.getHandle());
                }
            } else if (appstleMenuPageId != null) {
                api.deletePage(appstleMenuPageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePage(ShopifyAPI api, Long appstleMenuPageId, String pageContent, String handle) {
        UpdatePageRequest updatePageRequest = new UpdatePageRequest();
        Page page = new Page();
        page.setId(appstleMenuPageId);
        page.setHandle(handle != null ? handle : "appstle-menu");
        page.setTitle("Appstle Menu");
        page.setBodyHtml(pageContent);
        updatePageRequest.setPage(page);
        api.updatePage(updatePageRequest);
    }

    public List<String> getWhitelistedShopNames() {
        return HARD_WHITELISTED_SHOP_NAMES;
    }

}

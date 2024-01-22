package com.et.utils;

import com.amazonaws.util.IOUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.shop.Shop;
import com.et.api.utils.CurrencyFormattingUtils;
import com.et.constant.AppstleAttribute;
import com.et.constant.Constants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.liquid.*;
import com.et.pojo.OrderItem;
import com.et.pojo.SubscriptionProductInfo;
import com.et.repository.*;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.KlaviyoService;
import com.et.service.MailChimpService;
import com.et.service.MailgunService;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.web.rest.vm.AttributeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import com.shopify.java.graphql.client.type.SellingPlanInterval;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.et.service.impl.EmailTemplateSettingServiceImpl.PAY_AS_YOU_GO;
import static com.et.service.impl.EmailTemplateSettingServiceImpl.PREPAID;
import static java.util.Objects.requireNonNull;

@Component
public class CommonEmailUtils {

    @Autowired
    private LiquidUtils liquidUtils;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    private final Logger log = LoggerFactory.getLogger(CommonEmailUtils.class);

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private KlaviyoService klaviyoService;

    @Autowired
    private MailChimpService mailChimpService;

    @Autowired
    private PaymentPlanRepository paymentPlanRepository;

    @Value("classpath:base-mail-template.html")
    private Resource baseMailTemplate;

    @Autowired
    private SmsTemplateSettingRepository smsTemplateSettingRepository;

    @Autowired
    private CommonUtils commonUtils;

    public static final String NEXT_ACTION_URL = "nextActionUrl";

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Async
    public void sendSubscriptionUpdateEmail(
        ShopifyGraphqlClient shopifyGraphqlClient,
        ShopifyAPI api,
        SubscriptionContractDetails subscriptionContractDetails,
        EmailSettingType emailSettingType) throws Exception {
        sendSubscriptionUpdateEmail(shopifyGraphqlClient, api, subscriptionContractDetails, emailSettingType, null, null, true);
    }

    @Async
    public void sendSubscriptionUpdateEmail(
        ShopifyGraphqlClient shopifyGraphqlClient,
        ShopifyAPI api,
        SubscriptionContractDetails subscriptionContractDetails,
        EmailSettingType emailSettingType,
        Map<String, Object> additionalAttributes) throws Exception {
        sendSubscriptionUpdateEmail(shopifyGraphqlClient, api, subscriptionContractDetails, emailSettingType, additionalAttributes, null, true);
    }

    public void sendSubscriptionUpdateEmail(
        ShopifyGraphqlClient shopifyGraphqlClient,
        ShopifyAPI api,
        SubscriptionContractDetails subscriptionContractDetails,
        EmailSettingType emailSettingType,
        Map<String, Object> additionalAttributes,
        List<OrderItem> orderItems,
        boolean addActivityLog) throws Exception {

        String shop = subscriptionContractDetails.getShop();
        Shop shopDetails = api.getShopInfo().getShop();

        log.info("Start sending email for shop:{}, emailSettingType:{}, contractId:{}", shop, emailSettingType, subscriptionContractDetails.getSubscriptionContractId());

        EmailTemplateSetting emailTemplateSetting = emailTemplateSettingRepository.findByShopAndEmailSettingType(shop, emailSettingType).get();

        if (emailTemplateSetting.isSendEmailDisabled()) {
            log.info("Send email disabled for shop: {}, email type:{}", shop, emailSettingType);
            return;
        }

        Optional<CustomerPayment> customerPaymentOptional = customerPaymentRepository.findTop1ByCustomerId(subscriptionContractDetails.getCustomerId());

        SubjectModel subjectModel = new SubjectModel(shopDetails);
        String subject = liquidUtils.getValue(subjectModel, emailTemplateSetting.getSubject());

        Customer customer = new Customer(subscriptionContractDetails.getCustomerName(), subscriptionContractDetails.getCustomerFirstName(), subscriptionContractDetails.getCustomerLastName(), customerPaymentOptional.map(CustomerPayment::getCustomerUid).orElse(null), subscriptionContractDetails.getCustomerEmail());

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
        EmailModel model = buildEmailModel(shopifyGraphqlClient, shopDetails, emailTemplateSetting, subscriptionContractDetails.getGraphSubscriptionContractId(), shopInfo, customer, subscriptionContractDetails.getSubscriptionContractId(), additionalAttributes, orderItems, subscriptionContractDetails.getCancellationFeedback(), subscriptionContractDetails.getCancellationNote(), subscriptionContractDetails.getOrderName());

        CustomerPayment customerPayment = null;

        if (customerPaymentOptional.isPresent()) {
            customerPayment = customerPaymentOptional.get();
        } else {
            customerPayment = new CustomerPayment();
            customerPayment.setShop(shop);
            customerPayment.setCustomerId(subscriptionContractDetails.getCustomerId());
            customerPayment.setAdminGraphqlApiCustomerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + subscriptionContractDetails.getCustomerId());
            customerPayment.setCustomerUid(CommonUtils.generateRandomUid());
            customerPayment.setTokenCreatedTime(ZonedDateTime.now());
            customerPayment.setCardExpiryNotificationCounter(0L);

            customerPayment = customerPaymentRepository.save(customerPayment);
        }

        String customerToken = customerPayment.getCustomerUid();

        model.setCustomer_token(customerToken);

        String manageSubscriptionLinkURL = "https://" + shopInfo.getPublicDomain() + "/" + shopInfoUtils.getManageSubscriptionUrl(shop) + "/cp/" + customerToken;

        if (emailTemplateSetting.getManageSubscriptionButtonUrl() == null) {
            model.setManageSubscriptionLink(manageSubscriptionLinkURL);
        } else {
            model.setManageSubscriptionLink(emailTemplateSetting.getManageSubscriptionButtonUrl());
        }
        model.setSubject(subject);

        model.setFirstOrderAmount(subscriptionContractDetails.getOrderAmount());
        model.setFirstOrderAmountUSD(subscriptionContractDetails.getOrderAmountUSD());
        model.setCurrencyCode(subscriptionContractDetails.getCurrencyCode());

        List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
        }, subscriptionContractDetails.getContractDetailsJSON());

        Double contractAmount = 0D;

        for(SubscriptionProductInfo subscriptionProductInfo : contractJson) {
            contractAmount += Double.parseDouble(subscriptionProductInfo.getDiscountedPrice());
        }

        model.setContractAmount(contractAmount);

        commonUtils.getImageDimensions(emailTemplateSetting, model);

        String emailTemplateHtml = IOUtils.toString(baseMailTemplate.getInputStream());
        String customerEmail = subscriptionContractDetails.getCustomerEmail();
        if (!org.apache.commons.lang3.StringUtils.isBlank(shopInfo.getKlaviyoApiKey()) && !org.apache.commons.lang3.StringUtils.isBlank(shopInfo.getKlaviyoPublicApiKey())) {
            klaviyoService.sendMail(emailTemplateSetting, shopInfo, model, customerEmail);
        } else if (org.apache.commons.lang3.StringUtils.isNotBlank(shopInfo.getMailchimpApiKey())) {
            String html = liquidUtils.getValue(model, Optional.ofNullable(emailTemplateSetting.getHtml()).orElse(emailTemplateHtml));
            mailChimpService.sendMail(customerEmail, subject, html, emailTemplateSetting, shop, shopInfo.getMailchimpApiKey());
        } else {
            String html = liquidUtils.getValue(model, Optional.ofNullable(emailTemplateSetting.getHtml()).orElse(emailTemplateHtml));
            mailgunService.sendEmail(customerEmail, subject, html, emailTemplateSetting, shop, shopDetails);
        }

        log.info("Sent email successfully for shop: {}, emailSettingType: {}, customerEmail: {}, contractId: {}, addActivityLog {}", shop, emailSettingType, customerEmail, subscriptionContractDetails.getSubscriptionContractId(), addActivityLog);

        if (addActivityLog) {
            ActivityLogEventType activityLogEventType = findActivityLogEventType(emailTemplateSetting);
            commonUtils.writeActivityLog(shop, subscriptionContractDetails.getSubscriptionContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, activityLogEventType, ActivityLogStatus.SUCCESS);
        }
    }

    private ActivityLogEventType findActivityLogEventType(EmailTemplateSetting emailTemplateSetting) {
        if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CANCELLED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_CANCELED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PAUSED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_PAUSED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_PRODUCT_ADDED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_PRODUCT_REMOVED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_PRODUCT_REPLACED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.NEXT_ORDER_DATE_UPDATED) {
            return ActivityLogEventType.SEND_NEXT_ORDER_DATE_UPDATED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_FREQUENCY_UPDATED) {
            return ActivityLogEventType.SEND_NEXT_ORDER_DATE_UPDATED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SHIPPING_ADDRESS_UPDATED) {
            return ActivityLogEventType.SEND_SHIPPING_ADDRESS_UPDATED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_RESUMED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_RESUMED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_MANAGEMENT_LINK;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CREATED) {
            return ActivityLogEventType.SEND_SUBSCRIPTION_CREATED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.TRANSACTION_FAILED) {
            return ActivityLogEventType.SEND_TRANSACTION_FAILED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_SKIPPED) {
            return ActivityLogEventType.SEND_ORDER_SKIPPED_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.UPCOMING_ORDER) {
            return ActivityLogEventType.SEND_UPCOMING_ORDER_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.EXPIRING_CREDIT_CARD) {
            return ActivityLogEventType.SEND_EXPIRING_CREDIT_CARD_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SECURITY_CHALLENGE) {
            return ActivityLogEventType.SEND_SECURITY_CHALLENGE_EMAIL;
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.OUT_OF_STOCK) {
            return ActivityLogEventType.SEND_OUT_OF_STOCK_EMAIL;
        }

        log.info("ActivityLogEventType is null. EmailSettingType=" + Optional.of(emailTemplateSetting).map(EmailTemplateSetting::getEmailSettingType).map(Enum::toString).orElse(""));

        return null;
    }

    public ZonedDateTime getDateForEmail(Optional<String> dateOptional) {

        if (dateOptional.isEmpty()) {
            return null;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
        return ZonedDateTime.parse(dateOptional.get(), dateTimeFormatter);
    }

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    public EmailModel buildEmailModel(ShopifyGraphqlClient shopifyGraphqlClient, Shop shopDetails, EmailTemplateSetting emailTemplateSetting, String graphSubscriptionContractId, ShopInfo shopInfo, Customer customerInfo, Long subscriptionContractId, Map<String, Object> additionalAttributes, List<OrderItem> orderItems, String cancellationFeedback, String cancellationNote, String orderName) throws Exception {

        EmailModel model = new EmailModel();

        List<AttributeInfo> allAttributesList = new ArrayList<>();

        Map<String, String> customAttributesMap = new HashMap<>();

        model.setShop(shopDetails);
        model.setHeading_image_url(emailTemplateSetting.getHeadingImageUrl());
        model.setCustomer(customerInfo);

        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(graphSubscriptionContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if(ObjectUtils.isNotEmpty(subscriptionContractOptional.get().getCustomAttributes())) {
            allAttributesList.addAll(subscriptionContractOptional.get().getCustomAttributes().stream().map(attr -> {
                AttributeInfo attributeInfo = new AttributeInfo();
                attributeInfo.setKey(attr.getKey());
                attributeInfo.setValue(attr.getValue().orElse(""));
                customAttributesMap.put(attributeInfo.getKey(), attributeInfo.getValue());
                return attributeInfo;
            }).collect(Collectors.toList()));
        }

        if (CollectionUtils.isEmpty(orderItems)) {
            List<SubscriptionContractQuery.Edge> lineItems = Objects.requireNonNull(subscriptionContractResponseOptional.getData())
                .map(s -> s.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                    .map(SubscriptionContractQuery.Lines::getEdges)
                    .orElse(new ArrayList<>())).orElse(new ArrayList<>());


            orderItems = lineItems.stream().map(SubscriptionContractQuery.Edge::getNode).map(n -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(n.getVariantImage().map(i -> i.getTransformedSrc().toString()).orElse("https://cdn.shopify.com/s/files/1/0533/2089/files/placeholder-images-collection-2_large.png?format=jpg&quality=90&v=1530129132"));

                String price = currencyFormattingUtils.formatPrice(Double.parseDouble(n.getLineDiscountedPrice().getAmount().toString())).replaceAll(Constants.HTML_TAG_REGEX, "");

                if (EmailSettingType.SUBSCRIPTION_CREATED.equals(emailTemplateSetting.getEmailSettingType())) {
                    if (n.getPricingPolicy().isPresent() && !CollectionUtils.isEmpty(n.getPricingPolicy().get().getCycleDiscounts())) {
                        String initialAmount = n.getPricingPolicy().get().getCycleDiscounts().get(0).getComputedPrice().getAmount().toString();
                        price = currencyFormattingUtils.formatPrice(Double.parseDouble(initialAmount)).replaceAll(Constants.HTML_TAG_REGEX, "");
                    }
                }

                if (shopInfo.getCurrency().equals(n.getLineDiscountedPrice().getCurrencyCode().rawValue())) {
                    orderItem.setPrice(price);
                } else {
                    orderItem.setPrice(price + " " + n.getLineDiscountedPrice().getCurrencyCode().rawValue());
                }

                orderItem.setQuantity(n.getQuantity());
                orderItem.setTitle(n.getTitle());
                orderItem.setTaxable(n.isTaxable());
                if (n.getVariantTitle().isPresent() && !n.getVariantTitle().get().equalsIgnoreCase("Default Title") && !n.getVariantTitle().get().equals("-")) {
                    orderItem.setVariantTitle(n.getVariantTitle().get());
                }

                orderItem.setSubscriptionProduct(true);

                if(!CollectionUtils.isEmpty(n.getCustomAttributes())){
                    List<AttributeInfo> customAttributes = n.getCustomAttributes().stream().map(attr -> {
                       AttributeInfo attributeInfo = new AttributeInfo();
                       attributeInfo.setKey(attr.getKey());
                       attributeInfo.setValue(attr.getValue().orElse(""));
                       return attributeInfo;
                    }).collect(Collectors.toList());

                    boolean isOneTimeProduct = commonUtils.isAttributePresent(n.getCustomAttributes(), AppstleAttribute.ONE_TIME_PRODUCT.getKey());

                    boolean isFreeProduct = commonUtils.isAttributePresent(n.getCustomAttributes(), AppstleAttribute.FREE_PRODUCT.getKey());

                    if(isOneTimeProduct || isFreeProduct){
                        orderItem.setSubscriptionProduct(false);
                    }

                    orderItem.setCustomAttributes(customAttributes);
                }

                orderItem.setSellingPlanName(n.getSellingPlanName().orElse(""));

                orderItem.setVariantSku(n.getSku().orElse(""));
                return orderItem;
            }).collect(Collectors.toList());
        }

        if(!CollectionUtils.isEmpty(orderItems)) {
            for(OrderItem orderItem : orderItems) {
                if(CollectionUtils.isEmpty(orderItem.getCustomAttributes())) {
                    continue;
                }

                for(AttributeInfo attributeInfo : orderItem.getCustomAttributes()) {
                    customAttributesMap.put(attributeInfo.getKey(), attributeInfo.getValue());
                    allAttributesList.add(attributeInfo);
                }
            }
        }

        return setEmailModelData(shopifyGraphqlClient, shopDetails, emailTemplateSetting, graphSubscriptionContractId, shopInfo, customerInfo, subscriptionContractId, additionalAttributes, orderItems, cancellationFeedback, cancellationNote, allAttributesList, customAttributesMap, orderName);
    }

    public EmailModel setEmailModelData(ShopifyGraphqlClient shopifyGraphqlClient, Shop shopDetails, EmailTemplateSetting emailTemplateSetting, String graphSubscriptionContractId, ShopInfo shopInfo, Customer customerInfo, Long subscriptionContractId, Map<String, Object> additionalAttributes, List<OrderItem> orderItems, String cancellationFeedback, String cancellationNote, List<AttributeInfo> allAttributesList, Map<String, String > customAttributes, String orderName) throws Exception {

        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(emailTemplateSetting.getShop()).get();

        EmailModel model = new EmailModel();

        model.setShop(shopDetails);
        model.setHeading_image_url(emailTemplateSetting.getHeadingImageUrl());
        model.setCustomer(customerInfo);
        model.setCancellationReason(cancellationFeedback);
        model.setCancellationNote(cancellationNote);

        model.setAllAttributes(allAttributesList);

        model.setCustomAttributes(customAttributes);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(graphSubscriptionContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        if(!CollectionUtils.isEmpty(subscriptionContractResponseOptional.getErrors())) {
            List<String> collect = subscriptionContractResponseOptional.getErrors().stream().map(e -> e.getMessage()).collect(Collectors.toList());
            log.info("error querying subscription contract" + collect);
        }

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        Optional<ShippingAddressModel> shippingAddressModel = commonUtils.getContractShippingAddress(subscriptionContractOptional.get());

        Optional<SubscriptionContractQuery.Instrument> instrument = subscriptionContractResponseOptional
            .getData()
            .flatMap(d -> d.getSubscriptionContract()
                .flatMap(c -> c.getCustomerPaymentMethod()
                    .flatMap(SubscriptionContractQuery.CustomerPaymentMethod::getInstrument)));

        String lastFourDigits = "";
        Optional<SubscriptionContractQuery.BillingAddress> billingAddressOptional = null;
        Optional<SubscriptionContractQuery.BillingAddress1> paypalBillingAddressOptional = null;
        String billingFullName = "";
        String cardBrand = "";
        String billingAddress1 = null;
        String billingCity = null;
        String billingProvinceCode = null;
        String billingZip = null;
        String billingCountry = null;
        String billingCountryCode = null;
        String billingProvince = null;

        if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerPaypalBillingAgreement")) {
            lastFourDigits = "";

            paypalBillingAddressOptional = subscriptionContractResponseOptional
                .getData()
                .flatMap(d -> d.getSubscriptionContract()
                    .flatMap(c -> c.getCustomerPaymentMethod()
                        .flatMap(cp -> cp.getInstrument()
                            .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) cpi).getBillingAddress()))));

            billingFullName = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getName).orElse(StringUtils.EMPTY)).get();

            cardBrand = "";

            billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getAddress1).orElse(StringUtils.EMPTY)).get();

            billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCity).orElse(StringUtils.EMPTY)).get();

            billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getProvinceCode).orElse(StringUtils.EMPTY)).get();

            billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getZip).orElse(StringUtils.EMPTY)).get();

            billingCountry = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCountry).orElse(StringUtils.EMPTY)).get();

            billingCountryCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCountryCode).map(Object::toString).orElse(StringUtils.EMPTY)).get();

            billingProvince = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getProvince).orElse(StringUtils.EMPTY)).get();
        } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerCreditCard")) {

            lastFourDigits = subscriptionContractResponseOptional
                .getData()
                .flatMap(d -> d.getSubscriptionContract()
                    .flatMap(c -> c.getCustomerPaymentMethod()
                        .flatMap(cp -> cp.getInstrument()
                            .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getLastDigits())))).get();

            billingAddressOptional = subscriptionContractResponseOptional
                .getData()
                .flatMap(d -> d.getSubscriptionContract()
                    .flatMap(c -> c.getCustomerPaymentMethod()
                        .flatMap(cp -> cp.getInstrument()
                            .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getBillingAddress()))));

            billingFullName = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getName()).orElse(StringUtils.EMPTY);

            cardBrand = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBrand()).orElse(StringUtils.EMPTY);

            billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getAddress1).orElse(StringUtils.EMPTY)).get();

            billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCity).orElse(StringUtils.EMPTY)).get();

            billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getProvinceCode).orElse(StringUtils.EMPTY)).get();

            billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getZip).orElse(StringUtils.EMPTY)).get();

            billingCountry = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCountry).orElse(StringUtils.EMPTY)).get();

            billingCountryCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCountryCode).map(Object::toString).orElse(StringUtils.EMPTY)).get();

            billingProvince = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getProvince).orElse(StringUtils.EMPTY)).get();

        } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerShopPayAgreement")) {
            lastFourDigits = subscriptionContractResponseOptional
                .getData()
                .flatMap(d -> d.getSubscriptionContract()
                    .flatMap(c -> c.getCustomerPaymentMethod()
                        .flatMap(cp -> cp.getInstrument()
                            .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getLastDigits())))).get();
        }

        Optional<String> nextBillingDateOptional = Objects.requireNonNull(subscriptionContractResponseOptional.getData())
            .flatMap(a -> a.getSubscriptionContract()
                .flatMap(b -> b.getNextBillingDate().map(Object::toString)));

        ZonedDateTime nextBillingDate = getDateForEmail(nextBillingDateOptional);
        String[] timezone = shopInfo.getShopTimeZone().split(" ");

        Optional<String> subscriptionCreatedAt = subscriptionContractOptional.map(SubscriptionContractQuery.SubscriptionContract::getCreatedAt).map(Object::toString);

        ZonedDateTime orderCreatedDate = getDateForEmail(subscriptionCreatedAt);

        ZoneId zoneId = null;

        try {
            zoneId = ZoneId.of(timezone[1]);
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        List<EmailSettingType> blankNextOrderDate = List.of(EmailSettingType.SUBSCRIPTION_CANCELLED, EmailSettingType.SUBSCRIPTION_PAUSED);

        nextBillingDate = nextBillingDate.withZoneSameInstant(zoneId);
        orderCreatedDate = orderCreatedDate.withZoneSameInstant(zoneId);
        String nextOrderDate = blankNextOrderDate.contains(emailTemplateSetting.getEmailSettingType()) ? "-" : CommonUtils.convertDateInLanguage(nextBillingDate, customerPortalSettingsDTO.getLocaleDate());
        String planType = subscriptionContractResponseOptional.getData().map(d -> d.getSubscriptionContract().map(e -> e.getBillingPolicy().getIntervalCount() == e.getDeliveryPolicy().getIntervalCount() ? PAY_AS_YOU_GO : PREPAID).orElse(PAY_AS_YOU_GO)).orElse(PAY_AS_YOU_GO);
        Optional<Integer> minCyclesInt = subscriptionContractOptional.map(s -> s.getBillingPolicy()).flatMap(b -> b.getMinCycles());
        String minCycles = minCyclesInt.isPresent() ? minCyclesInt.get().toString() : "";
        Optional<Integer> maxCyclesInt = subscriptionContractOptional.map(s -> s.getBillingPolicy()).flatMap(b -> b.getMaxCycles());
        String maxCycles = maxCyclesInt.isPresent() ? maxCyclesInt.get().toString() : "";


        List<String> formattedBillingAddress = new ArrayList<>();
        if (billingAddressOptional != null) {
            billingAddressOptional.flatMap(SubscriptionContractQuery.BillingAddress::getAddress1).ifPresent(formattedBillingAddress::add);
            billingAddressOptional
                .map(address ->
                    address.getCity().orElse(StringUtils.EMPTY) + ", " +
                        address.getProvinceCode().orElse(StringUtils.EMPTY) + " - " +
                        address.getZip().orElse(StringUtils.EMPTY))
                .ifPresent(formattedBillingAddress::add);
        } else if (paypalBillingAddressOptional != null) {
            paypalBillingAddressOptional.flatMap(SubscriptionContractQuery.BillingAddress1::getAddress1).ifPresent(formattedBillingAddress::add);
            paypalBillingAddressOptional
                .map(address ->
                    address.getCity().orElse(StringUtils.EMPTY) + ", " +
                        address.getProvinceCode().orElse(StringUtils.EMPTY) + " - " +
                        address.getZip().orElse(StringUtils.EMPTY))
                .ifPresent(formattedBillingAddress::add);
        }

        int deliveryIntervalCount = subscriptionContractResponseOptional.getData().get().getSubscriptionContract().get().getDeliveryPolicy().getIntervalCount();
        SellingPlanInterval deliveryInterval = subscriptionContractResponseOptional.getData().get().getSubscriptionContract().get().getDeliveryPolicy().getInterval();

        int billingIntervalCount = subscriptionContractResponseOptional.getData().get().getSubscriptionContract().get().getBillingPolicy().getIntervalCount();
        SellingPlanInterval billingInterval = subscriptionContractResponseOptional.getData().get().getSubscriptionContract().get().getBillingPolicy().getInterval();

        model.setDeliveryIntervalCount(deliveryIntervalCount);
        model.setDeliveryInterval(deliveryInterval.rawValue());

        model.setBillingIntervalCount(billingIntervalCount);
        model.setBillingInterval(billingInterval.rawValue());

        String frequencyOfSubscription = deliveryIntervalCount + " " + deliveryInterval;

        String challengeUrl = null;

        if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SECURITY_CHALLENGE) {
            List<SubscriptionBillingAttempt> subscriptionBillingAttemptList = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shopInfo.getShop(), subscriptionContractId, BillingAttemptStatus.SECURITY_CHALLENGE);
            if (subscriptionBillingAttemptList.size() == 1) {
                SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptList.get(0);
                String billingAttemptResponseMessage = subscriptionBillingAttempt.getBillingAttemptResponseMessage();
                if (StringUtils.isNotBlank(billingAttemptResponseMessage)) {
                    JSONObject jsonObject = new JSONObject(billingAttemptResponseMessage);
                    if (jsonObject.has(NEXT_ACTION_URL)) {
                        challengeUrl = jsonObject.getString(NEXT_ACTION_URL);
                    } else {

                    }
                } else {

                }
            } else {

            }
        }

         BodyModel bodyModel = new BodyModel(shopDetails, customerInfo, lastFourDigits, nextOrderDate, frequencyOfSubscription, subscriptionContractId, planType, challengeUrl, minCycles, maxCycles, CommonUtils.convertDateInLanguage(orderCreatedDate, customerPortalSettingsDTO.getLocaleDate()), allAttributesList, customAttributes);
        if (!CollectionUtils.isEmpty(additionalAttributes)) {
            try {
                if (additionalAttributes.containsKey("productsAdded")) {
                    List<String> productsAdded = (List<String>) additionalAttributes.get("productsAdded");
                    if (!CollectionUtils.isEmpty(productsAdded)) {
                        bodyModel.setProductsAdded(productsAdded);
                    }
                }
                if (additionalAttributes.containsKey("productsRemoved")) {
                    List<String> productsRemoved = (List<String>) additionalAttributes.get("productsRemoved");
                    if (!CollectionUtils.isEmpty(productsRemoved)) {
                        bodyModel.setProductsRemoved(productsRemoved);
                    }
                }
                if (additionalAttributes.containsKey("outOfStockProducts")) {
                    List<String> outOfStockProducts = (List<String>) additionalAttributes.get("outOfStockProducts");
                    if (!CollectionUtils.isEmpty(outOfStockProducts)) {
                        bodyModel.setOutOfStockProducts(outOfStockProducts);
                    }
                }
            } catch (Exception e) {

            }
        }

        if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.OUT_OF_STOCK) && CollectionUtils.isEmpty(bodyModel.getOutOfStockProducts())) {
            List<String> outOfStockProducts = new ArrayList<>();
            outOfStockProducts.add("Product1");
            outOfStockProducts.add("Product2");
            bodyModel.setOutOfStockProducts(outOfStockProducts);
        }

        bodyModel.setOrderId(orderName.equals("UNKNOWN") ? "" : orderName);

        String body = liquidUtils.getValue(bodyModel, emailTemplateSetting.getContent());

        model.setSubscriptionContractId(subscriptionContractId);

        String shippingAddress = "";

        if (shippingAddressModel.isPresent()) {
            model.setShipping_first_name(shippingAddressModel.get().getShipping_first_name());
            model.setShipping_last_name(shippingAddressModel.get().getShipping_last_name());
            model.setShipping_address1(shippingAddressModel.get().getShipping_address1());
            model.setShipping_address2(shippingAddressModel.get().getShipping_address2());
            model.setShipping_city(shippingAddressModel.get().getShipping_city());
            model.setShipping_province_code(shippingAddressModel.get().getShipping_province_code());
            model.setShipping_zip(shippingAddressModel.get().getShipping_zip());
            model.setShipping_country(shippingAddressModel.get().getShipping_country());
            model.setShipping_country_code(shippingAddressModel.get().getShipping_country_code());
            model.setShipping_province(shippingAddressModel.get().getShipping_province());
            model.setShipping_company(shippingAddressModel.get().getShipping_company());
            shippingAddress = liquidUtils.getValue(shippingAddressModel.get(), emailTemplateSetting.getShippingAddress());
        }

        BillingAddressModel billingAddressModel = new BillingAddressModel(billingAddress1, billingCity, billingProvinceCode, billingZip, billingFullName, billingCountry, billingCountryCode, billingProvince);
        model.setBilling_full_name(billingAddressModel.getBilling_full_name());
        model.setBilling_address1(billingAddressModel.getBilling_address1());
        model.setBilling_city(billingAddressModel.getBilling_city());
        model.setBilling_province_code(billingAddressModel.getBilling_province_code());
        model.setBilling_zip(billingAddressModel.getBilling_zip());
        model.setBilling_country(billingAddressModel.getBilling_country());
        model.setBilling_country_code(billingAddressModel.getBilling_country_code());
        model.setBilling_province(billingAddressModel.getBilling_province());
        String billingAddress = liquidUtils.getValue(billingAddressModel, emailTemplateSetting.getBillingAddress());
        model.setBillingAddress(billingAddress);

        model.setHeader_text_color(emailTemplateSetting.getHeadingTextColor());
        model.setText_color(emailTemplateSetting.getContentTextColor());
        model.setFooter_text_color(emailTemplateSetting.getFooterTextColor());
        model.setFooter_link_color(emailTemplateSetting.getFooterLinkColor());
        model.setHeading(emailTemplateSetting.getHeading());
        model.setLogo_url(emailTemplateSetting.getLogo());
        model.setOrderItems(orderItems);
        model.setBody_content(body);
        model.setFooter(emailTemplateSetting.getFooterText());
        if (instrument.isPresent() && !instrument.get().get__typename().equals("CustomerPaypalBillingAgreement")) {
            model.setMaskedCardNumber(Optional.ofNullable(emailTemplateSetting.getEndingInText() + " ").orElse("Ending in ") + lastFourDigits);
        }
        model.setNextOrderDate(nextOrderDate);
        model.setShipping_address_text(emailTemplateSetting.getShippingAddressText());
        model.setBilling_address_text(emailTemplateSetting.getBillingAddressText());
        model.setNext_orderdate_text(emailTemplateSetting.getNextOrderdateText());
        model.setPayment_method_text(emailTemplateSetting.getPaymentMethodText());
        model.setManage_subscription_button_text(emailTemplateSetting.getManageSubscriptionButtonText());
        model.setManage_subscription_button_text_color(emailTemplateSetting.getManageSubscriptionButtonTextColor());
        model.setManage_subscription_button_color(emailTemplateSetting.getManageSubscriptionButtonColor());
        model.setText_image_url(emailTemplateSetting.getTextImageUrl());
        model.setQuantity_text(emailTemplateSetting.getQuantityText());
        model.setShippingAddress(shippingAddress);
        model.setPlanType(planType);
        model.setNote(subscriptionContractOptional.get().getNote().orElse(""));
        model.setSelling_plan_name_text(emailTemplateSetting.getSellingPlanNameText());
        model.setVariant_sku_text(emailTemplateSetting.getVariantSkuText());

        String cardLogo = "https://brand.mastercard.com//content/dam/mccom/brandcenter/thumbnails/mastercard_circles_92px_2x.png";
        if (cardBrand.toLowerCase().contains("visa")) {
            cardLogo = "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_logo_3.gif";
        } else if (cardBrand.toLowerCase().contains("ame")) {
            cardLogo = "http://www.credit-card-logos.com/images/american_express_credit-card-logos/american_express_logo_3.gif";
        } else if (cardBrand.toLowerCase().contains("discover")) {
            cardLogo = "http://www.credit-card-logos.com/images/discover_credit-card-logos/discover_network1.jpg";
        } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerPaypalBillingAgreement")) {
            cardLogo = "https://avatars.githubusercontent.com/u/476675?s=200";
        } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerShopPayAgreement")) {
            cardLogo = "https://cdn.shopify.com/shopifycloud/help/assets/manual/shop-pay/shop-pay-checkout-button-aaca6e0cd830643b301f23d84dd42c0544864dee645ccee2776c441dee1fea30.png";
        }

        model.setCardLogo(cardLogo);

        List<String> bodyLines = Arrays.stream(Optional.ofNullable(model.getBody_content()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
        model.setBodyLines(bodyLines);

        List<String> shippingLines = Arrays.stream(Optional.ofNullable(model.getShippingAddress()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
        model.setShippingLines(shippingLines);

        List<String> billingLines = Arrays.stream(Optional.ofNullable(model.getBillingAddress()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
        model.setBillingLines(billingLines);

        return model;
    }

}

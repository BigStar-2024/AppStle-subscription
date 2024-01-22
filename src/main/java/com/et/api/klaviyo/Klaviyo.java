package com.et.api.klaviyo;

import com.et.api.klaviyo.list.KlaviyoMailingListResponse;
import com.et.api.klaviyo.model.CustomerProperties;
import com.et.api.klaviyo.template.Datum;
import com.et.api.klaviyo.track.TrackRequest;
import com.et.api.shopify.product.Product;
import com.et.api.shopify.product.Variant;
import com.et.api.shopify.shop.Shop;
import com.et.liquid.Customer;
import com.et.liquid.EmailModel;
import com.et.service.KlaviyoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Base64;
import java.util.List;

public class Klaviyo extends ApiBinding {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();



    public static final String CREATE_SUBSCRIPTION_EVENT_NAME = "Create Subscription Event from Appstle";
    public static final String EXPIRING_CREDIT_CARD_EVENT_NAME = "Expiring Credit Card Event from Appstle";
    public static final String TRANSACTION_FAILED_EVENT_NAME = "Transaction Failed Event from Appstle";
    public static final String SECURITY_CHALLENGE_EVENT_NAME = "Security Challenge Event from Appstle";
    public static final String UPCOMING_ORDER_EVENT_NAME = "Upcoming Order Event from Appstle";
    public static final String SHIPPING_ADDRESS_UPDATED_EVENT_NAME = "Shipping Address  Updated Event from Appstle";
    public static final String ORDER_FREQUENCY_UPDATED_EVENT_NAME = "Order Frequency Updated Event from Appstle";
    public static final String NEXT_ORDER_DATE_UPDATED_EVENT_NAME = "Next Order Date Updated Event from Appstle";
    public static final String SUBSCRIPTION_PAUSED_EVENT_NAME = "Subscription paused Event from Appstle";
    public static final String SUBSCRIPTION_RESUMED_EVENT_NAME = "Subscription resumed Event from Appstle";
    public static final String SUBSCRIPTION_CANCELED_EVENT_NAME = "Subscription canceled Event from Appstle";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_EVENT_NAME = "Product added in Subscription Event from Appstle";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_EVENT_NAME = "Product removed from Subscription Event from Appstle";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_EVENT_NAME = "Product replaced in Subscription Event from Appstle";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_SENT_EVENT_NAME = "Subscription management link sent Event from Appstle";
    public static final String ORDER_SKIPPED_EVENT_NAME = "Order skipped Event from Appstle";
    public static final String BULK_ALL_SUBSCRIBERS_EVENT_NAME = "Bulk all subscribers Event from Appstle";
    public static final String BULK_ACTIVE_SUBSCRIBERS_ONLY_EVENT_NAME = "Bulk active subscribers only Event from Appstle";
    public static final String BULK_CANCELLED_SUBSCRIBERS_ONLY_EVENT_NAME = "Bulk cancelled subscribers only Event from Appstle";
    public static final String BULK_PAUSED_SUBSCRIBERS_ONLY_EVENT_NAME = "Bulk paused subscribers only Event from Appstle";

    public static final String OUT_OF_STOCK_EVENT_NAME = "Out of Stock Event from Appstle";

    public static final String TRANSACTION_SUCCESS_EVENT_NAME = "Transaction Success Event from Appstle";

    public static final String API_BASE_URl = "https://a.klaviyo.com";

    public static final String TRIGGER_CREATE_SUBSCRIPTION_EVENT = API_BASE_URl + "/api/track?data=%s";

    public static final String GET_LISTS = API_BASE_URl + "/api/v2/lists";

    public static final String CREATE_TEMPLATE = API_BASE_URl + "/api/v1/email-templates";

    public Klaviyo(String privateKey, String publicKey) {
        super(privateKey, publicKey);
    }

    private final Logger logger = LoggerFactory.getLogger(KlaviyoService.class);

    public boolean triggerCreateSubscriptionEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerCreateSubscriptionEvent");
        return triggerEmailFor(email, emailModel, CREATE_SUBSCRIPTION_EVENT_NAME);
    }

    public boolean triggerExpiringCreditCardEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerExpiringCreditCardEvent");
        return triggerEmailFor(email, emailModel, EXPIRING_CREDIT_CARD_EVENT_NAME);
    }

    public boolean triggerTransactionFailedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerTransactionFailedEvent");
        return triggerEmailFor(email, emailModel, TRANSACTION_FAILED_EVENT_NAME);
    }

    public boolean triggerSecurityChallengeEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=securityChallengeEvent");
        return triggerEmailFor(email, emailModel, SECURITY_CHALLENGE_EVENT_NAME);
    }

    public boolean triggerUpcomingOrderEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerUpcomingOrderEvent");
        return triggerEmailFor(email, emailModel, UPCOMING_ORDER_EVENT_NAME);
    }

    public boolean triggerShippingAddressUpdatedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerShippingAddressUpdatedEvent");
        return triggerEmailFor(email, emailModel, SHIPPING_ADDRESS_UPDATED_EVENT_NAME);
    }

    public boolean triggerOrderFrequencyUpdatedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerOrderFrequencyUpdatedEvent");
        return triggerEmailFor(email, emailModel, ORDER_FREQUENCY_UPDATED_EVENT_NAME);
    }

    public boolean triggerNextOrderUpdatedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerNextOrderUpdatedEvent");
        return triggerEmailFor(email, emailModel, NEXT_ORDER_DATE_UPDATED_EVENT_NAME);
    }

    public boolean triggerSubscriptionPausedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionPausedEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_PAUSED_EVENT_NAME);
    }

    public boolean triggerSubscriptionResumeEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionResumeEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_RESUMED_EVENT_NAME);
    }

    public boolean triggerSubscriptionProductAddedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionProductAddedEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_PRODUCT_ADDED_EVENT_NAME);
    }

    public boolean triggerSubscriptionProductRemovedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionProductRemovedEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_PRODUCT_REMOVED_EVENT_NAME);
    }

    public boolean triggerSubscriptionProductReplacedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionProductReplacedEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_PRODUCT_REPLACED_EVENT_NAME);
    }

    public boolean triggerSubscriptionCanceledEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionCanceledEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_CANCELED_EVENT_NAME);
    }

    public boolean triggerSubscriptionManagementLinkEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerSubscriptionManagementLinkEvent");
        return triggerEmailFor(email, emailModel, SUBSCRIPTION_MANAGEMENT_LINK_SENT_EVENT_NAME);
    }

    public boolean triggerOrderSkippedEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerOrderSkippedEvent");
        return triggerEmailFor(email, emailModel, ORDER_SKIPPED_EVENT_NAME);
    }

    public boolean triggerOutOfStockEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerOrderSkippedEvent");
        return triggerEmailFor(email, emailModel, OUT_OF_STOCK_EVENT_NAME);
    }

    public boolean triggerBulkAllSubscriberEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerBulkAllSubscriberEvent");
        return triggerEmailFor(email, emailModel, BULK_ALL_SUBSCRIBERS_EVENT_NAME);
    }

    public boolean triggerBulkActiveSubscriberOnlyEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerBulkActiveSubscriberOnlyEvent");
        return triggerEmailFor(email, emailModel, BULK_ACTIVE_SUBSCRIBERS_ONLY_EVENT_NAME);
    }

    public boolean triggerBulkCancelledSubscriberOnlyEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerBulkCancelledSubscriberOnlyEvent");
        return triggerEmailFor(email, emailModel, BULK_CANCELLED_SUBSCRIBERS_ONLY_EVENT_NAME);
    }

    public boolean triggerBulkPausedSubscriberOnlyEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerBulkPausedSubscriberOnlyEvent");
        return triggerEmailFor(email, emailModel, BULK_PAUSED_SUBSCRIBERS_ONLY_EVENT_NAME);
    }

    public boolean triggerTransactionSuccessEvent(String email, EmailModel emailModel) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + email + " shop=" + emailModel.getShop() + " emailSettingType=triggerTransactionSuccessEvent");
        return triggerEmailFor(email, emailModel, TRANSACTION_SUCCESS_EVENT_NAME);
    }


    private boolean triggerEmailFor(String email, EmailModel emailModel, String eventName) throws JsonProcessingException {
        CustomerProperties customerProperties = new CustomerProperties(email);

        TrackRequest trackRequest = new TrackRequest();
        trackRequest.setToken(this.publicKey);
        trackRequest.setEvent(eventName);

        trackRequest.setCustomer_properties(customerProperties);

        trackRequest.setProperties(emailModel);

        String data = Base64.getEncoder().encodeToString(OBJECT_MAPPER.writeValueAsBytes(trackRequest));

        String result = restTemplate.getForObject(String.format(TRIGGER_CREATE_SUBSCRIPTION_EVENT, data), String.class);

        return "1".equalsIgnoreCase(result);
    }

//    private EmailModel filterEmailModelForKlaviyo(EmailModel emailModel) {
//        // HTML content is not supported in track api data
//        // Remove fields with HTML content
//        EmailModel filteredEmailModel = null;
//
//        if (emailModel != null) {
//            filteredEmailModel = new EmailModel();
//
//            Shop shop = emailModel.getShop();
//            if(shop != null) {
//                Shop filteredShop = new Shop();
//                filteredShop.setName(shop.getName());
//                filteredShop.setEmail(shop.getEmail());
//                filteredShop.setPhone(shop.getPhone());
//                filteredShop.setDomain(shop.getDomain());
//                filteredShop.setShopOwner(shop.getShopOwner());
//                filteredShop.setAddress1(shop.getAddress1());
//                filteredShop.setAddress2(shop.getAddress2());
//                filteredShop.setCity(shop.getCity());
//                filteredShop.setProvince(shop.getProvince());
//                filteredShop.setProvinceCode(shop.getProvinceCode());
//                filteredShop.setZip(shop.getZip());
//                filteredShop.setCountry(shop.getCountry());
//                filteredShop.setCountryCode(shop.getCountryCode());
//
//                filteredEmailModel.setShop(filteredShop);
//            }
//
//            Product product = emailModel.getProduct();
//            if(product != null) {
//                Product filteredProduct = new Product();
//                filteredProduct.setTitle(product.getTitle());
//                filteredProduct.setHandle(product.getHandle());
//                filteredProduct.setId(product.getId());
//                filteredProduct.setVendor(product.getVendor());
//
//                filteredEmailModel.setProduct(filteredProduct);
//            }
//
//            Variant variant = emailModel.getVariant();
//            if(variant != null) {
//                Variant filteredVariant = new Variant();
//                filteredVariant.setTitle(variant.getTitle());
//                filteredVariant.setPrice(variant.getPrice());
//                filteredVariant.setSku(variant.getSku());
//
//                filteredEmailModel.setVariant(filteredVariant);
//            }
//
//            Customer customer = emailModel.getCustomer();
//            if(customer != null) {
//                Customer filteredCustomer = new Customer();
//                filteredCustomer.setUnsubscribe_url(customer.getUnsubscribe_url());
//
//                filteredEmailModel.setCustomer(filteredCustomer);
//            }
//        }
//
//        return filteredEmailModel;
//    }

    public List<KlaviyoMailingListResponse> getLists() {
        KlaviyoMailingListResponse[] result = restTemplate.getForObject(GET_LISTS, KlaviyoMailingListResponse[].class);
        return List.of(ArrayUtils.nullToEmpty(result, KlaviyoMailingListResponse[].class));
    }

    public Datum createTemplate(String name, String html) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestData= new LinkedMultiValueMap<>();
        requestData.add("api_key", this.privateKey);
        requestData.add("name", name);
        requestData.add("html", html);

        HttpEntity<MultiValueMap<String, String>> requestDataEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<Datum> responseEntity =  restTemplate.postForEntity(CREATE_TEMPLATE, requestDataEntity, Datum.class);

        Datum result = null;
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            result = responseEntity.getBody();
        }

        return result;
    }

}


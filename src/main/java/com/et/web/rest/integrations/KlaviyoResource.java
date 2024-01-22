package com.et.web.rest.integrations;



import com.amazonaws.util.CollectionUtils;
import com.et.api.klaviyo.Klaviyo;
import com.et.api.klaviyo.list.AddToListRequest;
import com.et.api.klaviyo.list.KlaviyoMailingListResponse;
import com.et.api.klaviyo.list.Profile;
import com.et.api.klaviyo.template.Datum;
import com.et.api.klaviyo.template.KlaviyoTemplatesResponse;
import com.et.domain.EmailTemplateSetting;
import com.et.repository.EmailTemplateSettingRepository;
import com.et.security.SecurityUtils;
import com.et.service.EmailTemplateSettingService;
import com.et.service.ShopInfoService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.KlaviyoEmailTemplateInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class KlaviyoResource {

    private final Logger log = LoggerFactory.getLogger(KlaviyoResource.class);

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    @Value("classpath:templates/klaviyo-sample-email-template.html")
    private Resource klaviyoEmailTemplateResource;


    @GetMapping("/klaviyo/templates")
    public List<KlaviyoEmailTemplateInfo> getKlaviyoTemplates() throws IOException, UnirestException {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        if (!StringUtils.isEmpty(shopInfo.getKlaviyoApiKey())) {

            HttpResponse<String> response = Unirest.get("https://a.klaviyo.com/api/v1/email-templates?api_key=" + shopInfo.getKlaviyoApiKey())
                .header("Content-Type", "application/json")
                .asString();

            ObjectMapper objectMapper = new ObjectMapper();

            KlaviyoTemplatesResponse klaviyoTemplateResponse = objectMapper.readValue(response.getBody(), KlaviyoTemplatesResponse.class);


            return klaviyoTemplateResponse.getData().stream().map(m -> new KlaviyoEmailTemplateInfo(m.getId(), m.getName())).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @PostMapping("/klaviyo/create-sample-template")
    public Datum createKlaviyoTemplate() {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        log.info("Creating sample klaviyo template for shop={}", shop);

        if (!StringUtils.isEmpty(shopInfo.getKlaviyoApiKey())) {

            Klaviyo klaviyoAPI = new Klaviyo(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoPublicApiKey());

            try {
                String htmlStr = IOUtils.toString(klaviyoEmailTemplateResource.getURI(), Charset.defaultCharset());

                return klaviyoAPI.createTemplate("Appstle Subscription Email Template", htmlStr);

            } catch (Exception e) {
                log.error("Error while creating sample klaviyo template for shop=" + shop, e);
            }

        }

        return null;
    }

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @GetMapping("/klaviyo/trigger-sample-event")
    public boolean triggerSampleBackInStockEvent() throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        log.info("Creating sample klaviyo event for shop={}", shop);

        if (!StringUtils.isEmpty(shopInfo.getKlaviyoPublicApiKey())) {

            Klaviyo klaviyoAPI = new Klaviyo(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoPublicApiKey());

            List<EmailTemplateSetting> emailTemplateSettings = emailTemplateSettingRepository.findByShop(shop);

            for (EmailTemplateSetting emailTemplateSetting : emailTemplateSettings) {
                try {
                    emailTemplateSettingService.sendTestMail(shop, "sample.event@appstle.com", emailTemplateSetting.getId());
                } catch (Exception ex) {

                }
            }


            /*try {

                boolean result = klaviyoAPI.triggerCreateSubscriptionEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerExpiringCreditCardEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerTransactionFailedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerUpcomingOrderEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerShippingAddressUpdatedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerOrderFrequencyUpdatedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerNextOrderUpdatedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerSubscriptionPausedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerSubscriptionResumeEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerSubscriptionCanceledEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerSubscriptionProductAddedEvent("sample.event@appstle.com", null);
                result = klaviyoAPI.triggerSubscriptionProductRemovedEvent("sample.event@appstle.com", null);
                return result;

            } catch (Exception e) {
                log.error("Error while creating sample klaviyo event for shop=" + shop, e);
            }*/

        }

        return false;
    }

    @GetMapping("/klaviyo/lists")
    public List<KlaviyoMailingListResponse> getLists(@RequestParam(name = "klaviyoApiKey", required = false) String klaviyoApiKey) {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        klaviyoApiKey = StringUtils.isNotBlank(klaviyoApiKey) ? klaviyoApiKey : shopInfo.getKlaviyoApiKey();

        if (StringUtils.isNotBlank(klaviyoApiKey)) {
            HttpResponse<String> response = Unirest.get("https://a.klaviyo.com/api/v2/lists?api_key=" + klaviyoApiKey.trim())
                .asString();

            if(response.isSuccess()) {
                List<KlaviyoMailingListResponse> klaviyoResponse = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>(){}, response.getBody());
                return klaviyoResponse;
            } else if(response.getStatus() == 401 || response.getStatus() == 403) {
                throw new BadRequestAlertException("Invalid Klaviyo API Key", "", "");
            } else {
                log.error("Error while getting klaviyo lists, responseBody={}", response.getBody());
            }

            return null;
        }

        return null;
    }

    @GetMapping("/klaviyo/add-member-to-list")
    public Profile addMemberToList(@RequestParam String email) throws IOException, UnirestException {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        if (StringUtils.isNoneBlank(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoListId())) {

            AddToListRequest addToListRequest = new AddToListRequest();
            Profile profile = new Profile();
            profile.setEmail(email);
            addToListRequest.getProfiles().add(profile);

            List<Profile> klaviyoResponse = sendAddMembersToList(addToListRequest, shopInfo.getKlaviyoListId(), shopInfo.getKlaviyoApiKey());
            if(!CollectionUtils.isNullOrEmpty(klaviyoResponse)) {
                return klaviyoResponse.get(0);
            } else {
                log.error("Error while adding email={} to list={}", email, shopInfo.getKlaviyoListId());
            }

            return null;
        }

        return null;
    }

    @GetMapping("/klaviyo/add-all-members-to-list")
    public List<Profile> addAllMembersToList() throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("Rest request to add all customer emails to klaviyo list for shop: {}", shop);

        List<String> customerEmails = subscriptionContractDetailsService.findCustomerEmailByShop(shop);

        if(!CollectionUtils.isNullOrEmpty(customerEmails)){

            ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

            if (StringUtils.isNoneBlank(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoListId())) {

                List<Profile> klaviyoResponse = new ArrayList<>();
                // Run in batch of 100
                for (List<String> emailsPartition : Lists.partition(customerEmails, 100)) {
                    AddToListRequest addToListRequest = new AddToListRequest();
                    List<Profile> profiles = new ArrayList<>();
                    for (String email : emailsPartition) {
                        Profile profile = new Profile();
                        profile.setEmail(email);
                        profiles.add(profile);
                    }
                    addToListRequest.setProfiles(profiles);

                    klaviyoResponse.addAll(sendAddMembersToList(addToListRequest, shopInfo.getKlaviyoListId(), shopInfo.getKlaviyoApiKey()));
                }
                if (!CollectionUtils.isNullOrEmpty(klaviyoResponse)) {
                    return klaviyoResponse;
                } else {
                    log.error("Error while syncing past emails to list={}", shopInfo.getKlaviyoListId());
                }
            }

            return null;
        }else{
            log.warn("No customer emails found for shop: {}", shop);
            return null;
        }
    }

    private List<Profile> sendAddMembersToList (AddToListRequest addToListRequest, String klaviyoListId, String klaviyoApiKey) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpResponse<String> response = Unirest.post("https://a.klaviyo.com/api/v2/list/"+klaviyoListId+"/members?api_key=" + klaviyoApiKey.trim())
            .header("Content-Type", "application/json")
            .body(objectMapper.writeValueAsString(addToListRequest))
            .asString();

        if(response.isSuccess()) {
            List<Profile> klaviyoResponse = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>(){}, response.getBody());
            return klaviyoResponse;
        } else {
            log.error("Error while adding emails to list={}, responseBody={}", klaviyoListId, response.getBody());
        }

        return null;
    }

}

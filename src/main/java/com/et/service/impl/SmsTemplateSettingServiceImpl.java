package com.et.service.impl;

import com.et.api.shopify.product.Product;
import com.et.domain.ShopInfo;
import com.et.liquid.Customer;
import com.et.liquid.GenericMessageModel;
import com.et.liquid.GenericSmsModel;
import com.et.liquid.LiquidUtils;
import com.et.repository.ShopInfoRepository;
import com.et.service.SmsTemplateSettingService;
import com.et.domain.SmsTemplateSetting;
import com.et.repository.SmsTemplateSettingRepository;
import com.et.service.dto.SmsTemplateSettingDTO;
import com.et.service.mapper.SmsTemplateSettingMapper;
import com.et.utils.ShopInfoUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import ClickSend.Api.SmsApi;
import ClickSend.ApiClient;
import ClickSend.ApiException;
import ClickSend.Configuration;
import ClickSend.Model.SmsMessage;
import ClickSend.Model.SmsMessageCollection;
import ClickSend.auth.HttpBasicAuth;

/**
 * Service Implementation for managing {@link SmsTemplateSetting}.
 */
@Service
@Transactional
public class SmsTemplateSettingServiceImpl implements SmsTemplateSettingService {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateSettingServiceImpl.class);

    private final SmsTemplateSettingRepository smsTemplateSettingRepository;

    private final SmsTemplateSettingMapper smsTemplateSettingMapper;

    @Autowired
    private LiquidUtils liquidUtils;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    public SmsTemplateSettingServiceImpl(SmsTemplateSettingRepository smsTemplateSettingRepository, SmsTemplateSettingMapper smsTemplateSettingMapper) {
        this.smsTemplateSettingRepository = smsTemplateSettingRepository;
        this.smsTemplateSettingMapper = smsTemplateSettingMapper;
    }

    @Override
    public SmsTemplateSettingDTO save(SmsTemplateSettingDTO smsTemplateSettingDTO) {
        log.debug("Request to save SmsTemplateSetting : {}", smsTemplateSettingDTO);
        SmsTemplateSetting smsTemplateSetting = smsTemplateSettingMapper.toEntity(smsTemplateSettingDTO);
        smsTemplateSetting = smsTemplateSettingRepository.save(smsTemplateSetting);
        return smsTemplateSettingMapper.toDto(smsTemplateSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsTemplateSettingDTO> findAll() {
        log.debug("Request to get all SmsTemplateSettings");
        return smsTemplateSettingRepository.findAll().stream()
            .map(smsTemplateSettingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsTemplateSettingDTO> findAllByShop(String shop) {
        log.debug("Request to get all SmsTemplateSettings");
        return smsTemplateSettingRepository.findAllByShop(shop).stream()
            .map(smsTemplateSettingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SmsTemplateSettingDTO> findOne(Long id) {
        log.debug("Request to get SmsTemplateSetting : {}", id);
        return smsTemplateSettingRepository.findById(id)
            .map(smsTemplateSettingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SmsTemplateSetting : {}", id);
        smsTemplateSettingRepository.deleteById(id);
    }

    @Override
    public void deleteByShop(String shop) {
        log.debug("Request to delete SmsTemplateSetting by shop : {}", shop);
        smsTemplateSettingRepository.deleteByShop(shop);
    }

    @Override
    public String getSmsContent(SmsTemplateSettingDTO smsTemplateSettingDTO) throws IOException{

        String smsTemplate = smsTemplateSettingDTO.getSmsContent();

        ShopInfo shopInfo = shopInfoRepository.findByShop(smsTemplateSettingDTO.getShop()).get();

        ZonedDateTime nextBillingDate = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(2);
        String nextOrderDate = nextBillingDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

//        String shortenUrl = generateShortUrlYourls("https://" + smsTemplateSettingDTO.getShop() + "/" + shopInfoUtils.getManageSubscriptionUrl(smsTemplateSettingDTO.getShop()) + "?token=" + 4242);

        Customer customer = new Customer("John Doe","John","Doe", "efwdwfefe", "john@gmail.com");
        GenericSmsModel genericMessageModel = new GenericSmsModel(smsTemplateSettingDTO.getShop(), customer, "https://" + shopInfo.getPublicDomain() + "/" + shopInfoUtils.getManageSubscriptionUrl(smsTemplateSettingDTO.getShop()) + "?token=" + 222222, "4242", nextOrderDate);
        String smsBody = liquidUtils.getValue(genericMessageModel, smsTemplate);
        return smsBody;
    }

    @Override
    public Boolean sendTestSMS(Long smsTemplateId, String phone) throws ApiException, IOException {

        SmsTemplateSettingDTO automationDTO = findOne(smsTemplateId).get();
        String smsBody = getSmsContent(automationDTO);

        smsBody = smsBody + " \nReply STOP to opt-out";

        ApiClient apiClient = Configuration.getDefaultApiClient();

        HttpBasicAuth basicAuth = (HttpBasicAuth) apiClient.getAuthentication("BasicAuth");
        basicAuth.setUsername("admin@appstle.com");
        basicAuth.setPassword("E42F60B8-E843-08C2-2483-2CC17EC61122");

        SmsApi smsApi = new SmsApi(apiClient);

        SmsMessage smsMessage = new SmsMessage();
        smsMessage.body(smsBody);
        smsMessage.to(phone);
        smsMessage.source("subscription");
        smsMessage.fromEmail("admin@appstle.com");

        SmsMessageCollection smsMessages = new SmsMessageCollection();
        smsMessages.messages(Arrays.asList(smsMessage));
        String result = smsApi.smsSendPost(smsMessages);
        return true;
    }

    public String generateShortUrlYourls(String longUrl) throws IOException {
        String encodedUrl = URLEncoder.encode(longUrl, StandardCharsets.UTF_8.toString());
        HttpResponse<JsonNode> response = Unirest.get("https://tiny.appstle.com/yourls-api.php?signature=05e2685fc7&action=shorturl&format=json&url=" + encodedUrl)
            .asJson();
        JsonNode node = response.getBody();
        String shorturl = node.getObject().getString("shorturl");
        return shorturl;
    }
}

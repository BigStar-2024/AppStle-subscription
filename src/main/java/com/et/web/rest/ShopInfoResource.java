package com.et.web.rest;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.shop.Shop;
import com.et.api.svix.SvixClient;
import com.et.constant.MailgunProperties;
import com.et.domain.SocialConnection;
import com.et.handler.OnBoardingHandler;
import com.et.security.SecurityUtils;
import com.et.service.ShopInfoService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.utils.WebhookUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.mailgun.MailgunDomainVM;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.type.WebhookSubscriptionTopic;
import com.svix.models.ApplicationOut;
import com.svix.models.DashboardAccessOut;
import org.apache.commons.lang3.time.DateUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ShopInfo}.
 */
@RestController
@RequestMapping("/api")
public class ShopInfoResource {

    private final Logger log = LoggerFactory.getLogger(ShopInfoResource.class);

    private static final String ENTITY_NAME = "shopInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopInfoService shopInfoService;

    public ShopInfoResource(ShopInfoService shopInfoService) {
        this.shopInfoService = shopInfoService;
    }

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private WebhookUtils webhookUtils;

    @Autowired
    private OnBoardingHandler onBoardingHandler;

    @Autowired
    private SocialConnectionService socialConnectionService;

    /**
     * {@code POST  /shop-infos} : Create a new shopInfo.
     *
     * @param shopInfoDTO the shopInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopInfoDTO, or with status {@code 400 (Bad Request)} if the shopInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-infos")
    public ResponseEntity<ShopInfoDTO> createShopInfo(@Valid @RequestBody ShopInfoDTO shopInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ShopInfo : {}", shopInfoDTO);
        if (shopInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(shopInfoDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ShopInfoDTO result = shopInfoService.save(shopInfoDTO);
        return ResponseEntity.created(new URI("/api/shop-infos/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-infos} : Updates an existing shopInfo.
     *
     * @param shopInfoDTO the shopInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopInfoDTO,
     * or with status {@code 400 (Bad Request)} if the shopInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-infos")
    public ResponseEntity<ShopInfoDTO> updateShopInfo(@Valid @RequestBody ShopInfoDTO shopInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ShopInfo : {}", shopInfoDTO);
        if (shopInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(shopInfoDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ShopInfoDTO existingShopInfo = shopInfoService.findByShop(shop).get();
        if (existingShopInfo.getEmailCustomDomain() == null || !existingShopInfo.getEmailCustomDomain().equalsIgnoreCase(shopInfoDTO.getEmailCustomDomain())) {
            if (StringUtils.isNotEmpty(shopInfoDTO.getEmailCustomDomain())) {
                HttpResponse<JsonNode> response = Unirest.post(mailgunProperties.getBaseUrl() + "domains")
                    .basicAuth("api", mailgunProperties.getApiKey())
                    .field("name", shopInfoDTO.getEmailCustomDomain())
                    .asJson();
                String a = "b";
            }
        }

        ShopInfoDTO result = shopInfoService.save(shopInfoDTO);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        if (BooleanUtils.isTrue(result.isPriceSyncEnabled()) || BooleanUtils.isTrue(result.isSkuSyncEnabled())) {
            String webhookUrl = webhookUtils.getWebhookUrlForTopic(shopifyGraphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_CONTRACTS_CREATE);
            webhookUtils.createWebhook(shopifyGraphqlClient, WebhookSubscriptionTopic.PRODUCTS_UPDATE, webhookUrl);
        } else {
            webhookUtils.deleteWebhookForTopic(shopifyGraphqlClient, WebhookSubscriptionTopic.PRODUCTS_UPDATE);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Shop Settings Updated.", ""))
            .body(result);
    }

    @PutMapping("/shop-info/regenerate-key")
    public ResponseEntity<ShopInfoDTO> regenerateAPIKey() throws URISyntaxException {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<ShopInfoDTO> optionalShopInfo = shopInfoService.findByShop(shop);

        if (optionalShopInfo.isPresent()) {

            ShopInfoDTO shopInfo = optionalShopInfo.get();
            shopInfo.setApiKey(CommonUtils.generateRandomUid());
            shopInfoService.save(shopInfo);
        }

        return ResponseUtil.wrapOrNotFound(optionalShopInfo);
    }


    /**
     * {@code GET  /shop-infos} : get all the shopInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopInfos in body.
     */

    @GetMapping("/shop-infos")
    public List<ShopInfoDTO> getAllShopInfos() {
        log.debug("REST request to get all ShopInfos");
        List<ShopInfoDTO> result = new ArrayList<>();
        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(SecurityUtils.getCurrentUserLogin().get());
        shopInfoDTOOptional.ifPresent(result::add);
        return result;
    }

    /**
     * {@code GET  /shop-infos/:id} : get the "id" shopInfo.
     *
     * @param id the id of the shopInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-infos/{id}")
    public ResponseEntity<ShopInfoDTO> getShopInfo(@PathVariable Long id) {
        log.debug("REST request to get ShopInfo : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfoDTO = shopInfoService.findByShop(shop);
        if (!shopInfoDTO.isPresent()) {
            Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(shop);
            if (socialConnection.isPresent()) {
                ShopifyAPI api = new ShopifyWithRateLimiter(socialConnection.get().getAccessToken(), shop);
                Shop shopDetails = api.getShopInfo().getShop();
                ShopInfoDTO shopInfoDTO1 = onBoardingHandler.insertShopInfo(shop, shopDetails);
                return ResponseUtil.wrapOrNotFound(Optional.of(shopInfoDTO1));
            } else {
                return ResponseUtil.wrapOrNotFound(Optional.of(new ShopInfoDTO()));
            }
        }
        return ResponseUtil.wrapOrNotFound(shopInfoDTO);
    }

    @GetMapping("/shop-infos/shop-infos-current-login")
    public ResponseEntity<ShopInfoDTO> getShopInfoByCurrentLogin() {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfoDTO = shopInfoService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(shopInfoDTO);
    }

    @CrossOrigin
    @GetMapping("/shop-infos-by-shop/{shop}")
    public ResponseEntity<ShopInfoDTO> getShopInfo(@PathVariable String shop,
                                                   HttpServletRequest request) {
        log.debug("REST request to get ShopInfo : {}", shop);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        Optional<ShopInfoDTO> shopInfoDTO = shopInfoService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(shopInfoDTO);
    }

    /**
     * {@code DELETE  /shop-infos/:id} : delete the "id" shopInfo.
     *
     * @param id the id of the shopInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-infos/{id}")
    public ResponseEntity<Void> deleteShopInfo(@PathVariable Long id) {
        log.debug("REST request to delete ShopInfo : {}", id);
        shopInfoService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @Autowired
    private MailgunProperties mailgunProperties;

    @GetMapping("/shop-infos/email-custom-domain")
    public MailgunDomainVM getShopInfoEmailCustomDomain(UriComponentsBuilder uriBuilder) throws IOException {
        log.debug("REST request to get a page of ShopInfos");
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(SecurityUtils.getCurrentUserLogin().get());
        ShopInfoDTO shopInfoDB = shopInfo.get();
        if (StringUtils.isNotBlank(shopInfoDB.getEmailCustomDomain())) {
            HttpResponse<String> response = Unirest.get(mailgunProperties.getBaseUrl() + "domains/" + shopInfoDB.getEmailCustomDomain())
                .basicAuth("api", mailgunProperties.getApiKey())
                .asString();

            if(response.getStatus() == 404) {
                // Domain is deleted
                shopInfoDB.setEmailCustomDomain(null);
                shopInfoDB.setVerifiedEmailCustomDomain(false);
                shopInfoService.save(shopInfoDB);
                return null;
            }

            MailgunDomainVM mailgunDomainVM = new ObjectMapper().readValue(response.getBody(), MailgunDomainVM.class);

            boolean isCustomDomainValid = false;
            if (!CollectionUtils.isEmpty(mailgunDomainVM.getSendingDnsRecords())) {
                isCustomDomainValid = mailgunDomainVM.getSendingDnsRecords().stream().allMatch(sr -> "valid".equalsIgnoreCase(sr.getValid()));
            }
            if (mailgunDomainVM.getDomain().getState().equalsIgnoreCase("unverified")) {
                Date createdDate = null;
                try {
                    createdDate = DateUtils.parseDate(mailgunDomainVM.getDomain().getCreatedAt(), "EEE, dd MMM yyyy HH:mm:ss z");
                } catch (ParseException e) {
                    log.error("Error occurred while parsing date for checking custom domain");
                }

                if (DateUtils.addMonths(createdDate, 1).before(new Date())) {

                    HttpResponse<String> deleteResponse = Unirest.delete(mailgunProperties.getBaseUrl() + "domains/" + mailgunDomainVM.getDomain().getName())
                        .basicAuth("api", mailgunProperties.getApiKey())
                        .asString();

                    isCustomDomainValid = false;

                    if (deleteResponse.isSuccess()) {
                        log.info("Mailgun domain deleted. domain={}, deleteResponse={}", mailgunDomainVM.getDomain().getName(), deleteResponse);
                        // Domain is deleted
                        shopInfoDB.setEmailCustomDomain(null);
                        shopInfoDB.setVerifiedEmailCustomDomain(false);
                        shopInfoService.save(shopInfoDB);
                        return null;
                    } else {
                        log.error("Mailgun error while delteting domain. domain={}, deleteResponse={}", mailgunDomainVM.getDomain().getName(), deleteResponse.getBody());
                    }
                }
            }

            if (shopInfoDB.isVerifiedEmailCustomDomain() == null || shopInfoDB.isVerifiedEmailCustomDomain().booleanValue() != isCustomDomainValid) {
                // Update status change in database
                shopInfoDB.setVerifiedEmailCustomDomain(isCustomDomainValid);
                shopInfoService.save(shopInfoDB);
            }

            return mailgunDomainVM;
        } else {
            return null;
        }
    }


    @GetMapping("/shop-infos/webhook-portal")
    public DashboardAccessOut getWebhookPortal() throws IOException {

        String shop = commonUtils.getShop();

        log.debug("REST request to get a webhook portal for shop={}", shop);

        try {
            SvixClient svixClient = commonUtils.prepareSvixClient(shop);

            // Register shop if not
            ApplicationOut svixApp = svixClient.getOrCreateApplication();

            // Get dashboard access
            DashboardAccessOut svixDashboard = svixClient.getDashboard();

            log.debug("Webhook portal received for shop={}, dashboard={}", shop, svixDashboard);

            return svixDashboard;

        } catch (Exception e) {
            log.info("Error while getting webhook portal for shop={}, ex={}", shop, ExceptionUtils.getStackTrace(e));
        }

        return null;
    }
}

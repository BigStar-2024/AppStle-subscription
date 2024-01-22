package com.et.web.rest.integrations;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.SubscriptionContractDetails;
import com.et.repository.SocialConnectionRepository;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.ShopInfoService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.vm.ZapierSubscriptionVM;
import com.shopify.java.graphql.client.queries.SubscriptionContactCustomerQuery;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.CUSTOMER_ID_PREFIX;

@RestController
@RequestMapping("/api/zapier")
public class ZapierResource {

    private final Logger log = LoggerFactory.getLogger(ZapierResource.class);

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SocialConnectionRepository socialConnectionRepository;

    @Autowired
    private CommonUtils commonUtils;

    @GetMapping("/subscription-created")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionCreatedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-created")
    public boolean subscriptionCreated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionCreatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-created")
    public boolean subscriptionCreated(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionCreatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @GetMapping("/subscription-updated")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionUpdatedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-updated")
    public boolean subscriptionUpdated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionUpdatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-updated")
    public boolean subscriptionUpdated(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionUpdatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @GetMapping("/subscription-recurring-order-placed")
    public List<SubscriptionBillingAttempt> subscriptionRecurringOrderPlacedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionBillingAttempt> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionBillingAttempt> subscriptionBillingAttemptList = subscriptionBillingAttemptRepository.findByShop(shop);

            int i = 0;

            for (SubscriptionBillingAttempt subscriptionBillingAttempt : subscriptionBillingAttemptList) {

                if (i == 4) {
                    break;
                }

                result.add(subscriptionBillingAttempt);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-recurring-order-placed")
    public boolean subscriptionRecurringOrderPlaced(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierRecurringOrderPlacedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-recurring-order-placed")
    public boolean subscriptionRecurringOrderPlaced(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierRecurringOrderPlacedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @GetMapping("/customer-subscriptions")
    public List<SubscriptionContactCustomerQuery.Customer> getCustomerSubscriptionsList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContactCustomerQuery.Customer> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            Set<Long> customerIds = subscriptionContractDetailsList.stream().map(SubscriptionContractDetails::getCustomerId).collect(Collectors.toSet());

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (Long customerId : customerIds) {
                if (i == 4) {
                    break;
                }
                SubscriptionContactCustomerQuery subscriptionContactCustomerQuery = new SubscriptionContactCustomerQuery(CUSTOMER_ID_PREFIX + customerId);
                Response<Optional<SubscriptionContactCustomerQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContactCustomerQuery);
                SubscriptionContactCustomerQuery.Customer customer = response.getData().map(d -> d.getCustomer().orElse(null)).orElse(null);
                result.add(customer);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/customer-subscriptions")
    public boolean customerSubscriptionsList(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierCustomerSubscriptionsUpdatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/customer-subscriptions")
    public boolean customerSubscriptionsList(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierCustomerSubscriptionsUpdatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo);
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @GetMapping("/subscription-cancelled")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionCanceledList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
            subscriptionContractDetailsList = subscriptionContractDetailsList.stream().filter(s -> s.getStatus().equals("cancelled")).collect(Collectors.toList());

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-cancelled")
    public boolean subscriptionCancelled(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionCanceledUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-cancelled")
    public boolean subscriptionCancelled(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionCanceledUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @GetMapping("/subscription-paused")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionPausedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
            subscriptionContractDetailsList = subscriptionContractDetailsList.stream().filter(s -> s.getStatus().equals("paused")).collect(Collectors.toList());

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-paused")
    public boolean subscriptionPaused(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionPausedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-paused")
    public boolean subscriptionPaused(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionPausedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }

    @GetMapping("/subscription-activated")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionActivatedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
            subscriptionContractDetailsList = subscriptionContractDetailsList.stream().filter(s -> s.getStatus().equals("active")).collect(Collectors.toList());

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-activated")
    public boolean subscriptionActivated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionActivatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-activated")
    public boolean subscriptionActivated(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionActivatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/subscription-next-order-updated")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionNextOrderDateUpdated(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-next-order-updated")
    public boolean subscriptionNextOrderUpdated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierNextOrderUpdatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-next-order-updated")
    public boolean subscriptionNextOrderDateUpdate(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierNextOrderUpdatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/subscription-order-frequency-updated")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionOrderFrequencyUpdatedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-order-frequency-updated")
    public boolean subscriptionOrderFrequencyUpdated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierOrderFrequencyUpdatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-order-frequency-updated")
    public boolean subscriptionOrderFrequencyUpdated(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierOrderFrequencyUpdatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/subscription-shipping-address-updated")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionShippingAddressUpdatedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-shipping-address-updated")
    public boolean subscriptionShippingAddressUpdated(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierShippingAddressUpdatedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-shipping-address-updated")
    public boolean subscriptionShippingAddressUpdated(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierShippingAddressUpdatedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/subscription-product-added")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionProductAddedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-product-added")
    public boolean subscriptionProductAdded(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionProductAddedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-product-added")
    public boolean subscriptionProductAdded(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionProductAddedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/subscription-product-removed")
    public List<SubscriptionContractQuery.SubscriptionContract> subscriptionProductRemovedList(@RequestParam(value = "api_key", required = true) String apiKey) throws Exception {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            List<SubscriptionContractQuery.SubscriptionContract> result = new ArrayList<>();
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            String shop = shopInfo.getShop();
            log.info("shop=" + shop);

            List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

            int i = 0;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                if (i == 4) {
                    break;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalQueryResponse.getData().get().getSubscriptionContract().get();
                result.add(subscriptionContract);
                i++;
            }

            return result;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/subscription-product-removed")
    public boolean subscriptionProductRemoved(@RequestParam(value = "api_key", required = true) String apiKey) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);

        if (shopInfoOptional.isPresent()) {
            ShopInfoDTO shopInfo = shopInfoOptional.get();

            log.info("shop=" + shopInfo.getShop());

            shopInfo.setZapierSubscriptionProductRemovedUrl(null);
            shopInfoService.save(shopInfo);
        }
        return true;
    }

    @PostMapping("/subscription-product-removed")
    public boolean subscriptionProductRemoved(@RequestParam(value = "api_key", required = true) String apiKey, @RequestBody ZapierSubscriptionVM zapierSubscriptionVM) {
        log.info("apiKey=" + apiKey + " zapierSubscriptionVM" + zapierSubscriptionVM.toString());

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        if (shopInfoOptional.isPresent()) {
            log.info("shopInfo found. shop=" + shopInfoOptional.get().toString());
            ShopInfoDTO shopInfo = shopInfoOptional.get();
            shopInfo.setZapierSubscriptionProductRemovedUrl(zapierSubscriptionVM.getHookUrl());
            log.info("shopInfo=" + shopInfo.toString());
            shopInfoService.save(shopInfo);
            return true;
        } else {
            log.info("shopInfo not found");
            return false;
        }
    }


    @GetMapping("/me")
    public HashMap<String, Object> zapierTestAuthentication(@RequestParam(value = "api_key", required = true) String apiKey) {
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByApiKey(apiKey);
        HashMap<String, Object> response = new HashMap<>();
        response.put("shop", shopInfo.get().getShop());
        return response;
    }
}

package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.constant.Constants;
import com.et.domain.SubscriptionBundling;
import com.et.domain.SubscriptionGroupPlan;
import com.et.domain.enumeration.BuildABoxType;
import com.et.domain.enumeration.BuildBoxVersion;
import com.et.security.SecurityUtils;
import com.et.service.ShopInfoService;
import com.et.service.SocialConnectionService;
import com.et.service.SubscriptionBundlingService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.et.web.rest.vm.bundling.DiscountCodeRequest;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import com.et.web.rest.vm.bundling.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.DiscountCodeBasicCreateMutation;
import com.shopify.java.graphql.client.type.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionBundling}.
 */
@RestController
@Api(tags = "Subscription Bundling Resource")
public class SubscriptionBundlingResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundlingResource.class);

    private static final String ENTITY_NAME = "subscriptionBundling";

    @Autowired
    private CommonUtils commonUtils;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBundlingService subscriptionBundlingService;

    private final ShopInfoService shopInfoService;

    private final SubscriptionContractDetailsService subscriptionContractDetailsService;

    public SubscriptionBundlingResource(SubscriptionBundlingService subscriptionBundlingService, ShopInfoService shopInfoService, SubscriptionContractDetailsService subscriptionContractDetailsService) {
        this.subscriptionBundlingService = subscriptionBundlingService;
        this.shopInfoService = shopInfoService;
        this.subscriptionContractDetailsService = subscriptionContractDetailsService;
    }

    /**
     * {@code POST  /api/subscription-bundlings} : Create a new subscriptionBundling.
     *
     * @param subscriptionBundlingDTO the subscriptionBundlingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBundlingDTO, or with status {@code 400 (Bad Request)} if the subscriptionBundling has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-bundlings")
    public ResponseEntity<SubscriptionBundlingDTO> createSubscriptionBundling(@RequestBody SubscriptionBundlingDTO subscriptionBundlingDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionBundling : {}", subscriptionBundlingDTO);
        String shop = commonUtils.getShop();
        subscriptionBundlingDTO.setShop(shop);
        SubscriptionBundlingDTO result = subscriptionBundlingService.save(subscriptionBundlingDTO);
        return ResponseEntity.created(new URI("/api/subscription-bundlings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Build-A-Box added successfully.", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api/subscription-bundlings} : Updates an existing subscriptionBundling.
     *
     * @param subscriptionBundlingDTO the subscriptionBundlingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundlingDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundlingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundlingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/subscription-bundlings")
    public ResponseEntity<SubscriptionBundlingDTO> updateSubscriptionBundling(@RequestBody SubscriptionBundlingDTO subscriptionBundlingDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionBundling : {}", subscriptionBundlingDTO);
        if (subscriptionBundlingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = commonUtils.getShop();
        subscriptionBundlingDTO.setShop(shop);
        SubscriptionBundlingDTO result = subscriptionBundlingService.save(subscriptionBundlingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Build-A-Box updated successfully.", subscriptionBundlingDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/api/subscription-bundlings/changeBuildABoxStatus/{buildABoxId}/{status}")
    public ResponseEntity<SubscriptionBundlingDTO> changeBuildABoxStatus(@PathVariable Long buildABoxId, @PathVariable Boolean status) throws URISyntaxException {
        subscriptionBundlingService.changeBuildABoxStatus(buildABoxId, status);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Build-A-Box status updated successfully.", ""))
            .body(new SubscriptionBundlingDTO());
    }

    /**
     * {@code GET  /api/subscription-bundlings} : get all the subscriptionBundlings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionBundlings in body.
     */
    @GetMapping("/api/subscription-bundlings")
    public List<SubscriptionBundlingDTO> getAllSubscriptionBundlings() {
        log.debug("REST request to get all SubscriptionBundlings");
        return subscriptionBundlingService.findAllByShop(commonUtils.getShop());
    }

    /**
     * {@code GET  /api/subscription-bundlings/:subscriptionId} : get the "subscriptionId" subscriptionBundling.
     *
     * @param id the id of the subscriptionBundlingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBundlingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/subscription-bundlings/{id}")
    public ResponseEntity<SubscriptionBundlingDTO> getSubscriptionBundling(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionBundling : {}", id);
        Optional<SubscriptionBundlingDTO> subscriptionBundlingDTO = subscriptionBundlingService.findOne(id);

        if (subscriptionBundlingDTO.isPresent()) {
            if (!subscriptionBundlingDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionBundlingDTO);
    }


    @CrossOrigin
    @GetMapping(value = {"/api/subscription-bundlings/by-token/{token}", "/subscriptions/cp/api/subscription-bundlings/by-token/{token}"})
    public ResponseEntity<SubscriptionBundlingDTO> getSubscriptionBundling(@PathVariable String token) {
        log.debug("REST request to get SubscriptionBundling : {}", token);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionBundlingDTO> subscriptionBundlingDTO = subscriptionBundlingService.findOneByToken(shop, token);

        if (subscriptionBundlingDTO.isPresent()) {
            if (!subscriptionBundlingDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("", "", "");
            }
        }
        return ResponseUtil.wrapOrNotFound(subscriptionBundlingDTO);
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-bundlings/single-product", "/subscriptions/cp/api/subscription-bundlings/single-product"})
    public ResponseEntity<List<SubscriptionBundlingDTO>> findByShopAndBuildABoxTypeSingle() {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<SubscriptionBundlingDTO> subscriptionBundlingDTO = subscriptionBundlingService.findByShopAndBuildABoxType(shop, BuildABoxType.SINGLE_PRODUCT);
        return ResponseEntity.ok().body(subscriptionBundlingDTO);
    }

    /**
     * {@code DELETE  /api/subscription-bundlings/:id} : delete the "id" subscriptionBundling.
     *
     * @param id the id of the subscriptionBundlingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-bundlings/{id}")
    public ResponseEntity<Void> deleteSubscriptionBundling(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionBundling : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionBundlingDTO> one = subscriptionBundlingService.findOne(id);

        if (one.isPresent()) {
            if (!one.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("", "", "");
            }
            SubscriptionBundlingDTO bundlingDTO = one.get();
            if (bundlingDTO.getUniqueRef() != null) {
                Long countExist = subscriptionContractDetailsService.countAllByShopAndSubscriptionTypeIdentifier(shop, bundlingDTO.getUniqueRef());
                if (countExist != null && countExist > 0) {
                    throw new BadRequestAlertException("Deleting Build-A-Box is not possible, " + countExist + " subscriptions found. You may deactivate the Build-A-Box.", "", "");
                }
            }
        }

        subscriptionBundlingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Build-A-Box removed successfully.", "")).build();
    }

    @GetMapping(value = {"/api/subscription-bundlings/external/get-bundle/{handle}", "/subscriptions/bb/api/subscription-bundlings/external/get-bundle/{handle}"})
    @CrossOrigin
    public ResponseEntity<SubscriptionBundlingResponse> getValidSubscriptionCustomer(@PathVariable String handle, HttpServletRequest request) {
        log.debug("REST request to get public bundle detail : {}", handle);

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionBundlingResponse subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetails(shopName, handle);
        if (subscriptionBundlingResponse.getBundle() != null && !CollectionUtils.isEmpty(subscriptionBundlingResponse.getSubscriptions())) {
            return ResponseEntity.ok().body(subscriptionBundlingResponse);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/api/external/v2/subscription-bundlings/external/get-bundle/{handle}")
    @CrossOrigin
    @ApiOperation("Returns Valid Subscription Customer")
    public ResponseEntity<SubscriptionBundlingResponse> getValidSubscriptionCustomerV2(@ApiParam("Customer Handle") @PathVariable String handle, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) {
        log.debug("REST request to get public bundle detail : {}", handle);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        SubscriptionBundlingResponse subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetails(shop, handle);
        if (subscriptionBundlingResponse.getBundle() != null && !CollectionUtils.isEmpty(subscriptionBundlingResponse.getSubscriptions())) {
            return ResponseEntity.ok().body(subscriptionBundlingResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = {"/api/v3/subscription-bundlings/external/get-bundle/{handle}", "/subscriptions/bb/api/v3/subscription-bundlings/external/get-bundle/{handle}"})
    @CrossOrigin
    public ResponseEntity<SubscriptionBundlingResponseV3> getValidSubscriptionCustomerV2(@ApiParam("Customer Handle") @PathVariable String handle, HttpServletRequest request) {
        log.debug("REST request to get public bundle detail : {}", handle);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        SubscriptionBundlingResponseV3 subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetailsV3(shop, handle);
        if (subscriptionBundlingResponse.getBundle() != null && subscriptionBundlingResponse.getSubscription() != null) {
            return ResponseEntity.ok().body(subscriptionBundlingResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    private SocialConnectionService socialConnectionService;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @PutMapping(value = {"/api/subscription-bundlings/discount/{token}", "/subscriptions/bb/api/subscription-bundlings/discount/{token}"})
    @CrossOrigin
    public DiscountCodeResponse generateDiscount(
        @PathVariable String token,
        @RequestBody DiscountCodeRequest discountCodeRequest,
        HttpServletRequest request) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        return getDiscountCodeResponse(token, shopName, discountCodeRequest);
    }

    @PutMapping("/api/external/v2/subscription-bundlings/discount/{token}")
    @CrossOrigin
    @ApiOperation("Generate Discount")
    public DiscountCodeResponse generateDiscountV2(
        @ApiParam("Token") @PathVariable String token,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Discount Code Request") @RequestBody DiscountCodeRequest discountCodeRequest,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-bundlings/discount/{token} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getDiscountCodeResponse(token, shop, discountCodeRequest);
    }

    @Nullable
    private DiscountCodeResponse getDiscountCodeResponse(String token, String shop, DiscountCodeRequest discountCodeRequest) throws Exception {
        DiscountCodeResponse discountCodeResponse = new DiscountCodeResponse();

        SubscriptionBundlingResponse subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetails(shop, token);
        SubscriptionBundling bundle = subscriptionBundlingResponse.getBundle();

        TieredDiscountDTO applicableDiscount = new TieredDiscountDTO();

        long cartQuantity = discountCodeRequest.getCart().getItems().stream()
            .map(Item::getQuantity).mapToInt(Long::intValue).sum();

        Long totalCartPrice = discountCodeRequest.getCart().getTotalPrice();
        if (bundle != null && bundle.getTieredDiscount() != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<TieredDiscountDTO> tieredDiscount = mapper.readValue(bundle.getTieredDiscount(), new TypeReference<List<TieredDiscountDTO>>() {
            });

            Optional<TieredDiscountDTO> applicableQuantityBasedDiscountOptional = tieredDiscount.stream()
                .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("QUANTITY"))
                .filter(tieredDiscountDTO -> cartQuantity >= tieredDiscountDTO.getQuantity())
                .reduce((first, second) -> {
                    if (first.getDiscount() > second.getDiscount()) {
                        return first;
                    } else {
                        return second;
                    }
                });

            Optional<TieredDiscountDTO> applicableSpendAmountBasedDiscountOptional = tieredDiscount.stream()
                .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("AMOUNT"))
                .filter(tieredDiscountDTO -> discountCodeRequest.getCart().getTotalPrice() / 100 >= tieredDiscountDTO.getQuantity())
                .reduce((first, second) -> {
                    if (first.getDiscount() > second.getDiscount()) {
                        return first;
                    } else {
                        return second;
                    }
                });

            if (applicableQuantityBasedDiscountOptional.isPresent() && applicableSpendAmountBasedDiscountOptional.isPresent()) {
                TieredDiscountDTO quantityBased = applicableQuantityBasedDiscountOptional.get();
                TieredDiscountDTO amountBased = applicableSpendAmountBasedDiscountOptional.get();
                if (quantityBased.getDiscount() > amountBased.getDiscount()) {
                    applicableDiscount = quantityBased;
                } else {
                    applicableDiscount = amountBased;
                }
            } else if (applicableQuantityBasedDiscountOptional.isPresent()) {
                applicableDiscount = applicableQuantityBasedDiscountOptional.get();
            } else if (applicableSpendAmountBasedDiscountOptional.isPresent()) {
                applicableDiscount = applicableSpendAmountBasedDiscountOptional.get();
            }
        }


        List<SubscriptionGroupPlan> subscriptions = subscriptionBundlingResponse.getSubscriptions();
        if (bundle != null && !CollectionUtils.isEmpty(subscriptions) && (applicableDiscount.getDiscount() != null || bundle.getDiscount() != null)) {

            List<SubscribedProductVariantInfo> products = new ArrayList<>();
            List<SubscribedProductVariantInfo> variants = new ArrayList<>();

            for(SubscriptionGroupPlan sgp: subscriptions) {
                SubscriptionGroupV2DTO subscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                }, sgp.getInfoJson());

                List<SubscribedProductVariantInfo> productsJSON = CommonUtils.fromJSON(new TypeReference<>() {
                }, subscriptionGroupDTO.getProductIds());
                products.addAll(Optional.ofNullable(productsJSON).orElse(new ArrayList<>()));

                List<SubscribedProductVariantInfo> variantsJSON = CommonUtils.fromJSON(new TypeReference<>() {
                }, subscriptionGroupDTO.getVariantIds());
                variants.addAll(Optional.ofNullable(variantsJSON).orElse(new ArrayList<>()));
            }


            Set<Long> cartProductIds = discountCodeRequest.getCart()
                .getItems()
                .stream()
                .map(Item::getProductId)
                .collect(Collectors.toSet());

            Set<Long> cartVariantIds = discountCodeRequest.getCart()
                .getItems()
                .stream()
                .map(Item::getVariantId)
                .collect(Collectors.toSet());

            Map<Long, Set<Long>> variantIdsByProductIds = new HashMap<>();

            for (Item item : discountCodeRequest.getCart().getItems()) {
                Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(item.getProductId(), new HashSet<>());
                productVariantIds.add(item.getVariantId());
                variantIdsByProductIds.put(item.getProductId(), productVariantIds);
            }

            List<String> productsToAdd = products.stream().filter(p -> {
                    boolean found = cartProductIds.contains(p.getId());

                    if (found) {
                        Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(p.getId(), new HashSet<>());
                        cartVariantIds.removeAll(productVariantIds);
                    }

                    return found;
                }).map(p -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + p.getId())
                .collect(Collectors.toList());

            List<String> variantsToAdd = variants.stream().filter(v -> {
                    return cartVariantIds.contains(v.getId());
                }).map(p -> ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + p.getId())
                .collect(Collectors.toList());

            DiscountProductsInput discountProductsInput = DiscountProductsInput
                .builder()
                .productsToAdd(productsToAdd)
                .productVariantsToAdd(variantsToAdd)
                .build();

            DiscountItemsInput discountItemsInput = DiscountItemsInput
                .builder()
                .products(discountProductsInput)
                .build();

            double discountPercentage = subscriptionBundlingService.prepareDiscountAmount(shop, bundle, applicableDiscount, totalCartPrice.doubleValue(), cartQuantity) / 100;
            if (discountPercentage > 0) {
                DiscountCustomerGetsInput discountCustomerGetsInput = DiscountCustomerGetsInput
                    .builder()
                    .appliesOnSubscription(true)
                    .appliesOnOneTimePurchase(false)
                    .items(discountItemsInput)
                    .value(
                        DiscountCustomerGetsValueInput.builder()
                            .percentage(discountPercentage)
                            .build()
                    )//if applicable discount not found the default discount will be applied
                    .build();

                DiscountCustomerSelectionInput discountCustomerSelectionInput = DiscountCustomerSelectionInput
                    .builder()
                    .all(true)
                    .build();

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
                String formattedDate = dateTimeFormatter.format(ZonedDateTime.now());

                DiscountMinimumRequirementInput.Builder discountMinimumRequirementInputBuilder = DiscountMinimumRequirementInput.builder();


                if (applicableDiscount.getDiscount() != null) {
                    if (applicableDiscount.getDiscountBasedOn().equals("QUANTITY")) {
                        DiscountMinimumQuantityInput discountMinimumQuantityInput =
                            DiscountMinimumQuantityInput
                                .builder()
                                .greaterThanOrEqualToQuantity(Optional.of(applicableDiscount.getQuantity().toString()).orElse("1"))
                                .build();
                        discountMinimumRequirementInputBuilder.quantity(discountMinimumQuantityInput);
                    }
                    if (applicableDiscount.getDiscountBasedOn().equals("AMOUNT")) {
                        DiscountMinimumSubtotalInput discountMinimumSubtotalInput =
                            DiscountMinimumSubtotalInput
                                .builder()
                                .greaterThanOrEqualToSubtotal(Double.valueOf(Optional.ofNullable(applicableDiscount.getQuantity()).orElse(1).toString()))
                                .build();
                        discountMinimumRequirementInputBuilder.subtotal(discountMinimumSubtotalInput);
                    }
                } else {
                    Optional<ShopInfoDTO> shopOption = shopInfoService.findByShop(shop);
                    if (shopOption.isPresent() && shopOption.get().getBuildBoxVersion() == BuildBoxVersion.V1) {
                        if (bundle.getMinProductCount() != null) {
                            DiscountMinimumQuantityInput discountMinimumQuantityInput =
                                DiscountMinimumQuantityInput
                                    .builder()
                                    .greaterThanOrEqualToQuantity(Optional.of(bundle.getMinProductCount().toString()).orElse("1"))
                                    .build();
                            discountMinimumRequirementInputBuilder.quantity(discountMinimumQuantityInput);
                        }

                        if (bundle.getMinOrderAmount() != null) {
                            DiscountMinimumSubtotalInput discountMinimumSubtotalInput =
                                DiscountMinimumSubtotalInput
                                    .builder()
                                    .greaterThanOrEqualToSubtotal(Double.valueOf(Optional.ofNullable(bundle.getMinOrderAmount()).orElse(0.0).toString()))
                                    .build();
                            discountMinimumRequirementInputBuilder.subtotal(discountMinimumSubtotalInput);
                        }
                    }
                }


                DiscountCodeBasicInput discountCodeBasicInput = DiscountCodeBasicInput
                    .builder()
                    .code("BUILD_A_BOX_DISCOUNT_" + RandomStringUtils.randomAlphabetic(10) + "_" + bundle.getUniqueRef())
                    .customerGets(discountCustomerGetsInput)
                    .title("BUILD_A_BOX_DISCOUNT" + "_" + bundle.getUniqueRef())
                    .minimumRequirement(discountMinimumRequirementInputBuilder.build())
                    .customerSelection(discountCustomerSelectionInput)
                    .recurringCycleLimit(Integer.MAX_VALUE)
                    .startsAt(formattedDate)
                    .build();

                DiscountCodeBasicCreateMutation discountCodeBasicCreateMutation = new DiscountCodeBasicCreateMutation(discountCodeBasicInput);

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                Response<Optional<DiscountCodeBasicCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(discountCodeBasicCreateMutation);

                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("errors=" + optionalMutationResponse.getErrors().get(0).getMessage() + " shop=" + shop);
                    return null;
                }

                List<DiscountCodeBasicCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                    .map(d -> d.getDiscountCodeBasicCreate().map(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getUserErrors)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.info("errors=" + userErrors.toString() + " shop=" + shop);
                    return null;
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

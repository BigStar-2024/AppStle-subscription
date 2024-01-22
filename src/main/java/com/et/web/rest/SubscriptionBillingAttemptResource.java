package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.SubscriptionContractDetails;
import com.et.domain.User;
import com.et.domain.enumeration.*;
import com.et.pojo.SubscriptionBillingAttemptDetails;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.repository.UserRepository;
import com.et.security.SecurityUtils;
import com.et.service.OrderInfoService;
import com.et.service.SubscriptionBillingAttemptQueryService;
import com.et.service.SubscriptionBillingAttemptService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.service.dto.OrderInfoDTO;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import com.et.service.dto.SubscriptionContractDetailsDTO;
import com.et.service.mapper.SubscriptionContractDetailsMapper;
import com.et.utils.CommonEmailUtils;
import com.et.utils.CommonUtils;
import com.et.utils.SubscriptionBillingAttemptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.VariantQuantity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.*;
import org.apache.commons.lang3.BooleanUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.et.constant.Constants.DATE_TIME_STAMP_FORMAT;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionBillingAttempt}.
 */
@RestController
@Api(tags = "Subscription Billing Attempt Resource")
public class SubscriptionBillingAttemptResource {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(SubscriptionBillingAttemptResource.class);

    private static final String ENTITY_NAME = "subscriptionBillingAttempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SubscriptionContractDetailsMapper subscriptionContractDetailsMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CommonEmailUtils commonEmailUtils;

    @Autowired
    private SubscriptionBillingAttemptUtils subscriptionBillingAttemptUtils;

    public SubscriptionBillingAttemptResource(SubscriptionBillingAttemptService subscriptionBillingAttemptService) {
        this.subscriptionBillingAttemptService = subscriptionBillingAttemptService;
    }

    /**
     * {@code POST  /api/subscription-billing-attempts} : Create a new subscriptionBillingAttempt.
     *
     * @param subscriptionBillingAttemptDTO the subscriptionBillingAttemptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBillingAttemptDTO, or with status {@code 400 (Bad Request)} if the subscriptionBillingAttempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-billing-attempts")
    public ResponseEntity<SubscriptionBillingAttemptDTO> createSubscriptionBillingAttempt(@Valid @RequestBody SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO, HttpServletRequest request) throws URISyntaxException {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to save SubscriptionBillingAttempt : {}", RequestURL, subscriptionBillingAttemptDTO);
        if (subscriptionBillingAttemptDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionBillingAttempt cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionBillingAttemptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionBillingAttemptDTO result = subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);
        return ResponseEntity.created(new URI("/api/subscription-billing-attempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api/subscription-billing-attempts} : Updates an existing subscriptionBillingAttempt.
     *
     * @param subscriptionBillingAttemptDTO the subscriptionBillingAttemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBillingAttemptDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBillingAttemptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBillingAttemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @CrossOrigin
    @PutMapping(value = {"/api/subscription-billing-attempts", "/subscriptions/cp/api/subscription-billing-attempts"})
    public ResponseEntity<SubscriptionBillingAttemptDTO> updateSubscriptionBillingAttempt(@Valid @RequestBody SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO, HttpServletRequest request) throws URISyntaxException, JsonProcessingException {
        boolean isExternal = commonUtils.isExternal();
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to update SubscriptionBillingAttempt : {} ", RequestURL, subscriptionBillingAttemptDTO);
        if (subscriptionBillingAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptRepository.findById(subscriptionBillingAttemptDTO.getId()).get();

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionBillingAttemptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (subscriptionBillingAttempt.getBillingDate() != subscriptionBillingAttemptDTO.getBillingDate() && isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(subscriptionBillingAttempt.getContractId(), subscriptionBillingAttempt.getShop());
        }

        SubscriptionBillingAttemptDTO result = subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);

        if((!subscriptionBillingAttempt.getBillingDate().withZoneSameInstant(ZoneId.of("UTC")).equals(subscriptionBillingAttemptDTO.getBillingDate())) || (subscriptionBillingAttempt.getOrderNote() != null ? !subscriptionBillingAttempt.getOrderNote().equals(subscriptionBillingAttemptDTO.getOrderNote()) : StringUtils.hasText(subscriptionBillingAttemptDTO.getOrderNote()))) {
            Map<String, Object> map = new HashMap<>();
            map.put("oldOrderNote", subscriptionBillingAttempt.getOrderNote());
            map.put("newOrderNote", subscriptionBillingAttemptDTO.getOrderNote());
            map.put("oldBillingDate", subscriptionBillingAttempt.getBillingDate().withZoneSameInstant(ZoneId.of("UTC")).toString());
            map.put("newBillingDate", subscriptionBillingAttemptDTO.getBillingDate().toString());

            commonUtils.writeActivityLog(shop, subscriptionBillingAttempt.getContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL, ActivityLogEventType.UPCOMING_ORDER_UPDATED, ActivityLogStatus.SUCCESS, map);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Billing Attempt Updated.", ""))
            .body(result);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-billing-attempts")
    @ApiOperation("Update Subscription Billing Attempt")
    public ResponseEntity<SubscriptionBillingAttemptDTO> updateSubscriptionBillingAttemptV2(@Valid @ApiParam("Subscription Billing Attempt Data Transfer Model") @RequestBody SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO, HttpServletRequest request, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey) throws URISyntaxException, JsonProcessingException {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to update SubscriptionBillingAttempt : {} ", RequestURL, subscriptionBillingAttemptDTO);

        if (subscriptionBillingAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptRepository.findById(subscriptionBillingAttemptDTO.getId()).get();


        SubscriptionBillingAttemptDTO result = subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);

        if((!subscriptionBillingAttempt.getBillingDate().withZoneSameInstant(ZoneId.of("UTC")).equals(subscriptionBillingAttemptDTO.getBillingDate())) || (subscriptionBillingAttempt.getOrderNote() != null ? !subscriptionBillingAttempt.getOrderNote().equals(subscriptionBillingAttemptDTO.getOrderNote()) : StringUtils.hasText(subscriptionBillingAttemptDTO.getOrderNote()))) {
            Map<String, Object> map = new HashMap<>();
            map.put("oldOrderNote", subscriptionBillingAttempt.getOrderNote());
            map.put("newOrderNote", subscriptionBillingAttemptDTO.getOrderNote());
            map.put("oldBillingDate", subscriptionBillingAttempt.getBillingDate().withZoneSameInstant(ZoneId.of("UTC")).toString());
            map.put("newBillingDate", subscriptionBillingAttemptDTO.getBillingDate().toString());

            commonUtils.writeActivityLog(subscriptionBillingAttemptDTO.getShop(), subscriptionBillingAttempt.getContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.MERCHANT_EXTERNAL_API, ActivityLogEventType.UPCOMING_ORDER_UPDATED, ActivityLogStatus.SUCCESS, map);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Billing Attempt Updated.", ""))
            .body(result);
    }

    /**
     * {@code GET  /api/subscription-billing-attempts} : get all the subscriptionBillingAttempts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionBillingAttempts in body.
     */
    /*@GetMapping("/api/subscription-billing-attempts")
    public List<SubscriptionBillingAttemptDTO> getAllSubscriptionBillingAttempts() {
        log.debug("REST request to get all SubscriptionBillingAttempts");
        return subscriptionBillingAttemptService.findAll();
    }*/

    /**
     * {@code GET  /api/subscription-billing-attempts/:id} : get the "id" subscriptionBillingAttempt.
     *
     * @param id the id of the subscriptionBillingAttemptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBillingAttemptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/subscription-billing-attempts/{id}")
    public ResponseEntity<SubscriptionBillingAttemptDTO> getSubscriptionBillingAttempt(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionBillingAttempt : {}", id);
        Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (subscriptionBillingAttemptDTO.isPresent()) {
            if (!shop.equals(subscriptionBillingAttemptDTO.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionBillingAttemptDTO);
    }

    /**
     * {@code DELETE  /api/subscription-billing-attempts/:id} : delete the "id" subscriptionBillingAttempt.
     *
     * @param id the id of the subscriptionBillingAttemptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-billing-attempts/{id}")
    public ResponseEntity<Void> deleteSubscriptionBillingAttempt(@PathVariable Long id, HttpServletRequest request) {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request to delete SubscriptionBillingAttempt : {} and shop : {}", RequestURL, id, shop);

        Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findById(id);

        if (subscriptionBillingAttemptDTO.isPresent()) {
            if (!shop.equals(subscriptionBillingAttemptDTO.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        subscriptionBillingAttemptService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * Endpoint to get top orders based on shop/ contractId/ customerId
     *
     * @param shopName   shopName
     * @param contractId contractId
     * @param customerId customerId
     * @return list of SubscriptionBillingAttemptDTO
     */
    @GetMapping(value = {"/api/subscription-billing-attempts/top-orders", "/subscriptions/cp/api/subscription-billing-attempts/top-orders"})
    @CrossOrigin
    public ResponseEntity<List<SubscriptionBillingAttemptDTO>> getTopOrders(@RequestParam(value = "shop", required = false) String shopName,
                                                                            @RequestParam(value = "contractId", required = false) Long contractId,
                                                                            @RequestParam(value = "customerId", required = false) Long customerId,
                                                                            @RequestParam(value = "customerUid", required = false) String customerUid,
                                                                            HttpServletRequest request) {

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-billing-attempts/top-orders, shop: {}", shopName);
            throw new BadRequestAlertException("", "", "");
        }

        return getListOfTopOrder(shopName, contractId, customerId, customerUid);
    }

    @GetMapping("/api/external/v2/subscription-billing-attempts/top-orders")
    @CrossOrigin
    @ApiOperation("Get Top Orders")
    public ResponseEntity<List<SubscriptionBillingAttemptDTO>> getTopOrdersV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                              @ApiParam("Contract ID") @RequestParam(value = "contractId", required = false) Long contractId,
                                                                              @ApiParam("Customer ID") @RequestParam(value = "customerId", required = false) Long customerId,
                                                                              @ApiParam("Customer UID") @RequestParam(value = "customerUid", required = false) String customerUid,
                                                                              HttpServletRequest request) {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-billing-attempts/top-orders api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getListOfTopOrder(shop, contractId, customerId, customerUid);
    }

    @NotNull
    private ResponseEntity<List<SubscriptionBillingAttemptDTO>> getListOfTopOrder(String shop, Long contractId, Long customerId, String customerUid) {
        customerId = commonUtils.getCustomerIdFromRequestParams(customerId, customerUid);

        if (customerId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        log.debug("REST request to get TopOrders by shop {}, contractId: {}, customerId: {}", shop, contractId, customerId);
        if (!StringUtils.isEmpty(shop) && (StringUtils.isEmpty(contractId) && StringUtils.isEmpty(customerId))) {
            return ResponseEntity.ok().body(subscriptionBillingAttemptService.getTopOrdersByShop(shop));
        } else if (!StringUtils.isEmpty(contractId) && !StringUtils.isEmpty(shop)) {
            return ResponseEntity.ok().body(subscriptionBillingAttemptService.getTopOrdersByContractId(contractId, shop));
        } else if (!StringUtils.isEmpty(customerId) && !StringUtils.isEmpty(shop)) {
            return ResponseEntity.ok().body(subscriptionBillingAttemptService.getTopOrdersByCustomerId(customerId, shop));
        } else
            return ResponseEntity.badRequest().body(null);
    }

    /**
     * Endpoint to get failed orders based on shop/ contractId/ customerId
     *
     * @param shop       shopName
     * @param contractId contractId
     * @param customerId customerId
     * @return list of SubscriptionBillingAttemptDTO
     */
    @GetMapping(value = {"/api/subscription-billing-attempts/past-orders", "/subscriptions/cp/api/subscription-billing-attempts/past-orders"})
    @CrossOrigin
    public ResponseEntity<List<SubscriptionBillingAttemptDTO>> getPastOrders(@RequestParam(value = "shop", required = false) String shop,
                                                                             @RequestParam(value = "contractId", required = false) Long contractId,
                                                                             @RequestParam(value = "customerId", required = false) Long customerId,
                                                                             @RequestParam(value = "customerUid", required = false) String customerUid,
                                                                             Pageable pageable,
                                                                             HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-billing-attempts/past-orders RequestURL: {}, shop: {}", RequestURL, shop);
            throw new BadRequestAlertException("", "", "");
        }

        return getListOfPastOrder(pageable, shop, contractId, customerId, customerUid);
    }

    @GetMapping("/api/external/v2/subscription-billing-attempts/past-orders")
    @CrossOrigin
    @ApiOperation("Get Past Order")
    public ResponseEntity<List<SubscriptionBillingAttemptDTO>> getPastOrdersV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                               @ApiParam("Billing Attempt ID") @RequestParam(value = "contractId", required = false) Long contractId,
                                                                               @ApiParam("Customer ID") @RequestParam(value = "customerId", required = false) Long customerId,
                                                                               @ApiParam("Customer UID") @RequestParam(value = "customerUid", required = false) String customerUid,
                                                                               Pageable pageable,
                                                                               HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        //log.info("REST request v2 RequestURL: {} /external/v2/subscription-billing-attempts/past-orders api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getListOfPastOrder(pageable, shop, contractId, customerId, customerUid);
    }

    @NotNull
    private ResponseEntity<List<SubscriptionBillingAttemptDTO>> getListOfPastOrder(Pageable pageable, String shop, Long contractId, Long customerId, String customerUid) throws Exception {
        customerId = commonUtils.getCustomerIdFromRequestParams(customerId, customerUid);

        Page<SubscriptionBillingAttemptDTO> result = null;

//        if (ObjectUtils.allNotNull(contractId, customerId)) {
//            return ResponseEntity.ok().body(Collections.EMPTY_LIST);
//        }

        log.debug("REST request to get FailedOrders by shop {}, contractId: {}, customerId: {}", shop, contractId, customerId);
        if (!StringUtils.isEmpty(shop) && (StringUtils.isEmpty(contractId) && StringUtils.isEmpty(customerId))) {
            result = subscriptionBillingAttemptService.getPastOrdersByShop(pageable, shop);
        } else if (!StringUtils.isEmpty(contractId) && !StringUtils.isEmpty(shop)) {
            result = subscriptionBillingAttemptService.getPastOrdersByContractId(pageable, contractId, shop);
        } else if (!StringUtils.isEmpty(customerId) && !StringUtils.isEmpty(shop)) {
            result = subscriptionBillingAttemptService.getPastOrdersByCustomerId(pageable, customerId, shop);
        }

        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = new ArrayList<>();
        if (result != null && !CollectionUtils.isEmpty(result.getContent())) {
            subscriptionBillingAttemptDTOList = result.getContent();
            List<OrderInfoDTO> newOrderInfoDTOList = new ArrayList<>();

            List<Long> orderIdList = result.get().map(SubscriptionBillingAttemptDTO::getOrderId).filter(Objects::nonNull).collect(Collectors.toList());
            List<String> gqlOrderIdList = result.get().map(SubscriptionBillingAttemptDTO::getGraphOrderId).filter(Objects::nonNull).collect(Collectors.toList());

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            OrdersStatusQuery ordersStatusQuery = new OrdersStatusQuery(gqlOrderIdList);
            Response<Optional<OrdersStatusQuery.Data>> ordersStatusResponse = shopifyGraphqlClient.getOptionalQueryResponse(ordersStatusQuery);
            List<OrdersStatusQuery.AsOrder> orderNodes = Objects.requireNonNull(ordersStatusResponse.getData())
                .map(OrdersStatusQuery.Data::getNodes).orElse(new ArrayList<>()).stream().filter(Objects::nonNull).map(node -> ((OrdersStatusQuery.AsOrder) node)).collect(Collectors.toList());

            List<OrderInfoDTO> orderInfoDTOList = orderInfoService.findByShopAndOrderIdIn(shop, orderIdList);
            for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : subscriptionBillingAttemptDTOList) {
                List<VariantQuantity> variantQuantityList = new ArrayList<>();
                OrderInfoDTO orderInfoDTO = orderInfoDTOList.stream().filter(dto -> dto.getOrderId().equals(subscriptionBillingAttemptDTO.getOrderId())).findFirst().orElse(new OrderInfoDTO());
                try {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(orderInfoDTO.getLinesJson())) {
                        List<VariantQuantity> variantQuantities = commonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<List<VariantQuantity>>() {
                        }, orderInfoDTO.getLinesJson());
                        variantQuantityList.addAll(variantQuantities);
                    } else if (subscriptionBillingAttemptDTO.getGraphOrderId() != null) {
                        OrderLineItemsQuery orderLineItemsQuery = new OrderLineItemsQuery(subscriptionBillingAttemptDTO.getGraphOrderId());
                        Response<Optional<OrderLineItemsQuery.Data>> orderLineItemsResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderLineItemsQuery);
                        List<OrderLineItemsQuery.Edge> lineItemsEdges = Objects.requireNonNull(orderLineItemsResponse.getData())
                            .map(d -> d.getOrder().map(OrderLineItemsQuery.Order::getLineItems)
                                .map(OrderLineItemsQuery.LineItems::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>());

                        variantQuantityList = lineItemsEdges.stream().map(OrderLineItemsQuery.Edge::getNode).map(n -> {
                            long variantId = Long.parseLong(n.getVariant().map(OrderLineItemsQuery.Variant::getId).orElse("").replace(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX, ""));
                            VariantQuantity variantQuantity = new VariantQuantity(variantId, n.getQuantity());
                            variantQuantity.setImage(n.getImage().map(OrderLineItemsQuery.Image::getTransformedSrc).map(Object::toString).orElse(null));
                            variantQuantity.setProductId(n.getProduct().map(OrderLineItemsQuery.Product::getId).orElse(null));
                            variantQuantity.setProductTitle(n.getTitle());
                            variantQuantity.setTitle(n.getTitle());
                            variantQuantity.setVariantTitle(n.getVariantTitle().orElse(""));
                            return variantQuantity;
                        }).collect(Collectors.toList());

                        //Save lineItems info in DB to avoid API calls to shopify in future
                        orderInfoDTO.setShop(subscriptionBillingAttemptDTO.getShop());
                        orderInfoDTO.setOrderId(subscriptionBillingAttemptDTO.getOrderId());
                        orderInfoDTO.setLinesJson(OBJECT_MAPPER.writeValueAsString(variantQuantityList));
                        newOrderInfoDTOList.add(orderInfoDTO);

                    }
                } catch (Exception ex) {
                    log.error("Some error occurred while getting past order's line items. Message: {}", ex.getMessage());
                }
                subscriptionBillingAttemptDTO.setVariantList(variantQuantityList);

                Optional<OrdersStatusQuery.AsOrder> orderNode = orderNodes.stream().filter(node -> node.getId().equals(subscriptionBillingAttemptDTO.getGraphOrderId())).findFirst();

                if(orderNode.isPresent()){
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));
                    subscriptionBillingAttemptDTO.setOrderCancelReason(orderNode.get().getCancelReason().orElse(null));
                    String cancelledAt = orderNode.get().getCancelledAt().map(Object::toString).orElse(null);
                    if(Objects.nonNull(cancelledAt)) {
                        subscriptionBillingAttemptDTO.setOrderCancelledAt(ZonedDateTime.parse(cancelledAt, dateTimeFormatter));
                    }
                    subscriptionBillingAttemptDTO.setOrderClosed(orderNode.get().isClosed());
                    String closedAt = orderNode.get().getClosedAt().map(Object::toString).orElse(null);
                    if(Objects.nonNull(closedAt)) {
                        subscriptionBillingAttemptDTO.setOrderClosedAt(ZonedDateTime.parse(closedAt, dateTimeFormatter));
                    }
                    subscriptionBillingAttemptDTO.setOrderConfirmed(orderNode.get().isConfirmed());
                    subscriptionBillingAttemptDTO.setOrderDisplayFinancialStatus(orderNode.get().getDisplayFinancialStatus().orElse(null));
                    subscriptionBillingAttemptDTO.setOrderDisplayFulfillmentStatus(orderNode.get().getDisplayFulfillmentStatus());
                    if(Objects.nonNull(orderNode.get().getProcessedAt())) {
                        subscriptionBillingAttemptDTO.setOrderProcessedAt(ZonedDateTime.parse(orderNode.get().getProcessedAt().toString(), dateTimeFormatter));
                    }
                }
            }

            if(commonUtils.newApproachFor(shop, "hideArchivedOrders")) {
                subscriptionBillingAttemptDTOList = subscriptionBillingAttemptDTOList.stream().filter(order -> BooleanUtils.isNotTrue(order.getOrderClosed())).collect(Collectors.toList());

            }

            if (!CollectionUtils.isEmpty(newOrderInfoDTOList)) {
                try {
                    orderInfoService.saveAll(newOrderInfoDTOList);
                } catch (Exception ex) {
                    log.error("Some error occurred while getting past order's line items. Message: {}", ex.getMessage());
                }
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(subscriptionBillingAttemptDTOList);
    }


    @PutMapping(value = {"/api/subscription-billing-attempts/skip-order/{id}", "/subscriptions/cp/api/subscription-billing-attempts/skip-order/{id}"})
    @CrossOrigin
    public ResponseEntity<SubscriptionBillingAttemptDTO> skipOrder(HttpServletRequest request,
                                                                   @PathVariable Long id,
                                                                   @RequestParam(value = "shop", required = false) String shopName,
                                                                   @RequestParam(value = "subscriptionContractId", required = false) Long subscriptionContractId,
                                                                   @RequestParam(value = "isPrepaid", defaultValue = "false") boolean isPrepaid) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-billing-attempts/skip-order/{id} RequestURL: {}, shop: {}", RequestURL, shopName);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(subscriptionContractId);

        if(isExternal) {
            commonUtils.checkAttributeBasedMinCycles(shopName, subscriptionContractId);
        }

        ResponseEntity<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOResponseEntity = subscriptionBillingAttemptSkipOrder(id, shopName, isPrepaid, subscriptionContractId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);

        return subscriptionBillingAttemptDTOResponseEntity;
    }


    @PutMapping("/api/external/v2/subscription-billing-attempts/skip-order/{id}")
    @CrossOrigin
    @ApiOperation("Skip Upcoming Order")
    public ResponseEntity<SubscriptionBillingAttemptDTO> skipOrderV2(HttpServletRequest request,
                                                                     @ApiParam("Billing Attempt ID") @PathVariable Long id,
                                                                     @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                     @ApiParam("Subscription Contract ID") @RequestParam(value = "subscriptionContractId", required = false) Long subscriptionContractId,
                                                                     @ApiParam("Is this contract Prepaid?") @RequestParam(value = "isPrepaid", defaultValue = "false") boolean isPrepaid) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-billing-attempts/skip-order/{id} api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to skip order for shop {}, id: {}, prepaid:{}", shop, id, isPrepaid);
        log.info(RequestURL + " Request URL for calling API /external/v2/subscription-billing-attempts/skip-order for skip subscription order of id : " + id + " with shop : " + shop + " and isPrepaid : " + isPrepaid);
        ResponseEntity<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOResponseEntity = subscriptionBillingAttemptSkipOrder(id, shop, isPrepaid, subscriptionContractId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        return subscriptionBillingAttemptDTOResponseEntity;
    }

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    @Nullable
    private ResponseEntity<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptSkipOrder(Long id, String shop, boolean isPrepaid, Long subscriptionContractId, ActivityLogEventSource eventSource) throws Exception {

        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOOptional = subscriptionBillingAttemptService.findOne(id);

        if (subscriptionBillingAttemptDTOOptional.isPresent()) {
            if (!shop.equals(subscriptionBillingAttemptDTOOptional.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        if (subscriptionBillingAttemptDTOOptional.isPresent()) {
            if(ActivityLogEventSource.CUSTOMER_PORTAL.equals(eventSource)) {
                commonUtils.checkIsFreezeOrderTillMinCycleCondition(subscriptionBillingAttemptDTOOptional.get().getContractId(), shop);
            }
        }


        if (subscriptionBillingAttemptDTOOptional.isPresent()) {
            log.info("Trying to skip upcoming order with contractId=" + subscriptionBillingAttemptDTOOptional.get().getContractId() + " shop=" + subscriptionBillingAttemptDTOOptional.get().getShop() + " id=" + subscriptionBillingAttemptDTOOptional.get().getId());
        }

        if (!isPrepaid) {
            Optional<SubscriptionBillingAttemptDTO> updatedSubscriptionBillingAttemptDTOOptional = subscriptionBillingAttemptService.skipOrder(shop, id);

            if (updatedSubscriptionBillingAttemptDTOOptional.isPresent()) {
                SubscriptionBillingAttemptDTO updatedSubscriptionBillingAttemptDTO = updatedSubscriptionBillingAttemptDTOOptional.get();
                subscriptionBillingAttemptUtils.updateSubscriptionContractNextBillingDate(updatedSubscriptionBillingAttemptDTO.getContractId());
                SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(updatedSubscriptionBillingAttemptDTO.getContractId()).get();
                commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles(), id);

                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("id", updatedSubscriptionBillingAttemptDTOOptional.get().getId());
                additionalInfo.put("orderDate", updatedSubscriptionBillingAttemptDTOOptional.get().getBillingDate().toString());
                additionalInfo.put("skipType", "OrderSkipped");
                commonUtils.writeActivityLog(shop, updatedSubscriptionBillingAttemptDTOOptional.get().getContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.BILLING_ATTEMPT_SKIPPED, ActivityLogStatus.SUCCESS, additionalInfo);

                commonEmailUtils.sendSubscriptionUpdateEmail(
                    commonUtils.prepareShopifyGraphqlClient(shop),
                    commonUtils.prepareShopifyResClient(shop),
                    subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractDetailsDTO.getSubscriptionContractId()).get(),
                    EmailSettingType.ORDER_SKIPPED);
            }

            ResponseEntity<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOResponseEntity = updatedSubscriptionBillingAttemptDTOOptional.map(billingAttemptDTO -> ResponseEntity.ok().body(billingAttemptDTO)).orElseGet(() -> ResponseEntity.badRequest().body(null));

            return subscriptionBillingAttemptDTOResponseEntity;
        } else {

            String graphQLId = ShopifyIdPrefix.FULFILLMENT_ORDER_ID_PREFIX + id;

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            FulfillmentOrderQuery fulfillmentOrderQuery = new FulfillmentOrderQuery(graphQLId);

            Response<Optional<FulfillmentOrderQuery.Data>> fulfillmentOrderQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(fulfillmentOrderQuery);

            String fulFillAt = Objects.requireNonNull(fulfillmentOrderQueryResponse.getData())
                .flatMap(d -> d.getFulfillmentOrder()
                    .flatMap(e -> e.getFulfillAt().map(Object::toString)))
                .orElse(null);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));
            ZonedDateTime fulFillAtDate = ZonedDateTime.parse(fulFillAt, dateTimeFormatter);

            List<FulfillmentOrderQuery.Node> orderFulfillments = Objects.requireNonNull(fulfillmentOrderQueryResponse.getData())
                .map(g -> g.getFulfillmentOrder()
                    .map(FulfillmentOrderQuery.FulfillmentOrder::getOrder)
                    .map(FulfillmentOrderQuery.Order::getFulfillmentOrders)
                    .map(j -> j.getEdges().stream().map(FulfillmentOrderQuery.Edge::getNode).collect(Collectors.toList()))
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            Optional<ZonedDateTime> last = orderFulfillments.stream()
                .filter(n -> n.getFulfillAt().isPresent())
                .map(p -> ZonedDateTime.parse(p.getFulfillAt().get().toString(), dateTimeFormatter))
                .filter(o -> o.isAfter(fulFillAtDate)).max(ZonedDateTime::compareTo);

            String graphOrderId = fulfillmentOrderQueryResponse
                .getData()
                .flatMap(d -> d.getFulfillmentOrder()
                    .map(FulfillmentOrderQuery.FulfillmentOrder::getOrder)
                    .map(FulfillmentOrderQuery.Order::getId))
                .orElse(null);

            Optional<SubscriptionContractDetails> subscriptionContractDetailsOptional = Optional.empty();
            if (subscriptionContractId != null) {
                subscriptionContractDetailsOptional = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId);
            } else {
                subscriptionContractDetailsOptional = subscriptionContractDetailsRepository.findByShopAndGraphOrderId(shop, graphOrderId);
            }

            if (subscriptionContractDetailsOptional.isEmpty()) {
                return null;
            }

            SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsOptional.get();


            ZonedDateTime lastFulfillmentDate = last.orElse(fulFillAtDate);
            String deliveryPolicyInterval = subscriptionContractDetails.getDeliveryPolicyInterval();
            Integer deliveryPolicyIntervalCount = subscriptionContractDetails.getDeliveryPolicyIntervalCount();

            ZonedDateTime newDate = commonUtils.nextIntervalDate(lastFulfillmentDate, deliveryPolicyInterval, deliveryPolicyIntervalCount);

            String formattedDate = newDate.format(dateTimeFormatter);
            FulfillmentOrderRescheduleMutation fulfillmentOrderRescheduleMutation = new FulfillmentOrderRescheduleMutation(graphQLId, formattedDate);
            Response<Optional<FulfillmentOrderRescheduleMutation.Data>> optionalFulfillmentOrderRescheduleMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(fulfillmentOrderRescheduleMutation);


            if (!CollectionUtils.isEmpty(optionalFulfillmentOrderRescheduleMutationResponse.getErrors())) {
                throw new BadRequestAlertException(optionalFulfillmentOrderRescheduleMutationResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<FulfillmentOrderRescheduleMutation.UserError> fulfillmentOrderRescheduleUserErrors = Objects.requireNonNull(optionalFulfillmentOrderRescheduleMutationResponse.getData())
                .map(d -> d.getFulfillmentOrderReschedule()
                    .map(FulfillmentOrderRescheduleMutation.FulfillmentOrderReschedule::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            if (!fulfillmentOrderRescheduleUserErrors.isEmpty()) {
                throw new BadRequestAlertException(fulfillmentOrderRescheduleUserErrors.get(0).getMessage(), "", "");
            }

            ZonedDateTime nextRenewalDate = commonUtils.nextIntervalDate(newDate, deliveryPolicyInterval, deliveryPolicyIntervalCount);
            String nextRenewalFormattedDate = nextRenewalDate.format(dateTimeFormatter);
            SubscriptionContractSetNextBillingDateMutation subscriptionContractSetNextBillingDateMutation = new SubscriptionContractSetNextBillingDateMutation(subscriptionContractDetails.getGraphSubscriptionContractId(), nextRenewalFormattedDate);
            Response<Optional<SubscriptionContractSetNextBillingDateMutation.Data>> optionalSubscriptionContractSetNextBillingDateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractSetNextBillingDateMutation);

            if (!CollectionUtils.isEmpty(optionalSubscriptionContractSetNextBillingDateMutationResponse.getErrors())) {
                throw new BadRequestAlertException(optionalSubscriptionContractSetNextBillingDateMutationResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionContractSetNextBillingDateMutation.UserError> subscriptionContractSetNextBillingDateMutationUserErrors = Objects.requireNonNull(optionalSubscriptionContractSetNextBillingDateMutationResponse.getData())
                .map(d -> d.getSubscriptionContractSetNextBillingDate()
                    .map(SubscriptionContractSetNextBillingDateMutation.SubscriptionContractSetNextBillingDate::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            if (!subscriptionContractSetNextBillingDateMutationUserErrors.isEmpty()) {
                throw new BadRequestAlertException(subscriptionContractSetNextBillingDateMutationUserErrors.get(0).getMessage(), "", "");
            }


            subscriptionContractDetails.setNextBillingDate(nextRenewalDate);
            subscriptionContractDetailsRepository.save(subscriptionContractDetails);

            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsMapper.toDto(subscriptionContractDetails);

            Optional<Long> nextBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(subscriptionContractId);

            subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, subscriptionContractId);

            subscriptionBillingAttemptService.deleteByContractIdAndStatusAndBillingDateAfter(subscriptionContractId, BillingAttemptStatus.SKIPPED, nextRenewalDate);

            commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles(), nextBillingId.orElse(null));

            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("id", id);
            additionalInfo.put("fulfilDate", fulFillAtDate.toString());
            additionalInfo.put("skipType", "FulfillmentSkipped");
            commonUtils.writeActivityLog(shop, subscriptionContractDetails.getSubscriptionContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.BILLING_ATTEMPT_SKIPPED, ActivityLogStatus.SUCCESS, additionalInfo);

            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                commonUtils.prepareShopifyResClient(shop),
                subscriptionContractDetails,
                EmailSettingType.ORDER_SKIPPED);

            return null;
        }
    }


    @PutMapping(value = {"/api/subscription-billing-attempts/attempt-billing/{id}", "/subscriptions/cp/api/subscription-billing-attempts/attempt-billing/{id}"})
    @CrossOrigin
    public void attemptBilling(@PathVariable Long id,
                               HttpServletRequest request) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        boolean isExternal = commonUtils.isExternal();
        Optional<SubscriptionBillingAttemptDTO> updatedSubscriptionBillingAttemptDTO = subscriptionBillingAttemptService.attemptBilling(shop, id, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @PutMapping("/api/external/v2/subscription-billing-attempts/attempt-billing/{id}")
    @CrossOrigin
    @ApiOperation("Attempt Billing")
    public void attemptBillingV2(@ApiParam("Billing Attempt ID") @PathVariable Long id,
                                 @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                 HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-billing-attempts/attempt-billing/{id} api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request for calling  API /external/v2/attempt-billing of id : {} and shop : {}", RequestURL, id, shop);
        Optional<SubscriptionBillingAttemptDTO> updatedSubscriptionBillingAttemptDTO = subscriptionBillingAttemptService.attemptBilling(shop, id, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
    }

    @CrossOrigin
    @PutMapping("/api/subscription-billing-attempts-update-order-note/{id}")
    public ResponseEntity<Boolean> updateOrderNote(
        HttpServletRequest request,
        @PathVariable("id") Long id,
        @RequestParam("orderNote") String orderNote) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findById(id).get();

        commonUtils.validateCustomerRequestForContract(subscriptionBillingAttemptDTO.getContractId());

        return subscriptionBillingAttemptOrderNote(id, shopName, orderNote);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-billing-attempts-update-order-note/{id}")
    @ApiOperation("Update Order Note")
    public ResponseEntity<Boolean> updateOrderNoteV2(
        HttpServletRequest request,
        @ApiParam("Billing Attempt ID") @PathVariable("id") Long id,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Updated Order Note") @RequestParam("orderNote") String orderNote) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-billing-attempts-update-order-note/{id} api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info(RequestURL + " Request URL for calling API /external/v2/subscription-billing-attempts-update-order-note to update order note of shop : " + shop + " orderNote : " + orderNote + " Id :" + id);
        return subscriptionBillingAttemptOrderNote(id, shop, orderNote);
    }

    private ResponseEntity<Boolean> subscriptionBillingAttemptOrderNote(Long id, String shop, String orderNote) {
        SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findById(id).get();

        if (!shop.equals(subscriptionBillingAttemptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        subscriptionBillingAttemptDTO.setOrderNote(orderNote);
        subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);
        HttpHeaders httpHeaders = HeaderUtil.createAlert(applicationName, "Order Note updated successfully.", "");
        return ResponseUtil.wrapOrNotFound(Optional.of(true), httpHeaders);
    }


    /**
     * Endpoint to get failed orders based on /emailId / contractId
     *
     * @param emailId
     * @param contractId contractId
     * @return list of SubscriptionBillingAttemptDTO
     */
    @GetMapping("/api/subscription-billing-attempts/past-orders/export")
    public ResponseEntity<Void> exportPastOrdersToCSV(@RequestParam(value = "emailId", required = true) String emailId,
                                                      @RequestParam(value = "status", required = true) String status,
                                                      @RequestParam(value = "contractId", required = false) Long contractId) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get FailedOrders by shop {}, contractId: {}, customerId: {}", shop, contractId);
        Optional<User> user = userRepository.findOneByLogin(shop);
        if (status.equalsIgnoreCase(BillingAttemptStatus.SUCCESS.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportSuccessSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.FAILURE.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportFailedSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.QUEUED.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportUpcomingSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportSkippedSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED_DUNNING_MGMT.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportSkippedByDunningMgmtSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED_INVENTORY_MGMT.toString())) {
            CompletableFuture.runAsync(() -> subscriptionBillingAttemptService.exportSkippedByInventoryMgmtSubscriptionBillingAttempt(shop, user.get(), emailId, contractId));
        } else {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert(applicationName, "Invalid " + status + " status is passed.", "")).build();
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + emailId, "")).build();
    }

    /**
     * Endpoint to get failed orders based on / contractId
     *
     * @param contractId contractId
     * @return list of SubscriptionBillingAttemptDTO
     */
    @GetMapping("/api/subscription-billing-attempts/past-orders/report")
    public ResponseEntity<List<SubscriptionBillingAttemptDetails>> getPastOrdersReport(@RequestParam(value = "contractId", required = false) Long contractId,
                                                                                   @RequestParam(value = "status", required = true) String status,
                                                                                   Pageable pageable) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get FailedOrders by shop {}, contractId: {}, customerId: {}", shop, contractId);
        Page<SubscriptionBillingAttemptDetails> page = null;
        if (status.equalsIgnoreCase(BillingAttemptStatus.SUCCESS.toString())) {
            page = subscriptionBillingAttemptService.getPastOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.SUCCESS);
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.FAILURE.toString())) {
            page = subscriptionBillingAttemptService.getPastOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.FAILURE);
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.QUEUED.toString())) {
            page = subscriptionBillingAttemptService.getUpcomingOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.QUEUED);
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED.toString())) {
            page = subscriptionBillingAttemptService.getPastOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.SKIPPED);
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED_DUNNING_MGMT.toString())) {
            page = subscriptionBillingAttemptService.getPastOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.SKIPPED_DUNNING_MGMT);
        } else if (status.equalsIgnoreCase(BillingAttemptStatus.SKIPPED_INVENTORY_MGMT.toString())) {
            page = subscriptionBillingAttemptService.getPastOrdersDetailsByShop(pageable, shop, contractId, BillingAttemptStatus.SKIPPED_INVENTORY_MGMT);
        } else {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert(applicationName, "Invalid " + status + " status is passed.", "")).build();
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/api/subscription-billing-attempts/{id}/retry-needed/{isRetryNeeded}")
    public ResponseEntity<Void> retryNeededForSubscriptionBillingAttempt(@PathVariable Long id,
                                                                         @PathVariable boolean isRetryNeeded,
                                                                         HttpServletRequest request) throws URISyntaxException {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        log.info("{} REST request for calling API /retry-needed to update retry billing needed flag of billingId : {} and shop : {} and isRetryNeeded : {}", RequestURL, id, shop, isRetryNeeded);
        SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptRepository.findById(id).get();

        Boolean oldIsRetryNeeded = subscriptionBillingAttempt.isRetryingNeeded();

        if (!shop.equals(subscriptionBillingAttempt.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if(!oldIsRetryNeeded.equals(isRetryNeeded)) {

            subscriptionBillingAttempt.setRetryingNeeded(isRetryNeeded);
            subscriptionBillingAttemptRepository.save(subscriptionBillingAttempt);

            Map<String, Object> map = new HashMap<>();
            map.put("oldIsRetryNeeded", oldIsRetryNeeded);
            map.put("newIsRetryNeeded", isRetryNeeded);
            map.put("reason", "Retry needed updated");

            commonUtils.writeActivityLog(shop, subscriptionBillingAttempt.getContractId(), ActivityLogEntityType.SUBSCRIPTION_BILLING_ATTEMPT, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL, ActivityLogEventType.BILLING_ATTEMPT_UPDATED, ActivityLogStatus.SUCCESS, map);

        }
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Update retry billing needed flag for billing Id " + id, "")).build();
    }

    @Autowired
    private SubscriptionBillingAttemptQueryService subscriptionBillingAttemptQueryService;

    /*@GetMapping("/api/subscription-billing-attempts-with-filter")
    public ResponseEntity<List<SubscriptionBillingAttemptDTO>> getAllSubscriptionBillingAttempts(SubscriptionBillingAttemptCriteria criteria) {
        log.debug("REST request to get SubscriptionBillingAttempts by criteria: {}", criteria);
        List<SubscriptionBillingAttemptDTO> entityList = subscriptionBillingAttemptQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }*/

}

package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.domain.ProductSwap;
import com.et.domain.SocialConnection;
import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.enumeration.BillingAttemptStatus;
import com.et.repository.ProductSwapRepository;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.security.SecurityUtils;
import com.et.service.ProductSwapService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.ProductSwapDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.VariantQuantity;
import com.et.web.rest.vm.VariantQuantityInfo;
import com.shopify.java.graphql.client.queries.ProductVariantQuery;
import liquibase.repackaged.org.apache.commons.lang3.ObjectUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.ProductSwap}.
 */
@RestController
@Api(tags = "Product Swap Resource")
public class ProductSwapResource {

    private final Logger log = LoggerFactory.getLogger(ProductSwapResource.class);

    private static final String ENTITY_NAME = "productSwap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductSwapService productSwapService;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    public ProductSwapResource(ProductSwapService productSwapService) {
        this.productSwapService = productSwapService;
    }

    /**
     * {@code POST  /api/product-swaps} : Create a new productSwap.
     *
     * @param productSwapDTO the productSwapDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productSwapDTO, or with status {@code 400 (Bad Request)} if the productSwap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/product-swaps")
    public ResponseEntity<ProductSwapDTO> createProductSwap(@Valid @RequestBody ProductSwapDTO productSwapDTO) throws URISyntaxException {
        log.debug("REST request to save ProductSwap : {}", productSwapDTO);
        if (productSwapDTO.getId() != null) {
            throw new BadRequestAlertException("A new productSwap cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productSwapDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductSwapDTO result = productSwapService.save(productSwapDTO);
        return ResponseEntity.created(new URI("/api/product-swaps/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "New Product Swap Automation Created.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /api/product-swaps} : Updates an existing productSwap.
     *
     * @param productSwapDTO the productSwapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSwapDTO,
     * or with status {@code 400 (Bad Request)} if the productSwapDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productSwapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/product-swaps")
    public ResponseEntity<ProductSwapDTO> updateProductSwap(@Valid @RequestBody ProductSwapDTO productSwapDTO) throws URISyntaxException {
        log.debug("REST request to update ProductSwap : {}", productSwapDTO);
        if (productSwapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productSwapDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductSwapDTO result = productSwapService.save(productSwapDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Product Swap Automation Updated.", ""))
            .body(result);
    }

    /**
     * {@code GET  /api/product-swaps} : get all the productSwaps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productSwaps in body.
     */
    @GetMapping("/api/product-swaps")
    public List<ProductSwapDTO> getAllProductSwaps() {
        log.debug("REST request to get all ProductSwaps");
        return productSwapService.findByShop(SecurityUtils.getCurrentUserLogin().get());
    }

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ProductSwapRepository productSwapRepository;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @PostMapping(value = {"/api/product-swaps-by-variant-groups/{contractId}", "/subscriptions/cp/api/product-swaps-by-variant-groups/{contractId}"})
    @CrossOrigin
    public List<List<VariantQuantity>> getProductSwapsByVariantId(@PathVariable Long contractId, @RequestBody VariantQuantityInfo variantQuantityInfo, HttpServletRequest request) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        return getProductSwapByShopAndVariantQuantity(shopName, variantQuantityInfo, contractId);
    }

    @PostMapping("/api/external/v2/product-swaps-by-variant-groups/{contractId}")
    @CrossOrigin
    @ApiOperation("Product Swap Automation Resource")
    public List<List<VariantQuantity>> getProductSwapsByVariantIdV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, @ApiParam("Variant Quantity View Model") @RequestBody VariantQuantityInfo variantQuantityInfo, @PathVariable Long contractId, HttpServletRequest request) throws Exception {
        log.debug("REST request to get all ProductSwaps");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/product-swaps-by-variant-groups api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getProductSwapByShopAndVariantQuantity(shop, variantQuantityInfo, contractId);
    }

    @NotNull
    private List<List<VariantQuantity>> getProductSwapByShopAndVariantQuantity(String shop, VariantQuantityInfo variantQuantityInfo, Long contractId) throws Exception {
        List<ProductSwap> productSwaps = productSwapRepository.findByShop(shop);

        if (productSwaps.isEmpty()) {
            return new ArrayList<>();
        }

        int totalCycles = 1;
        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatusIn(shop, contractId, Arrays.asList(BillingAttemptStatus.SUCCESS, BillingAttemptStatus.IMMEDIATE_TRIGGERED));
        totalCycles = totalCycles + subscriptionBillingAttempts.size();

        List<List<VariantQuantity>> variantQuantityLists = new ArrayList<>();
        /*List<VariantQuantity> itemList = new ArrayList<>();

        for (VariantQuantity variantQuantity : variantQuantityInfo.getVariantQuantityList()) {
            itemList.add(new VariantQuantity(variantQuantity.getVariantId(), variantQuantity.getQuantity()));
        }

       variantQuantityLists.add(0, itemList);*/

        List<VariantQuantity> variantQuantityList = variantQuantityInfo.getVariantQuantityList();
        for (int i = 0; i < 5; i++) {
            variantQuantityList = commonUtils.getProductsToBeSwapped(productSwaps, variantQuantityList, totalCycles);
            variantQuantityLists.add(variantQuantityList);
            totalCycles++;
        }

        Set<Long> variantIds = variantQuantityLists.stream()
            .flatMap(Collection::stream)
            .map(VariantQuantity::getVariantId)
            .collect(Collectors.toSet());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Map<Long, ProductVariantQuery.ProductVariant> map = new HashMap<>();

        for (Long variantId : variantIds) {
            ProductVariantQuery productVariantQuery = new ProductVariantQuery(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId);
            Response<Optional<ProductVariantQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            Objects.requireNonNull(optionalQueryResponse.getData())
                .flatMap(ProductVariantQuery.Data::getProductVariant)
                .map(variant -> map.put(variantId, variant));
        }

        for (List<VariantQuantity> variantQuantities : variantQuantityLists) {
            for (VariantQuantity variantQuantity : variantQuantities) {
                ProductVariantQuery.ProductVariant productVariant = map.get(variantQuantity.getVariantId());
                if(ObjectUtils.isNotEmpty(productVariant)) {
                    variantQuantity.setTitle(productVariant.getTitle());
                    variantQuantity.setProductTitle(productVariant.getProduct().getTitle());
                    variantQuantity.setProductId(productVariant.getProduct().getId());
                    variantQuantity.setImage(productVariant.getImage().map(i -> i.getTransformedSrc().toString()).orElse(productVariant.getProduct().getFeaturedImage().map(i -> i.getTransformedSrc().toString()).orElse("")));
                }
            }
        }

        return variantQuantityLists;
    }

    /**
     * {@code GET  /api/product-swaps/:id} : get the "id" productSwap.
     *
     * @param id the id of the productSwapDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productSwapDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/product-swaps/{id}")
    public ResponseEntity<ProductSwapDTO> getProductSwap(@PathVariable Long id) {
        log.debug("REST request to get ProductSwap : {}", id);
        Optional<ProductSwapDTO> productSwapDTO = productSwapService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (productSwapDTO.isPresent()) {
            if (!productSwapDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(productSwapDTO);
    }

    /**
     * {@code DELETE  /api/product-swaps/:id} : delete the "id" productSwap.
     *
     * @param id the id of the productSwapDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/product-swaps/{id}")
    public ResponseEntity<Void> deleteProductSwap(@PathVariable Long id) {
        log.debug("REST request to delete ProductSwap : {}", id);
        Optional<ProductSwapDTO> productSwapDTO = productSwapService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (productSwapDTO.isPresent()) {
            if (!productSwapDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }
        productSwapService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Product Swap Automation Deleted.", "")).build();
    }
}

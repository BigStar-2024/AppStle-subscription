package com.et.web.rest;

import com.et.api.constants.ShopifyIdPrefix;
import com.et.security.SecurityUtils;
import com.et.service.CustomerPaymentService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.CustomerPaymentDTO;

import com.et.web.rest.vm.CustomerTokenInfo;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.CustomerPayment}.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Customer Payment Resource")
public class CustomerPaymentResource {

    private final Logger log = LoggerFactory.getLogger(CustomerPaymentResource.class);

    private static final String ENTITY_NAME = "customerPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerPaymentService customerPaymentService;

    @Autowired
    private CommonUtils commonUtils;

    public CustomerPaymentResource(CustomerPaymentService customerPaymentService) {
        this.customerPaymentService = customerPaymentService;
    }

    /**
     * {@code POST  /customer-payments} : Create a new customerPayment.
     *
     * @param customerPaymentDTO the customerPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerPaymentDTO, or with status {@code 400 (Bad Request)} if the customerPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-payments")
    public ResponseEntity<CustomerPaymentDTO> createCustomerPayment(@Valid @RequestBody CustomerPaymentDTO customerPaymentDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerPayment : {}", customerPaymentDTO);
        if (customerPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPaymentDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CustomerPaymentDTO result = customerPaymentService.save(customerPaymentDTO);
        return ResponseEntity.created(new URI("/api/customer-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-payments} : Updates an existing customerPayment.
     *
     * @param customerPaymentDTO the customerPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the customerPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-payments")
    public ResponseEntity<CustomerPaymentDTO> updateCustomerPayment(@Valid @RequestBody CustomerPaymentDTO customerPaymentDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerPayment : {}", customerPaymentDTO);
        if (customerPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPaymentDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CustomerPaymentDTO result = customerPaymentService.save(customerPaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerPaymentDTO.getId().toString()))
            .body(result);
    }


    /**
     * {@code GET  /customer-payments/:id} : get the "id" customerPayment.
     *
     * @param id the id of the customerPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-payments/{id}")
    public ResponseEntity<CustomerPaymentDTO> getCustomerPayment(@PathVariable Long id) {
        log.debug("REST request to get CustomerPayment : {}", id);
        Optional<CustomerPaymentDTO> customerPaymentDTO = customerPaymentService.findOne(id);

        String shop = commonUtils.getShop();
        if (customerPaymentDTO.isPresent()) {
            if (!customerPaymentDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(customerPaymentDTO);
    }


    /*@GetMapping("/customer-payments/token/{customerId}")
    @CrossOrigin
    public CustomerTokenInfo getCustomerToken(@PathVariable Long customerId, @RequestParam(value = "shop", required = false) String shop, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", customerId);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v1 RequestURL: {} /customer-payments/token/{customerId} shop: {}", RequestURL, shop);

        if (shop == null) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        }

        return getCustomerTokenInfo(customerId, shop);
    }

    @GetMapping("/external/v2/customer-payments/token/{customerId}")
    @ApiOperation("Get Customer Payment Token")
    @CrossOrigin
    public CustomerTokenInfo getCustomerTokenV2(@ApiParam("Your Customer's Id") @PathVariable Long customerId, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", customerId);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/customer-payments/token/{customerId} api_key: {}", RequestURL, apiKey);

        Optional<ShopInfoDTO> shopInfoDTO = commonUtils.getShopInfoByAPIKey(apiKey);
        if (shopInfoDTO.isPresent()) {
            return getCustomerTokenInfo(customerId, shopInfoDTO.get().getShop());
        } else {
            throw new BadRequestAlertException("Invalid API key.", ENTITY_NAME, "invalidAPIKeyForGetCustomerTokenV2");
        }
    }*/


    /**
     * {@code DELETE  /customer-payments/:id} : delete the "id" customerPayment.
     *
     * @param id the id of the customerPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-payments/{id}")
    public ResponseEntity<Void> deleteCustomerPayment(@PathVariable Long id) {
        log.debug("REST request to delete CustomerPayment : {}", id);
        Optional<CustomerPaymentDTO> one = customerPaymentService.findOne(id);

        String shop = commonUtils.getShop();
        if (one.isPresent()) {
            if (!one.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }


        customerPaymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

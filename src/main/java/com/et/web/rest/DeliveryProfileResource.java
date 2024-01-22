package com.et.web.rest;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.security.SecurityUtils;
import com.et.service.DeliveryProfileService;
import com.et.service.SocialConnectionService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.DeliveryProfileDTO;

import com.et.web.rest.vm.*;
import com.et.web.rest.vm.LocationInfo;
import com.et.web.rest.vm.shippingprofile.*;
import com.et.web.rest.vm.shippingprofile.CountryInfo;
import com.et.web.rest.vm.shippingprofile.CreateShippingProfileRequestV2;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.DeliveryProfile}.
 */
@RestController
@Api(tags = "Delivery Profile Resource")
public class DeliveryProfileResource {

    public static final String FREE_SHIPPING_PROFILE_NAME = "Appstle Subscriptions Free Shipping";
    private final Logger log = LoggerFactory.getLogger(DeliveryProfileResource.class);

    private static final String ENTITY_NAME = "deliveryProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryProfileService deliveryProfileService;

    @Autowired
    private CommonUtils commonUtils;

    public DeliveryProfileResource(DeliveryProfileService deliveryProfileService) {
        this.deliveryProfileService = deliveryProfileService;
    }

    @PutMapping("/api/delivery-profiles")
    public ResponseEntity<DeliveryProfileDTO> updateDeliveryProfile(@RequestBody CreateShippingProfileRequestV2 createShippingProfileRequestVM) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        updateShippingProfileV2Info(createShippingProfileRequestVM, shop);
        return null;
    }

    @PutMapping("/api/delivery-profiles/v3")
    public ResponseEntity<CreateShippingProfileRequestV3> updateDeliveryProfileV3(@RequestBody CreateShippingProfileRequestV3 createShippingProfileRequestVM) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.updateShippingProfileV3Info(createShippingProfileRequestVM, shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Shipping profile updated", ""))
            .body(createShippingProfileRequestV3);
    }

    @PutMapping("/api/delivery-profiles/v4")
    public ResponseEntity<CreateShippingProfileRequestV3> updateDeliveryProfileV4(@RequestBody CreateShippingProfileRequestV3 createShippingProfileRequestVM) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.updateShippingProfileV4Info(createShippingProfileRequestVM, shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Shipping profile updated", ""))
            .body(createShippingProfileRequestV3);
    }


    private void updateShippingProfileV2Info(CreateShippingProfileRequestV2 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        for (com.et.web.rest.vm.shippingprofile.LocationInfo locationInfo : createShippingProfileRequestVM.getLocationInfos()) {
            for (CountryInfo countryInfo : locationInfo.getCountryInfos()) {
                for (DeliveryMethodInfo deliveryMethodInfo : countryInfo.getDeliveryMethodInfo()) {
                    deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
                }
            }
        }

        updateShippingProfileInternal(createShippingProfileRequestVM, shop);
    }

    private void updateShippingProfileInternal(CreateShippingProfileRequestV2 createShippingProfileRequestVM, String shop) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        DeliveryProfileInput deliveryProfileInput = buildDeliveryProfileInput(createShippingProfileRequestVM);
        DeliveryProfileUpdateMutation deliveryProfileUpdateMutation = new DeliveryProfileUpdateMutation(createShippingProfileRequestVM.getId(), deliveryProfileInput);

        Response<Optional<DeliveryProfileUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileUpdate()
                .map(DeliveryProfileUpdateMutation.DeliveryProfileUpdate::getUserErrors).orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());


        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        String deliveryProfileId = optionalMutationResponse.getData()
            .flatMap(d -> d.getDeliveryProfileUpdate().flatMap(e -> e.getProfile().map(DeliveryProfileUpdateMutation.Profile::getId)))
            .orElse(null);
    }

    private List<DeliveryCountryInput> getDeliveryCountryInputs(List<CountryInfo> countryInfos) {
        List<DeliveryCountryInput> countries = new ArrayList<>();

        countryInfos.forEach(countryInfo -> {
            DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
            if (countryInfo.getRestOfWorld()) {
                deliveryCountryInputBuilder.restOfWorld(true);
                DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
                countries.add(deliveryCountryInput);
            } else {
                deliveryCountryInputBuilder.code(CountryCode.safeValueOf(countryInfo.getCode()));
                if (BooleanUtils.isTrue(countryInfo.getShouldIncludeAllProvince())) {
                    deliveryCountryInputBuilder.includeAllProvinces(true);
                } else if (!StringUtils.isEmpty(countryInfo.getProvinceCode())) {
                    List<String> provinceCodeInfoList = Arrays.asList(countryInfo.getProvinceCode().split(","));

                    List<DeliveryProvinceInput> provinceInputs = new ArrayList<>();

                    provinceCodeInfoList.stream().forEach(p -> {
                        DeliveryProvinceInput.Builder deliveryProvinceInputBuilder = DeliveryProvinceInput.builder();
                        deliveryProvinceInputBuilder.code(p);
                        DeliveryProvinceInput deliveryProvinceInput = deliveryProvinceInputBuilder.build();
                        provinceInputs.add(deliveryProvinceInput);
                    });

                    deliveryCountryInputBuilder.provinces(provinceInputs);
                }
                DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
                countries.add(deliveryCountryInput);
            }
        });
        return countries;
    }

    @NotNull
    private List<DeliveryCountryInput> getDeliveryCountryInputs(@RequestBody CreateShippingProfileRequestVM createShippingProfileRequestVM) {
        List<DeliveryCountryInput> countries = new ArrayList<>();

        if (createShippingProfileRequestVM.isRestOfWorld()) {
            DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
            deliveryCountryInputBuilder.restOfWorld(true);
            DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
            countries.add(deliveryCountryInput);
        } else if (createShippingProfileRequestVM.getCountryInfos() != null) {
            createShippingProfileRequestVM.getCountryInfos().forEach(countryInfo -> {
                DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
                deliveryCountryInputBuilder.code(CountryCode.safeValueOf(countryInfo.getCode()));
                if (countryInfo.isShouldIncludeAllProvince()) {
                    deliveryCountryInputBuilder.includeAllProvinces(true);
                } else if (countryInfo.getProvinceCode() != null) {
                    List<String> provinceCodeInfoList = Arrays.asList(countryInfo.getProvinceCode().split(","));

                    List<DeliveryProvinceInput> provinceInputs = new ArrayList<>();

                    provinceCodeInfoList.stream().forEach(p -> {
                        DeliveryProvinceInput.Builder deliveryProvinceInputBuilder = DeliveryProvinceInput.builder();
                        deliveryProvinceInputBuilder.code(p);
                        DeliveryProvinceInput deliveryProvinceInput = deliveryProvinceInputBuilder.build();
                        provinceInputs.add(deliveryProvinceInput);
                    });

                    deliveryCountryInputBuilder.provinces(provinceInputs);
                }
                DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
                countries.add(deliveryCountryInput);
            });
        }
        return countries;
    }

    @Autowired
    private SocialConnectionService socialConnectionService;

    @PostMapping("/api/delivery-profiles/create-free-shipping")
    public ResponseEntity<DeliveryProfileDTO> createFreeShipping() throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        List<DeliveryMethodInfo> deliveryMethodInfos = new ArrayList<>();
        DeliveryMethodInfo deliveryMethodInfo = new DeliveryMethodInfo();
        deliveryMethodInfo.setAmount(0d);
        deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
        deliveryMethodInfo.setName("Free Shipping");
        deliveryMethodInfos.add(deliveryMethodInfo);

        return createShippingProfileInternal(FREE_SHIPPING_PROFILE_NAME, deliveryMethodInfos, new ArrayList<>(), buildCountries(), SecurityUtils.getCurrentUserLogin().get());
    }

    @PostMapping("/api/delivery-profiles/v2/create-shipping-profile")
    @CrossOrigin
    public ResponseEntity<DeliveryProfileDTO> createShippingProfileV2(@RequestBody CreateShippingProfileRequestV2 createShippingProfileRequestVM, HttpServletRequest request) throws Exception {
        String shopName = SecurityUtils.getCurrentUserLogin().get();
        return createShippingProfileV2Info(createShippingProfileRequestVM, shopName);
    }

    @PostMapping("/api/external/v2/delivery-profiles/v2/create-shipping-profile")
    @CrossOrigin
    @ApiOperation("Create Shipping Profile Externally")
    public ResponseEntity<DeliveryProfileDTO> createShippingProfileV2External(@ApiParam("Shipping Profile Request View Modelf") @RequestBody CreateShippingProfileRequestV2 createShippingProfileRequestVM, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/delivery-profiles/v2/create-shipping-profile api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return createShippingProfileV2Info(createShippingProfileRequestVM, shop);
    }

    @PostMapping("/api/delivery-profiles/v4/create-shipping-profile")
    @CrossOrigin
    public ResponseEntity<CreateShippingProfileRequestV3> createShippingProfileV4(@RequestBody CreateShippingProfileRequestV3 createShippingProfileRequestVM, HttpServletRequest request) throws Exception {
        String shopName = SecurityUtils.getCurrentUserLogin().get();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.createShippingProfileV4Info(createShippingProfileRequestVM, shopName);

      return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Shipping Profile has been successfully created.", ""))
            .body(createShippingProfileRequestV3);
    }

    @PostMapping("/api/delivery-profiles/v3/create-shipping-profile")
    @CrossOrigin
    public ResponseEntity<CreateShippingProfileRequestV3> createShippingProfileV3(@RequestBody CreateShippingProfileRequestV3 createShippingProfileRequestVM, HttpServletRequest request) throws Exception {
        String shopName = SecurityUtils.getCurrentUserLogin().get();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.createShippingProfileV3Info(createShippingProfileRequestVM, shopName);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, createShippingProfileRequestV3.getId()))
            .body(createShippingProfileRequestV3);
    }

    @PostMapping("/api/external/v2/delivery-profiles/v3/create-shipping-profile")
    @CrossOrigin
    @ApiOperation("Create Shipping Profile Externally")
    public ResponseEntity<CreateShippingProfileRequestV3> createShippingProfileV3External(@ApiParam("Shipping Profile Request View Model") @RequestBody CreateShippingProfileRequestV3 createShippingProfileRequestVM, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/delivery-profiles/v2/create-shipping-profile api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.createShippingProfileV3Info(createShippingProfileRequestVM, shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, createShippingProfileRequestV3.getId()))
            .body(createShippingProfileRequestV3);
    }

    @NotNull
    private ResponseEntity<DeliveryProfileDTO> createShippingProfileV2Info(CreateShippingProfileRequestV2 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        for (com.et.web.rest.vm.shippingprofile.LocationInfo locationInfo : createShippingProfileRequestVM.getLocationInfos()) {
            for (CountryInfo countryInfo : locationInfo.getCountryInfos()) {
                for (DeliveryMethodInfo deliveryMethodInfo : countryInfo.getDeliveryMethodInfo()) {
                    deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
                }
            }
        }

        return createShippingProfileInternal(createShippingProfileRequestVM, shop);
    }

    @PostMapping("/api/delivery-profiles/create-shipping-profile")
    @CrossOrigin
    public ResponseEntity<DeliveryProfileDTO> createShippingProfile(@RequestBody CreateShippingProfileRequestVM createShippingProfileRequestVM, HttpServletRequest request) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        return createShippingProfileInfo(createShippingProfileRequestVM, shopName);
    }

    @PostMapping("/api/external/v2/delivery-profiles/create-shipping-profile")
    @CrossOrigin
    @ApiOperation("Create Shipping Profile")
    public ResponseEntity<DeliveryProfileDTO> createShippingProfileV2(@ApiParam("Shipping Profile Request View Model") @RequestBody CreateShippingProfileRequestVM createShippingProfileRequestVM, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/delivery-profiles/create-shipping-profile api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return createShippingProfileInfo(createShippingProfileRequestVM, shop);
    }

    @NotNull
    private ResponseEntity<DeliveryProfileDTO> createShippingProfileInfo(CreateShippingProfileRequestVM createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        for (DeliveryMethodInfo deliveryMethodInfo : createShippingProfileRequestVM.getDeliveryMethodInfo()) {
            deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
        }

        List<DeliveryCountryInput> countries = getDeliveryCountryInputs(createShippingProfileRequestVM);

        return createShippingProfileInternal(createShippingProfileRequestVM.getName(), createShippingProfileRequestVM.getDeliveryMethodInfo(), createShippingProfileRequestVM.getLocationInfos(), countries, shop);
    }

    private ResponseEntity<DeliveryProfileDTO> createShippingProfileInternal(CreateShippingProfileRequestV2 createShippingProfileRequestVM, String shop) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        DeliveryProfileInput deliveryProfileInput = buildDeliveryProfileInput(createShippingProfileRequestVM);
        DeliveryProfileCreateMutation deliveryProfileCreateMutation = new DeliveryProfileCreateMutation(deliveryProfileInput);

        Response<Optional<DeliveryProfileCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileCreateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileCreate()
                .map(DeliveryProfileCreateMutation.DeliveryProfileCreate::getUserErrors).orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());


        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        String deliveryProfileId = optionalMutationResponse.getData()
            .flatMap(d -> d.getDeliveryProfileCreate().flatMap(e -> e.getProfile().map(DeliveryProfileCreateMutation.Profile::getId)))
            .orElse(null);

        DeliveryProfileDTO deliveryProfileDTO = new DeliveryProfileDTO();
        deliveryProfileDTO.setShop(shop);
        deliveryProfileDTO.setDeliveryProfileId(deliveryProfileId);

        DeliveryProfileDTO result = deliveryProfileService.save(deliveryProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @NotNull
    private ResponseEntity<DeliveryProfileDTO> createShippingProfileInternal(String name, List<DeliveryMethodInfo> deliveryMethodInfos, List<LocationInfo> locationInfoList, List<DeliveryCountryInput> countries, String shop) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<String> locationIds = locationInfoList.stream().map(LocationInfo::getLocationId).collect(Collectors.toList());
//        List<String> locationIds = buildLocationIds(shopifyGraphqlClient);

        List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate = buildMethodDefinitions(deliveryMethodInfos);

        DeliveryProfileInput deliveryProfileInput = buildDeliveryProfileInput(locationIds, name, methodDefinitionsToCreate, countries);
        DeliveryProfileCreateMutation deliveryProfileCreateMutation = new DeliveryProfileCreateMutation(deliveryProfileInput);

        Response<Optional<DeliveryProfileCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileCreateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileCreate()
                .map(DeliveryProfileCreateMutation.DeliveryProfileCreate::getUserErrors).orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());


        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        String deliveryProfileId = optionalMutationResponse.getData()
            .flatMap(d -> d.getDeliveryProfileCreate().flatMap(e -> e.getProfile().map(DeliveryProfileCreateMutation.Profile::getId)))
            .orElse(null);

        DeliveryProfileDTO deliveryProfileDTO = new DeliveryProfileDTO();
        deliveryProfileDTO.setShop(shop);
        deliveryProfileDTO.setDeliveryProfileId(deliveryProfileId);

        DeliveryProfileDTO result = deliveryProfileService.save(deliveryProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private DeliveryProfileInput buildDeliveryProfileInput(CreateShippingProfileRequestV2 createShippingProfileRequestVM) {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();

        deliverProfileInputBuilder.name(createShippingProfileRequestVM.getName());

        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = buildLocationsGroupToCreate(createShippingProfileRequestVM);
        deliverProfileInputBuilder.locationGroupsToCreate(locationGroupsToCreate);

        DeliveryProfileInput deliveryProfileInput = deliverProfileInputBuilder.build();
        return deliveryProfileInput;
    }

    @NotNull
    private DeliveryProfileInput buildDeliveryProfileInput(List<String> locationIds, String profileName, List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate, List<DeliveryCountryInput> countries) {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();

        deliverProfileInputBuilder.name(profileName);

        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = buildLocationsGroupToCreate(locationIds, methodDefinitionsToCreate, countries);
        deliverProfileInputBuilder.locationGroupsToCreate(locationGroupsToCreate);

        DeliveryProfileInput deliveryProfileInput = deliverProfileInputBuilder.build();
        return deliveryProfileInput;
    }

    private DeliveryProfileInput buildDeliveryProfileInput(String profileName) {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();

        deliverProfileInputBuilder.name(profileName);

        DeliveryProfileInput deliveryProfileInput = deliverProfileInputBuilder.build();
        return deliveryProfileInput;
    }

    @NotNull
    private List<DeliveryProfileLocationGroupInput> buildLocationsGroupToCreate(List<String> locationIds, List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate, List<DeliveryCountryInput> countries) {
        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = new ArrayList<>();

        DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = buildDeliveryProfileLocationGroupInput(locationIds, methodDefinitionsToCreate, countries);

        locationGroupsToCreate.add(deliveryProfileLocationGroupInput);
        return locationGroupsToCreate;
    }

    private List<DeliveryProfileLocationGroupInput> buildLocationsGroupToCreate(CreateShippingProfileRequestV2 createShippingProfileRequestVM) {
        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = new ArrayList<>();

        for (com.et.web.rest.vm.shippingprofile.LocationInfo locationInfo : createShippingProfileRequestVM.getLocationInfos()) {
            List<String> locationIds = new ArrayList<>();
            locationIds.add(locationInfo.getLocationId());
            DeliveryProfileLocationGroupInput.Builder deliveryProfileLocationGroupInputBuilder = DeliveryProfileLocationGroupInput.builder();
            List<DeliveryLocationGroupZoneInput> deliveryLocationGroupZoneInputs = new ArrayList<>();
            for (CountryInfo countryInfo : locationInfo.getCountryInfos()) {
                List<DeliveryMethodInfo> deliveryMethodInfo = countryInfo.getDeliveryMethodInfo();
                List<DeliveryMethodDefinitionInput> deliveryMethodDefinitionInputs1 = buildMethodDefinitions(deliveryMethodInfo);
                List<CountryInfo> countryInfo1 = List.of(countryInfo);
                List<DeliveryCountryInput> deliveryCountryInputs = getDeliveryCountryInputs(countryInfo1);
                deliveryLocationGroupZoneInputs.add(buildDeliveryLocationGroupZoneInput(deliveryMethodDefinitionInputs1, deliveryCountryInputs));
            }
            deliveryProfileLocationGroupInputBuilder.zonesToCreate(deliveryLocationGroupZoneInputs);
            deliveryProfileLocationGroupInputBuilder.locations(locationIds);
            DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = deliveryProfileLocationGroupInputBuilder.build();
            locationGroupsToCreate.add(deliveryProfileLocationGroupInput);
        }

        return locationGroupsToCreate;
    }

    @NotNull
    private List<DeliveryProfileLocationGroupInput> buildLocationsGroupToCreate() {
        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = new ArrayList<>();

        DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = buildDeliveryProfileLocationGroupInput();

        locationGroupsToCreate.add(deliveryProfileLocationGroupInput);
        return locationGroupsToCreate;
    }

    @NotNull
    private DeliveryProfileLocationGroupInput buildDeliveryProfileLocationGroupInput(List<String> locationIds, List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate, List<DeliveryCountryInput> countries) {
        DeliveryProfileLocationGroupInput.Builder deliveryProfileLocationGroupInputBuilder = DeliveryProfileLocationGroupInput.builder();

        List<DeliveryLocationGroupZoneInput> zonesToCreate = buildZonesToCreate(methodDefinitionsToCreate, countries);

        deliveryProfileLocationGroupInputBuilder.zonesToCreate(zonesToCreate);
        deliveryProfileLocationGroupInputBuilder.locations(locationIds);

        DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = deliveryProfileLocationGroupInputBuilder.build();
        return deliveryProfileLocationGroupInput;
    }

    @NotNull
    private DeliveryProfileLocationGroupInput buildDeliveryProfileLocationGroupInput() {
        DeliveryProfileLocationGroupInput.Builder deliveryProfileLocationGroupInputBuilder = DeliveryProfileLocationGroupInput.builder();
        DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = deliveryProfileLocationGroupInputBuilder.build();
        return deliveryProfileLocationGroupInput;
    }

    @NotNull
    private List<DeliveryLocationGroupZoneInput> buildZonesToCreate(List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate, List<DeliveryCountryInput> countries) {
        List<DeliveryLocationGroupZoneInput> zonesToCreate = new ArrayList<>();

        DeliveryLocationGroupZoneInput deliveryLocationGroupZoneInput = buildDeliveryLocationGroupZoneInput(methodDefinitionsToCreate, countries);
        zonesToCreate.add(deliveryLocationGroupZoneInput);
        return zonesToCreate;
    }

    @NotNull
    private DeliveryLocationGroupZoneInput buildDeliveryLocationGroupZoneInput(List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate, List<DeliveryCountryInput> countries) {
        DeliveryLocationGroupZoneInput.Builder deliveryLocationGroupZoneInputBuilder = DeliveryLocationGroupZoneInput.builder();
        deliveryLocationGroupZoneInputBuilder.name(RandomStringUtils.random(5));

        deliveryLocationGroupZoneInputBuilder.countries(countries);

        deliveryLocationGroupZoneInputBuilder.methodDefinitionsToCreate(methodDefinitionsToCreate);

        DeliveryLocationGroupZoneInput deliveryLocationGroupZoneInput = deliveryLocationGroupZoneInputBuilder.build();
        return deliveryLocationGroupZoneInput;
    }

    @NotNull
    private List<DeliveryMethodDefinitionInput> buildMethodDefinitions(List<DeliveryMethodInfo> deliveryMethodInfos) {
        List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate = new ArrayList<>();

        /*DeliveryMethodInfo prev = null;
        for (int i = 0; i < 249; i++) {
            if (i < deliveryMethodInfos.size()) {
                prev = deliveryMethodInfos.get(i);
                continue;
            }

            int i1 = i + 1;

            DeliveryMethodInfo deliveryMethodInfo = new DeliveryMethodInfo();
            deliveryMethodInfo.setCurrencyCode(prev.getCurrencyCode());
            deliveryMethodInfo.setAmount(12.99 * i1);
            String s = String.valueOf(i * 100);
            String s1 = String.valueOf(i1 * 100);
            deliveryMethodInfo.setName("Delivery BETWEEN " + s + " KG AND " + s1 + " KG");

            WeightCondition weightConditionLess = new WeightCondition();
            weightConditionLess.setWeight(i * 100);
            weightConditionLess.setDeliveryCondition("GREATER_THAN_OR_EQUAL_TO");
            weightConditionLess.setWeightUnit("KILOGRAMS");
            deliveryMethodInfo.getWeightConditions().add(weightConditionLess);

            WeightCondition weightConditionGreater = new WeightCondition();
            weightConditionGreater.setWeight(i1 * 100);
            weightConditionGreater.setDeliveryCondition("LESS_THAN_OR_EQUAL_TO");
            weightConditionGreater.setWeightUnit("KILOGRAMS");
            deliveryMethodInfo.getWeightConditions().add(weightConditionGreater);

            deliveryMethodInfos.add(deliveryMethodInfo);
        }*/

        for (DeliveryMethodInfo deliveryMethodInfo : deliveryMethodInfos) {
            DeliveryMethodDefinitionInput deliveryMethodDefinitionInput = buildDeliveryMethodDefinitionInput(deliveryMethodInfo);
            methodDefinitionsToCreate.add(deliveryMethodDefinitionInput);
        }

        return methodDefinitionsToCreate;
    }

    @NotNull
    private DeliveryMethodDefinitionInput buildDeliveryMethodDefinitionInput(DeliveryMethodInfo deliveryMethodInfo) {
        DeliveryMethodDefinitionInput.Builder deliveryMethodDefinitionInputBuilder = DeliveryMethodDefinitionInput.builder();

        if (!StringUtils.isEmpty(deliveryMethodInfo.getCarrierServiceId())) {
            DeliveryParticipantInput participant = DeliveryParticipantInput
                .builder()
                .carrierServiceId(deliveryMethodInfo.getCarrierServiceId())
                .adaptToNewServices(true)
                .build();
            deliveryMethodDefinitionInputBuilder.participant(participant);
        } else {
            MoneyInput price = buildShippingPrice(deliveryMethodInfo.getCurrencyCode(), deliveryMethodInfo.getAmount());
            DeliveryRateDefinitionInput rateDefinition = buildDeliveryRateDefinition(price);
            deliveryMethodDefinitionInputBuilder.rateDefinition(rateDefinition);
        }

        deliveryMethodDefinitionInputBuilder.name(deliveryMethodInfo.getName());

        if (!CollectionUtils.isEmpty(deliveryMethodInfo.getPriceConditions())) {
            List<DeliveryPriceConditionInput> priceConditionsToCreate = new ArrayList<>();
            for (PriceCondition priceCondition : deliveryMethodInfo.getPriceConditions()) {
                MoneyInput criteria = MoneyInput.builder()
                    .amount(priceCondition.getAmount())
                    .currencyCode(deliveryMethodInfo.getCurrencyCode())
                    .build();

                DeliveryPriceConditionInput deliveryPriceConditionInput = DeliveryPriceConditionInput
                    .builder()
                    .criteria(criteria)
                    .operator(DeliveryConditionOperator.safeValueOf(priceCondition.getDeliverCondtion()))
                    .build();
                priceConditionsToCreate.add(deliveryPriceConditionInput);
            }
            deliveryMethodDefinitionInputBuilder.priceConditionsToCreate(priceConditionsToCreate);
        }


        if (!CollectionUtils.isEmpty(deliveryMethodInfo.getWeightConditions())) {
            List<DeliveryWeightConditionInput> weightConditionInputs = new ArrayList<>();
            for (WeightCondition weightCondition : deliveryMethodInfo.getWeightConditions()) {
                WeightInput weightInput = WeightInput
                    .builder()
                    .value(weightCondition.getWeight())
                    .unit(WeightUnit.safeValueOf(weightCondition.getWeightUnit()))
                    .build();

                DeliveryWeightConditionInput deliveryWeightConditionInput = DeliveryWeightConditionInput
                    .builder()
                    .criteria(weightInput)
                    .operator(DeliveryConditionOperator.safeValueOf(weightCondition.getDeliveryCondition()))
                    .build();
                weightConditionInputs.add(deliveryWeightConditionInput);
            }
            deliveryMethodDefinitionInputBuilder.weightConditionsToCreate(weightConditionInputs);
        }


        DeliveryMethodDefinitionInput deliveryMethodDefinitionInput = deliveryMethodDefinitionInputBuilder.build();
        return deliveryMethodDefinitionInput;
    }

    @NotNull
    private DeliveryRateDefinitionInput buildDeliveryRateDefinition(MoneyInput price) {
        DeliveryRateDefinitionInput deliveryRateDefinitionInput = DeliveryRateDefinitionInput.builder().price(price).build();
        return deliveryRateDefinitionInput;
    }

    @NotNull
    private MoneyInput buildShippingPrice(CurrencyCode shopCurrencyCode, double amount) {
        MoneyInput price = MoneyInput.builder().amount(amount).currencyCode(shopCurrencyCode).build();
        return price;
    }

    @NotNull
    private List<DeliveryCountryInput> buildCountries() {
        List<DeliveryCountryInput> countries = new ArrayList<>();

        DeliveryCountryInput deliveryCountryInput = buildDeliveryCountryInput();
        countries.add(deliveryCountryInput);
        return countries;
    }

    @NotNull
    private DeliveryCountryInput buildDeliveryCountryInput() {
        DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
        deliveryCountryInputBuilder.restOfWorld(true);
        DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
        return deliveryCountryInput;
    }

    @NotNull
    private CurrencyCode getCurrencyCode(ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        ShopQuery shopQuery = new ShopQuery();

        Response<Optional<ShopQuery.Data>> shopQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopQuery);
        CurrencyCode shopCurrencyCode = Objects.requireNonNull(shopQueryResponse.getData()).map(s -> s.getShop().getCurrencyCode()).orElse(CurrencyCode.$UNKNOWN);
        return shopCurrencyCode;
    }


    @PutMapping({"/api/delivery-profiles/update-free-shipping-seller-group-ids", "/api/delivery-profiles/update-shipping-seller-group-ids"})
    public ResponseEntity<DeliveryProfileDTO> updateFreeShippingSellerGroupIds(@RequestBody AssociateFreeShippingDTO associateFreeShippingDTO) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileService.findOne(associateFreeShippingDTO.getId()).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String deliveryProfileId = deliveryProfileDTO.getDeliveryProfileId();
        DeliveryProfileQuery deliveryProfileQuery = new DeliveryProfileQuery(deliveryProfileId);

        Response<Optional<DeliveryProfileQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);

        if (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors())) {
            throw new RuntimeException();
        }

        Set<String> exitingSellerPlanGroupIds = Objects.requireNonNull(optionalQueryResponse.getData())
            .map(d -> d.getDeliveryProfile()
                .map(e -> e.getSellingPlanGroups().getEdges().stream()
                    .map(n -> n.getNode().getId()).collect(Collectors.toSet()))
                .orElse(new HashSet<>()))
            .orElse(new HashSet<>());

        Set<String> newSellerPlanGroupIds = associateFreeShippingDTO.getSellerGroupIds().stream()
            .map(s -> ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + s)
            .collect(Collectors.toSet());

        List<String> sellerGroupIdsToBeRemoved = new ArrayList<>();

        for (String exitingSellerPlanGroupId : exitingSellerPlanGroupIds) {
            if (!newSellerPlanGroupIds.contains(exitingSellerPlanGroupId)) {
                sellerGroupIdsToBeRemoved.add(exitingSellerPlanGroupId);
            }
        }


        DeliveryProfileInput.Builder deliveryProfileInputBuilder = DeliveryProfileInput.builder();
        deliveryProfileInputBuilder.sellingPlanGroupsToAssociate(new ArrayList<>(newSellerPlanGroupIds));
        deliveryProfileInputBuilder.sellingPlanGroupsToDissociate(sellerGroupIdsToBeRemoved);

        DeliveryProfileInput deliveryProfileInput = deliveryProfileInputBuilder.build();
        DeliveryProfileUpdateMutation deliveryProfileUpdateMutation = new DeliveryProfileUpdateMutation(deliveryProfileId, deliveryProfileInput);
        Response<Optional<DeliveryProfileUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileUpdate()
                .map(DeliveryProfileUpdateMutation.DeliveryProfileUpdate::getUserErrors).orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());


        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        log.info("shop=" + shop + " deliveryProfile=" + deliveryProfileDTO + " newSellerPlanGroupIds=" + newSellerPlanGroupIds + " sellerGroupIdsToBeRemoved=" + sellerGroupIdsToBeRemoved);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Plans updated successfully.", ""))
            .body(deliveryProfileDTO);
    }

    private List<String> buildLocationIds(ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        LocationSearchQuery locationsQuery = new LocationSearchQuery(Input.optional(null));
        Response<Optional<LocationSearchQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(locationsQuery);

        List<LocationSearchQuery.Node> locations = Objects.requireNonNull(optionalQueryResponse.getData()).map(d -> d.getLocations().getEdges().stream().map(LocationSearchQuery.Edge::getNode).collect(Collectors.toList())).orElse(new ArrayList<>());

        List<String> locationIds = locations.stream().map(LocationSearchQuery.Node::getId).collect(Collectors.toList());

        return locationIds;
    }

    /**
     * {@code GET  /delivery-profiles} : get all the deliveryProfiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryProfiles in body.
     */
    @GetMapping("/api/delivery-profiles")
    public List<DeliveryProfileDTO> getAllDeliveryProfiles() throws Exception {
        log.debug("REST request to get all DeliveryProfiles");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<DeliveryProfileDTO> deliveryProfileDTOList = deliveryProfileService.findByShop(shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<DeliveryProfileDTO> deliveryProfileDTOListToRemove = new ArrayList<>();

        for (Iterator<DeliveryProfileDTO> iterator = deliveryProfileDTOList.iterator(); iterator.hasNext(); ) {
            DeliveryProfileDTO deliveryProfileDTO = iterator.next();
            DeliveryProfileQuery deliveryProfileQuery = new DeliveryProfileQuery(deliveryProfileDTO.getDeliveryProfileId());
            Response<Optional<DeliveryProfileQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);

            if (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors())){
                iterator.remove();
            } else if (Objects.requireNonNull(optionalQueryResponse.getData()).isEmpty() || optionalQueryResponse.getData().get().getDeliveryProfile().isEmpty()){
                deliveryProfileDTOListToRemove.add(deliveryProfileDTO);
                iterator.remove();
            } else {
                String deliveryProfileName = Objects.requireNonNull(optionalQueryResponse.getData())
                    .flatMap(s -> s.getDeliveryProfile()
                        .map(DeliveryProfileQuery.DeliveryProfile::getName))
                    .orElse(null);

                deliveryProfileDTO.setName(deliveryProfileName);
            }
        }

        if(!CollectionUtils.isEmpty(deliveryProfileDTOListToRemove)){
            for(DeliveryProfileDTO deliveryProfileDTO : deliveryProfileDTOListToRemove) {
                deliveryProfileService.delete(deliveryProfileDTO.getId());
            }
        }

        return deliveryProfileDTOList;
    }

    @GetMapping("/api/delivery-profiles/v3")
    public ResponseEntity<List<CreateShippingProfileRequestV3>> getAllDetailedDeliveryProfileV3(){
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<DeliveryProfileDTO> deliveryProfileDTOOptional = deliveryProfileService.findByShop(shop);
        return ResponseEntity.ok(deliveryProfileDTOOptional.stream().map(deliveryProfileDTO -> {
            try {
                return deliveryProfileService.getShippingProfileDetailV3(shop, deliveryProfileDTO.getDeliveryProfileId(), deliveryProfileDTO.getId());
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * {@code GET  /delivery-profiles/:id} : get the "id" deliveryProfile.
     *
     * @param id the id of the deliveryProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/delivery-profiles/{id}")
    public ResponseEntity<DeliveryProfileDTO> getDeliveryProfile(@PathVariable Long id) throws Exception {
        log.debug("REST request to get DeliveryProfile : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<DeliveryProfileDTO> deliveryProfileDTOOptional = deliveryProfileService.findOne(id);

        if (deliveryProfileDTOOptional.isPresent()) {
            DeliveryProfileDTO deliveryProfileDTO = deliveryProfileDTOOptional.get();

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            String deliveryProfileId = deliveryProfileDTO.getDeliveryProfileId();
            DeliveryProfileDetailedQuery deliveryProfileQuery = new DeliveryProfileDetailedQuery(deliveryProfileId);

            Response<Optional<DeliveryProfileDetailedQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);

            if (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors())) {
                throw new RuntimeException();
            }

            Set<String> exitingSellerPlanGroupIds = Objects.requireNonNull(optionalQueryResponse.getData())
                .map(d -> d.getDeliveryProfile()
                    .map(e -> e.getSellingPlanGroups().getEdges().stream()
                        .map(n -> n.getNode().getId()).collect(Collectors.toSet()))
                    .orElse(new HashSet<>()))
                .orElse(new HashSet<>());

            deliveryProfileDTO.setSellerGroupIds(exitingSellerPlanGroupIds);
        }

        return ResponseUtil.wrapOrNotFound(deliveryProfileDTOOptional);
    }


    @GetMapping("/api/delivery-profiles/detailed/{id}")
    public ResponseEntity<DeliveryProfileDetailedQuery.Data> getDetailedDeliveryProfile(@PathVariable Long id) throws Exception {
        log.debug("REST request to get DeliveryProfile : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<DeliveryProfileDTO> deliveryProfileDTOOptional = deliveryProfileService.findOne(id);

        if (deliveryProfileDTOOptional.isPresent()) {
            DeliveryProfileDTO deliveryProfileDTO = deliveryProfileDTOOptional.get();

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            String deliveryProfileId = deliveryProfileDTO.getDeliveryProfileId();
            DeliveryProfileDetailedQuery deliveryProfileQuery = new DeliveryProfileDetailedQuery(deliveryProfileId);

            Response<Optional<DeliveryProfileDetailedQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);

            Optional<DeliveryProfileDetailedQuery.Data> data = optionalQueryResponse.getData();
            return ResponseUtil.wrapOrNotFound(data);
        }

        return null;
    }

    @GetMapping("/api/delivery-profiles/v3/detailed/{id}")
    public ResponseEntity<CreateShippingProfileRequestV3> getDetailedDeliveryProfileV3(@PathVariable Long id) throws Exception {
        log.debug("REST request to get DeliveryProfile : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<DeliveryProfileDTO> deliveryProfileDTOOptional = deliveryProfileService.findOne(id);

        if(deliveryProfileDTOOptional.isEmpty()){
            throw new BadRequestAlertException("Delivery profile not found for id:"+id, ENTITY_NAME, "");
        }

        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileDTOOptional.get();
        String deliveryProfileId = deliveryProfileDTO.getDeliveryProfileId();
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = deliveryProfileService.getShippingProfileDetail(shop, deliveryProfileId);
        return ResponseEntity.ok(createShippingProfileRequestV3);
    }

    /**
     * {@code DELETE  /delivery-profiles/:id} : delete the "id" deliveryProfile.
     *
     * @param id the id of the deliveryProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/delivery-profiles/{id}")
    public ResponseEntity<Void> deleteDeliveryProfile(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete DeliveryProfile : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<DeliveryProfileDTO> deliveryProfileDTOOptional = deliveryProfileService.findOne(id);

        if (deliveryProfileDTOOptional.isPresent()) {

            deleteProfileFromShopify(shop, deliveryProfileDTOOptional.get().getDeliveryProfileId());

            deliveryProfileService.delete(id);
        }


        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Shipping Profile Deleted Successfully.", "")).build();
    }

    private void deleteProfileFromShopify(String shop, String deliveryProfileId) throws Exception {
        DeliveryProfileRemoveMutation deliveryProfileRemoveMutation = new DeliveryProfileRemoveMutation(deliveryProfileId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<DeliveryProfileRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileRemoveMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileRemoveMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileRemove()
                .map(DeliveryProfileRemoveMutation.DeliveryProfileRemove::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!CollectionUtils.isEmpty(userErrors)) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }
    }

    @GetMapping(value = {"/api/delivery-profiles/get-locations", "/subscriptions/cp/api/delivery-profiles/get-locations"})
    public List<ShopFulfillmentLocation> getLocations() throws Exception {
        log.debug("REST request to get DeliveryProfile locations : {}");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<ShopFulfillmentLocation> finalLocations = new ArrayList<>();
        String cursor = null;
        boolean hasNextPage = true;

        while (hasNextPage) {
            LocationSearchQuery locationsQuery = new LocationSearchQuery(hasNextPage && cursor != null ? Input.optional(cursor) : Input.optional(null));
            Response<Optional<LocationSearchQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(locationsQuery);

            if (optionalQueryResponse.getData().isPresent()) {

                List<ShopFulfillmentLocation> locations = Objects.requireNonNull(optionalQueryResponse.getData())
                    .map(d -> d.getLocations().getEdges().stream()
                        .map(LocationSearchQuery.Edge::getNode)
                        .map(n -> {
                            ShopFulfillmentLocation shopFulfillmentLocation = new ShopFulfillmentLocation();
                            shopFulfillmentLocation.setId(n.getId());
                            shopFulfillmentLocation.setName(n.getName());
                            shopFulfillmentLocation.setPickupEnabled(n.getLocalPickupSettingsV2().isPresent());
                            return shopFulfillmentLocation;
                        })
                        .collect(Collectors.toList()))
                    .orElse(new ArrayList<>());

                finalLocations.addAll(locations);

                hasNextPage = optionalQueryResponse.getData().get().getLocations().getPageInfo().isHasNextPage();
            }

            if (hasNextPage) {
                LocationSearchQuery.PageInfo pageInfo = optionalQueryResponse.getData()
                    .map(LocationSearchQuery.Data::getLocations)
                    .map(LocationSearchQuery.Locations::getPageInfo)
                    .orElse(null);
                if (pageInfo.getEndCursor().isPresent()) {
                    cursor = pageInfo.getEndCursor().get();
                }
            }
        }
        ShopFulfillmentServicesQuery shopFulfillmentServicesQuery = new ShopFulfillmentServicesQuery();
        Response<Optional<ShopFulfillmentServicesQuery.Data>> optionalQueryResponse1 = shopifyGraphqlClient.getOptionalQueryResponse(shopFulfillmentServicesQuery);

        List<ShopFulfillmentLocation> locations1 = optionalQueryResponse1.getData()
            .map(ShopFulfillmentServicesQuery.Data::getShop)
            .map(s -> s.getFulfillmentServices().stream().filter(l -> l.getLocation().isPresent())
                .map(l -> l.getLocation().get()).map(l -> {
                    ShopFulfillmentLocation shopFulfillmentLocation = new ShopFulfillmentLocation();
                    shopFulfillmentLocation.setId(l.getId());
                    shopFulfillmentLocation.setName(l.getName());
                    shopFulfillmentLocation.setPickupEnabled(l.getLocalPickupSettingsV2().isPresent());
                    return shopFulfillmentLocation;
                }).collect(Collectors.toList()))
            .orElse(new ArrayList<>());

        finalLocations.addAll(locations1);

        finalLocations = finalLocations.stream().distinct().collect(Collectors.toList());

        return finalLocations;
    }
}

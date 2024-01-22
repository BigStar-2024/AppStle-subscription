package com.et.service.impl;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.DeliveryProfile;
import com.et.repository.DeliveryProfileRepository;
import com.et.service.DeliveryProfileService;
import com.et.service.dto.DeliveryProfileDTO;
import com.et.service.mapper.DeliveryProfileMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.shippingprofile.*;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DeliveryProfile}.
 */
@Service
@Transactional
public class DeliveryProfileServiceImpl implements DeliveryProfileService {

    private final Logger log = LoggerFactory.getLogger(DeliveryProfileServiceImpl.class);

    private static final String ENTITY_NAME = "deliveryProfile";

    private final DeliveryProfileRepository deliveryProfileRepository;

    private final DeliveryProfileMapper deliveryProfileMapper;


    @Autowired
    private CommonUtils commonUtils;

    public DeliveryProfileServiceImpl(DeliveryProfileRepository deliveryProfileRepository, DeliveryProfileMapper deliveryProfileMapper) {
        this.deliveryProfileRepository = deliveryProfileRepository;
        this.deliveryProfileMapper = deliveryProfileMapper;
    }

    @Override
    public DeliveryProfileDTO save(DeliveryProfileDTO deliveryProfileDTO) {
        log.debug("Request to save DeliveryProfile : {}", deliveryProfileDTO);
        DeliveryProfile deliveryProfile = deliveryProfileMapper.toEntity(deliveryProfileDTO);
        deliveryProfile = deliveryProfileRepository.save(deliveryProfile);
        return deliveryProfileMapper.toDto(deliveryProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryProfileDTO> findAll() {
        log.debug("Request to get all DeliveryProfiles");
        return deliveryProfileRepository.findAll().stream()
            .map(deliveryProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryProfileDTO> findOne(Long id) {
        log.debug("Request to get DeliveryProfile : {}", id);
        return deliveryProfileRepository.findById(id)
            .map(deliveryProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryProfile : {}", id);
        deliveryProfileRepository.deleteById(id);
    }

    @Override
    public List<DeliveryProfileDTO> findByShop(String shop) {
        return deliveryProfileRepository.findByShop(shop).stream()
            .map(deliveryProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public CreateShippingProfileRequestV3 createShippingProfileV4Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);
        for (ZoneInfo zoneInfo : createShippingProfileRequestVM.getZones()) {
            for (DeliveryMethodInfoV3 deliveryMethodInfo : zoneInfo.getDeliveryMethods()) {
                deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
            }
        }
        DeliveryProfileInput deliveryProfileInput = addDeliveryProfileInput(createShippingProfileRequestVM);
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
        save(deliveryProfileDTO);

        return getShippingProfileDetail(shop, deliveryProfileId);
    }

    @Override
    public CreateShippingProfileRequestV3 updateShippingProfileV4Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);
        for (ZoneInfo zoneInfo : createShippingProfileRequestVM.getZones()) {
            for (DeliveryMethodInfoV3 deliveryMethodInfo : zoneInfo.getDeliveryMethods()) {
                deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
            }
        }

        DeliveryProfileInput deliveryProfileInput = updateDeliveryProfileInput(shopifyGraphqlClient, createShippingProfileRequestVM);
        DeliveryProfileUpdateMutation deliveryProfileUpdateMutation = new DeliveryProfileUpdateMutation(createShippingProfileRequestVM.getId(), deliveryProfileInput);

        Response<Optional<DeliveryProfileUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(deliveryProfileUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<DeliveryProfileUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getDeliveryProfileUpdate().map(DeliveryProfileUpdateMutation.DeliveryProfileUpdate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }
        String deliveryProfileId = optionalMutationResponse.getData().flatMap(d -> d.getDeliveryProfileUpdate().flatMap(e -> e.getProfile().map(DeliveryProfileUpdateMutation.Profile::getId))).orElse(null);
        return getShippingProfileDetail(shop, deliveryProfileId);
    }

    @Override
    public CreateShippingProfileRequestV3 createShippingProfileV3Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        for (ZoneInfo zoneInfo : createShippingProfileRequestVM.getZones()) {
            for (DeliveryMethodInfoV3 deliveryMethodInfo : zoneInfo.getDeliveryMethods()) {
                deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
            }
        }

        DeliveryProfileInput deliveryProfileInput = buildDeliveryProfileInput(shopifyGraphqlClient, createShippingProfileRequestVM);
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
        save(deliveryProfileDTO);

        return getShippingProfileDetail(shop, deliveryProfileId);
    }

    @Override
    public CreateShippingProfileRequestV3 updateShippingProfileV3Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CurrencyCode shopCurrencyCode = getCurrencyCode(shopifyGraphqlClient);

        for (ZoneInfo zoneInfo : createShippingProfileRequestVM.getZones()) {
            for (DeliveryMethodInfoV3 deliveryMethodInfo : zoneInfo.getDeliveryMethods()) {
                deliveryMethodInfo.setCurrencyCode(shopCurrencyCode);
            }
        }

        DeliveryProfileInput deliveryProfileInput = buildDeliveryProfileInput(shopifyGraphqlClient, createShippingProfileRequestVM);
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

        return getShippingProfileDetail(shop, deliveryProfileId);
    }

    private CurrencyCode getCurrencyCode(ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        ShopQuery shopQuery = new ShopQuery();

        Response<Optional<ShopQuery.Data>> shopQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopQuery);
        return Objects.requireNonNull(shopQueryResponse.getData()).map(s -> s.getShop().getCurrencyCode()).orElse(CurrencyCode.$UNKNOWN);
    }

    private DeliveryProfileInput buildDeliveryProfileInput(ShopifyGraphqlClient shopifyGraphqlClient, CreateShippingProfileRequestV3 createShippingProfileRequestVM) throws Exception {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();

        deliverProfileInputBuilder.name(createShippingProfileRequestVM.getName());

        deliverProfileInputBuilder.sellingPlanGroupsToAssociate(buildSellingPlanGroupsToAssociate(createShippingProfileRequestVM));

        if(StringUtils.isNotBlank(createShippingProfileRequestVM.getId())) {
            deliverProfileInputBuilder.sellingPlanGroupsToDissociate(buildSellingPlanGroupsToDissociate(shopifyGraphqlClient, createShippingProfileRequestVM));
        }

        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = buildLocationsGroupToCreate(createShippingProfileRequestVM);
        deliverProfileInputBuilder.locationGroupsToCreate(locationGroupsToCreate);

        DeliveryProfileInput deliveryProfileInput = deliverProfileInputBuilder.build();
        return deliveryProfileInput;
    }

    private DeliveryProfileInput addDeliveryProfileInput(CreateShippingProfileRequestV3 createShippingProfileRequestVM) throws Exception {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();
        deliverProfileInputBuilder.name(createShippingProfileRequestVM.getName());
        return deliverProfileInputBuilder.build();
    }

    private DeliveryProfileInput updateDeliveryProfileInput(ShopifyGraphqlClient shopifyGraphqlClient, CreateShippingProfileRequestV3 createShippingProfileRequestVM) throws Exception {
        DeliveryProfileInput.Builder deliverProfileInputBuilder = DeliveryProfileInput.builder();
        deliverProfileInputBuilder.sellingPlanGroupsToAssociate(buildSellingPlanGroupsToAssociate(createShippingProfileRequestVM));
        if(StringUtils.isNotBlank(createShippingProfileRequestVM.getId())) {
            deliverProfileInputBuilder.sellingPlanGroupsToDissociate(buildSellingPlanGroupsToDissociate(shopifyGraphqlClient, createShippingProfileRequestVM));
        }
        return deliverProfileInputBuilder.build();
    }

    private List<String> buildSellingPlanGroupsToAssociate(CreateShippingProfileRequestV3 createShippingProfileRequestV3) {
        List<String> sellingPlanGroupsToAssociate = new ArrayList<>();
        for (SellingPlanGroupInfo sellingPlanGroupInfo : createShippingProfileRequestV3.getSellingPlanGroups()) {
            sellingPlanGroupsToAssociate.add(ShopifyGraphQLUtils.getGraphQLSellingPlanGroupId(sellingPlanGroupInfo.getValue()));
        }
        return sellingPlanGroupsToAssociate;
    }

    private List<String> buildSellingPlanGroupsToDissociate(ShopifyGraphqlClient shopifyGraphqlClient, CreateShippingProfileRequestV3 createShippingProfileRequestV3) throws Exception {

        String deliveryProfileId = createShippingProfileRequestV3.getId();
        DeliveryProfileQuery deliveryProfileQuery = new DeliveryProfileQuery(deliveryProfileId);

        Response<Optional<DeliveryProfileQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);

        if (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors())) {
            throw new BadRequestAlertException("Shipping profile not found for id: "+deliveryProfileId, ENTITY_NAME, "");
        }

        Set<String> exitingSellerPlanGroupIds = Objects.requireNonNull(optionalQueryResponse.getData())
            .map(d -> d.getDeliveryProfile()
                .map(e -> e.getSellingPlanGroups().getEdges().stream()
                    .map(n -> n.getNode().getId()).collect(Collectors.toSet()))
                .orElse(new HashSet<>()))
            .orElse(new HashSet<>());

        Set<String> newSellerPlanGroupIds = createShippingProfileRequestV3.getSellingPlanGroups().stream()
            .map(sellingPlanGroup -> ShopifyGraphQLUtils.getGraphQLSellingPlanGroupId(sellingPlanGroup.getValue()))
            .collect(Collectors.toSet());

        List<String> sellerGroupIdsToBeRemoved = new ArrayList<>();

        for (String exitingSellerPlanGroupId : exitingSellerPlanGroupIds) {
            if (!newSellerPlanGroupIds.contains(exitingSellerPlanGroupId)) {
                sellerGroupIdsToBeRemoved.add(exitingSellerPlanGroupId);
            }
        }
        return sellerGroupIdsToBeRemoved;
    }

    private List<DeliveryProfileLocationGroupInput> buildLocationsGroupToCreate(CreateShippingProfileRequestV3 createShippingProfileRequestVM) {
        List<DeliveryProfileLocationGroupInput> locationGroupsToCreate = new ArrayList<>();

        DeliveryProfileLocationGroupInput.Builder deliveryProfileLocationGroupInputBuilder = DeliveryProfileLocationGroupInput.builder();

        deliveryProfileLocationGroupInputBuilder.locations(buildLocations(createShippingProfileRequestVM));

        deliveryProfileLocationGroupInputBuilder.zonesToCreate(buildZonesToCreate(createShippingProfileRequestVM.getZones()));

        DeliveryProfileLocationGroupInput deliveryProfileLocationGroupInput = deliveryProfileLocationGroupInputBuilder.build();
        locationGroupsToCreate.add(deliveryProfileLocationGroupInput);

        return locationGroupsToCreate;
    }

    private List<String> buildLocations(CreateShippingProfileRequestV3 createShippingProfileRequestV3) {
        List<String> locations = new ArrayList<>();
        for (LocationInfoV3 locationInfo : createShippingProfileRequestV3.getLocations()) {
            locations.add(ShopifyGraphQLUtils.getGraphQlLocationId(locationInfo.getValue()));
        }
        return locations;
    }

    @NotNull
    private List<DeliveryLocationGroupZoneInput> buildZonesToCreate(List<ZoneInfo> zoneInfos) {

        List<DeliveryLocationGroupZoneInput> deliveryLocationGroupZoneInputList = new ArrayList<>();
        for (ZoneInfo zoneInfo : zoneInfos) {
            DeliveryLocationGroupZoneInput.Builder deliveryLocationGroupZoneInputBuilder = DeliveryLocationGroupZoneInput.builder();
            deliveryLocationGroupZoneInputBuilder.name(RandomStringUtils.random(5));

            List<DeliveryCountryInput> deliveryCountryInputs = getDeliveryCountryInputs(zoneInfo.getCountries(), zoneInfo.getRestOfWorld());
            deliveryLocationGroupZoneInputBuilder.countries(deliveryCountryInputs);

            List<DeliveryMethodDefinitionInput> deliveryMethodDefinitionInputs = buildMethodDefinitions(zoneInfo.getDeliveryMethods());
            deliveryLocationGroupZoneInputBuilder.methodDefinitionsToCreate(deliveryMethodDefinitionInputs);

            DeliveryLocationGroupZoneInput deliveryLocationGroupZoneInput = deliveryLocationGroupZoneInputBuilder.build();
            deliveryLocationGroupZoneInputList.add(deliveryLocationGroupZoneInput);
        }

        return deliveryLocationGroupZoneInputList;
    }

    private List<DeliveryCountryInput> getDeliveryCountryInputs(List<CountryInfoV3> countryInfos, Boolean restOfTheWorld) {
        List<DeliveryCountryInput> countries = new ArrayList<>();

        if (restOfTheWorld) {
            DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
            deliveryCountryInputBuilder.restOfWorld(true);
            DeliveryCountryInput deliveryCountryInput = deliveryCountryInputBuilder.build();
            countries.add(deliveryCountryInput);
        } else {
            countryInfos.forEach(countryInfo -> {
                DeliveryCountryInput.Builder deliveryCountryInputBuilder = DeliveryCountryInput.builder();
                deliveryCountryInputBuilder.code(CountryCode.safeValueOf(countryInfo.getValue()));
                if (BooleanUtils.isTrue(countryInfo.getIncludeAllProvinces())) {
                    deliveryCountryInputBuilder.includeAllProvinces(true);
                } else if (!CollectionUtils.isEmpty(countryInfo.getProvinces())) {

                    List<DeliveryProvinceInput> provinceInputs = new ArrayList<>();

                    countryInfo.getProvinces().forEach(p -> {
                        DeliveryProvinceInput.Builder deliveryProvinceInputBuilder = DeliveryProvinceInput.builder();
                        deliveryProvinceInputBuilder.code(p.getValue());
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

    @NotNull
    private List<DeliveryMethodDefinitionInput> buildMethodDefinitions(List<DeliveryMethodInfoV3> deliveryMethodInfos) {
        List<DeliveryMethodDefinitionInput> methodDefinitionsToCreate = new ArrayList<>();

        for (DeliveryMethodInfoV3 deliveryMethodInfo : deliveryMethodInfos) {
            DeliveryMethodDefinitionInput.Builder deliveryMethodDefinitionInputBuilder = DeliveryMethodDefinitionInput.builder();

            String deliveryMethodDefinationName = StringUtils.isNotBlank(deliveryMethodInfo.getName()) ? deliveryMethodInfo.getName() : RandomStringUtils.random(5);
            deliveryMethodDefinitionInputBuilder.name(deliveryMethodDefinationName);

            if (deliveryMethodInfo.getDefinitionType().equalsIgnoreCase("CARRIER")) {
                MoneyInput fixedFee = buildShippingPrice(deliveryMethodInfo.getCurrencyCode(), deliveryMethodInfo.getCarrierFixedFee());

                DeliveryParticipantInput participant = DeliveryParticipantInput.builder()
                    .carrierServiceId(deliveryMethodInfo.getCarrierServiceId())
                    .adaptToNewServices(true)
                    .fixedFee(fixedFee)
                    .percentageOfRateFee(deliveryMethodInfo.getCarrierPercentageFee())
                    .build();

                deliveryMethodDefinitionInputBuilder.participant(participant);
            } else {
                MoneyInput price = buildShippingPrice(deliveryMethodInfo.getCurrencyCode(), deliveryMethodInfo.getAmount());
                DeliveryRateDefinitionInput rateDefinition = buildDeliveryRateDefinition(price);
                deliveryMethodDefinitionInputBuilder.rateDefinition(rateDefinition);
            }

            if ("PRICE".equalsIgnoreCase(deliveryMethodInfo.getDeliveryConditionType())) {
                if(ObjectUtils.anyNotNull(deliveryMethodInfo.getMinValue(), deliveryMethodInfo.getMaxValue())){

                    List<DeliveryPriceConditionInput> priceConditionsToCreate = new ArrayList<>();

                    if(Objects.nonNull(deliveryMethodInfo.getMinValue())){
                        MoneyInput criteria = MoneyInput.builder()
                            .amount(deliveryMethodInfo.getMinValue())
                            .currencyCode(deliveryMethodInfo.getCurrencyCode())
                            .build();

                        DeliveryPriceConditionInput deliveryPriceConditionInput = DeliveryPriceConditionInput
                            .builder()
                            .criteria(criteria)
                            .operator(DeliveryConditionOperator.GREATER_THAN_OR_EQUAL_TO)
                            .build();

                        priceConditionsToCreate.add(deliveryPriceConditionInput);
                    }
                    if(Objects.nonNull(deliveryMethodInfo.getMaxValue())){
                        MoneyInput criteria = MoneyInput.builder()
                            .amount(deliveryMethodInfo.getMaxValue())
                            .currencyCode(deliveryMethodInfo.getCurrencyCode())
                            .build();

                        DeliveryPriceConditionInput deliveryPriceConditionInput = DeliveryPriceConditionInput
                            .builder()
                            .criteria(criteria)
                            .operator(DeliveryConditionOperator.LESS_THAN_OR_EQUAL_TO)
                            .build();

                        priceConditionsToCreate.add(deliveryPriceConditionInput);
                    }
                    if(!CollectionUtils.isEmpty(priceConditionsToCreate)) {
                        deliveryMethodDefinitionInputBuilder.priceConditionsToCreate(priceConditionsToCreate);
                    }
                }
            } else {
                if(ObjectUtils.anyNotNull(deliveryMethodInfo.getMinValue(), deliveryMethodInfo.getMaxValue())){

                    List<DeliveryWeightConditionInput> weightConditionInputs = new ArrayList<>();

                    if(Objects.nonNull(deliveryMethodInfo.getMinValue())){
                        WeightInput weightInput = WeightInput
                            .builder()
                            .value(deliveryMethodInfo.getMinValue())
                            .unit(WeightUnit.safeValueOf(deliveryMethodInfo.getWeightUnit()))
                            .build();

                        DeliveryWeightConditionInput deliveryWeightConditionInput = DeliveryWeightConditionInput
                            .builder()
                            .criteria(weightInput)
                            .operator(DeliveryConditionOperator.GREATER_THAN_OR_EQUAL_TO)
                            .build();

                        weightConditionInputs.add(deliveryWeightConditionInput);
                    }
                    if(Objects.nonNull(deliveryMethodInfo.getMaxValue())){
                        WeightInput weightInput = WeightInput
                            .builder()
                            .value(deliveryMethodInfo.getMaxValue())
                            .unit(WeightUnit.safeValueOf(deliveryMethodInfo.getWeightUnit()))
                            .build();

                        DeliveryWeightConditionInput deliveryWeightConditionInput = DeliveryWeightConditionInput
                            .builder()
                            .criteria(weightInput)
                            .operator(DeliveryConditionOperator.LESS_THAN_OR_EQUAL_TO)
                            .build();

                        weightConditionInputs.add(deliveryWeightConditionInput);
                    }
                    if(!CollectionUtils.isEmpty(weightConditionInputs)) {
                        deliveryMethodDefinitionInputBuilder.weightConditionsToCreate(weightConditionInputs);
                    }
                }
            }
            DeliveryMethodDefinitionInput deliveryMethodDefinitionInput = deliveryMethodDefinitionInputBuilder.build();
            methodDefinitionsToCreate.add(deliveryMethodDefinitionInput);
        }

        return methodDefinitionsToCreate;
    }

    @NotNull
    private DeliveryRateDefinitionInput buildDeliveryRateDefinition(MoneyInput price) {
        DeliveryRateDefinitionInput.Builder deliveryRateDefinitionInputBuilder = DeliveryRateDefinitionInput.builder();
        deliveryRateDefinitionInputBuilder.price(price);
        return deliveryRateDefinitionInputBuilder.build();
    }

    @NotNull
    private MoneyInput buildShippingPrice(CurrencyCode shopCurrencyCode, double amount) {
        MoneyInput.Builder priceBuilder = MoneyInput.builder();
        priceBuilder.amount(amount);
        priceBuilder.currencyCode(shopCurrencyCode);
        return priceBuilder.build();
    }

    private  Response<Optional<DeliveryProfileDetailedQuery.Data>> getDeliveryProfileDetailed(String shop, String deliveryProfileId) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        DeliveryProfileDetailedQuery deliveryProfileQuery = new DeliveryProfileDetailedQuery(deliveryProfileId);
        return shopifyGraphqlClient.getOptionalQueryResponse(deliveryProfileQuery);
    }

    @Override
    public CreateShippingProfileRequestV3 getShippingProfileDetail(String shop, String deliveryProfileId) throws Exception {
        Response<Optional<DeliveryProfileDetailedQuery.Data>> optionalQueryResponse = getDeliveryProfileDetailed(shop, deliveryProfileId);
        if (optionalQueryResponse.hasErrors()) {
            throw new BadRequestAlertException(optionalQueryResponse.getErrors().get(0).getMessage(), ENTITY_NAME, "");
        }
        Optional<DeliveryProfileDetailedQuery.DeliveryProfile> deliveryProfileOptional = Objects.requireNonNull(optionalQueryResponse.getData()).flatMap(DeliveryProfileDetailedQuery.Data::getDeliveryProfile);
        if (deliveryProfileOptional.isPresent()) {
            return getCreateShippingProfileRequestV3(deliveryProfileOptional);
        } else {
            throw new BadRequestAlertException("Delivery profile not found for id:" + deliveryProfileId, ENTITY_NAME, "");
        }
    }

    @Override
    public CreateShippingProfileRequestV3 getShippingProfileDetailV3(String shop, String deliveryProfileId, Long id) throws Exception {
        Response<Optional<DeliveryProfileDetailedQuery.Data>> optionalQueryResponse = getDeliveryProfileDetailed(shop, deliveryProfileId);
        if (optionalQueryResponse.hasErrors()) {
            return null;
        } else if (Objects.requireNonNull(optionalQueryResponse.getData()).isEmpty() || optionalQueryResponse.getData().get().getDeliveryProfile().isEmpty()){
            delete(id);
            return null;
        }
        Optional<DeliveryProfileDetailedQuery.DeliveryProfile> deliveryProfileOptional = Objects.requireNonNull(optionalQueryResponse.getData()).flatMap(DeliveryProfileDetailedQuery.Data::getDeliveryProfile);
        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = getCreateShippingProfileRequestV3(deliveryProfileOptional);
        createShippingProfileRequestV3.setProfileId(id);
        return createShippingProfileRequestV3;
    }

    private CreateShippingProfileRequestV3 getCreateShippingProfileRequestV3(Optional<DeliveryProfileDetailedQuery.DeliveryProfile> deliveryProfileOptional) {
        DeliveryProfileDetailedQuery.DeliveryProfile deliveryProfile = deliveryProfileOptional.get();

        CreateShippingProfileRequestV3 createShippingProfileRequestV3 = new CreateShippingProfileRequestV3();

        createShippingProfileRequestV3.setId(deliveryProfile.getId());
        createShippingProfileRequestV3.setName(deliveryProfile.getName());

        List<SellingPlanGroupInfo> sellingPlansGroups = deliveryProfile.getSellingPlanGroups().getEdges().stream()
            .map(DeliveryProfileDetailedQuery.Edge::getNode)
            .map(sellingPlansGroup -> {
                SellingPlanGroupInfo sellingPlanGroupInfo = new SellingPlanGroupInfo();
                sellingPlanGroupInfo.setValue(sellingPlansGroup.getId());
                sellingPlanGroupInfo.setLabel(sellingPlansGroup.getName());
                return sellingPlanGroupInfo;
            }).collect(Collectors.toList());

        createShippingProfileRequestV3.setSellingPlanGroups(sellingPlansGroups);

        deliveryProfile.getProfileLocationGroups().forEach(profileLocationGroup -> {
            List<LocationInfoV3> locationIds = profileLocationGroup.getLocationGroup().getLocations().getEdges().stream()
                .map(DeliveryProfileDetailedQuery.Edge1::getNode)
                .map(location -> {
                    LocationInfoV3 locationInfoV3 = new LocationInfoV3();
                    locationInfoV3.setValue(location.getId());
                    locationInfoV3.setLabel(location.getName());
                    return locationInfoV3;
                })
                .collect(Collectors.toList());

            createShippingProfileRequestV3.setLocations(locationIds);

            List<ZoneInfo> zoneInfoList = profileLocationGroup.getLocationGroupZones().getEdges().stream()
                .map(DeliveryProfileDetailedQuery.Edge2::getNode)
                .map(groupZone -> {
                    ZoneInfo zoneInfo = new ZoneInfo();
                    List<CountryInfoV3> countries = new ArrayList<>();
                    groupZone.getZone().getCountries().forEach(country -> {
                        if (country.getCode().isRestOfWorld()) {
                            zoneInfo.setRestOfWorld(true);
                        } else if (country.getCode().getCountryCode().isPresent()) {
                            zoneInfo.setRestOfWorld(false);
                            CountryInfoV3 countryInfoV3 = new CountryInfoV3();
                            countryInfoV3.setValue(country.getCode().getCountryCode().get().toString());
                            countryInfoV3.setLabel(country.getTranslatedName());

                            List<Province> provinces = country.getProvinces().stream()
                                .map(DeliveryProfileDetailedQuery.Province::getCode)
                                .map(item -> {
                                    Province province = new Province();
                                    province.setValue(item);
                                    return province;
                                })
                                .collect(Collectors.toList());
                            countryInfoV3.setProvinces(provinces);
                            countries.add(countryInfoV3);
                        }
                    });
                    zoneInfo.setCountries(countries);

                    List<DeliveryMethodInfoV3> deliveryMethodInfoList = groupZone.getMethodDefinitions().getEdges().stream()
                        .map(DeliveryProfileDetailedQuery.Edge3::getNode)
                        .map(deliveryMethod -> {

                            DeliveryMethodInfoV3 deliveryMethodInfo = new DeliveryMethodInfoV3();

                            deliveryMethodInfo.setName(deliveryMethod.getName());
                            if (deliveryMethod.getRateProvider().get__typename().equals("DeliveryRateDefinition")) {
                                DeliveryProfileDetailedQuery.AsDeliveryRateDefinition rateDefinition = (DeliveryProfileDetailedQuery.AsDeliveryRateDefinition) deliveryMethod.getRateProvider();
                                deliveryMethodInfo.setDefinitionType("OWN");
                                deliveryMethodInfo.setAmount(Double.parseDouble(rateDefinition.getPrice().getAmount().toString()));
                                deliveryMethodInfo.setCurrencyCode(rateDefinition.getPrice().getCurrencyCode());
                            } else {
                                DeliveryProfileDetailedQuery.AsDeliveryParticipant deliveryParticipant = (DeliveryProfileDetailedQuery.AsDeliveryParticipant) deliveryMethod.getRateProvider();
                                deliveryMethodInfo.setDefinitionType("CARRIER");
                                deliveryMethodInfo.setCarrierServiceId(deliveryParticipant.getCarrierService().getId());
                                deliveryMethodInfo.setCarrierFixedFee(deliveryParticipant.getFixedFee().map(fee -> Double.parseDouble(fee.getAmount().toString())).orElse(null));
                                deliveryMethodInfo.setCarrierPercentageFee(deliveryParticipant.getPercentageOfRateFee());
                            }
                            deliveryMethod.getMethodConditions().forEach(methodCondition -> {
                                if (methodCondition.getConditionCriteria().get__typename().equals("MoneyV2")) {
                                    deliveryMethodInfo.setDeliveryConditionType("PRICE");
                                    DeliveryProfileDetailedQuery.AsMoneyV2 moneyV2 = (DeliveryProfileDetailedQuery.AsMoneyV2) methodCondition.getConditionCriteria();
                                    if(methodCondition.getOperator().equals(DeliveryConditionOperator.GREATER_THAN_OR_EQUAL_TO)){
                                        deliveryMethodInfo.setMinValue(Double.parseDouble(moneyV2.getAmount().toString()));
                                    }else{
                                        deliveryMethodInfo.setMaxValue(Double.parseDouble(moneyV2.getAmount().toString()));
                                    }
                                    deliveryMethodInfo.setCurrencyCode(moneyV2.getCurrencyCode());
                                } else {
                                    deliveryMethodInfo.setDeliveryConditionType("WEIGHT");
                                    DeliveryProfileDetailedQuery.AsWeight weight = (DeliveryProfileDetailedQuery.AsWeight) methodCondition.getConditionCriteria();
                                    if(methodCondition.getOperator().equals(DeliveryConditionOperator.GREATER_THAN_OR_EQUAL_TO)){
                                        deliveryMethodInfo.setMinValue(weight.getValue());
                                    }else{
                                        deliveryMethodInfo.setMaxValue(weight.getValue());
                                    }
                                    deliveryMethodInfo.setWeightUnit(weight.getUnit().toString());
                                }
                            });
                            return deliveryMethodInfo;
                        }).collect(Collectors.toList());
                    zoneInfo.setDeliveryMethods(deliveryMethodInfoList);
                    return zoneInfo;
                }).collect(Collectors.toList());
            createShippingProfileRequestV3.setZones(zoneInfoList);
        });
        return createShippingProfileRequestV3;
    }
}

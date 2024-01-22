package com.et.utils;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.asset.Asset;
import com.et.api.shopify.theme.GetThemesResponse;
import com.et.api.svix.SvixClient;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.constant.AppstleAttribute;
import com.et.constant.SvixConstants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.liquid.EmailModel;
import com.et.liquid.ShippingAddressModel;
import com.et.liquid.ShippingOptionModel;
import com.et.pojo.VariantInfo;
import com.et.repository.*;
import com.et.security.ExternalTokenType;
import com.et.security.ExternalUserPasswordAuthenticationToken;
import com.et.security.SecurityUtils;
import com.et.service.*;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.service.dto.ShopInfoDTO;
import com.et.service.dto.SubscriptionContractOneOffDTO;
import com.et.service.mapper.SubscriptionContractOneOffMapper;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.VariantQuantity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX;
import static com.et.api.utils.SubscriptionUtils.getNextBillingDate;
import static java.util.Objects.requireNonNull;

@Component
@Lazy
public class CommonUtils {

    private final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    public static final int TOTAL_ATTEMPTS = 10;

    public static String buildStepFunctionExecutionName(String executionName) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timeStampSuffix = "-" + dateTimeFormatter.format(ZonedDateTime.now());

        if((executionName.length() + timeStampSuffix.length()) > 80) {
            executionName = executionName.substring(0, (80-timeStampSuffix.length()));
        }

        executionName = executionName + timeStampSuffix;

        return executionName;
    }

    public static String buildStepFunctionExecutionName(Long contractId, String executionName) {
        String executionNamePrefix = "";
        if(contractId != null) {
            executionNamePrefix = contractId + "";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(executionName)) {
            executionNamePrefix = executionNamePrefix + "-" + executionName.trim();
        }

        return buildStepFunctionExecutionName(executionNamePrefix);
    }

    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
            String a = "b";
        }
        return data;
    }

    public static <T> T fromJSON(final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, new TypeReference<>() {});
        } catch (Exception e) {
            // Handle the problem
            String a = "b";
        }
        return data;
    }

    public static <T> T fromJSONIgnoreUnknownProperty(final TypeReference<T> type,
                                                      final String jsonPacket) {
        T data = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            data = objectMapper.readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
            String a = "b";
        }
        return data;
    }

    public static <T> T fromJSONIgnoreUnknownProperty(final String jsonPacket) {
        T data = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            data = objectMapper.readValue(jsonPacket, new TypeReference<>() {});
        } catch (Exception e) {
            // Handle the problem
            String a = "b";
        }
        return data;
    }

    public Long getCustomerIdFromCustomerUid(String customerUid) {
        Optional<Long> customerId = customerPaymentService.getCustomerIdFromCustomerUid(customerUid);
        throwExceptionIfCustomerIdNotFound(customerId);
        return customerId.orElse(null);
    }

    public Long getCustomerIdFromRequestParams(Long customerId, String customerUid) {
        if (!StringUtils.isEmpty(customerId)) {
            return customerId;
        } else if (StringUtils.hasText(customerUid)) {
            return this.getCustomerIdFromCustomerUid(customerUid);
        }
        //log.error("Unable to find customerId from passed request params.");
        return null;
    }

    public ShopifyGraphqlClient prepareShopifyGraphqlClient(String shopName) {
        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(shopName);

        if (socialConnection.isEmpty()) {
            throw new BadRequestAlertException("Invalid request", "data", "idnull");
        }
        final String accessToken = socialConnection.get().getAccessToken();
        return new ShopifyGraphqlClient(shopName, accessToken, Optional.ofNullable(socialConnection.get().getGraphqlRateLimit()).orElse(2));
    }

    public ShopifyGraphqlClient prepareShopifyGraphqlClient(String shopName, int permitPerSecond) {
        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(shopName);

        if (socialConnection.isEmpty()) {
            throw new BadRequestAlertException("Invalid request", "data", "idnull");
        }
        final String accessToken = socialConnection.get().getAccessToken();
        return new ShopifyGraphqlClient(shopName, accessToken, permitPerSecond);
    }

    public SvixClient prepareSvixClient(String shop) {
        SvixClient svixClient = new SvixClient(SvixConstants.API_KEY, shop);

        return svixClient;
    }

    public void writeActivityLog(String shop, Long entityId, ActivityLogEntityType entityType, ActivityLogEventSource eventSource, ActivityLogEventType eventType, ActivityLogStatus status) {
        writeActivityLog(shop, entityId, entityType, eventSource, eventType, status, null);
    }

    public void writeActivityLog(String shop, Long entityId, ActivityLogEntityType entityType, ActivityLogEventSource eventSource, ActivityLogEventType eventType, ActivityLogStatus status, Map<String, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            ActivityLog activityLog = new ActivityLog();
            activityLog.setEntityId(entityId);
            activityLog.setEntityType(entityType);
            activityLog.setEventSource(eventSource);
            activityLog.setShop(shop);
            activityLog.setCreateAt(ZonedDateTime.now());
            activityLog.setEventType(eventType);
            activityLog.setStatus(status);

            if (map != null) {
                activityLog.setAdditionalInfo(objectMapper.writeValueAsString(map));
            }

            log.info(objectMapper.writeValueAsString(activityLog));
        } catch (Exception ex) {
            log.error("Error while writing activity log. shop={}, entityId={}, eventType={}", shop, entityId, eventType);
        }
    }

    public ShopifyAPI prepareShopifyResClient(String shopName) {
        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(shopName);

        if (socialConnection.isEmpty()) {
            throw new BadRequestAlertException("Invalid request", "data", "idnull");
        }
        final String accessToken = socialConnection.get().getAccessToken();
        return new ShopifyWithRateLimiter(accessToken, shopName, RateLimiter.create(Optional.ofNullable(socialConnection.get().getRestRateLimit()).orElse(2)));
    }

    public String getShop() {
        return SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
    }

    public String getShopAccessToken() {
        String shop = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(shop);
        return socialConnection.isPresent() ? socialConnection.get().getAccessToken() : null;
    }

    public Boolean isExternal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication instanceof ExternalUserPasswordAuthenticationToken)
            .orElse(false);
    }

    public Optional<String> getCustomerToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Optional<String> optionalToken = Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication instanceof ExternalUserPasswordAuthenticationToken)
            .map(authentication -> ((ExternalUserPasswordAuthenticationToken) authentication).getToken())
            .filter(org.apache.commons.lang3.StringUtils::isNotBlank);
        return optionalToken;
    }

    public boolean isCustomerPortalRequest() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(s -> s instanceof ExternalUserPasswordAuthenticationToken).orElse(false);
    }

    @Autowired
    private DunningManagementService dunningManagementService;

    public void updateQueuedAttempts(String shop, Long contractId) {
        Optional<SubscriptionContractDetails> subscriptionContractDetailsOptional = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);
        if(subscriptionContractDetailsOptional.isPresent()){
            Optional<Long> nextBillingId = subscriptionBillingAttemptRepository
                .findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(contractId, BillingAttemptStatus.QUEUED)
                .map(SubscriptionBillingAttempt::getId);
            SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsOptional.get();
            updateQueuedAttempts(
                subscriptionContractDetails.getNextBillingDate(),
                shop,
                contractId,
                subscriptionContractDetails.getBillingPolicyIntervalCount(),
                subscriptionContractDetails.getBillingPolicyInterval(),
                subscriptionContractDetails.getMaxCycles(),
                nextBillingId.orElse(null)
            );
        }
    }

    public void updateQueuedAttempts(ZonedDateTime billingDateToStartWith, String shop, Long subscriptionContractId, Integer billingPolicyIntervalCount, String billingPolicyInterval, Integer maxCycles, Long nextBillingId) {

        log.info("Updating queued attempts for contract {}", subscriptionContractId);
        Map<String, Object> activityAdditionalInfo = new HashMap<>();
        activityAdditionalInfo.put("billingDateToStartWith", billingDateToStartWith.toString());
        activityAdditionalInfo.put("billingPolicyIntervalCount", billingPolicyIntervalCount);
        activityAdditionalInfo.put("billingPolicyInterval", billingPolicyInterval);
        activityAdditionalInfo.put("maxCycles", maxCycles);
        try {

            List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractId, BillingAttemptStatus.SUCCESS);

            List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractId, BillingAttemptStatus.QUEUED);

            int totalAttempts = Math.min(TOTAL_ATTEMPTS, Optional.ofNullable(maxCycles).orElse(Integer.MAX_VALUE) - successAttempts.size() - 1);

            activityAdditionalInfo.put("totalAttempts", totalAttempts);
            activityAdditionalInfo.put("queuedAttempts", subscriptionBillingAttempts.size());


            if (subscriptionBillingAttempts.size() >= totalAttempts) {
                activityAdditionalInfo.put("cause", "Queued attempts are greater than or equal to total attempts");
                writeActivityLog(shop, subscriptionContractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.UPDATE_QUEUED_ATTEMPTS, ActivityLogStatus.INFO, activityAdditionalInfo);
                return;
            }

            int iterations = totalAttempts - subscriptionBillingAttempts.size();
            activityAdditionalInfo.put("remainingIterations", iterations);

            Optional<ZonedDateTime> lastBillingDateOptional = subscriptionBillingAttempts
                .stream()
                .sorted((b1, b2) -> b2.getBillingDate().compareTo(b1.getBillingDate()))
                .map(SubscriptionBillingAttempt::getBillingDate)
                .findFirst();

            List<SubscriptionBillingAttempt> subscriptionBillingAttemptListToSave = new ArrayList<>();

            ZonedDateTime nextBillingDate = lastBillingDateOptional.orElse(null);
            while (iterations > 0) {
                nextBillingDate = nextBillingDate == null ? billingDateToStartWith : getNextBillingDate(billingPolicyIntervalCount, billingPolicyInterval, nextBillingDate);

                SubscriptionBillingAttempt subscriptionBillingAttempt = new SubscriptionBillingAttempt();
                subscriptionBillingAttempt.setBillingDate(nextBillingDate);
                subscriptionBillingAttempt.setContractId(subscriptionContractId);
                subscriptionBillingAttempt.setShop(shop);
                subscriptionBillingAttempt.setStatus(BillingAttemptStatus.QUEUED);
                subscriptionBillingAttempt.setRetryingNeeded(true);
                //subscriptionBillingAttemptRepository.save(subscriptionBillingAttempt);
                subscriptionBillingAttemptListToSave.add(subscriptionBillingAttempt);
                iterations--;
            }

            if(!CollectionUtils.isEmpty(subscriptionBillingAttemptListToSave)) {
                activityAdditionalInfo.put("firstBillingDateToSave", subscriptionBillingAttemptListToSave.get(0).getBillingDate().toString());
                activityAdditionalInfo.put("lastBillingDateToSave", subscriptionBillingAttemptListToSave.get(subscriptionBillingAttemptListToSave.size() - 1).getBillingDate().toString());
            }

            subscriptionBillingAttemptRepository.saveAll(subscriptionBillingAttemptListToSave);

            Optional<SubscriptionBillingAttempt> firstUpcomingOrder = subscriptionBillingAttemptRepository.findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(subscriptionContractId, BillingAttemptStatus.QUEUED);
            SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId).get();

            if (firstUpcomingOrder.isPresent()) {
                if (!firstUpcomingOrder.get().getBillingDate().equals(subscriptionContractDetails.getNextBillingDate())) {
                    activityAdditionalInfo.put("cause", "firstUpcomingOrder Date isn't same as subscription next Order date");
                    log.info("firstUpcomingOrder Date isn't same subscription next Order date for shop=" + shop + " contractId=" + subscriptionContractId);
                }
            }

            writeActivityLog(shop, subscriptionContractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.UPDATE_QUEUED_ATTEMPTS, ActivityLogStatus.SUCCESS, activityAdditionalInfo);

            if(firstUpcomingOrder.isPresent()) {
                mayBeReAssignOneTimeVariants(shop, subscriptionContractId, nextBillingId, firstUpcomingOrder.get().getId());
            }
        } catch (Exception ex) {
            activityAdditionalInfo.put("error", ex.getMessage());
            try {
                writeActivityLog(shop, subscriptionContractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.UPDATE_QUEUED_ATTEMPTS, ActivityLogStatus.FAILURE, activityAdditionalInfo);
            }catch (Exception e){
                log.error("Error occurred while writing activity log for Updating queued attempts, {}", e.getMessage());
            }
            log.error("An error occurred updateQueuedAttempts. ex=" + ExceptionUtils.getStackTrace(ex));
        }
    }

    public void mayBeReAssignOneTimeVariants(String shop, Long contractId, Long oldBillingId, Long newBillingId){
        try{
            if(ObjectUtils.allNotNull(oldBillingId, newBillingId)){
                List<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOList = findByShopAndSubscriptionContractId(shop, contractId);
                if(!CollectionUtils.isEmpty(subscriptionContractOneOffDTOList)){
                    List<SubscriptionContractOneOffDTO> activeOneOffDTOList = subscriptionContractOneOffDTOList.stream().filter(dto -> dto.getBillingAttemptId().equals(oldBillingId)).collect(Collectors.toList());
                    for(SubscriptionContractOneOffDTO activeOneOffDTO : activeOneOffDTOList){
                        activeOneOffDTO.setBillingAttemptId(newBillingId);
                        save(activeOneOffDTO);
                    }
                }
            }
        }catch (Exception e){
            log.error("Error occurred while re-assigning one time variants, error= {}", e.getMessage());
        }
    }

    @Autowired
    private SubscriptionContractOneOffRepository subscriptionContractOneOffRepository;

    @Autowired
    private SubscriptionContractOneOffMapper subscriptionContractOneOffMapper;

    public SubscriptionContractOneOffDTO save(SubscriptionContractOneOffDTO subscriptionContractOneOffDTO) {
        log.debug("Request to save SubscriptionContractOneOff : {}", subscriptionContractOneOffDTO);
        SubscriptionContractOneOff subscriptionContractOneOff = subscriptionContractOneOffMapper.toEntity(subscriptionContractOneOffDTO);
        subscriptionContractOneOff = subscriptionContractOneOffRepository.save(subscriptionContractOneOff);
        return subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);
    }

    public List<SubscriptionContractOneOffDTO> findByShopAndSubscriptionContractId(String shop, Long subscriptionContractId) {
        log.debug("Request to get all SubscriptionContractOneOffs");
        return subscriptionContractOneOffRepository.findByShopAndSubscriptionContractId(shop, subscriptionContractId)
            .stream().map(t -> subscriptionContractOneOffMapper.toDto(t)).collect(Collectors.toList());
    }

    @Autowired
    private ProductSwapRepository productSwapRepository;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<VariantQuantity> getProductsToBeSwapped(
        List<ProductSwap> productSwapList, List<VariantQuantity> original, int totalCycles) {

        List<VariantQuantity> resultList = new ArrayList<>();

        for (VariantQuantity variantQuantity : original) {
            resultList.add(new VariantQuantity(variantQuantity.getVariantId(), variantQuantity.getQuantity()));
        }

        Set<Long> variantIds = original.stream().map(VariantQuantity::getVariantId).collect(Collectors.toSet());
        Map<Long, Integer> quantityByVariantId = original.stream().collect(Collectors.toMap(VariantQuantity::getVariantId, VariantQuantity::getQuantity, (b1, b2) -> b1));

        Map<String, List<Long>> productSwapMap = new LinkedHashMap<>();
        Map<String, List<Long>> sourceVariantIdsByKey = new LinkedHashMap<>();
        Map<Long, String> variantTitles = new HashMap<>();

        for (ProductSwap productSwap : productSwapList) {
            if (productSwap.getForBillingCycle() != null) {
                if (!productSwap.getForBillingCycle().equals(totalCycles)) {
                    continue;
                }
            }

            List<VariantInfo> sourceVariants = fromJSON(
                new TypeReference<>() {
                },
                productSwap.getSourceVariants()
            );

            List<VariantInfo> destinationVariants = fromJSON(
                new TypeReference<>() {
                },
                productSwap.getDestinationVariants()
            );

            List<Long> sourceVariantIds = sourceVariants.stream().map(VariantInfo::getId).collect(Collectors.toList());
            List<Long> destinationVariantIds = destinationVariants.stream().map(VariantInfo::getId).collect(Collectors.toList());

            destinationVariants.forEach(swapVariant -> {
                Long id = swapVariant.getId();
                String title =  swapVariant.getAdditionalProperties().getOrDefault("displayName", "").toString();
                variantTitles.put(id, title);
            });

            String key = sourceVariantIds.stream().sorted().map(Object::toString).reduce((i1, i2) -> i1 + "_" + i2).get();
            productSwapMap.put(key, destinationVariantIds);
            sourceVariantIdsByKey.put(key, sourceVariantIds);
        }

        Set<String> combinationsToSwap = getCombinationsToSwap(variantIds, productSwapMap);

        for (String combinationToSwap : combinationsToSwap) {

            Set<Long> variantIdsToRemove = new HashSet<>();
            Set<Long> variantIdsToAdd = new HashSet<>();

            Set<Long> destinationVariantIds = new HashSet<>(productSwapMap.get(combinationToSwap));
            Set<Long> sourceVariantIds = new HashSet<>(sourceVariantIdsByKey.get(combinationToSwap));

            Set<Integer> quantitySet = sourceVariantIds.stream().map(quantityByVariantId::get).collect(Collectors.toSet());

            if (quantitySet.size() != 1) {
                continue;
            }

            Integer quantity = new ArrayList<>(quantitySet).get(0);

            for (Long sourceVariantId : sourceVariantIds) {
                if (!destinationVariantIds.contains(sourceVariantId)) {
                    variantIdsToRemove.add(sourceVariantId);
                }
            }

            for (Long destinationVariantId : destinationVariantIds) {
                if (!sourceVariantIds.contains(destinationVariantId)) {
                    variantIdsToAdd.add(destinationVariantId);
                }
            }

            for (Long variantIdToAdd : variantIdsToAdd) {
                String title = variantTitles.get(variantIdToAdd);
                resultList.add(new VariantQuantity(variantIdToAdd, quantity, title));
            }

            for (Long variantIdToRemove : variantIdsToRemove) {
                resultList = resultList.stream().filter(v -> !v.getVariantId().equals(variantIdToRemove)).collect(Collectors.toList());
            }
        }

        return resultList;
    }

    public Set<String> getCombinationsToSwap(Set<Long> variantIds, Map<String, List<Long>> productSwapMap) {

        Map<String, Set<Long>> contractCombinationMap = new HashMap<>();

        variantIds = variantIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        for (int i = 1; i <= variantIds.size(); i++) {
            Set<Set<Long>> combinations = Sets.combinations(variantIds, i);

            for (Set<Long> combination : combinations) {
                String contractVariantIdCombination = new ArrayList<>(combination).stream().sorted().map(Object::toString).reduce((i1, i2) -> i1 + "_" + i2).get();
                contractCombinationMap.put(contractVariantIdCombination, combination);
            }
        }

        Set<Long> variantIdsAlreadyTaken = new HashSet<>();

        Set<String> combinationsToSwap = new HashSet<>();

        for (Map.Entry<String, List<Long>> entry : productSwapMap.entrySet()) {
            if (contractCombinationMap.containsKey(entry.getKey())) {
                Set<Long> contractCombinationVariantIds = contractCombinationMap.get(entry.getKey());

                boolean variantAlreadyDoesNotExists = Collections.disjoint(variantIdsAlreadyTaken, contractCombinationVariantIds);

                if (!variantAlreadyDoesNotExists) {
                    continue;
                }

                combinationsToSwap.add(entry.getKey());
                variantIdsAlreadyTaken.addAll(contractCombinationVariantIds);
            }
        }

        return combinationsToSwap;
    }

    public ZonedDateTime nextIntervalDate(ZonedDateTime lastFulfillmentDate, String deliveryPolicyInterval, Integer deliveryPolicyIntervalCount) {
        ZonedDateTime newDate = null;
        if (deliveryPolicyInterval.equalsIgnoreCase("day")) {
            newDate = lastFulfillmentDate.plusDays(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("month")) {
            newDate = lastFulfillmentDate.plusMonths(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("week")) {
            newDate = lastFulfillmentDate.plusWeeks(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("year")) {
            newDate = lastFulfillmentDate.plusYears(deliveryPolicyIntervalCount);
        }
        return newDate;
    }

    public LocalDateTime nextIntervalDate(LocalDateTime lastFulfillmentDate, String deliveryPolicyInterval, Integer deliveryPolicyIntervalCount) {
        LocalDateTime newDate = null;
        if (deliveryPolicyInterval.equalsIgnoreCase("day")) {
            newDate = lastFulfillmentDate.plusDays(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("month")) {
            newDate = lastFulfillmentDate.plusMonths(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("week")) {
            newDate = lastFulfillmentDate.plusWeeks(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("year")) {
            newDate = lastFulfillmentDate.plusYears(deliveryPolicyIntervalCount);
        }
        return newDate;
    }

    public LocalDateTime previousIntervalDate(LocalDateTime lastFulfillmentDate, String deliveryPolicyInterval, Integer deliveryPolicyIntervalCount) {
        LocalDateTime newDate = null;
        if (deliveryPolicyInterval.equalsIgnoreCase("day")) {
            newDate = lastFulfillmentDate.minusDays(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("month")) {
            newDate = lastFulfillmentDate.minusMonths(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("week")) {
            newDate = lastFulfillmentDate.minusWeeks(deliveryPolicyIntervalCount);
        } else if (deliveryPolicyInterval.equalsIgnoreCase("year")) {
            newDate = lastFulfillmentDate.minusYears(deliveryPolicyIntervalCount);
        }
        return newDate;
    }

    /**
     * @return random UUID string with time based token.
     * We can get time of this token anytime for extra validatiomonins of token.
     */
    @NotNull
    public static String generateRandomUid() {
        return RandomStringUtils.random(16, true, true);
    }

    public void throwExceptionIfCustomerIdNotFound(Optional<Long> customerIdByCustomerUid) {
        if (customerIdByCustomerUid.isEmpty()) {
            throw new BadRequestAlertException("Customer Token is incorrect or expired.", "ENTITY_NAME", "customerTokenExpired");
        }
    }

    public static String convertDateInLanguage(ZonedDateTime date, String selectedLocale) {
        DateTimeFormatter dateTimeFormatter;

        try {
            String[] localeArray = selectedLocale.split("-");
            Locale locale = new Locale(localeArray[0], localeArray[1]);
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        } catch (Exception e) {
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(new Locale("en", "US"));
        }
        return date.format(dateTimeFormatter);
    }

    public static List<String> convertCommaSeparatedStringToList(String productIdsString) {
        return Arrays.asList(org.apache.commons.lang3.StringUtils.split(productIdsString, ","));
    }

    public Boolean isShopifyThemeVersionTwo(ShopifyAPI api, GetThemesResponse.BasicThemeInfo publishedTheme) {

        Set<String> nameList = Set.of("templates/product.json", "templates/cart.json",
            "templates/collection.json", "templates/index.json");

        List<Asset> assets = api.getAssets(publishedTheme.getId()).getAssets();

        List<Asset> templateThemeFiles = assets.stream()
            .filter(a -> nameList.contains(a.getKey())).collect(Collectors.toList());

        Boolean isShopifyThemeVersionTwo = false;

        isShopifyThemeVersionTwo = templateThemeFiles.size() > 2;
        return isShopifyThemeVersionTwo;
    }

    public Optional<ShopInfoDTO> getShopInfoByAPIKey(String apiKey) {
        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        return shopInfoOptional;
    }

    public Optional<String> getShopByAPIKey(String apiKey) {
        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        return shopInfoOptional.map(ShopInfoDTO::getShop);
    }

    public void restrictV1APIRequestFromCustomerPortal(HttpServletRequest request) {
        /*Optional.ofNullable(request).map(r -> {
            List<String> headers = new ArrayList<>();
            String origin = Optional.ofNullable(request.getHeader("origin")).orElse("");
            String referer = Optional.ofNullable(request.getHeader("referer")).orElse("");
            String shop = Optional.ofNullable(request.getParameter("shop")).orElse("");
            boolean isInvalidAPICall = false;
            if (!request.getServletPath().contains("/external/v2")
                && !referer.contains("https://subscription-admin.appstle.com")
                && !origin.contains("https://subscription-admin.appstle.com")) {
                headers.add("version: V1");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(shop) && !referer.equalsIgnoreCase("http://localhost:9008/")) {
                    Optional<ShopInfoDTO> shopInfoDTO = shopInfoService.findByShop(shop);
                    if (shopInfoDTO.isPresent() && !shopInfoDTO.get().isAllowV1FormCustomerPortal()) {
                        isInvalidAPICall = true;
                    }
                }
            } else {
                headers.add("version: V2");
            }
            headers.add("Path: " + request.getServletPath());
            headers.add("referer: " + referer);
            headers.add("origin: " + origin);
            headers.add("shop: " + shop);
            headers.add("isInvalidAPICall: " + isInvalidAPICall);
            //log.info("CROSS_ORIGIN_API: {}", String.join(" | ", headers));

            if (org.apache.commons.lang3.StringUtils.isBlank(referer) && org.apache.commons.lang3.StringUtils.isEmpty(origin)) {
                //log.info("referer and origin not found for shop=" + shop);
            }

            if (isInvalidAPICall) {
                throw new BadRequestAlertException("You don't have access to this API. Please contact Appstle support team.", "CUSTOMER_PORTAL", "invalidV1APICallFromCustomerPortal");
            }
            return null;
        }).orElse("REQUEST_NOT_FOUND");*/
    }

    public void checkIsFreezeOrderTillMinCycleCondition(Long contractId, String shop) {
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get();
        List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
        if (customerPortalSettingsDTO.isFreezeOrderTillMinCycle() && (successAttempts.size() + 1) < Optional.ofNullable(subscriptionContractDetails.getMinCycles()).orElse(1)) {
            throw new BadRequestAlertException(customerPortalSettingsDTO.getFreezeUpdateSubscriptionMessage().replace("{{minCycles}}", Optional.ofNullable(subscriptionContractDetails.getMinCycles()).orElse(1).toString()), "", "minCycleNotCompleteForUpdateSubscriptionContractDate");
        }
    }

    public void checkAttributeBasedMinCycles(String shop, Long subscriptionContractId) {
        checkAttributeBasedMinCycles(shop, subscriptionContractId, null);
    }

    public void checkAttributeBasedMinCycles(String shop, Long subscriptionContractId, String lineId) {

        ShopifyGraphqlClient shopifyGraphqlClient = prepareShopifyGraphqlClient(shop);

        // Check Line based cancellation
        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = null;
        try {
            subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
        } catch (Exception e) {
            log.info("Error in getting contract from shopify for contractId, Exception={}", subscriptionContractId, ExceptionUtils.getStackTrace(e));
        }

        if (subscriptionContractQueryResponse != null) {
            //getting all line items in array
            List<SubscriptionContractQuery.Node> existingLineItems =
                requireNonNull(subscriptionContractQueryResponse.getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

            for (SubscriptionContractQuery.Node line : existingLineItems) {
                if (lineId != null && !lineId.equalsIgnoreCase(line.getId())) {
                    continue;
                }

                // Check line based min_cycles for cancellation
                String lineMinCyclesValue = getAttributeValue(line.getCustomAttributes(), AppstleAttribute.MIN_CYCLES.getKey());

                if (org.apache.commons.lang3.StringUtils.isNotBlank(lineMinCyclesValue)) {
                    Integer lineMinCycles = null;
                    try {
                        lineMinCycles = Integer.parseInt(lineMinCyclesValue.trim());
                    } catch (NumberFormatException nfe) {
                        log.info("Error in parsing line min cycle attribute value. contractId={}, lineMinCyclesValue={}, Exception={}", subscriptionContractId, lineMinCyclesValue, ExceptionUtils.getStackTrace(nfe));
                    }

                    if (lineMinCycles != null) {
                        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
                        List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractId, BillingAttemptStatus.SUCCESS);
                        String errorMessage = customerPortalSettingsDTO.getFreezeUpdateSubscriptionMessage();

                        if (StringUtils.isEmpty(errorMessage)) {
                            errorMessage = "Subscription cannot be modified until {{minCycles}} orders have completed.";
                        }

                        if ((successAttempts.size() + 1) < lineMinCycles) {
                            throw new BadRequestAlertException(errorMessage.replace("{{minCycles}}", lineMinCycles.toString()), "", "");
                        }
                    }
                }
            }
        }
    }

    public void checkCustomerPermissions(String shop, String permission) {
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();

        switch (permission){
            case "editProduct" : {
                if(BooleanUtils.isFalse(customerPortalSettingsDTO.isEditProductFlag())) {
                    throw new BadRequestAlertException("Subscription cannot be modified","","");
                }
                break;
            }
            case "swapProductVariant": {
                if(BooleanUtils.isFalse(customerPortalSettingsDTO.getEnableSwapProductVariant()) && BooleanUtils.isFalse(customerPortalSettingsDTO.getEnableSwapProductFeature())){
                    throw new BadRequestAlertException("Subscription cannot be modified","","");
                }
                break;
            }
            case "addProductVariant": {
                if(BooleanUtils.isFalse(customerPortalSettingsDTO.getAddAdditionalProduct())){
                    throw new BadRequestAlertException("Subscription cannot be modified","","");
                }
                break;
            }
            case "addOneTimeProductVariant": {
                if (BooleanUtils.isFalse(customerPortalSettingsDTO.isAddOneTimeProduct())) {
                    throw new BadRequestAlertException("Subscription cannot be modified", "", "");
                }
                break;
            }
            case "pauseResumeSub": {
                if (BooleanUtils.isFalse(customerPortalSettingsDTO.isPauseResumeSub())) {
                    throw new BadRequestAlertException("Subscription cannot be modified", "", "");
                }
                break;
            }
        }

    }

    public void getImageDimensions(EmailTemplateSetting emailTemplateSetting, EmailModel model) {
        model.setLogoHeight(emailTemplateSetting.getLogoHeight());
        model.setLogoWidth(emailTemplateSetting.getLogoWidth());
        model.setLogoAlignment(emailTemplateSetting.getLogoAlignment());
        model.setThanksImageHeight(emailTemplateSetting.getThanksImageHeight());
        model.setThanksImageWidth(emailTemplateSetting.getThanksImageWidth());
        model.setThanksImageAlignment(emailTemplateSetting.getThanksImageAlignment());
    }

    public void validateCustomerRequestForContract(Long contractId) {

        if (getCustomerToken().isEmpty()) {
            if (isCustomerPortalRequest()) {
                if (((ExternalUserPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getExternalTokenType().equals(ExternalTokenType.BUILD_A_BOX)) {
                    log.info("customer token is empty for contractId=" + contractId + " getExternalTokenType().equals(ExternalTokenType.BUILD_A_BOX)");
                } else {
                    log.info("customer token is empty for contractId=" + contractId + " getExternalTokenType().equals(ExternalTokenType.CUSTOMER_PORTAL)");
                }
                throw new BadRequestAlertException("Customer is invalid", "", "invalidCustomer");
            }
            return;
        }

        Optional<Long> customerIdOptional = customerPaymentService.getCustomerIdFromCustomerUid(getCustomerToken().get());

        if (customerIdOptional.isEmpty()) {
            throw new BadRequestAlertException("Customer does not exist", "", "invalidCustomer");
        }

        Long customerId = customerIdOptional.get();

        Optional<SubscriptionContractDetails> subscriptionContractDetailsOptional = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);

        if (!customerId.equals(subscriptionContractDetailsOptional.get().getCustomerId())) {
            throw new BadRequestAlertException("Customer is invalid", "", "invalidCustomer");
        }
    }

    public static ZonedDateTime getMinDate(ZonedDateTime... dates) {
        if (dates != null && dates.length > 0) {
            return Arrays.stream(dates).min(Comparator.comparing(ZonedDateTime::toEpochSecond)).get();
        }
        return null;
    }

    public static ZonedDateTime getMaxDate(ZonedDateTime... dates) {
        if (dates != null && dates.length > 0) {
            return Arrays.stream(dates).max(Comparator.comparing(ZonedDateTime::toEpochSecond)).get();
        }
        return null;
    }

    public static ZoneId getZoneIdFromOffset(int offsetHours, int offsetMinutes) {
        NumberFormat hoursNF = new DecimalFormat("+00;-00");
        NumberFormat minutesNF = new DecimalFormat("00");
        String offset = hoursNF.format(offsetHours) + ":" + minutesNF.format(offsetMinutes);

        return ZoneId.of(offset);
    }

    public String getAttributeValue(List<SubscriptionContractQuery.CustomAttribute> customAttributes, String key) {
        try {
            if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(customAttributes)) {
                Optional<SubscriptionContractQuery.CustomAttribute> customAttribute = customAttributes.stream().filter(ca -> ca.getKey().equalsIgnoreCase(key)).findFirst();

                if (customAttribute.isPresent()) {
                    return customAttribute.get().getValue().orElseGet(null);
                }
            }
        } catch (Exception e) {
            log.info("Error while getting attribute value. error={}", ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    public boolean isAttributePresent(List<SubscriptionContractQuery.CustomAttribute> customAttributes, String key) {

        try {
            if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(customAttributes)) {
                return customAttributes.stream().anyMatch(ca -> ca.getKey().equalsIgnoreCase(key));
            }
        } catch (Exception e) {
            log.info("Error while getting attribute value. error={}", ExceptionUtils.getStackTrace(e));
        }

        return false;
    }

    public boolean newApproachFor(String shop, String approachKey) {
        try {
            Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByShop(shop);
            if (shopInfoOptional.isEmpty()) {
                return false;
            }

            ShopInfoDTO shopInfo = shopInfoOptional.get();
            String overwriteSetting = shopInfo.getOverwriteSetting();

            if (org.apache.commons.lang3.StringUtils.isBlank(overwriteSetting)) {
                return false;
            }


            JSONObject jsonObject = new JSONObject(overwriteSetting);
            return jsonObject.getBoolean(approachKey);
        } catch (Exception ex) {
            log.error("An exception occurred while reading json for new Swap Approach. ex=" + ExceptionUtils.getStackTrace(ex));
        }
        return false;
    }

    public Optional<ShippingAddressModel> getContractShippingAddress(SubscriptionContractQuery.SubscriptionContract subscriptionContract){
        return subscriptionContract.getDeliveryMethod()
            .map(f -> {
                if (f instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) {
                    Optional<SubscriptionContractQuery.Address> shippingAddressOptional = Optional.of(((SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) f).getAddress());
                    Optional<SubscriptionContractQuery.ShippingOption> shippingOptionOptional = Optional.of(((SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) f).getShippingOption());

                    ShippingAddressModel shippingAddressModel = new ShippingAddressModel(
                        shippingAddressOptional.map(a -> a.getAddress1().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getCity().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getProvinceCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getZip().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getFirstName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getLastName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getAddress2().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getCountry().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getCountryCode().map(Object::toString).orElse(CountryCode.$UNKNOWN.rawValue())).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getPhone().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        shippingAddressOptional.map(a -> a.getCompany().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        org.apache.commons.lang3.StringUtils.EMPTY,
                        shippingAddressOptional.map(a -> a.getProvince().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        "SHIPPING"
                    );

                    if(shippingOptionOptional.isPresent()){
                        ShippingOptionModel shippingOptionModel = new ShippingOptionModel(
                            shippingOptionOptional.map(a -> a.getTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            shippingOptionOptional.map(a -> a.getPresentmentTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            shippingOptionOptional.map(a -> a.getDescription().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            shippingOptionOptional.map(a -> a.getCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            org.apache.commons.lang3.StringUtils.EMPTY
                        );
                        shippingAddressModel.setShippingOption(shippingOptionModel);
                    }

                    return shippingAddressModel;

                } else if (f instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) {
                    Optional<SubscriptionContractQuery.Address1> localDeliveryAddressOptional = Optional.of(((SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) f).getAddress());
                    Optional<SubscriptionContractQuery.LocalDeliveryOption> localDeliveryOptionOptional = Optional.of(((SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) f).getLocalDeliveryOption());

                    ShippingAddressModel shippingAddressModel = new ShippingAddressModel(
                        localDeliveryAddressOptional.map(a -> a.getAddress1().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getCity().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getProvinceCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getZip().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getFirstName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getLastName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getAddress2().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getCountry().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getCountryCode().map(Object::toString).orElse(CountryCode.$UNKNOWN.rawValue())).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getPhone().orElse(localDeliveryOptionOptional.map(SubscriptionContractQuery.LocalDeliveryOption::getPhone).orElse(org.apache.commons.lang3.StringUtils.EMPTY))).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localDeliveryAddressOptional.map(a -> a.getCompany().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        org.apache.commons.lang3.StringUtils.EMPTY,
                        localDeliveryAddressOptional.map(a -> a.getProvince().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        "LOCAL"
                    );

                    if(localDeliveryOptionOptional.isPresent()){
                        ShippingOptionModel shippingOptionModel = new ShippingOptionModel(
                            localDeliveryOptionOptional.map(a -> a.getTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            localDeliveryOptionOptional.map(a -> a.getPresentmentTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            localDeliveryOptionOptional.map(a -> a.getDescription().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            localDeliveryOptionOptional.map(a -> a.getCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            localDeliveryOptionOptional.map(SubscriptionContractQuery.LocalDeliveryOption::getPhone).orElse(subscriptionContract.getCustomer().flatMap(SubscriptionContractQuery.Customer::getPhone).orElse("1111111111"))
                        );
                        shippingAddressModel.setShippingOption(shippingOptionModel);
                    }

                    return shippingAddressModel;

                } else if (f instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodPickup) {
                    Optional<SubscriptionContractQuery.PickupOption> pickupOptionOptional = Optional.of(((SubscriptionContractQuery.AsSubscriptionDeliveryMethodPickup) f).getPickupOption());
                    Optional<SubscriptionContractQuery.Location> pickupLocation = pickupOptionOptional.map(SubscriptionContractQuery.PickupOption::getLocation);
                    Optional<SubscriptionContractQuery.Address2> localPickupAddressOptional = pickupLocation.map(SubscriptionContractQuery.Location::getAddress);

                    ShippingAddressModel shippingAddressModel = new ShippingAddressModel(
                        localPickupAddressOptional.map(a -> a.getAddress1().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getCity().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getProvinceCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getZip().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        pickupLocation.map(SubscriptionContractQuery.Location::getName).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        org.apache.commons.lang3.StringUtils.EMPTY,
                        localPickupAddressOptional.map(a -> a.getAddress2().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getCountry().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getCountryCode().map(Object::toString).orElse(CountryCode.$UNKNOWN.rawValue())).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getPhone().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        org.apache.commons.lang3.StringUtils.EMPTY,
                        pickupLocation.map(SubscriptionContractQuery.Location::getId).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        localPickupAddressOptional.map(a -> a.getProvince().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                        "PICK_UP"
                    );

                    if(pickupOptionOptional.isPresent()){
                        ShippingOptionModel shippingOptionModel = new ShippingOptionModel(
                            pickupOptionOptional.map(a -> a.getTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            pickupOptionOptional.map(a -> a.getPresentmentTitle().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            pickupOptionOptional.map(a -> a.getDescription().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            pickupOptionOptional.map(a -> a.getCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                            org.apache.commons.lang3.StringUtils.EMPTY
                        );
                        shippingAddressModel.setShippingOption(shippingOptionModel);
                    }

                    return shippingAddressModel;

                } else {
                    return null;
                }
            });
    }

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Async
    public void mayBeUpdateShippingPriceAsync(Long contractId, String shop){
        mayBeUpdateShippingPrice(contractId, shop);
    }

    public void mayBeUpdateShippingPrice(Long contractId, String shop) {
        ShopifyGraphqlClient shopifyGraphqlClient = prepareShopifyGraphqlClient(shop);
        mayBeUpdateShippingPrice(contractId, shop, shopifyGraphqlClient);
    }

    public void mayBeUpdateShippingPrice(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient)  {

        try {
            log.info("mayBeUpdateShippingPrice for contractId=" + contractId + " shop=" + shop);

            SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

            ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

            String existingTitle = "";

            String phone = "";

            if (contractId != null) {

                if (BooleanUtils.isTrue(shopInfo.isDisableShippingPricingAutoCalculation())) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " shopInfo.isDisableShippingPricingAutoCalculation()=" + Boolean.TRUE);
                    return;
                }

                Optional<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);
                if (Objects.requireNonNull(subscriptionContractDetails).isPresent() && BooleanUtils.isTrue(subscriptionContractDetails.get().isAllowDeliveryPriceOverride())) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionContractDetails.get().isAllowDeliveryPriceOverride()=" + subscriptionContractDetails.get().isAllowDeliveryPriceOverride());
                    return;
                }
            }
            Optional<SubscriptionContractQuery.Data> subscriptionContractResponseDataOptional = subscriptionContractResponseOptional.getData();

            if (Objects.requireNonNull(subscriptionContractResponseDataOptional).isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionContractResponseDataOptional is empty");
                return;
            }

            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = subscriptionContractResponseDataOptional.get().getSubscriptionContract();

            if (subscriptionContractOptional.isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionContractOptional is empty");
                return;
            }

            Optional<SubscriptionContractQuery.DeliveryMethod> deliveryMethodOptional = subscriptionContractOptional.get().getDeliveryMethod();

            if (deliveryMethodOptional.isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " deliveryMethodOptional is empty");
                return;
            }

            SubscriptionContractQuery.DeliveryMethod existingDeliveryMethod = deliveryMethodOptional.get();


            MailingAddressInput.Builder mailingAddressInputBuilder = MailingAddressInput.builder();

            if(existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) {
                SubscriptionContractQuery.Address address = ((SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) deliveryMethodOptional.get()).getAddress();

                if (address.getProvinceCode().isPresent()) {
                    mailingAddressInputBuilder.provinceCode(address.getProvinceCode().get());
                }

                if (address.getZip().isPresent()) {
                    mailingAddressInputBuilder.zip(address.getZip().get());
                }

                if (address.getAddress1().isPresent()) {
                    mailingAddressInputBuilder.address1(address.getAddress1().get());
                }

                if (address.getAddress2().isPresent()) {
                    mailingAddressInputBuilder.address2(address.getAddress2().get());
                }

                if (address.getCity().isPresent()) {
                    mailingAddressInputBuilder.city(address.getCity().get());
                }

                if (address.getCountryCode().isPresent()) {
                    mailingAddressInputBuilder.countryCode(address.getCountryCode().get());
                }

                if (address.getPhone().isPresent()) {
                    mailingAddressInputBuilder.phone(address.getPhone().get());
                }

                SubscriptionContractQuery.ShippingOption shippingOption = ((SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) deliveryMethodOptional.get()).getShippingOption();

                existingTitle = shippingOption.getTitle().orElse("");

            } else if(existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) {
                SubscriptionContractQuery.Address1 address = ((SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) deliveryMethodOptional.get()).getAddress();

                if (address.getProvinceCode().isPresent()) {
                    mailingAddressInputBuilder.provinceCode(address.getProvinceCode().get());
                }

                if (address.getZip().isPresent()) {
                    mailingAddressInputBuilder.zip(address.getZip().get());
                }

                if (address.getAddress1().isPresent()) {
                    mailingAddressInputBuilder.address1(address.getAddress1().get());
                }

                if (address.getAddress2().isPresent()) {
                    mailingAddressInputBuilder.address2(address.getAddress2().get());
                }

                if (address.getCity().isPresent()) {
                    mailingAddressInputBuilder.city(address.getCity().get());
                }

                if (address.getCountryCode().isPresent()) {
                    mailingAddressInputBuilder.countryCode(address.getCountryCode().get());
                }

                if (address.getPhone().isPresent()) {
                    mailingAddressInputBuilder.phone(address.getPhone().get());
                }

                SubscriptionContractQuery.LocalDeliveryOption shippingOption = ((SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) deliveryMethodOptional.get()).getLocalDeliveryOption();

                existingTitle = shippingOption.getTitle().orElse("");
                phone = shippingOption.getPhone();
            } else if(existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodPickup) {
                SubscriptionContractQuery.PickupOption pickupOption = ((SubscriptionContractQuery.AsSubscriptionDeliveryMethodPickup) deliveryMethodOptional.get()).getPickupOption();

                if (pickupOption.getLocation().getAddress().getProvinceCode().isPresent()) {
                    mailingAddressInputBuilder.provinceCode(pickupOption.getLocation().getAddress().getProvinceCode().get());
                }

                if (pickupOption.getLocation().getAddress().getZip().isPresent()) {
                    mailingAddressInputBuilder.zip(pickupOption.getLocation().getAddress().getZip().get());
                }

                if (pickupOption.getLocation().getAddress().getAddress1().isPresent()) {
                    mailingAddressInputBuilder.address1(pickupOption.getLocation().getAddress().getAddress1().get());
                }

                if (pickupOption.getLocation().getAddress().getAddress2().isPresent()) {
                    mailingAddressInputBuilder.address2(pickupOption.getLocation().getAddress().getAddress2().get());
                }

                if (pickupOption.getLocation().getAddress().getCity().isPresent()) {
                    mailingAddressInputBuilder.city(pickupOption.getLocation().getAddress().getCity().get());
                }

                if (pickupOption.getLocation().getAddress().getCountryCode().isPresent()) {
                    mailingAddressInputBuilder.countryCode(CountryCode.safeValueOf(pickupOption.getLocation().getAddress().getCountryCode().get()));
                }

                if (pickupOption.getLocation().getAddress().getPhone().isPresent()) {
                    mailingAddressInputBuilder.phone(pickupOption.getLocation().getAddress().getPhone().get());
                }

                existingTitle = pickupOption.getTitle().orElse("");
            }

            MailingAddressInput mailingAddressInput = mailingAddressInputBuilder
                .build();


            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractUpdateMutation.Data>> subscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            if (!CollectionUtils.isEmpty(subscriptionContractUpdateMutationResponse.getErrors())) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionContractUpdateMutationResponse=" + subscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                return;
            }

            List<SubscriptionContractUpdateMutation.UserError> subscriptionContractUpdateMutationResponseUserErrors = Objects.requireNonNull(subscriptionContractUpdateMutationResponse.getData())
                .map(d -> d.getSubscriptionContractUpdate()
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            if (!subscriptionContractUpdateMutationResponseUserErrors.isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionContractUpdateMutationResponseUserErrors=" + subscriptionContractUpdateMutationResponseUserErrors.get(0).getMessage());
                return;
            }

            String draftId = subscriptionContractUpdateMutationResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .flatMap(draft -> draft.map(SubscriptionContractUpdateMutation.Draft::getId))
                .orElse(null);

            SubscriptionDraftShippingRateQuery subscriptionDraftShippingRateQuery = new SubscriptionDraftShippingRateQuery(draftId, mailingAddressInput);
            Response<Optional<SubscriptionDraftShippingRateQuery.Data>> subscriptionDraftShippingRateQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionDraftShippingRateQuery);

            if (!CollectionUtils.isEmpty(subscriptionDraftShippingRateQueryResponse.getErrors())) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponse.getErrors()=" + subscriptionDraftShippingRateQueryResponse.getErrors().get(0).getMessage());
                return;
            }

            if (Objects.requireNonNull(subscriptionDraftShippingRateQueryResponse.getData()).isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponse.getData() is empty");
                return;
            }

            SubscriptionDraftShippingRateQuery.Data subscriptionDraftShippingRateQueryResponseData = subscriptionDraftShippingRateQueryResponse.getData().get();

            if (subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().isEmpty()");
                return;
            }

            Optional<SubscriptionDraftShippingRateQuery.DeliveryOptions> deliveryOptions = subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().get().getDeliveryOptions();

            int count = 0;
            while (deliveryOptions.isEmpty() && count < 6) {
                Thread.sleep(1000L);
                subscriptionDraftShippingRateQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionDraftShippingRateQuery);
                if (!CollectionUtils.isEmpty(subscriptionDraftShippingRateQueryResponse.getErrors())) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponse.getErrors().isEmpty()");
                    continue;
                }

                if (Objects.requireNonNull(subscriptionDraftShippingRateQueryResponse.getData()).isEmpty()) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponse.getData().isEmpty()");
                    continue;
                }

                subscriptionDraftShippingRateQueryResponseData = subscriptionDraftShippingRateQueryResponse.getData().get();

                if (subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().isEmpty()) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().isEmpty()");
                    continue;
                }

                deliveryOptions = subscriptionDraftShippingRateQueryResponseData.getSubscriptionDraft().get().getDeliveryOptions();
                count++;
            }

            if (deliveryOptions.isEmpty()) {
                log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " shippingOptions.isEmpty()");
                return;
            }

            SubscriptionDraftShippingRateQuery.DeliveryOptions deliveryOptions1 = deliveryOptions.get();
            if (deliveryOptions1 instanceof SubscriptionDraftShippingRateQuery.AsSubscriptionDeliveryOptionResultSuccess) {

                if (((SubscriptionDraftShippingRateQuery.AsSubscriptionDeliveryOptionResultSuccess) deliveryOptions1).getDeliveryOptions().isEmpty()) {
                    log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " ((SubscriptionDraftShippingRateQuery.AsSubscriptionShippingOptionResultSuccess) shippingOptions1).getShippingOptions().isEmpty()");
                }

                for (SubscriptionDraftShippingRateQuery.DeliveryOption deliveryOption : ((SubscriptionDraftShippingRateQuery.AsSubscriptionDeliveryOptionResultSuccess) deliveryOptions1).getDeliveryOptions()) {
                    Double amount = null;
                    String title = "";
                    SubscriptionDeliveryMethodInput subscriptionDeliveryMethodInput = null;

                    if((existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping)
                        && (deliveryOption instanceof  SubscriptionDraftShippingRateQuery.AsSubscriptionShippingOption)) {
                        Optional<SubscriptionDraftShippingRateQuery.Price> deliveryPriceShipping = ((SubscriptionDraftShippingRateQuery.AsSubscriptionShippingOption) deliveryOption).getPrice();
                        if(deliveryPriceShipping.isPresent()) {
                            amount = Double.parseDouble(deliveryPriceShipping.get().getAmount().toString());
                            title = ((SubscriptionDraftShippingRateQuery.AsSubscriptionShippingOption) deliveryOption).getTitle();

                            SubscriptionDeliveryMethodShippingOptionInput subscriptionDeliveryMethodShippingOptionInput = SubscriptionDeliveryMethodShippingOptionInput
                                .builder()
                                .code(title)
                                .title(title)
                                .presentmentTitle(title)
                                .build();

                            SubscriptionDeliveryMethodShippingInput subscriptionDeliveryMethodShippingInput = SubscriptionDeliveryMethodShippingInput
                                .builder()
                                .shippingOption(subscriptionDeliveryMethodShippingOptionInput)
                                .build();

                            subscriptionDeliveryMethodInput = SubscriptionDeliveryMethodInput
                                .builder()
                                .shipping(subscriptionDeliveryMethodShippingInput)
                                .build();
                        }

                    } else if((existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) && (deliveryOption instanceof  SubscriptionDraftShippingRateQuery.AsSubscriptionLocalDeliveryOption)) {
                        Optional<SubscriptionDraftShippingRateQuery.Price1> deliveryPriceLocalDelivery = ((SubscriptionDraftShippingRateQuery.AsSubscriptionLocalDeliveryOption) deliveryOption).getPrice();
                        if(deliveryPriceLocalDelivery.isPresent()) {
                            amount = Double.parseDouble(deliveryPriceLocalDelivery.get().getAmount().toString());
                            title = ((SubscriptionDraftShippingRateQuery.AsSubscriptionLocalDeliveryOption) deliveryOption).getTitle();

                            SubscriptionDeliveryMethodLocalDeliveryOptionInput subscriptionDeliveryMethodLocalDeliveryOptionInput = SubscriptionDeliveryMethodLocalDeliveryOptionInput
                                .builder()
                                .code(title)
                                .title(title)
                                .presentmentTitle(title)
                                .phone(phone)
                                .build();

                            SubscriptionDeliveryMethodLocalDeliveryInput subscriptionDeliveryMethodLocalDeliveryInput = SubscriptionDeliveryMethodLocalDeliveryInput
                                .builder()
                                .localDeliveryOption(subscriptionDeliveryMethodLocalDeliveryOptionInput)
                                .build();

                            subscriptionDeliveryMethodInput = SubscriptionDeliveryMethodInput
                                .builder()
                                .localDelivery(subscriptionDeliveryMethodLocalDeliveryInput)
                                .build();
                        }

                    } else if((existingDeliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodPickup)
                        && (deliveryOption instanceof  SubscriptionDraftShippingRateQuery.AsSubscriptionPickupOption)) {
                        Optional<SubscriptionDraftShippingRateQuery.Price2> deliveryPricePickup = ((SubscriptionDraftShippingRateQuery.AsSubscriptionPickupOption) deliveryOption).getPrice();
                        if(deliveryPricePickup.isPresent()) {
                            amount = Double.parseDouble(deliveryPricePickup.get().getAmount().toString());
                            title = "PICK_UP";
                            String locationId = ((SubscriptionDraftShippingRateQuery.AsSubscriptionPickupOption) deliveryOption).getLocation().getId();

                            SubscriptionDeliveryMethodPickupOptionInput subscriptionDeliveryMethodPickupOptionInput = SubscriptionDeliveryMethodPickupOptionInput
                                .builder()
                                .code(title)
                                .title(title)
                                .presentmentTitle(title)
                                .locationId(locationId)
                                .build();

                            SubscriptionDeliveryMethodPickupInput subscriptionDeliveryMethodPickupInput = SubscriptionDeliveryMethodPickupInput
                                .builder()
                                .pickupOption(subscriptionDeliveryMethodPickupOptionInput)
                                .build();

                            subscriptionDeliveryMethodInput = SubscriptionDeliveryMethodInput
                                .builder()
                                .pickup(subscriptionDeliveryMethodPickupInput)
                                .build();
                        }

                    }

                    if (amount != null) {

                        Double existingDeliveryPrice = subscriptionContractResponseDataOptional
                            .flatMap(d -> d.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getDeliveryPrice)
                                .map(SubscriptionContractQuery.DeliveryPrice::getAmount)
                                .map(Objects::toString)
                                .map(Double::parseDouble))
                            .orElse(0.0d);

                        if (amount.compareTo(existingDeliveryPrice) == 0 && title.equalsIgnoreCase(existingTitle)) {

                            log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " shippingOption.getPrice().get().getAmount().equals(existingDeliveryPrice)");
                            Map<String, Object> map = new HashMap<>();
                            map.put("reason", "Shipping price or title hasn't changed.");
                            writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SHOPIFY_EVENT, ActivityLogEventType.SYSTEM_UPDATED_DELIVERY_PRICE, ActivityLogStatus.INFO, map);
                            return;
                        }

                        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                        subscriptionDraftInputBuilder.deliveryPrice(amount);
                        if (ObjectUtils.isNotEmpty(subscriptionDeliveryMethodInput)) {
                            subscriptionDraftInputBuilder.deliveryMethod(subscriptionDeliveryMethodInput);
                        }
                        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                        SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(draftId, subscriptionDraftInput);
                        Response<Optional<SubscriptionDraftUpdateMutation.Data>> optionalDraftUpdateResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftUpdateMutation);


                        if (!CollectionUtils.isEmpty(optionalDraftUpdateResponse.getErrors())) {
                            log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " optionalDraftUpdateResponse.getErrors()=" + optionalDraftUpdateResponse.getErrors().get(0).getMessage());
                            return;
                        }

                        List<SubscriptionDraftUpdateMutation.UserError> optionalDraftUpdateResponseUserErrors = Objects.requireNonNull(optionalDraftUpdateResponse.getData())
                            .map(d -> d.getSubscriptionDraftUpdate()
                                .map(SubscriptionDraftUpdateMutation.SubscriptionDraftUpdate::getUserErrors)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>());

                        if (!optionalDraftUpdateResponseUserErrors.isEmpty()) {
                            log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " optionalDraftUpdateResponseUserErrors=" + optionalDraftUpdateResponseUserErrors.get(0).getMessage());
                            return;
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (!CollectionUtils.isEmpty(optionalDraftCommitResponse.getErrors())) {
                            log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " optionalDraftCommitResponse.getErrors()=" + optionalDraftCommitResponse.getErrors().get(0).getMessage());
                            return;
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = Objects.requireNonNull(optionalDraftCommitResponse.getData())
                            .map(d -> d.getSubscriptionDraftCommit()
                                .map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>());

                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            log.info("Not able to update delivery profile for contractId=" + contractId + " shop=" + shop + " optionalDraftCommitResponseUserErrors=" + optionalDraftCommitResponseUserErrors.get(0).getMessage());
                            return;
                        }

                        log.info("Successfully updated delivery profile for contractId=" + contractId + " shop=" + shop + " with amount=" + amount + " oldPrice=" + existingDeliveryPrice + "with title=" + title + "oldTitle" + existingTitle);

                        Map<String, Object> additionalInfo = new HashMap<>();
                        additionalInfo.put("oldShippingPrice", existingDeliveryPrice);
                        additionalInfo.put("newShippingPrice", amount);
                        additionalInfo.put("newShippingTitle", title);
                        additionalInfo.put("oldShippingTitle", existingTitle);

                        writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SHOPIFY_EVENT, ActivityLogEventType.SYSTEM_UPDATED_DELIVERY_PRICE, ActivityLogStatus.SUCCESS, additionalInfo);

                    }
                }
            }

            if (deliveryOptions1 instanceof SubscriptionDraftShippingRateQuery.AsSubscriptionDeliveryOptionResultFailure) {
                Optional<String> messageOptional = ((SubscriptionDraftShippingRateQuery.AsSubscriptionDeliveryOptionResultFailure) deliveryOptions1).getMessage();
                if (messageOptional.isPresent()) {
                    String message = messageOptional.get();
                }
            }
        } catch (Exception ex) {
            log.error("ex=" + ExceptionUtils.getStackTrace(ex) + " shop=" + shop);
        }
    }

    public List<String> checkOutOfStockProduct(Long contractId, String shop, ShopInfoDTO shopInfoDTO) {

        ShopifyGraphqlClient shopifyGraphqlClient = prepareShopifyGraphqlClient(shop);

        List<String> oosVariantIds = new ArrayList<>();

        try {
            SubscriptionContractVariantsQuery subscriptionContractVariantsQuery = new SubscriptionContractVariantsQuery(ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractVariantsQuery.Data>> subscriptionContractVariantIdQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractVariantsQuery);

            List<SubscriptionContractVariantsQuery.Node> lines = Objects.requireNonNull(subscriptionContractVariantIdQueryResponse
                    .getData())
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractVariantsQuery.SubscriptionContract::getLines)
                    .map(SubscriptionContractVariantsQuery.Lines::getEdges)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractVariantsQuery.Edge::getNode)
                .collect(Collectors.toList());


            List<String> variantIds = lines.stream()
                .map(SubscriptionContractVariantsQuery.Node::getVariantId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());

            List<String> deletedVariantTitles = lines.stream()
                .filter(n -> n.getVariantId().isEmpty())
                .map(n -> n.getTitle() + " -" + n.getVariantTitle().orElse(""))
                .distinct()
                .collect(Collectors.toList());


            Map<String, Object> map = new HashMap<>();

            // Check inventory read access:
            AccessScopesQuery accessScopesQuery = new AccessScopesQuery();
            Response<Optional<AccessScopesQuery.Data>> accessScopesResponse = shopifyGraphqlClient.getOptionalQueryResponse(accessScopesQuery);

            boolean hasInventoryReadAccess = Objects.requireNonNull(accessScopesResponse.getData())
                .flatMap(AccessScopesQuery.Data::getAppInstallation)
                .map(AccessScopesQuery.AppInstallation::getAccessScopes).orElse(new ArrayList<>())
                .stream()
                .anyMatch(as -> "read_inventory".equalsIgnoreCase(as.getHandle()));

            if (hasInventoryReadAccess) {

                ProductVariantInventoryNodesQuery productVariantInventoryNodesQuery = new ProductVariantInventoryNodesQuery(variantIds);
                Response<Optional<ProductVariantInventoryNodesQuery.Data>> productVariantInventoryNodesQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantInventoryNodesQuery);

                List<ProductVariantInventoryNodesQuery.AsProductVariant> productVariantList = Objects.requireNonNull(productVariantInventoryNodesQueryResponse
                        .getData())
                    .map(ProductVariantInventoryNodesQuery.Data::getNodes)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(s -> (ProductVariantInventoryNodesQuery.AsProductVariant) s)
                    .collect(Collectors.toList());

                Map<String, Integer> requiredVariantQuantityMap = new HashMap<>();
                variantIds.forEach(variantId -> {
                    int variantQuantity = lines.stream().filter(n -> n.getVariantId().isPresent() && n.getVariantId().get().equals(variantId))
                        .mapToInt(SubscriptionContractVariantsQuery.Node::getQuantity)
                        .sum();
                    requiredVariantQuantityMap.put(variantId, variantQuantity);
                });

                final List<String> inventoryLocations = Arrays.stream(Optional.ofNullable(shopInfoDTO.getInventoryLocations()).orElse("").split(","))
                    .filter(s -> StringUtils.hasText(s))
                    .map(String::trim)
                    .map(s -> ShopifyGraphQLUtils.getGraphQlLocationId(s))
                    .distinct()
                    .collect(Collectors.toList());


                Map<String, Integer> availableVariantQuantityMap = new HashMap<>();

                for (ProductVariantInventoryNodesQuery.AsProductVariant productVariantInventory : productVariantList) {
                    if (productVariantInventory.getInventoryPolicy() == ProductVariantInventoryPolicy.DENY
                        && productVariantInventory.getInventoryItem().isTracked()) {
                        int availableVariantQuantity = productVariantInventory.getInventoryItem().getInventoryLevels().getNodes()
                            .stream()
                            .filter(n -> CollectionUtils.isEmpty(inventoryLocations) || inventoryLocations.contains(n.getLocation().getId()))
                            .map(n -> n.getQuantities())
                            .flatMap(List::stream)
                            .filter(q -> "available".equalsIgnoreCase(q.getName()))
                            .mapToInt(ProductVariantInventoryNodesQuery.Quantity::getQuantity)
                            .sum();

                        availableVariantQuantityMap.put(productVariantInventory.getId(), availableVariantQuantity);
                    }
                }

                oosVariantIds = variantIds.stream().filter(variantId -> {
                    int requiredQty = requiredVariantQuantityMap.get(variantId);
                    int availableQty = availableVariantQuantityMap.getOrDefault(variantId, Integer.MAX_VALUE); // MAX VALUE for not tracked
                    return (requiredQty > availableQty);
                }).collect(Collectors.toList());

                map.put("required", requiredVariantQuantityMap);
                map.put("available", availableVariantQuantityMap);

            } else {

                ProductVariantAvailableForSalesQuery productVariantAvailableForSalesQuery = new ProductVariantAvailableForSalesQuery(variantIds);
                Response<Optional<ProductVariantAvailableForSalesQuery.Data>> productVariantAvailableForSalesQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantAvailableForSalesQuery);

                List<ProductVariantAvailableForSalesQuery.AsProductVariant> productVariantList = Objects.requireNonNull(productVariantAvailableForSalesQueryResponse
                        .getData())
                    .map(ProductVariantAvailableForSalesQuery.Data::getNodes)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(s -> (ProductVariantAvailableForSalesQuery.AsProductVariant) s)
                    .collect(Collectors.toList());

                oosVariantIds = productVariantList.stream()
                    .filter(s -> !s.isAvailableForSale())
                    .map(s -> s.getId())
                    .distinct()
                    .collect(Collectors.toList());
            }

            List<String> oosVariantNames = new ArrayList<>();
            for (SubscriptionContractVariantsQuery.Node line : lines) {
                if (line.getVariantId().isPresent() && oosVariantIds.contains(line.getVariantId().get())) {
                    String productName = Optional.ofNullable(line.getTitle()).orElse("");
                    String variantName = "";
                    if (line.getVariantTitle().isPresent() && !line.getVariantTitle().get().equalsIgnoreCase("Default Title")) {
                        variantName = line.getVariantTitle().get();
                    }
                    productName = productName + (StringUtils.hasText(variantName) ? " -" + variantName : "");

                    oosVariantNames.add(productName);
                }
            }

            oosVariantNames.addAll(deletedVariantTitles);
        } catch (Exception e) {
            log.info("An error occurred while executing inventory Check. shop={}, contractId={}, ex={}", shop, contractId, ExceptionUtils.getStackTrace(e));
        }
        return oosVariantIds;
    }

    public void removeInvalidDiscounts(String shop, Long contractId, ShopifyGraphqlClient shopifyGraphqlClient,
                                       Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse) {
        try {

            List<SubscriptionContractQuery.Node2> invalidDiscounts = subscriptionContractResponse.getData()
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getDiscounts)
                    .map(SubscriptionContractQuery.Discounts::getEdges)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractQuery.Edge2::getNode)
                .filter(d -> d.getRejectionReason().isPresent())
                .collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(invalidDiscounts)) {

                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));

                Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                long countOfErrors = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                    .orElse(new ArrayList<>()).stream()
                    .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                    .count();


                if (countOfErrors == 0) {
                    // get draft Id from the response
                    Optional<String> optionalDraftId = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId());

                    if (optionalDraftId.isPresent()) {

                        String draftId = optionalDraftId.get();

                        for (SubscriptionContractQuery.Node2 invalidDiscount : invalidDiscounts) {
                            SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(draftId, invalidDiscount.getId());
                            shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);

                            Map<String, Object> map = new HashMap<>();
                            map.put("discountId", invalidDiscount.getId());
                            map.put("discountCode", invalidDiscount.getTitle().orElse(""));
                            map.put("reason", invalidDiscount.getRejectionReason().get().rawValue());
                            writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.DISCOUNT_REMOVED, ActivityLogStatus.SUCCESS, map);
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while removing invalid discounts. shop={}, contractId={}, error={}", shop, contractId, ExceptionUtils.getStackTrace(e));
        }
    }

    public static String appendVersionParams(String url, String shop, Object versionNumber) {
        String urlWithParams = UriComponentsBuilder.fromUriString(url)
            .queryParamIfPresent("shop", Optional.ofNullable(shop))
            .queryParamIfPresent("v", Optional.ofNullable(versionNumber))
            .encode()
            .toUriString();

        return urlWithParams;
    }

    public String getCustomerEmailById(String customerId, String shop) {

        String customerEmail = "";

        CustomerBriefQuery.Customer customer = getCustomerDataById(customerId, shop);

        if(ObjectUtils.isNotEmpty(customer)) {
            if(customer.getEmail().isPresent()) {
                customerEmail = customer.getEmail().orElse("");
            }
        }
        return customerEmail;
    }

    public CustomerBriefQuery.Customer getCustomerDataById(String customerId, String shop) {

        CustomerBriefQuery.Customer customer = null;

        ShopifyGraphqlClient shopifyGraphqlClient = prepareShopifyGraphqlClient(shop);
        try {

            CustomerBriefQuery customerBriefQuery = new CustomerBriefQuery(customerId);
            Response<Optional<CustomerBriefQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerBriefQuery);

            if (response.getData().isPresent() && response.getData().get().getCustomer().isPresent() && response.getData().get().getCustomer().get().getEmail().isPresent()) {
                customer = response.getData().get().getCustomer().get();
            }
        } catch (Exception e) {
            log.error("Error occured while getting email for customer {}", customerId);
        }
        return customer;
    }

}

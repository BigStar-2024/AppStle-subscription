package com.et.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.api.utils.CurrencyUtils;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.api.utils.SubscriptionUtils;
import com.et.constant.AppstleAttribute;
import com.et.constant.Constants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.liquid.ShippingAddressModel;
import com.et.pojo.*;
import com.et.repository.ActivityLogRepository;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.repository.UserRepository;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.service.dto.DiscountType;
import com.et.service.mapper.SubscriptionContractDetailsMapper;
import com.et.utils.*;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.*;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.*;
import static com.et.constant.Constants.DATE_TIME_STAMP_FORMAT;
import static com.et.web.rest.SubscriptionContractDetailsResource.ENTITY_NAME;
import static java.util.Objects.requireNonNull;

/**
 * Service Implementation for managing {@link SubscriptionContractDetails}.
 */
@Lazy
@Service
@Transactional
public class SubscriptionContractDetailsServiceImpl implements SubscriptionContractDetailsService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractDetailsServiceImpl.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    private final SubscriptionContractDetailsMapper subscriptionContractDetailsMapper;

    private final MailgunService mailgunService;

    private final UserRepository userRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionContractService subscriptionContractService;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private SeedsUtils seedsUtils;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private SubscriptionGroupService subscriptionGroupService;

    @Autowired
    private CommonEmailUtils commonEmailUtils;

    @Autowired
    private AwsUtils awsUtils;

    @Autowired
    private SubscriptionBillingAttemptService billingAttemptService;

    @Autowired
    private SubscriptionBundlingService subscriptionBundlingService;

    @Autowired
    private ShipperHqService shipperHqService;

    public SubscriptionContractDetailsServiceImpl(SubscriptionContractDetailsRepository subscriptionContractDetailsRepository, SubscriptionContractDetailsMapper subscriptionContractDetailsMapper, MailgunService mailgunService, UserRepository userRepository) {
        this.subscriptionContractDetailsRepository = subscriptionContractDetailsRepository;
        this.subscriptionContractDetailsMapper = subscriptionContractDetailsMapper;
        this.mailgunService = mailgunService;
        this.userRepository = userRepository;
    }

    @Override
    public SubscriptionContractDetailsDTO save(SubscriptionContractDetailsDTO subscriptionContractDetailsDTO) {
        log.debug("Request to save SubscriptionContractDetails : {}", subscriptionContractDetailsDTO);
        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsMapper.toEntity(subscriptionContractDetailsDTO);
        subscriptionContractDetails = subscriptionContractDetailsRepository.save(subscriptionContractDetails);
        return subscriptionContractDetailsMapper.toDto(subscriptionContractDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionContractDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionContractDetails");
        return subscriptionContractDetailsRepository.findAll(pageable)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionContractDetailsDTO> findAll() {
        log.debug("Request to get all SubscriptionContractDetails");
        return subscriptionContractDetailsRepository.findAll().stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionContractDetailsDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionContractDetails : {}", id);
        return subscriptionContractDetailsRepository.findById(id)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionContractDetails : {}", id);
        subscriptionContractDetailsRepository.deleteById(id);
    }

    @Override
    public Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop) {
        return subscriptionContractDetailsRepository.findByShop(pageable, shop)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop,
                                                           ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                           ZonedDateTime fromUpdatedDate, ZonedDateTime toUpdatedDate,
                                                           ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                           String subscriptionContractId, String customerName,
                                                           String orderName, String status,
                                                           Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                           String planType, String recordType,
                                                           Long productId, Long variantId, Long sellingPlanId,
                                                           Double minOrderAmount, Double maxOrderAmount) {
        return subscriptionContractDetailsRepository.findByShop(pageable, shop,
                fromCreatedDate, toCreatedDate,
                fromUpdatedDate, toUpdatedDate,
                fromNextDate, toNextDate,
                subscriptionContractId, customerName,
                orderName, status,
                billingPolicyIntervalCount, billingPolicyInterval,
                planType, recordType,
                ShopifyGraphQLUtils.getGraphQLProductId(productId),
                ShopifyGraphQLUtils.getGraphQLVariantId(variantId),
                ShopifyGraphQLUtils.getGraphQLSellingPlanId(sellingPlanId), minOrderAmount, maxOrderAmount)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop,
                                                           ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                           ZonedDateTime fromUpdatedDate, ZonedDateTime toUpdatedDate,
                                                           ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                           String subscriptionContractId, String customerName,
                                                           String orderName, String status,
                                                           Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                           String planType, String recordType,
                                                           Long productId, Long variantId, List<String> sellingPlanIds,
                                                           Double minOrderAmount, Double maxOrderAmount) {
        return subscriptionContractDetailsRepository.findByShop(pageable, shop,
                fromCreatedDate, toCreatedDate,
                fromUpdatedDate, toUpdatedDate,
                fromNextDate, toNextDate,
                subscriptionContractId, customerName,
                orderName, status,
                billingPolicyIntervalCount, billingPolicyInterval,
                planType, recordType,
                ShopifyGraphQLUtils.getGraphQLProductId(productId),
                ShopifyGraphQLUtils.getGraphQLVariantId(variantId),
                sellingPlanIds, minOrderAmount, maxOrderAmount)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByShop(String shop) {
        return subscriptionContractDetailsRepository.findByShop(shop).stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Set<Long> findByShopGroupByCustomerId(String shop) {
        return subscriptionContractDetailsRepository.findByShopGroupByCustomerId(shop);
    }

    @Override
    public Page<CustomerInfo> findByShopGroupByCustomerIdPaginated(Pageable pageable, String shop, String name, String email, Boolean activeMoreThanOneSubscription) {
        if (BooleanUtils.isTrue(activeMoreThanOneSubscription)) {
            return subscriptionContractDetailsRepository.findByShopGroupByCustomerIdAndHavingMoreThanOneSubscriptionPaginated(pageable, shop, name, email);
        }
        return subscriptionContractDetailsRepository.findByShopGroupByCustomerIdPaginated(pageable, shop, name, email);
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByShopAndCustomerIds(String shop, Set<Long> customerIds) {
        if (customerIds.isEmpty()) {
            return Collections.emptyList();
        }
        return subscriptionContractDetailsRepository.findByShopAndCustomerIdIn(shop, customerIds).stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<SubscriptionContractDetailsDTO> findByContractId(Long contractId) {
        try {
            return subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId)
                .map(subscriptionContractDetailsMapper::toDto);
        } catch (Exception e) {
            // Remove duplicates
            List<SubscriptionContractDetails> list = subscriptionContractDetailsRepository.findAllBySubscriptionContractId(contractId);

            if (!CollectionUtils.isNullOrEmpty(list) && list.size() > 1) {

                log.info("Duplicate records found for contractId={}", contractId);

                SubscriptionContractDetails contractToKeep = list.stream()
                    .filter(contract -> StringUtils.isNotBlank(contract.getImportedId()))
                    .max(Comparator.comparing(SubscriptionContractDetails::getCreatedAt)).orElse(null);

                if (Objects.nonNull(contractToKeep)) {
                    list.remove(contractToKeep);
                } else {
                    list.sort(Comparator.comparing(SubscriptionContractDetails::getCreatedAt));
                    contractToKeep = list.remove(list.size() - 1);
                }
                subscriptionContractDetailsRepository.deleteAll(list);

                return Optional.ofNullable(contractToKeep).map(subscriptionContractDetailsMapper::toDto);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Long> findSubscriptionContractIdsByShop(String shop) {
        return subscriptionContractDetailsRepository.findSubscriptionContractIdsByShop(shop);
    }

    @Override
    public Page<Long> findSubscriptionContractIdsByShop(String shop, Pageable pageable) {
        return subscriptionContractDetailsRepository.findSubscriptionContractIdsByShop(pageable, shop);
    }

    @Override
    public List<Long> findSubscriptionContractIdsByShopAndIsImported(String shop) {
        return subscriptionContractDetailsRepository.findSubscriptionContractIdsByShopAndIsImported(shop);
    }

    @Override
    public List<String> findCustomerEmailByShop(String shop) {
        return subscriptionContractDetailsRepository.findCustomerEmailsByShop(shop);
    }

    @Override
    public List<Long> findCustomerIdsByShop(String shop) {
        return subscriptionContractDetailsRepository.findCustomerIdsByShop(shop);
    }

    @Autowired
    private ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    /*public void exportSubscriptionContractsV2(String shop, User user, String emailId, ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate, ZonedDateTime fromNextDate, ZonedDateTime toNextDate, String subscriptionContractId, String customerName, String orderName, String status, Integer billingPolicyIntervalCount, String billingPolicyInterval, String planType, String recordType) {
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = findByShop(shop, fromCreatedDate, toCreatedDate, fromNextDate, toNextDate, subscriptionContractId, customerName, orderName, status, billingPolicyIntervalCount, billingPolicyInterval, planType, recordType);

        List<List<SubscriptionContractDetailsDTO>> partition = Lists.partition(subscriptionContractDetailsDTOS, 5);

        AmazonS3 amazonS3 = amazonS3();
        String bucketName = "subscription-exports";
        String key = "abc";
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);

        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(initiateMultipartUploadRequest);

        String uploadId = initiateMultipartUploadResult.getUploadId();

        for (List<SubscriptionContractDetailsDTO> contractDetailsDTOS : partition) {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setPartNumber(1);

            String content = "fedsddsdsf";
            StringInputStream inputStream = new StringInputStream(content);
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(content.length());

            UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadPartRequest);
        }

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest();
        completeMultipartUploadRequest.setBucketName(bucketName);
        completeMultipartUploadRequest.setUploadId(uploadId);
        completeMultipartUploadRequest.setKey(key);

        List<PartETag> partETags = new ArrayList<>();
        partETags.add(new PartETag(1, uploadPartResult.getETag()));
        completeMultipartUploadRequest.setPartETags(partETags);
        amazonS3.completeMultipartUpload(completeMultipartUploadRequest);

    }*/

    @Override
    public int exportSubscriptionContracts(ExportInputRequest exportInputRequest) {
        File tempFile = null;
        int totalPages = 0;
        Pageable pageable = PageRequest.of(exportInputRequest.getPageNumber(), exportInputRequest.getPageSize());
        try {
            String[] headers = {
                "ID",
                "Status",
                "Customer ID",
                "Customer email",
                "Customer name",
                "Customer phone",
                "Delivery type",
                "Delivery first name",
                "Delivery last name",
                "Delivery address 1",
                "Delivery address 2",
                "Delivery province code",
                "Delivery city",
                "Delivery zip",
                "Delivery country code",
                "Delivery phone",
                "Delivery company",
                "Shipping Price",
                "Delivery price currency",
                "Created at",
                "Updated at",
                "Next order date",
                "Billing interval type",
                "Billing interval count",
                "Billing min cycles",
                "Billing max cycles",
                "Billing address",
                "Billing country",
                "Billing country code",
                "Billing city",
                "Billing province code",
                "Billing zip",
                "Delivery interval type",
                "Delivery interval count",
                "Payment ID",
                "Payment method",
                "Billing full name",
                "Payment method brand",
                "Payment method expiry year",
                "Payment method expiry month",
                "Payment method last digits",
                "Line title",
                "Line SKU",
                "Line variant quantity",
                "Line variant price",
                "Line price currency",
                "Line product ID",
                "Line variant ID",
                "Line selling plan ID",
                "Line selling plan name",
                "Line Attributes",
                "Subscription Attributes",
                "Order notes",
                "Cancellation date",
                "Cancellation reason",
                "Cancellation note",
                "Paused on date",
                "Total orders till date",
                "Past order names",
                "Total revenue generated",
                "First order name",
                "First order amount",
                "Last order name",
                "Last order date",
                "Last order amount",
                "Discount applied"
            };
            tempFile = File.createTempFile("Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                Page<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOPage = findByShop(pageable, exportInputRequest.getShop(),
                    exportInputRequest.getFromCreatedDate(), exportInputRequest.getToCreatedDate(),
                    null, null,
                    exportInputRequest.getFromNextDate(), exportInputRequest.getToNextDate(),
                    exportInputRequest.getSubscriptionContractId(), exportInputRequest.getCustomerName(),
                    exportInputRequest.getOrderName(), exportInputRequest.getStatus(),
                    exportInputRequest.getBillingPolicyIntervalCount(), exportInputRequest.getBillingPolicyInterval(),
                    exportInputRequest.getPlanType(), exportInputRequest.getRecordType(),
                    exportInputRequest.getProductId(), exportInputRequest.getVariantId(), exportInputRequest.getSellingPlanIds(), exportInputRequest.getMinOrderAmount(), exportInputRequest.getMaxOrderAmount());
                if (subscriptionContractDetailsDTOPage.hasContent()) {
                    totalPages = subscriptionContractDetailsDTOPage.getTotalPages();
                    List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsDTOPage.getContent();
                    ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(exportInputRequest.getShop());
                    log.info("subscriptionContractDetailsDTOS.size.=" + subscriptionContractDetailsDTOS.size());
                    for (int j = 0; j < subscriptionContractDetailsDTOS.size(); j++) {
                        try {
                            SubscriptionContractDetailsDTO subscription = subscriptionContractDetailsDTOS.get(j);
                            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractByContract = shopifyGraphqlSubscriptionContractService.getSubscriptionContractRaw(shopifyGraphqlClient, subscription.getSubscriptionContractId());
                            if (subscriptionContractByContract.isPresent()) {
                                SubscriptionContractQuery.SubscriptionContract sc = subscriptionContractByContract.get();

                                if (sc.getLines().getEdges().isEmpty()) {
                                    log.info("sc.getLines().getEdges(). j=" + j + " shop=" + exportInputRequest.getShop());
                                }

                                Optional<ShippingAddressModel> shippingAddressModel = commonUtils.getContractShippingAddress(sc);

                                String subscriptionAttributes = "";
                                if (!CollectionUtils.isNullOrEmpty(sc.getCustomAttributes())) {
                                    StringBuilder sb = new StringBuilder();
                                    sc.getCustomAttributes().forEach(customAttribute -> {
                                        sb.append("{\"").append(customAttribute.getKey())
                                            .append("\": \"").append(customAttribute.getValue().orElse(""))
                                            .append("\"}, ");
                                    });
                                    subscriptionAttributes = sb.substring(0, sb.length() - 2);
                                }

                                final String finalSubscriptionAttributes = subscriptionAttributes;


                                List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatus(subscription.getSubscriptionContractId(), BillingAttemptStatus.SUCCESS);
                                subscriptionBillingAttemptDTOList.sort(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));
                                int totalOrders = subscriptionBillingAttemptDTOList.size();
                                StringBuilder pastOrderNames = new StringBuilder();
                                Double totalRevenue = 0D;
                                String lastOrderName = "";
                                ZonedDateTime lastOrderDate = null;
                                Double lastOrderAmount = null;
                                if (Objects.nonNull(subscription.getOrderId())) {
                                    totalOrders++;
                                    pastOrderNames = new StringBuilder(subscription.getOrderName() + ", ");
                                    if(Objects.nonNull(subscription.getOrderAmountUSD())){
                                        totalRevenue += subscription.getOrderAmountUSD();
                                    }
                                }
                                if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                                    int lastIndex = subscriptionBillingAttemptDTOList.size() - 1;
                                    for(SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : subscriptionBillingAttemptDTOList){
                                        pastOrderNames.append(subscriptionBillingAttemptDTO.getOrderName()).append(", ");
                                        if(Objects.nonNull(subscriptionBillingAttemptDTO.getOrderAmountUSD())){
                                            totalRevenue += subscriptionBillingAttemptDTO.getOrderAmountUSD();
                                        }
                                    }
                                    lastOrderName = subscriptionBillingAttemptDTOList.get(lastIndex).getOrderName();
                                    lastOrderAmount = subscriptionBillingAttemptDTOList.get(lastIndex).getOrderAmount();
                                    lastOrderDate = subscriptionBillingAttemptDTOList.get(lastIndex).getAttemptTime();
                                }

                                final int totalOrdersTillDate = totalOrders;
                                final String finalLastOrderName = lastOrderName;
                                final String finalLastOrderDate = lastOrderDate != null ? lastOrderDate.toString() : "";
                                final String finalLastOrderAmount = lastOrderAmount != null ? lastOrderAmount.toString() : "";
                                final String finalPastOrderNames = StringUtils.isNotBlank(pastOrderNames.toString()) ? pastOrderNames.substring(0, pastOrderNames.length() - 2) : "";
                                final String finalTotalRevenue = totalRevenue.toString();

                                List<String> discountApplied = sc.getDiscounts().getEdges()
                                    .stream()
                                    .map(SubscriptionContractQuery.Edge2::getNode)
                                    .map(node2 -> node2.getTitle().orElse(""))
                                    .filter(StringUtils::isNotBlank)
                                    .collect(Collectors.toList());

                                final String finalDiscountApplied = StringUtils.join(discountApplied, ",");

                                int finalJ = j;
                                sc.getLines().getEdges().forEach(product -> {
                                    try {

                                        String lastFourDigits = "";
                                        Optional<SubscriptionContractQuery.BillingAddress> billingAddressOptional = null;
                                        Optional<SubscriptionContractQuery.BillingAddress1> paypalBillingAddressOptional = null;
                                        String billingFullName = "";
                                        String cardBrand = "";
                                        String billingAddress1 = null;
                                        String billingCity = null;
                                        String billingProvinceCode = null;
                                        String billingZip = null;
                                        String billingCountry = null;
                                        String billingCountryCode = null;
                                        Integer expirationMonth = null;
                                        Integer expirationYear = null;
                                        String paymentId = null;
                                        String lineAttributes = "";
                                        String paymentMethodName = "";

                                        if (sc.getCustomerPaymentMethod().isPresent()) {
                                            Optional<SubscriptionContractQuery.Instrument> instrument = sc.getCustomerPaymentMethod().map(d -> d.getInstrument().get());

                                            paymentId = sc.getCustomerPaymentMethod().get().getId();

                                            if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerPaypalBillingAgreement")) {
                                                lastFourDigits = "";

                                                paypalBillingAddressOptional = sc.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) cpi).getBillingAddress()));

                                                billingFullName = "";

                                                cardBrand = "";

                                                billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getAddress1).orElse(StringUtils.EMPTY)).get();

                                                billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCity).orElse(StringUtils.EMPTY)).get();

                                                billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getProvinceCode).orElse(StringUtils.EMPTY)).get();

                                                billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getZip).orElse(StringUtils.EMPTY)).get();

                                                billingCountry = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCountry).orElse(StringUtils.EMPTY)).get();

                                                billingCountryCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCountryCode).map(Object::toString).orElse(StringUtils.EMPTY)).get();

                                                paymentMethodName = "CustomerPaypalBillingAgreement";
                                            } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerCreditCard")) {

                                                lastFourDigits = sc.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getLastDigits())).get();

                                                billingAddressOptional = sc.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getBillingAddress()));

                                                billingFullName = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getName()).orElse(StringUtils.EMPTY);

                                                cardBrand = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBrand()).orElse(StringUtils.EMPTY);

                                                billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getAddress1).orElse(StringUtils.EMPTY)).get();

                                                billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCity).orElse(StringUtils.EMPTY)).get();

                                                billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getProvinceCode).orElse(StringUtils.EMPTY)).get();

                                                billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getZip).orElse(StringUtils.EMPTY)).get();

                                                billingCountry = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCountry).orElse(StringUtils.EMPTY)).get();

                                                billingCountryCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCountryCode).map(Object::toString).orElse(StringUtils.EMPTY)).get();

                                                expirationMonth = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getExpiryMonth()).get();

                                                expirationYear = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getExpiryYear()).get();

                                                paymentMethodName = "CustomerCreditCard";

                                            } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerShopPayAgreement")) {
                                                lastFourDigits = sc.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getLastDigits())).get();

                                                expirationMonth = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getExpiryMonth()).get();

                                                expirationYear = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getExpiryYear()).get();

                                                paymentMethodName = "CustomerShopPayAgreement";
                                            }
                                        }

                                        if (!CollectionUtils.isNullOrEmpty(product.getNode().getCustomAttributes())) {
                                            StringBuilder sb = new StringBuilder();
                                            product.getNode().getCustomAttributes().forEach(customAttribute -> {
                                                sb.append("{\"").append(customAttribute.getKey())
                                                    .append("\": \"").append(customAttribute.getValue().orElse(""))
                                                    .append("\"}, ");
                                            });
                                            lineAttributes = sb.substring(0, sb.length() - 2);
                                        }

                                        log.info("Came to Record. j=" + finalJ + " shop=" + exportInputRequest.getShop());

                                        csvPrinter.printRecord(
                                            subscription.getSubscriptionContractId(),
                                            subscription.getStatus(),
                                            subscription.getCustomerId(),
                                            subscription.getCustomerEmail(),
                                            subscription.getCustomerName(),
                                            subscription.getPhone(),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_type).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_first_name).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_last_name).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_address1).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_address2).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_province_code).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_city).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_zip).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_country_code).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_phone).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_company).orElse(""),
                                            sc.getDeliveryPrice().getAmount(),
                                            sc.getDeliveryPrice().getCurrencyCode(),
                                            subscription.getCreatedAt(),
                                            subscription.getUpdatedAt(),
                                            Optional.of(sc.getNextBillingDate().get()).orElse(""),
                                            sc.getBillingPolicy().getInterval(),
                                            sc.getBillingPolicy().getIntervalCount(),
                                            sc.getBillingPolicy().getMinCycles().map(Object::toString).orElse(""),
                                            sc.getBillingPolicy().getMaxCycles().map(Object::toString).orElse(""),
                                            billingAddress1,
                                            billingCountry,
                                            billingCountryCode,
                                            billingCity,
                                            billingProvinceCode,
                                            billingZip,
                                            sc.getDeliveryPolicy().getInterval(),
                                            sc.getDeliveryPolicy().getIntervalCount(),
                                            Optional.ofNullable(paymentId).orElse("").replace(ShopifyIdPrefix.CUSTOMER_PAYMENT_METHOD_ID_PREFIX, ""),
                                            paymentMethodName,
                                            billingFullName,
                                            cardBrand,
                                            Optional.ofNullable(expirationYear).map(Object::toString).orElse(""),
                                            Optional.ofNullable(expirationMonth).map(Object::toString).orElse(""),
                                            lastFourDigits,
                                            product.getNode().getVariantTitle().isPresent()
                                                && !product.getNode().getVariantTitle().get().trim().toLowerCase().equals("default")
                                                && !product.getNode().getVariantTitle().get().trim().equals("-") ? product.getNode().getTitle() + " - " + product.getNode().getVariantTitle().orElse("") : product.getNode().getTitle(),
                                            product.getNode().getSku().orElse(""),
                                            product.getNode().getQuantity(),
                                            product.getNode().getCurrentPrice().getAmount(),
                                            product.getNode().getCurrentPrice().getCurrencyCode(),
                                            product.getNode().getProductId().orElse("").replace(PRODUCT_ID_PREFIX, ""),
                                            product.getNode().getVariantId().orElse("").replace(PRODUCT_VARIANT_ID_PREFIX, ""),
                                            product.getNode().getSellingPlanId().orElse("").replace("gid://shopify/SellingPlan/", ""),
                                            product.getNode().getSellingPlanName().orElse(""),
                                            lineAttributes,
                                            finalSubscriptionAttributes,
                                            subscription.getOrderNote(),
                                            subscription.getCancelledOn(),
                                            subscription.getCancellationFeedback(),
                                            subscription.getCancellationNote(),
                                            subscription.getPausedOn(),
                                            totalOrdersTillDate,
                                            finalPastOrderNames,
                                            finalTotalRevenue,
                                            subscription.getOrderName(),
                                            subscription.getOrderAmount(),
                                            finalLastOrderName,
                                            finalLastOrderDate,
                                            finalLastOrderAmount,
                                            finalDiscountApplied
                                        );
                                    } catch (IOException e) {
                                        log.error("ex=" + ExceptionUtils.getStackTrace(e));
                                    }
                                });
                            } else {
                                log.info("Subscription Contract Details is not present. j=" + j + " shop=" + exportInputRequest.getShop());
                            }
                        } catch (Exception ex) {
                            log.error("ex=" + ExceptionUtils.getStackTrace(ex));
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Subscription Contract Exported (" + (pageable.getPageNumber() + 1) + " of " + totalPages + ")", "Check attached csv file for your all subscription list details.", "subscription-support@appstle.com", exportInputRequest.getShop(), exportInputRequest.getEmailId());
        return totalPages;
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByShop(String shop,
                                                           ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                           ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                           String subscriptionContractId, String customerName,
                                                           String orderName, String status,
                                                           Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                           String planType, String recordType,
                                                           Long productId,
                                                           Long variantId,
                                                           Long sellingPlanId) {
        return subscriptionContractDetailsRepository.findByShop(shop, fromCreatedDate, toCreatedDate,
                fromNextDate, toNextDate,
                subscriptionContractId, customerName,
                orderName, status,
                billingPolicyIntervalCount, billingPolicyInterval,
                planType, recordType,
                ShopifyGraphQLUtils.getGraphQLProductId(productId),
                ShopifyGraphQLUtils.getGraphQLVariantId(variantId),
                ShopifyGraphQLUtils.getGraphQLSellingPlanId(sellingPlanId)).stream()
            .map(subscriptionContractDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByShopAndCustomerId(String shop, long customerId) {
        return subscriptionContractDetailsRepository.findByShopAndCustomerId(shop, customerId).stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<SubscriptionContractDetailsDTO> findByShopAndImportedId(String shop, String importedId) {
        return subscriptionContractDetailsRepository.findByShopAndImportedId(shop, importedId)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public Optional<SubscriptionContractDetailsDTO> getSubscriptionByContractId(Long contractId) {
        return subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public void deleteByContractId(Long contractId) {
        subscriptionContractDetailsRepository.deleteBySubscriptionContractId(contractId);
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByContractIdIn(Set<Long> subscriptionContractIds) {
        if (subscriptionContractIds.isEmpty()) {
            return new ArrayList<>();
        }
        return subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(subscriptionContractIds)
            .stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findNextOrderDateDiscrepancy() {
        return subscriptionContractDetailsRepository.findNextOrderDateDiscrepancy()
            .stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteBySubscriptionContractIdIn(Set<Long> contractIds) {
        subscriptionContractDetailsRepository.deleteBySubscriptionContractIdIn(contractIds);
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findSubscriptionWithNullOrderAmount() {
        return subscriptionContractDetailsRepository.findSubscriptionWithNullOrderAmount().stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionContractDetailsDTO> findByProductOrVariantId(String shop, String gqlProductOrVariantId) {
        return subscriptionContractDetailsRepository.findByProductOrVariantId(shop, gqlProductOrVariantId).stream()
            .map(subscriptionContractDetailsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Page<SubscriptionContractDetailsDTO> findByProductOrVariantId(String shop, String gqlProductOrVariantId, Pageable pageable) {
        return subscriptionContractDetailsRepository.findByProductOrVariantId(shop, gqlProductOrVariantId, pageable)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    @Override
    public List<ProductDeliveryAnalytics> getProductDeliveryAnalytics(String shop) {

        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime dateAfter7Day = currentDate.plusWeeks(1);
        ZonedDateTime dateAfter30Day = currentDate.plusMonths(1);
        ZonedDateTime dateAfter90Day = currentDate.plusMonths(3);
        ZonedDateTime dateAfter365Day = currentDate.plusMonths(12);

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndStatus(shop, "active");

        Map<String, ProductDeliveryAnalytics> productDeliveryAnalyticsMap = new HashMap<>();

        for (SubscriptionContractDetails scd : subscriptionContractDetailsList) {
            long totalCycles7Days = 0;
            long totalCycles30days = 0;
            long totalCycles90Days = 0;
            long totalCycles365Days = 0;

            ZonedDateTime maxDate = dateAfter365Day;
            if (scd.getMaxCycles() != null) {
                maxDate = SubscriptionUtils.getNextBillingDate(
                    scd.getBillingPolicyIntervalCount() * (scd.getMaxCycles() - 1),
                    scd.getBillingPolicyInterval(),
                    scd.getCreatedAt());

                maxDate = CommonUtils.getMinDate(maxDate, dateAfter365Day);
            }

            ChronoUnit chronoUnit = null;
            if ("day".equalsIgnoreCase(scd.getDeliveryPolicyInterval())) {
                chronoUnit = ChronoUnit.DAYS;
            } else if ("week".equalsIgnoreCase(scd.getDeliveryPolicyInterval())) {
                chronoUnit = ChronoUnit.WEEKS;
            } else if ("month".equalsIgnoreCase(scd.getDeliveryPolicyInterval())) {
                chronoUnit = ChronoUnit.MONTHS;
            } else if ("year".equalsIgnoreCase(scd.getDeliveryPolicyInterval())) {
                chronoUnit = ChronoUnit.YEARS;
            }

            if (chronoUnit != null && currentDate.isBefore(maxDate)) {
                totalCycles7Days = Math.max(chronoUnit.between(currentDate, CommonUtils.getMinDate(dateAfter7Day, maxDate)) / scd.getDeliveryPolicyIntervalCount(), 0);
                totalCycles7Days += (totalCycles7Days == 0 && scd.getNextBillingDate().isBefore(dateAfter7Day)) ? 1 : 0;

                totalCycles30days = Math.max(chronoUnit.between(currentDate, CommonUtils.getMinDate(dateAfter30Day, maxDate)) / scd.getDeliveryPolicyIntervalCount(), 0);
                totalCycles30days += (totalCycles30days == 0 && scd.getNextBillingDate().isBefore(dateAfter30Day)) ? 1 : 0;

                totalCycles90Days = Math.max(chronoUnit.between(currentDate, CommonUtils.getMinDate(dateAfter90Day, maxDate)) / scd.getDeliveryPolicyIntervalCount(), 0);
                totalCycles90Days += (totalCycles90Days == 0 && scd.getNextBillingDate().isBefore(dateAfter90Day)) ? 1 : 0;

                totalCycles365Days = Math.max(chronoUnit.between(currentDate, CommonUtils.getMinDate(dateAfter365Day, maxDate)) / scd.getDeliveryPolicyIntervalCount(), 0);
                totalCycles365Days += (totalCycles365Days == 0 && scd.getNextBillingDate().isBefore(dateAfter365Day)) ? 1 : 0;
            }

            List<ProductDeliveryAnalytics> productDeliveryAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductDeliveryAnalytics>>() {
                },
                scd.getContractDetailsJSON()
            );

            for (ProductDeliveryAnalytics productDeliveryAnalytics : productDeliveryAnalyticsList) {
                ProductDeliveryAnalytics existingProductDeliveryAnalytics = productDeliveryAnalyticsMap.get(productDeliveryAnalytics.getVariantId());

                if (existingProductDeliveryAnalytics == null) {
                    existingProductDeliveryAnalytics = productDeliveryAnalytics;
                    productDeliveryAnalyticsMap.put(productDeliveryAnalytics.getVariantId(), existingProductDeliveryAnalytics);
                }

                Long quantity = Optional.ofNullable(productDeliveryAnalytics.getQuantity()).orElse(0L);
                sumUpDeliveryQuantity(existingProductDeliveryAnalytics, totalCycles7Days, totalCycles30days, totalCycles90Days, totalCycles365Days, quantity);
            }

        }


        List<ProductDeliveryAnalytics> productAnalyticsList = new ArrayList(productDeliveryAnalyticsMap.values());
        return productAnalyticsList.stream()
            .filter(
                x ->
                    x.getDeliveryInNext7Days().longValue() > 0
                        || x.getDeliveryInNext30Days().longValue() > 0
                        || x.getDeliveryInNext90Days().longValue() > 0
                        || x.getDeliveryInNext365Days().longValue() > 0
            )
            .collect(Collectors.toList());
    }

    @Override
    public void exportProductDeliveryAnalytics(String email, String shop) {

        List<ProductDeliveryAnalytics> productDeliveryAnalyticsList = getProductDeliveryAnalytics(shop);

        File tempFile = null;

        try {
            String[] headers = {
                "Product",
                "Variant",
                "Next 7 days",
                "Next 30 days",
                "Next 90 days",
                "Next 365 days"
            };
            tempFile = File.createTempFile("Subscriptions-Discount-Code-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                for(ProductDeliveryAnalytics productDeliveryAnalytics : productDeliveryAnalyticsList) {
                    try {
                        csvPrinter.printRecord(
                            productDeliveryAnalytics.getTitle(),
                            productDeliveryAnalytics.getVariantTitle(),
                            productDeliveryAnalytics.getDeliveryInNext7Days(),
                            productDeliveryAnalytics.getDeliveryInNext30Days(),
                            productDeliveryAnalytics.getDeliveryInNext90Days(),
                            productDeliveryAnalytics.getDeliveryInNext365Days()
                        );
                    } catch (Exception ex) {
                        log.error("An error occurred while printing csv record for shop = {}. Ex=" + ExceptionUtils.getStackTrace(ex), shop);
                    }
                }
            }
        } catch (Exception ex){
            log.error("An error occurred while exporting product delivery forecast for shop = {}. Ex=" + ExceptionUtils.getStackTrace(ex), shop);
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Delivery Forecast Export", "Check attached csv file.", "subscription-support@appstle.com", shop, email);

    }

    private void sumUpDeliveryQuantity(ProductDeliveryAnalytics productDeliveryAnalytics, long totalCycles7Days, long totalCycles30Days, long totalCycles90Days, long totalCycles365Days, long quantity) {

        long deliveryIn7Days = Optional.ofNullable(productDeliveryAnalytics.getDeliveryInNext7Days()).orElse(0L) + (quantity * totalCycles7Days);
        long deliveryIn30Days = Optional.ofNullable(productDeliveryAnalytics.getDeliveryInNext30Days()).orElse(0L) + (quantity * totalCycles30Days);
        long deliveryIn90Days = Optional.ofNullable(productDeliveryAnalytics.getDeliveryInNext90Days()).orElse(0L) + (quantity * totalCycles90Days);
        long deliveryIn365Days = Optional.ofNullable(productDeliveryAnalytics.getDeliveryInNext365Days()).orElse(0L) + (quantity * totalCycles365Days);

        productDeliveryAnalytics.setDeliveryInNext7Days(deliveryIn7Days);
        productDeliveryAnalytics.setDeliveryInNext30Days(deliveryIn30Days);
        productDeliveryAnalytics.setDeliveryInNext90Days(deliveryIn90Days);
        productDeliveryAnalytics.setDeliveryInNext365Days(deliveryIn365Days);
    }

    @Override
    public List<ProductRevenueAnalytics> getProductRevenueAnalytics(String shop, String filterBy, Long days, ZonedDateTime fromDate, ZonedDateTime toDate) {
        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        String[] shopTimeZone = shopInfo.getShopTimeZone().split(" ");

        ZoneId zoneId = null;

        try {
            zoneId = ZoneId.of(shopTimeZone[1]);
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDate, toDate, zoneId);

        List<ProductRevenue> activeSuccessAttempts = subscriptionContractDetailsRepository.findSuccessContractAttempts(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        List<ProductRevenue> activeFailedAttempts = subscriptionContractDetailsRepository.findFailedContractAttempts(shop, filterProperties.getFromDate(), filterProperties.getToDate());

        Map<String, Integer> successAttemptsMap = new HashMap<>();
        Map<String, Integer> failedAttemptsMap = new HashMap<>();

        Map<String, ProductRevenueAnalytics> productRevenueAnalyticsMap = new HashMap<>();

        for (ProductRevenue billingAttemptWithContractWrapper : activeSuccessAttempts) {
            String contractDetailsJSON = billingAttemptWithContractWrapper.getContractDetailsJson();
            String contractBillingAttempt = billingAttemptWithContractWrapper.getContractBillingAttempt();
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                contractDetailsJSON
            );

            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    successAttemptsMap.put(productRevenueAnalytics.getVariantId(), successAttemptsMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + Integer.parseInt(contractBillingAttempt));

                    sumUpProductVariantPriceWithQuantity(productRevenueAnalytics, productRevenueAnalyticsFromMap, contractBillingAttempt);
                }
            }
        }

        for (ProductRevenue billingAttemptWithContractWrapper : activeFailedAttempts) {
            String contractDetailsJSON = billingAttemptWithContractWrapper.getContractDetailsJson();
            String contractBillingAttempt = billingAttemptWithContractWrapper.getContractBillingAttempt();
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                contractDetailsJSON
            );

            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    failedAttemptsMap.put(productRevenueAnalytics.getVariantId(), failedAttemptsMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + Integer.parseInt(contractBillingAttempt));
                }
            }
        }

        getProductWiseCancellationRate(shop, filterProperties, productRevenueAnalyticsMap);

        getProductWiseChurnRate(shop, filterProperties, productRevenueAnalyticsMap);

        List<ProductRevenueAnalytics> productAnalyticsList = new ArrayList(productRevenueAnalyticsMap.values());

        for(ProductRevenueAnalytics productRevenueAnalytics : productAnalyticsList) {
            String variantId = productRevenueAnalytics.getVariantId();
            Integer successAttemptCount = successAttemptsMap.getOrDefault(variantId, 0);
            Integer failedAttemptCount = failedAttemptsMap.getOrDefault(variantId, 0);
            if (successAttemptCount > 0) {
                Integer totalAttemptCount = successAttemptCount + failedAttemptCount;
                Double approvalRate = round(((double) successAttemptCount / (double) totalAttemptCount) * 100);
                productRevenueAnalytics.setApprovalRate(approvalRate);
            }
        }
        return productAnalyticsList.stream()
            .filter(
                x ->
                    x.getTotalProductQuantity() > 0
                        || x.getTotalProductPrice() > 0 || x.getCancellationRate() > 0 || x.getApprovalRate() > 0 || x.getChurnRate() > 0
            )
            .collect(Collectors.toList());
    }

    private void sumUpProductVariantPriceWithQuantity(ProductRevenueAnalytics productRevenueAnalytics, ProductRevenueAnalytics productRevenueAnalyticsFromMap, String contractBillingAttempt) {
        Long currentVariantQuantity = productRevenueAnalytics.getQuantity();
        Double currentVariantPrice = productRevenueAnalytics.getCurrentPrice();
        Double calculateCurrentTotalPrice = Double.parseDouble(String.valueOf(currentVariantPrice * currentVariantQuantity * Long.parseLong(contractBillingAttempt)));

        // Calculate total product price * quantity
        productRevenueAnalyticsFromMap.setTotalProductPrice((long) (productRevenueAnalyticsFromMap.getTotalProductPrice() + calculateCurrentTotalPrice));
        // Calculate total product quantity
        productRevenueAnalyticsFromMap.setTotalProductQuantity(productRevenueAnalyticsFromMap.getTotalProductQuantity() + (currentVariantQuantity * Long.parseLong(contractBillingAttempt)));
    }

    private void getProductWiseCancellationRate(String shop, FilterProperties filterProperties, Map<String, ProductRevenueAnalytics> productRevenueAnalyticsMap) {
        List<SubscriptionContractDetailsDTO> newSubscriptions = getNewSubscriptions(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        List<SubscriptionContractDetailsDTO> totalSubscriptions = getTotalSubscriptionsDataCreatedInDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());

        if (totalSubscriptions.size() == 0) {
            return;
        }

        Map<String, Integer> activeProductMap = new HashMap<>();
        Map<String, Integer> totalProductMap = new HashMap<>();

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : totalSubscriptions) {
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                subscriptionContractDetailsDTO.getContractDetailsJSON()
            );
            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    totalProductMap.put(productRevenueAnalytics.getVariantId(), totalProductMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + 1);
                }
            }
        }

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : newSubscriptions) {
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                subscriptionContractDetailsDTO.getContractDetailsJSON()
            );

            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    activeProductMap.put(productRevenueAnalytics.getVariantId(), activeProductMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + 1);
                }
            }
        }

        for (Map.Entry<String, ProductRevenueAnalytics> entry : productRevenueAnalyticsMap.entrySet()) {

            String variantKey = entry.getKey();
            ProductRevenueAnalytics productRevenueAnalytics = entry.getValue();

            Integer totalSubscriptionCount = totalProductMap.getOrDefault(variantKey, 0);
            Integer newSubscription = activeProductMap.getOrDefault(variantKey, 0);

            if (totalSubscriptionCount > 0) {
                Double cancellationRate = round((((double) totalSubscriptionCount - (double) newSubscription) / (double) totalSubscriptionCount) * 100);
                productRevenueAnalytics.setCancellationRate(cancellationRate);
            }
        }
    }

    private void getProductWiseChurnRate(String shop, FilterProperties filterProperties, Map<String, ProductRevenueAnalytics> productRevenueAnalyticsMap) {
        List<SubscriptionContractDetailsDTO> cancelledSubscriptionsBetween = getTotalCancelledSubscriptionsButCreatedBeforeDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        List<SubscriptionContractDetailsDTO> totalActiveSubscriptionsCreatedBefore = getTotalSubscriptionsDataCreatedBefore(shop, filterProperties.getFromDate());

        if (totalActiveSubscriptionsCreatedBefore.size() == 0) {
            return;
        }

        Map<String, Integer> cancelledProductMap = new HashMap<>();
        Map<String, Integer> totalProductMap = new HashMap<>();

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : cancelledSubscriptionsBetween) {
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                subscriptionContractDetailsDTO.getContractDetailsJSON()
            );

            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    cancelledProductMap.put(productRevenueAnalytics.getVariantId(), cancelledProductMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + 1);
                }
            }
        }

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : totalActiveSubscriptionsCreatedBefore) {
            List<ProductRevenueAnalytics> productRevenueAnalyticsList = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<List<ProductRevenueAnalytics>>() {
                },
                subscriptionContractDetailsDTO.getContractDetailsJSON()
            );
            for (ProductRevenueAnalytics productRevenueAnalytics : productRevenueAnalyticsList) {
                if(StringUtils.isNotBlank(productRevenueAnalytics.getVariantId())) {
                    ProductRevenueAnalytics productRevenueAnalyticsFromMap = productRevenueAnalyticsMap.get(productRevenueAnalytics.getVariantId());
                    if (productRevenueAnalyticsFromMap == null) {
                        productRevenueAnalyticsFromMap = productRevenueAnalytics;
                        productRevenueAnalyticsMap.put(productRevenueAnalytics.getVariantId(), productRevenueAnalyticsFromMap);
                    }
                    totalProductMap.put(productRevenueAnalytics.getVariantId(), totalProductMap.getOrDefault(productRevenueAnalytics.getVariantId(), 0) + 1);
                }
            }
        }

        for (Map.Entry<String, ProductRevenueAnalytics> entry : productRevenueAnalyticsMap.entrySet()) {
            String variantKey = entry.getKey();
            ProductRevenueAnalytics productRevenueAnalytics = entry.getValue();

            Integer totalSubscriptionCount = totalProductMap.getOrDefault(variantKey, 0);
            Integer cancelledSubscriptionCount = cancelledProductMap.getOrDefault(variantKey, 0);

            if (totalSubscriptionCount > 0) {
                Double churnRate = round(((double) cancelledSubscriptionCount / (double) totalSubscriptionCount) * 100);
                productRevenueAnalytics.setChurnRate(churnRate);
            }
        }
    }

    private double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }

    @NotNull
    private ZonedDateTime getNextDeliveryDate(SubscriptionContractDetails subscriptionContractDetails, ZonedDateTime nextBillingDate) {
        nextBillingDate = commonUtils.nextIntervalDate(nextBillingDate.toLocalDateTime(), subscriptionContractDetails.getDeliveryPolicyInterval(), subscriptionContractDetails.getDeliveryPolicyIntervalCount()).atZone(ZoneId.of("UTC"));
        return nextBillingDate;
    }

    @NotNull
    private ZonedDateTime getPreviousDeliveryDate(SubscriptionContractDetails subscriptionContractDetails, ZonedDateTime nextBillingDate) {
        nextBillingDate = commonUtils.previousIntervalDate(nextBillingDate.toLocalDateTime(), subscriptionContractDetails.getDeliveryPolicyInterval(), subscriptionContractDetails.getDeliveryPolicyIntervalCount()).atZone(ZoneId.of("UTC"));
        return nextBillingDate;
    }

    @Override
    public void updateLinePrice(String shop, Long contractId, Long variantId, Double price, ActivityLogEventSource activityLogEventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);


        while (!org.springframework.util.CollectionUtils.isEmpty(optionalQueryResponse.getErrors()) && optionalQueryResponse.getErrors().get(0).getMessage().equals("Throttled")) {
            Thread.sleep(1000l);
            optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
        }

        String graphqlVariantId = PRODUCT_VARIANT_ID_PREFIX + variantId;
        List<SubscriptionContractQuery.Node> nodeToBeUpdatedList = requireNonNull(optionalQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getVariantId().isPresent() && j.getVariantId().get().equalsIgnoreCase(graphqlVariantId)).collect(Collectors.toList());

        if (!CollectionUtils.isNullOrEmpty(nodeToBeUpdatedList)) {
            for (SubscriptionContractQuery.Node nodeToBeUpdated : nodeToBeUpdatedList) {
                String lineId = nodeToBeUpdated.getId();
                subscriptionContractUpdateLineItemPrice(contractId, shop, price, lineId, activityLogEventSource);
            }
        }
    }

    @Override
    public void subscriptionContractUpdateLineItem(Long contractId, String shop, Integer quantity, String productVariantId, String lineId, String sellingPlanName, Double price, boolean isPricePerUnit, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).get().getSubscriptionContract().get();

        Optional<SubscriptionContractQuery.Node> optionalNodeWithSellingPlan = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getSellingPlanId().isPresent()).findFirst();

        SubscriptionContractQuery.Node nodeToBeUpdated = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getId().equals(lineId)).findFirst().get();

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                String draftId = optionalDraftId.get();
                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                if (optionalNodeWithSellingPlan.isPresent() && nodeToBeUpdated.getSellingPlanId().isEmpty()) {
                    SubscriptionContractQuery.Node node = optionalNodeWithSellingPlan.get();
                    subscriptionLineUpdateInputBuilder.sellingPlanId(node.getSellingPlanId().get());

                    if (node.getSellingPlanName().isPresent()) {
                        subscriptionLineUpdateInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                    }
                }

                Double existingPrice = Double.parseDouble(nodeToBeUpdated.getCurrentPrice().getAmount().toString());

                Double linePrice = price;
                if (isPricePerUnit &&
                    subscriptionContract.getDeliveryPolicy().getIntervalCount() > 0 &&
                    subscriptionContract.getBillingPolicy().getIntervalCount() != subscriptionContract.getDeliveryPolicy().getIntervalCount()) {
                    // Apply interval multiplier for pre-paid subscriptions
                    int intervalMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();
                    linePrice = price * intervalMultiplier;
                }

                // Check Min-Max quantity based on attributes
                validateMinMaxQuantityBasedOnAttributes(nodeToBeUpdated, quantity);

                Integer existingQuantity = nodeToBeUpdated.getQuantity();

                String existingSellingPlanName = nodeToBeUpdated.getSellingPlanName().orElse(null);

                List<SubscriptionContractQuery.CycleDiscount> existingCycleDiscounts = nodeToBeUpdated.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>());

                if (linePrice != null &&
                    !existingPrice.equals(linePrice) &&
                    !existingCycleDiscounts.isEmpty() &&
                    existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE) &&
                    existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue)) {

                    ArrayList<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                    DecimalFormat df = new DecimalFormat("#.##");

                    /*for (SubscriptionContractQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {
                        SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();
                        SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput =
                            SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                .afterCycle(existingCycleDiscount.getAfterCycle())
                                .adjustmentType(existingCycleDiscount.getAdjustmentType())
                                .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build())
                                //.computedPrice(df.format(linePrice * ((100 - adjustmentValue.getPercentage()) / 100)))
                                .build();
                        cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);
                    }*/

                    //subscriptionLineUpdateInputBuilder.pricingPolicy(null);
                } else if (linePrice != null &&
                    !existingPrice.equals(linePrice) &&
                    !existingCycleDiscounts.isEmpty() &&
                    existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT)) {
                    //subscriptionLineUpdateInputBuilder.pricingPolicy(null);
                } else if (linePrice != null &&
                    !existingPrice.equals(linePrice) &&
                    !existingCycleDiscounts.isEmpty() &&
                    existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE)) {
                    //subscriptionLineUpdateInputBuilder.pricingPolicy(null);
                }

                SubscriptionLineUpdateInput subscriptionLineUpdateInput = null;
                if (!existingPrice.equals(linePrice)) {
                    subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                        .quantity(quantity)
                        .productVariantId(productVariantId)
                        .currentPrice(linePrice)
                        .build();
                } else {
                    subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                        .quantity(quantity)
                        .build();
                }

                if (sellingPlanName != null && !sellingPlanName.equals(existingSellingPlanName)) {
                    subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                        .sellingPlanName(sellingPlanName)
                        .build();
                }

                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                if (optionalMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                }

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                }

                if (existingPrice == null || !existingPrice.equals(linePrice)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("oldPrice", Optional.ofNullable(existingPrice).orElse(0D));
                    map.put("newPrice", linePrice);
                    map.put("variantId", productVariantId);
                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_PRICE_CHANGE, ActivityLogStatus.SUCCESS, map);
                }

                if (existingQuantity == null || !existingQuantity.equals(quantity)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("oldQuantity", Optional.ofNullable(existingQuantity).orElse(0));
                    map.put("newQuantity", quantity);
                    map.put("variantId", productVariantId);
                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_QUANTITY_CHANGE, ActivityLogStatus.SUCCESS, map);
                }
            }

        }
    }

    @Override
    public void subscriptionContractUpdateLineItemQuantity(Long contractId, String shop, Integer quantity, String lineId, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("REST request for {} Response received from graphql update subscription contract {} ", shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                String draftId = optionalDraftId.get();
                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                Optional<SubscriptionContractQuery.Node> existingLineItem = optionalSubscriptionContractQueryResponse
                    .getData()
                    .map(d ->
                        d.getSubscriptionContract()
                            .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                            .map(SubscriptionContractQuery.Lines::getEdges)
                            .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(n -> n.getId().equals(lineId))
                    .findFirst();

                // Check Min-Max quantity based on attributes
                validateMinMaxQuantityBasedOnAttributes(existingLineItem.get(), quantity);

                Integer existingQuantity = existingLineItem.map(SubscriptionContractQuery.Node::getQuantity).orElse(null);

                SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                    .quantity(quantity)
                    .build();

                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                if (optionalMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                }

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                }

                if (existingLineItem.isPresent() && (existingQuantity == null || !existingQuantity.equals(quantity))) {

                    String variantId = existingLineItem.get().getVariantId().isPresent() ? ShopifyGraphQLUtils.getVariantId(existingLineItem.get().getVariantId().get()) + "" : "";
                    Map<String, Object> map = new HashMap<>();
                    map.put("oldQuantity", Optional.ofNullable(existingQuantity).orElse(0));
                    map.put("newQuantity", quantity);
                    map.put("variantId", variantId);
                    map.put("title", Optional.ofNullable(existingLineItem.get().getTitle()).orElse(""));
                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_QUANTITY_CHANGE, ActivityLogStatus.SUCCESS, map);
                }

                commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);
                resyncBuildABoxDiscount(shop, contractId);
            }
        }
    }

    private void validateMinMaxQuantityBasedOnAttributes(SubscriptionContractQuery.Node line, int newQuantity) {
        String minQuantityValue = commonUtils.getAttributeValue(line.getCustomAttributes(), AppstleAttribute.MIN_QUANTITY.getKey());
        String maxQuantityValue = commonUtils.getAttributeValue(line.getCustomAttributes(), AppstleAttribute.MAX_QUANTITY.getKey());

        if (StringUtils.isNotBlank(minQuantityValue)) {
            Integer minQuantity = null;
            try {
                minQuantity = Integer.parseInt(minQuantityValue.trim());
            } catch (NumberFormatException nfe) {
                log.info("Error in parsing line min quantity attribute value. variantId={}, minQuantityValue={}, Exception={}", line.getVariantId().orElse(""), minQuantityValue, ExceptionUtils.getStackTrace(nfe));
            }

            if (minQuantity != null) {
                if (newQuantity < minQuantity) {
                    throw new BadRequestAlertException("Minimum " + minQuantity + " quantity is required", "", "minQuantityNotReached");
                }
            }
        }

        if (StringUtils.isNotBlank(maxQuantityValue)) {
            Integer maxQuantity = null;
            try {
                maxQuantity = Integer.parseInt(maxQuantityValue.trim());
            } catch (NumberFormatException nfe) {
                log.info("Error in parsing line max quantity attribute value. variantId={}, maxQuantityValue={}, Exception={}", line.getVariantId().orElse(""), maxQuantityValue, ExceptionUtils.getStackTrace(nfe));
            }

            if (maxQuantity != null) {
                if (newQuantity > maxQuantity) {
                    throw new BadRequestAlertException("Maximum " + maxQuantity + " quantity is allowed", "", "maxQuantityReached");
                }
            }
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateBillingInterval(Long contractId, String shop, int billingIntervalCount, SellingPlanInterval billingInterval, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        SellingPlanInterval oldBillingInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getBillingPolicyInterval().toUpperCase());
        Integer oldBillingIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        Integer oldDeliveryIntervalCount = subscriptionContractDetailsDTO.getDeliveryPolicyIntervalCount();

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(ObjectUtils.isNotEmpty(shopInfoDTO.getIanaTimeZone()) ? shopInfoDTO.getIanaTimeZone() : "UTC"));

        if (ObjectUtils.allNotNull(shopInfoDTO.getZoneOffsetHours(), shopInfoDTO.getZoneOffsetMinutes())) {
            ZoneId zoneId = CommonUtils.getZoneIdFromOffset(shopInfoDTO.getZoneOffsetHours().intValue(), shopInfoDTO.getZoneOffsetMinutes().intValue());
            nowUtc = nowUtc.withZoneSameInstant(zoneId);
        }

        if (ObjectUtils.allNotNull(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute())) {
            nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute()));
        } else if (shopInfoDTO.getRecurringOrderHour() != null && shopInfoDTO.getRecurringOrderMinute() != null) {
            nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getRecurringOrderHour(), shopInfoDTO.getRecurringOrderMinute()));
        }

        ZonedDateTime nextBillingDate = SubscriptionUtils.getNextBillingDate(billingIntervalCount, billingInterval.rawValue().toLowerCase(), nowUtc);
        if (!Objects.isNull(shopInfoDTO.getLocalOrderDayOfWeek())) {
            nextBillingDate = setNextDayOfWeek(nextBillingDate, shopInfoDTO.getLocalOrderDayOfWeek());
        }

        nextBillingDate = nextBillingDate.withZoneSameInstant(ZoneId.of("UTC"));

        if (BooleanUtils.isFalse(shopInfoDTO.isEnableChangeFromNextBillingDate())) {
            nextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate().withZoneSameInstant(ZoneId.of("UTC"));
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Integer maxCycles = subscriptionContractDetailsDTO.getMaxCycles();
        Integer minCycles = subscriptionContractDetailsDTO.getMinCycles();

        Integer deliveryIntervalCount = subscriptionContractDetailsDTO.getDeliveryPolicyIntervalCount();

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        List<SubscriptionContractQuery.Node> subscriptionLineItems = requireNonNull(subscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .collect(Collectors.toList());

        Map<String, Double> subscriptionLineItemPriceByLineId = new HashMap<>();
        for (SubscriptionContractQuery.Node subscriptionLineItem : subscriptionLineItems) {
            double subscriptionItemUnitPrice = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString()) / (oldBillingIntervalCount / oldDeliveryIntervalCount);
            subscriptionLineItemPriceByLineId.put(subscriptionLineItem.getId(), subscriptionItemUnitPrice);
        }

        SellingPlanInterval deliveryInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getDeliveryPolicyInterval().toUpperCase());

        if (deliveryIntervalCount.equals(oldBillingIntervalCount)) {
            deliveryIntervalCount = billingIntervalCount;
            deliveryInterval = billingInterval;
        }

        boolean isIntervalChanged = false;
        if (!oldBillingInterval.equals(billingInterval)) {
            deliveryInterval = billingInterval;
            isIntervalChanged = true;
        }

        Boolean overwriteAnchorDay = false;

        if (BooleanUtils.isTrue(shopInfoDTO.isOverwriteAnchorDay()) && !subscriptionContract.getBillingPolicy().getAnchors().isEmpty()) {
            if (BooleanUtils.isFalse(isIntervalChanged)) {
                int oldAnchorDay = subscriptionContract.getBillingPolicy().getAnchors().get(0).getDay();
                int newAnchorDay = 0;
                if (billingInterval.equals(SellingPlanInterval.WEEK)) {
                    newAnchorDay = nextBillingDate.getDayOfWeek().getValue();
                } else if (billingInterval.equals(SellingPlanInterval.MONTH)) {
                    newAnchorDay = nextBillingDate.getDayOfMonth();
                } else if (billingInterval.equals(SellingPlanInterval.YEAR)) {
                    newAnchorDay = nextBillingDate.getDayOfMonth();
                    Optional<Integer> oldAnchorMonth = subscriptionContract.getBillingPolicy().getAnchors().get(0).getMonth();
                    if (oldAnchorMonth.isPresent() && nextBillingDate.getMonth().getValue() != oldAnchorMonth.get()) {
                        overwriteAnchorDay = true;
                    }
                }
                if (newAnchorDay != oldAnchorDay) {
                    overwriteAnchorDay = true;
                }
            } else {
                overwriteAnchorDay = true;
            }
        }

        List<SellingPlanAnchorInput> oldAnchors = getOldAnchors(subscriptionContract.getBillingPolicy().getAnchors());

        SubscriptionContractQuery.SubscriptionContract subscriptionContractResponseEntity = updateContractWith(
            contractId,
            shop,
            billingIntervalCount,
            billingInterval,
            nextBillingDate,
            shopifyGraphqlClient,
            subscriptionContractDetailsDTO,
            maxCycles,
            minCycles,
            deliveryIntervalCount,
            deliveryInterval,
            overwriteAnchorDay,
            oldAnchors);

        Map<String, Object> map = new HashMap<>();
        map.put("billingInterval", billingInterval);
        map.put("billingIntervalCount", billingIntervalCount);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.BILLING_INTERVAL_CHANGE, ActivityLogStatus.SUCCESS, map);
        if (isIntervalChanged) {
            map = new HashMap<>();
            map.put("deliveryInterval", deliveryInterval);
            map.put("deliveryIntervalCount", deliveryIntervalCount);
            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DELIVERY_INTERVAL_CHANGE, ActivityLogStatus.SUCCESS, map);
        }

        commonEmailUtils.sendSubscriptionUpdateEmail(
            shopifyGraphqlClient,
            commonUtils.prepareShopifyResClient(shop),
            subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
            EmailSettingType.ORDER_FREQUENCY_UPDATED);


        for (Map.Entry<String, Double> entry : subscriptionLineItemPriceByLineId.entrySet()) {
            SubscriptionContractQuery.Node node = subscriptionLineItems.stream().filter(n -> n.getId().equals(entry.getKey())).findFirst().get();
            subscriptionContractUpdateLineItem(contractId, shop, node.getQuantity(), node.getVariantId().orElse(""), node.getId(), node.getSellingPlanName().orElse(null), entry.getValue() * (billingIntervalCount / deliveryIntervalCount), false, "", eventSource);
        }

        return subscriptionContractResponseEntity;
    }
    @Override
    public List<SellingPlanAnchorInput> getOldAnchors(List<SubscriptionContractQuery.Anchor> anchorDays) {
        List<SellingPlanAnchorInput> oldAnchors = new ArrayList<>();

        for (SubscriptionContractQuery.Anchor anchor : anchorDays ){
            SellingPlanAnchorInput.Builder sellingPlanAnchorBuilder = SellingPlanAnchorInput.builder();

            sellingPlanAnchorBuilder.type(anchor.getType());
            sellingPlanAnchorBuilder.day(anchor.getDay());
            sellingPlanAnchorBuilder.month(anchor.getMonth().orElse(null));
            sellingPlanAnchorBuilder.cutoffDay(anchor.getCutoffDay().orElse(null));

            oldAnchors.add(sellingPlanAnchorBuilder.build());

        }
        return oldAnchors;
    }

    private ZonedDateTime setNextDayOfWeek(ZonedDateTime nextBillingDate, OrderDayOfWeek localOrderDayOfWeek) {
        ZonedDateTime adjustedDate = null;
        switch (localOrderDayOfWeek) {
            case MONDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                break;
            case TUESDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
                break;
            case WEDNESDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
                break;
            case THURSDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
                break;
            case FRIDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
                break;
            case SATURDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
                break;
            case SUNDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
        }
        return adjustedDate;
    }

    public List<AppstleCycle> getExistingDiscountCycles(SubscriptionContractQuery.SubscriptionContract subscriptionContract, String lineId) {
        if (subscriptionContract == null) {
            return null;
        }

        Optional<SubscriptionContractQuery.Node> lineNode = subscriptionContract.getLines().getEdges()
            .stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(n -> n.getId().equals(lineId))
            .findFirst();

        if (lineNode.isPresent() && lineNode.get().getPricingPolicy().isPresent()) {
            List<AppstleCycle> cycles = lineNode.get().getPricingPolicy().get().getCycleDiscounts()
                .stream().map(cycleDiscount -> buildAppstleCycle(cycleDiscount)).collect(Collectors.toList());

            return cycles;
        }

        return null;
    }

    private AppstleCycle buildAppstleCycle(SubscriptionContractQuery.CycleDiscount cycleDiscount) {

        AppstleCycle appstleCycle = new AppstleCycle();

        appstleCycle.setAfterCycle(cycleDiscount.getAfterCycle());

        if (SellingPlanPricingPolicyAdjustmentType.PERCENTAGE.equals(cycleDiscount.getAdjustmentType())) {
            appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
            SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) cycleDiscount.getAdjustmentValue();
            appstleCycle.setValue(adjustmentValue.getPercentage());
        } else if (SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT.equals(cycleDiscount.getAdjustmentType())) {
            appstleCycle.setDiscountType(DiscountType.FIXED);
            SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) cycleDiscount.getAdjustmentValue();
            appstleCycle.setValue(Double.parseDouble(adjustmentValue.getAmount().toString()));
        } else if (SellingPlanPricingPolicyAdjustmentType.PRICE.equals(cycleDiscount.getAdjustmentType())) {
            appstleCycle.setDiscountType(DiscountType.PRICE);
            SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) cycleDiscount.getAdjustmentValue();
            appstleCycle.setValue(Double.parseDouble(adjustmentValue.getAmount().toString()));
        }

        return appstleCycle;
    }


    @Override
    public void subscriptionContractUpdateLineItemPrice(Long contractId, String shop, Double basePrice, String lineId, ActivityLogEventSource eventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = optionalSubscriptionContractQueryResponse
            .getData().flatMap(SubscriptionContractQuery.Data::getSubscriptionContract).stream().findFirst();

        if (subscriptionContractOptional.isPresent()) {
            subscriptionContractUpdateLineItemPrice(subscriptionContractOptional.get(), shop, basePrice, lineId, eventSource);
        }
    }

    @Override
    public void subscriptionContractUpdateLineItemPrice(SubscriptionContractQuery.SubscriptionContract subscriptionContract, String shop, Double basePrice, String lineId, ActivityLogEventSource eventSource) throws Exception {
        Optional<SubscriptionContractQuery.Node> existingLineItem = subscriptionContract.getLines().getEdges()
            .stream().map(SubscriptionContractQuery.Edge::getNode)
            .filter(n -> n.getId().equals(lineId))
            .findFirst();

        if (existingLineItem.isPresent()) {
            List<AppstleCycle> cycles = getExistingDiscountCycles(subscriptionContract, lineId);
            subscriptionContractUpdateLineItemPricingPolicy(shop, subscriptionContract, lineId, basePrice, cycles, eventSource);
        }
    }

    @Override
    public void subscriptionContractUpdateLineItemPricingPolicy(String shop, Long contractId, String lineId, Double basePrice, List<AppstleCycle> cycles, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalSubscriptionContractQueryResponse.getData().get().getSubscriptionContract().get();

        subscriptionContractUpdateLineItemPricingPolicy(shop, subscriptionContract, lineId, basePrice, cycles, eventSource);
    }

    @Override
    public void subscriptionContractUpdateLineItemPricingPolicy(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, String lineId, Double basePrice, List<AppstleCycle> cycles, ActivityLogEventSource eventSource) throws Exception {
        Long contractId = ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Optional<SubscriptionContractQuery.Node> existingLineItem = subscriptionContract.getLines().getEdges()
            .stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(n -> n.getId().equals(lineId))
            .findFirst();

        if (existingLineItem.isPresent()) {

            double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

            double existingBasePrice = Double.parseDouble(existingLineItem.get().getCurrentPrice().getAmount().toString()) / fulfillmentCountMultiplier;

            double newBasePrice = basePrice;

            boolean isDiscountCyclesChanged;

            if (existingLineItem.get().getPricingPolicy().isPresent()) {
                existingBasePrice = Double.parseDouble(existingLineItem.get().getPricingPolicy().get().getBasePrice().getAmount().toString());
                isDiscountCyclesChanged = isDiscountCyclesChanged(cycles, existingLineItem.get().getPricingPolicy().get().getCycleDiscounts());
            } else {
                isDiscountCyclesChanged = isDiscountCyclesChanged(cycles, null);
            }

            if (existingBasePrice != newBasePrice || isDiscountCyclesChanged) {

                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

                Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
                log.info("REST request for {} Response received from graphql update subscription contract {} ", shop, contractId);

                long countOfErrors = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                    .orElse(new ArrayList<>()).stream()
                    .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                    .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

                if (countOfErrors == 0) {
                    // get draft Id from the response
                    Optional<String> optionalDraftId = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId());


                    if (optionalDraftId.isPresent()) {
                        String draftId = optionalDraftId.get();
                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                        Double currentPrice = newBasePrice * fulfillmentCountMultiplier;

                        Optional<SubscriptionContractQuery.PricingPolicy> existingPricingPolicyOptional = existingLineItem.get().getPricingPolicy();

                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                        if (!CollectionUtils.isNullOrEmpty(cycles)) {
                            //sort by after cycle
                            cycles.sort(Comparator.comparing(AppstleCycle::getAfterCycle));

                            // Only consider first 2 cycles
                            cycles = cycles.subList(0, Math.min(cycles.size(), 2));

                            int totalCycles = 1;
                            List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                            totalCycles = totalCycles + subscriptionBillingAttempts.size();

                            for (AppstleCycle cycle : cycles) {

                                SubscriptionPricingPolicyCycleDiscountsInput cycleDiscount = buildCycleDiscountInput(newBasePrice, cycle.getAfterCycle(), cycle.getDiscountType().toDiscountTypeUnit(), cycle.getValue(), fulfillmentCountMultiplier);

                                if (cycleDiscount != null) {
                                    cycleDiscounts.add(cycleDiscount);

                                    if (totalCycles >= cycleDiscount.afterCycle()) {
                                        currentPrice = Double.parseDouble(cycleDiscount.computedPrice().toString());
                                    }
                                }
                            }
                        }

                        SubscriptionPricingPolicyInput pricingPolicyInput = null;
                        if (!CollectionUtils.isNullOrEmpty(cycleDiscounts)) {
                            SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder();
                            pricingPolicyInputBuilder.basePrice(newBasePrice);
                            pricingPolicyInputBuilder.cycleDiscounts(cycleDiscounts);
                            pricingPolicyInput = pricingPolicyInputBuilder.build();
                        }

                        SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                            .currentPrice(currentPrice)
                            .pricingPolicy(pricingPolicyInput)
                            .build();

                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                        if (optionalMutationResponse.hasErrors()) {
                            throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                        }

                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                            throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (optionalDraftCommitResponse.hasErrors()) {
                            throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                        }

                        String variantId = existingLineItem.get().getVariantId().isPresent() ? ShopifyGraphQLUtils.getVariantId(existingLineItem.get().getVariantId().get()) + "" : "";
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("variantId", variantId);
                        map.put("title", Optional.ofNullable(existingLineItem.get().getTitle()).orElse(""));

                        boolean dataChanged = false;

                        if (existingBasePrice != currentPrice) {
                            dataChanged = true;
                            map.put("oldPrice", Optional.ofNullable(existingBasePrice).orElse(0d));
                            map.put("newPrice", newBasePrice);
                        }

                        if (existingPricingPolicyOptional.isEmpty() && pricingPolicyInput != null) {
                            dataChanged = true;
                            map.put("oldPricingPolicy", "");
                            map.put("newPricingPolicy", getPricingPolicyMap(pricingPolicyInput));
                        } else if (existingPricingPolicyOptional.isPresent() && pricingPolicyInput == null) {
                            dataChanged = true;
                            map.put("oldPricingPolicy", getPricingPolicyMap(existingPricingPolicyOptional.get()));
                            map.put("newPricingPolicy", "");
                        } else if (existingPricingPolicyOptional.isPresent() && pricingPolicyInput != null
                            && !OBJECT_MAPPER.writeValueAsString(getPricingPolicyMap(existingPricingPolicyOptional.get())).equalsIgnoreCase(OBJECT_MAPPER.writeValueAsString(getPricingPolicyMap(pricingPolicyInput)))) {
                            dataChanged = true;
                            map.put("oldPricingPolicy", getPricingPolicyMap(existingPricingPolicyOptional.get()));
                            map.put("newPricingPolicy", getPricingPolicyMap(pricingPolicyInput));
                        }

                        if (dataChanged) {
                            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_PRICING_POLICY_CHANGE, ActivityLogStatus.SUCCESS, map);
                            commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);
                            resyncBuildABoxDiscount(shop, contractId);
                        }
                    }
                }
            }
        }
    }

    private boolean isDiscountCyclesChanged(List<AppstleCycle> appstleCycles, List<SubscriptionContractQuery.CycleDiscount> cycleDiscounts) {
        appstleCycles = Optional.ofNullable(appstleCycles).orElse(new ArrayList<>());
        cycleDiscounts = Optional.ofNullable(cycleDiscounts).orElse(new ArrayList<>());

        if (CollectionUtils.isNullOrEmpty(appstleCycles) && CollectionUtils.isNullOrEmpty(cycleDiscounts)) {
            return false;
        }

        if (appstleCycles.size() != cycleDiscounts.size()) {
            return true;
        }

        appstleCycles.sort(Comparator.comparing(AppstleCycle::getAfterCycle));

        for (int i = 0; i < appstleCycles.size(); i++) {
            AppstleCycle appstleCycle = appstleCycles.get(i);
            SubscriptionContractQuery.CycleDiscount cycleDiscount = cycleDiscounts.get(i);
            AppstleCycle cycleDiscountConverted = buildAppstleCycle(cycleDiscount);

            if (!(cycleDiscountConverted.getAfterCycle().equals(appstleCycle.getAfterCycle())
                && Objects.equals(cycleDiscountConverted.getDiscountType(), appstleCycle.getDiscountType())
                && cycleDiscountConverted.getValue().equals(appstleCycle.getValue()))) {
                return true;
            }
        }

        return false;
    }

    private Map<String, Object> getPricingPolicyMap(SubscriptionPricingPolicyInput pricingPolicyInput) {
        try {
            if (pricingPolicyInput != null) {
                Map<String, Object> map = new LinkedHashMap<>();
                Double basePrice = Double.parseDouble(pricingPolicyInput.basePrice().toString());
                map.put("basePrice", basePrice);

                List<Map<String, Object>> cycleDiscounts = new ArrayList<>();
                if (!CollectionUtils.isNullOrEmpty(pricingPolicyInput.cycleDiscounts())) {
                    pricingPolicyInput.cycleDiscounts().forEach(cd -> {
                        Map<String, Object> cycleDiscountMap = new LinkedHashMap<>();
                        cycleDiscountMap.put("afterCycle", cd.afterCycle());
                        cycleDiscountMap.put("adjustmentType", cd.adjustmentType().rawValue());
                        if (Objects.equals(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE, cd.adjustmentType())) {
                            cycleDiscountMap.put("adjustmentValue", cd.adjustmentValue().percentage());
                        } else if (Objects.equals(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT, cd.adjustmentType())) {
                            cycleDiscountMap.put("adjustmentValue", Double.parseDouble(cd.adjustmentValue().fixedValue().toString()));
                        }
                        cycleDiscountMap.put("computedPrice", Double.parseDouble(cd.computedPrice().toString()));

                        cycleDiscounts.add(cycleDiscountMap);
                    });
                }

                map.put("cycleDiscounts", cycleDiscounts);

                return map;
            }
        } catch (Exception e) {
            log.info("Error occurred for getPricingPolicyJson(pricingPolicyInput). Error = {}", ExceptionUtils.getStackTrace(e));
        }

        return new LinkedHashMap<>();
    }

    private Map<String, Object> getPricingPolicyMap(SubscriptionContractQuery.PricingPolicy pricingPolicy) {
        try {
            if (pricingPolicy != null) {
                Map<String, Object> map = new LinkedHashMap<>();
                Double basePrice = Double.parseDouble(pricingPolicy.getBasePrice().getAmount().toString());
                map.put("basePrice", basePrice);

                List<Map<String, Object>> cycleDiscounts = new ArrayList<>();
                if (!CollectionUtils.isNullOrEmpty(pricingPolicy.getCycleDiscounts())) {
                    pricingPolicy.getCycleDiscounts().forEach(cd -> {
                        Map<String, Object> cycleDiscountMap = new LinkedHashMap<>();
                        cycleDiscountMap.put("afterCycle", cd.getAfterCycle());
                        cycleDiscountMap.put("adjustmentType", cd.getAdjustmentType().rawValue());

                        if (cd.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) {
                            SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) cd.getAdjustmentValue();
                            cycleDiscountMap.put("adjustmentValue", adjustmentValue.getPercentage());
                        } else if (cd.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2) {
                            SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) cd.getAdjustmentValue();
                            cycleDiscountMap.put("adjustmentValue", Double.parseDouble(adjustmentValue.getAmount().toString()));
                        }

                        cycleDiscountMap.put("computedPrice", Double.parseDouble(cd.getComputedPrice().getAmount().toString()));

                        cycleDiscounts.add(cycleDiscountMap);
                    });
                }

                map.put("cycleDiscounts", cycleDiscounts);

                return map;
            }
        } catch (Exception e) {
            log.info("Error occurred for getPricingPolicyJson(pricingPolicy). Error = {}", ExceptionUtils.getStackTrace(e));
        }

        return new LinkedHashMap<>();
    }

    @Override
    public void subscriptionContractUpdateLineItemSellingPlan(Long contractId, String shop, Long sellingPlanId, String sellingPlanName, String lineId, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.Node> existingLineItem = optionalSubscriptionContractQueryResponse
            .getData()
            .map(d ->
                d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                    .map(SubscriptionContractQuery.Lines::getEdges)
                    .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>())
            .stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(n -> n.getId().equals(lineId))
            .findFirst();

        if (existingLineItem.isPresent()) {

            SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalSubscriptionContractQueryResponse.getData().get().getSubscriptionContract().get();

            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
            log.info("REST request for {} Response received from graphql update subscription contract {} ", shop, contractId);

            long countOfErrors = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

            if (countOfErrors == 0) {
                // get draft Id from the response
                Optional<String> optionalDraftId = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                    .map(draft -> draft.get().getId());


                if (optionalDraftId.isPresent()) {
                    String draftId = optionalDraftId.get();
                    SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                    String existingSellingPlanId = existingLineItem.get().getSellingPlanId().orElse(null);
                    String existingSellingPlanName = existingLineItem.get().getSellingPlanName().orElse(null);

                    if (sellingPlanId != null) {
                        subscriptionLineUpdateInputBuilder.sellingPlanId(ShopifyGraphQLUtils.getGraphQLSellingPlanId(sellingPlanId));
                    }

                    if (sellingPlanName != null) {
                        subscriptionLineUpdateInputBuilder.sellingPlanName(sellingPlanName);
                    }

                    SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                    SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                    Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                    if (optionalMutationResponse.hasErrors()) {
                        throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                    }

                    List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalMutationResponseUserErrors.isEmpty()) {
                        throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                    }

                    String variantId = existingLineItem.get().getVariantId().isPresent() ? ShopifyGraphQLUtils.getVariantId(existingLineItem.get().getVariantId().get()) + "" : "";
                    Map<String, Object> map = new HashMap<>();
                    map.put("variantId", variantId);
                    map.put("title", Optional.ofNullable(existingLineItem.get().getTitle()).orElse(""));

                    boolean dataChaged = false;
                    if ((existingSellingPlanId == null && sellingPlanId != null)
                        || !ShopifyGraphQLUtils.getSellingPlanId(existingSellingPlanId).equals(sellingPlanId)) {
                        dataChaged = true;
                        map.put("oldSellingPlanId", ShopifyGraphQLUtils.getSellingPlanId(Optional.ofNullable(existingSellingPlanId).orElse("")));
                        map.put("newSellingPlanId", sellingPlanId);
                    }

                    if ((existingSellingPlanName == null && sellingPlanName != null)
                        || !existingSellingPlanName.equals(sellingPlanName)) {
                        dataChaged = true;
                        map.put("oldSellingPlanName", existingSellingPlanName);
                        map.put("newSellingPlanName", sellingPlanName);
                    }

                    if (dataChaged) {
                        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_SELLING_PLAN_CHANGE, ActivityLogStatus.SUCCESS, map);
                    }
                }
            }
        }
    }

    @Override
    public SubscriptionContractDetailsDTO updateDetails(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract) {
        return updateDetails(shop, subscriptionContract, false);
    }

    @Override
    public SubscriptionContractDetailsDTO updateDetails(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, Boolean updateCancelled) {
        try {
            Long contractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));
            Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = findByContractId(contractId);

            if (!updateCancelled && subscriptionContract.getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED) && subscriptionContractDetailsDTOOptional.isEmpty()) {
                return null;
            }

            if (subscriptionContractDetailsDTOOptional.isEmpty()) {
                log.info("SubscriptionContractId=" + contractId + " was missing in DB for shop=" + shop);
            }

            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsDTOOptional.orElse(new SubscriptionContractDetailsDTO());

            subscriptionContractDetailsDTO.setShop(shop);
            subscriptionContractDetailsDTO.setSubscriptionContractId(contractId);
            subscriptionContractDetailsDTO.setGraphSubscriptionContractId(subscriptionContract.getId());
            subscriptionContractDetailsDTO.setBillingPolicyInterval(subscriptionContract.getBillingPolicy().getInterval().rawValue());
            subscriptionContractDetailsDTO.setBillingPolicyIntervalCount(subscriptionContract.getBillingPolicy().getIntervalCount());
            subscriptionContractDetailsDTO.setMinCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null));
            subscriptionContractDetailsDTO.setMaxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null));

            String currencyCode = subscriptionContract.getLines().getEdges().stream().findFirst()
                .map(SubscriptionContractQuery.Edge::getNode)
                .map(SubscriptionContractQuery.Node::getCurrentPrice)
                .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
                .map(CurrencyCode::rawValue).orElse(null);

            subscriptionContractDetailsDTO.setCurrencyCode(currencyCode);

            if (subscriptionContract.getCustomer().isPresent()) {
                subscriptionContractDetailsDTO.setCustomerId(Long.parseLong(subscriptionContract.getCustomer().get().getId().replace(CUSTOMER_ID_PREFIX, "")));
                subscriptionContractDetailsDTO.setGraphCustomerId(subscriptionContract.getCustomer().get().getId());

                if (StringUtils.isBlank(subscriptionContractDetailsDTO.getCustomerName())) {
                    subscriptionContractDetailsDTO.setCustomerName(subscriptionContract.getCustomer().get().getDisplayName());
                }

                if (StringUtils.isBlank(subscriptionContractDetailsDTO.getCustomerEmail())) {
                    subscriptionContractDetailsDTO.setCustomerEmail(subscriptionContract.getCustomer().get().getEmail().orElse(null));
                }

                if (StringUtils.isBlank(subscriptionContractDetailsDTO.getPhone())) {
                    subscriptionContractDetailsDTO.setPhone(subscriptionContract.getCustomer().get().getPhone().orElse(null));
                }

                if (subscriptionContract.getCustomer().get().getDisplayName() != null) {
                    String[] nameTokens = subscriptionContract.getCustomer().get().getDisplayName().split(" ");
                }
            }

            if (subscriptionContract.getDeliveryPolicy() != null) {
                if (subscriptionContract.getDeliveryPolicy().getInterval() != null) {
                    subscriptionContractDetailsDTO.setDeliveryPolicyInterval(subscriptionContract.getDeliveryPolicy().getInterval().rawValue());
                    subscriptionContractDetailsDTO.setDeliveryPolicyIntervalCount(subscriptionContract.getDeliveryPolicy().getIntervalCount());
                }
            }

            if (!subscriptionContract.getStatus().rawValue().toLowerCase().equals(subscriptionContractDetailsDTO.getStatus())) {
                subscriptionContractDetailsDTO.setStatus(subscriptionContract.getStatus().rawValue().toLowerCase());
            }

            if (subscriptionContract.getStatus() == SubscriptionContractSubscriptionStatus.ACTIVE) {
                if (subscriptionContractDetailsDTO.getActivatedOn() == null) {

                    List<ActivityLog> activityLogs = activityLogRepository.findActivityLogsForContract(
                        shop,
                        ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS,
                        contractId,
                        List.of(ActivityLogEventType.CONTRACT_ACTIVATED),
                        ActivityLogStatus.SUCCESS);

                    if (CollectionUtils.isNullOrEmpty(activityLogs)) {
                        subscriptionContractDetailsDTO.setActivatedOn(ZonedDateTime.now(ZoneId.of("UTC")));
                    } else {
                        ActivityLog activityLog = activityLogs.get(0);
                        subscriptionContractDetailsDTO.setActivatedOn(activityLog.getCreateAt());
                    }
                }
            } else if (subscriptionContract.getStatus() == SubscriptionContractSubscriptionStatus.CANCELLED) {
                if (subscriptionContractDetailsDTO.getCancelledOn() == null) {
                    List<ActivityLog> activityLogs = activityLogRepository.findActivityLogsForContract(
                        shop,
                        ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS,
                        contractId,
                        List.of(ActivityLogEventType.CONTRACT_CANCELLED, ActivityLogEventType.DUNNING_CANCELLED),
                        ActivityLogStatus.SUCCESS);

                    if (CollectionUtils.isNullOrEmpty(activityLogs)) {
                        subscriptionContractDetailsDTO.setCancelledOn(ZonedDateTime.now(ZoneId.of("UTC")));
                    } else {
                        ActivityLog activityLog = activityLogs.get(0);
                        subscriptionContractDetailsDTO.setCancelledOn(activityLog.getCreateAt());
                    }
                }
            } else if (subscriptionContract.getStatus() == SubscriptionContractSubscriptionStatus.PAUSED) {
                if (subscriptionContractDetailsDTO.getPausedOn() == null) {
                    List<ActivityLog> activityLogs = activityLogRepository.findActivityLogsForContract(
                        shop,
                        ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS,
                        contractId,
                        List.of(ActivityLogEventType.CONTRACT_PAUSED, ActivityLogEventType.DUNNING_PAUSED),
                        ActivityLogStatus.SUCCESS);

                    if (CollectionUtils.isNullOrEmpty(activityLogs)) {
                        subscriptionContractDetailsDTO.setPausedOn(ZonedDateTime.now(ZoneId.of("UTC")));
                    } else {
                        ActivityLog activityLog = activityLogs.get(0);
                        subscriptionContractDetailsDTO.setPausedOn(activityLog.getCreateAt());
                    }
                }
            }

            if (subscriptionContract.getOriginOrder().isPresent()) {
                SubscriptionContractQuery.OriginOrder originOrder = subscriptionContract.getOriginOrder().get();
                subscriptionContractDetailsDTO.setGraphOrderId(originOrder.getId());
                subscriptionContractDetailsDTO.setOrderId(Long.parseLong(originOrder.getId().replace(ORDER_ID_PREFIX, "")));
                subscriptionContractDetailsDTO.setOrderName(originOrder.getName());
            } else {
                subscriptionContractDetailsDTO.setGraphOrderId(null);
                subscriptionContractDetailsDTO.setOrderId(null);
                subscriptionContractDetailsDTO.setOrderName("UNKNOWN");
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));

            if (subscriptionContract.getNextBillingDate().isPresent()) {
                ZonedDateTime nextBillingDate = ZonedDateTime.parse(subscriptionContract.getNextBillingDate().get().toString(), dateTimeFormatter);
                subscriptionContractDetailsDTO.setNextBillingDate(nextBillingDate);
            } else {
                subscriptionContractDetailsDTO.setNextBillingDate(null);
            }

            ZonedDateTime createdAt = ZonedDateTime.parse(subscriptionContract.getCreatedAt().toString(), dateTimeFormatter);
            subscriptionContractDetailsDTO.setCreatedAt(createdAt);

            List<SubscriptionProductInfo> products = getProductData(subscriptionContract);

            String scdJson = OBJECT_MAPPER.writeValueAsString(products);
            subscriptionContractDetailsDTO.setContractDetailsJSON(scdJson);

            if (!subscriptionContract.getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED) && subscriptionContractDetailsDTOOptional.isEmpty()) {
                // If empty trigger create webhook
                sendManualContractCreateWebhook(subscriptionContractDetailsDTO);
            }

            subscriptionContractDetailsDTO = save(subscriptionContractDetailsDTO);

            return subscriptionContractDetailsDTO;
        } catch (Exception e) {
            log.info("Error while updating subscription contact details. ex={}", ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    public void sendManualContractCreateWebhook(SubscriptionContractDetailsDTO subscriptionContractDetails) {

        String shop = subscriptionContractDetails.getShop();
        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        try {
            log.info("Sending manual subscription create webhook. shop={}, contractId={}", shop, subscriptionContractDetails.getSubscriptionContractId());

            SubscriptionContractInfo subscriptionContractInfo = new SubscriptionContractInfo();

            subscriptionContractInfo.setId(subscriptionContractDetails.getSubscriptionContractId());
            subscriptionContractInfo.setAdminGraphqlApiId(subscriptionContractDetails.getGraphSubscriptionContractId());

            BillingPolicy billingPolicy = new BillingPolicy();
            billingPolicy.setInterval(subscriptionContractDetails.getBillingPolicyInterval());
            billingPolicy.setIntervalCount(subscriptionContractDetails.getBillingPolicyIntervalCount().longValue());
            billingPolicy.setMaxCycles(subscriptionContractDetails.getMaxCycles());
            billingPolicy.setMinCycles(subscriptionContractDetails.getMinCycles());
            subscriptionContractInfo.setBillingPolicy(billingPolicy);

            subscriptionContractInfo.setCurrencyCode(subscriptionContractDetails.getCurrencyCode());
            subscriptionContractInfo.setCustomerId(subscriptionContractDetails.getCustomerId());
            subscriptionContractInfo.setAdminGraphqlApiCustomerId(subscriptionContractDetails.getGraphCustomerId());

            DeliveryPolicy deliveryPolicy = new DeliveryPolicy();
            deliveryPolicy.setIntervalCount(subscriptionContractDetails.getDeliveryPolicyIntervalCount().longValue());
            deliveryPolicy.setInterval(subscriptionContractDetails.getDeliveryPolicyInterval());
            subscriptionContractInfo.setDeliveryPolicy(deliveryPolicy);

            subscriptionContractInfo.setStatus(subscriptionContractDetails.getStatus());
            subscriptionContractInfo.setOriginOrderId(subscriptionContractDetails.getOrderId());
            subscriptionContractInfo.setAdminGraphqlApiOriginOrderId(subscriptionContractDetails.getGraphOrderId());

            String webhookUrl = Optional.ofNullable(shopInfoDTO.getShopifyWebhookUrl()).orElse("https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/");
            HttpResponse<String> response = Unirest.post(webhookUrl).header("X-Shopify-Shop-Domain", shop).header("X-Shopify-Topic", "subscription_contracts/create").header("Content-Type", "application/json").body(OBJECT_MAPPER.writeValueAsString(subscriptionContractInfo)).asString();

            log.info("Manual subscription create webhook sent successfully. shop={}, contractId={}", shop, subscriptionContractDetails.getSubscriptionContractId());
        } catch (Exception e) {
            log.error("Error wile sending manual subscription contract create webhook. shop={}, contractId={}, ex={}", shop, subscriptionContractDetails.getSubscriptionContractId(), ExceptionUtils.getStackTrace(e));
        }
    }


    @Override
    public List<SubscriptionProductInfo> getProductData(SubscriptionContractQuery.SubscriptionContract subscriptionContract) {

        List<SubscriptionProductInfo> products = new ArrayList<>();

        try {
            List<SubscriptionContractQuery.Edge> subscriptionEdges = Optional.ofNullable(subscriptionContract.getLines().getEdges()).orElse(new ArrayList<>());


            for (SubscriptionContractQuery.Edge subscriptionEdge : subscriptionEdges) {
                SubscriptionContractQuery.Node lineItemNode = subscriptionEdge.getNode();
                SubscriptionProductInfo product = new SubscriptionProductInfo();
                product.setProductId(lineItemNode.getProductId().orElse(""));
                product.setSku(lineItemNode.getSku().orElse(""));
                product.setLineId(lineItemNode.getId());
                product.setTitle(lineItemNode.getTitle());
                product.setQuantity(lineItemNode.getQuantity());
                product.setVariantId(lineItemNode.getVariantId().orElse(""));
                product.setVariantTitle(lineItemNode.getVariantTitle().orElse(""));
                product.setVariantImage(lineItemNode.getVariantImage().map(SubscriptionContractQuery.VariantImage::getTransformedSrc).map(Object::toString).orElse(null));
                product.setCurrentPrice(lineItemNode.getCurrentPrice().getAmount().toString());
                product.setDiscountedPrice(lineItemNode.getLineDiscountedPrice().getAmount().toString());
                product.setPricingPolicyUpdated(true);
                if (lineItemNode.getPricingPolicy().isPresent()) {
                    SubscriptionContractQuery.PricingPolicy pricingPolicy = lineItemNode.getPricingPolicy().get();
                    product.setBasePrice(pricingPolicy.getBasePrice().getAmount().toString());

                    PricingPolicy pricingPolicyDTO = new PricingPolicy();
                    pricingPolicyDTO.setBasePrice(pricingPolicy.getBasePrice().getAmount().toString());

                    if (!org.springframework.util.CollectionUtils.isEmpty(pricingPolicy.getCycleDiscounts())) {
                        List<CycleDiscount> cycleDiscountDTOList = new ArrayList<>(2);
                        pricingPolicy.getCycleDiscounts().forEach(cycleDiscount -> {
                            CycleDiscount cycleDiscountDTO = new CycleDiscount();
                            cycleDiscountDTO.setAfterCycle(cycleDiscount.getAfterCycle());
                            cycleDiscountDTO.setComputedPrice(cycleDiscount.getComputedPrice().getAmount().toString());
                            cycleDiscountDTO.setAdjustmentType(cycleDiscount.getAdjustmentType().rawValue());

                            if (cycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) {
                                SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) cycleDiscount.getAdjustmentValue();
                                cycleDiscountDTO.setAdjustmentValue(adjustmentValue.getPercentage() + "");
                            } else if (cycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2) {
                                SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) cycleDiscount.getAdjustmentValue();
                                cycleDiscountDTO.setAdjustmentValue(adjustmentValue.getAmount().toString());
                            }

                            cycleDiscountDTOList.add(cycleDiscountDTO);
                        });
                        pricingPolicyDTO.setCycleDiscounts(cycleDiscountDTOList);
                    }
                    product.setPricingPolicy(pricingPolicyDTO);
                }

                product.setSellingPlanId(lineItemNode.getSellingPlanId().orElse(""));
                product.setSellingPlanName(lineItemNode.getSellingPlanName().orElse(""));
                products.add(product);
            }
        } catch (Exception ex) {

        }

        return products;
    }

    @Override
    public void updateDeliveryPriceBySubscriptionContractId(String shop, Long contractId, Double deliveryPrice, ActivityLogEventSource eventSource) throws Exception {
        String graphSubscriptionContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(graphSubscriptionContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Double oldDeliveryPrice =
            requireNonNull(subscriptionContractQueryResponse.getData())
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getDeliveryPrice)
                    .map(SubscriptionContractQuery.DeliveryPrice::getAmount)
                    .map(Object::toString)
                    .map(Double::parseDouble)
                    .orElse(0.0D))
                .orElse(0.0D);

        Optional<String> draftIdOptional = generateDraftId(graphSubscriptionContractId, shopifyGraphqlClient);

        if (draftIdOptional.isPresent()) {
            SubscriptionDraftInput subscriptionDraftInput = SubscriptionDraftInput.builder().deliveryPrice(deliveryPrice).build();

            SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(draftIdOptional.get(), subscriptionDraftInput);
            Response<Optional<SubscriptionDraftUpdateMutation.Data>> optionalDraftUpdateResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftUpdateMutation);

            if (optionalDraftUpdateResponse.hasErrors()) {
                throw new Exception(optionalDraftUpdateResponse.getErrors().get(0).getMessage());
            }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftIdOptional.get());
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

            if (optionalMutationResponse2.hasErrors()) {
                throw new Exception(optionalMutationResponse2.getErrors().get(0).getMessage());
            }

            Map<String, Object> map = new HashMap<>();
            map.put("oldDeliveryPrice", oldDeliveryPrice);
            map.put("newDeliveryPrice", deliveryPrice);
            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.MANUAL_DELIVERY_PRICE_UPDATED, ActivityLogStatus.SUCCESS, map);
        } else {
            throw new Exception("Draft Id not found.");
        }
    }

    private Optional<String> generateDraftId(String graphSubscriptionContractId, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(graphSubscriptionContractId);
        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

        return Objects.requireNonNull(optionalMutationResponse.getData())
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
            .map(draft -> draft.get().getId());
    }

    @Override
    public SubscriptionContractUpdateResult updateDeliveryMethod(String shop, Long contractId, String deliveryMethodName, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        SubscriptionDeliveryMethodShippingOptionInput subscriptionDeliveryMethodShippingOptionInput = SubscriptionDeliveryMethodShippingOptionInput
            .builder()
            .code(deliveryMethodName)
            .title(deliveryMethodName)
            .presentmentTitle(deliveryMethodName)
            .build();

        SubscriptionDeliveryMethodShippingInput subscriptionDeliveryMethodShippingInput = SubscriptionDeliveryMethodShippingInput
            .builder()
            .shippingOption(subscriptionDeliveryMethodShippingOptionInput)
            .build();

        SubscriptionDeliveryMethodInput subscriptionDeliveryMethodInput = SubscriptionDeliveryMethodInput
            .builder()
            .shipping(subscriptionDeliveryMethodShippingInput)
            .build();

        subscriptionDraftInputBuilder.deliveryMethod(subscriptionDeliveryMethodInput);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            log.error("An error occurred while updating delivery method name.");
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("deliveryMethodName", deliveryMethodName);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DELIVERY_METHOD_UPDATED, ActivityLogStatus.SUCCESS, map);

        return subscriptionContractUpdateResult;
    }

    @Override
    public void subscriptionContractUpdateStatus(Long subscriptionContractId, String status, String shop, Integer pauseDurationCycle, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = findByContractId(subscriptionContractId);

        // IF DB entry is not present, create it
        if (subscriptionContractDetailsDTO.isEmpty()) {
            getSubscriptionContractRawInternal(subscriptionContractId, shop, shopifyGraphqlClient);
            subscriptionContractDetailsDTO = findByContractId(subscriptionContractId);
        }

        if(subscriptionContractDetailsDTO.isEmpty()) {
            log.info("No contract found for status update. shop={}, contractId={}, status={}", shop, subscriptionContractId, status);
            return;
        }

        if(status.equalsIgnoreCase(subscriptionContractDetailsDTO.get().getStatus())) {
            log.info("Contract already in same status. shop={}, contractId={}, status={}", shop, subscriptionContractId, status);
            return;
        }

        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        ZonedDateTime nextBillingDate = mayBeAdjustNextBillingDate(subscriptionContractId);

        subscriptionDraftInputBuilder.status(SubscriptionContractSubscriptionStatus.safeValueOf(status));

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, subscriptionContractId, subscriptionDraftInput);

        if (subscriptionContractUpdateResult.isSuccess()) {
            ActivityLogEventType activityLogEventType;
            if (status.equalsIgnoreCase("ACTIVE")) {
                activityLogEventType = ActivityLogEventType.CONTRACT_ACTIVATED;
            } else if (status.equalsIgnoreCase("PAUSED")) {
                activityLogEventType = ActivityLogEventType.CONTRACT_PAUSED;
            } else {
                activityLogEventType = ActivityLogEventType.CONTRACT_CANCELLED;
            }

            commonUtils.writeActivityLog(shop, subscriptionContractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, activityLogEventType, ActivityLogStatus.SUCCESS);

            if (subscriptionContractDetailsDTO.isPresent()) {
                if (status.equalsIgnoreCase("ACTIVE")) {
                    subscriptionContractDetailsDTO.get().setStatus("active");
                    subscriptionContractDetailsDTO.get().setActivatedOn(ZonedDateTime.now(ZoneId.of("UTC")));
                } else if (status.equalsIgnoreCase("PAUSED")) {
                    subscriptionContractDetailsDTO.get().setStatus("paused");
                    subscriptionContractDetailsDTO.get().setPausedOn(ZonedDateTime.now(ZoneId.of("UTC")));
                } else {
                    subscriptionContractDetailsDTO.get().setStatus("cancelled");
                    subscriptionContractDetailsDTO.get().setCancelledOn(ZonedDateTime.now(ZoneId.of("UTC")));
                }
                SubscriptionContractDetailsDTO persistedSubscriptionContract = save(subscriptionContractDetailsDTO.get());
                sendSubscriptionPausedMail(subscriptionContractId, shop, shopifyGraphqlClient, persistedSubscriptionContract);
                sendSubscriptionResumedMail(subscriptionContractId, shop, shopifyGraphqlClient, persistedSubscriptionContract);
                sendMailForSubscriptionCanceled(subscriptionContractId, shop);
            }
        } else {
            log.error("An error occurred while activating/pausing subscription contractId=" + subscriptionContractId + " status=" + status + " errorMessage=" + subscriptionContractUpdateResult.getErrorMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        if (status.equalsIgnoreCase("ACTIVE") && nextBillingDate != null) {
            subscriptionContractUpdateNextBillingDate(subscriptionContractId, shop, nextBillingDate, eventSource);
        }

        if(status.equalsIgnoreCase("PAUSED") && Objects.nonNull(pauseDurationCycle)){
            List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatusAndShop(subscriptionContractId, BillingAttemptStatus.QUEUED, shop);

            if(pauseDurationCycle < subscriptionBillingAttemptDTOList.size()) {
                subscriptionBillingAttemptDTOList.sort(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));
                ZonedDateTime pauseTillDate = subscriptionBillingAttemptDTOList.get(pauseDurationCycle - 1).getBillingDate();
                pauseTillDate = pauseTillDate.plusMinutes(1);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));
                String waitTillTimestamp = dateTimeFormatter.format(pauseTillDate);
                SubscriptionUpdateStatusRequest subscriptionUpdateStatusRequest = new SubscriptionUpdateStatusRequest(
                    shop,
                    subscriptionContractId,
                    "ACTIVE",
                    waitTillTimestamp
                );

                String statusUpdateRequestJson = OBJECT_MAPPER.writeValueAsString(subscriptionUpdateStatusRequest);

                awsUtils.startStepExecution(shop, subscriptionContractId, AwsUtils.SUBSCRIPTION_UPDATE_STATUS_SM_ARN, statusUpdateRequestJson);
            }

        }
    }

    private ZonedDateTime mayBeAdjustNextBillingDate(Long contractId) {

        Optional<SubscriptionContractDetailsDTO> optionalSubscriptionContractDetailsDTO = findByContractId(contractId);

        if (optionalSubscriptionContractDetailsDTO.isEmpty()) {
            return null;
        }

        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = optionalSubscriptionContractDetailsDTO.get();

        Integer billingPolicyIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        String billingPolicyInterval = subscriptionContractDetailsDTO.getBillingPolicyInterval();

        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));

        ZonedDateTime subscriptionContractDetailsNextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate();
        if (utcNow.compareTo(subscriptionContractDetailsNextBillingDate) < 0) {
            return null;
        }

        ZonedDateTime nextBillingDate = SubscriptionUtils.getNextBillingDate(billingPolicyIntervalCount, billingPolicyInterval, subscriptionContractDetailsNextBillingDate);

        while (nextBillingDate.compareTo(utcNow) < 1) {
            nextBillingDate = SubscriptionUtils.getNextBillingDate(billingPolicyIntervalCount, billingPolicyInterval, nextBillingDate);
        }

        return nextBillingDate.withZoneSameInstant(ZoneId.of("UTC"));
    }

    @Override
    public void hideSubscription(String shop, Long contractId, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        subscriptionContractService.delete(shop, contractId, true, null, null, shopifyGraphqlClient, eventSource);
        deleteByContractId(contractId);
        subscriptionBillingAttemptService.deleteByContractId(contractId);
    }

    @Override
    public void updateNextBillingDateTime(String shop, Long contractId, Integer hour, Integer minute, Integer zonedOffSetHours) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = findByContractId(contractId);
        if (subscriptionContractDetailsDTOOptional.isPresent()) {
            updateNextBillingDateTime(shop, hour, minute, zonedOffSetHours, subscriptionContractDetailsDTOOptional.get(), shopifyGraphqlClient);
        } else {
            throw new Exception("No subscription found");
        }
    }

    @Override
    public void updateNextBillingDateTime(String shop, Integer hour, Integer minute, Integer zonedOffSetHours,
                                          SubscriptionContractDetailsDTO subscriptionContractDetailsDTO,
                                          ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        Long contractId = subscriptionContractDetailsDTO.getSubscriptionContractId();
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(zonedOffSetHours));
        ZonedDateTime utcExistingNextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate();
        ZonedDateTime existingNextBillingDate = utcExistingNextBillingDate.withZoneSameInstant(zoneId);
        LocalTime localTime = LocalTime.of(hour, minute);
        ZonedDateTime nextBillingDate = existingNextBillingDate.with(localTime).withZoneSameInstant(ZoneId.of("UTC"));

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("UTC"));

        if (currentTime.compareTo(nextBillingDate) >= 0) {
            return;
        }

        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
        String formattedDate = dateTimeFormatter.format(nextBillingDate);
        subscriptionDraftInputBuilder.nextBillingDate(formattedDate);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            log.error("An error occurred while updating next order date during bulk automations. shop=" + shop + " contractId=" + contractId + " error=" + subscriptionContractUpdateResult.getErrorMessage());
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("hour", hour);
        map.put("minute", minute);
        map.put("zonedOffSetHours", zonedOffSetHours);

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION, ActivityLogEventType.NEXT_BILLING_TIME_CHANGE, ActivityLogStatus.SUCCESS, map);

        Optional<Long> nextBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(contractId);

        subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, contractId);

        subscriptionBillingAttemptService.deleteByContractIdAndStatusAndBillingDateAfter(contractId, BillingAttemptStatus.SKIPPED, nextBillingDate);

        disableRetryForLastFailedAttemptIfAny(contractId);

        subscriptionContractDetailsDTO.setNextBillingDate(nextBillingDate);

        save(subscriptionContractDetailsDTO);

        commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles(), nextBillingId.orElse(null));
    }

    @Override
    public Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractUpdateNextBillingDate(Long contractId, String shop, ZonedDateTime nextBillingDate, ActivityLogEventSource eventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
        String formattedDate = dateTimeFormatter.format(nextBillingDate);
        subscriptionDraftInputBuilder.nextBillingDate(formattedDate);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();
        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByShop(shop);

        ShopInfoDTO shopInfo = shopInfoOptional.get();

        if(BooleanUtils.isTrue(shopInfo.isOverwriteAnchorDay()) && !subscriptionContract.getBillingPolicy().getAnchors().isEmpty()) {

            boolean overwriteAnchorDay = false;

            int oldAnchorDay = subscriptionContract.getBillingPolicy().getAnchors().get(0).getDay();

            SellingPlanInterval billingInterval = subscriptionContract.getBillingPolicy().getInterval();

            int newAnchorDay = 0;
            if (billingInterval.equals(SellingPlanInterval.WEEK)) {
                newAnchorDay = nextBillingDate.getDayOfWeek().getValue();
            } else if (billingInterval.equals(SellingPlanInterval.MONTH)) {
                newAnchorDay = nextBillingDate.getDayOfMonth();
            } else if (billingInterval.equals(SellingPlanInterval.YEAR)) {
                newAnchorDay = nextBillingDate.getDayOfMonth();
                Optional<Integer> oldAnchorMonth = subscriptionContract.getBillingPolicy().getAnchors().get(0).getMonth();
                if (oldAnchorMonth.isPresent() && nextBillingDate.getMonth().getValue() != oldAnchorMonth.get()) {
                    overwriteAnchorDay = true;
                }
            }
            if (newAnchorDay != oldAnchorDay) {
                overwriteAnchorDay = true;
            }

            if(overwriteAnchorDay){

                SubscriptionBillingPolicyInput subscriptionBillingPolicyInput = SubscriptionBillingPolicyInput
                    .builder()
                    .intervalCount(subscriptionContract.getBillingPolicy().getIntervalCount())
                    .interval(subscriptionContract.getBillingPolicy().getInterval())
                    .maxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null))
                    .minCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null))
                    .anchors(new ArrayList<>())
                    .build();
                subscriptionDraftInputBuilder.billingPolicy(subscriptionBillingPolicyInput);

                Map<String, Object> map = new HashMap<>();
                map.put("oldAnchor", getOldAnchors(subscriptionContract.getBillingPolicy().getAnchors()));
                commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.ANCHOR_DAY_REMOVED, ActivityLogStatus.SUCCESS, map);
            }

        }

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        Optional<Long> nextBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(contractId);

        subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, contractId);

        subscriptionBillingAttemptService.deleteByContractIdAndStatusAndBillingDateAfter(contractId, BillingAttemptStatus.SKIPPED, nextBillingDate);

        disableRetryForLastFailedAttemptIfAny(contractId);

        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        subscriptionContractDetailsDTO.setNextBillingDate(nextBillingDate);

        save(subscriptionContractDetailsDTO);

        commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles(), nextBillingId.orElse(null));

        commonEmailUtils.sendSubscriptionUpdateEmail(
            shopifyGraphqlClient,
            commonUtils.prepareShopifyResClient(shop),
            subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
            EmailSettingType.NEXT_ORDER_DATE_UPDATED);

        Map<String, Object> map = new HashMap<>();
        map.put("nextOrderDate", nextBillingDate.toString());
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.NEXT_BILLING_DATE_CHANGE, ActivityLogStatus.SUCCESS, map);

        return subscriptionContractService.findSubscriptionContractByContractId(contractId, shop);
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractUpdateShippingAddress(Long contractId, String shop, ChangeShippingAddressVM changeShippingAddressVM, ActivityLogEventSource eventSource) throws Exception {
        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);

        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop settings not found for shop: "+ shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();

        try {
            if(StringUtils.isNotBlank(shopInfoDTO.getShipperHqApiKey()) && StringUtils.isNotBlank(shopInfoDTO.getShipperHqAuthCode())) {
                shipperHqService.checkAddressValidity(shopInfoDTO, changeShippingAddressVM);
            }

        } catch (Exception e) {
            throw e;
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
        if (StringUtils.isBlank(changeShippingAddressVM.getMethodType()) || changeShippingAddressVM.getMethodType().equalsIgnoreCase("SHIPPING")) {
            deliveryMethodBuilder.shipping(buildStandardShippingInput(changeShippingAddressVM));
        } else if (changeShippingAddressVM.getMethodType().equalsIgnoreCase("LOCAL")) {
            deliveryMethodBuilder.localDelivery(buildLocalDeliveryInput(changeShippingAddressVM));
        } else if (changeShippingAddressVM.getMethodType().equalsIgnoreCase("PICK_UP")) {
            deliveryMethodBuilder.pickup(buildLocalPickupInput(changeShippingAddressVM));
        }

        SubscriptionDeliveryMethodInput deliveryMethodInput = deliveryMethodBuilder.build();

        subscriptionDraftInputBuilder.deliveryMethod(deliveryMethodInput);
        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SHIPPING_ADDRESS_CHANGE, ActivityLogStatus.SUCCESS);

        commonEmailUtils.sendSubscriptionUpdateEmail(
            shopifyGraphqlClient,
            commonUtils.prepareShopifyResClient(shop),
            subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
            EmailSettingType.SHIPPING_ADDRESS_UPDATED);

        commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    @Override
    public void subscriptionContractUpdateMissingAddress(Long contractId, String shop, ChangeShippingAddressVM changeShippingAddressVM, ActivityLogEventSource eventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
        if (StringUtils.isBlank(changeShippingAddressVM.getMethodType()) || changeShippingAddressVM.getMethodType().equalsIgnoreCase("SHIPPING")) {
            deliveryMethodBuilder.shipping(buildStandardShippingInput(changeShippingAddressVM));
        } else if (changeShippingAddressVM.getMethodType().equalsIgnoreCase("LOCAL")) {
            deliveryMethodBuilder.localDelivery(buildLocalDeliveryInput(changeShippingAddressVM));
        } else if (changeShippingAddressVM.getMethodType().equalsIgnoreCase("PICK_UP")) {
            deliveryMethodBuilder.pickup(buildLocalPickupInput(changeShippingAddressVM));
        }

        SubscriptionDeliveryMethodInput deliveryMethodInput = deliveryMethodBuilder.build();

        subscriptionDraftInputBuilder.deliveryMethod(deliveryMethodInput);
        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SHIPPING_ADDRESS_CHANGE, ActivityLogStatus.SUCCESS);

    }

    private SubscriptionDeliveryMethodShippingInput buildStandardShippingInput(ChangeShippingAddressVM changeShippingAddressVM) {

        MailingAddressInput.Builder mailingAddressInputBuilder = MailingAddressInput.builder();
        mailingAddressInputBuilder.phone(changeShippingAddressVM.getPhone());
        mailingAddressInputBuilder.zip(changeShippingAddressVM.getZip());
        mailingAddressInputBuilder.city(changeShippingAddressVM.getCity());
        mailingAddressInputBuilder.address1(changeShippingAddressVM.getAddress1());
        mailingAddressInputBuilder.address2(changeShippingAddressVM.getAddress2());
        mailingAddressInputBuilder.firstName(changeShippingAddressVM.getFirstName());
        mailingAddressInputBuilder.lastName(changeShippingAddressVM.getLastName());
        mailingAddressInputBuilder.countryCode(CountryCode.safeValueOf(changeShippingAddressVM.getCountryCode()));
        mailingAddressInputBuilder.provinceCode(changeShippingAddressVM.getProvinceCode());
        mailingAddressInputBuilder.company(changeShippingAddressVM.getCompany());

        MailingAddressInput mailingAddressInput = mailingAddressInputBuilder.build();

        return SubscriptionDeliveryMethodShippingInput.builder().address(mailingAddressInput).build();
    }

    private SubscriptionDeliveryMethodLocalDeliveryInput buildLocalDeliveryInput(ChangeShippingAddressVM changeShippingAddressVM) {

        MailingAddressInput.Builder mailingAddressInputBuilder = MailingAddressInput.builder();
        mailingAddressInputBuilder.phone(changeShippingAddressVM.getPhone());
        mailingAddressInputBuilder.zip(changeShippingAddressVM.getZip());
        mailingAddressInputBuilder.city(changeShippingAddressVM.getCity());
        mailingAddressInputBuilder.address1(changeShippingAddressVM.getAddress1());
        mailingAddressInputBuilder.address2(changeShippingAddressVM.getAddress2());
        mailingAddressInputBuilder.firstName(changeShippingAddressVM.getFirstName());
        mailingAddressInputBuilder.lastName(changeShippingAddressVM.getLastName());
        mailingAddressInputBuilder.countryCode(CountryCode.safeValueOf(changeShippingAddressVM.getCountryCode()));
        mailingAddressInputBuilder.provinceCode(changeShippingAddressVM.getProvinceCode());
        mailingAddressInputBuilder.company(changeShippingAddressVM.getCompany());

        MailingAddressInput mailingAddressInput = mailingAddressInputBuilder.build();

        SubscriptionDeliveryMethodLocalDeliveryOptionInput.Builder deliveryOptionInputBuilder = SubscriptionDeliveryMethodLocalDeliveryOptionInput.builder();
        if(StringUtils.isNotBlank(changeShippingAddressVM.getPhone())) {
            deliveryOptionInputBuilder.phone(changeShippingAddressVM.getPhone());
        } else {
            throw new BadRequestAlertException("Phone No is required for Local Delivery", "", "");
        }

        SubscriptionDeliveryMethodLocalDeliveryOptionInput deliveryOptionInput = deliveryOptionInputBuilder.build();

        SubscriptionDeliveryMethodLocalDeliveryInput localDeliveryInput = SubscriptionDeliveryMethodLocalDeliveryInput.builder()
            .address(mailingAddressInput)
            .localDeliveryOption(deliveryOptionInput)
            .build();

        return localDeliveryInput;
    }

    private SubscriptionDeliveryMethodPickupInput buildLocalPickupInput(ChangeShippingAddressVM changeShippingAddressVM) {

        SubscriptionDeliveryMethodPickupOptionInput subscriptionDeliveryMethodPickupOptionInput = SubscriptionDeliveryMethodPickupOptionInput.builder()
            .locationId(ShopifyGraphQLUtils.getGraphQlLocationId(changeShippingAddressVM.getLocationId())).build();

        return SubscriptionDeliveryMethodPickupInput.builder().pickupOption(subscriptionDeliveryMethodPickupOptionInput).build();
    }
    @Override
    public void updateDeliveryMethodType(Long contractId, String shop, DeliveryMethodType fromDeliveryType, DeliveryMethodType toDeliveryType) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new BadRequestAlertException("Subscription contract not found", "", "");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        Optional<ShippingAddressModel> shippingAddressModelOptional = commonUtils.getContractShippingAddress(subscriptionContract);

        if (shippingAddressModelOptional.isPresent()) {
            if(!shippingAddressModelOptional.get().getShipping_type().equalsIgnoreCase(fromDeliveryType.toString())){
                throw new BadRequestAlertException("Contract current delivery method is "+shippingAddressModelOptional.get().getShipping_type()+", required "+fromDeliveryType, "", "");
            }

            ShippingAddressModel shippingAddressModel = shippingAddressModelOptional.get();

            ChangeShippingAddressVM changeShippingAddressVM = shippingAddressModelToChangeShippingAddressVM(shippingAddressModel);

            SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();

            if (toDeliveryType.equals(DeliveryMethodType.SHIPPING)) {
                deliveryMethodBuilder.shipping(buildStandardShippingInput(changeShippingAddressVM));
            } else if (toDeliveryType.equals(DeliveryMethodType.LOCAL)) {
                deliveryMethodBuilder.localDelivery(buildLocalDeliveryInput(changeShippingAddressVM));
            } else {
               throw new BadRequestAlertException("To delivery method type is invalid", "", "");
            }

            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
            subscriptionDraftInputBuilder.deliveryMethod(deliveryMethodBuilder.build());

            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

            if (!subscriptionContractUpdateResult.isSuccess()) {
                throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
            }
        } else {
            throw new BadRequestAlertException("Address not found", "", "");
        }
    }

    private ChangeShippingAddressVM shippingAddressModelToChangeShippingAddressVM(ShippingAddressModel shippingAddressModel) {
        ChangeShippingAddressVM changeShippingAddressVM = new ChangeShippingAddressVM();

        changeShippingAddressVM.setAddress1(shippingAddressModel.getShipping_address1());
        changeShippingAddressVM.setAddress2(shippingAddressModel.getShipping_address2());
        changeShippingAddressVM.setPhone(shippingAddressModel.getShipping_phone());
        changeShippingAddressVM.setCity(shippingAddressModel.getShipping_city());
        changeShippingAddressVM.setCountry(shippingAddressModel.getShipping_country());
        changeShippingAddressVM.setMethodType(shippingAddressModel.getShipping_type());
        changeShippingAddressVM.setCountryCode(shippingAddressModel.getShipping_country_code());
        changeShippingAddressVM.setFirstName(shippingAddressModel.getShipping_first_name());
        changeShippingAddressVM.setLastName(shippingAddressModel.getShipping_last_name());
        changeShippingAddressVM.setProvinceCode(shippingAddressModel.getShipping_province_code());
        changeShippingAddressVM.setLocationId(shippingAddressModel.getPick_up_location_id());
        changeShippingAddressVM.setZip(shippingAddressModel.getShipping_zip());
        changeShippingAddressVM.setCompany(shippingAddressModel.getShipping_company());

        return changeShippingAddressVM;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        return subscriptionContractsAddLineItem(contractId, shop, quantity, productVariantId, isOneTimeProduct, requestURL, null, eventSource);
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Boolean isOneTimeProduct, String requestURL, List<AttributeInfo> customAttributes, ActivityLogEventSource eventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        String gqlProductVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(productVariantId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.Node> optionalExistingNode = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getVariantId().isPresent() && j.getVariantId().get().equals(gqlProductVariantId))
            .findFirst();

        if(optionalExistingNode.isPresent() && commonUtils.newApproachFor(shop, "updateExistingQuantityOnAddProduct") && !isOneTimeProduct){
            int newQuantity = optionalExistingNode.get().getQuantity() + quantity;
            subscriptionContractUpdateLineItemQuantity(contractId, shop, newQuantity, optionalExistingNode.get().getId(), eventSource);
            return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
        }

        Optional<String> optionalContractCurrencyCode = requireNonNull(optionalSubscriptionContractQueryResponse
            .getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines).
                map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .findFirst()
            .map(SubscriptionContractQuery.Edge::getNode)
            .map(SubscriptionContractQuery.Node::getCurrentPrice)
            .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
            .map(CurrencyCode::rawValue);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalSubscriptionContractQueryResponse
            .getData()
            .flatMap(SubscriptionContractQuery.Data::getSubscriptionContract)
            .orElse(null);

        List<AttributeInput> attributeInputList = new ArrayList<>();
        if(!CollectionUtils.isNullOrEmpty(customAttributes)){
            attributeInputList = customAttributes.stream()
                .map(attributeInfo -> AttributeInput.builder().key(attributeInfo.getKey()).value(attributeInfo.getValue()).build()).collect(Collectors.toList());
        }


        if (optionalContractCurrencyCode.isPresent() && !optionalContractCurrencyCode.get().equals(shopInfoDTO.getCurrency())) {
            //optionalSubscriptionContractQueryResponse.getData().map(d -> d.getSubscriptionContract().map(e -> e.getb))
            String currencyCode = optionalContractCurrencyCode.get();
            CountryCode countryCode = CurrencyUtils.getCountryCode(currencyCode);
            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(countryCode).build();
            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(gqlProductVariantId, contextualPricingContext);
            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            Object price = requireNonNull(productVariantResponse.getData())
                .flatMap(e -> e.getProductVariant()
                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                .orElse(null);

            return subscriptionContractAddLineItem(contractId, shop, quantity, gqlProductVariantId, Double.parseDouble(price.toString()), isOneTimeProduct, requestURL, attributeInputList, eventSource);
        } else {
            ProductVariantQuery productVariantQuery = new ProductVariantQuery(gqlProductVariantId);
            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            Object price = requireNonNull(productVariantResponse.getData()).flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice)).orElse(null);

            return subscriptionContractAddLineItem(contractId, shop, quantity, gqlProductVariantId, Double.parseDouble(price.toString()), isOneTimeProduct, requestURL, attributeInputList, eventSource);
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        return subscriptionContractAddLineItem(contractId, shop, quantity, productVariantId, price, isOneTimeProduct, requestURL, null, eventSource);
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, List<AttributeInput> attributeInputList, ActivityLogEventSource eventSource) throws Exception {

        log.info("Adding line item to contract {}", contractId);

        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalSubscriptionContractQueryResponse
            .getData()
            .flatMap(SubscriptionContractQuery.Data::getSubscriptionContract)
            .orElse(null);

        Optional<SubscriptionContractQuery.Node> optionalExistingNode = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getSellingPlanId().isPresent())
            .filter(j -> !j.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts)
                .orElse(new ArrayList<>()).isEmpty())
            .findFirst();

        if (optionalExistingNode.isEmpty()) {
            optionalExistingNode = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                    .map(SubscriptionContractQuery.Lines::getEdges)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(j -> j.getSellingPlanId().isPresent())
                .findFirst();
        }


        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", requestURL, message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {

                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();

                double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

                SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput
                    .builder();

                Double currentPrice = price * fulfillmentCountMultiplier;

                if(BooleanUtils.isTrue(isOneTimeProduct)) {
                    ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                    Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
                    String gqlProductId = optionalProductVariantQueryResponse.getData().map(d -> d.getProductVariant().map(e -> e.getProduct().getId()).orElse(null)).orElse(null);

                    Long variantId = ShopifyGraphQLUtils.getVariantId(productVariantId);
                    Long productId = ShopifyGraphQLUtils.getProductId(gqlProductId);

                    SubscriptionContractQuery.BillingPolicy billingPolicy = subscriptionContract.getBillingPolicy();

                    List<FrequencyInfoDTO> matchingSellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(
                        shop,
                        productId,
                        variantId,
                        billingPolicy.getIntervalCount(),
                        billingPolicy.getInterval());

                    if (!org.springframework.util.CollectionUtils.isEmpty(matchingSellingPlans)) {
                        FrequencyInfoDTO firstMatchingSellingPlan = matchingSellingPlans.get(0);

                        subscriptionLineInputBuilder.sellingPlanId(firstMatchingSellingPlan.getId());
                        subscriptionLineInputBuilder.sellingPlanName(firstMatchingSellingPlan.getFrequencyName());
                    } else if (optionalExistingNode.isPresent()) {

                        SubscriptionContractQuery.Node node = optionalExistingNode.get();
                        subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());

                        if (node.getSellingPlanName().isPresent()) {
                            subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                        }

                    }
                }

                if(BooleanUtils.isFalse(isOneTimeProduct) || BooleanUtils.isTrue(customerPortalSettingsDTO.getApplySubscriptionDiscountForOtp())) {
                    if (BooleanUtils.isTrue(customerPortalSettingsDTO.getApplySellingPlanBasedDiscount())) {

                        log.info("Applying discount to line item based on product's selling plan");

                        ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                        Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
                        String gqlProductId = optionalProductVariantQueryResponse.getData().map(d -> d.getProductVariant().map(e -> e.getProduct().getId()).orElse(null)).orElse(null);

                        Long variantId = ShopifyGraphQLUtils.getVariantId(productVariantId);
                        Long productId = ShopifyGraphQLUtils.getProductId(gqlProductId);

                        SubscriptionContractQuery.BillingPolicy billingPolicy = subscriptionContract.getBillingPolicy();

                        List<FrequencyInfoDTO> matchingSellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(
                            shop,
                            productId,
                            variantId,
                            billingPolicy.getIntervalCount(),
                            billingPolicy.getInterval());

                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                        if (!org.springframework.util.CollectionUtils.isEmpty(matchingSellingPlans)) {

                            int totalCycles = 1;
                            List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                            totalCycles = totalCycles + subscriptionBillingAttempts.size();

                            FrequencyInfoDTO firstMatchingSellingPlan = matchingSellingPlans.get(0);

                            subscriptionLineInputBuilder.sellingPlanId(firstMatchingSellingPlan.getId());
                            subscriptionLineInputBuilder.sellingPlanName(firstMatchingSellingPlan.getFrequencyName());

                            DiscountTypeUnit discountType = null;
                            Double discountOffer = null;

                            if (firstMatchingSellingPlan.getAfterCycle2() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle2()) {
                                discountType = firstMatchingSellingPlan.getDiscountType2();
                                discountOffer = firstMatchingSellingPlan.getDiscountOffer2();
                            } else if (!firstMatchingSellingPlan.isFreeTrialEnabled() && firstMatchingSellingPlan.getAfterCycle1() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle1()) {
                                discountType = firstMatchingSellingPlan.getDiscountType();
                                discountOffer = firstMatchingSellingPlan.getDiscountOffer();
                            }

                            DecimalFormat df = new DecimalFormat("#.##");

                            if (discountOffer != null) {
                                if (DiscountTypeUnit.PERCENTAGE.equals(discountType)) {
                                    currentPrice = Double.parseDouble(df.format(Math.max(0, price * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
                                } else if (DiscountTypeUnit.FIXED.equals(discountType)) {
                                    currentPrice = Double.parseDouble(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - discountOffer)));
                                } else if (DiscountTypeUnit.PRICE.equals(discountType)) {
                                    currentPrice = Double.parseDouble(df.format(Math.max(0, (discountOffer * fulfillmentCountMultiplier))));
                                }
                            }

                            buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle1(), firstMatchingSellingPlan.getDiscountType(), firstMatchingSellingPlan.getDiscountOffer(), price, fulfillmentCountMultiplier, cycleDiscounts);

                            buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle2(), firstMatchingSellingPlan.getDiscountType2(), firstMatchingSellingPlan.getDiscountOffer2(), price, fulfillmentCountMultiplier, cycleDiscounts);
                        }

                        if (!org.springframework.util.CollectionUtils.isEmpty(cycleDiscounts)) {
                            SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                                .basePrice(price)
                                .cycleDiscounts(cycleDiscounts)
                                .build();
                            subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
                        }

                    } else if (optionalExistingNode.isPresent()) {

                        log.info("Applying discount to line item based on existing product's selling plan");

                        SubscriptionContractQuery.Node node = optionalExistingNode.get();
                        subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());

                        if (node.getSellingPlanName().isPresent()) {
                            subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                        }

                        List<SubscriptionContractQuery.CycleDiscount> existingCycleDiscounts = node.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>());

                        int totalCycles = 1;
                        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                        totalCycles = totalCycles + subscriptionBillingAttempts.size();

                        ArrayList<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                        DecimalFormat df = new DecimalFormat("#.##");

                        for (SubscriptionContractQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {

                            if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) {
                                SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();
                                SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput =
                                    SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                        .afterCycle(existingCycleDiscount.getAfterCycle())
                                        .adjustmentType(existingCycleDiscount.getAdjustmentType())
                                        .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build())
                                        .computedPrice(df.format(Math.max(0, price * ((100 - adjustmentValue.getPercentage()) / 100) * fulfillmentCountMultiplier)))
                                        .build();
                                cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                                if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                                    currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                                }
                            } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                                && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT) {

                                SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                                SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                    .afterCycle(existingCycleDiscount.getAfterCycle())
                                    .adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT)
                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                                    .computedPrice(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - Double.parseDouble(adjustmentValue.getAmount().toString()))))
                                    .build();

                                cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                                if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                                    currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                                }
                            } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                                && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE) {

                                SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                                SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                    .afterCycle(existingCycleDiscount.getAfterCycle())
                                    .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE)
                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                                    .computedPrice(df.format(Math.max(0, (Double.parseDouble(adjustmentValue.getAmount().toString()) * fulfillmentCountMultiplier))))
                                    .build();

                                cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                                if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                                    currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                                }
                            }
                        }

                        if (!CollectionUtils.isNullOrEmpty(cycleDiscounts)) {
                            SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                                .basePrice(price)
                                .cycleDiscounts(cycleDiscounts)
                                .build();
                            subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
                        }
                    }
                }

                if("bandbox-rocks.myshopify.com".equalsIgnoreCase(shop) && BooleanUtils.isTrue(isOneTimeProduct)) {
                    ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                    Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
                    String gqlProductId = optionalProductVariantQueryResponse.getData().map(d -> d.getProductVariant().map(e -> e.getProduct().getId()).orElse(null)).orElse(null);

                    Long variantId = ShopifyGraphQLUtils.getVariantId(productVariantId);
                    Long productId = ShopifyGraphQLUtils.getProductId(gqlProductId);

                    SubscriptionContractQuery.BillingPolicy billingPolicy = subscriptionContract.getBillingPolicy();

                    List<FrequencyInfoDTO> matchingSellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(
                        shop,
                        productId,
                        variantId,
                        billingPolicy.getIntervalCount(),
                        billingPolicy.getInterval());

                    if (org.springframework.util.CollectionUtils.isEmpty(matchingSellingPlans)) {
                        matchingSellingPlans = subscriptionGroupService.getAllSellingPlans(shop);
                    }

                    List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                    if (!org.springframework.util.CollectionUtils.isEmpty(matchingSellingPlans)) {

                        int totalCycles = 1;
                        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                        totalCycles = totalCycles + subscriptionBillingAttempts.size();

                        FrequencyInfoDTO firstMatchingSellingPlan = matchingSellingPlans.get(0);

                        subscriptionLineInputBuilder.sellingPlanId(firstMatchingSellingPlan.getId());
                        subscriptionLineInputBuilder.sellingPlanName(firstMatchingSellingPlan.getFrequencyName());

                        DiscountTypeUnit discountType = null;
                        Double discountOffer = null;

                        if (firstMatchingSellingPlan.getAfterCycle2() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle2()) {
                            discountType = firstMatchingSellingPlan.getDiscountType2();
                            discountOffer = firstMatchingSellingPlan.getDiscountOffer2();
                        } else if (!firstMatchingSellingPlan.isFreeTrialEnabled() && firstMatchingSellingPlan.getAfterCycle1() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle1()) {
                            discountType = firstMatchingSellingPlan.getDiscountType();
                            discountOffer = firstMatchingSellingPlan.getDiscountOffer();
                        }

                        DecimalFormat df = new DecimalFormat("#.##");

                        if (discountOffer != null) {
                            if (DiscountTypeUnit.PERCENTAGE.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, price * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
                            } else if (DiscountTypeUnit.FIXED.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - discountOffer)));
                            } else if (DiscountTypeUnit.PRICE.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, (discountOffer * fulfillmentCountMultiplier))));
                            }
                        }

                        buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle1(), firstMatchingSellingPlan.getDiscountType(), firstMatchingSellingPlan.getDiscountOffer(), price, fulfillmentCountMultiplier, cycleDiscounts);

                        buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle2(), firstMatchingSellingPlan.getDiscountType2(), firstMatchingSellingPlan.getDiscountOffer2(), price, fulfillmentCountMultiplier, cycleDiscounts);
                    }

                    if (!org.springframework.util.CollectionUtils.isEmpty(cycleDiscounts)) {
                        SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                            .basePrice(price)
                            .cycleDiscounts(cycleDiscounts)
                            .build();
                        subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
                    }
                }

                if (isOneTimeProduct) {
                    attributeInputList = Optional.ofNullable(attributeInputList).orElse(new ArrayList<>());
                    if(!ObjectUtils.notEqual(price, 0D)) {
                        attributeInputList.add(AttributeInput.builder().key(AppstleAttribute.FREE_PRODUCT.getKey()).value("true").build());
                    }
                    attributeInputList.add(AttributeInput.builder().key(AppstleAttribute.ONE_TIME_PRODUCT.getKey()).value("true").build());
                }

                if (!CollectionUtils.isNullOrEmpty(attributeInputList)) {
                    subscriptionLineInputBuilder.customAttributes(attributeInputList);
                }

                SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder
                    .currentPrice(currentPrice)
                    .productVariantId(productVariantId)
                    .quantity(quantity)
                    .build();

                String draftId = optionalDraftId.get();

                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
                Response<Optional<SubscriptionDraftLineAddMutation.Data>> draftLineAddMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                if (draftLineAddMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftLineAddMutation.UserError> draftLineAddMutationResponseUserErrors =
                    requireNonNull(draftLineAddMutationResponse.getData())
                        .map(d -> d.getSubscriptionDraftLineAdd()
                            .map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>());

                if (draftLineAddMutationResponseUserErrors.size() > 0) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                String lineId =
                    draftLineAddMutationResponse
                        .getData()
                        .flatMap(d -> d.getSubscriptionDraftLineAdd()
                            .flatMap(e -> e.getLineAdded()
                                .map(SubscriptionDraftLineAddMutation.LineAdded::getId)))
                        .orElse(null);

                boolean applySubscriptionDiscount = BooleanUtils.isTrue(customerPortalSettingsDTO.isApplySubscriptionDiscount());
                if (applySubscriptionDiscount && lineId != null &&
                    (BooleanUtils.isFalse(isOneTimeProduct) || BooleanUtils.isTrue(customerPortalSettingsDTO.getApplySubscriptionDiscountForOtp()))) {

                    SubscriptionDiscountTypeUnit discountType = Optional.ofNullable(customerPortalSettingsDTO.getSubscriptionDiscountTypeUnit()).orElse(SubscriptionDiscountTypeUnit.PERCENTAGE);
                    Integer percentage = customerPortalSettingsDTO.getSubscriptionDiscount().intValue();
                    Double amount = customerPortalSettingsDTO.getSubscriptionDiscount();

                    String discountTitle = "PRODUCT_DISCOUNT";
                    Boolean appliesOnEachItem = false;
                    Integer recurringCycleLimit = Integer.MAX_VALUE;

                    List<String> lineItems = new ArrayList<>();
                    lineItems.add(lineId);

                    applyDiscount(
                        contractId,
                        percentage,
                        discountTitle,
                        recurringCycleLimit,
                        appliesOnEachItem,
                        amount,
                        discountType.toString(),
                        shopifyGraphqlClient,
                        draftId,
                        lineItems,
                        null);
                }


                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (draftCommitResponseUserErrors.size() > 0) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                resyncBuildABoxDiscount(shop, contractId);

                Map<String, Object> map = new HashMap<>();
                map.put("variantId", productVariantId);
                map.put("price", price);
                map.put("quantity", quantity);
                commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, isOneTimeProduct ? ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_ADDED : ActivityLogEventType.PRODUCT_ADD, ActivityLogStatus.SUCCESS, map);

                List<String> productsAdded = new ArrayList<>();

                String newProductVariantName = getProductVariantNameById(shop, productVariantId);

                if (StringUtils.isNotBlank(newProductVariantName)) {
                    productsAdded.add(newProductVariantName);
                }

                Map<String, Object> additionalEmailAttributes = new HashMap<>();
                additionalEmailAttributes.put("productsAdded", productsAdded);
                sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED, additionalEmailAttributes);

                commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);
            }
        }
        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    private String getProductVariantNameById(String shop, String productVariantId) {
        String productName = "";
        try{
            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
            Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            productName = requireNonNull(optionalProductVariantQueryResponse.getData())
                .flatMap(ProductVariantQuery.Data::getProductVariant)
                .map(ProductVariantQuery.ProductVariant::getProduct)
                .map(ProductVariantQuery.Product::getTitle).orElse("");

            String newVariantName = requireNonNull(optionalProductVariantQueryResponse.getData())
                .flatMap(ProductVariantQuery.Data::getProductVariant)
                .filter(variant -> !variant.getTitle().equalsIgnoreCase("Default Title") && !variant.getTitle().equals("-"))
                .map(ProductVariantQuery.ProductVariant::getTitle).orElse("");

            String productSku = requireNonNull(optionalProductVariantQueryResponse.getData())
                .flatMap(ProductVariantQuery.Data::getProductVariant)
                .flatMap(ProductVariantQuery.ProductVariant::getSku).orElse("");

            productName = productName + (StringUtils.isNotBlank(newVariantName) ? " -" + newVariantName : "") + (StringUtils.isNotBlank(productSku) ? "  |" + " SKU: " + productSku : "");

        }catch (Exception e){
            log.error("Error fetching added product variant name. error={}", e.getMessage());
        }
        return productName;
    }

    private void subscriptionContractDraftAddLineItem(String shop, Integer quantity, String productVariantId, Double price, SubscriptionContractQuery.SubscriptionContract subscriptionContract, ShopifyGraphqlClient shopifyGraphqlClient, String draftId, List<AttributeInput> attributeInputList, Boolean carryForwardDiscount, String oldNodeId, List<Map<String, Object>> activityLogAttributes) throws Exception {

        Long contractId = ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId());

        Optional<SubscriptionContractQuery.Node> optionalExistingNode = subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getSellingPlanId().isPresent())
            .filter(j -> !j.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts)
                .orElse(new ArrayList<>()).isEmpty())
            .findFirst();

        if (optionalExistingNode.isEmpty()) {
            optionalExistingNode = subscriptionContract.getLines().getEdges().stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(j -> j.getSellingPlanId().isPresent())
                .findFirst();
        }

        Optional<SubscriptionContractQuery.Node> optionalOldNode = subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(j -> j.getId().equalsIgnoreCase(oldNodeId))
            .findFirst();

        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();

        ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
        Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
        String gqlProductId = optionalProductVariantQueryResponse.getData().map(d -> d.getProductVariant().map(e -> e.getProduct().getId()).orElse(null)).orElse(null);

        Long variantId = ShopifyGraphQLUtils.getVariantId(productVariantId);
        Long productId = ShopifyGraphQLUtils.getProductId(gqlProductId);

        SubscriptionContractQuery.BillingPolicy billingPolicy = subscriptionContract.getBillingPolicy();

        List<FrequencyInfoDTO> matchingSellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(
            shop,
            productId,
            variantId,
            billingPolicy.getIntervalCount(),
            billingPolicy.getInterval());

        FrequencyInfoDTO firstMatchingSellingPlan = null;

        if (!org.springframework.util.CollectionUtils.isEmpty(matchingSellingPlans)) {
            firstMatchingSellingPlan = matchingSellingPlans.get(0);
        }

        SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput
            .builder();

        if(BooleanUtils.isTrue(customerPortalSettingsDTO.getApplySellingPlanBasedDiscount())) {
            // Set selling plan
            if(firstMatchingSellingPlan != null) {
                subscriptionLineInputBuilder.sellingPlanId(firstMatchingSellingPlan.getId());
                subscriptionLineInputBuilder.sellingPlanName(firstMatchingSellingPlan.getFrequencyName());
            } else if(optionalOldNode.isPresent()) {
                SubscriptionContractQuery.Node node = optionalOldNode.get();
                if (node.getSellingPlanId().isPresent()) {
                    subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());
                }
                if (node.getSellingPlanName().isPresent()) {
                    subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                }
            } else if (optionalExistingNode.isPresent()) {
                SubscriptionContractQuery.Node node = optionalExistingNode.get();
                if (node.getSellingPlanId().isPresent()) {
                    subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());
                }
                if (node.getSellingPlanName().isPresent()) {
                    subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                }
            }
        } else {
             String sellingPlanId = null;
             if(optionalOldNode.isPresent()) {
                SubscriptionContractQuery.Node node = optionalOldNode.get();
                if (node.getSellingPlanId().isPresent()) {
                    sellingPlanId = node.getSellingPlanId().get();
                    subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());
                }
                if (node.getSellingPlanName().isPresent()) {
                    subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                }
             }

             if(sellingPlanId == null) {
                 if (firstMatchingSellingPlan != null) {
                     subscriptionLineInputBuilder.sellingPlanId(firstMatchingSellingPlan.getId());
                     subscriptionLineInputBuilder.sellingPlanName(firstMatchingSellingPlan.getFrequencyName());
                 } else if (optionalExistingNode.isPresent()) {
                     SubscriptionContractQuery.Node node = optionalExistingNode.get();
                     if (node.getSellingPlanId().isPresent()) {
                         subscriptionLineInputBuilder.sellingPlanId(node.getSellingPlanId().get());
                     }
                     if (node.getSellingPlanName().isPresent()) {
                         subscriptionLineInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                     }
                 }
             }
        }



        double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

        Double currentPrice = price * fulfillmentCountMultiplier;

        SubscriptionPricingPolicyInput pricingPolicyInput = null;

        // Set Pricing Policy
        ArrayList<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();
        if (BooleanUtils.isTrue(carryForwardDiscount) && optionalOldNode.isPresent()) {

            SubscriptionContractQuery.Node node = optionalOldNode.get();

            List<SubscriptionContractQuery.CycleDiscount> existingCycleDiscounts = node.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>());

            int totalCycles = 1;
            List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
            totalCycles = totalCycles + subscriptionBillingAttempts.size();

            DecimalFormat df = new DecimalFormat("#.##");

            for (SubscriptionContractQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {

                if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) {
                    SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();
                    SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput =
                        SubscriptionPricingPolicyCycleDiscountsInput.builder()
                            .afterCycle(existingCycleDiscount.getAfterCycle())
                            .adjustmentType(existingCycleDiscount.getAdjustmentType())
                            .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build())
                            .computedPrice(df.format(Math.max(0, price * ((100 - adjustmentValue.getPercentage()) / 100) * fulfillmentCountMultiplier)))
                            .build();
                    cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                    if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                        currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                    }
                } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                    && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT) {
                    SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                    SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                        .afterCycle(existingCycleDiscount.getAfterCycle())
                        .adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT)
                        .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                        .computedPrice(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - Double.parseDouble(adjustmentValue.getAmount().toString()))))
                        .build();

                    cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                    if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                        currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                    }
                } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                    && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE) {
                    SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                    SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                        .afterCycle(existingCycleDiscount.getAfterCycle())
                        .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE)
                        .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                        .computedPrice(df.format(Math.max(0, (Double.parseDouble(adjustmentValue.getAmount().toString()) * fulfillmentCountMultiplier))))
                        .build();

                    cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                    if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                        currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                    }
                }
            }

            if (!CollectionUtils.isNullOrEmpty(cycleDiscounts)) {
                pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                    .basePrice(price)
                    .cycleDiscounts(cycleDiscounts)
                    .build();
                subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
            }

        }

        if (CollectionUtils.isNullOrEmpty(cycleDiscounts) && BooleanUtils.isTrue(customerPortalSettingsDTO.getApplySellingPlanBasedDiscount())) {

            if (firstMatchingSellingPlan != null) {

                int totalCycles = 1;
                List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                totalCycles = totalCycles + subscriptionBillingAttempts.size();

                DiscountTypeUnit discountType = null;
                Double discountOffer = null;

                if (firstMatchingSellingPlan.getAfterCycle2() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle2()) {
                    discountType = firstMatchingSellingPlan.getDiscountType2();
                    discountOffer = firstMatchingSellingPlan.getDiscountOffer2();
                } else if (!firstMatchingSellingPlan.isFreeTrialEnabled() && firstMatchingSellingPlan.getAfterCycle1() != null && totalCycles >= firstMatchingSellingPlan.getAfterCycle1()) {
                    discountType = firstMatchingSellingPlan.getDiscountType();
                    discountOffer = firstMatchingSellingPlan.getDiscountOffer();
                }

                DecimalFormat df = new DecimalFormat("#.##");

                if (discountOffer != null) {
                    if (DiscountTypeUnit.PERCENTAGE.equals(discountType)) {
                        currentPrice = Double.parseDouble(df.format(Math.max(0, price * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
                    } else if (DiscountTypeUnit.FIXED.equals(discountType)) {
                        currentPrice = Double.parseDouble(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - discountOffer)));
                    } else if (DiscountTypeUnit.PRICE.equals(discountType)) {
                        currentPrice = Double.parseDouble(df.format(Math.max(0, (discountOffer * fulfillmentCountMultiplier))));
                    }
                }

                buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle1(), firstMatchingSellingPlan.getDiscountType(), firstMatchingSellingPlan.getDiscountOffer(), price, fulfillmentCountMultiplier, cycleDiscounts);

                buildCycleDiscount(firstMatchingSellingPlan.getAfterCycle2(), firstMatchingSellingPlan.getDiscountType2(), firstMatchingSellingPlan.getDiscountOffer2(), price, fulfillmentCountMultiplier, cycleDiscounts);
            }

            if (!org.springframework.util.CollectionUtils.isEmpty(cycleDiscounts)) {
                pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                    .basePrice(price)
                    .cycleDiscounts(cycleDiscounts)
                    .build();
                subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
            }
        }

        if (CollectionUtils.isNullOrEmpty(cycleDiscounts) && optionalExistingNode.isPresent()) {
            SubscriptionContractQuery.Node node = optionalExistingNode.get();

            if(BooleanUtils.isTrue(carryForwardDiscount)) {
                List<SubscriptionContractQuery.CycleDiscount> existingCycleDiscounts = node.getPricingPolicy().map(SubscriptionContractQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>());

                int totalCycles = 1;
                List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                totalCycles = totalCycles + subscriptionBillingAttempts.size();

                DecimalFormat df = new DecimalFormat("#.##");

                for (SubscriptionContractQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {

                    if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) {
                        SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();
                        SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput =
                            SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                .afterCycle(existingCycleDiscount.getAfterCycle())
                                .adjustmentType(existingCycleDiscount.getAdjustmentType())
                                .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build())
                                .computedPrice(df.format(Math.max(0, price * ((100 - adjustmentValue.getPercentage()) / 100) * fulfillmentCountMultiplier)))
                                .build();
                        cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                        if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                            currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                        }
                    } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                        && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT) {
                        SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                        SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                            .afterCycle(existingCycleDiscount.getAfterCycle())
                            .adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT)
                            .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                            .computedPrice(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - Double.parseDouble(adjustmentValue.getAmount().toString()))))
                            .build();

                        cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                        if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                            currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                        }
                    } else if (existingCycleDiscount.getAdjustmentValue() instanceof SubscriptionContractQuery.AsMoneyV2
                        && existingCycleDiscount.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE) {
                        SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                        SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder()
                            .afterCycle(existingCycleDiscount.getAfterCycle())
                            .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE)
                            .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build())
                            .computedPrice(df.format(Math.max(0, (Double.parseDouble(adjustmentValue.getAmount().toString()) * fulfillmentCountMultiplier))))
                            .build();

                        cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                        if (totalCycles >= existingCycleDiscount.getAfterCycle()) {
                            currentPrice = Double.parseDouble(subscriptionPricingPolicyCycleDiscountsInput.computedPrice().toString());
                        }
                    }
                }

                if (!CollectionUtils.isNullOrEmpty(cycleDiscounts)) {
                    pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                        .basePrice(price)
                        .cycleDiscounts(cycleDiscounts)
                        .build();
                    subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
                }
            }
        }

        if (!CollectionUtils.isNullOrEmpty(attributeInputList)) {
            subscriptionLineInputBuilder.customAttributes(attributeInputList);
        }

        SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder
            .currentPrice(currentPrice)
            .productVariantId(productVariantId)
            .quantity(quantity)
            .build();

        SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
        Response<Optional<SubscriptionDraftLineAddMutation.Data>> draftLineAddMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

        if (draftLineAddMutationResponse.hasErrors()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<SubscriptionDraftLineAddMutation.UserError> draftLineAddMutationResponseUserErrors =
            requireNonNull(draftLineAddMutationResponse.getData())
                .map(d -> d.getSubscriptionDraftLineAdd()
                    .map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

        if (draftLineAddMutationResponseUserErrors.size() > 0) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        if(ObjectUtils.isNotEmpty(pricingPolicyInput)) {

            List<AppstleCycle> appstleCycleList = new ArrayList<>();
            for(SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountsInput : pricingPolicyInput.cycleDiscounts()){
                AppstleCycle appstleCycle = new AppstleCycle();
                switch (cycleDiscountsInput.adjustmentType()){
                    case PRICE:
                        appstleCycle.setDiscountType(DiscountType.PRICE);
                        if(Objects.nonNull(cycleDiscountsInput.adjustmentValue().fixedValue())) {
                            appstleCycle.setValue(Double.parseDouble(cycleDiscountsInput.adjustmentValue().fixedValue().toString()));
                        }
                        break;
                    case PERCENTAGE:
                        appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                        if(Objects.nonNull(cycleDiscountsInput.adjustmentValue().percentage())) {
                            appstleCycle.setValue(cycleDiscountsInput.adjustmentValue().percentage());
                        }
                        break;
                    case FIXED_AMOUNT:
                        appstleCycle.setDiscountType(DiscountType.FIXED);
                        if(Objects.nonNull(cycleDiscountsInput.adjustmentValue().fixedValue())) {
                            appstleCycle.setValue(Double.parseDouble(cycleDiscountsInput.adjustmentValue().fixedValue().toString()));
                        }
                        break;
                }
                appstleCycle.setAfterCycle(cycleDiscountsInput.afterCycle());
                appstleCycleList.add(appstleCycle);
            }

            Map<String, Object> addedProductAttributes = new HashMap<>();

            addedProductAttributes.put("variantId", productVariantId);
            addedProductAttributes.put("price", price);
            addedProductAttributes.put("quantity", quantity);
            addedProductAttributes.put("pricingPolicy", OBJECT_MAPPER.writeValueAsString(appstleCycleList));
            activityLogAttributes.add(addedProductAttributes);
        }

        String lineId =
            draftLineAddMutationResponse
                .getData()
                .flatMap(d -> d.getSubscriptionDraftLineAdd()
                    .flatMap(e -> e.getLineAdded()
                        .map(SubscriptionDraftLineAddMutation.LineAdded::getId)))
                .orElse(null);

        boolean applySubscriptionDiscount = BooleanUtils.isTrue(customerPortalSettingsDTO.isApplySubscriptionDiscount());
        if (applySubscriptionDiscount && lineId != null) {

            SubscriptionDiscountTypeUnit discountType = Optional.ofNullable(customerPortalSettingsDTO.getSubscriptionDiscountTypeUnit()).orElse(SubscriptionDiscountTypeUnit.PERCENTAGE);
            Integer percentage = customerPortalSettingsDTO.getSubscriptionDiscount().intValue();
            Double amount = customerPortalSettingsDTO.getSubscriptionDiscount();

            String discountTitle = "PRODUCT_DISCOUNT";
            Boolean appliesOnEachItem = false;
            Integer recurringCycleLimit = Integer.MAX_VALUE;

            List<String> lineItems = new ArrayList<>();
            lineItems.add(lineId);

            applyDiscount(
                contractId,
                percentage,
                discountTitle,
                recurringCycleLimit,
                appliesOnEachItem,
                amount,
                discountType.toString(),
                shopifyGraphqlClient,
                draftId,
                lineItems,
                null);
        }
    }

    @Override
    public void resyncBuildABoxDiscount(String shop, Long contractId) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", "", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            List<SubscriptionContractQuery.Node> existingLineItems =
                requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

            List<SubscriptionContractQuery.Node2> appliedDiscounts = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                .map(data -> data.getSubscriptionContract().stream()
                    .map(SubscriptionContractQuery.SubscriptionContract::getDiscounts)
                    .map(discounts -> discounts.getEdges()
                        .stream().map(SubscriptionContractQuery.Edge2::getNode).collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
                ).stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

            String buildABoxUniqueRef = "";

            Optional<SubscriptionContractDetailsDTO> optionalSubscriptionContractDetailsDTO = findByContractId(contractId);

            Optional<SubscriptionContractQuery.Node2> build_a_box_discount = appliedDiscounts.stream().filter(node2 -> node2.getTitle().get().contains("BUILD_A_BOX_DISCOUNT")).findFirst();
            if (appliedDiscounts.size() > 0 && build_a_box_discount.isPresent()) {
                List<String> stringList = Arrays.stream(build_a_box_discount.get().getTitle().get().split("_")).collect(Collectors.toList());
                buildABoxUniqueRef = stringList.get(stringList.size() - 1);
            } else if (optionalSubscriptionContractDetailsDTO.isPresent()) {
                if(StringUtils.isNotEmpty(optionalSubscriptionContractDetailsDTO.get().getSubscriptionTypeIdentifier())) {
                    buildABoxUniqueRef = optionalSubscriptionContractDetailsDTO.get().getSubscriptionTypeIdentifier();
                } else {
                    return;
                }
            } else {
                return;
            }
            int totalQuantity = existingLineItems.stream().map(SubscriptionContractQuery.Node::getQuantity).mapToInt(Integer::intValue).sum();
            double totalAmount = existingLineItems.stream().map(node -> Double.parseDouble(node.getCurrentPrice().getAmount().toString()) * node.getQuantity()).mapToDouble(Double::doubleValue).sum();

            SubscriptionBundlingResponse subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetails(shop, buildABoxUniqueRef);
            DiscountCodeResponse discountCodeResponse = generateDiscountCode(shop, subscriptionBundlingResponse, totalQuantity, totalAmount, existingLineItems, build_a_box_discount);

            List<SubscriptionContractQuery.Node2> build_a_box_discount_list = appliedDiscounts.stream().filter(node2 -> node2.getTitle().get().contains("BUILD_A_BOX_DISCOUNT")).collect(Collectors.toList());
            if (discountCodeResponse.getDiscountCode() != null) {

                if (build_a_box_discount_list.size() > 0) {
                    //Removing Existing build a box discount because previous discount is not matched with current applicable discount
                    for (SubscriptionContractQuery.Node2 item : build_a_box_discount_list) {
                        SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(optionalDraftId.get(), item.getId());
                        shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                    }
                }

                //Applying current applicable discount
                SubscriptionDraftDiscountCodeApplyMutation subscriptionDraftDiscountCodeApplyMutation = new SubscriptionDraftDiscountCodeApplyMutation(optionalDraftId.get(), discountCodeResponse.getDiscountCode());
                shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountCodeApplyMutation);


                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (draftCommitResponseUserErrors.size() > 0) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }
            } else if (BooleanUtils.isFalse(discountCodeResponse.getDiscountNeeded())) {
                if (build_a_box_discount_list.size() > 0) {
                    //Removing Existing build a box discount because previous discount isn't applicable anymore
                    for (SubscriptionContractQuery.Node2 item : build_a_box_discount_list) {
                        SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(optionalDraftId.get(), item.getId());
                        shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                    if (draftCommitResponseUserErrors.size() > 0) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                    }
                }
            }
        }
    }

    private SubscriptionPricingPolicyCycleDiscountsInput buildCycleDiscountInput(Double basePrice, Integer afterCycle, DiscountTypeUnit discountTypeUnit, Double discountOffer, double fulfillmentCountMultiplier) {
        if (afterCycle == null) {
            return null;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        SubscriptionPricingPolicyCycleDiscountsInput.Builder subscriptionPricingPolicyCycleDiscountsInputBuilder =
            SubscriptionPricingPolicyCycleDiscountsInput.builder();
        subscriptionPricingPolicyCycleDiscountsInputBuilder.afterCycle(afterCycle);

        if (discountTypeUnit == DiscountTypeUnit.PERCENTAGE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, basePrice * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
        } else if (discountTypeUnit == DiscountTypeUnit.FIXED) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, (basePrice * fulfillmentCountMultiplier) - discountOffer)));
        } if (discountTypeUnit == DiscountTypeUnit.PRICE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, discountOffer * fulfillmentCountMultiplier)));
        }

        return subscriptionPricingPolicyCycleDiscountsInputBuilder.build();
    }

    private void buildCycleDiscount(Integer afterCycle, DiscountTypeUnit discountTypeUnit, Double discountOffer, Double price, double fulfillmentCountMultiplier, List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts) {
        if (afterCycle == null || discountTypeUnit == null || discountOffer == null) {
            return;
        }
        cycleDiscounts.add(buildCycleDiscountInput(price, afterCycle, discountTypeUnit, discountOffer, fulfillmentCountMultiplier));
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItemWithNoDiscount(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", requestURL, message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {

                SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput
                    .builder();

                if(isOneTimeProduct) {
                    List<AttributeInput> customAttributes = List.of(AttributeInput.builder().key(AppstleAttribute.ONE_TIME_PRODUCT.getKey()).value("true").build());
                    subscriptionLineInputBuilder.customAttributes(customAttributes);
                }

                Double currentPrice = price;

                SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder
                    .currentPrice(currentPrice)
                    .productVariantId(productVariantId)
                    .quantity(quantity)
                    .build();

                String draftId = optionalDraftId.get();

                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
                Response<Optional<SubscriptionDraftLineAddMutation.Data>> draftLineAddMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                if (draftLineAddMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftLineAddMutation.UserError> draftLineAddMutationResponseUserErrors =
                    requireNonNull(draftLineAddMutationResponse.getData())
                        .map(d -> d.getSubscriptionDraftLineAdd()
                            .map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>());

                if (draftLineAddMutationResponseUserErrors.size() > 0) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftLineAddMutationResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                String lineId =
                    draftLineAddMutationResponse
                        .getData()
                        .flatMap(d -> d.getSubscriptionDraftLineAdd()
                            .flatMap(e -> e.getLineAdded()
                                .map(SubscriptionDraftLineAddMutation.LineAdded::getId)))
                        .orElse(null);

                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
                boolean applySubscriptionDiscount = BooleanUtils.isTrue(customerPortalSettingsDTO.isApplySubscriptionDiscount());
                if (applySubscriptionDiscount && lineId != null) {

                    SubscriptionDiscountTypeUnit discountType = Optional.ofNullable(customerPortalSettingsDTO.getSubscriptionDiscountTypeUnit()).orElse(SubscriptionDiscountTypeUnit.PERCENTAGE);
                    Integer percentage = customerPortalSettingsDTO.getSubscriptionDiscount().intValue();
                    Double amount = customerPortalSettingsDTO.getSubscriptionDiscount();

                    String discountTitle = "PRODUCT_DISCOUNT";
                    Boolean appliesOnEachItem = false;
                    Integer recurringCycleLimit = Integer.MAX_VALUE;

                    List<String> lineItems = new ArrayList<>();
                    lineItems.add(lineId);

                    applyDiscount(
                        contractId,
                        percentage,
                        discountTitle,
                        recurringCycleLimit,
                        appliesOnEachItem,
                        amount,
                        discountType.toString(),
                        shopifyGraphqlClient,
                        draftId,
                        lineItems,
                        null);
                }


                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (draftCommitResponseUserErrors.size() > 0) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                Map<String, Object> map = new HashMap<>();
                map.put("variantId", productVariantId);
                map.put("price", price);
                map.put("quantity", quantity);
                commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, isOneTimeProduct ? ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_ADDED : ActivityLogEventType.PRODUCT_ADD, ActivityLogStatus.SUCCESS, map);

                List<String> productsAdded = new ArrayList<>();

                String newProductVariantName = getProductVariantNameById(shop, productVariantId);

                if(StringUtils.isNotBlank(newProductVariantName)){
                    productsAdded.add(newProductVariantName);
                }

                Map<String, Object> additionalEmailAttributes = new HashMap<>();
                additionalEmailAttributes.put("productsAdded", productsAdded);
                sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED, additionalEmailAttributes);
            }

        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItemWithRetry(Long contractId, String shop, String lineId, String requestURL, Boolean removeDiscount, ActivityLogEventSource eventSource) throws Exception {
        try {
            return subscriptionContractsRemoveLineItem(contractId, shop, lineId, requestURL, removeDiscount, eventSource);
        }catch(Exception e){
            if(e.getMessage().contains("The subscription contract has changed. Retry the operation.")){
                return subscriptionContractsRemoveLineItem(contractId, shop, lineId, requestURL, removeDiscount, eventSource);
            }else if((e.getMessage().contains("Contract draft discount with title") && e.getMessage().contains("is invalid")) ||
                    (e.getMessage().contains("Contract draft has invalid discount code:") && e.getMessage().contains("applied"))){
                shopifyGraphqlSubscriptionContractService.removeDiscountCodeBasedOnError(shop, contractId, commonUtils.prepareShopifyGraphqlClient(shop), e.getMessage());
                return subscriptionContractsRemoveLineItem(contractId, shop, lineId, requestURL, removeDiscount, eventSource);
            }else{
                throw e;
            }
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItem(Long contractId, String shop, String lineId, String requestURL, Boolean removeDiscount, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

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


        //trying to find out selected line item
        Optional<SubscriptionContractQuery.Node> existingLineItemNodeOptional =
            requireNonNull(subscriptionContractQueryResponse.getData())
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                    .map(SubscriptionContractQuery.Lines::getEdges)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>())
                .stream().map(SubscriptionContractQuery.Edge::getNode)
                .filter(g -> g.getId().equals(lineId))
                .findFirst();

        if (existingLineItemNodeOptional.isEmpty()) {
            throw new BadRequestAlertException("Couldn't find LineId=" + lineId, "", "");
        }

        //getting line items in array with no one time products included
        List<SubscriptionContractQuery.Node> existingLineItemsWithNoOneTimeProduct =
            existingLineItems.stream()
                .filter(g -> g.getCustomAttributes().stream().noneMatch(customAttribute -> customAttribute.getKey().equalsIgnoreCase(AppstleAttribute.ONE_TIME_PRODUCT.getKey()) || customAttribute.getKey().equalsIgnoreCase(AppstleAttribute.FREE_PRODUCT.getKey())))
                .collect(Collectors.toList());

        boolean isOneTimeProduct = commonUtils.isAttributePresent(existingLineItemNodeOptional.get().getCustomAttributes(), AppstleAttribute.ONE_TIME_PRODUCT.getKey());

        boolean isFreeProduct = commonUtils.isAttributePresent(existingLineItemNodeOptional.get().getCustomAttributes(), AppstleAttribute.FREE_PRODUCT.getKey());

        if(!isOneTimeProduct && existingLineItemsWithNoOneTimeProduct.size() < 2 && !isFreeProduct) {
            throw new BadRequestAlertException("Cannot remove line item. Atleast one subscription product must be present in a subscription", "", "");
        }

        SubscriptionContractQuery.Node existingLineNode = existingLineItemNodeOptional.get();

        String lineMinCyclesValue = commonUtils.getAttributeValue(existingLineNode.getCustomAttributes(), AppstleAttribute.MIN_CYCLES.getKey());

        if (StringUtils.isNotBlank(lineMinCyclesValue)) {
            Integer lineMinCycles = null;
            try {
                lineMinCycles = Integer.parseInt(lineMinCyclesValue.trim());
            } catch (NumberFormatException nfe) {
                log.info("Error in parsing line min cycle attribute value. contractId={}, lineMinCyclesValue={}, Exception={}", contractId, lineMinCyclesValue, ExceptionUtils.getStackTrace(nfe));
            }

            if (lineMinCycles != null) {
                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
                List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                if ((successAttempts.size() + 1) < lineMinCycles) {
                    throw new BadRequestAlertException(customerPortalSettingsDTO.getFreezeUpdateSubscriptionMessage().replace("{{minCycles}}", lineMinCycles.toString()), "", "minCycleNotCompleteForUpdateSubscriptionContractDate");
                }
            }
        }

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", requestURL, message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {

                String draftId = optionalDraftId.get();

                Map<String, Set<String>> lineIdsByDiscountId = new HashMap<>();

                for (SubscriptionContractQuery.Node existingLineItem : existingLineItems) {
                    for (SubscriptionContractQuery.DiscountAllocation discountAllocation : existingLineItem.getDiscountAllocations()) {
                        if (discountAllocation.getDiscount() instanceof SubscriptionContractQuery.AsSubscriptionManualDiscount) {
                            String discountId = ((SubscriptionContractQuery.AsSubscriptionManualDiscount) discountAllocation.getDiscount()).getId();
                            Set<String> lineIds = lineIdsByDiscountId.getOrDefault(discountId, new HashSet<>());
                            lineIds.add(existingLineItem.getId());
                            lineIdsByDiscountId.put(discountId, lineIds);
                        }
                    }
                }

                for (SubscriptionContractQuery.DiscountAllocation discountAllocation : existingLineNode.getDiscountAllocations()) {
                    if (discountAllocation.getDiscount() instanceof SubscriptionContractQuery.AsSubscriptionManualDiscount) {
                        String discountId = ((SubscriptionContractQuery.AsSubscriptionManualDiscount) discountAllocation.getDiscount()).getId();

                        if (lineIdsByDiscountId.getOrDefault(discountId, new HashSet<>()).size() > 1) {
                            continue;
                        }
                        if (removeDiscount) {
                            SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(draftId, discountId);
                            shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                        }
                    }
                }

                /*SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractQueryResponse.getData().get().getSubscriptionContract().get();

                if(subscriptionContract.getDiscounts() != null && !CollectionUtils.isNullOrEmpty(subscriptionContract.getDiscounts().getEdges())) {
                    for(SubscriptionContractQuery.Edge2 discountEdge : subscriptionContract.getDiscounts().getEdges()) {
                        String discountId = discountEdge.getNode().getId();
                        SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(optionalDraftId.get(), discountId);
                        shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                    }
                }*/

                //Remove line item from draft id
                SubscriptionDraftLineRemoveMutation subscriptionDraftLineRemoveMutation = new SubscriptionDraftLineRemoveMutation(draftId, lineId);
                Response<Optional<SubscriptionDraftLineRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineRemoveMutation);


                List<SubscriptionContractQuery.Node2> appliedDiscounts = requireNonNull(subscriptionContractQueryResponse.getData())
                    .map(data -> data.getSubscriptionContract().stream()
                        .map(SubscriptionContractQuery.SubscriptionContract::getDiscounts)
                        .map(discounts -> discounts.getEdges()
                            .stream().map(SubscriptionContractQuery.Edge2::getNode).collect(Collectors.toList())
                        )
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
                    ).stream().flatMap(Collection::stream).collect(Collectors.toList());

                //After Removed line item again committing
                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (!org.springframework.util.CollectionUtils.isEmpty(optionalDraftCommitResponse.getErrors())) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftCommitMutation.UserError> userErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(s -> s.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "10001");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    if (existingLineNode.getVariantId().isPresent()) {
                        map.put("variantId", existingLineNode.getVariantId().get());
                        map.put("price", existingLineNode.getCurrentPrice().getAmount());
                    }
                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS,
                        eventSource, isOneTimeProduct ? ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_REMOVED : ActivityLogEventType.PRODUCT_REMOVE,
                        ActivityLogStatus.SUCCESS, map);

                    List<String> productsRemoved = new ArrayList<>();
                    String oldVariantName = existingLineNode.getTitle() + " " + (existingLineNode.getVariantTitle().isPresent() ? "-" + existingLineNode.getVariantTitle().get() : "") + (existingLineNode.getSku().isPresent() ? (StringUtils.isNotBlank(existingLineNode.getSku().get()) ? "  |" + " SKU: " + existingLineNode.getSku().get() : "") : "");

                    if(!StringUtils.isBlank(oldVariantName)){
                        productsRemoved.add(oldVariantName);
                    }

                    if(!isOneTimeProduct) {
                        Map<String, Object> additionalEmailAttributes = new HashMap<>();
                        additionalEmailAttributes.put("productsRemoved", productsRemoved);
                        sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED, additionalEmailAttributes);
                    }

                    commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);
                }
            }
        }
        resyncBuildABoxDiscount(shop, contractId);
        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    private void subscriptionContractsDraftRemoveLineItem(String shop, String lineId, Boolean removeDiscount, SubscriptionContractQuery.SubscriptionContract subscriptionContract, ShopifyGraphqlClient shopifyGraphqlClient, String draftId) throws Exception {

        Long contractId = ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId());

        //getting all line items in array
        List<SubscriptionContractQuery.Node> existingLineItems = subscriptionContract.getLines().getEdges()
            .stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .collect(Collectors.toList());


        //trying to find out selected line item
        Optional<SubscriptionContractQuery.Node> existingLineItemNodeOptional = subscriptionContract.getLines().getEdges()
            .stream().map(SubscriptionContractQuery.Edge::getNode)
            .filter(g -> g.getId().equals(lineId))
            .findFirst();

        if (existingLineItemNodeOptional.isEmpty()) {
            throw new BadRequestAlertException("Couldn't find LineId=" + lineId, "", "");
        }

        SubscriptionContractQuery.Node existingLineNode = existingLineItemNodeOptional.get();

        String lineMinCyclesValue = commonUtils.getAttributeValue(existingLineNode.getCustomAttributes(), AppstleAttribute.MIN_CYCLES.getKey());

        if (StringUtils.isNotBlank(lineMinCyclesValue)) {
            Integer lineMinCycles = null;
            try {
                lineMinCycles = Integer.parseInt(lineMinCyclesValue.trim());
            } catch (NumberFormatException nfe) {
                log.info("Error in parsing line min cycle attribute value. contractId={}, lineMinCyclesValue={}, Exception={}", contractId, lineMinCyclesValue, ExceptionUtils.getStackTrace(nfe));
            }

            if (lineMinCycles != null) {
                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
                List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                if ((successAttempts.size() + 1) < lineMinCycles) {
                    throw new BadRequestAlertException(customerPortalSettingsDTO.getFreezeUpdateSubscriptionMessage().replace("{{minCycles}}", lineMinCycles.toString()), "", "minCycleNotCompleteForUpdateSubscriptionContractDate");
                }
            }
        }

        Map<String, Set<String>> lineIdsByDiscountId = new HashMap<>();

        for (SubscriptionContractQuery.Node existingLineItem : existingLineItems) {
            for (SubscriptionContractQuery.DiscountAllocation discountAllocation : existingLineItem.getDiscountAllocations()) {
                if (discountAllocation.getDiscount() instanceof SubscriptionContractQuery.AsSubscriptionManualDiscount) {
                    String discountId = ((SubscriptionContractQuery.AsSubscriptionManualDiscount) discountAllocation.getDiscount()).getId();
                    Set<String> lineIds = lineIdsByDiscountId.getOrDefault(discountId, new HashSet<>());
                    lineIds.add(existingLineItem.getId());
                    lineIdsByDiscountId.put(discountId, lineIds);
                }
            }
        }

        for (SubscriptionContractQuery.DiscountAllocation discountAllocation : existingLineNode.getDiscountAllocations()) {
            if (discountAllocation.getDiscount() instanceof SubscriptionContractQuery.AsSubscriptionManualDiscount) {
                String discountId = ((SubscriptionContractQuery.AsSubscriptionManualDiscount) discountAllocation.getDiscount()).getId();

                if (lineIdsByDiscountId.getOrDefault(discountId, new HashSet<>()).size() > 1) {
                    continue;
                }
                if (removeDiscount) {
                    SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(draftId, discountId);
                    shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                }
            }
        }

        //Remove line item from draft id
        SubscriptionDraftLineRemoveMutation subscriptionDraftLineRemoveMutation = new SubscriptionDraftLineRemoveMutation(draftId, lineId);
        Response<Optional<SubscriptionDraftLineRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineRemoveMutation);

    }

    private DiscountCodeResponse generateDiscountCode(String shop, SubscriptionBundlingResponse subscriptionBundlingResponse, int totalQuantity, double totalAmount, List<SubscriptionContractQuery.Node> remaningItemList, Optional<SubscriptionContractQuery.Node2> currentDiscount) throws Exception {

        DiscountCodeResponse discountCodeResponse = new DiscountCodeResponse();
        TieredDiscountDTO applicableDiscount = new TieredDiscountDTO();
        SubscriptionBundling bundle = subscriptionBundlingResponse.getBundle();
        List<SubscriptionGroupPlan> subscriptions = subscriptionBundlingResponse.getSubscriptions();

        if (bundle != null && bundle.getTieredDiscount() != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<TieredDiscountDTO> tieredDiscount = mapper.readValue(bundle.getTieredDiscount(), new TypeReference<List<TieredDiscountDTO>>() {
            });
            if (!CollectionUtils.isNullOrEmpty(tieredDiscount)) {
                applicableDiscount = findApplicableTieredDiscount(tieredDiscount, totalAmount, totalQuantity);
            }
        }

        if (bundle != null && !CollectionUtils.isNullOrEmpty(subscriptions) && (applicableDiscount.getDiscount() != null || (bundle.getDiscount() != null && bundle.getDiscount() != 0D))) {

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

            Set<Long> cartProductIds = remaningItemList
                .stream()
                .map(node -> ShopifyGraphQLUtils.getProductId(node.getProductId().get())).collect(Collectors.toSet());


            Set<Long> cartVariantIds = remaningItemList
                .stream()
                .map(node -> ShopifyGraphQLUtils.getVariantId(node.getVariantId().get()))
                .collect(Collectors.toSet());

            Map<Long, Set<Long>> variantIdsByProductIds = new HashMap<>();

            for (SubscriptionContractQuery.Node item : remaningItemList) {
                Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(ShopifyGraphQLUtils.getProductId(item.getProductId().get()), new HashSet<>());
                productVariantIds.add(ShopifyGraphQLUtils.getVariantId(item.getVariantId().get()));
                variantIdsByProductIds.put(ShopifyGraphQLUtils.getProductId(item.getProductId().get()), productVariantIds);
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

            List<String> variantsToRemove = variants.stream().filter(v -> {
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
            double discountAmountToApply = subscriptionBundlingService.prepareDiscountAmount(shop, bundle, applicableDiscount, totalAmount, totalQuantity) / 100;
            if (discountAmountToApply > 0) {

                if(currentDiscount != null && currentDiscount.isPresent()) {

                    List<SubscriptionContractQuery.Node> lineItemsWithNoDiscount = remaningItemList.stream()
                        .filter(item -> {
                            return !item.getDiscountAllocations().stream()
                                .anyMatch(allocation -> allocation.getDiscount().toString().contains(currentDiscount.get().getId()));
                        })
                        .collect(Collectors.toList());

                    if (!lineItemsWithNoDiscount.isEmpty()) {
                        List<SubscriptionContractQuery.Node> matchedLineItems = lineItemsWithNoDiscount.stream()
                            .filter(lineItem ->
                                (lineItem.getProductId().isPresent() && productsToAdd.contains(lineItem.getProductId().get())) ||
                                    (lineItem.getVariantId().isPresent() && variantsToAdd.contains(lineItem.getVariantId().get())))
                            .collect(Collectors.toList());

                        if(matchedLineItems.isEmpty()) {
                            String input = currentDiscount.get().getValue().toString();

                            String numberToSearch = String.valueOf((int) (discountAmountToApply * 100));
                            String regex = "\\b" + Pattern.quote(numberToSearch) + "\\b";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(input);
                            if (matcher.find()) {
                                discountCodeResponse.setDiscountNeeded(true);
                                return discountCodeResponse;
                            }
                        }
                    } else {
                        String input = currentDiscount.get().getValue().toString();

                        String numberToSearch = String.valueOf((int) (discountAmountToApply * 100));
                        String regex = "\\b" + Pattern.quote(numberToSearch) + "\\b";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(input);
                        if (matcher.find()) {
                            discountCodeResponse.setDiscountNeeded(true);
                            return discountCodeResponse;
                        }
                    }

                }

                DiscountCustomerGetsInput discountCustomerGetsInput = DiscountCustomerGetsInput
                    .builder()
                    .appliesOnSubscription(true)
                    .appliesOnOneTimePurchase(true)
                    .items(discountItemsInput)
                    .value(
                        DiscountCustomerGetsValueInput.builder()
                            .percentage(discountAmountToApply)
                            .build()
                    )//if applicable discount not found the default discount will be applied
                    .build();

                DiscountCustomerSelectionInput discountCustomerSelectionInput = DiscountCustomerSelectionInput
                    .builder()
                    .all(true)
                    .build();

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
                String formattedDate = dateTimeFormatter.format(ZonedDateTime.now().minusDays(1));

                DiscountMinimumQuantityInput discountMinimumQuantityInput =
                    DiscountMinimumQuantityInput
                        .builder()
                        .greaterThanOrEqualToQuantity(Optional.ofNullable(applicableDiscount.getQuantity() != null && applicableDiscount.getDiscountBasedOn().equals("QUANTITY") ? applicableDiscount.getQuantity() :
                            applicableDiscount.getQuantity() != null && applicableDiscount.getDiscountBasedOn().equals("AMOUNT") ? 1 : bundle.getMinProductCount()).orElse(0).toString())
                        .build();

                DiscountMinimumRequirementInput discountMinimumRequirementInput =
                    DiscountMinimumRequirementInput
                        .builder()
                        .quantity(discountMinimumQuantityInput)
                        .build();

                DiscountCodeBasicInput discountCodeBasicInput = DiscountCodeBasicInput
                    .builder()
                    .code("BUILD_A_BOX_DISCOUNT_" + RandomStringUtils.randomAlphabetic(10) + "_" + bundle.getUniqueRef())
                    .customerGets(discountCustomerGetsInput)
                    .title("BUILD_A_BOX_DISCOUNT" + "_" + bundle.getUniqueRef())
                    .minimumRequirement(discountMinimumRequirementInput)
                    .customerSelection(discountCustomerSelectionInput)
                    .recurringCycleLimit(Integer.MAX_VALUE)
                    .startsAt(formattedDate)
                    .build();

                DiscountCodeBasicCreateMutation discountCodeBasicCreateMutation = new DiscountCodeBasicCreateMutation(discountCodeBasicInput);

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                Response<Optional<DiscountCodeBasicCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(discountCodeBasicCreateMutation);

                if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("errors=" + optionalMutationResponse.getErrors().get(0).getMessage() + " shop=" + shop);
                    discountCodeResponse.setDiscountNeeded(false);
                    return discountCodeResponse;
                }

                List<DiscountCodeBasicCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                    .map(d -> d.getDiscountCodeBasicCreate().map(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getUserErrors)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.info("errors=" + userErrors.toString() + " shop=" + shop);
                    discountCodeResponse.setErrorMessage(userErrors.get(0).getMessage());
                    discountCodeResponse.setDiscountNeeded(false);
                    return discountCodeResponse;
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
                discountCodeResponse.setDiscountAmount(discountAmountToApply * 100);
            } else {
                discountCodeResponse.setDiscountNeeded(false);
            }
        } else {
            discountCodeResponse.setDiscountNeeded(false);
        }
        return discountCodeResponse;
    }

    private TieredDiscountDTO findApplicableTieredDiscount(List<TieredDiscountDTO> tieredDiscount, double totalAmount, int totalQuantity) throws IOException {
        TieredDiscountDTO applicableDiscount = new TieredDiscountDTO();

        Optional<TieredDiscountDTO> applicableQuantityBasedDiscountOptional = tieredDiscount.stream()
            .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("QUANTITY"))
            .filter(tieredDiscountDTO -> totalQuantity >= tieredDiscountDTO.getQuantity())
            .reduce((first, second) -> {
                if (first.getDiscount() > second.getDiscount()) {
                    return first;
                } else {
                    return second;
                }
            });

        Optional<TieredDiscountDTO> applicableSpendAmountBasedDiscountOptional = tieredDiscount.stream()
            .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("AMOUNT"))
            .filter(tieredDiscountDTO -> totalAmount >= tieredDiscountDTO.getQuantity())
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
        return applicableDiscount;
    }


    @Override
    public void applyDiscount(
        Long contractId,
        Integer percentage,
        String discountTitle,
        Integer recurringCycleLimit,
        Boolean appliesOnEachItem,
        Double amount,
        String discountType,
        ShopifyGraphqlClient shopifyGraphqlClient,
        String draftId,
        List<String> lineItems,
        ActivityLogEventSource eventSource) throws Exception {

        SubscriptionManualDiscountValueInput subscriptionManualDiscountValueInput = null;
        if (discountType.equals("PERCENTAGE")) {
            subscriptionManualDiscountValueInput =
                SubscriptionManualDiscountValueInput
                    .builder()
                    .percentage(percentage)
                    .build();
        } else {
            SubscriptionManualDiscountFixedAmountInput.Builder subscriptionManualDiscountFixedAmountInputBuilder = SubscriptionManualDiscountFixedAmountInput.builder();

            if (BooleanUtils.isTrue(appliesOnEachItem)) {
                subscriptionManualDiscountFixedAmountInputBuilder.appliesOnEachItem(true);
            } else {
                subscriptionManualDiscountFixedAmountInputBuilder.appliesOnEachItem(false);
            }

            SubscriptionManualDiscountFixedAmountInput build = subscriptionManualDiscountFixedAmountInputBuilder
                .amount(amount)
                .build();

            subscriptionManualDiscountValueInput = SubscriptionManualDiscountValueInput
                .builder()
                .fixedAmount(build)
                .build();
        }


        SubscriptionManualDiscountEntitledLinesInput.Builder subscriptionManualDiscountEntitledLinesInputBuilder = SubscriptionManualDiscountEntitledLinesInput.builder();

        if (lineItems.size() > 0) {
            SubscriptionManualDiscountLinesInput subscriptionManualDiscountLinesInput = SubscriptionManualDiscountLinesInput
                .builder()
                .add(lineItems)
                .build();

            subscriptionManualDiscountEntitledLinesInputBuilder.lines(subscriptionManualDiscountLinesInput);
            subscriptionManualDiscountEntitledLinesInputBuilder.all(false);
        } else {
            subscriptionManualDiscountEntitledLinesInputBuilder.all(true);
        }

        SubscriptionManualDiscountEntitledLinesInput subscriptionManualDiscountEntitledLinesInput =
            subscriptionManualDiscountEntitledLinesInputBuilder.build();

        SubscriptionManualDiscountInput subscriptionManualDiscountInput =
            SubscriptionManualDiscountInput
                .builder()
                .title(discountTitle)
                .recurringCycleLimit(recurringCycleLimit)
                .entitledLines(subscriptionManualDiscountEntitledLinesInput)
                .value(subscriptionManualDiscountValueInput)
                .build();
        SubscriptionDraftDiscountAddMutation subscriptionDraftDiscountAddMutation = new SubscriptionDraftDiscountAddMutation(draftId, subscriptionManualDiscountInput);

        Response<Optional<SubscriptionDraftDiscountAddMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountAddMutation);

        if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        List<SubscriptionDraftDiscountAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getSubscriptionDraftDiscountAdd().map(SubscriptionDraftDiscountAddMutation.SubscriptionDraftDiscountAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
        }

        if (eventSource != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("discountType", discountType);
            map.put("discountTitle", discountTitle);
            map.put("recurringCycleLimit", recurringCycleLimit);
            if (discountType.equals("PERCENTAGE")) {
                map.put("percentage", percentage);
            } else {
                map.put("appliesOnEachItem", appliesOnEachItem);
                map.put("amount", amount);
            }
            commonUtils.writeActivityLog(shopifyGraphqlClient.getShop(), contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DISCOUNT_APPLIED, ActivityLogStatus.SUCCESS, map);
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveDiscount(Long contractId, String shop, String discountId, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("REST request {} Response received from graphql update subscription contract {} ", shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(optionalDraftId.get(), discountId);
                Response<Optional<SubscriptionDraftDiscountRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);

                if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("discountId", discountId);
            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DISCOUNT_REMOVED, ActivityLogStatus.SUCCESS, map);
        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }
    @Override
    public SubscriptionContractQuery.SubscriptionContract removeContractDiscountByCode(Long contractId, String shop, String discountCode, ActivityLogEventSource eventSource) throws Exception {

        String discountId = null;
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();
        discountId = (String) subscriptionContract.getDiscounts().getEdges().stream().map(SubscriptionContractQuery.Edge2::getNode).filter((node) -> {
            return node.getTitle().isPresent() && ((String) node.getTitle().get()).equals(discountCode);
        }).findFirst().map(SubscriptionContractQuery.Node2::getId).orElse(null);

        if(StringUtils.isBlank(discountId)){
            return null;
        }

        return subscriptionContractsRemoveDiscount(contractId, shop, discountId, eventSource);

    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract getSubscriptionContractRawInternal(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient, false);

    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract getSubscriptionContractRawInternal(@PathVariable Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, Boolean updateCancelled) throws Exception {

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);


        while (!org.springframework.util.CollectionUtils.isEmpty(optionalQueryResponse.getErrors()) && optionalQueryResponse.getErrors().get(0).getMessage().equals("Throttled")) {
            Thread.sleep(1000l);
            optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
        }

        if(!CollectionUtils.isNullOrEmpty(optionalQueryResponse.getErrors())){
            SubscriptionContractV2Query subscriptionContractV2Query = new SubscriptionContractV2Query(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractV2Query.Data>> optionalV2QueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractV2Query);
            Optional<SubscriptionContractV2Query.SubscriptionContract> subscriptionContractV2Optional = requireNonNull(optionalV2QueryResponse.getData()).flatMap(SubscriptionContractV2Query.Data::getSubscriptionContract);
            if (subscriptionContractV2Optional.isPresent()) {
                Optional<ChangeShippingAddressVM> changeShippingAddressVM = subscriptionContractV2Optional.get().getDeliveryMethod().map(f -> {
                    ChangeShippingAddressVM shippingAddressVM = null;
                    if (f instanceof SubscriptionContractV2Query.AsSubscriptionDeliveryMethodLocalDelivery) {
                        Optional<SubscriptionContractV2Query.Address1> localDeliveryAddressOptional = Optional.of(((SubscriptionContractV2Query.AsSubscriptionDeliveryMethodLocalDelivery) f).getAddress());

                        shippingAddressVM = new ChangeShippingAddressVM();
                        shippingAddressVM.setAddress1(localDeliveryAddressOptional.map(a -> a.getAddress1().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setCity(localDeliveryAddressOptional.map(a -> a.getCity().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setProvinceCode(localDeliveryAddressOptional.map(a -> a.getProvinceCode().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setZip(localDeliveryAddressOptional.map(a -> a.getZip().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setFirstName(localDeliveryAddressOptional.map(a -> a.getFirstName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setLastName(localDeliveryAddressOptional.map(a -> a.getLastName().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setAddress2(localDeliveryAddressOptional.map(a -> a.getAddress2().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setCountry(localDeliveryAddressOptional.map(a -> a.getCountry().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setCountryCode(localDeliveryAddressOptional.map(a -> a.getCountryCode().map(Object::toString).orElse(CountryCode.$UNKNOWN.rawValue())).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setPhone(localDeliveryAddressOptional.map(a -> a.getPhone().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setCompany(localDeliveryAddressOptional.map(a -> a.getCompany().orElse(org.apache.commons.lang3.StringUtils.EMPTY)).orElse(org.apache.commons.lang3.StringUtils.EMPTY));
                        shippingAddressVM.setMethodType("LOCAL");

                        if(StringUtils.isBlank(shippingAddressVM.getPhone())){
                            if (subscriptionContractV2Optional.get().getCustomer().isPresent() && subscriptionContractV2Optional.get().getCustomer().get().getPhone().isPresent()) {
                                shippingAddressVM.setPhone(subscriptionContractV2Optional.get().getCustomer().get().getPhone().orElse(""));
                            } else {
                                shippingAddressVM.setPhone("1111111111");
                            }
                        }
                    }
                    return shippingAddressVM;
                });
                if(changeShippingAddressVM.isPresent() && StringUtils.isNotBlank(changeShippingAddressVM.get().getPhone())){
                    subscriptionContractUpdateMissingAddress(contractId, shop, changeShippingAddressVM.get(), ActivityLogEventSource.SYSTEM_EVENT);
                    optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                }
            }
        }

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(optionalQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isPresent()) {
            updateDetails(shop, subscriptionContractOptional.get(), updateCancelled);
            commonUtils.removeInvalidDiscounts(shop, contractId, shopifyGraphqlClient, optionalQueryResponse);
        }
        return subscriptionContractOptional.orElse(null);

    }

    private void sendSubscriptionProductAlterationMail(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, EmailSettingType emailSettingType) throws Exception {
        sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, emailSettingType, null);
    }

    @Override
    public void sendSubscriptionProductAlterationMail(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, EmailSettingType emailSettingType, Map<String, Object> additionalAttributes) throws Exception {
        commonEmailUtils.sendSubscriptionUpdateEmail(
            shopifyGraphqlClient,
            commonUtils.prepareShopifyResClient(shop),
            subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
            emailSettingType,
            additionalAttributes);
    }

    private void sendSubscriptionPausedMail(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, SubscriptionContractDetailsDTO persistedSubscriptionContract) throws Exception {
        if ("paused".equalsIgnoreCase(persistedSubscriptionContract.getStatus())) {
            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                commonUtils.prepareShopifyResClient(shop),
                subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
                EmailSettingType.SUBSCRIPTION_PAUSED);
        }
    }

    private void sendSubscriptionResumedMail(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, SubscriptionContractDetailsDTO persistedSubscriptionContract) throws Exception {
        if ("active".equalsIgnoreCase(persistedSubscriptionContract.getStatus())) {
            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                commonUtils.prepareShopifyResClient(shop),
                subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get(),
                EmailSettingType.SUBSCRIPTION_RESUMED);
        }
    }

    private void sendMailForSubscriptionCanceled(Long id, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(id).get();

        if (subscriptionContractDetails.getStatus().equalsIgnoreCase("cancelled")) {
            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                commonUtils.prepareShopifyResClient(shop),
                subscriptionContractDetails,
                EmailSettingType.SUBSCRIPTION_CANCELLED);
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceRemovedVariants(String shop, Long contractId, String removedVariantTitle, Long newVariantId, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        Optional<SubscriptionContractQuery.Node> existingLineItem = null;

        do {
            existingLineItem = subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> (s.getTitle() + " " + (s.getVariantTitle().isPresent() ? "-" + s.getVariantTitle().get() : "")).trim().equals(removedVariantTitle.trim()) && s.getVariantId().isEmpty())
                .findFirst();

            if (existingLineItem.isPresent()) {

                Optional<SubscriptionContractQuery.Node> newLineItem = subscriptionContract.getLines().getEdges()
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equalsIgnoreCase(ShopifyGraphQLUtils.getGraphQLVariantId(newVariantId)))
                    .findFirst();


                List<AttributeInfo> customAttributes = null;

                if(!CollectionUtils.isNullOrEmpty(existingLineItem.get().getCustomAttributes())) {
                    customAttributes = existingLineItem.get().getCustomAttributes().stream()
                        .map(ca -> new AttributeInfo(ca.getKey(), ca.getValue().orElse("")))
                        .collect(Collectors.toList());
                }

                if (newLineItem.isEmpty()) {
                    subscriptionContract = subscriptionContractsAddLineItem(contractId, shop, existingLineItem.get().getQuantity(), ShopifyGraphQLUtils.getGraphQLVariantId(newVariantId), false, "", customAttributes, eventSource);
                }

                SubscriptionContractQuery.Node updatedRemovedVariant = subscriptionContract.getLines().getEdges().stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(s -> (s.getTitle() + " " + (s.getVariantTitle().isPresent() ? "-" + s.getVariantTitle().get() : "")).trim().equals(removedVariantTitle.trim()) && s.getVariantId().isEmpty()).findFirst().get();

                subscriptionContract = subscriptionContractsRemoveLineItem(contractId, shop, updatedRemovedVariant.getId(), "", true, eventSource);
            }

        } while (existingLineItem.isPresent());

        commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);

        return subscriptionContract;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceVariants(String shop, Long contractId, List<Long> oldVariantIds, List<Long> newVariantIds) throws Exception {

        // Validate input data
        if (!ObjectUtils.allNotNull(shop, contractId, oldVariantIds, newVariantIds)) {
            return null;
        }

        if (CollectionUtils.isNullOrEmpty(oldVariantIds)
            || CollectionUtils.isNullOrEmpty(newVariantIds)
            || org.apache.commons.collections.CollectionUtils.intersection(oldVariantIds, newVariantIds).size() > 0) {
            return null;
        }

        // Remove duplicate
        newVariantIds = newVariantIds.stream().distinct().collect(Collectors.toList());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        boolean matchFound = false;

        do {
            List<Long> existingVariantIds = subscriptionContract.getLines().getEdges().stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getVariantId().isPresent())
                .map(s -> s.getVariantId().get())
                .map(ShopifyGraphQLUtils::getVariantId)
                .collect(Collectors.toList());

            if (existingVariantIds.containsAll(oldVariantIds)) {
                matchFound = true;
            } else {
                matchFound = false;
                break;
            }

            Optional<SubscriptionContractQuery.Node> firstOldLine = subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(oldVariantIds.get(0))))
                .findFirst();

            if(firstOldLine.isEmpty()) {
                break;
            }

            int qty = firstOldLine.get().getQuantity();

            List<AttributeInfo> customAttributes = null;
            if(!CollectionUtils.isNullOrEmpty(firstOldLine.get().getCustomAttributes())) {
                customAttributes = firstOldLine.get().getCustomAttributes().stream()
                    .map(ca -> new AttributeInfo(ca.getKey(), ca.getValue().orElse("")))
                    .collect(Collectors.toList());

            }

            for (Long newVariantId : newVariantIds) {
                subscriptionContract = subscriptionContractsAddLineItem(contractId, shop, qty, ShopifyGraphQLUtils.getGraphQLVariantId(newVariantId), false, "", customAttributes, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }

            for (Long oldVariantId : oldVariantIds) {
                SubscriptionContractQuery.Node oldLine = subscriptionContract.getLines().getEdges()
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(oldVariantId)))
                    .findFirst().get();

                subscriptionContract = subscriptionContractsRemoveLineItem(contractId, shop, oldLine.getId(), "", true, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }

        } while (matchFound);

        return subscriptionContract;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceVariantsV2WithRetry(String shop, Long contractId, List<Long> oldVariantIds, List<Long> newVariantIds, Integer quantity, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource, int retryCycle) throws Exception {
        try {
            return replaceVariantsV2(shop, contractId, oldVariantIds, newVariantIds, quantity, oldLineId, carryForwardDiscount, eventSource);
        }catch (NullPointerException ex){
            throw ex;
        }catch(Exception e){
            if(e.getMessage().contains("The subscription contract has changed. Retry the operation.") && retryCycle > 0){
                retryCycle--;
                replaceVariantsV2WithRetry(shop, contractId, oldVariantIds, newVariantIds, quantity, oldLineId, carryForwardDiscount, eventSource, retryCycle);
            }else if(retryCycle > 0 &&
                ((e.getMessage().contains("Contract draft discount with title") && e.getMessage().contains("is invalid")) ||
                (e.getMessage().contains("Contract draft has invalid discount code:") && e.getMessage().contains("applied")))){
                shopifyGraphqlSubscriptionContractService.removeDiscountCodeBasedOnError(shop, contractId, commonUtils.prepareShopifyGraphqlClient(shop), e.getMessage());
                retryCycle--;
                replaceVariantsV2WithRetry(shop, contractId, oldVariantIds, newVariantIds, quantity, oldLineId, carryForwardDiscount, eventSource, retryCycle);
            }else{
                throw e;
            }
        }
        return null;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceVariantsV2(String shop, Long contractId, List<Long> oldVariantIds, List<Long> newVariantIds, Integer quantity, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        if (StringUtils.isNotBlank(oldLineId)) {
            Optional<SubscriptionContractQuery.Node> oldLine = subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getId().equals(oldLineId))
                .findFirst();

            if(oldLine.isPresent()) {
                if (oldLine.get().getVariantId().isPresent()) {
                    oldVariantIds = List.of(ShopifyGraphQLUtils.getVariantId(oldLine.get().getVariantId().get()));
                } else {
                    return replaceRemovedVariants(shop, contractId, oldLine.get().getTitle() + " " + (oldLine.get().getVariantTitle().isPresent() ? "-" + oldLine.get().getVariantTitle().get() : "").trim(), newVariantIds.get(0), eventSource);
                }
            } else {
                throw new BadRequestAlertException("Contract line not found for id: " + oldLineId, ENTITY_NAME, "");
            }
        }
        // Validate input data
        if (!ObjectUtils.allNotNull(shop, contractId, oldVariantIds, newVariantIds)) {
            return null;
        }

        if (CollectionUtils.isNullOrEmpty(oldVariantIds)
            || CollectionUtils.isNullOrEmpty(newVariantIds)
            || org.apache.commons.collections.CollectionUtils.intersection(oldVariantIds, newVariantIds).size() > 0) {
            throw new BadRequestAlertException("New variant Id cannot be same as Old variant Id", ENTITY_NAME, "idnull");
        }

        List<Long> existingVariantIds = subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(s -> s.getVariantId().isPresent())
            .map(s -> s.getVariantId().get())
            .map(ShopifyGraphQLUtils::getVariantId)
            .collect(Collectors.toList());

        boolean matchFound = existingVariantIds.containsAll(oldVariantIds);

        List<Map<String, Object>> addedProductsAttributeList = new ArrayList<>();
        List<Map<String, Object>> removedProductAttributesList = new ArrayList<>();

        List<String> productsAdded = new ArrayList<>();
        List<String> productsRemoved = new ArrayList<>();

        while (matchFound) {
            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
            log.info("{} REST request for {} Response received from graphql update subscription contract {} ", "", shop, contractId);

            long countOfErrors = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", "", message)).count();

            if (countOfErrors == 0) {

                Optional<String> optionalDraftId = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                    .map(draft -> draft.get().getId());

                if (optionalDraftId.isPresent()) {

                    String draftId = optionalDraftId.get();

                    final String oldVariantGraphId = ShopifyGraphQLUtils.getGraphQLVariantId(oldVariantIds.get(0));

                    if (Objects.isNull(quantity)) {
                        quantity = subscriptionContract.getLines().getEdges()
                            .stream()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(oldVariantGraphId))
                            .map(SubscriptionContractQuery.Node::getQuantity)
                            .findFirst().orElse(1);
                    }

                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

                    List<AttributeInput> attributeInputList = new ArrayList<>();
                    if (BooleanUtils.isTrue(shopInfoDTO.isKeepLineAttributes())) {
                        Optional<List<SubscriptionContractQuery.CustomAttribute>> existingCustomAttributes = subscriptionContract.getLines().getEdges()
                            .stream()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(oldVariantGraphId))
                            .map(SubscriptionContractQuery.Node::getCustomAttributes).findFirst();

                        if (existingCustomAttributes.isPresent()) {
                            for (SubscriptionContractQuery.CustomAttribute customAttribute : existingCustomAttributes.get()) {
                                AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                                attributeInputBuilder.key(customAttribute.getKey());
                                if (customAttribute.getValue().isPresent()) {
                                    attributeInputBuilder.value(customAttribute.getValue().get());
                                }
                                attributeInputList.add(attributeInputBuilder.build());
                            }
                        }
                    }

                    Optional<String> optionalContractCurrencyCode = subscriptionContract.getLines().getEdges().stream()
                        .findFirst()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .map(SubscriptionContractQuery.Node::getCurrentPrice)
                        .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
                        .map(CurrencyCode::rawValue);

                    for (Long newVariantId : newVariantIds) {

                        String newProductVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(newVariantId);

                        Object price = null;
                        String newProductName = "";
                        if (optionalContractCurrencyCode.isPresent() && !optionalContractCurrencyCode.get().equals(shopInfoDTO.getCurrency())) {
                            String currencyCode = optionalContractCurrencyCode.get();
                            CountryCode countryCode = CurrencyUtils.getCountryCode(currencyCode);
                            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(countryCode).build();
                            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(newProductVariantId, contextualPricingContext);
                            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            if(requireNonNull(productVariantResponse.getData()).isEmpty() ||
                                requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant).isEmpty()){
                                throw new BadRequestAlertException("Product Variant not found for ID: "+ newProductVariantId, "", "");
                            }

                            price = requireNonNull(productVariantResponse.getData())
                                .flatMap(e -> e.getProductVariant()
                                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                                .orElse(null);

                            newProductName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .map(ProductVariantContextualPricingQuery.ProductVariant::getProduct)
                                .map(ProductVariantContextualPricingQuery.Product::getTitle).orElse("");

                            String newVariantName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .filter(variant -> !variant.getTitle().equalsIgnoreCase("Default Title") && !variant.getTitle().equals("-"))
                                .map(ProductVariantContextualPricingQuery.ProductVariant::getTitle).orElse("");

                            String productSku = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .flatMap(ProductVariantContextualPricingQuery.ProductVariant::getSku).orElse("");

                            newProductName = newProductName + (StringUtils.isNotBlank(newVariantName) ? " -" + newVariantName : "") + (StringUtils.isNotBlank(productSku) ? "  |" + " SKU: " + productSku : "");

                        } else {
                            ProductVariantQuery productVariantQuery = new ProductVariantQuery(newProductVariantId);
                            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            if(requireNonNull(productVariantResponse.getData()).isEmpty() ||
                                requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantQuery.Data::getProductVariant).isEmpty()){
                                throw new BadRequestAlertException("Product Variant not found for ID: "+ newProductVariantId, "", "");
                            }

                            price = requireNonNull(productVariantResponse.getData()).flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice)).orElse(null);

                            newProductName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .map(ProductVariantQuery.ProductVariant::getProduct)
                                .map(ProductVariantQuery.Product::getTitle).orElse("");

                            String newVariantName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .filter(variant -> !variant.getTitle().equalsIgnoreCase("Default Title") && !variant.getTitle().equals("-"))
                                .map(ProductVariantQuery.ProductVariant::getTitle).orElse("");

                            String productSku = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .flatMap(ProductVariantQuery.ProductVariant::getSku).orElse("");

                            newProductName = newProductName + (StringUtils.isNotBlank(newVariantName) ? " -" + newVariantName : "") + (StringUtils.isNotBlank(productSku) ? "  |" + " SKU: " + productSku : "");

                        }

                        if(!StringUtils.isBlank(newProductName)){
                            productsAdded.add(newProductName);
                        }

                        subscriptionContractDraftAddLineItem(shop, quantity, newProductVariantId, Double.parseDouble(price.toString()), subscriptionContract, shopifyGraphqlClient, draftId, attributeInputList, carryForwardDiscount, null, addedProductsAttributeList);
                    }

                    for (Long oldVariantId : oldVariantIds) {
                        String oldVariantName = "";
                        SubscriptionContractQuery.Node oldLine;

                        if (StringUtils.isBlank(oldLineId)) {
                            oldLine = subscriptionContract.getLines().getEdges()
                                .stream()
                                .map(SubscriptionContractQuery.Edge::getNode)
                                .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(oldVariantId)))
                                .findFirst().get();
                        } else {
                            oldLine = subscriptionContract.getLines().getEdges()
                                .stream()
                                .map(SubscriptionContractQuery.Edge::getNode)
                                .filter(s -> s.getId().equals(oldLineId))
                                .findFirst().get();
                        }

                        subscriptionContractsDraftRemoveLineItem(shop, oldLine.getId(), true, subscriptionContract, shopifyGraphqlClient, draftId);

                        oldVariantName = oldLine.getTitle() + " " + (oldLine.getVariantTitle().isPresent() ? "-" + oldLine.getVariantTitle().get() : "") + (oldLine.getSku().isPresent() ? (StringUtils.isNotBlank(oldLine.getSku().get()) ? "  |" + " SKU: " + oldLine.getSku().get() : "") : "");

                        if(!StringUtils.isBlank(oldVariantName)){
                            productsRemoved.add(oldVariantName);
                        }

                        Map<String, Object> removedProductAttributes = new HashMap<>();
                        removedProductAttributes.put("variantId", oldVariantId);
                        removedProductAttributes.put("price", oldLine.getCurrentPrice().getAmount());
                        removedProductAttributes.put("pricingPolicy", oldLine.getPricingPolicy().isPresent() ? OBJECT_MAPPER.writeValueAsString(oldLine.getPricingPolicy().get()) : null);
                        removedProductAttributesList.add(removedProductAttributes);
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData())
                        .map(d -> d.getSubscriptionDraftCommit()
                            .map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>());

                    if (draftCommitResponseUserErrors.size() > 0) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

//                for (Map<String, Object> addedProductAttributes : addedProductsAttributeList) {
//                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION, ActivityLogEventType.PRODUCT_ADD, ActivityLogStatus.SUCCESS, addedProductAttributes);
//                    sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED);
//                }
//
//                for (Map<String, Object> removedProductAttributes : removedProductAttributesList) {
//                    commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION, ActivityLogEventType.PRODUCT_REMOVE, ActivityLogStatus.SUCCESS, removedProductAttributes);
//                    sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED);
//                }

                    subscriptionContract = getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
                }
            }
            existingVariantIds = subscriptionContract.getLines().getEdges().stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getVariantId().isPresent())
                .map(s -> s.getVariantId().get())
                .map(ShopifyGraphQLUtils::getVariantId)
                .collect(Collectors.toList());

            matchFound = existingVariantIds.containsAll(oldVariantIds);
        }

        resyncBuildABoxDiscount(shop, contractId);

        Map<String, Object> productReplaceAttributes = new HashMap<>();
        productReplaceAttributes.put("oldProducts", removedProductAttributesList);
        productReplaceAttributes.put("newProducts", addedProductsAttributeList);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_REPLACE, ActivityLogStatus.SUCCESS, productReplaceAttributes);

        Map<String, Object> additionalEmailAttributes = new HashMap<>();
        additionalEmailAttributes.put("productsAdded", productsAdded);
        additionalEmailAttributes.put("productsRemoved", productsRemoved);
        sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED, additionalEmailAttributes);

        return subscriptionContract;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceVariantsV3WithRetry(String shop, Long contractId, List<Long> oldVariantIds, Map<Long, Integer> newVariants, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource, int retryCycle) throws Exception {
        try {
            return replaceVariantsV3(shop, contractId, oldVariantIds, newVariants, oldLineId, carryForwardDiscount, eventSource);
        }catch (NullPointerException ex){
            throw ex;
        }catch(Exception e){
            if(e.getMessage().contains("The subscription contract has changed. Retry the operation.") && retryCycle > 0){
                retryCycle--;
                replaceVariantsV3WithRetry(shop, contractId, oldVariantIds, newVariants, oldLineId, carryForwardDiscount, eventSource, retryCycle);
            }else if(retryCycle > 0 &&
                ((e.getMessage().contains("Contract draft discount with title") && e.getMessage().contains("is invalid")) ||
                    (e.getMessage().contains("Contract draft has invalid discount code:") && e.getMessage().contains("applied")))){
                shopifyGraphqlSubscriptionContractService.removeDiscountCodeBasedOnError(shop, contractId, commonUtils.prepareShopifyGraphqlClient(shop), e.getMessage());
                retryCycle--;
                replaceVariantsV3WithRetry(shop, contractId, oldVariantIds, newVariants, oldLineId, carryForwardDiscount, eventSource, retryCycle);
            }else{
                throw e;
            }
        }
        return null;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract replaceVariantsV3(String shop, Long contractId, List<Long> oldVariants, Map<Long, Integer> newVariants, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        if (StringUtils.isNotBlank(oldLineId)) {
            String oldVariantGqlId = requireNonNull(subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getId().equals(oldLineId))
                .findFirst().orElse(null)).getVariantId().orElse("");
            if (StringUtils.isBlank(oldVariantGqlId)) {
                throw new BadRequestAlertException("Contract line not found for id: " + oldLineId, ENTITY_NAME, "");
            }
            oldVariants = List.of(ShopifyGraphQLUtils.getVariantId(oldVariantGqlId));
        }

        // Validate input data
        if (!ObjectUtils.allNotNull(shop, contractId, oldVariants, newVariants)) {
            return null;
        }

        if (oldVariants.isEmpty()
            || newVariants.isEmpty()
            || oldVariants.stream().anyMatch(newVariants::containsKey)) {
            throw new BadRequestAlertException("New variant Id cannot be same as Old variant Id", ENTITY_NAME, "idnull");
        }

        List<Long> existingVariantIds = subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(s -> s.getVariantId().isPresent())
            .map(s -> s.getVariantId().get())
            .map(ShopifyGraphQLUtils::getVariantId)
            .collect(Collectors.toList());

        boolean matchFound = existingVariantIds.containsAll(oldVariants);

        List<Map<String, Object>> addedProductsAttributeList = new ArrayList<>();
        List<Map<String, Object>> removedProductAttributesList = new ArrayList<>();

        List<String> productsAdded = new ArrayList<>();
        List<String> productsRemoved = new ArrayList<>();

        while (matchFound) {
            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
            log.info("{} REST request for {} Response received from graphql update subscription contract {} ", "", shop, contractId);

            long countOfErrors = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", "", message)).count();

            if (countOfErrors == 0) {

                Optional<String> optionalDraftId = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                    .map(draft -> draft.get().getId());

                if (optionalDraftId.isPresent()) {

                    String draftId = optionalDraftId.get();

                    final String oldVariantGraphId = ShopifyGraphQLUtils.getGraphQLVariantId(oldVariants.get(0));

                    Optional<SubscriptionContractQuery.Node> optionalOldNode = subscriptionContract.getLines().getEdges().stream()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(oldVariantGraphId))
                        .findFirst();

                    if(optionalOldNode.isEmpty()) {
                        continue;
                    }

                    SubscriptionContractQuery.Node oldNode = optionalOldNode.get();

                    int defaultQuantity = oldNode.getQuantity();

                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

                    List<AttributeInput> attributeInputList = new ArrayList<>();
                    if (BooleanUtils.isTrue(shopInfoDTO.isKeepLineAttributes())) {

                       List<SubscriptionContractQuery.CustomAttribute> existingCustomAttributes = oldNode.getCustomAttributes();

                        if (!CollectionUtils.isNullOrEmpty(existingCustomAttributes)) {
                            for (SubscriptionContractQuery.CustomAttribute customAttribute : existingCustomAttributes) {
                                AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                                attributeInputBuilder.key(customAttribute.getKey());
                                if (customAttribute.getValue().isPresent()) {
                                    attributeInputBuilder.value(customAttribute.getValue().get());
                                }
                                attributeInputList.add(attributeInputBuilder.build());
                            }
                        }
                    }

                    Optional<String> optionalContractCurrencyCode = subscriptionContract.getLines().getEdges().stream()
                        .findFirst()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .map(SubscriptionContractQuery.Node::getCurrentPrice)
                        .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
                        .map(CurrencyCode::rawValue);

                    for (Map.Entry<Long, Integer> newEntry : newVariants.entrySet()) {

                        String newProductVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(newEntry.getKey());

                        Object price = null;
                        String newProductName = "";
                        if (optionalContractCurrencyCode.isPresent() && !optionalContractCurrencyCode.get().equals(shopInfoDTO.getCurrency())) {
                            String currencyCode = optionalContractCurrencyCode.get();
                            CountryCode countryCode = CurrencyUtils.getCountryCode(currencyCode);
                            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(countryCode).build();
                            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(newProductVariantId, contextualPricingContext);
                            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            if(requireNonNull(productVariantResponse.getData()).isEmpty() ||
                                requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant).isEmpty()){
                                throw new BadRequestAlertException("Product Variant not found for ID: "+ newProductVariantId, "", "");
                            }

                            price = requireNonNull(productVariantResponse.getData())
                                .flatMap(e -> e.getProductVariant()
                                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                                .orElse(null);

                            newProductName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .map(ProductVariantContextualPricingQuery.ProductVariant::getProduct)
                                .map(ProductVariantContextualPricingQuery.Product::getTitle).orElse("");

                            String newVariantName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .filter(variant -> !variant.getTitle().equalsIgnoreCase("Default Title") && !variant.getTitle().equals("-"))
                                .map(ProductVariantContextualPricingQuery.ProductVariant::getTitle).orElse("");

                            String productSku = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant)
                                .flatMap(ProductVariantContextualPricingQuery.ProductVariant::getSku).orElse("");

                            newProductName = newProductName + (StringUtils.isNotBlank(newVariantName) ? " -" + newVariantName : "") + (StringUtils.isNotBlank(productSku) ? "  |" + " SKU: " + productSku : "");

                        } else {
                            ProductVariantQuery productVariantQuery = new ProductVariantQuery(newProductVariantId);
                            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            if(requireNonNull(productVariantResponse.getData()).isEmpty() ||
                                requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantQuery.Data::getProductVariant).isEmpty()){
                                throw new BadRequestAlertException("Product Variant not found for ID: "+ newProductVariantId, "", "");
                            }

                            price = requireNonNull(productVariantResponse.getData()).flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice)).orElse(null);

                            newProductName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .map(ProductVariantQuery.ProductVariant::getProduct)
                                .map(ProductVariantQuery.Product::getTitle).orElse("");

                            String newVariantName = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .filter(variant -> !variant.getTitle().equalsIgnoreCase("Default Title") && !variant.getTitle().equals("-"))
                                .map(ProductVariantQuery.ProductVariant::getTitle).orElse("");

                            String productSku = requireNonNull(productVariantResponse.getData())
                                .flatMap(ProductVariantQuery.Data::getProductVariant)
                                .flatMap(ProductVariantQuery.ProductVariant::getSku).orElse("");

                            newProductName = newProductName + (StringUtils.isNotBlank(newVariantName) ? " -" + newVariantName : "") + (StringUtils.isNotBlank(productSku) ? "  |" + " SKU: " + productSku : "");

                        }

                        if(!StringUtils.isBlank(newProductName)){
                            productsAdded.add(newProductName);
                        }

                        Integer quantity = Objects.nonNull(newEntry.getValue()) ? newEntry.getValue() : defaultQuantity;

                        subscriptionContractDraftAddLineItem(shop, quantity, newProductVariantId, Double.parseDouble(price.toString()), subscriptionContract, shopifyGraphqlClient, draftId, attributeInputList, carryForwardDiscount, oldNode.getId(), addedProductsAttributeList);
                    }

                    for (Long oldVariantId : oldVariants) {
                        String oldVariantName = "";
                        SubscriptionContractQuery.Node oldLine;

                        if (StringUtils.isBlank(oldLineId)) {
                            oldLine = subscriptionContract.getLines().getEdges()
                                .stream()
                                .map(SubscriptionContractQuery.Edge::getNode)
                                .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(oldVariantId)))
                                .findFirst().get();
                        } else {
                            oldLine = subscriptionContract.getLines().getEdges()
                                .stream()
                                .map(SubscriptionContractQuery.Edge::getNode)
                                .filter(s -> s.getId().equals(oldLineId))
                                .findFirst().get();
                        }

                        subscriptionContractsDraftRemoveLineItem(shop, oldLine.getId(), false, subscriptionContract, shopifyGraphqlClient, draftId);

                        oldVariantName = oldLine.getTitle() + " " + (oldLine.getVariantTitle().isPresent() ? "-" + oldLine.getVariantTitle().get() : "") + (oldLine.getSku().isPresent() ? (StringUtils.isNotBlank(oldLine.getSku().get()) ? "  |" + " SKU: " + oldLine.getSku().get() : "") : "");

                        if(!StringUtils.isBlank(oldVariantName)){
                            productsRemoved.add(oldVariantName);
                        }

                        Map<String, Object> removedProductAttributes = new HashMap<>();
                        removedProductAttributes.put("variantId", oldVariantId);
                        removedProductAttributes.put("price", oldLine.getCurrentPrice().getAmount());
                        removedProductAttributes.put("pricingPolicy", oldLine.getPricingPolicy().isPresent() ? OBJECT_MAPPER.writeValueAsString(oldLine.getPricingPolicy().get()) : null);
                        removedProductAttributesList.add(removedProductAttributes);
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                    if (draftCommitResponseUserErrors.size() > 0) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

                    subscriptionContract = getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
                }
            }
            existingVariantIds = subscriptionContract.getLines().getEdges().stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> s.getVariantId().isPresent())
                .map(s -> s.getVariantId().get())
                .map(ShopifyGraphQLUtils::getVariantId)
                .collect(Collectors.toList());

            matchFound = existingVariantIds.containsAll(oldVariants);
        }

        resyncBuildABoxDiscount(shop, contractId);

        Map<String, Object> productReplaceAttributes = new HashMap<>();
        productReplaceAttributes.put("oldProducts", removedProductAttributesList);
        productReplaceAttributes.put("newProducts", addedProductsAttributeList);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.PRODUCT_REPLACE, ActivityLogStatus.SUCCESS, productReplaceAttributes);

        Map<String, Object> additionalEmailAttributes = new HashMap<>();
        additionalEmailAttributes.put("productsAdded", productsAdded);
        additionalEmailAttributes.put("productsRemoved", productsRemoved);
        sendSubscriptionProductAlterationMail(contractId, shop, shopifyGraphqlClient, EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED, additionalEmailAttributes);

        commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);

        return subscriptionContract;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItem(String shop, Long contractId, Long variantId) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        Optional<SubscriptionContractQuery.Node> existingLineItem = null;

        do {
            existingLineItem = subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> (s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(variantId))))
                .findFirst();

            if (existingLineItem.isPresent()) {
                subscriptionContract = subscriptionContractsRemoveLineItem(contractId, shop, existingLineItem.get().getId(), "", true, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }
        } while (existingLineItem.isPresent());

        return subscriptionContract;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveDeletedLineItem(String shop, Long contractId) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        Optional<SubscriptionContractQuery.Node> existingLineItem = null;

        do {
            existingLineItem = subscriptionContract.getLines().getEdges()
                .stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .filter(s -> (s.getVariantId().isEmpty()))
                .findFirst();

            if (existingLineItem.isPresent()) {
                subscriptionContract = subscriptionContractsRemoveLineItem(contractId, shop, existingLineItem.get().getId(), "", true, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }
        } while (existingLineItem.isPresent());

        return subscriptionContract;

    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractUpdateMinCycles(Long contractId, String shop, Integer minCycles, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        Integer billingIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        SellingPlanInterval billingInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getBillingPolicyInterval().toUpperCase());

        ZonedDateTime nextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Integer maxCycles = subscriptionContractDetailsDTO.getMaxCycles();

        Integer deliveryIntervalCount = subscriptionContractDetailsDTO.getDeliveryPolicyIntervalCount();
        SellingPlanInterval deliveryInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getDeliveryPolicyInterval().toUpperCase());

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        List<SellingPlanAnchorInput> oldAnchors = getOldAnchors(subscriptionContract.getBillingPolicy().getAnchors());

        Integer oldMinCycle = subscriptionContractDetailsDTO.getMinCycles();

        SubscriptionContractQuery.SubscriptionContract subscriptionContractResponseEntity = updateContractWith(
            contractId,
            shop,
            billingIntervalCount,
            billingInterval,
            nextBillingDate,
            shopifyGraphqlClient,
            subscriptionContractDetailsDTO,
            maxCycles,
            minCycles,
            deliveryIntervalCount,
            deliveryInterval,
            false,
            oldAnchors);

        Map<String, Object> map = new HashMap<>();
        map.put("oldMinCycles", oldMinCycle);
        map.put("newMinCycles", minCycles);
        map.put("reason", "Min Cycle Updated");

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SUBSCRIPTION_CONTRACT_UPDATED, ActivityLogStatus.SUCCESS, map);

        return subscriptionContractResponseEntity;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract updateContractWith(
        Long contractId,
        String shop,
        int billingIntervalCount,
        SellingPlanInterval billingInterval,
        ZonedDateTime nextBillingDate,
        ShopifyGraphqlClient shopifyGraphqlClient,
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO,
        Integer maxCycles,
        Integer minCycles,
        Integer deliveryIntervalCount,
        SellingPlanInterval deliveryInterval,
        Boolean overwriteAnchorDay,
        List<SellingPlanAnchorInput> anchorList) throws Exception {

        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        SubscriptionBillingPolicyInput.Builder subscriptionBillingPolicyInput = SubscriptionBillingPolicyInput
            .builder()
            .intervalCount(billingIntervalCount)
            .interval(billingInterval)
            .maxCycles(maxCycles)
            .minCycles(minCycles);

        SubscriptionDeliveryPolicyInput subscriptionDeliveryPolicyInput = SubscriptionDeliveryPolicyInput
            .builder()
            .intervalCount(deliveryIntervalCount)
            .interval(deliveryInterval)
            .build();

        subscriptionDraftInputBuilder.deliveryPolicy(subscriptionDeliveryPolicyInput);

        if(BooleanUtils.isTrue(overwriteAnchorDay)){
            subscriptionBillingPolicyInput.anchors(new ArrayList<>());
            Map<String, Object> map = new HashMap<>();
            map.put("oldAnchor", anchorList);
            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.ANCHOR_DAY_REMOVED, ActivityLogStatus.SUCCESS, map);
        } else {
            subscriptionBillingPolicyInput.anchors(anchorList);
        }

        subscriptionDraftInputBuilder.billingPolicy(subscriptionBillingPolicyInput.build());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedDate = dateTimeFormatter.format(nextBillingDate);
        subscriptionDraftInputBuilder.nextBillingDate(formattedDate);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        Optional<Long> nextBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(contractId);

        subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, contractId);

        subscriptionBillingAttemptService.deleteByContractIdAndStatusAndBillingDateAfter(contractId, BillingAttemptStatus.SKIPPED, nextBillingDate);

        disableRetryForLastFailedAttemptIfAny(contractId);

        subscriptionContractDetailsDTO.setBillingPolicyInterval(billingInterval.rawValue().toLowerCase());
        subscriptionContractDetailsDTO.setBillingPolicyIntervalCount(billingIntervalCount);
        subscriptionContractDetailsDTO.setNextBillingDate(nextBillingDate);
        subscriptionContractDetailsDTO.setMaxCycles(maxCycles);

        save(subscriptionContractDetailsDTO);

        commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles(), nextBillingId.orElse(null));

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    private List<SellingPlanAnchorInput> buildAnchors(ZonedDateTime nextBillingDate, SellingPlanInterval interval, Optional<Integer> cutoffDays) {
        List<SellingPlanAnchorInput> anchors = new ArrayList<>();

        if(interval.equals(SellingPlanInterval.DAY)){
            return anchors;
        }

        SellingPlanAnchorInput.Builder anchorInputBuilder = SellingPlanAnchorInput.builder();
        int anchorDay = 0;

        if (interval.equals(SellingPlanInterval.WEEK)) {
            anchorInputBuilder.type(SellingPlanAnchorType.WEEKDAY);
            anchorDay = nextBillingDate.getDayOfWeek().getValue();
        } else if (interval.equals(SellingPlanInterval.MONTH)) {
            anchorInputBuilder.type(SellingPlanAnchorType.MONTHDAY);
            anchorDay = nextBillingDate.getDayOfMonth();
        } else if (interval.equals(SellingPlanInterval.YEAR)) {
            anchorInputBuilder.type(SellingPlanAnchorType.YEARDAY);
            anchorInputBuilder.month(nextBillingDate.getMonthValue());
            anchorDay = nextBillingDate.getDayOfMonth();
        }

        anchorInputBuilder.day(anchorDay);
        anchorInputBuilder.cutoffDay(cutoffDays.orElse(null));

        SellingPlanAnchorInput sellingPlanAnchorInput = anchorInputBuilder.build();
        anchors.add(sellingPlanAnchorInput);
        return anchors;
    }

    private void disableRetryForLastFailedAttemptIfAny(Long contractId) {
        /*Optional<SubscriptionBillingAttempt> subscriptionBillingAttempt = subscriptionBillingAttemptRepository.findLatestBillingAttemptByContractId(contractId);
        if (subscriptionBillingAttempt.isPresent() && subscriptionBillingAttempt.get().getStatus().equals(BillingAttemptStatus.FAILURE)) {
            subscriptionBillingAttempt.get().setRetryingNeeded(false);
            subscriptionBillingAttemptRepository.save(subscriptionBillingAttempt.get());
        }*/
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateMaxCycles(Long contractId, String shop, Integer maxCycles, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatusAndShop(contractId, BillingAttemptStatus.SUCCESS, shop);

        if(ObjectUtils.isNotEmpty(maxCycles) && (subscriptionBillingAttemptDTOList.size() + 1) >= maxCycles) {
            throw new BadRequestAlertException("Successful number of orders are greater than or equal to the max cycle", "", "");
        }

        Integer billingIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        SellingPlanInterval billingInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getBillingPolicyInterval().toUpperCase());

        ZonedDateTime nextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Integer minCycles = subscriptionContractDetailsDTO.getMinCycles();

        Integer deliveryIntervalCount = subscriptionContractDetailsDTO.getDeliveryPolicyIntervalCount();
        SellingPlanInterval deliveryInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getDeliveryPolicyInterval().toUpperCase());

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        List<SellingPlanAnchorInput> oldAnchors = getOldAnchors(subscriptionContract.getBillingPolicy().getAnchors());

        Integer oldMaxCycles = subscriptionContractDetailsDTO.getMaxCycles();

        SubscriptionContractQuery.SubscriptionContract subscriptionContractResponseEntity = updateContractWith(
            contractId,
            shop,
            billingIntervalCount,
            billingInterval,
            nextBillingDate,
            shopifyGraphqlClient,
            subscriptionContractDetailsDTO,
            maxCycles,
            minCycles,
            deliveryIntervalCount,
            deliveryInterval,
            false,
            oldAnchors);

        Map<String, Object> map = new HashMap<>();
        map.put("oldMaxCycles", oldMaxCycles);
        map.put("newMaxCycles", maxCycles);
        map.put("reason","Max Cycle Updated");

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SUBSCRIPTION_CONTRACT_UPDATED, ActivityLogStatus.SUCCESS, map);

        return subscriptionContractResponseEntity;
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract splitExistingContract(String shop, Long contractId, List<String> lineIds, boolean isSplitContract, ActivityLogEventSource eventSource) throws Exception {
        log.info("Request to split existing contract for contractId: {}", contractId);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = getShopifySubscriptionContractById(shop, contractId);

        long totalExistingVariants = subscriptionContract.getLines().getEdges().size();

        if (totalExistingVariants <= lineIds.size() && BooleanUtils.isTrue(isSplitContract)) {
            throw new BadRequestAlertException("Provided lineIds cannot be greater than or equal to existing lines", "SubscriptionContractQuery.SubscriptionContract", null);
        }

        List<SubscriptionContractLineDTO> lines = new ArrayList<>();

        subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(s -> lineIds.contains(s.getId()))
            .forEach(oldLine -> {
                SubscriptionContractLineDTO subscriptionContractLineDTO = new SubscriptionContractLineDTO();
                subscriptionContractLineDTO.setQuantity(oldLine.getQuantity());
                subscriptionContractLineDTO.setVariantId(oldLine.getVariantId().get());
                subscriptionContractLineDTO.setCurrentPrice(Double.parseDouble(oldLine.getCurrentPrice().getAmount().toString()));
                if (!CollectionUtils.isNullOrEmpty(oldLine.getCustomAttributes())) {
                    List<AttributeInput> attributeInputList = new ArrayList<>();

                    for (SubscriptionContractQuery.CustomAttribute customAttribute : oldLine.getCustomAttributes()) {
                        AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                        attributeInputBuilder.key(customAttribute.getKey());
                        if (customAttribute.getValue().isPresent()) {
                            attributeInputBuilder.value(customAttribute.getValue().get());
                        }
                        attributeInputList.add(attributeInputBuilder.build());
                    }
                    subscriptionContractLineDTO.setCustomAttributes(attributeInputList);
                }
                lines.add(subscriptionContractLineDTO);
            });
        if (!CollectionUtils.isNullOrEmpty(lines)) {
            SubscriptionContractQuery.SubscriptionContract newSubscriptionContract = createMatchingContract(shop, subscriptionContract, lines, isSplitContract, eventSource);

            if (isSplitContract) {
                for (String lineId : lineIds) {
                    subscriptionContract = subscriptionContractsRemoveLineItem(contractId, shop, lineId, "", true, eventSource);
                }
                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = findByContractId(contractId);
                if (subscriptionContractDetailsDTOOptional.isPresent()) {
                    SubscriptionContractDetailsDTO oldSubscriptionContractDetailsDTO = subscriptionContractDetailsDTOOptional.get();

                    List<SubscriptionProductInfo> products = getProductData(subscriptionContract);
                    String scdJson = OBJECT_MAPPER.writeValueAsString(products);

                    oldSubscriptionContractDetailsDTO.setContractDetailsJSON(scdJson);
                    oldSubscriptionContractDetailsDTO.setOrderAmountUSD(null);
                    save(oldSubscriptionContractDetailsDTO);
                }
            }

            return newSubscriptionContract;
        }
        return null;
    }

    private SubscriptionContractQuery.SubscriptionContract getShopifySubscriptionContractById(String shop, Long contractId) throws Exception {
        log.info("Request to get shopify subscription contract by contractId {}", contractId);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = shopifyGraphqlSubscriptionContractService.getSubscriptionContractRaw(shopifyGraphqlClient, contractId);

        if (subscriptionContractOptional.isEmpty()) {
            throw new BadRequestAlertException("Subscription contract not found for given contract id", "SubscriptionContractQuery.SubscriptionContract", "");
        }

        return subscriptionContractOptional.get();
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract createMatchingContract(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, List<SubscriptionContractLineDTO> lines, boolean isSplitContract, ActivityLogEventSource eventSource) throws Exception {
        log.info("Request to create matching contract for existing contract: {}", subscriptionContract.getId());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractCreateInput.Builder subscriptionContractCreateInputBuilder = SubscriptionContractCreateInput.builder();

        if (subscriptionContract.getCustomer().isEmpty()) {
            throw new BadRequestAlertException("No customer found for Subscription contract", ENTITY_NAME, "createSubscriptionContractError");
        }

        subscriptionContractCreateInputBuilder.customerId(subscriptionContract.getCustomer().get().getId());
        subscriptionContractCreateInputBuilder.currencyCode(subscriptionContract.getDeliveryPrice().getCurrencyCode());
        subscriptionContractCreateInputBuilder.nextBillingDate(subscriptionContract.getNextBillingDate().get());

        SubscriptionDraftInput.Builder contractBuilder = SubscriptionDraftInput.builder();
        if (subscriptionContract.getCustomerPaymentMethod().isEmpty()) {
            throw new BadRequestAlertException("No Payment methods found for the customer", ENTITY_NAME, "createSubscriptionContractError");
        }
        contractBuilder.paymentMethodId(subscriptionContract.getCustomerPaymentMethod().get().getId());
        contractBuilder.status(subscriptionContract.getStatus());

        SubscriptionBillingPolicyInput.Builder billingPolicyBuilder = SubscriptionBillingPolicyInput.builder();
        billingPolicyBuilder.interval(subscriptionContract.getBillingPolicy().getInterval());
        billingPolicyBuilder.intervalCount(subscriptionContract.getBillingPolicy().getIntervalCount());
        billingPolicyBuilder.minCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null));
        billingPolicyBuilder.maxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null));
        contractBuilder.billingPolicy(billingPolicyBuilder.build());

        SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder = SubscriptionDeliveryPolicyInput.builder();
        deliveryPolicyBuilder.interval(subscriptionContract.getDeliveryPolicy().getInterval());
        deliveryPolicyBuilder.intervalCount(subscriptionContract.getDeliveryPolicy().getIntervalCount());
        contractBuilder.deliveryPolicy(deliveryPolicyBuilder.build());

        MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();
        Optional<ShippingAddressModel> address = commonUtils.getContractShippingAddress(subscriptionContract);
        if(address.isPresent()) {
            addressBuilder.firstName(address.map(ShippingAddressModel::getShipping_first_name).orElse(""));
            addressBuilder.lastName(address.map(ShippingAddressModel::getShipping_last_name).orElse(""));
            addressBuilder.address1(address.map(ShippingAddressModel::getShipping_address1).orElse(""));
            addressBuilder.address2(address.map(ShippingAddressModel::getShipping_address2).orElse(""));
            addressBuilder.city(address.map(ShippingAddressModel::getShipping_city).orElse(""));
            addressBuilder.provinceCode(address.map(ShippingAddressModel::getShipping_province_code).orElse(""));
            addressBuilder.countryCode(address.map(model -> CountryCode.safeValueOf(model.getShipping_country_code())).orElse(null));
            addressBuilder.zip(address.map(ShippingAddressModel::getShipping_zip).orElse(""));
            addressBuilder.phone(address.map(ShippingAddressModel::getShipping_phone).orElse(""));
            addressBuilder.company(address.map(ShippingAddressModel::getShipping_company).orElse(""));

            SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();

            if (address.get().getShipping_type().equalsIgnoreCase("SHIPPING")) {
                SubscriptionDeliveryMethodShippingInput.Builder shippingBuilder = SubscriptionDeliveryMethodShippingInput.builder();
                shippingBuilder.address(addressBuilder.build());

                SubscriptionDeliveryMethodShippingOptionInput subscriptionDeliveryMethodShippingOptionInput = null;

                if(ObjectUtils.isNotEmpty(address.get().getShippingOption())) {
                    subscriptionDeliveryMethodShippingOptionInput = SubscriptionDeliveryMethodShippingOptionInput
                        .builder()
                        .title(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getTitle()) ? option.getTitle() : "SHIPPING").orElse("SHIPPING"))
                        .presentmentTitle(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getPresentmentTitle()) ? option.getPresentmentTitle() : "SHIPPING").orElse("SHIPPING"))
                        .description(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getDescription()) ? option.getDescription() : "SHIPPING").orElse("SHIPPING"))
                        .code(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getCode()) ? option.getCode() : "SHIPPING").orElse("SHIPPING"))
                        .build();

                } else {
                    subscriptionDeliveryMethodShippingOptionInput = SubscriptionDeliveryMethodShippingOptionInput
                        .builder()
                        .title("SHIPPING")
                        .presentmentTitle("SHIPPING")
                        .description("SHIPPING")
                        .code("SHIPPING")
                        .build();

                }
                shippingBuilder.shippingOption(subscriptionDeliveryMethodShippingOptionInput);
                deliveryMethodBuilder.shipping(shippingBuilder.build());

            } else if (address.get().getShipping_type().equalsIgnoreCase("LOCAL")) {
                SubscriptionDeliveryMethodLocalDeliveryInput.Builder localBuilder = SubscriptionDeliveryMethodLocalDeliveryInput.builder();
                localBuilder.address(addressBuilder.build());

                SubscriptionDeliveryMethodLocalDeliveryOptionInput localDeliveryOptionInput = null;

                if(ObjectUtils.isNotEmpty(address.get().getShippingOption())) {
                    localDeliveryOptionInput = SubscriptionDeliveryMethodLocalDeliveryOptionInput.builder()
                        .title(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getTitle()) ? option.getTitle() : "LOCAL").orElse("LOCAL"))
                        .presentmentTitle(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getPresentmentTitle()) ? option.getPresentmentTitle() : "LOCAL").orElse("LOCAL"))
                        .description(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getDescription()) ? option.getDescription() : "LOCAL").orElse("LOCAL"))
                        .code(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getCode()) ? option.getCode() : "LOCAL").orElse("LOCAL"))
                        .phone(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getPhone()) ? option.getPhone() : "1111111111").orElse("1111111111"))
                        .build();
                } else {
                    localDeliveryOptionInput = SubscriptionDeliveryMethodLocalDeliveryOptionInput.builder()
                        .title("LOCAL")
                        .presentmentTitle("LOCAL")
                        .description("LOCAL")
                        .code("LOCAL")
                        .phone(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getPhone()) ? option.getPhone() : "1111111111").orElse("1111111111"))
                        .build();
                }

                localBuilder.localDeliveryOption(localDeliveryOptionInput);

                deliveryMethodBuilder.localDelivery(localBuilder.build());

            } else if (address.get().getShipping_type().equalsIgnoreCase("PICK_UP")) {
                SubscriptionDeliveryMethodPickupOptionInput.Builder pickupBuilder = SubscriptionDeliveryMethodPickupOptionInput.builder();
                pickupBuilder.locationId(address.get().getPick_up_location_id());

                if(ObjectUtils.isNotEmpty(address.get().getShippingOption())) {
                        pickupBuilder.title(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getTitle()) ? option.getTitle() : "PICKUP").orElse("PICKUP"))
                        .presentmentTitle(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getPresentmentTitle()) ? option.getPresentmentTitle() : "PICKUP").orElse("PICKUP"))
                        .description(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getDescription()) ? option.getDescription() : "PICKUP").orElse("PICKUP"))
                        .code(address.map(ShippingAddressModel::getShippingOption).map(option -> StringUtils.isNotBlank(option.getCode()) ? option.getCode() : "PICKUP").orElse("PICKUP"));
                } else {
                    pickupBuilder.title("PICK_UP");
                    pickupBuilder.presentmentTitle("PICK_UP");
                    pickupBuilder.description("PICK_UP");
                    pickupBuilder.code("PICK_UP");
                }

                deliveryMethodBuilder.pickup(SubscriptionDeliveryMethodPickupInput.builder().pickupOption(pickupBuilder.build()).build());
            }

            contractBuilder.deliveryMethod(deliveryMethodBuilder.build());
        }

        contractBuilder.deliveryPrice(subscriptionContract.getDeliveryPrice().getAmount());

        if(subscriptionContract.getNote().isPresent()) {
            contractBuilder.note(subscriptionContract.getNote().get());
        }

        List<AttributeInput> attributeInputList = subscriptionContract.getCustomAttributes().stream().map(attr -> {
            AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
            attributeInputBuilder.key(attr.getKey());
            if (attr.getValue().isPresent()) {
                attributeInputBuilder.value(attr.getValue().get());
            }
            return attributeInputBuilder.build();
        }).collect(Collectors.toList());

        contractBuilder.customAttributes(attributeInputList);

        subscriptionContractCreateInputBuilder.contract(contractBuilder.build());

        SubscriptionContractCreateInput subscriptionContractCreateInput = subscriptionContractCreateInputBuilder.build();

        SubscriptionContractCreateMutation subscriptionContractCreateMutation = new SubscriptionContractCreateMutation(subscriptionContractCreateInput);
        Response<Optional<SubscriptionContractCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractCreateMutation);

        String draftId = Objects.requireNonNull(optionalMutationResponse.getData()).flatMap(s -> s.getSubscriptionContractCreate().flatMap(c -> c.getDraft().map(SubscriptionContractCreateMutation.Draft::getId))).orElse(null);

        if (draftId == null) {
            if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                log.info("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors());
                throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
            }

            List<SubscriptionContractCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getSubscriptionContractCreate().map(SubscriptionContractCreateMutation.SubscriptionContractCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.info("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()));
                throw new BadRequestAlertException(userErrors.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
            }
        }

        if (!CollectionUtils.isNullOrEmpty(lines)) {
            for (SubscriptionContractLineDTO line : lines) {
                SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput.builder();
                subscriptionLineInputBuilder.quantity(line.getQuantity());
                String productVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(line.getVariantId());
                subscriptionLineInputBuilder.productVariantId(productVariantId);
                subscriptionLineInputBuilder.customAttributes(line.getCustomAttributes());

                if (line.getCurrentPrice() != null) {
                    subscriptionLineInputBuilder.currentPrice(line.getCurrentPrice());
                } else {
                    int intervalMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();
                    subscriptionLineInputBuilder.currentPrice(line.getUnitPrice() * intervalMultiplier);
                }

                SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder.build();
                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
                Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse1 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                if (!CollectionUtils.isNullOrEmpty(optionalMutationResponse1.getErrors())) {
                    log.info("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage());
                    throw new BadRequestAlertException(optionalMutationResponse1.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                }


                List<SubscriptionDraftLineAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse1.getData()).map(d -> d.getSubscriptionDraftLineAdd().map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.info("userErrors=" + userErrors);
                    throw new BadRequestAlertException(userErrors.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                }
            }
        }
        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);


        if (!CollectionUtils.isNullOrEmpty(optionalMutationResponse2.getErrors())) {
            log.info("optionalMutationResponse2.getErrors()=" + optionalMutationResponse2.getErrors());
            throw new BadRequestAlertException(optionalMutationResponse2.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
        }

        List<SubscriptionDraftCommitMutation.UserError> userErrors1 = Objects.requireNonNull(optionalMutationResponse2.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors1.isEmpty()) {
            log.info("userErrors1=" + userErrors1);
            throw new BadRequestAlertException(userErrors1.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
        }

        String graphContractId = optionalMutationResponse2.getData().flatMap(d -> d.getSubscriptionDraftCommit().flatMap(e -> e.getContract().map(SubscriptionDraftCommitMutation.Contract::getId))).orElse(null);

        long numContractId = ShopifyGraphQLUtils.getSubscriptionContractId(graphContractId);

        Map<String, Object> map = new HashMap<>();
        map.put("newContractId", numContractId);
        commonUtils.writeActivityLog(shop, ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId()), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SPLIT_CONTRACT, ActivityLogStatus.SUCCESS, map);

        return getSubscriptionContractRawInternal(numContractId, shop, shopifyGraphqlClient);
    }

    @Transactional
    @Override
    public void updateNewContractDetails(String shop, Long newContractId, Long oldContractId, boolean isSplitContract, boolean attemptBilling, ActivityLogEventSource eventSource) throws Exception {
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOpt = findByContractId(newContractId);

        if (subscriptionContractDetailsDTOOpt.isPresent()) {
            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsDTOOpt.get();
            if (isSplitContract) {
                subscriptionContractDetailsDTO.setOriginType(SubscriptionOriginType.SPLIT_CONTRACT);
            } else {
                subscriptionContractDetailsDTO.setOriginType(SubscriptionOriginType.SPLIT_ATTEMPT_BILLING);
            }
            subscriptionContractDetailsDTO.setOriginalContractId(oldContractId);
            save(subscriptionContractDetailsDTO);

            if (attemptBilling) {
                billingAttemptService.asyncAttemptBilling(
                    shop,
                    newContractId,
                    subscriptionContractDetailsDTO.getNextBillingDate(),
                    subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(),
                    subscriptionContractDetailsDTO.getBillingPolicyInterval(),
                    subscriptionContractDetailsDTO.getMaxCycles(),
                    eventSource);
            }
        }
    }

    @Override
    public void refreshLineInfo(String shop, Long contractId, Long variantId) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String gqlContractId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

        List<String> mathingLineIds = subscriptionContract.getLines().getEdges().stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(ShopifyGraphQLUtils.getGraphQLVariantId(variantId)))
            .map(SubscriptionContractQuery.Node::getId)
            .collect(Collectors.toList());

        if (!CollectionUtils.isNullOrEmpty(mathingLineIds)) {

            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);


            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            long countOfErrors = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

            if (countOfErrors == 0) {
                // get draft Id from the response
                Optional<String> optionalDraftId = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                    .map(draft -> draft.get().getId());

                if (optionalDraftId.isPresent()) {
                    String draftId = optionalDraftId.get();

                    for (String lineId : mathingLineIds) {
                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                        SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                            .productVariantId(ShopifyGraphQLUtils.getGraphQLVariantId(variantId)).build();


                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                        if (optionalMutationResponse.hasErrors()) {
                            throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                        }

                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                            throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                        }
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                    }
                }
            }
        }
    }

    @Override
    public void maybeRemoveDuplicateContracts(String shop) {
        List<Long> duplicateContractIds = subscriptionContractDetailsRepository.findDuplicateContractIds(shop);
        if (!CollectionUtils.isNullOrEmpty(duplicateContractIds)) {
            for (Long contractId : duplicateContractIds) {
                findByContractId(contractId);
            }
        }
    }

    @Override
    public Long countAllByShopAndSubscriptionTypeIdentifier(String shop, String uniqueIdentifire) {
        return subscriptionContractDetailsRepository.countAllByShopAndSubscriptionTypeIdentifier(shop, uniqueIdentifire);

    }

    @Override
    public void rescheduleOrderFulfillment(String shop, String fulfillmentId, ZonedDateTime deliveryDate) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_STAMP_FORMAT);
        String formattedDate = deliveryDate.format(dateTimeFormatter);

        FulfillmentOrderRescheduleMutation fulfillmentOrderRescheduleMutation = new FulfillmentOrderRescheduleMutation(fulfillmentId, formattedDate);
        Response<Optional<FulfillmentOrderRescheduleMutation.Data>> optionalFulfillmentOrderRescheduleMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(fulfillmentOrderRescheduleMutation);

        if (!org.springframework.util.CollectionUtils.isEmpty(optionalFulfillmentOrderRescheduleMutationResponse.getErrors())) {
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
    }

    @Override
    public Boolean isOverwriteAnchorDay(Long contractId, String shop, ZonedDateTime nextBillingDate, Integer billingIntervalCount, SellingPlanInterval billingInterval) throws Exception {

        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        if (BooleanUtils.isNotTrue(shopInfoDTO.isOverwriteAnchorDay())) {
            return false;
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        if (Objects.nonNull(billingInterval) && Objects.nonNull(billingIntervalCount)) {
            ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(ObjectUtils.isNotEmpty(shopInfoDTO.getIanaTimeZone()) ? shopInfoDTO.getIanaTimeZone() : "UTC"));

            if (ObjectUtils.allNotNull(shopInfoDTO.getZoneOffsetHours(), shopInfoDTO.getZoneOffsetMinutes())) {
                ZoneId zoneId = CommonUtils.getZoneIdFromOffset(shopInfoDTO.getZoneOffsetHours().intValue(), shopInfoDTO.getZoneOffsetMinutes().intValue());
                nowUtc = nowUtc.withZoneSameInstant(zoneId);
            }

            if (ObjectUtils.allNotNull(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute())) {
                nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute()));
            } else if (shopInfoDTO.getRecurringOrderHour() != null && shopInfoDTO.getRecurringOrderMinute() != null) {
                nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getRecurringOrderHour(), shopInfoDTO.getRecurringOrderMinute()));
            }

            nextBillingDate = SubscriptionUtils.getNextBillingDate(billingIntervalCount, billingInterval.rawValue().toLowerCase(), nowUtc);
            if (!Objects.isNull(shopInfoDTO.getLocalOrderDayOfWeek())) {
                nextBillingDate = setNextDayOfWeek(nextBillingDate, shopInfoDTO.getLocalOrderDayOfWeek());
            }

            nextBillingDate = nextBillingDate.withZoneSameInstant(ZoneId.of("UTC"));

            if (BooleanUtils.isFalse(shopInfoDTO.isEnableChangeFromNextBillingDate())) {
                nextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate().withZoneSameInstant(ZoneId.of("UTC"));
            }
        }

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        boolean isIntervalChanged = false;

        SellingPlanInterval oldBillingInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getBillingPolicyInterval().toUpperCase());
        if (Objects.nonNull(billingInterval)) {

            if (!oldBillingInterval.equals(billingInterval)) {
                isIntervalChanged = true;
            }
        }else{
            billingInterval = oldBillingInterval;
        }

        if (BooleanUtils.isTrue(shopInfoDTO.isOverwriteAnchorDay()) && !subscriptionContract.getBillingPolicy().getAnchors().isEmpty()) {
            if (BooleanUtils.isFalse(isIntervalChanged)) {
                int oldAnchorDay = subscriptionContract.getBillingPolicy().getAnchors().get(0).getDay();
                int newAnchorDay = 0;
                if (billingInterval.equals(SellingPlanInterval.WEEK)) {
                    newAnchorDay = nextBillingDate.getDayOfWeek().getValue();
                } else if (billingInterval.equals(SellingPlanInterval.MONTH)) {
                    newAnchorDay = nextBillingDate.getDayOfMonth();
                } else if (billingInterval.equals(SellingPlanInterval.YEAR)) {
                    newAnchorDay = nextBillingDate.getDayOfMonth();
                    Optional<Integer> oldAnchorMonth = subscriptionContract.getBillingPolicy().getAnchors().get(0).getMonth();
                    if (oldAnchorMonth.isPresent() && nextBillingDate.getMonth().getValue() != oldAnchorMonth.get()) {
                        return true;
                    }
                }
                if (newAnchorDay != oldAnchorDay) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public SubscriptionContractQuery.SubscriptionContract updateContractSellingPlanAndPricingPolicy(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, FrequencyInfoDTO sellingPlan) throws Exception {

        Long contractId = ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        try {
            ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

            double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

            List<SubscriptionContractQuery.Node> existingNodes = subscriptionContract.getLines().getEdges().stream()
                .map(SubscriptionContractQuery.Edge::getNode)
                .collect(Collectors.toList());

            Optional<String> optionalContractCurrencyCode = existingNodes.stream().findFirst()
                .map(SubscriptionContractQuery.Node::getCurrentPrice)
                .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
                .map(CurrencyCode::rawValue);

            for (SubscriptionContractQuery.Node node : existingNodes) {
                if (node.getVariantId().isPresent()) {
                    SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder()
                        .sellingPlanId(sellingPlan.getId())
                        .sellingPlanName(sellingPlan.getFrequencyName());

                    int totalCycles = 1;
                    List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
                    totalCycles = totalCycles + subscriptionBillingAttempts.size();

                    List<AppstleCycle> cycles = sellingPlan.getAppstleCycles();

                    if (BooleanUtils.isTrue(sellingPlan.getDiscountEnabled())) {
                        AppstleCycle appstleCycle = new AppstleCycle();
                        appstleCycle.setAfterCycle(sellingPlan.getAfterCycle1());
                        appstleCycle.setValue(sellingPlan.getDiscountOffer());
                        if (sellingPlan.getDiscountType().equals(DiscountTypeUnit.FIXED)) {
                            appstleCycle.setDiscountType(DiscountType.FIXED);
                        } else if (sellingPlan.getDiscountType().equals(DiscountTypeUnit.PERCENTAGE)) {
                            appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                        } else if (sellingPlan.getDiscountType().equals(DiscountTypeUnit.PRICE)) {
                            appstleCycle.setDiscountType(DiscountType.PRICE);
                        }
                        cycles.add(appstleCycle);
                    }

                    if (BooleanUtils.isTrue(sellingPlan.getDiscountEnabled2())) {
                        AppstleCycle appstleCycle = new AppstleCycle();
                        appstleCycle.setAfterCycle(sellingPlan.getAfterCycle2());
                        appstleCycle.setValue(sellingPlan.getDiscountOffer2());
                        if (sellingPlan.getDiscountType2().equals(DiscountTypeUnit.FIXED)) {
                            appstleCycle.setDiscountType(DiscountType.FIXED);
                        } else if (sellingPlan.getDiscountType2().equals(DiscountTypeUnit.PERCENTAGE)) {
                            appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                        } else if (sellingPlan.getDiscountType2().equals(DiscountTypeUnit.PRICE)) {
                            appstleCycle.setDiscountType(DiscountType.PRICE);
                        }
                        cycles.add(appstleCycle);
                    }

                    int finalTotalCycles = totalCycles;
                    Optional<AppstleCycle> optionalAppstleCycle = cycles.stream().sorted(Comparator.comparing(AppstleCycle::getAfterCycle).reversed()).filter(s -> s.getAfterCycle() <= finalTotalCycles).findFirst();

                    if (optionalAppstleCycle.isPresent()) {
                        String productVariantId = node.getVariantId().get();
                        AppstleCycle appstleCycle = optionalAppstleCycle.get();
                        Optional<AppstleCycle> optionalAppstleCycle1 = cycles.stream().sorted(Comparator.comparing(AppstleCycle::getAfterCycle)).filter(s -> s.getAfterCycle() > appstleCycle.getAfterCycle()).findFirst();

                        Object price = null;
                        if (optionalContractCurrencyCode.isPresent() && !optionalContractCurrencyCode.get().equals(shopInfoDTO.getCurrency())) {
                            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(CurrencyUtils.getCountryCode(optionalContractCurrencyCode.get())).build();
                            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(productVariantId, contextualPricingContext);
                            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            price = requireNonNull(productVariantResponse.getData())
                                .flatMap(e -> e.getProductVariant()
                                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                                .orElse(null);
                        } else {
                            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            price = requireNonNull(productVariantResponse.getData()).flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice)).orElse(null);
                        }

                        Double basePrice = Double.parseDouble(price.toString());

                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscountInputList = new ArrayList<>();

                        SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountInput = buildCycleDiscountInput(basePrice, appstleCycle.getAfterCycle(), appstleCycle.getDiscountType().toDiscountTypeUnit(), appstleCycle.getValue(), fulfillmentCountMultiplier);
                        cycleDiscountInputList.add(cycleDiscountInput);
                        double computedPrice = Double.parseDouble(cycleDiscountInput.computedPrice().toString());

                        if (optionalAppstleCycle1.isPresent()) {
                            SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountInput1 = buildCycleDiscountInput(basePrice, optionalAppstleCycle1.get().getAfterCycle(), optionalAppstleCycle1.get().getDiscountType().toDiscountTypeUnit(), optionalAppstleCycle1.get().getValue(), fulfillmentCountMultiplier);
                            cycleDiscountInputList.add(cycleDiscountInput1);
                        }

                        SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder();
                        pricingPolicyInputBuilder.basePrice(basePrice);

                        pricingPolicyInputBuilder.cycleDiscounts(cycleDiscountInputList);

                        subscriptionLineUpdateInputBuilder
                            .pricingPolicy(pricingPolicyInputBuilder.build())
                            .currentPrice(computedPrice);

                    }

                    SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());
                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                    if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                        throw new Exception(optionalSubscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                    }

                    // get draft Id from the response
                    Optional<String> optionalDraftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId());

                    String draftId = optionalDraftId.get();

                    SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, node.getId(), subscriptionLineUpdateInput);
                    Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                    if (optionalMutationResponse.hasErrors()) {
                        throw new Exception(optionalMutationResponse.getErrors().get(0).getMessage());
                    }

                    List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalMutationResponseUserErrors.isEmpty()) {
                        throw new Exception(optionalMutationResponseUserErrors.get(0).getMessage());
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new Exception(optionalDraftCommitResponse.getErrors().get(0).getMessage());
                    }

                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                        throw new Exception(optionalDraftCommitResponseUserErrors.get(0).getMessage());
                    }
                }
            }
            commonUtils.mayBeUpdateShippingPriceAsync(contractId, shop);
            return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
        } catch (Exception e) {
            log.info("Error occurred while recalculating subscription discount for contract id ={} Exception = {}", contractId, ExceptionUtils.getStackTrace(e));
        }

        return subscriptionContract;
    }

    @Override
    public void updatePaymentInfo(Long contractId, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractBriefQuery contractBriefQuery = new SubscriptionContractBriefQuery(ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractBriefQuery.Data>> contractBriefResponse = shopifyGraphqlClient.getOptionalQueryResponse(contractBriefQuery);

        String customerPaymentMethodId = Objects.requireNonNull(contractBriefResponse.getData())
            .flatMap(cbr -> cbr.getSubscriptionContract()
                .flatMap(sc -> sc.getCustomerPaymentMethod().map(SubscriptionContractBriefQuery.CustomerPaymentMethod::getId)))
            .orElse(null);

        CustomerPaymentMethodSendUpdateEmailMutation customerPaymentMethodSendUpdateEmailMutation = new CustomerPaymentMethodSendUpdateEmailMutation(customerPaymentMethodId);
        Response<Optional<CustomerPaymentMethodSendUpdateEmailMutation.Data>> customerPaymentMethodSendUpdateEmailMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodSendUpdateEmailMutation);

        if (customerPaymentMethodSendUpdateEmailMutationResponse.hasErrors()) {
            throw new BadRequestAlertException(customerPaymentMethodSendUpdateEmailMutationResponse.getErrors().get(0).getMessage(), "", "");
        }

        List<CustomerPaymentMethodSendUpdateEmailMutation.UserError> customerPaymentMethodSendUpdateEmailMutationResponseUserErrors =
            requireNonNull(customerPaymentMethodSendUpdateEmailMutationResponse.getData())
                .map(d -> d.getCustomerPaymentMethodSendUpdateEmail()
                    .map(CustomerPaymentMethodSendUpdateEmailMutation.CustomerPaymentMethodSendUpdateEmail::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

        if (!customerPaymentMethodSendUpdateEmailMutationResponseUserErrors.isEmpty()) {
            throw new BadRequestAlertException(customerPaymentMethodSendUpdateEmailMutationResponseUserErrors.get(0).getMessage(), "", "");
        }
    }

    @Override
    public SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateDeliveryInterval(Long contractId, String shop, int deliveryIntervalCount, SellingPlanInterval deliveryInterval, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = findByContractId(contractId).get();

        Integer oldBillingIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        Integer oldDeliveryIntervalCount = subscriptionContractDetailsDTO.getDeliveryPolicyIntervalCount();
        SellingPlanInterval oldDeliveryInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getDeliveryPolicyInterval().toUpperCase());

        Integer billingIntervalCount = subscriptionContractDetailsDTO.getBillingPolicyIntervalCount();
        SellingPlanInterval billingInterval = SellingPlanInterval.safeValueOf(subscriptionContractDetailsDTO.getBillingPolicyInterval().toUpperCase());

        boolean isIntervalChanged = false;
        if (!oldDeliveryInterval.equals(deliveryInterval)) {
            billingInterval = deliveryInterval;
            isIntervalChanged = true;
        }

        ZonedDateTime nextBillingDate = subscriptionContractDetailsDTO.getNextBillingDate().withZoneSameInstant(ZoneId.of("UTC"));

        if (isIntervalChanged) {
            ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

            if (BooleanUtils.isTrue(shopInfoDTO.isEnableChangeFromNextBillingDate())) {
                ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(ObjectUtils.isNotEmpty(shopInfoDTO.getIanaTimeZone()) ? shopInfoDTO.getIanaTimeZone() : "UTC"));

                if (ObjectUtils.allNotNull(shopInfoDTO.getZoneOffsetHours(), shopInfoDTO.getZoneOffsetMinutes())) {
                    ZoneId zoneId = CommonUtils.getZoneIdFromOffset(shopInfoDTO.getZoneOffsetHours().intValue(), shopInfoDTO.getZoneOffsetMinutes().intValue());
                    nowUtc = nowUtc.withZoneSameInstant(zoneId);
                }

                if (ObjectUtils.allNotNull(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute())) {
                    nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute()));
                } else if (shopInfoDTO.getRecurringOrderHour() != null && shopInfoDTO.getRecurringOrderMinute() != null) {
                    nowUtc = nowUtc.with(LocalTime.of(shopInfoDTO.getRecurringOrderHour(), shopInfoDTO.getRecurringOrderMinute()));
                }
                nextBillingDate = SubscriptionUtils.getNextBillingDate(billingIntervalCount, billingInterval.rawValue().toLowerCase(), nowUtc);

                nextBillingDate = nextBillingDate.withZoneSameInstant(ZoneId.of("UTC"));

                if (!Objects.isNull(shopInfoDTO.getLocalOrderDayOfWeek())) {
                    nextBillingDate = setNextDayOfWeek(nextBillingDate, shopInfoDTO.getLocalOrderDayOfWeek());
                }
            }
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        List<SubscriptionContractQuery.Node> subscriptionLineItems = requireNonNull(subscriptionContractQueryResponse.getData())
            .map(d -> d.getSubscriptionContract()
                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                .map(SubscriptionContractQuery.Lines::getEdges)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractQuery.Edge::getNode)
            .collect(Collectors.toList());

        Map<String, Double> subscriptionLineItemPriceByLineId = new HashMap<>();
        for (SubscriptionContractQuery.Node subscriptionLineItem : subscriptionLineItems) {
            double subscriptionItemUnitPrice = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString()) / (oldBillingIntervalCount / oldDeliveryIntervalCount);
            subscriptionLineItemPriceByLineId.put(subscriptionLineItem.getId(), subscriptionItemUnitPrice);
        }

        Integer maxCycles = subscriptionContractDetailsDTO.getMaxCycles();
        Integer minCycles = subscriptionContractDetailsDTO.getMinCycles();

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

        if (subscriptionContractOptional.isEmpty()) {
            throw new Exception("Subscription contract not found");
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

        List<SellingPlanAnchorInput> oldAnchors = getOldAnchors(subscriptionContract.getBillingPolicy().getAnchors());

        SubscriptionContractQuery.SubscriptionContract subscriptionContractResponseEntity = updateContractWith(
            contractId,
            shop,
            billingIntervalCount,
            billingInterval,
            nextBillingDate,
            shopifyGraphqlClient,
            subscriptionContractDetailsDTO,
            maxCycles,
            minCycles,
            deliveryIntervalCount,
            deliveryInterval,
            false,
            oldAnchors);

        Map<String, Object> map = new HashMap<>();
        map.put("deliveryInterval", deliveryInterval);
        map.put("deliveryIntervalCount", deliveryIntervalCount);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DELIVERY_INTERVAL_CHANGE, ActivityLogStatus.SUCCESS, map);
        if (isIntervalChanged) {
            map = new HashMap<>();
            map.put("billingInterval", billingInterval);
            map.put("billingIntervalCount", billingIntervalCount);
            commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.BILLING_INTERVAL_CHANGE, ActivityLogStatus.SUCCESS, map);
        }

        for (Map.Entry<String, Double> entry : subscriptionLineItemPriceByLineId.entrySet()) {
            SubscriptionContractQuery.Node node = subscriptionLineItems.stream().filter(n -> n.getId().equals(entry.getKey())).findFirst().get();
            subscriptionContractUpdateLineItem(contractId, shop, node.getQuantity(), node.getVariantId().orElse(""), node.getId(), node.getSellingPlanName().orElse(null), entry.getValue() * (billingIntervalCount / deliveryIntervalCount), false, "", eventSource);
        }

        return subscriptionContractResponseEntity;
    }

    @Override
    public List<SubscriptionContractDetailsDTO> getTotalSubscriptionsDataCreatedInDateRange(String shop, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return subscriptionContractDetailsRepository.getTotalSubscriptionsDataCreatedInDateRange(shop, fromDate, toDate).stream()
            .map(subscriptionContractDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionContractDetailsDTO> getNewSubscriptions(String shop, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return subscriptionContractDetailsRepository.getNewSubscriptions(shop, fromDate, toDate).stream()
            .map(subscriptionContractDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionContractDetailsDTO> getTotalCancelledSubscriptionsButCreatedBeforeDateRange(String shop, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return subscriptionContractDetailsRepository.getTotalCancelledSubscriptionsButCreatedBeforeDateRange(shop, fromDate, toDate).stream()
            .map(subscriptionContractDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionContractDetailsDTO> getTotalSubscriptionsDataCreatedBefore(String shop, ZonedDateTime fromDate) {
        return subscriptionContractDetailsRepository.getTotalSubscriptionsDataCreatedBefore(shop, fromDate).stream()
            .map(subscriptionContractDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

}


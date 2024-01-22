package com.et.service.impl;


import com.amazonaws.util.CollectionUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionBillingService;
import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.SubscriptionContractDetails;
import com.et.domain.User;
import com.et.domain.enumeration.*;
import com.et.pojo.SubscriptionBillingAttemptDetails;
import com.et.pojo.SubscriptionProductInfo;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.MailgunService;
import com.et.service.ShopInfoService;
import com.et.service.SubscriptionBillingAttemptService;
import com.et.service.SubscriptionContractOneOffService;
import com.et.service.dto.ShopInfoDTO;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import com.et.service.dto.SubscriptionContractOneOffDTO;
import com.et.service.mapper.SubscriptionBillingAttemptMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.AttributeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionBillingAttempt}.
 */
@Service
@Transactional
@Lazy
public class SubscriptionBillingAttemptServiceImpl implements SubscriptionBillingAttemptService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBillingAttemptServiceImpl.class);

    private final SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    private final SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper;

    private final MailgunService mailgunService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopifyGraphqlSubscriptionBillingService shopifyGraphqlSubscriptionBillingService;

    @Autowired
    private SubscriptionContractOneOffService subscriptionContractOneOffService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
   private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    public SubscriptionBillingAttemptServiceImpl(SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository, SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper, MailgunService mailgunService) {
        this.subscriptionBillingAttemptRepository = subscriptionBillingAttemptRepository;
        this.subscriptionBillingAttemptMapper = subscriptionBillingAttemptMapper;
        this.mailgunService = mailgunService;
    }

    @Override
    public SubscriptionBillingAttemptDTO save(SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO) {
        log.debug("Request to save SubscriptionBillingAttempt : {}", subscriptionBillingAttemptDTO);
        SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptMapper.toEntity(subscriptionBillingAttemptDTO);
        subscriptionBillingAttempt = subscriptionBillingAttemptRepository.save(subscriptionBillingAttempt);
        return subscriptionBillingAttemptMapper.toDto(subscriptionBillingAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionBillingAttemptDTO> findAll() {
        log.debug("Request to get all SubscriptionBillingAttempts");
        return subscriptionBillingAttemptRepository.findAll().stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBillingAttemptDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionBillingAttempt : {}", id);
        return subscriptionBillingAttemptRepository.findById(id)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionBillingAttempt : {}", id);
        subscriptionBillingAttemptRepository.deleteById(id);
    }

    @Override
    public Optional<ZonedDateTime> findNextBillingDateForContractId(Long subscriptionContractId) {
        Optional<SubscriptionBillingAttempt> subscriptionBillingAttempt = subscriptionBillingAttemptRepository
            .findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(subscriptionContractId, BillingAttemptStatus.QUEUED);
        return subscriptionBillingAttempt.map(SubscriptionBillingAttempt::getBillingDate);
    }

    @Override
    public Optional<Long> findNextBillingIdForContractId(Long subscriptionContractId) {
        Optional<SubscriptionBillingAttempt> subscriptionBillingAttempt = subscriptionBillingAttemptRepository
            .findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(subscriptionContractId, BillingAttemptStatus.QUEUED);
        return subscriptionBillingAttempt.map(SubscriptionBillingAttempt::getId);
    }


    @Override
    public List<SubscriptionBillingAttemptDTO> getTopOrdersByShop(String shop) {
        return subscriptionBillingAttemptRepository.findTopOrdersByShop(shop).stream()
            .map(subscriptionBillingAttemptMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> getTopOrdersByContractId(Long contractId, String shop) {
        return subscriptionBillingAttemptRepository.findTopOrderByContractId(contractId, shop).stream()
            .map(subscriptionBillingAttemptMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> getTopOrdersByCustomerId(Long customerId, String shop) {
        return subscriptionBillingAttemptRepository.findTopOrdersByCustomerId(customerId, shop).stream()
            .map(subscriptionBillingAttemptMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<SubscriptionBillingAttemptDTO> getPastOrdersByShop(Pageable pageable, String shop) {
        return subscriptionBillingAttemptRepository.findPastOrdersByShop(pageable, shop)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public Page<SubscriptionBillingAttemptDTO> getPastOrdersByContractId(Pageable pageable, Long contractId, String shop) {
        return subscriptionBillingAttemptRepository.findPastOrderByContractId(pageable, contractId, shop)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public Page<SubscriptionBillingAttemptDTO> getPastOrdersByCustomerId(Pageable pageable, Long customerId, String shop) {
        return subscriptionBillingAttemptRepository.findPastOrdersByCustomerId(pageable, customerId, shop)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<SubscriptionBillingAttemptDTO> skipOrder(String shop, Long id) {
        if (subscriptionBillingAttemptRepository.skipOrder(shop, id, BillingAttemptStatus.SKIPPED) > 0) {
            return this.findOne(id);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByStatusAndContractId(BillingAttemptStatus status, Long contractId) {
        int i = subscriptionBillingAttemptRepository.deleteByStatusAndContractId(status.toString(), contractId);
        String a = "b";
    }

    @Override
    public void deleteByContractIdAndStatusAndBillingDateAfter(Long contractId, BillingAttemptStatus status, ZonedDateTime date){
        subscriptionBillingAttemptRepository.deleteByContractIdAndStatusAndBillingDateAfter(contractId, status, date);
    }

    @Async
    @Transactional
    @Override
    public void asyncAttemptBilling(String shop, Long contractId, ZonedDateTime nextBillingDate, Integer billingIntervalCount, String billingInterval, Integer maxCycles, ActivityLogEventSource eventSource) throws Exception {
        log.info("Async request to attempt billing");

        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = findByContractIdAndStatusAndShop(shop, contractId, BillingAttemptStatus.QUEUED);

        if(subscriptionBillingAttemptDTOList.isEmpty()){
            commonUtils.updateQueuedAttempts(nextBillingDate, shop, contractId, billingIntervalCount, billingInterval, maxCycles, null);
            subscriptionBillingAttemptDTOList = findByContractIdAndStatusAndShop(shop, contractId, BillingAttemptStatus.QUEUED);
        }
        Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTO = subscriptionBillingAttemptDTOList.stream().min(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));

        if(subscriptionBillingAttemptDTO.isPresent()){
            attemptBilling(shop, subscriptionBillingAttemptDTO.get().getId(), eventSource);
        }
    }

    @Override
    public Optional<SubscriptionBillingAttemptDTO> attemptBilling(String shop, Long id, ActivityLogEventSource eventSource) throws Exception {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        SubscriptionBillingAttempt billingAttempt = subscriptionBillingAttemptRepository.findById(id).get();
        Long contractId = billingAttempt.getContractId();

        Optional<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);

        if (subscriptionContractDetails.isPresent()) {
            SubscriptionContractDetails contractDetails = subscriptionContractDetails.get();
            String contractStatus = contractDetails.getStatus();
            if (!contractStatus.equalsIgnoreCase("active")) {
                throw new BadRequestAlertException("Billing attempt failed, Contract is not active.", "", "");
            }
        }

        if (billingAttempt.getStatus().equals(BillingAttemptStatus.SKIPPED_INVENTORY_MGMT) || billingAttempt.getStatus().equals(BillingAttemptStatus.SKIPPED_DUNNING_MGMT)) {
            Long count = subscriptionBillingAttemptRepository.countSuccessfulOrdersAfterSpecifiedDate(shop, billingAttempt.getBillingDate(), contractId).orElse(0L);
            if(count > 0) {
                throw new BadRequestAlertException("There is already an order placed after this skipped order","", "");
            }
        }

        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatusIn(shop, contractId, Arrays.asList(BillingAttemptStatus.PROGRESS, BillingAttemptStatus.IMMEDIATE_TRIGGERED, BillingAttemptStatus.REQUESTING));

        if(subscriptionBillingAttempts.size() > 0) {
            throw new BadRequestAlertException("Order placement already in progress", "", "");
        }

        /*ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        Optional<String> billingAttemptOptional = shopifyGraphqlSubscriptionBillingService.createSubscriptionBillingAttempt(shopifyGraphqlClient, shop, contractId);
        if (billingAttemptOptional.isEmpty()) {
            return Optional.empty();
        }*/

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        if (BooleanUtils.isTrue(shopInfoDTO.isEnableInventoryCheck())) {

            List<String> oosVariantIds = commonUtils.checkOutOfStockProduct(contractId, shop, shopInfoDTO);

            if(!CollectionUtils.isNullOrEmpty(oosVariantIds)) {
                String oosVariantId = StringUtils.join(oosVariantIds);

                throw new BadRequestAlertException("Products are out of stock " + oosVariantId, "", "");
            }

        }
        commonUtils.mayBeUpdateShippingPrice(contractId, shop);

        if (BooleanUtils.isFalse(shopInfoDTO.isChangeNextOrderDateOnBillingAttempt())) {
            List<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);

            List<SubscriptionContractOneOffDTO> filteredOneOffDTOList = subscriptionContractOneOffDTOList.stream().filter(dto -> dto.getBillingAttemptId().equals(id)).collect(Collectors.toList());

            SubscriptionBillingAttemptDTO billingAttemptDTO = subscriptionBillingAttemptMapper.toDto(billingAttempt);
            billingAttemptDTO.setId(null);
            billingAttemptDTO.setBillingDate(ZonedDateTime.now(ZoneId.of("UTC")));
            billingAttemptDTO.setStatus(BillingAttemptStatus.QUEUED);
            billingAttemptDTO = save(billingAttemptDTO);
            billingAttempt = subscriptionBillingAttemptRepository.findById(billingAttemptDTO.getId()).get();

            if (!CollectionUtils.isNullOrEmpty(filteredOneOffDTOList)) {
                for (SubscriptionContractOneOffDTO subscriptionContractOneOffDTO : filteredOneOffDTOList) {
                    subscriptionContractOneOffDTO.setBillingAttemptId(billingAttempt.getId());
                    subscriptionContractOneOffService.save(subscriptionContractOneOffDTO);
                }
            }

            billingAttempt.setStatus(BillingAttemptStatus.IMMEDIATE_TRIGGERED);
            billingAttempt = subscriptionBillingAttemptRepository.save(billingAttempt);

        } else {
            billingAttempt.setStatus(BillingAttemptStatus.IMMEDIATE_TRIGGERED);
            billingAttempt = subscriptionBillingAttemptRepository.save(billingAttempt);
        }


        Map<String, Object> map = new HashMap<>();
        map.put("billingAttemptId", billingAttempt.getId());
        map.put("billingDate", billingAttempt.getBillingDate().toString());
        commonUtils.writeActivityLog(shop, billingAttempt.getContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.BILLING_ATTEMPT_TRIGGERED, ActivityLogStatus.SUCCESS, map);
        return Optional.of(subscriptionBillingAttemptMapper.toDto(billingAttempt));
    }

    @Override
    public Optional<SubscriptionBillingAttemptDTO> findById(Long id) {
        return subscriptionBillingAttemptRepository.findById(id)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public void exportUpcomingSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Billing date",
                "Customer Email",
                "Customer Name",
                "Selling Plan",
                "Status"
            };

            tempFile = File.createTempFile("Upcoming-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDetailsList = new ArrayList<>();

            int page = 0;
            int size = 1000;

           Page<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDetailsPage = null;
            do {
                Pageable pageable = PageRequest.of(page, size);
                subscriptionBillingAttemptDetailsPage = getUpcomingOrdersDetailsByShop(pageable, shop, null, BillingAttemptStatus.QUEUED);

                if(!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDetailsPage.getContent())) {
                    subscriptionBillingAttemptDetailsList.addAll(subscriptionBillingAttemptDetailsPage.getContent());
                }
                page++;
            } while (!subscriptionBillingAttemptDetailsPage.isLast());

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDetailsList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDetailsList) {
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        subscriptionBillingAttempt.getBillingDate(),
                        subscriptionContractDetails.getCustomerEmail(),
                        subscriptionContractDetails.getCustomerName(),
                        !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getStatus()
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Upcoming Order Exported", "Check attached csv file for your upcoming order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public Page<SubscriptionBillingAttemptDTO> getPastOrdersByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status) {
        return subscriptionBillingAttemptRepository.getPastOrdersByShop(pageable, shop, contractId, status)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public Page<SubscriptionBillingAttemptDetails> getPastOrdersDetailsByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status) {
        if(status == BillingAttemptStatus.SUCCESS) {
            Page<SubscriptionBillingAttemptDetails> pastOrdersDetailsByShop = subscriptionBillingAttemptRepository.getSuccessfulPastOrdersDetailsByShop(pageable, shop, contractId, status);
            return pastOrdersDetailsByShop;
        } else {
            Page<SubscriptionBillingAttemptDetails> pastOrdersDetailsByShop = subscriptionBillingAttemptRepository.getPastOrdersDetailsByShop(pageable, shop, contractId, status);
            return pastOrdersDetailsByShop;
        }
    }

    @Override
    public Page<SubscriptionBillingAttemptDTO> getUpcomingOrdersByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status) {
        return subscriptionBillingAttemptRepository.getUpcomingOrdersByShop(pageable, shop, contractId, status)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    @Override
    public Page<SubscriptionBillingAttemptDetails> getUpcomingOrdersDetailsByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status) {
        return subscriptionBillingAttemptRepository.getUpcomingOrdersDetails(pageable, shop, contractId, status);
    }

    @Override
    public void exportSuccessSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Billing Date",
                "Customer Email",
                "Customer Name",
                "Selling Plan",
                "Attempt Time",
                "No of Attempt",
                "Order Id",
                "Order Name",
                "Order Amount",
                "Status",
                "Attributes"
            };

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            tempFile = File.createTempFile("Success-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<AttributeInfo> customAttributes = new ArrayList<>();

            Map<Long, List<AttributeInfo>> contractMap = new HashMap<>();

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDTOList = getPastOrdersDetails(contractId, shop, BillingAttemptStatus.SUCCESS);

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDTOList) {
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    if (!contractMap.containsKey(subscriptionContractDetails.getSubscriptionContractId())) {
                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                        if(subscriptionContractQueryResponse.getData().isPresent()) {
                            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractQueryResponse.getData().get().getSubscriptionContract();
                            if(subscriptionContract.isPresent()) {
                                customAttributes = subscriptionContract.get().getCustomAttributes().stream().map(customAttribute1 -> {
                                    AttributeInfo attributeInfo = new AttributeInfo();
                                    attributeInfo.setKey(customAttribute1.getKey());
                                    attributeInfo.setValue(customAttribute1.getValue().orElse(""));
                                    return attributeInfo;
                                }).collect(Collectors.toList());
                                contractMap.put(subscriptionContractDetails.getSubscriptionContractId(), customAttributes);
                            }
                        }
                    }
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        subscriptionBillingAttempt.getBillingDate(),
                        subscriptionContractDetails.getCustomerEmail(),
                        subscriptionContractDetails.getCustomerName(),
                        !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getAttemptTime(),
                        subscriptionBillingAttempt.getAttemptCount(),
                        subscriptionBillingAttempt.getOrderId(),
                        subscriptionBillingAttempt.getOrderName(),
                        subscriptionBillingAttempt.getOrderAmount(),
                        subscriptionBillingAttempt.getStatus(),
                        contractMap.get(subscriptionContractDetails.getSubscriptionContractId())
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Success Past Order Exported", "Check attached csv file for your success past order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public void exportFailedSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Customer Id",
                "Customer Email",
                "Customer Name",
                "Phone Number",
                "Selling Plan",
                "Status",
                "Attempt Time",
                "No of Attempt",
                "Error Message"
            };

            tempFile = File.createTempFile("Failed-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDTOList = getPastOrdersDetails(contractId, shop, BillingAttemptStatus.FAILURE);

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDTOList) {
                    String errorMessage = null;
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    if (subscriptionBillingAttempt.getBillingAttemptResponseMessage() != null) {
                        JSONObject jsonObject = new JSONObject(subscriptionBillingAttempt.getBillingAttemptResponseMessage());
                        errorMessage = jsonObject.getString("error_message");
                    }
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        Optional.ofNullable(subscriptionContractDetails).map(SubscriptionContractDetails::getCustomerId).map(Object::toString).orElse(""),
                        Optional.ofNullable(subscriptionContractDetails).map(SubscriptionContractDetails::getCustomerEmail).orElse(""),
                        Optional.ofNullable(subscriptionContractDetails).map(SubscriptionContractDetails::getCustomerName).orElse(""),
                        Optional.ofNullable(subscriptionContractDetails).map(SubscriptionContractDetails::getPhone).orElse(""),
                    !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getStatus(),
                        subscriptionBillingAttempt.getAttemptTime(),
                        subscriptionBillingAttempt.getAttemptCount(),
                        errorMessage
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Failed Past Order Exported", "Check attached csv file for your failed past order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> findByContractIdAndStatusAndShop(Long contractId, BillingAttemptStatus billingAttemptStatus, String shop) {
        return subscriptionBillingAttemptRepository.findByContractIdAndStatusAndShop(contractId, billingAttemptStatus, shop).stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> findByContractIdAndStatus(Long contractId, BillingAttemptStatus billingAttemptStatus) {
        return subscriptionBillingAttemptRepository.findByContractIdAndStatus(contractId, billingAttemptStatus).stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> findRecentAttemptsWithStatus(String shop, BillingAttemptStatus billingAttemptStatus, ZonedDateTime zonedDateTime) {
        return subscriptionBillingAttemptRepository.findRecentAttemptsWithStatus(shop, billingAttemptStatus, zonedDateTime).stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionBillingAttemptDTO> findByContractIdAndStatusAndShop(String shop, Long contractId, BillingAttemptStatus billingAttemptStatus){
        return subscriptionBillingAttemptRepository.findByContractIdAndStatusAndShop(contractId, billingAttemptStatus, shop).stream()
            .map(subscriptionBillingAttemptMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void exportSkippedSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Billing Date",
                "Customer Email",
                "Customer Name",
                "Selling Plan",
                "Status",
            };

            tempFile = File.createTempFile("Skipped-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDTOList = getPastOrdersDetails(contractId, shop, BillingAttemptStatus.SKIPPED);

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDTOList) {
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        subscriptionBillingAttempt.getBillingDate(),
                        subscriptionContractDetails.getCustomerEmail(),
                        subscriptionContractDetails.getCustomerName(),
                        !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getStatus()
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Skipped Order Exported", "Check attached csv file for your skipped order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public void exportSkippedByDunningMgmtSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Billing Date",
                "Customer Email",
                "Customer Name",
                "Selling Plan",
                "Status"
            };

            tempFile = File.createTempFile("Skipped-By-Dunning-Mgmt-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDTOList = getPastOrdersDetails(contractId, shop, BillingAttemptStatus.SKIPPED_DUNNING_MGMT);

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDTOList) {
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        subscriptionBillingAttempt.getBillingDate(),
                        subscriptionContractDetails.getCustomerEmail(),
                        subscriptionContractDetails.getCustomerName(),
                        !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getStatus()
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Skipped By Dunning Management Order Exported", "Check attached csv file for your skipped order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public void exportSkippedByInventoryMgmtSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId) {
        File tempFile = null;
        try {
            String[] headers = {
                "Contract Id",
                "Billing Date",
                "Customer Email",
                "Customer Name",
                "Selling Plan",
                "Status",
            };

            tempFile = File.createTempFile("Skipped-By-Inventory-Mgmt-Subscriptions-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<SubscriptionBillingAttemptDetails> subscriptionBillingAttemptDTOList = getPastOrdersDetails(contractId, shop, BillingAttemptStatus.SKIPPED_INVENTORY_MGMT);

            if (!CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                for (SubscriptionBillingAttemptDetails subscriptionBillingAttemptDetails : subscriptionBillingAttemptDTOList) {
                    SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptDetails.getSubscriptionBillingAttempt();
                    SubscriptionContractDetails subscriptionContractDetails = subscriptionBillingAttemptDetails.getSubscriptionContractDetails();
                    List<SubscriptionProductInfo> contractJson = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionContractDetails.getContractDetailsJSON());
                    csvPrinter.printRecord(
                        subscriptionBillingAttempt.getContractId(),
                        subscriptionBillingAttempt.getBillingDate(),
                        subscriptionContractDetails.getCustomerEmail(),
                        subscriptionContractDetails.getCustomerName(),
                        !CollectionUtils.isNullOrEmpty(contractJson) && contractJson.size() > 0 ? contractJson.get(0).getSellingPlanName() : "",
                        subscriptionBillingAttempt.getStatus()
                    );
                }
            }
            csvPrinter.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Skipped By Inventory Management Order Exported", "Check attached csv file for your skipped order list details.", "subscription-support@appstle.com", user.getLogin(), emailId);
    }

    @Override
    public void deleteByContractId(Long contractId) {
        subscriptionBillingAttemptRepository.deleteByContractId(contractId);
    }

    @Override
    public void deleteByContractIdIn(Set<Long> contractIds) {
        if (contractIds.isEmpty()) {
            return;
        }
        subscriptionBillingAttemptRepository.deleteByContractIdIn(contractIds);
    }

    @Override
    public List<Long> findDuplicateBillingAttemptContractIds() {
        return subscriptionBillingAttemptRepository.findDuplicateBillingAttempts();
    }

    @Override
    public List<SubscriptionBillingAttemptDTO> findBillingAttemptsInProgressState(ZonedDateTime attemptTime) {
        return subscriptionBillingAttemptRepository.findBillingAttemptsInProgressState(attemptTime).stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void updateUSDOrderAmount(String orderId, Double amount) {
        subscriptionBillingAttemptRepository.updateUSDOrderAmount(orderId, amount);
    }

    private List<SubscriptionBillingAttemptDTO> getPastOrders(Long contractId, String shop, BillingAttemptStatus status) {
        return subscriptionBillingAttemptRepository.getPastOrders(shop, contractId, status).stream()
            .map(subscriptionBillingAttemptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private List<SubscriptionBillingAttemptDetails> getPastOrdersDetails(Long contractId, String shop, BillingAttemptStatus status) {
        return subscriptionBillingAttemptRepository.getPastOrdersDetails(shop, contractId, status);
    }
}

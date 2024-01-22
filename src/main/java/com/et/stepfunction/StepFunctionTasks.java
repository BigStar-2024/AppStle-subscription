package com.et.stepfunction;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.*;
import com.amazonaws.util.CollectionUtils;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.CustomerPayment;
import com.et.domain.EmailTemplateSetting;
import com.et.domain.SubscriptionContractDetails;
import com.et.domain.enumeration.*;
import com.et.pojo.*;
import com.et.pojo.bulkAutomation.*;
import com.et.repository.CustomerPaymentRepository;
import com.et.repository.EmailTemplateSettingRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.*;
import com.et.service.dto.BulkAutomationDTO;
import com.et.service.dto.CsvImportResult;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import com.et.service.dto.SubscriptionContractDetailsDTO;
import com.et.service.mapper.SubscriptionContractDetailsMapper;
import com.et.utils.*;
import com.et.web.rest.MigrationInputRequest;
import com.et.web.rest.MiscellaneousResource;
import com.et.web.rest.SubscriptionContractDetailsResource;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.DeleteLabelKey;
import com.et.web.rest.vm.SyncLabelsInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shopify.java.graphql.client.type.SellingPlanInterval;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.et.constant.Constants.SPRING_PROFILE_JOBS;

@Component
@Profile(SPRING_PROFILE_JOBS)
public class StepFunctionTasks implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(StepFunctionTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AWSStepFunctions awsStepFunctions;

    @Autowired
    private AmazonSQS amazonSqs;

    @Autowired
    private MiscellaneousResource miscellaneousResource;

    @Autowired
    private SubscriptionContractDetailsResource subscriptionContractDetailsResource;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private AwsUtils awsUtils;

    @Autowired
    private BulkAutomationService bulkAutomationService;

    @Autowired
    private ShopLabelUtils shopLabelUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private CommonEmailUtils commonEmailUtils;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    @Autowired
    private SubscriptionContractDetailsMapper subscriptionContractDetailsMapper;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    private ApplicationContext applicationContext;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Scheduled(fixedRate = 1000)
    public void updateSubscriptionShipping() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.UPDATE_SUBSCRIPTION_SHIPPING_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleUpdateSubscriptionShipping(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void exportActivity() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.EXPORT_SUBSCRIPTION_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleExportTask(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void exportActivityLogs() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.EXPORT_ACTIVITY_LOGS_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleActivityLogsExportTask(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void exportMigration() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.SUBSCRIPTION_MIGRATION_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleMigration(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void syncLabels() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.SYNC_LABELS_ACTIVITY_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleSyncLabels(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void deleteKeyFromLabels() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.DELETE_LABEL_KEY_ACTIVITY_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleDeleteKeyFromLabels(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void executeBulkProcess() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.BULK_AUTOMATION_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleBulkAutomation(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void executeSwapAutomation() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.SWAP_AUTOMATION_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleSwapAutomation(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void executeUpdateQueuedAttemptsActivity() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.UPDATE_QUEUE_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }

        handleQueuedAttemptsUpdate(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void executeUpdateStatus() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.SUBSCRIPTION_UPDATE_STATUS_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }
        handleUpdateStatus(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    @Scheduled(fixedRate = 1000)
    public void executeSendEmail() {
        long startTime = System.currentTimeMillis();
        log.info("The time is now {}", dateFormat.format(new Date()));

        GetActivityTaskRequest getActivityTaskRequest = new GetActivityTaskRequest().withActivityArn(AwsUtils.SUBSCRIPTION_SEND_EMAIL_ACT_ARN);
        GetActivityTaskResult activityTaskResult = awsStepFunctions.getActivityTask(getActivityTaskRequest);

        if (activityTaskResult.getTaskToken() == null) {
            log.info("No task token found");
            long endTime = System.currentTimeMillis();
            log.info("timeTaken=" + (endTime - startTime));
            return;
        }
        handleSendEmail(activityTaskResult.getInput(), activityTaskResult.getTaskToken());
    }

    private void handleDeleteKeyFromLabels(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            DeleteLabelKey deleteLabelKey = objectMapper.readValue(jsonInput, DeleteLabelKey.class);

            shopLabelUtils.deleteLabelKey(deleteLabelKey.getKey());

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);

        } finally {
            timer.cancel();
        }
    }

    private void handleSyncLabels(String jsonInput, String taskToken) {

        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            List<SyncLabelsInfo> syncLabelsInfoList = objectMapper.readValue(jsonInput, new TypeReference<List<SyncLabelsInfo>>() {
            });

            shopLabelUtils.syncLabels(syncLabelsInfoList);

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);

        } finally {
            timer.cancel();
        }

    }

    private void handleExportTask(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        String shop = null;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            ExportInputRequest exportInputRequest = objectMapper.readValue(jsonInput, ExportInputRequest.class);
            shop = exportInputRequest.getShop();

            int totalPages = subscriptionContractDetailsService.exportSubscriptionContracts(exportInputRequest);
            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            if (exportInputRequest.getPageNumber() + 1 < totalPages) {
                exportInputRequest.setPageNumber(exportInputRequest.getPageNumber() + 1);
                awsUtils.startExportExecution(exportInputRequest);
            } else {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.EXPORT, null);
            }
        } catch (Exception e) {
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);

            if (StringUtils.isNotBlank(shop)) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.EXPORT, e.getMessage());
            }
        } finally {
            timer.cancel();
        }
    }


    private void handleUpdateSubscriptionShipping(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            UpdateSubscriptionShipping updateSubscriptionShipping = objectMapper.readValue(jsonInput, UpdateSubscriptionShipping.class);
            String shop = updateSubscriptionShipping.getShop();
            Long contractId = updateSubscriptionShipping.getContractId();

            commonUtils.mayBeUpdateShippingPrice(contractId, shop);
        } catch (Exception e) {
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    public void handleActivityLogsExportTask(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        String shop = null;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            ExportActivityLogsRequest activityLogsRequest = objectMapper.readValue(jsonInput, ExportActivityLogsRequest.class);
            shop = activityLogsRequest.getShop();

            int totalPages = activityLogService.exportActivityLogs(activityLogsRequest);
            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            if (activityLogsRequest.getPageNumber() + 1 < totalPages) {
                activityLogsRequest.setPageNumber(activityLogsRequest.getPageNumber() + 1);
                awsUtils.startStepExecution(shop, null, AwsUtils.EXPORT_ACTIVITY_LOGS_SM_ARN, objectMapper.writeValueAsString(activityLogsRequest));
            } else {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.EXPORT_ACTIVITY_LOGS, null);
            }
        } catch (Exception e) {
            log.error("An error occurred while running ActivityLog Export Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);

            if (StringUtils.isNotBlank(shop)) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.EXPORT_ACTIVITY_LOGS, e.getMessage());
            }
        } finally {
            timer.cancel();
        }
    }

    private void handleMigration(String jsonInput, String taskToken) {

        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            MigrationInputRequest migrationInputRequest = objectMapper.readValue(jsonInput, MigrationInputRequest.class);

            Reader subscriptionDataReader = awsUtils.getDataReaderForMigration(migrationInputRequest.getSubscriptionDataS3Key());
            Reader customerDataReader = null;

            String importType = migrationInputRequest.getImportType();
            if (importType.equalsIgnoreCase("stripe")
                || importType.equalsIgnoreCase("paypal")
                || importType.equalsIgnoreCase("authorize_net")) {
                customerDataReader = awsUtils.getDataReaderForMigration(migrationInputRequest.getCustomerDataS3Key());
            }
            MDC.put("shop", migrationInputRequest.getShop());
            CsvImportResult csvImportResult = miscellaneousResource.executeMigration(migrationInputRequest, subscriptionDataReader, customerDataReader);

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString(csvImportResult)).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);
        } catch (final Exception e) {
            log.error("An error occurred while executing migration: {}", ExceptionUtils.getStackTrace(e));
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            MDC.clear();
        }
    }

    private void handleBulkAutomation(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        String shop = null;
        BulkAutomationType bulkAutomationType = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            BulkAutomationActivityRequest bulkAutomationActivityRequest = objectMapper.readValue(jsonInput, BulkAutomationActivityRequest.class);
            bulkAutomationType = bulkAutomationActivityRequest.getBulkAutomationType();
            shop = bulkAutomationActivityRequest.getShop();

            switch (bulkAutomationType) {
                case UPDATE_BILLING_INTERVAL:
                    handleBulkUpdateBillingInterval(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_DELIVERY_PRICE:
                    handleBulkUpdateDeliveryPrice(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_STATUS:
                    handleBulkUpdateStatus(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_DELIVERY_METHOD:
                    handleBulkUpdateDeliveryMethod(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_NEXT_BILLING_DATE_TIME:
                    handleBulkUpdateBillingDateTime(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_NEXT_BILLING_DATE:
                    handleBulkUpdateBillingDate(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_LINE_PRICE:
                    handleBulkUpdateLinePrice(bulkAutomationActivityRequest, taskToken);
                    break;
                case REPLACE_REMOVED_VARIANT:
                    handleBulkReplaceRemovedVariants(bulkAutomationActivityRequest, taskToken);
                    break;
                case REPLACE_PRODUCT:
                    handleBulkReplaceProducts(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_MIN_MAX_CYCLES:
                    handleBulkUpdateMinMaxCycles(bulkAutomationActivityRequest, taskToken);
                    break;
                case HIDE_SUBSCRIPTIONS:
                    handleBulkHideSubscriptions(bulkAutomationActivityRequest, taskToken);
                    break;
                case DELETE_REMOVED_PRODUCT:
                    handleBulkDeleteProduct(bulkAutomationActivityRequest, taskToken);
                    break;
                case ADD_REMOVE_DISCOUNT_CODE:
                    handleBulkAddRemoveDiscountCode(bulkAutomationActivityRequest, taskToken);
                    break;
                case UPDATE_DELIVERY_METHOD_TYPE:
                    handleBulkUpdateDeliveryMethodType(bulkAutomationActivityRequest, taskToken);
                    break;
                case ADD_PRODUCT:
                    handleBulkAddProduct(bulkAutomationActivityRequest, taskToken);
            }
        } catch (Exception e) {
            if (!Objects.isNull(bulkAutomationType)) {
                bulkAutomationService.stopBulkAutomationProcess(shop, bulkAutomationType, e.getMessage());
            }
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    private void handleSwapAutomation(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            ReplaceVariantsInfo replaceVariantsInfo = objectMapper.readValue(jsonInput, ReplaceVariantsInfo.class);

            Map<Long, Integer> newVariants = new HashMap<>();
            for (Long variantId : replaceVariantsInfo.getNewVariantIdList()) {
                newVariants.put(variantId, null);
            }

            subscriptionContractDetailsService.replaceVariantsV3WithRetry(
                replaceVariantsInfo.getShop(),
                replaceVariantsInfo.getContractId(),
                replaceVariantsInfo.getOldVariantIdList(),
                newVariants,
                replaceVariantsInfo.getOldLineId(),
                replaceVariantsInfo.getCarryForwardDiscount(),
                replaceVariantsInfo.getEventSource(),
                2
            );

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running resolveIndex Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    private void handleBulkUpdateBillingInterval(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {
            final int billingIntervalCount = Integer.parseInt(processAttributes.get("intervalCount").toString());
            final SellingPlanInterval billingInterval = SellingPlanInterval.safeValueOf(processAttributes.get("interval").toString());

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shop, billingIntervalCount, billingInterval, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_BILLING_INTERVAL, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateBillingIntervalRequest updateBillingIntervalRequest = new UpdateBillingIntervalRequest(shop, contractIds.get(i), billingIntervalCount, billingInterval);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateBillingIntervalRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_INTERVAL_SQS_URL, shop, objectMapper.writeValueAsString(updateBillingIntervalRequest), "bulk-update-billing-intervals");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_BILLING_INTERVAL);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("No contract Ids found");
        }
    }

    private void handleBulkUpdateDeliveryPrice(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {
            final Double deliveryPrice = Double.parseDouble(processAttributes.get("deliveryPrice").toString());

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.updateDeliveryPriceBySubscriptionContractId(shop, contractId, deliveryPrice, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_DELIVERY_PRICE, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateDeliveryPriceRequest updateDeliveryPriceRequest = new UpdateDeliveryPriceRequest(shop, contractIds.get(i), deliveryPrice);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateDeliveryPriceRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_DELIVERY_PRICE_SQS_URL, shop, objectMapper.writeValueAsString(updateDeliveryPriceRequest), "bulk-update-delivery-price");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_PRICE);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateStatus(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final String status = processAttributes.get("status").toString();

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.subscriptionContractUpdateStatus(contractId, status, shop, null, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_STATUS, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_STATUS, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_STATUS, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateSubscriptionStatusRequest updateSubscriptionStatusRequest = new UpdateSubscriptionStatusRequest(shop, contractIds.get(i), status);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateSubscriptionStatusRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_STATUS_SQS_URL, shop, objectMapper.writeValueAsString(updateSubscriptionStatusRequest), "bulk-update-status");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_STATUS, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_STATUS, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_STATUS).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_STATUS);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateDeliveryMethod(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final String deliveryMethodName = processAttributes.get("deliveryMethodName").toString();

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.updateDeliveryMethod(shop, contractId, deliveryMethodName, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_DELIVERY_METHOD, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateDeliveryMethodRequest updateDeliveryMethodRequest = new UpdateDeliveryMethodRequest(shop, contractIds.get(i), deliveryMethodName);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateDeliveryMethodRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_DELIVERY_METHOD_SQS_URL, shop, objectMapper.writeValueAsString(updateDeliveryMethodRequest), "bulk-update-delivery-method");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_METHOD);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateBillingDateTime(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final Integer hour = Integer.parseInt(processAttributes.get("hour").toString());
            final Integer minute = Integer.parseInt(processAttributes.get("minute").toString());
            final Integer zonedOffsetHours = Integer.parseInt(processAttributes.get("zonedOffsetHours").toString());

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.updateNextBillingDateTime(shop, contractId, hour, minute, zonedOffsetHours);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateNextBillingDateTimeRequest updateNextBillingDateTimeRequest = new UpdateNextBillingDateTimeRequest(shop, contractIds.get(i), hour, minute, zonedOffsetHours);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateNextBillingDateTimeRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_NEXT_BILLING_DATE_TIME_SQS_URL, shop, objectMapper.writeValueAsString(updateNextBillingDateTimeRequest), "bulk-update-next-billing-date-time");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateBillingDate(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final ZonedDateTime nextBillingDate = ZonedDateTime.parse(processAttributes.get("nextBillingDate").toString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shop, nextBillingDate, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateNextBillingDateRequest updateNextBillingDateRequest = new UpdateNextBillingDateRequest(shop, contractIds.get(i), nextBillingDate);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateNextBillingDateRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.BULK_UPDATE_NEXT_BILLING_DATE_SQS_URL, shop, objectMapper.writeValueAsString(updateNextBillingDateRequest), "bulk-update-next-billing-date");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_NEXT_BILLING_DATE);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateLinePrice(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        final Long variantId = Long.parseLong(processAttributes.get("variantId").toString());
        final Double price = Double.parseDouble(processAttributes.get("price").toString());

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<SubscriptionContractDetailsDTO> contractsPage = subscriptionContractDetailsService.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId), pageable);
            contractIds = contractsPage.getContent().stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            isLastPage = contractsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.updateLinePrice(shop, contractId, variantId, price, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_LINE_PRICE, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_LINE_PRICE, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_LINE_PRICE, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateLinePriceRequest updateLinePriceRequest = new UpdateLinePriceRequest(shop, contractIds.get(i), variantId, price);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateLinePriceRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.UPDATE_LINE_PRICE_SQS_URL, shop, objectMapper.writeValueAsString(updateLinePriceRequest), "update-line-price");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_LINE_PRICE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_LINE_PRICE, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_LINE_PRICE).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_LINE_PRICE);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkReplaceRemovedVariants(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final String title = processAttributes.get("title").toString();
            final Long newVariantId = Long.parseLong(processAttributes.get("newVariantId").toString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.replaceRemovedVariants(shop, contractId, title, newVariantId, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.REPLACE_REMOVED_VARIANT, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        ReplaceRemovedVariantRequest replaceRemovedVariantRequest = new ReplaceRemovedVariantRequest(shop, contractIds.get(i), title, newVariantId);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            replaceRemovedVariantRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.REPLACE_REMOVED_VARIANT_SQS_URL, shop, objectMapper.writeValueAsString(replaceRemovedVariantRequest), "replace-removed-variant");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.REPLACE_REMOVED_VARIANT);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkReplaceProducts(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            //Todo: get contracts by old variant ids
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            final List<Long> newVariantIds = objectMapper.readValue(processAttributes.get("newVariantIds").toString(), new TypeReference<List<Long>>() {
            });
            final List<Long> oldVariantIds = objectMapper.readValue(processAttributes.get("oldVariantIds").toString(), new TypeReference<List<Long>>() {
            });

            //TODO: Remove the else block if everything works smoothly
            if (true) {

                Map<Long, Integer> newVariants = new HashMap<>();
                for(Long variantId : newVariantIds){
                    newVariants.put(variantId, null);
                }

                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.replaceVariantsV3WithRetry(
                            shop,
                            contractId,
                            oldVariantIds,
                            newVariants,
                            null,
                            true,
                            ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION,
                            2);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.REPLACE_PRODUCT, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_PRODUCT, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        ReplaceProductRequest replaceProductRequest = new ReplaceProductRequest(shop, contractIds.get(i), newVariantIds, oldVariantIds);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            replaceProductRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.REPLACE_PRODUCT_SQS_URL, shop, objectMapper.writeValueAsString(replaceProductRequest), "replace-product");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_PRODUCT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.REPLACE_PRODUCT).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.REPLACE_PRODUCT);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkUpdateMinMaxCycles(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        final Integer minCycles = processAttributes.get("minCycles") != null ? Integer.parseInt(processAttributes.get("minCycles").toString()) : null;
        final Integer maxCycles = processAttributes.get("maxCycles") != null ? Integer.parseInt(processAttributes.get("maxCycles").toString()) : null;
        final Boolean updateMinCycles = Boolean.parseBoolean(processAttributes.get("updateMinCycles").toString());
        final Boolean updateMaxCycles = Boolean.parseBoolean(processAttributes.get("updateMaxCycles").toString());

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        if(BooleanUtils.isTrue(updateMinCycles)) {
                            subscriptionContractDetailsService.subscriptionContractUpdateMinCycles(contractId, shop, minCycles, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                        }
                        if(BooleanUtils.isTrue(updateMaxCycles)) {
                            subscriptionContractDetailsService.subscriptionContractsUpdateMaxCycles(contractId, shop, maxCycles, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                        }
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_MIN_MAX_CYCLES, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        UpdateMinMaxCyclesRequest updateMinMaxCyclesRequest = new UpdateMinMaxCyclesRequest(shop, contractIds.get(i), minCycles, maxCycles, updateMinCycles, updateMaxCycles);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            updateMinMaxCyclesRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.UPDATE_MIN_MAX_CYCLES_SQS_URL, shop, objectMapper.writeValueAsString(updateMinMaxCyclesRequest), "update-min-max-cycles");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_MIN_MAX_CYCLES);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkHideSubscriptions(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        subscriptionContractDetailsService.hideSubscription(shop, contractId, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.HIDE_SUBSCRIPTIONS, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        HideSubscriptionsRequest hideSubscriptionsRequest = new HideSubscriptionsRequest(shop, contractIds.get(i));
                        ;
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            hideSubscriptionsRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.HIDE_SUBSCRIPTION_SQS_URL, shop, objectMapper.writeValueAsString(hideSubscriptionsRequest), "bulk-hide-subscriptions");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.HIDE_SUBSCRIPTIONS);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkDeleteProduct(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;
        Long variantId = null;
        Boolean deleteRemovedProductsFromShopify = false;

        if (processAttributes.containsKey("variantId") && Objects.nonNull(processAttributes.get("variantId"))) {
            variantId = Long.parseLong(processAttributes.get("variantId").toString());
        }
        if (processAttributes.containsKey("deleteRemovedProductsFromShopify") && Objects.nonNull(processAttributes.get("deleteRemovedProductsFromShopify"))) {
            deleteRemovedProductsFromShopify = Boolean.parseBoolean(processAttributes.get("deleteRemovedProductsFromShopify").toString());
        }

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            if (BooleanUtils.isTrue(deleteRemovedProductsFromShopify)) {
                Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
                contractIds = contractIdsPage.getContent();
                isLastPage = contractIdsPage.isLast();
            } else if (Objects.nonNull(variantId)) {
                Page<SubscriptionContractDetailsDTO> contractIdsPage = subscriptionContractDetailsService.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId), pageable);
                contractIds = contractIdsPage.getContent().stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
                isLastPage = contractIdsPage.isLast();
            }
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            //TODO: Remove the else block if everything works smoothly
            if (true) {
                for (Long contractId : contractIds) {
                    try {
                        if (variantId != null) {
                            subscriptionContractDetailsService.subscriptionContractsRemoveLineItem(shop, contractId, variantId);
                        }

                        if (BooleanUtils.isTrue(deleteRemovedProductsFromShopify)) {
                            subscriptionContractDetailsService.subscriptionContractsRemoveDeletedLineItem(shop, contractId);
                        }
                    } catch (Exception e) {
                        log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.DELETE_REMOVED_PRODUCT, contractId, e.getMessage());
                        JSONObject errorObject = new JSONObject();
                        errorObject.put("contractId", contractId);
                        errorObject.put("message", e.getMessage());
                        bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, errorObject.toString());
                    }
                }
                if (isLastPage) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, null);
                }
            } else {
                for (int i = 0; i < contractIds.size(); i++) {
                    try {
                        DeleteProductRequest deleteProductRequest = new DeleteProductRequest(shop, contractIds.get(i), variantId, deleteRemovedProductsFromShopify);
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            deleteProductRequest.setLast(true);
                        }
                        awsUtils.send(AwsUtils.DELETE_REMOVED_PRODUCT_SQS_URL, shop, objectMapper.writeValueAsString(deleteProductRequest), "delete-removed-product");
                    } catch (Exception e) {
                        if (isLastPage && (i == contractIds.size() - 1)) {
                            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        } else {
                            bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, "Contract Id:" + contractIds.get(i) + " error: " + e.getMessage());
                        }
                        log.error("An error occurred while sending SQS message for contract: {}, error: {}", contractIds.get(i), ExceptionUtils.getStackTrace(e));
                    }
                }
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.DELETE_REMOVED_PRODUCT);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkAddRemoveDiscountCode(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;
        Boolean addDiscount = true;
        String discountCode = null;

        if (processAttributes.containsKey("addDiscount") && Objects.nonNull(processAttributes.get("addDiscount"))) {
            addDiscount = Boolean.parseBoolean(processAttributes.get("addDiscount").toString());
        }
        if (processAttributes.containsKey("discountCode") && Objects.nonNull(processAttributes.get("discountCode"))) {
            discountCode = processAttributes.get("discountCode").toString();
        }

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            if (BooleanUtils.isTrue(addDiscount)) {
                Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
                contractIds = contractIdsPage.getContent();
                isLastPage = contractIdsPage.isLast();
            }
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            for (Long contractId : contractIds) {
                try {
                    if (BooleanUtils.isTrue(addDiscount)) {
                        subscriptionContractDetailsResource.applyDiscountCodeInfo(contractId, shop, discountCode, null, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } else {
                        subscriptionContractDetailsService.removeContractDiscountByCode(contractId, shop, discountCode, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    }
                } catch (Exception e) {
                    log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE, contractId, e.getMessage());
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("contractId", contractId);
                    errorObject.put("message", e.getMessage());
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE, errorObject.toString());
                }
            }
            if (isLastPage) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE, null);
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleQueuedAttemptsUpdate(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            UpdateQueuedAttemptsRequest updateQueuedAttemptsRequest = objectMapper.readValue(jsonInput, UpdateQueuedAttemptsRequest.class);

            commonUtils.updateQueuedAttempts(
                updateQueuedAttemptsRequest.getShop(),
                updateQueuedAttemptsRequest.getContractId()
            );

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running update queued attempts Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    private void handleUpdateStatus(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            SubscriptionUpdateStatusRequest subscriptionUpdateStatusRequest = objectMapper.readValue(jsonInput, SubscriptionUpdateStatusRequest.class);

            if (Objects.isNull(subscriptionUpdateStatusRequest.getContractId())) {
                throw new BadRequestAlertException("Contract Id cannot be null or empty", "", "");
            }

            Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.getSubscriptionByContractId(subscriptionUpdateStatusRequest.getContractId());
            if (subscriptionContractDetailsDTO.isEmpty()) {
                throw new BadRequestAlertException("No contract found for ID:" + subscriptionUpdateStatusRequest.getContractId(), "", "");
            }

            if (!subscriptionUpdateStatusRequest.getStatus().equalsIgnoreCase("active") ||
                (subscriptionUpdateStatusRequest.getStatus().equalsIgnoreCase("active") && subscriptionContractDetailsDTO.get().getStatus().equalsIgnoreCase("paused"))) {

                subscriptionContractDetailsService.subscriptionContractUpdateStatus(
                    subscriptionUpdateStatusRequest.getContractId(),
                    subscriptionUpdateStatusRequest.getStatus().toUpperCase(),
                    subscriptionUpdateStatusRequest.getShop(),
                    null,
                    ActivityLogEventSource.SYSTEM_EVENT
                );
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running update queued attempts Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    private void handleSendEmail(String jsonInput, String taskToken) {
        HeartbeatTask heartbeatTask = new HeartbeatTask(taskToken, awsStepFunctions);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatTask, 0, 50000);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            EmailRequestInfo emailRequestInfo = objectMapper.readValue(jsonInput, EmailRequestInfo.class);

            log.info("Handle send email: {}", emailRequestInfo);

            Optional<SubscriptionContractDetailsDTO> optionalSubscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(emailRequestInfo.getContractId());
            Optional<SubscriptionContractDetails> optionalSubscriptionContractDetails = optionalSubscriptionContractDetailsDTO.map(subscriptionContractDetailsMapper::toEntity);

            SubscriptionContractDetails subscriptionContractDetails = optionalSubscriptionContractDetails.get();

            if (!performPreProcessingChecks(emailRequestInfo, subscriptionContractDetails)) {
                log.info("Pre processing checks failed for email request: {}", emailRequestInfo);
                SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
                awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);
                return;
            }

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(emailRequestInfo.getShop());
            ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(emailRequestInfo.getShop());


            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                shopifyAPI,
                subscriptionContractDetails,
                emailRequestInfo.getEmailSettingType(),
                emailRequestInfo.getAdditionalAttributes(),
                emailRequestInfo.getOrderItemList(),
                emailRequestInfo.isAddActivityLog()
            );

            performPostProcessingChecks(emailRequestInfo, subscriptionContractDetails);

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

        } catch (Exception e) {
            log.error("An error occurred while running send email queued attempts Task. ex=" + ExceptionUtils.getStackTrace(e), e);
            SendTaskFailureRequest sendTaskFailureRequest = new SendTaskFailureRequest().withCause(ExceptionUtils.getStackTrace(e)).withError("ERROR").withTaskToken(taskToken);
            awsStepFunctions.sendTaskFailure(sendTaskFailureRequest);
        } finally {
            timer.cancel();
        }
    }

    private boolean performPreProcessingChecks(EmailRequestInfo emailRequestInfo, SubscriptionContractDetails subscriptionContractDetails) {

        String shop = emailRequestInfo.getShop();

        if (emailRequestInfo.getEmailSettingType().equals(EmailSettingType.SUBSCRIPTION_CREATED)) {
            EmailTemplateSetting emailTemplateSetting = emailTemplateSettingRepository.findByShopAndEmailSettingType(shop, EmailSettingType.SUBSCRIPTION_CREATED).get();

            if (emailTemplateSetting.isSendEmailDisabled() || subscriptionContractDetails.getImportedId() != null) {
                log.info("Create email not sent. Email template disabled or imported contract");
                subscriptionContractDetails.setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus.EMAIL_SETTINGS_DISABLED);
                subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                return false;
            }

            List<CustomerPayment> customerPaymentList = customerPaymentRepository.findByCustomerId(subscriptionContractDetails.getCustomerId());

            if (customerPaymentList.isEmpty()) {
                log.info("Create email not sent. Customer Payment details not found");
                subscriptionContractDetails.setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus.CUSTOMER_PAYMENT_EMPTY);
                subscriptionContractDetails.setSubscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatus.CUSTOMER_PAYMENT_EMPTY);
                subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                return false;
            }
        } else if (emailRequestInfo.getEmailSettingType().equals(EmailSettingType.UPCOMING_ORDER)) {
            List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatus(subscriptionContractDetails.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);

            if(CollectionUtils.isNullOrEmpty(subscriptionBillingAttemptDTOList)) {
                Map<String, Object> map = new HashMap<>();
                map.put("reason", "Order max cycles reached");
                commonUtils.writeActivityLog(shop, subscriptionContractDetails.getSubscriptionContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.SEND_UPCOMING_ORDER_EMAIL, ActivityLogStatus.FAILURE, map);
                return false;
            }
        }

        return true;
    }

    private void performPostProcessingChecks(EmailRequestInfo emailRequestInfo, SubscriptionContractDetails subscriptionContractDetails) {
        if (emailRequestInfo.getEmailSettingType().equals(EmailSettingType.SUBSCRIPTION_CREATED)) {
            subscriptionContractDetails.setSubscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatus.PHONE_NUMBER_EMPTY);

            subscriptionContractDetails.setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus.SENT);
            subscriptionContractDetailsRepository.save(subscriptionContractDetails);
        }
    }

    private void handleBulkUpdateDeliveryMethodType(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            final DeliveryMethodType fromDeliveryType = DeliveryMethodType.valueOf(processAttributes.get("fromDeliveryType").toString());
            final DeliveryMethodType toDeliveryType = DeliveryMethodType.valueOf(processAttributes.get("toDeliveryType").toString());

            for (Long contractId : contractIds) {
                try {
                    subscriptionContractDetailsService.updateDeliveryMethodType(contractId, shop, fromDeliveryType, toDeliveryType);
                } catch (Exception e) {
                    log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE, contractId, e.getMessage());
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("contractId", contractId);
                    errorObject.put("message", e.getMessage());
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE, errorObject.toString());
                }
            }
            if (isLastPage) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE, null);
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    private void handleBulkAddProduct(BulkAutomationActivityRequest bulkAutomationActivityRequest, String taskToken) throws Exception {

        String shop = bulkAutomationActivityRequest.getShop();
        int pageSize = bulkAutomationActivityRequest.getPageSize();
        int pageNumber = bulkAutomationActivityRequest.getPageNumber();
        List<Long> contractIds = bulkAutomationActivityRequest.getContractIds();
        Boolean allSubscriptions = bulkAutomationActivityRequest.getAllSubscriptions();
        Map<String, Object> processAttributes = bulkAutomationActivityRequest.getProcessAttributes();

        boolean isLastPage = true;
        String productType = null;
        Double price = null;
        Boolean oneTimeProduct = false;
        String productVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(Long.parseLong(processAttributes.get("variantId").toString()));

        if (processAttributes.containsKey("price") && Objects.nonNull(processAttributes.get("price"))) {
            price = Double.parseDouble(processAttributes.get("price").toString());
        }
        if (processAttributes.containsKey("productType") && Objects.nonNull(processAttributes.get("productType"))) {
            productType = processAttributes.get("productType").toString();
        }

        if(StringUtils.equals(productType, "FREE_PRODUCT")) {
            price = 0D;
        }

        if(StringUtils.equals(productType, "FREE_PRODUCT") || (StringUtils.equals(productType, "ONE_TIME_PRODUCT"))) {
            oneTimeProduct = true;
        }

        if (BooleanUtils.isTrue(allSubscriptions)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Long> contractIdsPage = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop, pageable);
            contractIds = contractIdsPage.getContent();
            isLastPage = contractIdsPage.isLast();
        }

        if (!CollectionUtils.isNullOrEmpty(contractIds)) {

            for (Long contractId : contractIds) {
                try {
                    if(Objects.isNull(price)) {
                        subscriptionContractDetailsService.subscriptionContractsAddLineItem(contractId, shop, 1, productVariantId, oneTimeProduct, null, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } else {
                        subscriptionContractDetailsService.subscriptionContractAddLineItem(contractId, shop, 1, productVariantId, price, oneTimeProduct, null, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    }
                } catch (Exception e) {
                    log.error("Some error occurred while running Bulk Automation {} for contract {} message: {}", BulkAutomationType.ADD_PRODUCT, contractId, e.getMessage());
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("contractId", contractId);
                    errorObject.put("message", e.getMessage());
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.ADD_PRODUCT, errorObject.toString());
                }
            }
            if (isLastPage) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.ADD_PRODUCT, null);
            }

            SendTaskSuccessRequest sendTaskSuccessRequest = new SendTaskSuccessRequest().withOutput(OBJECT_MAPPER.writeValueAsString("")).withTaskToken(taskToken);
            awsStepFunctions.sendTaskSuccess(sendTaskSuccessRequest);

            BulkAutomationDTO bulkAutomationDTO = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.ADD_PRODUCT).get();
            if (!isLastPage && BooleanUtils.isTrue(bulkAutomationDTO.isRunning())) {
                bulkAutomationActivityRequest.setPageNumber(pageNumber + 1);
                StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.ADD_PRODUCT);
                bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
                bulkAutomationService.save(bulkAutomationDTO);
            }
        } else {
            throw new RuntimeException("ContractIds cannot be empty");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

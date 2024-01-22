package com.et.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.amazonaws.services.stepfunctions.model.StopExecutionRequest;
import com.amazonaws.util.IOUtils;
import com.et.domain.enumeration.BulkAutomationType;
import com.et.pojo.ExportInputRequest;
import com.et.pojo.bulkAutomation.BulkAutomationActivityRequest;
import com.et.web.rest.MigrationInputRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.et.constant.AWSConstants.ACCESS_KEY;
import static com.et.constant.AWSConstants.SECRET_KEY;
@Component
public class AwsUtils {

    @Autowired
    private AmazonSQSBufferedAsyncClient sqs;

    @Autowired
    private AmazonS3 amazonS3;


    @Autowired
    private AWSStepFunctions awsStepFunctions;

    public static final String BULK_UPDATE_INTERVAL_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-interval-queue.fifo";

    public static final String BULK_UPDATE_INTERVAL_SQS = "subscription-bulk-update-interval-queue.fifo";

    public static final String BULK_UPDATE_DELIVERY_PRICE_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-delivery-price.fifo";

    public static final String BULK_UPDATE_DELIVERY_PRICE_SQS = "subscription-bulk-update-delivery-price.fifo";

    public static final String BULK_UPDATE_STATUS_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-status.fifo";

    public static final String BULK_UPDATE_STATUS_SQS = "subscription-bulk-update-status.fifo";

    public static final String BULK_UPDATE_DELIVERY_METHOD_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-delivery-method.fifo";

    public static final String BULK_UPDATE_DELIVERY_METHOD_SQS = "subscription-bulk-update-delivery-method.fifo";

    public static final String HIDE_SUBSCRIPTION_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-hide.fifo";

    public static final String HIDE_SUBSCRIPTION_SQS = "subscription-bulk-hide.fifo";

    public static final String BULK_UPDATE_NEXT_BILLING_DATE_TIME_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-next-billing-date-time.fifo";

    public static final String BULK_UPDATE_NEXT_BILLING_DATE_TIME_SQS = "subscription-bulk-update-next-billing-date-time.fifo";

    public static final String BULK_UPDATE_NEXT_BILLING_DATE_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-next-billing-date.fifo";

    public static final String BULK_UPDATE_NEXT_BILLING_DATE_SQS = "subscription-bulk-update-next-billing-date.fifo";

    public static final String REPLACE_REMOVED_VARIANT_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-replace-removed-variant.fifo";

    public static final String REPLACE_REMOVED_VARIANT_SQS = "subscription-bulk-replace-removed-variant.fifo";

    public static final String UPDATE_LINE_PRICE_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-line-item.fifo";

    public static final String UPDATE_LINE_PRICE_SQS = "subscription-bulk-update-line-item.fifo";

    public static final String REPLACE_PRODUCT_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-replace-line-item.fifo";

    public static final String REPLACE_PRODUCT_SQS = "subscription-bulk-replace-line-item.fifo";

    public static final String DELETE_REMOVED_PRODUCT_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-delete-removed-product.fifo";

    public static final String DELETE_REMOVED_PRODUCT_SQS = "subscription-bulk-delete-removed-product.fifo";

    public static final String UPDATE_MIN_MAX_CYCLES_SQS_URL = "https://sqs.us-west-1.amazonaws.com/114479152947/subscription-bulk-update-min-max-cycles.fifo";

    public static final String UPDATE_MIN_MAX_CYCLES_SQS = "subscription-bulk-update-min-max-cycles.fifo";

    public static final String EXPORT_SUBSCRIPTION_SM_ARN = "arn:aws:states:us-west-1:114479152947:stateMachine:export_subscriptions";

    public static final String EXPORT_ACTIVITY_LOGS_SM_ARN = "arn:aws:states:us-west-1:114479152947:stateMachine:export_activity_logs";

    public static final String MIGRATION_SUBSCRIPTION_SM_ARN = "arn:aws:states:us-west-1:114479152947:stateMachine:migration_subscriptions";

    public static final String EXPORT_SUBSCRIPTION_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:export_subscriptions";

    public static final String UPDATE_SUBSCRIPTION_SHIPPING_ARN = "arn:aws:states:us-west-1:114479152947:activity:update_subscription_shipping";

    public static final String EXPORT_ACTIVITY_LOGS_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:export_activity_logs";

    public static final String SUBSCRIPTION_MIGRATION_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription_migration";

    public static final String SYNC_LABELS_ACTIVITY_ARN = "arn:aws:states:us-west-1:114479152947:activity:sync_subscription_labels";

    public static final String DELETE_LABEL_KEY_ACTIVITY_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription_delete_label_key";

    public static final String BULK_AUTOMATION_SM_ARN = "arn:aws:states:us-west-1:114479152947:stateMachine:subscriptions-bulk-automation";

    public static final String BULK_AUTOMATION_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription-bulk-automation";

    public static final String SWAP_AUTOMATION_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription-swap-automation";

    public static final String UPDATE_QUEUE_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription-update-queued-attempts";

    public static final String SUBSCRIPTION_UPDATE_STATUS_SM_ARN = "arn:aws:states:us-west-1:114479152947:stateMachine:subscription-update-status";

    public static final String SUBSCRIPTION_UPDATE_STATUS_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription-update-status";

    public static final String SUBSCRIPTION_SEND_EMAIL_ACT_ARN = "arn:aws:states:us-west-1:114479152947:activity:subscription-send-email";

    public static final String SUBSCRIPTION_MIGRATION_BUCKET = "subscription-migration-v2";

    public static final String APPSTLE_ASSETS = "appstle-assets";

    public static final String APPSTLE_ASSETS_S3_KEY = "subscription/ShopifyCountries.json";

    public AWSCredentials getAwsCredentials() {
        return new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    }

    //S3
    public AmazonS3 getS3Client(){
        return amazonS3;
    }

    public S3Object getS3Object(String bucket, String key){
        return amazonS3.getObject(bucket, key);
    }

    //SQS
    public void send(String queueUrl, String shop, String message, String groupId){

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("Shop", new MessageAttributeValue().withDataType("String").withStringValue(shop));

        SendMessageRequest send_msg_request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageGroupId(groupId)
            .withMessageBody(message)
            .withMessageDeduplicationId(DigestUtils.sha256Hex(message) + "_" + Calendar.getInstance().getTimeInMillis())
            .withMessageAttributes(messageAttributes);
        sqs.sendMessage(send_msg_request);
    }

    //AWS Step Functions
    public void startExportExecution(ExportInputRequest exportInputRequest) throws IOException {
        String shop = exportInputRequest.getShop();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String exportInputRequestJSON = objectMapper.writeValueAsString(exportInputRequest);

        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();
        startExecutionRequest.setInput(exportInputRequestJSON);
        String executionName = (exportInputRequest.getPageNumber()+1) + "-" + shop;
        executionName = CommonUtils.buildStepFunctionExecutionName(executionName);
        startExecutionRequest.setName(executionName);
        startExecutionRequest.setStateMachineArn(EXPORT_SUBSCRIPTION_SM_ARN);

        awsStepFunctions.startExecution(startExecutionRequest);
    }

    public void startMigrationExecution(MigrationInputRequest migrationInputRequest) throws IOException {
        String shop = migrationInputRequest.getShop();
        ObjectMapper objectMapper = new ObjectMapper();
        String migrationInputRequestJSON = objectMapper.writeValueAsString(migrationInputRequest);

        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();
        startExecutionRequest.setInput(migrationInputRequestJSON);
        String executionName = CommonUtils.buildStepFunctionExecutionName(shop);
        startExecutionRequest.setName(executionName);
        startExecutionRequest.setStateMachineArn(MIGRATION_SUBSCRIPTION_SM_ARN);
        awsStepFunctions.startExecution(startExecutionRequest);
    }

    public StartExecutionResult startStepExecution(String shop, BulkAutomationActivityRequest stepInputRequest, String stateMachineARN, BulkAutomationType bulkAutomationType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String stepInputRequestJSON = objectMapper.writeValueAsString(stepInputRequest);

        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();
        startExecutionRequest.setInput(stepInputRequestJSON);
        String executionName = bulkAutomationType + "-" + shop;
        executionName = CommonUtils.buildStepFunctionExecutionName(executionName);
        startExecutionRequest.setName(executionName);
        startExecutionRequest.setStateMachineArn(stateMachineARN);
        return awsStepFunctions.startExecution(startExecutionRequest);
    }

    public void startStepExecution(String shop, Long contractId, String smARN, String requestInput) {

        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();
        startExecutionRequest.setInput(requestInput);
        String executionName = CommonUtils.buildStepFunctionExecutionName(contractId, shop);
        startExecutionRequest.setName(executionName);
        startExecutionRequest.setStateMachineArn(smARN);
        awsStepFunctions.startExecution(startExecutionRequest);
    }

    public void stopStepExecution(String executionArn) {
        if(StringUtils.isBlank(executionArn)) {
            return;
        }

        StopExecutionRequest stopExecutionRequest = new StopExecutionRequest();
        stopExecutionRequest.setExecutionArn(executionArn);
        stopExecutionRequest.setCause("Force stop");
        awsStepFunctions.stopExecution(stopExecutionRequest);
    }

    public byte[] downloadFile(String bucketName, String fileName) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (AmazonS3Exception | IOException e) {
            String a = "b";
        }
        return null;
    }

    public Reader getDataReader(String bucketName, String s3Key) {
        S3Object subscriptionDataObject = getS3Object(bucketName, s3Key);
        InputStreamReader streamReader = new InputStreamReader(subscriptionDataObject.getObjectContent(), StandardCharsets.UTF_8);
        return new BufferedReader(streamReader);
    }

    public Reader getDataReaderForMigration(String s3Key) {
        return getDataReader(AwsUtils.SUBSCRIPTION_MIGRATION_BUCKET, s3Key);
    }
}

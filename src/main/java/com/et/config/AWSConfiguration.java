package com.et.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
/*import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.StaticRegionProvider;*/
import io.awspring.cloud.core.config.AmazonWebserviceClientFactoryBean;
import io.awspring.cloud.core.region.StaticRegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.et.constant.AWSConstants.ACCESS_KEY;
import static com.et.constant.AWSConstants.SECRET_KEY;


@Configuration
public class AWSConfiguration {

    @Bean
    public AWSCredentials awsCredentials() {
        AWSCredentials credentials =
            new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3(AWSCredentials awsCredentials) {
        AmazonS3 s3client =
            AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_WEST_1)
                .build();

        return s3client;
    }


    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {

        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(Regions.US_WEST_1)
            .build();

        return dynamoDB;
    }

    @Lazy
    @Bean(destroyMethod = "shutdown")
    public AmazonSQSBufferedAsyncClient amazonSQS(AWSCredentials awsCredentials) throws Exception {
        AmazonWebserviceClientFactoryBean<AmazonSQSAsyncClient> clientFactoryBean = new AmazonWebserviceClientFactoryBean<>(
            AmazonSQSAsyncClient.class,
            new AWSStaticCredentialsProvider(awsCredentials),
            new StaticRegionProvider("us-west-1"));
        clientFactoryBean.afterPropertiesSet();
        return new AmazonSQSBufferedAsyncClient(clientFactoryBean.getObject());
    }

    @Bean
    public AWSStepFunctions awsStepFunctions(AWSCredentials awsCredentials) {

        AWSStepFunctions build = AWSStepFunctionsClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(Regions.US_WEST_1)
            .build();

        return build;
    }

    @Bean(destroyMethod = "shutdown")
    public AmazonCloudFront amazonCloudFront(AWSCredentials appikonAwsCredentials) {
        return AmazonCloudFrontClientBuilder.standard()
            .withRegion(Regions.US_WEST_1)
            .withCredentials(new AWSStaticCredentialsProvider(appikonAwsCredentials))
            .build();
    }
}

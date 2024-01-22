package com.et.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Base64;

import static com.et.constant.AWSConstants.ACCESS_KEY;
import static com.et.constant.AWSConstants.SECRET_KEY;

@Configuration
//@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class SecretManagerDatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(SecretManagerDatabaseConfiguration.class);

    /*@Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {


        String secretName = "/secrets/subscription/credentials";

        // Create a Secrets Manager client
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
            .withRegion(Regions.US_WEST_1)
            .withCredentials(credentialsProvider)
            .build();


        log.info("came after AWSSecretsManager client");
        String secret, decodedBinarySecret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;


        getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        log.info("came after getSecretValueResult = client.getSecretValue(getSecretValueRequest)");

        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
            JSONObject jsonObject = new JSONObject(secret);
            properties.setUsername(jsonObject.getString("username"));
            properties.setPassword(jsonObject.getString("password"));
            log.info("came after getSecretValueResult.getSecretString() != null. secret=" + secret);

        } else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
            log.info("came after decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array()). decodedBinarySecret=" + decodedBinarySecret);
        }

        log.info("came after if (getSecretValueResult.getSecretString() != null)");

        HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }*/
}

package com.et.config.paramstore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.util.StringUtils;
import io.awspring.cloud.core.SpringCloudClientConfiguration;
import io.awspring.cloud.paramstore.AwsParamStoreProperties;
import io.awspring.cloud.paramstore.AwsParamStorePropertySourceLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Spring Cloud Bootstrap Configuration for setting up an
 * {@link AwsParamStorePropertySourceLocator} and its dependencies.
 *
 * @author Joris Kuipers
 * @author Matej Nedic
 * @author Eddú Meléndez
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AwsParamStoreProperties.class)
@ConditionalOnClass({ AWSSimpleSystemsManagement.class, AwsParamStorePropertySourceLocator.class })
@ConditionalOnProperty(prefix = AwsParamStoreProperties.CONFIG_PREFIX, name = "enabled", matchIfMissing = true)
public class AwsParamStoreBootstrapConfiguration {

	private final Environment environment;

	public AwsParamStoreBootstrapConfiguration(Environment environment) {
		this.environment = environment;
	}

    @Bean
	AwsParamStorePropertySourceLocator awsParamStorePropertySourceLocator(AWSSimpleSystemsManagement ssmClient,
			AwsParamStoreProperties properties) {
		if (StringUtils.isNullOrEmpty(properties.getName())) {
			properties.setName(this.environment.getProperty("spring.application.name"));
		}
		return new AwsParamStorePropertySourceLocator(ssmClient, properties);
	}

	public static AWSSimpleSystemsManagement createSimpleSystemManagementClient(AwsParamStoreProperties properties) {
		AWSSimpleSystemsManagementClientBuilder builder = AWSSimpleSystemsManagementClientBuilder.standard()
				.withClientConfiguration(SpringCloudClientConfiguration.getClientConfiguration());
		if (!StringUtils.isNullOrEmpty(properties.getRegion())) {
			builder.withRegion(properties.getRegion());
		}
		if (properties.getEndpoint() != null) {
			AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
					properties.getEndpoint().toString(), null);
			builder.withEndpointConfiguration(endpointConfiguration);
		}
		return builder.build();
	}

    public static AWSSimpleSystemsManagement createSimpleSystemManagementClient(AWSCredentials credentials) {
        AWSSimpleSystemsManagement build = AWSSimpleSystemsManagementClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_WEST_1)
            .build();

        return build;
    }

}

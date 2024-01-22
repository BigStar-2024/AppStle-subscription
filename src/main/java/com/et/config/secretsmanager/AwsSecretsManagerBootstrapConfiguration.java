package com.et.config.secretsmanager;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.util.StringUtils;
import io.awspring.cloud.core.SpringCloudClientConfiguration;
import io.awspring.cloud.secretsmanager.AwsSecretsManagerProperties;
import io.awspring.cloud.secretsmanager.AwsSecretsManagerPropertySourceLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static com.et.config.paramstore.AwsParamStoreBootstrapConfiguration.createSimpleSystemManagementClient;

/**
 * Spring Cloud Bootstrap Configuration for setting up an
 * {@link AwsSecretsManagerPropertySourceLocator} and its dependencies.
 *
 * @author Fabio Maia
 * @author Matej Nedic
 * @author Eddú Meléndez
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AwsSecretsManagerProperties.class)
@ConditionalOnClass({ AWSSecretsManager.class, AwsSecretsManagerPropertySourceLocator.class })
@ConditionalOnProperty(prefix = AwsSecretsManagerProperties.CONFIG_PREFIX, name = "enabled", matchIfMissing = true)
public class AwsSecretsManagerBootstrapConfiguration {

	private final Environment environment;

	public AwsSecretsManagerBootstrapConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Bean
	AwsSecretsManagerPropertySourceLocator awsSecretsManagerPropertySourceLocator(AWSSecretsManager smClient,
			AwsSecretsManagerProperties properties) {
		if (StringUtils.isNullOrEmpty(properties.getName())) {
			properties.setName(this.environment.getProperty("spring.application.name"));
		}
		return new AwsSecretsManagerPropertySourceLocator(smClient, properties);
	}

	public static AWSSecretsManager createSecretsManagerClient(AwsSecretsManagerProperties properties) {
		AWSSecretsManagerClientBuilder builder = AWSSecretsManagerClientBuilder.standard()
				.withClientConfiguration(SpringCloudClientConfiguration.getClientConfiguration());
		if (!StringUtils.isNullOrEmpty(properties.getRegion())) {
			builder.withRegion(properties.getRegion());
		}
		if (properties.getEndpoint() != null) {
			EndpointConfiguration endpointConfiguration = new EndpointConfiguration(properties.getEndpoint().toString(),
					null);
			builder.withEndpointConfiguration(endpointConfiguration);
		}
		return builder.build();
	}


	public static AWSSecretsManager createSecretsManagerClient(AWSCredentials credentials) {
		AWSSecretsManager build = AWSSecretsManagerClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_1)
				.build();

		return build;
	}

    @Bean
    @ConditionalOnMissingBean
    AWSSimpleSystemsManagement ssmClient(AWSCredentials credentials) {
        return createSimpleSystemManagementClient(credentials);
    }

}

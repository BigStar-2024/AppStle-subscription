package com.et.config.paramstore.reload;

import io.awspring.cloud.paramstore.AwsParamStorePropertySource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.util.TaskSchedulerWrapper;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * {@link EnableAutoConfiguration Auto-Configuration} for reloading properties from Parameter Store.
 *
 * @author Maciej Walkowiak
 * @author Matej Nedic
 */
@Configuration
/*@ConditionalOnClass({ EndpointAutoConfiguration.class, RestartEndpoint.class, ContextRefresher.class })
@AutoConfigureAfter({ InfoEndpointAutoConfiguration.class, RefreshEndpointAutoConfiguration.class,
		RefreshAutoConfiguration.class })
@ConditionalOnBean(ContextRefresher.class)*/
public class ParameterStoreReloadAutoConfiguration {

	@Bean("parameterStoreTaskScheduler")
	@ConditionalOnMissingBean
	public TaskSchedulerWrapper<TaskScheduler> taskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setThreadNamePrefix("spring-cloud-aws-parameterstore-ThreadPoolTaskScheduler-");
		threadPoolTaskScheduler.setDaemon(true);

		return new TaskSchedulerWrapper<>(threadPoolTaskScheduler);
	}

	@Bean("parameterStoreConfigurationUpdateStrategy")
	@ConditionalOnMissingBean(name = "parameterStoreConfigurationUpdateStrategy")
	public ConfigurationUpdateStrategy parameterStoreConfigurationUpdateStrategy(ContextRefresher refresher) {
		return ConfigurationUpdateStrategy.create(refresher);
	}

	@Bean
	@ConditionalOnBean(ConfigurationUpdateStrategy.class)
	public ConfigurationChangeDetector<AwsParamStorePropertySource> parameterStorePollingAwsPropertySourceChangeDetector(
			@Qualifier("parameterStoreConfigurationUpdateStrategy") ConfigurationUpdateStrategy strategy,
			@Qualifier("parameterStoreTaskScheduler") TaskSchedulerWrapper<TaskScheduler> taskScheduler,
			ConfigurableEnvironment environment) {

        ReloadProperties reloadProperties = new ReloadProperties();
        return new PollingAwsPropertySourceChangeDetector<>(reloadProperties, AwsParamStorePropertySource.class,
				strategy, taskScheduler.getTaskScheduler(), environment);
	}
}

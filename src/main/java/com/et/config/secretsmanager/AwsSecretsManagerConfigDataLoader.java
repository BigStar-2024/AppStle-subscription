package com.et.config.secretsmanager;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import io.awspring.cloud.secretsmanager.AwsSecretsManagerPropertySource;
import io.awspring.cloud.secretsmanager.AwsSecretsManagerPropertySources;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Loads config data from AWS Secret Manager.
 *
 * @author Eddú Meléndez
 * @author Maciej Walkowiak
 * @since 2.3.0
 */
public class AwsSecretsManagerConfigDataLoader implements ConfigDataLoader<AwsSecretsManagerConfigDataResource> {

	private final DeferredLogFactory logFactory;

	public AwsSecretsManagerConfigDataLoader(DeferredLogFactory logFactory) {
		this.logFactory = logFactory;
		reconfigureLoggers(logFactory);
	}

	@Override
	public ConfigData load(ConfigDataLoaderContext context, AwsSecretsManagerConfigDataResource resource) {
		try {
			AWSSecretsManager sm = context.getBootstrapContext().get(AWSSecretsManager.class);
			AwsSecretsManagerPropertySource propertySource = resource.getPropertySources()
					.createPropertySource(resource.getContext(), resource.isOptional(), sm);
			if (propertySource != null) {
				return new ConfigData(Collections.singletonList(propertySource));
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new ConfigDataResourceNotFoundException(resource, e);
		}
	}

	private void reconfigureLoggers(DeferredLogFactory logFactory) {
		List<Class<?>> loggers = Arrays.asList(AwsSecretsManagerPropertySource.class,
				AwsSecretsManagerPropertySources.class);

		loggers.forEach(it -> reconfigureLogger(it, logFactory));
	}

	static void reconfigureLogger(Class<?> type, DeferredLogFactory logFactory) {
		// loggers in these classes must be static non-final
		ReflectionUtils.doWithFields(type, field -> {

			field.setAccessible(true);
			field.set(null, logFactory.getLog(type));

		}, AwsSecretsManagerConfigDataLoader::isUpdateableLogField);
	}

	private static boolean isUpdateableLogField(Field field) {
		return !Modifier.isFinal(field.getModifiers()) && field.getType().isAssignableFrom(Log.class);
	}

}

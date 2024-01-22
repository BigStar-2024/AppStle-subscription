package com.et.config.secretsmanager;

import io.awspring.cloud.secretsmanager.AwsSecretsManagerPropertySources;
import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.core.style.ToStringCreator;

import java.util.Objects;

/**
 * Config data resource for AWS Secret Manager integration.
 *
 * @author Eddú Meléndez
 * @author Maciej Walkowiak
 * @since 2.3.0
 */
public class AwsSecretsManagerConfigDataResource extends ConfigDataResource {

	private final String context;

	private final boolean optional;

	private final AwsSecretsManagerPropertySources propertySources;

	public AwsSecretsManagerConfigDataResource(String context, boolean optional,
			AwsSecretsManagerPropertySources propertySources) {
		this.context = context;
		this.optional = optional;
		this.propertySources = propertySources;
	}

	/**
	 * Returns context which is equal to Secret Manager secret name.
	 * @return the context
	 */
	public String getContext() {
		return this.context;
	}

	/**
	 * If application startup should fail when secret cannot be loaded or does not exist.
	 * @return is optional
	 */
	public boolean isOptional() {
		return this.optional;
	}

	public AwsSecretsManagerPropertySources getPropertySources() {
		return this.propertySources;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AwsSecretsManagerConfigDataResource that = (AwsSecretsManagerConfigDataResource) o;
		return this.optional == that.optional && this.context.equals(that.context);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.optional, this.context);
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("context", context).append("optional", optional).toString();

	}

}

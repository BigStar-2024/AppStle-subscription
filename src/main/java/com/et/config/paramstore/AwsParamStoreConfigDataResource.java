package com.et.config.paramstore;

import io.awspring.cloud.paramstore.AwsParamStorePropertySources;
import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.core.style.ToStringCreator;

import java.util.Objects;

/**
 * Config data resource for AWS System Manager Management integration.
 *
 * @author Eddú Meléndez
 * @since 2.3.0
 */
public class AwsParamStoreConfigDataResource extends ConfigDataResource {

	private final String context;

	private final boolean optional;

	private final AwsParamStorePropertySources propertySources;

	public AwsParamStoreConfigDataResource(String context, boolean optional,
			AwsParamStorePropertySources propertySources) {
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

	public AwsParamStorePropertySources getPropertySources() {
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
		AwsParamStoreConfigDataResource that = (AwsParamStoreConfigDataResource) o;
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

package com.et.config.paramstore.reload;

/**
 * Configuration for reload strategies.
 *
 * Heavily inspired by Spring Cloud Kubernetes.
 *
 * @author Nicola Ferraro
 * @author Matej Nedic
 * @author Maciej Walkowiak
 */
public enum ReloadStrategy {
	/**
	 * Fire a refresh of beans annotated with @ConfigurationProperties or @RefreshScope.
	 */
	REFRESH,

	/**
	 * Restarts the Spring ApplicationContext to apply the new configuration.
	 */
	RESTART_CONTEXT
}

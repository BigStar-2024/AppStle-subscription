package com.et.config.paramstore.reload;

import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * Configuration related to reloading properties.
 *
 * Heavily inspired by Spring Cloud Kubernetes.
 *
 * @author Nicola Ferraro
 * @author Matej Nedic
 * @author Maciej Walkowiak
 */
public class ReloadProperties {

	/**
	 * Reload strategy to run when properties change.
	 */
	private ReloadStrategy strategy;

	/**
	 * If {@link ReloadStrategy#RESTART_CONTEXT} is configured, maximum waiting time for server restart.
	 */
	private Duration maxWaitForRestart = Duration.ofSeconds(2);

	/**
	 * Refresh period for {@link PollingAwsPropertySourceChangeDetector}.
	 */
	private Duration period = Duration.ofMinutes(1);

	public boolean isEnabled() {
		return strategy != null;
	}

	@Nullable
	public ReloadStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(@Nullable ReloadStrategy strategy) {
		this.strategy = strategy;
	}

	public Duration getMaxWaitForRestart() {
		return maxWaitForRestart;
	}

	public void setMaxWaitForRestart(Duration maxWaitForRestart) {
		this.maxWaitForRestart = maxWaitForRestart;
	}

	public Duration getPeriod() {
		return period;
	}

	public void setPeriod(Duration period) {
		this.period = period;
	}
}

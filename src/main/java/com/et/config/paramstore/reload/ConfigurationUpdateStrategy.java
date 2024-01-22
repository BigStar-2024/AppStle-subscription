package com.et.config.paramstore.reload;

import org.springframework.cloud.context.refresh.ContextRefresher;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the superclass of all named strategies that can be fired when the configuration changes.
 *
 * Heavily inspired by Spring Cloud Kubernetes.
 *
 * @author Nicola Ferraro
 * @author Maciej Walkowiak
 */
public class ConfigurationUpdateStrategy implements Runnable {

	private final Runnable reloadProcedure;

	public static ConfigurationUpdateStrategy create(ContextRefresher refresher) {
        return new ConfigurationUpdateStrategy(refresher::refresh);
	}

	private ConfigurationUpdateStrategy(Runnable reloadProcedure) {
		this.reloadProcedure = Objects.requireNonNull(reloadProcedure, "reloadProcedure cannot be null");
	}

	public void run() {
		this.reloadProcedure.run();
	}

	private static void wait(ReloadProperties properties) {
		long waitMillis = ThreadLocalRandom.current().nextLong(properties.getMaxWaitForRestart().toMillis());
		try {
			Thread.sleep(waitMillis);
		}
		catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}
}

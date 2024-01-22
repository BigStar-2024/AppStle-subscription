package com.et.config.paramstore.reload;

import io.awspring.cloud.paramstore.AwsParamStorePropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Configuration change detector that checks for changed configuration on a scheduled basis.
 * <p>
 * Heavily inspired by Spring Cloud Kubernetes.
 *
 * @param <T> - property source class to check
 * @author Matej Nedic
 * @author Maciej Walkowiak
 */
public class PollingAwsPropertySourceChangeDetector<T extends AwsParamStorePropertySource>
    extends ConfigurationChangeDetector<T> implements InitializingBean {

    protected Log log = LogFactory.getLog(getClass());
    private final TaskScheduler taskExecutor;

    public PollingAwsPropertySourceChangeDetector(ReloadProperties properties, Class<T> clazz,
                                                  ConfigurationUpdateStrategy strategy, TaskScheduler taskExecutor, ConfigurableEnvironment environment) {
        super(properties, strategy, environment, clazz);
        this.taskExecutor = taskExecutor;

    }

    @Override
    public void afterPropertiesSet() {
        log.info("Polling configurations change detector activated");
        PeriodicTrigger trigger = new PeriodicTrigger(15, TimeUnit.MINUTES);
        trigger.setInitialDelay(15);
        taskExecutor.schedule(this::executeCycle, trigger);
    }

    public void executeCycle() {
        if (true) {
            log.info("Polling for changes in secrets");
            List<T> currentSecretSources = locateMapPropertySources(this.environment);
            if (!currentSecretSources.isEmpty()) {
                for (T propertySource : currentSecretSources) {
                    AwsParamStorePropertySource clone = new AwsParamStorePropertySource(propertySource.getName(), propertySource.getSource());
                    clone.init();
                    if (changed(propertySource, clone)) {
                        reloadProperties();
                    }
                }
            }
        }
    }
}

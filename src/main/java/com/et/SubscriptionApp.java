package com.et;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.codepipeline.AWSCodePipeline;
import com.amazonaws.services.codepipeline.AWSCodePipelineClientBuilder;
import com.amazonaws.services.codepipeline.model.ListPipelineExecutionsRequest;
import com.amazonaws.services.codepipeline.model.ListPipelineExecutionsResult;
import com.amazonaws.services.codepipeline.model.PipelineExecutionSummary;
import com.et.config.ApplicationProperties;
import com.et.constant.MailgunProperties;
import io.awspring.cloud.messaging.config.annotation.EnableSqs;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static com.et.constant.AWSConstants.ACCESS_KEY;
import static com.et.constant.AWSConstants.SECRET_KEY;
import static com.et.constant.Constants.*;
import static tech.jhipster.config.JHipsterConstants.SPRING_PROFILE_DEVELOPMENT;
import static tech.jhipster.config.JHipsterConstants.SPRING_PROFILE_PRODUCTION;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class, MailgunProperties.class})
@EnableSqs
@EnableCaching
public class SubscriptionApp implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionApp.class);


    private final Environment env;

    public SubscriptionApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes subscription.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }

        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_STAGING)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'staging' profiles at the same time.");
        }

        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_JOBS)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'jobs' profiles at the same time.");
        }

        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SubscriptionApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
        setBuildVersion(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    private static void setBuildVersion(Environment env) {
        try {
            long latestBuildTime = System.currentTimeMillis();

            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

            if (activeProfiles.contains(SPRING_PROFILE_PRODUCTION) || activeProfiles.contains(SPRING_PROFILE_JOBS)) {

                AWSCredentials awsCredentials =
                    new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

                AWSCodePipeline codePipeline = AWSCodePipelineClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_WEST_1)
                    .build();

                ListPipelineExecutionsRequest listPipelinesRequest = new ListPipelineExecutionsRequest();
                listPipelinesRequest.setPipelineName("subscription_web");
                listPipelinesRequest.setMaxResults(20);
                ListPipelineExecutionsResult listPipelinesResult = codePipeline.listPipelineExecutions(listPipelinesRequest);

                List<PipelineExecutionSummary> pipelineExecutions = listPipelinesResult.getPipelineExecutionSummaries();

                if(!CollectionUtils.isEmpty(pipelineExecutions)) {
                    Optional<PipelineExecutionSummary> latestSuccessPipeline = pipelineExecutions.stream()
                        .filter(pe -> "Succeeded".equalsIgnoreCase(pe.getStatus()))
                        .sorted(Comparator.comparing(PipelineExecutionSummary::getLastUpdateTime).reversed())
                        .findFirst();

                    if(latestSuccessPipeline.isPresent()) {
                        latestBuildTime = latestSuccessPipeline.get().getLastUpdateTime().getTime();
                        log.info("Setting latest build number for PROD. latestBuildTime={}", latestBuildTime);
                    }
                }

            } else {
                log.info("Setting latest build number for Non-PROD. latestBuildTime={}", latestBuildTime);
            }

            System.setProperty("latest.build.time", String.valueOf(latestBuildTime));

        } catch (Exception e) {
            log.error("Error while preparing latest build time.", e);
        }
    }
}

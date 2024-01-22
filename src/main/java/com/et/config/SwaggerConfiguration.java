package com.et.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.*;
import org.springdoc.core.fn.RouterOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import tech.jhipster.config.JHipsterProperties;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;
import static tech.jhipster.config.apidoc.JHipsterSpringDocGroupsConfiguration.MANAGEMENT_GROUP_NAME;

@Configuration
public class SwaggerConfiguration {

    private final JHipsterProperties.ApiDocs properties;

    /**
     * <p>Constructor for OpenApiAutoConfiguration.</p>
     *
     * @param jHipsterProperties a {@link JHipsterProperties} object.
     */
    public SwaggerConfiguration(JHipsterProperties jHipsterProperties) {
        properties = jHipsterProperties.getApiDocs();
    }


    @Bean
    public GroupedOpenApi externalOpenAPI(
        @Value("${spring.application.name:application}") String appName
    ) {
        return GroupedOpenApi.builder()
            .group("external")
            .addOpenApiCustomiser(openApi -> openApi.info(new Info()
                .title("External")
                .description("")
                .version(properties.getVersion())
            )).addOpenApiCustomiser(openApi -> {
                for (JHipsterProperties.ApiDocs.Server server : properties.getServers()) {
                    openApi.getServers().clear();
                    openApi.addServersItem(new Server().url(server.getUrl()).description(server.getDescription()));
                }
            })
            .pathsToMatch("/api/external/v2/**")
            .build();
    }

    @Bean
    public GroupedOpenApi customerPortalOpenAPI(
        @Value("${spring.application.name:application}") String appName
    ) {
        return GroupedOpenApi.builder()
            .group("customer-portal")
            .addOpenApiCustomiser(openApi -> openApi.info(new Info()
                .title("Customer Portal APIs")
                .description("")
                .version(properties.getVersion())
            )).addOpenApiCustomiser(openApi -> {
                for (JHipsterProperties.ApiDocs.Server server : properties.getServers()) {
                    openApi.getServers().clear();
                    openApi.addServersItem(new Server().url(server.getUrl()).description(server.getDescription()));
                }
            })
            .pathsToMatch("/subscriptions/cp/api/**")
            .build();
    }


    @Bean
    public GroupedOpenApi openAPIDefaultGroupedOpenAPI(
        List<OpenApiCustomiser> openApiCustomisers,
        List<OperationCustomizer> operationCustomizers,
        @Qualifier("apiFirstGroupedOpenAPI") Optional<GroupedOpenApi> apiFirstGroupedOpenAPI,
        SpringDocConfigProperties springDocConfigProperties
    ) {
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
            .group(DEFAULT_GROUP_NAME)
            .pathsToMatch(properties.getDefaultIncludePattern());
        openApiCustomisers.stream()
            .filter(customiser -> !(customiser instanceof ActuatorOpenApiCustomizer))
            .forEach(builder::addOpenApiCustomiser);
        builder.addOpenApiCustomiser(openApi -> {
            for (JHipsterProperties.ApiDocs.Server server : properties.getServers()) {
                openApi.getServers().clear();
                openApi.addServersItem(new Server().url(server.getUrl()).description(server.getDescription()));
            }
        });
        operationCustomizers.stream()
            .filter(customiser -> !(customiser instanceof ActuatorOperationCustomizer))
            .forEach(builder::addOperationCustomizer);
        apiFirstGroupedOpenAPI.map(GroupedOpenApi::getPackagesToScan)
            .ifPresent(packagesToScan -> packagesToScan.forEach(builder::packagesToExclude));
        return builder.build();
    }

    static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";
    /**
     * OpenApi management group configuration for the management endpoints (actuator) OpenAPI docs.
     *
     * @return the GroupedOpenApi configuration
     */
    @Bean
    @ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
    public GroupedOpenApi openAPIManagementGroupedOpenAPI(
        @Value("${spring.application.name:application}") String appName,
        ActuatorOpenApiCustomizer actuatorOpenApiCustomiser,
        ActuatorOperationCustomizer actuatorCustomizer
    ) {
        return GroupedOpenApi.builder()
            .group(MANAGEMENT_GROUP_NAME)
            .addOpenApiCustomiser(openApi -> openApi.info(new Info()
                .title(StringUtils.capitalize(appName) + " " + MANAGEMENT_TITLE_SUFFIX)
                .description(MANAGEMENT_DESCRIPTION)
                .version(properties.getVersion())
            )).addOpenApiCustomiser(openApi -> {
                for (JHipsterProperties.ApiDocs.Server server : properties.getServers()) {
                    openApi.getServers().clear();
                    openApi.addServersItem(new Server().url(server.getUrl()).description(server.getDescription()));
                }
            })
            .addOpenApiCustomiser(actuatorOpenApiCustomiser)
            .addOperationCustomizer(actuatorCustomizer)
            .pathsToMatch(properties.getManagementIncludePattern())
            .build();
    }
}

package com.et.config;

import com.et.security.AuthoritiesConstants;
import com.et.security.jwt.CSPHeaderWriter;
import com.et.security.jwt.CustomerMaskingFilter;
import com.et.security.jwt.JWTConfigurer;
import com.et.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SecurityConfiguration(TokenProvider tokenProvider,
                                 CorsFilter corsFilter,
                                 SecurityProblemSupport problemSupport,
                                 UserDetailsService userDetailsService,
                                 AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.userDetailsService = userDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/h2-console/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/test/**")
            .antMatchers("/assets/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new CustomerMaskingFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .frameOptions()
            .disable()
            .addHeaderWriter(new CSPHeaderWriter(tokenProvider, userDetailsService))
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/oauth/**").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/activatecharge").permitAll()
            .antMatchers("/api/subscription-customers/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-customers/valid/**", "/subscriptions/cp/api/subscription-customers/valid/**").permitAll()
            .antMatchers("/api/subscription-customers-detail/valid/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts/contract-external/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts/contract/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers(HttpMethod.DELETE, "/api/subscription-contracts/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/bulk-subscription-groups/**").permitAll()
            .antMatchers("/api/subscription-groups/all-selling-plans").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-payment-method/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-delivery-price/**").permitAll()
            .antMatchers("/api/subscription-contracts-update-billing-interval/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-delivery-interval/**").permitAll()
            .antMatchers("/api/subscription-contracts-update-min-cycles/**").permitAll()
            .antMatchers("/api/subscription-contracts-update-max-cycles/**").permitAll()
            .antMatchers("/api/subscription-contracts-update-shipping-address/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-line-item/**").permitAll()
            .antMatchers("/api/subscription-contracts-update-line-item-quantity/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-line-item-attributes/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-bundlings/by-token/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-bundlings/single-product/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
//            .antMatchers("/api/subscription-contracts-add-line-item/**").permitAll()
            .antMatchers("/api/v2/subscription-contracts-add-line-item/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/update-subscription-contract-line-amount").permitAll()
            .antMatchers("/api/subscription-contracts-remove-line-item/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-details/replace-variants-v2").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/update-delivery-price").permitAll()
            .antMatchers("/api/miscellaneous/update-customer-portal-settings").permitAll()
            .antMatchers("/api/update-email-template-settings").permitAll()
            .antMatchers("/api/subscription-contracts-apply-discount").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-status").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/data/product/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/data/variant/**").permitAll()
            .antMatchers("/api/data/products/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/data/products-selling-plans/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/data/variant-contextual-pricing/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/data/product-variants/**").permitAll()
            .antMatchers("/api/data/v2/product-variants/**").permitAll()
            .antMatchers("/api/zapier/**").permitAll()
            .antMatchers("/api/product-swaps-by-variant-groups").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers(HttpMethod.GET, "/api/subscription-custom-csses/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL, AuthoritiesConstants.BUILD_A_BOX)
            .antMatchers(HttpMethod.GET, "/api/cancellation-managements/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/shopify/**").permitAll()
            .antMatchers("/api/subscription-contract-one-offs-by-contractId/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/upcoming-subscription-contract-one-offs-by-contractId").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-one-offs-update-quantity").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-freeze-status-detail/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-add-discount").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-remove-discount").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/delivery-profiles/create-shipping-profile").permitAll()
            .antMatchers("/api/subscription-bundlings/external/get-bundle/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.BUILD_A_BOX)
            .antMatchers("/api/subscription-bundlings/discount/**", "/subscriptions/bb/api/subscription-bundlings/discount/**").permitAll()
            .antMatchers("/api/shop-billing-confirmation-url/**").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/subscription-billing-attempts").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-billing-attempts/top-orders/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers(HttpMethod.GET, "/api/subscription-billing-attempts/past-orders").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-billing-attempts/skip-order/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-billing-attempts/attempt-billing/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-billing-date/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers(HttpMethod.DELETE, "/api/customer-portal-settings/**").hasAuthority(AuthoritiesConstants.MERCHANT_PORTAL)
            .antMatchers("/api/customer-portal-settings/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/shop-infos-by-current-login").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL, AuthoritiesConstants.BUILD_A_BOX)
            .antMatchers("/api/subscription-contract-details/customer/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-details/subscription-fulfillments/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-details/current-cycle/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-details/split-existing-contract").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contracts-update-order-note/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-contract-details/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.CUSTOMER_PORTAL)
            .antMatchers("/api/subscription-billing-attempts-update-order-note/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/subscription-bundle-settings/**").hasAnyAuthority(AuthoritiesConstants.MERCHANT_PORTAL, AuthoritiesConstants.BUILD_A_BOX)
            .antMatchers("/api/bundling-discount/**").permitAll()
            .antMatchers("/api/miscellaneous/**").permitAll()
            .antMatchers("/api/activity-logs/**").permitAll()
            .antMatchers("/api/shop-infos-by-shop/**").permitAll()
            .antMatchers("/api/plan-infos/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/shop-asset-urls/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers(HttpMethod.POST, "/api/payment-plans/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers(HttpMethod.PUT, "/api/payment-plans/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/subscriptions/cart-discount").permitAll() //TODO: added for testing purpose
            .antMatchers("/api/**").hasAuthority(AuthoritiesConstants.MERCHANT_PORTAL)
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/env").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/bc-sf-filter/filter").permitAll()
            .antMatchers("/management/jhiopenapigroups").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, userDetailsService, authenticationManagerBuilder);
    }
}

package com.et.security.jwt;

import com.et.security.ExternalUserPasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static com.et.security.AuthoritiesConstants.*;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a
 * valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String EXTERNAL_AUTHORIZATION_HEADER = "ExternalAuthorization";
    public static final String SHOPIFY_AUTHORIZATION_HEADER = "ShopifyAuthorization";
    public static final String API_KEY_PARAM = "api_key";
    public static final String APP_KEY_PARAM = "app_key";
    private final UserDetailsService userDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    private TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider, UserDetailsService userDetailsService, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

            String jwt = resolveToken(httpServletRequest);
            String externalJwt = resolveExternalToken(httpServletRequest);
            String shopifyJwt = resolveShopifyToken(httpServletRequest);


            if ((httpServletRequest.getServletPath().startsWith("/subscriptions"))
                && this.tokenProvider.validateHmac(httpServletRequest.getParameterMap())) {
                log.info("servletPath=" + httpServletRequest.getServletPath());
                if ((httpServletRequest.getServletPath().startsWith("/subscriptions/cp/api"))
                    || httpServletRequest.getServletPath().startsWith("/subscriptions/bb/api")) {
                    Authentication authentication = tokenProvider.getShopifyProxyAuthentication(httpServletRequest);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    MDC.put("shop", authentication.getName());
                    log.info("httpServletRequest.getServletPath().startsWith(\"/subscriptions/cp/api\"))\n" +
                        "                    || httpServletRequest.getServletPath().startsWith(\"/subscriptions/bb/api\") came here");
                } else {
                    UserDetails user = userDetailsService.loadUserByUsername(Optional.ofNullable(httpServletRequest.getParameterMap().get("shop")[0]).orElse("").trim());
                    UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    setShopCookie(httpServletResponse, authenticationToken);
                    MDC.put("shop", authenticationToken.getName());
                }
            } else if (StringUtils.hasText(externalJwt) && this.tokenProvider.validateExternalToken(externalJwt)) {
                Authentication authentication = this.tokenProvider.getExternalAuthentication(externalJwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                setShopCookie(httpServletResponse, authentication);
                log.info("authentication successful for " + authentication.getName());
                MDC.put("shop", authentication.getName());
            } else if (StringUtils.hasText(shopifyJwt) && this.tokenProvider.validateShopifyToken(shopifyJwt)) {
                Authentication authentication = this.tokenProvider.getShopifyAuthentication(shopifyJwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                setShopCookie(httpServletResponse, authentication);
                MDC.put("shop", authentication.getName());
            } else if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                setShopCookie(httpServletResponse, authentication);
                MDC.put("shop", authentication.getName());
            } else if (userDetailsService != null
                && httpServletRequest.getHeader("Username") != null
                && httpServletRequest.getHeader("Password") != null) {
                String username = httpServletRequest.getHeader("Username");
                String password = httpServletRequest.getHeader("Password");
                String shop = httpServletRequest.getHeader("Shop");
                if(!StringUtils.hasText(shop)) {
                    shop = username;
                }

                if (username.equals("admin") || username.equals("user")) {
                    throw new RuntimeException("Please use Dynamo DB password and username");
                }

                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));
                authorities.add(new SimpleGrantedAuthority(ADMIN));

                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password, authorities);

                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                authorities.addAll(authentication.getAuthorities());

                // Check shop exists
                userDetailsService.loadUserByUsername(shop);

                authentication = new UsernamePasswordAuthenticationToken(shop, "", authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                setShopCookie(httpServletResponse, authentication);
                MDC.put("shop", authentication.getName());
            } else if (httpServletRequest.getServletPath().contains("/external/")) {

                Authentication authentication = this.tokenProvider.getExternalApiKeyAuthentication(
                    httpServletRequest.getParameter(API_KEY_PARAM),
                    httpServletRequest.getParameter(APP_KEY_PARAM)
                );

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    MDC.put("shop", authentication.getName());
                    log.info("external request for requestPath=" + httpServletRequest.getServletPath());
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);


        } catch (Exception ex) {
            new RuntimeException(ex);
        } finally {
            MDC.clear();
        }
    }

    private void setShopCookie(HttpServletResponse httpServletResponse, Authentication authentication) {
        Cookie shopCookie = new Cookie("jhi-shop", authentication.getName());
        shopCookie.setPath("/");
        shopCookie.setMaxAge(10);
        httpServletResponse.addCookie(shopCookie);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveExternalToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(EXTERNAL_AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveShopifyToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SHOPIFY_AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

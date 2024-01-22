package com.et.security.jwt;

import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.et.security.jwt.JWTFilter.*;

public final class CSPHeaderWriter implements HeaderWriter {
    private static final String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public CSPHeaderWriter(TokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {

        String shop = null;
        String jwt = resolveToken(request);
        String externalJwt = resolveExternalToken(request);
        String shopifyJwt = resolveShopifyToken(request);

        if (StringUtils.hasText(externalJwt) && this.tokenProvider.validateExternalToken(externalJwt)) {
            Authentication authentication = this.tokenProvider.getExternalAuthentication(externalJwt);
            MDC.put("shop", authentication.getName());
            shop = authentication.getName();
        } else if (StringUtils.hasText(shopifyJwt) && this.tokenProvider.validateShopifyToken(shopifyJwt)) {
            Authentication authentication = this.tokenProvider.getShopifyAuthentication(shopifyJwt);
            MDC.put("shop", authentication.getName());
            shop = authentication.getName();
        } else if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            MDC.put("shop", authentication.getName());
            shop = authentication.getName();
        } else if (request.getServletPath().contains("/external/")) {

            Authentication authentication = this.tokenProvider.getExternalApiKeyAuthentication(
                request.getParameter(API_KEY_PARAM),
                request.getParameter(APP_KEY_PARAM)
            );

            if (authentication != null) {
                MDC.put("shop", authentication.getName());
                shop = authentication.getName();
            }
        } else if (request.getParameterMap().containsKey("shop")) {
            shop = request.getParameterMap().get("shop")[0];
        }


        if (shop != null) {
            String contentSecurityValue = "frame-ancestors https://" + shop + " https://admin.shopify.com;";
            response.setHeader(CONTENT_SECURITY_POLICY_HEADER, contentSecurityValue);
        }
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

    @Override
    public String toString() {
        return getClass().getName();
    }

}

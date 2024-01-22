package com.et.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ExternalUserPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;
    private ExternalTokenType externalTokenType;

    public ExternalUserPasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token, ExternalTokenType externalTokenType) {
        super(principal, credentials, authorities);
        this.token = token;
        this.externalTokenType = externalTokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ExternalTokenType getExternalTokenType() {
        return externalTokenType;
    }

    public void setExternalTokenType(ExternalTokenType externalTokenType) {
        this.externalTokenType = externalTokenType;
    }
}

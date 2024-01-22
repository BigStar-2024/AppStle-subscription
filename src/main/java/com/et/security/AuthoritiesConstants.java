package com.et.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String CUSTOMER_PORTAL = "ROLE_CUSTOMER_PORTAL";

    public static final String BUILD_A_BOX = "ROLE_BUILD_A_BOX";

    public static final String MERCHANT_PORTAL = "ROLE_MERCHANT_PORTAL";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}

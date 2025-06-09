package org.psc.share_food.config;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Configuration for authentication-related settings
 */
@ApplicationScoped
public class AuthConfig {
    
    // Cookie name for the session token
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    
    // Session expiration time in seconds (24 hours)
    private static final int SESSION_EXPIRATION_SECONDS = 86400;
    
    // Domain for the cookie
    private static final String COOKIE_DOMAIN = null; // null means the cookie will use the domain from the request
    
    // Path for the cookie
    private static final String COOKIE_PATH = "/";
    
    // Whether the cookie should be secure (HTTPS only)
    private static final boolean COOKIE_SECURE = false; // Set to true in production
    
    // Whether the cookie should be HTTP only (not accessible from JavaScript)
    private static final boolean COOKIE_HTTP_ONLY = true;
    
    // Secret key for signing the session token
    private final String secretKey;
    
    public AuthConfig() {
        // Generate a random secret key
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        this.secretKey = Base64.getEncoder().encodeToString(bytes);
    }
    
    public String getSessionCookieName() {
        return SESSION_COOKIE_NAME;
    }
    
    public int getSessionExpirationSeconds() {
        return SESSION_EXPIRATION_SECONDS;
    }
    
    public String getCookieDomain() {
        return COOKIE_DOMAIN;
    }
    
    public String getCookiePath() {
        return COOKIE_PATH;
    }
    
    public boolean isCookieSecure() {
        return COOKIE_SECURE;
    }
    
    public boolean isCookieHttpOnly() {
        return COOKIE_HTTP_ONLY;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
}
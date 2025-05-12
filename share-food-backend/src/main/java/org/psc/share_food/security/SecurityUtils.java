package org.psc.share_food.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

import java.security.Principal;
import java.util.Optional;

/**
 * Utility class for security-related operations
 */
@ApplicationScoped
public class SecurityUtils {

    @Inject
    private SecurityContext securityContext;

    /**
     * Gets the authenticated user from the security context
     * @return Optional containing the UserDetail if authenticated, empty otherwise
     */
    public Optional<UserDetail> getAuthenticatedUser() {
        Principal principal = securityContext.getCallerPrincipal();
        if (principal instanceof CustomPrincipal) {
            return Optional.of(((CustomPrincipal) principal).userDetail());
        }
        return Optional.empty();
    }

    /**
     * Checks if the current user has a specific role
     * @param role The role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return securityContext.isCallerInRole(role);
    }
}
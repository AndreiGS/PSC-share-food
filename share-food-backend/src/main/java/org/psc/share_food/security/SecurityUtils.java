package org.psc.share_food.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

import java.security.Principal;
import java.util.Optional;

@ApplicationScoped
public class SecurityUtils {

    @Inject
    private SecurityContext securityContext;

    public Optional<UserDetail> getAuthenticatedUser() {
        Principal principal = securityContext.getCallerPrincipal();
        if (principal instanceof CustomPrincipal) {
            return Optional.of(((CustomPrincipal) principal).userDetail());
        }
        return Optional.empty();
    }

    public boolean hasRole(String role) {
        return securityContext.isCallerInRole(role);
    }
}
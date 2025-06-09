package org.psc.share_food.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class CustomSecurityContextFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SecurityContext originalSecurityContext = requestContext.getSecurityContext();
        Principal principal = originalSecurityContext.getUserPrincipal();

        if (principal instanceof CustomPrincipal customPrincipal) {
            CustomSecurityContext customSecurityContext = new CustomSecurityContext(customPrincipal);
            requestContext.setSecurityContext(customSecurityContext);
        }
    }
}
package org.psc.share_food.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Cookie;
import org.psc.share_food.config.AuthConfig;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.service.AuthenticationService;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomAuthentication implements HttpAuthenticationMechanism {

    @Inject
    private AuthenticationService authService;

    @Inject
    private AuthConfig authConfig;

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext httpMessageContext) {

        // Check for session cookie-based authentication
        Cookie sessionCookie = extractSessionCookie(request);
        if (sessionCookie != null) {
             Optional<UserDto> userOptional = authService.validateSession(sessionCookie);

            if (userOptional.isPresent()) {
                UserDetail userDetail = convertToUserDetail(userOptional.get());

                Set<String> roles = userDetail.getRoles().stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .collect(Collectors.toSet());
                roles.addAll(userDetail.getAuthorities());

                return httpMessageContext.notifyContainerAboutLogin(
                        new CustomPrincipal(userDetail, roles),
                        roles);
            } else {
                return httpMessageContext.responseUnauthorized();
            }
        }

        // If no authentication was attempted, continue without authentication
        return httpMessageContext.doNothing();
    }

    private Cookie extractSessionCookie(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if (authConfig.getSessionCookieName().equals(cookie.getName())) {
                    return new Cookie.Builder(cookie.getName())
                            .value(cookie.getValue())
                            .build();
                }
            }
        }
        return null;
    }

    /**
     * Converts a UserDto to a UserDetail
     * @param userDto The UserDto to convert
     * @return A UserDetail object with the same data
     */
    private UserDetail convertToUserDetail(UserDto userDto) {
        return new UserDetail(
            userDto.getId(),
            userDto.getUsername(),
            userDto.getEmail(),
            userDto.getRoles(),
            userDto.getAuthorities(),
            userDto.getProvider()
        );
    }
}

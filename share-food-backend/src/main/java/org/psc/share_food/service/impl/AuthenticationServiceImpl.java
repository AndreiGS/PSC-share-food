package org.psc.share_food.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import org.psc.share_food.config.AuthConfig;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.dao.UserSessionDAO;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.entity.User;
import org.psc.share_food.entity.UserSession;
import org.psc.share_food.mapper.UserMapper;
import org.psc.share_food.service.AuthenticationService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private AuthConfig authConfig;

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserSessionDAO userSessionDAO;

    @Inject
    private UserMapper userMapper;

    @Inject
    public AuthenticationServiceImpl(AuthConfig authConfig, UserDAO userDAO, UserSessionDAO userSessionDAO, UserMapper userMapper) {
        this.authConfig = authConfig;
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.userMapper = userMapper;
    }

    @Override
    public NewCookie createSession(UserDto userDto) {
        // Generate a random session ID
        String sessionId = generateSessionId();

        // Get the user entity
        User user = userDAO.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Calculate expiration time
        Instant expirationTime = Instant.now().plusSeconds(authConfig.getSessionExpirationSeconds());

        // Create and save the session
        UserSession session = new UserSession(sessionId, user, expirationTime, userDto.getProvider());
        userSessionDAO.save(session);

        // Clean up expired sessions periodically
        userSessionDAO.deleteExpiredSessions(Instant.now());

        // Create a cookie with the session ID
        return new NewCookie(
                authConfig.getSessionCookieName(),
                sessionId,
                authConfig.getCookiePath(),
                authConfig.getCookieDomain(),
                "Authentication session",
                authConfig.getSessionExpirationSeconds(),
                authConfig.isCookieSecure(),
                authConfig.isCookieHttpOnly()
        );
    }

    @Override
    public Optional<UserDto> validateSession(Cookie sessionCookie) {
        if (sessionCookie == null) {
            return Optional.empty();
        }

        String sessionId = sessionCookie.getValue();
        
        // Find the session in the database
        return userSessionDAO.findValidSession(sessionId, Instant.now())
                .map(UserSession::getUser)
                .map(userMapper::toUserDto);
    }

    @Override
    public NewCookie invalidateSession() {
        // Create an expired cookie to invalidate the session on the client side
        return new NewCookie(
                authConfig.getSessionCookieName(),
                "",
                authConfig.getCookiePath(),
                authConfig.getCookieDomain(),
                "Authentication session",
                0, // Expire immediately
                authConfig.isCookieSecure(),
                authConfig.isCookieHttpOnly()
        );
    }

    @Override
    public boolean hasRole(UserDto user, String roleName) {
        return user.getRoles().contains(roleName);
    }

    @Override
    public boolean hasAuthority(UserDto user, String authorityName) {
        return user.getAuthorities().contains(authorityName);
    }

    private String generateSessionId() {
        // Generate a secure random session ID
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
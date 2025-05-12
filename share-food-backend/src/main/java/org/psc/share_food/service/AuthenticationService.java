package org.psc.share_food.service;

import org.psc.share_food.dto.UserDto;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

import java.util.Optional;

/**
 * Service for handling authentication-related operations
 */
public interface AuthenticationService {
    
    /**
     * Creates a session for a user and returns a cookie to be set in the response
     * @param user The authenticated user
     * @return A NewCookie object to be set in the response
     */
    NewCookie createSession(UserDto user);
    
    /**
     * Validates a session cookie and returns the user if valid
     * @param sessionCookie The session cookie from the request
     * @return Optional containing the user if the session is valid, empty otherwise
     */
    Optional<UserDto> validateSession(Cookie sessionCookie);
    
    /**
     * Invalidates a session
     * @return A NewCookie object that expires the session cookie
     */
    NewCookie invalidateSession();
    
    /**
     * Checks if a user has a specific role
     * @param user The user to check
     * @param roleName The role name to check for
     * @return true if the user has the role, false otherwise
     */
    boolean hasRole(UserDto user, String roleName);
    
    /**
     * Checks if a user has a specific authority
     * @param user The user to check
     * @param authorityName The authority name to check for
     * @return true if the user has the authority, false otherwise
     */
    boolean hasAuthority(UserDto user, String authorityName);
}
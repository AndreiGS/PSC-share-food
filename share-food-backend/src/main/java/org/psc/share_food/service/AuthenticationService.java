package org.psc.share_food.service;

import org.psc.share_food.dto.UserDto;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

import java.util.Optional;

public interface AuthenticationService {
    
    NewCookie createSession(UserDto user, boolean isSecure);
    Optional<UserDto> validateSession(Cookie sessionCookie);
    NewCookie invalidateSession(boolean isSecure);
    boolean hasRole(UserDto user, String roleName);
    boolean hasAuthority(UserDto user, String authorityName);
}
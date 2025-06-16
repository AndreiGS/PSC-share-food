package org.psc.share_food.dto;

import java.time.Instant;

/**
 * Event representing a user authentication event
 */
public class UserAuthenticatedEvent {

    private UserDto user;
    private Instant timestamp;
    private String authMethod;

    public UserAuthenticatedEvent() {
        this.timestamp = Instant.now();
    }

    public UserAuthenticatedEvent(UserDto user, String authMethod) {
        this();
        this.user = user;
        this.authMethod = authMethod;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }
}

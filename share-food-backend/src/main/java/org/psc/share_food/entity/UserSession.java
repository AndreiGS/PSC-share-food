package org.psc.share_food.entity;

import jakarta.persistence.*;
import org.psc.share_food.constant.OAuthProvider;

import java.time.Instant;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @Column(name = "session_id", length = 64)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiration_time", nullable = false)
    private Instant expirationTime;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OAuthProvider provider;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public UserSession() {
    }

    public UserSession(String sessionId, User user, Instant expirationTime, OAuthProvider provider) {
        this.sessionId = sessionId;
        this.user = user;
        this.expirationTime = expirationTime;
        this.provider = provider;
        this.createdAt = Instant.now();
    }

    // Getters and setters
    public String getSessionId() {
        return sessionId;
    }

    public UserSession setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserSession setUser(User user) {
        this.user = user;
        return this;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public UserSession setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public UserSession setProvider(OAuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserSession setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
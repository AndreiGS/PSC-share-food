package org.psc.share_food.security;

import org.psc.share_food.constant.OAuthProvider;

import java.util.Set;
import java.util.HashSet;

public class UserDetail {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();
    private Set<String> authorities = new HashSet<>();
    private OAuthProvider provider;

    public UserDetail() {
    }

    public UserDetail(Long id, String username, String email, Set<String> roles, Set<String> authorities, OAuthProvider provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.authorities = authorities;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public UserDetail setProvider(OAuthProvider provider) {
        this.provider = provider;
        return this;
    }
}
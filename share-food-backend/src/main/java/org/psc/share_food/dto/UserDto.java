package org.psc.share_food.dto;

import jakarta.json.bind.annotation.JsonbTransient;
import org.psc.share_food.constant.OAuthProvider;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDto implements Serializable {

    private Long id;

    @JsonbTransient
    private Boolean isNew;

    private String username;

    private String email;

    private Set<String> roles = new HashSet<>();
    
    private Set<String> authorities = new HashSet<>();

    private OAuthProvider provider;

    public UserDto() {
    }

    public UserDto(Long id, String username, String email,
                   Set<String> roles, Set<String> authorities, OAuthProvider provider) {
        this.id = id;
        this.isNew = false;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.authorities = authorities;
        this.provider = provider;
    }

    public UserDto(Long id, Boolean isNew, String username, String email,
                   Set<String> roles, Set<String> authorities, OAuthProvider provider) {
        this.id = id;
        this.isNew = isNew;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.authorities = authorities;
        this.provider = provider;
    }

    @JsonbTransient
    public Boolean isNew() {
        return isNew;
    }

    public UserDto setNew(Boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public UserDto setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public UserDto setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
        return this;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public UserDto setProvider(OAuthProvider provider) {
        this.provider = provider;
        return this;
    }
}

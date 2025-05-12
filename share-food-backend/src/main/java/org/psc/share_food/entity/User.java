package org.psc.share_food.entity;

import jakarta.persistence.*;
import org.psc.share_food.constant.OAuthProvider;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OAuthProvider provider;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> directAuthorities = new HashSet<>();

    public User() {
    }

    public User(String username, String email, OAuthProvider provider) {
        this.username = username;
        this.email = email;
        this.provider = provider;
    }

    public User(String username, String email, OAuthProvider provider, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.roles = roles;
    }

    public User(String username, String email, OAuthProvider provider, Set<Role> roles, Set<Authority> directAuthorities) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.roles = roles;
        this.directAuthorities = directAuthorities;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public User setProvider(OAuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Set<Authority> getDirectAuthorities() {
        return directAuthorities;
    }

    public User setDirectAuthorities(Set<Authority> directAuthorities) {
        this.directAuthorities = directAuthorities;
        return this;
    }

    public Set<Authority> getAllAuthorities() {
        Set<Authority> all = new HashSet<>(directAuthorities);
        for (Role role : roles) {
            all.addAll(role.getAuthorities());
        }
        return all;
    }
}
package org.psc.share_food.dto;

import java.io.Serializable;

public class GithubUserDto implements Serializable {

    private Long id;
    private String login;
    private String email;

    public GithubUserDto() {
    }

    public GithubUserDto(Long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public GithubUserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public GithubUserDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GithubUserDto setEmail(String email) {
        this.email = email;
        return this;
    }
}

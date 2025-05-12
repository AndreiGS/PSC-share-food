package org.psc.share_food.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.psc.share_food.config.GitHubOAuthConfig;
import org.psc.share_food.dao.RoleDAO;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.dto.GithubUserDto;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.dto.TokenResponse;
import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.entity.Role;
import org.psc.share_food.entity.User;
import org.psc.share_food.mapper.UserMapper;
import org.psc.share_food.service.OAuthService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Named("github-oauth-service")
@ApplicationScoped
@Transactional
public class GitHubOAuthService implements OAuthService {

    @Inject
    private GitHubOAuthConfig config;

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private UserMapper userMapper;

    @Inject
    public GitHubOAuthService(GitHubOAuthConfig config, UserDAO userDAO, RoleDAO roleDAO, UserMapper userMapper) {
        this.config = config;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userMapper = userMapper;
    }

    public Optional<UserDto> authenticate(String code) {
        try {
            TokenResponse tokenResponse = exchangeCodeForToken(code);
            GithubUserDto githubUser = getUserInfo(tokenResponse.getAccessToken());

            Optional<User> userOptional = userDAO.findByUsernameAndProvider(githubUser.getLogin(), OAuthProvider.GITHUB);
            User user = userOptional.orElseGet(
                    () -> {
                        Set<Role> roles = new HashSet<>();

                        // Add USER role to all users
                        Role userRole = roleDAO.findByName("ROLE_USER");
                        roles.add(userRole);

                        return userDAO.save(new User(githubUser.getLogin(), githubUser.getEmail(), OAuthProvider.GITHUB, roles));
                    });

            return Optional.of(userMapper.toUserDto(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Exchanges the authorization code for an access token
     * @param code The authorization code from GitHub
     * @return The access token response from GitHub
     */
    public TokenResponse exchangeCodeForToken(String code) {
        Client client = ClientBuilder.newClient();
        Form form = new Form()
                .param("client_id", config.getClientId())
                .param("client_secret", config.getClientSecret())
                .param("code", code)
                .param("redirect_uri", config.getRedirectUri());

        String json;
        try (Response response = client.target(config.getTokenUrl())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Accept", MediaType.APPLICATION_JSON)
                .post(Entity.form(form))) {

            json = response.readEntity(String.class);
        }

        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(json, TokenResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token information", e);
        }
    }

    /**
     * Fetches the user information from GitHub using the access token
     * @param accessToken The GitHub access token
     * @return The GitHub user information
     */
    public GithubUserDto getUserInfo(String accessToken) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(config.getUserInfoUrl())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .get();
        String json = response.readEntity(String.class);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(json, GithubUserDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GitHub user information", e);
        }
    }
}

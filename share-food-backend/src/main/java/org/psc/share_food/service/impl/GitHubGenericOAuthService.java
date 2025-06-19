package org.psc.share_food.service.impl;

import com.mysql.cj.log.Log;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
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
public class GitHubGenericOAuthService extends GenericOAuthService implements OAuthService {

    private static final Logger LOG = Logger.getLogger(GitHubGenericOAuthService.class);

    @Inject
    private GitHubOAuthConfig config;

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private UserMapper userMapper;

    @Inject
    public GitHubGenericOAuthService(GitHubOAuthConfig config, UserDAO userDAO, RoleDAO roleDAO, UserMapper userMapper) {
        this.config = config;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userMapper = userMapper;
    }

    public Optional<UserDto> authenticateByCode(String code) {
        try {
            Optional<TokenResponse> tokenResponseOpt = exchangeCodeForToken(code);
            if (tokenResponseOpt.isEmpty()) {
                LOG.error("Failed to exchange code for token");
                return Optional.empty();
            }

            TokenResponse tokenResponse = tokenResponseOpt.get();
            Optional<GithubUserDto> githubUserOpt = getUserInfo(tokenResponse.getAccessToken());
            if (githubUserOpt.isEmpty()) {
                LOG.error("Failed to get GitHub user information");
                return Optional.empty();
            }

            GithubUserDto githubUser = githubUserOpt.get();
            LOG.info("GitHub user: " + githubUser.getLogin());

            Optional<User> userOptional = userDAO.findByUsernameAndProvider(githubUser.getLogin(), OAuthProvider.GITHUB);
            User user = userOptional.orElse(new User());
            if (userOptional.isPresent()) {
                if (!areEqual(githubUser, userOptional.get())) {
                    user.setEmail(githubUser.getEmail());
                    userDAO.save(user);
                    LOG.info("Updated user: " + user.getUsername());
                }
                LOG.info("User already exists: " + user.getUsername());
            } else {
                Set<Role> roles = new HashSet<>();

                // Add USER role to all users
                Role userRole = roleDAO.findByName("USER");
                roles.add(userRole);

                user = userDAO.save(new User(githubUser.getLogin(), githubUser.getEmail(), OAuthProvider.GITHUB, roles));
                LOG.info("Created new user: " + user.getUsername());
            }

            return Optional.of(userMapper.toUserDto(user));
        } catch (Exception e) {
            LOG.error("Failed to authenticate user", e);
            return Optional.empty();
        }
    }

    private boolean areEqual(GithubUserDto githubUser, User user) {
        return ((githubUser.getEmail() != null && githubUser.getEmail().equals(user.getEmail()))
                    || (githubUser.getEmail() == null && user.getEmail() == null))
                && githubUser.getLogin().equals(user.getUsername());
    }

    public Optional<TokenResponse> exchangeCodeForToken(String code) {
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

            if (response.getStatus() != 200) {
                LOG.error("Failed to exchange code for token: " + response.getStatus());
                LOG.error(response.readEntity(String.class));
                return Optional.empty();
            }

            json = response.readEntity(String.class);
        }

        LOG.info("User token response: " + json);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return Optional.of(jsonb.fromJson(json, TokenResponse.class));
        } catch (Exception e) {
            LOG.error("Failed to parse token response", e);
            return Optional.empty();
        }
    }

    public Optional<GithubUserDto> getUserInfo(String accessToken) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(config.getUserInfoUrl())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get();
        if (response.getStatus() != 200) {
            LOG.error("Failed to get GitHub user information: " + response.getStatus());
            LOG.error(response.readEntity(String.class));
            return Optional.empty();
        }

        String json = response.readEntity(String.class);
        LOG.info("GitHub user information: " + json);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return Optional.of(jsonb.fromJson(json, GithubUserDto.class));
        } catch (Exception e) {
            LOG.error("Failed to parse GitHub user information", e);
            return Optional.empty();
        }
    }
}

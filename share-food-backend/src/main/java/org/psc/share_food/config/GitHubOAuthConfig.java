package org.psc.share_food.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.psc.share_food.pubsub.impl.AwsSecretManager;

import java.util.ResourceBundle;

@Named
@ApplicationScoped
public class GitHubOAuthConfig {
    
    private final ResourceBundle bundle;

    @Inject
    private AwsSecretManager awsSecretManager;

    public GitHubOAuthConfig() {
        this.bundle = ResourceBundle.getBundle("META-INF/github-oauth");
    }
    
    public String getClientId() {
        return bundle.getString("github.client.id");
    }
    
    public String getClientSecret() {
        String secretName = bundle.getString("github.client.secret");
        return awsSecretManager.getSecret(secretName);
//        return bundle.getString("github.client.secret");
    }
    
    public String getRedirectUri() {
        return bundle.getString("github.redirect.uri");
    }
    
    public String getTokenUrl() {
        return "https://github.com/login/oauth/access_token";
    }
    
    public String getUserInfoUrl() {
        return "https://api.github.com/user";
    }
}

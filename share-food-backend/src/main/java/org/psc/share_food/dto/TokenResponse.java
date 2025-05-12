package org.psc.share_food.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import java.io.Serializable;

public class TokenResponse implements Serializable {
    
    @JsonbProperty("access_token")
    private String accessToken;
    
    @JsonbProperty("token_type")
    private String tokenType;
    
    private String scope;
    
    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

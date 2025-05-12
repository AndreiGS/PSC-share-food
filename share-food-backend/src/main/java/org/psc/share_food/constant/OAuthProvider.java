package org.psc.share_food.constant;

public enum OAuthProvider {
    GITHUB;

    public String provider() {
        return name().toLowerCase();
    }

    public static OAuthProvider byName(String provider) {
        return valueOf(provider.toUpperCase());
    }
}

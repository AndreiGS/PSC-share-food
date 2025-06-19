package org.psc.share_food.security;

import java.security.Principal;
import java.util.Set;

public record CustomPrincipal(UserDetail userDetail, Set<String> authorities) implements Principal {

    @Override
    public String getName() {
        return userDetail.getUsername();
    }

    public boolean hasRole(String role) {
        if (role.startsWith("ROLE_")) {
            return authorities.contains(role);
        }
        return authorities.contains("ROLE_" + role);
    }

    public boolean hasAuthority(String authority) {
        return authorities.contains(authority);
    }

}
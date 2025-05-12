package org.psc.share_food.security;

import java.security.Principal;

/**
 * Custom implementation of Principal that holds user details
 */
public record CustomPrincipal(UserDetail userDetail) implements Principal {

    @Override
    public String getName() {
        return userDetail.getUsername();
    }

}
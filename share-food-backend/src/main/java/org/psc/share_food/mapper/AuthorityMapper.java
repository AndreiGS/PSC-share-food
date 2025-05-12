package org.psc.share_food.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.psc.share_food.entity.Authority;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthorityMapper {

    @Inject
    public AuthorityMapper() {

    }

    public String toString(Authority authority) {
        return authority.getName();
    }

    public Set<String> toStringSet(Set<Authority> authorities) {
        return authorities.stream()
                .map(this::toString)
                .collect(Collectors.toSet());
    }
}

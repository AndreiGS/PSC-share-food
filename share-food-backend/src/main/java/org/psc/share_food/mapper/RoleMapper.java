package org.psc.share_food.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.psc.share_food.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleMapper {

    @Inject
    public RoleMapper() {

    }

    public String toString(Role role) {
        return role.getName();
    }

    public Set<String> toStringSet(Set<Role> roles) {
        return roles.stream()
                .map(this::toString)
                .collect(Collectors.toSet());
    }
}

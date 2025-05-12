package org.psc.share_food.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.entity.User;

@ApplicationScoped
public class UserMapper {

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private AuthorityMapper authorityMapper;

    public UserMapper() {
    }

    @Inject
    public UserMapper(RoleMapper roleMapper, AuthorityMapper authorityMapper) {
        this.roleMapper = roleMapper;
        this.authorityMapper = authorityMapper;
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleMapper.toStringSet(user.getRoles()),
                authorityMapper.toStringSet(user.getAllAuthorities()),
                user.getProvider()
        );
    }
}

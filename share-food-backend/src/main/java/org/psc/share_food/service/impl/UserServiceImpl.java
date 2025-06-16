package org.psc.share_food.service.impl;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.mapper.UserMapper;
import org.psc.share_food.service.UserService;

import java.util.Optional;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    @Inject
    public UserServiceImpl(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<UserDto> getUser(Long id) {
        return userDAO.findById(id)
                .map(user -> userMapper.toUserDto(user));
    }
}

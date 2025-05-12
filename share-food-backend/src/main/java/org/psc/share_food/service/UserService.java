package org.psc.share_food.service;

import org.psc.share_food.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> getUser(Long id);
}

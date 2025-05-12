package org.psc.share_food.dao;

import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.GenericDAO;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User, Long> {
    Optional<User> findByUsernameAndProvider(String username, OAuthProvider provider);
}

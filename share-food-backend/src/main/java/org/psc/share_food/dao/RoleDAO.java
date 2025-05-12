package org.psc.share_food.dao;

import org.psc.share_food.entity.Role;
import org.psc.share_food.utils.repository.GenericDAO;

public interface RoleDAO extends GenericDAO<Role, Long> {
    Role findByName(String name);
}

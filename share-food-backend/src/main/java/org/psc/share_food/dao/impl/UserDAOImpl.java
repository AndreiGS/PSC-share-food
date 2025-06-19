package org.psc.share_food.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.impl.GenericDAOImpl;

import java.util.Optional;

@Stateless
public class UserDAOImpl extends GenericDAOImpl<User, Long>
        implements UserDAO {

    @PersistenceContext(unitName = "share-food-pu")
    private EntityManager entityManager;

    @Inject
    public UserDAOImpl() {
        super(User.class);
    }

    public Optional<User> findByUsernameAndProvider(String username, OAuthProvider provider) {
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username AND u.provider = :provider", User.class)
                    .setParameter("username", username)
                    .setParameter("provider", provider)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
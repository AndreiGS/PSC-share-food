package org.psc.share_food.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.psc.share_food.dao.UserSessionDAO;
import org.psc.share_food.entity.UserSession;
import org.psc.share_food.utils.repository.impl.GenericDAOImpl;

import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class UserSessionDAOImpl extends GenericDAOImpl<UserSession, String> implements UserSessionDAO {

    @PersistenceContext(unitName = "share-food-pu")
    private EntityManager entityManager;

    public UserSessionDAOImpl() {
        super(UserSession.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Optional<UserSession> findValidSession(String sessionId, Instant currentTime) {
        return entityManager.createQuery(
                "SELECT s FROM UserSession s WHERE s.sessionId = :sessionId AND s.expirationTime > :currentTime",
                UserSession.class)
                .setParameter("sessionId", sessionId)
                .setParameter("currentTime", currentTime)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteExpiredSessions(Instant currentTime) {
        entityManager.createQuery(
                "DELETE FROM UserSession s WHERE s.expirationTime <= :currentTime")
                .setParameter("currentTime", currentTime)
                .executeUpdate();
    }

    @Override
    public void deleteAllUserSessions(Long userId) {
        entityManager.createQuery(
                "DELETE FROM UserSession s WHERE s.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}

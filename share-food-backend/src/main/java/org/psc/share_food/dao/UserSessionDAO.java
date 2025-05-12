package org.psc.share_food.dao;

import org.psc.share_food.entity.UserSession;
import org.psc.share_food.utils.repository.GenericDAO;

import java.time.Instant;
import java.util.Optional;

public interface UserSessionDAO extends GenericDAO<UserSession, String> {
    Optional<UserSession> findValidSession(String sessionId, Instant currentTime);
    void deleteExpiredSessions(Instant currentTime);
    void deleteAllUserSessions(Long userId);
}
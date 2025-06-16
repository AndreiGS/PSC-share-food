package org.psc.share_food.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.psc.share_food.dao.OutboxDAO;
import org.psc.share_food.entity.OutboxEvent;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.GenericDAO;
import org.psc.share_food.utils.repository.impl.GenericDAOImpl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OutboxDAOImpl extends GenericDAOImpl<OutboxEvent, UUID>
        implements OutboxDAO {

    @PersistenceContext(unitName = "share-food-pu")
    private EntityManager entityManager;

    @Inject
    public OutboxDAOImpl() {
        super(OutboxEvent.class);
    }

    public List<OutboxEvent> findUnprocessedEvents(int limit) {
        return entityManager.createQuery(
                "SELECT e FROM OutboxEvent e WHERE e.processedAt IS NULL ORDER BY e.createdAt",
                OutboxEvent.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}

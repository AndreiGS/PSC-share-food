package org.psc.share_food.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.psc.share_food.dao.DonationRequestDAO;
import org.psc.share_food.entity.DonationRequest;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.impl.GenericDAOImpl;

import java.util.List;

@Stateless
public class DonationRequestDAOImpl extends GenericDAOImpl<DonationRequest, Long>
        implements DonationRequestDAO {

    @PersistenceContext(unitName = "share-food-pu")
    private EntityManager entityManager;

    @Inject
    public DonationRequestDAOImpl() {
        super(DonationRequest.class);
    }

    @Override
    public List<DonationRequest> findByUser(User user) {
        return entityManager.createQuery(
                "SELECT d FROM DonationRequest d WHERE d.user = :user ORDER BY d.createdAt DESC", DonationRequest.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<DonationRequest> findByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT d FROM DonationRequest d WHERE d.user.id = :userId ORDER BY d.createdAt DESC", DonationRequest.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
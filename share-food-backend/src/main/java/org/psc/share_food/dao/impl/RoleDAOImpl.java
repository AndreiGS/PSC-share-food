package org.psc.share_food.dao.impl;


import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.psc.share_food.dao.RoleDAO;
import org.psc.share_food.entity.Role;
import org.psc.share_food.utils.repository.impl.GenericDAOImpl;

@Stateless
public class RoleDAOImpl extends GenericDAOImpl<Role, Long>
        implements RoleDAO {

//    @Inject
//    private EntityManager entityManager;
//
//    @Inject
//    public RoleDAOImpl(EntityManager entityManager) {
//        super(Role.class);
//        this.entityManager = entityManager;
//    }

    @PersistenceContext(unitName = "share-food-pu")
    private EntityManager entityManager;

    @Inject
    public RoleDAOImpl() {
        super(Role.class);
    }

    @Override
    public Role findByName(String name) {
        EntityManager em = getEntityManager();
        return em.createQuery(
                "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}

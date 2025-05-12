package org.psc.share_food.utils.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.psc.share_food.utils.repository.GenericDAO;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionImpl;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    private final Class<T> entityClass;

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

//    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
//        EntityManager em = getEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            R result = action.apply(em);
//            tx.commit();
//            return result;
//        } catch (RuntimeException e) {
//            if (tx.isActive()) tx.rollback();
//            throw e;
//        }
//    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = getEntityManager();
        T value = em.find(entityClass, id);
        return Optional.ofNullable(value);
    }

    @Override
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        return em.createQuery(String.format("SELECT e FROM %s e", entityClass.getSimpleName()), entityClass)
            .getResultList();
    }

    @Override
    public T save(T entity) {
        EntityManager em = getEntityManager();
        em.persist(entity);
        return entity;
    }

    public void printDatabaseUrl(EntityManager entityManager) {
        try {
            SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
            JdbcConnectionAccess connectionAccess = session
                    .getJdbcServices()
                    .getBootstrapJdbcConnectionAccess();

            try (Connection connection = connectionAccess.obtainConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("Database URL: " + metaData.getURL());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = getEntityManager();
        return em.merge(entity);
    }

    @Override
    public boolean delete(T entity) {
        EntityManager em = getEntityManager();
        if (em.contains(entity)) {
            em.remove(entity);
            return true;
        }

        Optional<Field> idFieldOptional = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();

        if (idFieldOptional.isEmpty()) {
            return false;
        }

        Field idField = idFieldOptional.get();
        try {
            idField.setAccessible(true);
            Object id = idField.get(entity);
            if (id == null) {
                return false;
            }

            @SuppressWarnings("unchecked") Optional<T> existingEntity = findById((ID) id);
            if (existingEntity.isPresent()) {
                em.remove(existingEntity.get());
                return true;
            }
        } catch (IllegalAccessException e) {
            return false;
        }

        return false;
    }

    @Override
    public boolean deleteById(ID id) {
        EntityManager em = getEntityManager();
        Optional<T> entity = findById(id);
        if (entity.isPresent() && em.contains(entity.get())) {
            em.remove(entity);
            return true;
        }

        return false;
    }

    protected abstract EntityManager getEntityManager();

}

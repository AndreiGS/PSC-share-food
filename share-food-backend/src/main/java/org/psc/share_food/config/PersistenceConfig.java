package org.psc.share_food.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//@ApplicationScoped
public class PersistenceConfig {
//
//    @Produces
//    @ApplicationScoped
//    public EntityManagerFactory createEntityManagerFactory() {
//        return Persistence.createEntityManagerFactory("share-food-persistence-unit");
//    }
//
//    @Produces
//    public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
//        return entityManagerFactory.createEntityManager();
//    }
//
//    public void closeEntityManager(@Disposes EntityManager entityManager) {
//        if (entityManager.isOpen()) {
//            entityManager.close();
//        }
//    }
//
//    public void closeEntityManagerFactory(@Disposes EntityManagerFactory entityManagerFactory) {
//        if (entityManagerFactory.isOpen()) {
//            entityManagerFactory.close();
//        }
//    }
}

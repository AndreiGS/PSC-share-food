package org.psc.share_food.utils.repository;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID>
{
    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    boolean delete(T entity);

    boolean deleteById(ID id);
}

package com.marketplace.eventstore.jdbc;

import java.util.List;
import java.util.Optional;

public interface Repository<T, U> {
    List<T> save(List<T> entity);

    T save(T entity);

    Optional<T> findById(U id);

    List<T> findAll();
}

package com.marketplace.domain.repository;

import java.util.Optional;

public interface Repository<T, U> {
    /**
     * @param id
     * @return
     */
    boolean exists(U id);

    /**
     * @param id
     * @return
     */
    Optional<T> load(U id);

    /**
     * @param entity
     * @return
     */
    T add(T entity);

    default String entityId(U id, Class<T> tClass) {
        return String.format("%s:%s", tClass.getSimpleName(), id.toString());
    }

    default void deleteAll() {
    }
}

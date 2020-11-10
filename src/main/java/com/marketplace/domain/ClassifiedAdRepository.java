package com.marketplace.domain;

import java.util.Optional;

public interface ClassifiedAdRepository {
    /**
     * @param id
     * @return
     */
    boolean exists(ClassifiedAdId id);

    /**
     * @param id
     * @return
     */
    Optional<ClassifiedAd> load(ClassifiedAdId id);

    /**
     * @param entity
     * @return
     */
    ClassifiedAd add(ClassifiedAd entity);

    default String entityId(ClassifiedAdId id) {
        return String.format("ClassifiedAd:%s", id.toString());
    }
}

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
    ClassifiedAd save(ClassifiedAd entity);
}

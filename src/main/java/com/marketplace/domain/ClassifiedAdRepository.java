package com.marketplace.domain;

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
    ClassifiedAd load(ClassifiedAdId id);

    /**
     * @param entity
     * @return
     */
    ClassifiedAd save(ClassifiedAd entity);
}

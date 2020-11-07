package com.marketplace.domain;

import java.util.*;

public class ClassifiedAdRepositoryImpl implements ClassifiedAdRepository {
    Map<ClassifiedAdId, ClassifiedAd> cache = new HashMap<>();

    @Override
    public boolean exists(ClassifiedAdId id) {
        return cache.containsKey(id);
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public ClassifiedAd save(ClassifiedAd entity) {
        Objects.requireNonNull(entity, "entity cannot be null");
        ClassifiedAdId id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("entity id cannot be null");
        }
        cache.put(id, entity);
        return entity;
    }
}

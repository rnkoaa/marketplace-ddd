package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;

import java.util.*;

public class ClassifiedAdCommandRepositoryImpl implements ClassifiedAdCommandRepository {
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
    public ClassifiedAd add(ClassifiedAd entity) {
        Objects.requireNonNull(entity, "entity cannot be null");
        ClassifiedAdId id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("entity id cannot be null");
        }
        cache.put(id, entity);
        return entity;
    }
}

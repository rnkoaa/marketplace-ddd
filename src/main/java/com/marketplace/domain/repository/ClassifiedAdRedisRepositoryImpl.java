package com.marketplace.domain.repository;

import com.marketplace.domain.ClassifiedAd;
import com.marketplace.domain.ClassifiedAdId;
import com.marketplace.domain.ClassifiedAdRepository;

import java.util.Optional;

public class ClassifiedAdRedisRepositoryImpl implements ClassifiedAdRepository {
    private final RedisTemplate redisTemplate;

    public ClassifiedAdRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exists(ClassifiedAdId id) {
        return redisTemplate.exists(entityId(id));
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
        return redisTemplate.get(entityId(id), ClassifiedAd.class);
    }

    @Override
    public ClassifiedAd add(ClassifiedAd entity) {
        redisTemplate.put(entityId(entity.getId()), entity);
        return entity;
    }

}

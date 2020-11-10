package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.repository.RedisTemplate;

import java.util.Optional;

public class ClassifiedAdRedisRepositoryImpl implements ClassifiedAdRepository {
    private final RedisTemplate redisTemplate;

    public ClassifiedAdRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exists(ClassifiedAdId id) {
        return redisTemplate.exists(myId(id));
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
        return redisTemplate.get(myId(id), ClassifiedAd.class);
    }

    @Override
    public ClassifiedAd add(ClassifiedAd entity) {
        redisTemplate.put(myId(entity.getId()), entity);
        return entity;
    }

    private String myId(ClassifiedAdId id) {
        return entityId(id, ClassifiedAd.class);
    }

}

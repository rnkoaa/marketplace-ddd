package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.repository.RedisTemplate;
import java.util.Optional;

public class ClassifiedAdRedisCommandRepositoryImpl implements ClassifiedAdCommandRepository {
    private final RedisTemplate redisTemplate;

    public ClassifiedAdRedisCommandRepositoryImpl(RedisTemplate redisTemplate) {
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
    public Optional<ClassifiedAd> add(ClassifiedAd entity) {
        redisTemplate.put(myId(entity.getId()), entity);
        return Optional.of(entity);
    }

    private String myId(ClassifiedAdId id) {
        return entityId(id, ClassifiedAd.class);
    }

}

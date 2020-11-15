package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.repository.MongoTemplate;
import com.marketplace.domain.repository.RedisTemplate;

import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdMongoRepositoryImpl implements ClassifiedAdRepository {
    private final MongoTemplate mongoTemplate;

    @Inject
    public ClassifiedAdMongoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exists(ClassifiedAdId id) {
//        return redisTemplate.exists(myId(id));
        return false;
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
//        return redisTemplate.get(myId(id), ClassifiedAd.class);
        return Optional.empty();
    }

    @Override
    public ClassifiedAd add(ClassifiedAd entity) {
        return mongoTemplate.save(entity, ClassifiedAd.class);
    }

    private String myId(ClassifiedAdId id) {
        return entityId(id, ClassifiedAd.class);
    }

}

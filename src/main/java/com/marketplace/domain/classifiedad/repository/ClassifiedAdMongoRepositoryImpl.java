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
//        String idStr = id.toString();
//        Optional<ClassifiedAd> byId = mongoTemplate.findById(idStr, ClassifiedAd.class);
//        return byId.isPresent();
        return load(id).isPresent();
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
        String idStr = id.toString();
        return mongoTemplate.findById(idStr, ClassifiedAd.class);
    }

    @Override
    public ClassifiedAd add(ClassifiedAd entity) {
        return mongoTemplate.save(entity, ClassifiedAd.class);
    }

    private String myId(ClassifiedAdId id) {
        return entityId(id, ClassifiedAd.class);
    }

}

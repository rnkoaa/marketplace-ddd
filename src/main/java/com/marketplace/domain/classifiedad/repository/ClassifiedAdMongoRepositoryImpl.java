package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;
import com.marketplace.domain.repository.MongoTemplate;
import com.marketplace.domain.repository.RedisTemplate;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassifiedAdMongoRepositoryImpl implements ClassifiedAdRepository {
    private final MongoTemplate mongoTemplate;

    @Inject
    public ClassifiedAdMongoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exists(ClassifiedAdId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
        Optional<ClassifiedAdEntity> found = mongoTemplate.findById(id.id(), ClassifiedAdEntity.class);
        return found.map(ClassifiedAdEntity::toClassifiedAd);
    }

    @Override
    public ClassifiedAd add(ClassifiedAd entity) {
        var classifiedAdEntity = new ClassifiedAdEntity(entity);
        ClassifiedAdEntity save = mongoTemplate.save(classifiedAdEntity, ClassifiedAdEntity.class);
        if (save != null) {
            return entity;
        }
        return null;
    }

    @Override
    public List<ClassifiedAd> findAll() {
        List<ClassifiedAdEntity> all = mongoTemplate.findAll(ClassifiedAdEntity.class);
        return all.stream().map(ClassifiedAdEntity::toClassifiedAd).collect(Collectors.toList());
    }

    private String myId(ClassifiedAdId id) {
        return entityId(id, ClassifiedAd.class);
    }

}

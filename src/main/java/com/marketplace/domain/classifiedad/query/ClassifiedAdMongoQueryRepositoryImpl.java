package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdMongoQueryRepositoryImpl implements ClassifiedAdMongoQueryRepository {

    @Inject
    public ClassifiedAdMongoQueryRepositoryImpl() {
    }

    @Override
    public Optional<ClassifiedAdQueryEntity> findById(UUID id) {
//    Optional<ClassifiedAdEntity> found =
//        mongoTemplate.findById(id, collectionName, ClassifiedAdEntity.class);
//    return found.map(ClassifiedAdEntity::toClassifiedAdReadEntity);
        return Optional.empty();
    }

    @Override
    public List<ClassifiedAdQueryEntity> findAll() {
//    List<ClassifiedAdEntity> all = mongoTemplate.findAll(collectionName, ClassifiedAdEntity.class);
//    return convert(all);
        return List.of();
    }

    @Override
    public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
//    List<ClassifiedAdEntity> all =
//        mongoTemplate.findByQuery(collectionName, eq("owner", ownerId), ClassifiedAdEntity.class);
//    return convert(all);
        return List.of();
    }

    @Override
    public List<ClassifiedAdQueryEntity> find() {
//    var all = mongoTemplate.findByQuery(collectionName, filter, ClassifiedAdEntity.class);
//    return convert(all);
        return List.of();
    }

    List<ClassifiedAdQueryEntity> convert(List<ClassifiedAdEntity> entities) {
        return entities.stream()
            .map(ClassifiedAdEntity::toClassifiedAdReadEntity)
            .collect(Collectors.toList());
    }
}

package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Named
@Singleton
public class ClassifiedAdCommandRepositoryImpl implements ClassifiedAdCommandRepository {

    @Inject
    public ClassifiedAdCommandRepositoryImpl() {
    }

    @Override
    public boolean exists(ClassifiedAdId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<ClassifiedAd> load(ClassifiedAdId id) {
//    Optional<ClassifiedAdEntity> found = mongoTemplate.findById(id.id(), collectionName, ClassifiedAdEntity.class);
//    return found.map(it -> {
//      System.out.println(it);
//      return it.toClassifiedAd();
//    });
        return Optional.empty();
    }

    @Override
    public Optional<ClassifiedAd> add(ClassifiedAd entity) {
//    var classifiedAdEntity = new ClassifiedAdEntity(entity);
//    ClassifiedAdEntity save = mongoTemplate.add(classifiedAdEntity, entity.getId().id(), collectionName, ClassifiedAdEntity.class);
//    if (save != null) {
//      return entity;
//    }
//    return null;
        return Optional.empty();
    }

    @Override
    public List<ClassifiedAd> findAll() {
//    List<ClassifiedAdEntity> all = mongoTemplate.findAll(collectionName, ClassifiedAdEntity.class);
//    return all.stream().map(ClassifiedAdEntity::toClassifiedAd).collect(Collectors.toList());
        return List.of();
    }
}

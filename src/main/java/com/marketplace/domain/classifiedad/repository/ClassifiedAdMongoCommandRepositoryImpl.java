package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;
import com.marketplace.domain.repository.MongoTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@Singleton
public class ClassifiedAdMongoCommandRepositoryImpl implements ClassifiedAdCommandRepository {

  private final MongoTemplate mongoTemplate;
  private final String collectionName = ClassifiedAd.class.getSimpleName().toLowerCase();

  @Inject
  public ClassifiedAdMongoCommandRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public boolean exists(ClassifiedAdId id) {
    return load(id).isPresent();
  }

  @Override
  public Optional<ClassifiedAd> load(ClassifiedAdId id) {
    Optional<ClassifiedAdEntity> found = mongoTemplate.findById(id.id(), collectionName, ClassifiedAdEntity.class);
    return found.map(ClassifiedAdEntity::toClassifiedAd);
  }

  @Override
  public ClassifiedAd add(ClassifiedAd entity) {
    var classifiedAdEntity = new ClassifiedAdEntity(entity);
    ClassifiedAdEntity save = mongoTemplate.add(classifiedAdEntity, entity.getId().id(), collectionName, ClassifiedAdEntity.class);
    if (save != null) {
      return entity;
    }
    return null;
  }

  @Override
  public List<ClassifiedAd> findAll() {
    List<ClassifiedAdEntity> all = mongoTemplate.findAll(collectionName, ClassifiedAdEntity.class);
    return all.stream().map(ClassifiedAdEntity::toClassifiedAd).collect(Collectors.toList());
  }
}
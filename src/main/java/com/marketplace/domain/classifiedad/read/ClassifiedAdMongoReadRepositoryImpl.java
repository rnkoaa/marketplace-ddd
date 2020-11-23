package com.marketplace.domain.classifiedad.read;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;
import com.marketplace.domain.repository.MongoTemplate;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdMongoReadRepositoryImpl implements ReadRepository<ClassifiedAdReadEntity, UUID> {

  private final MongoTemplate mongoTemplate;
  private final String collectionName = ClassifiedAd.class.getSimpleName().toLowerCase();

  @Inject
  public ClassifiedAdMongoReadRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<ClassifiedAdReadEntity> findById(UUID id) {
    Optional<ClassifiedAdEntity> found = mongoTemplate.findById(id, collectionName, ClassifiedAdEntity.class);
    return found.map(ClassifiedAdEntity::toClassifiedAdReadEntity);
  }


}

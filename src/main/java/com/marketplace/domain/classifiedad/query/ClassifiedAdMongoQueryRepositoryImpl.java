package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;
import com.marketplace.domain.repository.MongoTemplate;
import java.util.Objects;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import reactor.core.publisher.Mono;

import static com.mongodb.client.model.Filters.eq;

@Named
@Singleton
public class ClassifiedAdMongoQueryRepositoryImpl implements ClassifiedAdMongoQueryRepository {

  private final MongoTemplate mongoTemplate;
  private final String collectionName = ClassifiedAd.class.getSimpleName().toLowerCase();

  @Inject
  public ClassifiedAdMongoQueryRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<ClassifiedAdQueryEntity> findById(UUID id) {
    Mono<Optional<ClassifiedAdEntity>> found =
        mongoTemplate.findById(id, collectionName, ClassifiedAdEntity.class);
    Optional<ClassifiedAdEntity> foundEntity = found.block();
    Objects.requireNonNull(foundEntity);
    return foundEntity.map(ClassifiedAdEntity::toClassifiedAdReadEntity);
  }

  @Override
  public List<ClassifiedAdQueryEntity> findAll() {
    Mono<List<ClassifiedAdEntity>> found = mongoTemplate.findAll(collectionName, ClassifiedAdEntity.class);
    List<ClassifiedAdEntity> foundEntity = found.block();
    Objects.requireNonNull(foundEntity);
    return convert(foundEntity);
  }

  @Override
  public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
    Mono<List<ClassifiedAdEntity>> found = mongoTemplate.findByQuery(collectionName, eq("owner", ownerId), ClassifiedAdEntity.class);
    List<ClassifiedAdEntity> foundEntity = found.block();
    Objects.requireNonNull(foundEntity);
    return convert(foundEntity);
  }

  @Override
  public List<ClassifiedAdQueryEntity> find(Bson filter) {
    Mono<List<ClassifiedAdEntity>> found = mongoTemplate.findByQuery(collectionName, filter, ClassifiedAdEntity.class);
    List<ClassifiedAdEntity> foundEntity = found.block();
    Objects.requireNonNull(foundEntity);
    return convert(foundEntity);
  }

  List<ClassifiedAdQueryEntity> convert(List<ClassifiedAdEntity> entities) {
    return entities.stream()
        .map(ClassifiedAdEntity::toClassifiedAdReadEntity)
        .collect(Collectors.toList());
  }
}

package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;
import com.marketplace.domain.repository.MongoTemplate;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

// these will block for now
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
    Mono<Optional<ClassifiedAdEntity>> found = mongoTemplate.findById(id.value(), collectionName, ClassifiedAdEntity.class);
//    return
    return found.map(opt -> opt.map(ClassifiedAdEntity::toClassifiedAd)).block();
  }

  @Override
  public ClassifiedAd add(ClassifiedAd entity) {
    var classifiedAdEntity = new ClassifiedAdEntity(entity);
    Mono<ClassifiedAdEntity> add = mongoTemplate.add(classifiedAdEntity, entity.getId().value(), collectionName, ClassifiedAdEntity.class);
    return add.flatMap(save -> {
      if (save != null) {
        return Mono.just(entity);
      }
      return Mono.empty();
    }).block();
  }

  @Override
  public List<ClassifiedAd> findAll() {
    Mono<List<ClassifiedAdEntity>> all = mongoTemplate.findAll(collectionName, ClassifiedAdEntity.class);
    return Objects.requireNonNull(all.switchIfEmpty(Mono.just(List.of()))
        .block())
        .stream()
        .map(ClassifiedAdEntity::toClassifiedAd).collect(Collectors.toList());
  }
}

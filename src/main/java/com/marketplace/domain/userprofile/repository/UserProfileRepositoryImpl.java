package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.repository.MongoTemplate;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;
import reactor.core.publisher.Mono;

@Named
@Singleton
public class UserProfileRepositoryImpl implements UserProfileRepository {

  private final String collectionName = UserProfile.class.getSimpleName().toLowerCase();
  private final MongoTemplate mongoTemplate;

  @Inject
  public UserProfileRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public boolean exists(UserId id) {
    return load(id).isPresent();
  }

  @Override
  public Optional<UserProfile> load(UserId id) {
    Mono<Optional<UserProfileEntity>> found = mongoTemplate.findById(id.value(), collectionName, UserProfileEntity.class);
    Optional<UserProfileEntity> optionalEntity = found.switchIfEmpty(Mono.just(Optional.empty())).block();
    Objects.requireNonNull(optionalEntity);
    return optionalEntity.map(UserProfileEntity::toUserProfile);
  }

  @Override
  public UserProfile add(UserProfile entity) {
    var userProfileEntity = UserProfileEntity.create(entity);
    var save = mongoTemplate.add(userProfileEntity, entity.getId().value(), collectionName, UserProfileEntity.class);
    UserProfileEntity block = save.block();
    if (block != null) {
      return entity;
    }
    return null;
  }

  @Override
  public void deleteAll() {
    mongoTemplate.deleteAll(collectionName, UserProfileEntity.class).block();
  }
}

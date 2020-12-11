package com.marketplace.domain.userprofile.entity;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.framework.Strings;
import com.marketplace.mongo.entity.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Entity
@Immutable
public abstract class UserProfileEntity implements MongoEntity {

  @Id
  abstract UUID getId();

  abstract String getFirstName();

  abstract String getMiddleName();

  abstract String getLastName();

  abstract String getDisplayName();

  abstract String getPhotoUrl();

  public static UserProfileEntity create(UserProfile entity) {
    return ImmutableUserProfileEntity.builder()
        .id(entity.getId().id())
        .firstName(entity.getFullName().firstName())
        .lastName(entity.getFullName().lastName())
        .middleName(entity.getFullName().middleName())
        .displayName(entity.getDisplayName().value())
        .photoUrl(entity.getPhotoUrl())

        .build();
  }

  @BsonIgnore
  public FullName fullName() {
    return new FullName(getFirstName(), getMiddleName(), getLastName());
  }

  @BsonIgnore
  public DisplayName displayName() {
    return new DisplayName(getDisplayName());
  }

  public static UserProfile toUserProfile(UserProfileEntity userProfileEntity) {
    UserProfile userProfile = new UserProfile(UserId.from(userProfileEntity.getId()),
        userProfileEntity.fullName(),
        userProfileEntity.displayName());
    if (!Strings.isNullOrEmpty(userProfileEntity.getPhotoUrl())) {
      userProfile.updatePhoto(userProfileEntity.getPhotoUrl());
    }
    return userProfile;
  }

  public static UserProfileEntity from(UserProfile userProfile) {
    return ImmutableUserProfileEntity.builder()
        .id(userProfile.getId().id())
        .firstName(userProfile.getFullName().firstName())
        .middleName(userProfile.getFullName().middleName())
        .lastName(userProfile.getFullName().lastName())
        .displayName(userProfile.getDisplayName().value())
        .photoUrl(userProfile.getPhotoUrl())
        .build();
  }

  // TODO use compile time error to validate that BsonIgnore is required here
  @BsonIgnore
  @Override
  public String getCollection() {
    return UserProfile.class.getSimpleName().toLowerCase();
  }
}

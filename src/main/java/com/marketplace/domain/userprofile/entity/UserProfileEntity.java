package com.marketplace.domain.userprofile.entity;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity.Builder;
import com.marketplace.cqrs.framework.Strings;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Entity
@Immutable
public abstract class UserProfileEntity  {

  @Id
  public abstract UUID getId();

  public abstract String getFirstName();

  public abstract String getMiddleName();

  public abstract String getLastName();

  public abstract String getDisplayName();

  public abstract Optional<String> getPhotoUrl();

  public static UserProfileEntity create(UserProfile entity) {
    var userProfileEntityBuilder = ImmutableUserProfileEntity.builder()
        .id(entity.getId().id())
        .firstName(entity.getFullName().firstName())
        .lastName(entity.getFullName().lastName())
        .middleName(entity.getFullName().middleName())
        .displayName(entity.getDisplayName().value());

    if (!Strings.isNullOrEmpty(entity.getPhotoUrl())) {
      userProfileEntityBuilder.photoUrl(entity.getPhotoUrl());
    }

    return userProfileEntityBuilder.build();
  }

  public FullName fullName() {
    return new FullName(getFirstName(), getMiddleName(), getLastName());
  }

  public DisplayName displayName() {
    return new DisplayName(getDisplayName());
  }

  public static UserProfile toUserProfile(UserProfileEntity userProfileEntity) {
    UserProfile userProfile = new UserProfile(UserId.from(userProfileEntity.getId()),
        userProfileEntity.fullName(),
        userProfileEntity.displayName());
    userProfileEntity.getPhotoUrl().ifPresent(userProfile::updatePhoto);
    return userProfile;
  }

  public static UserProfileEntity from(UserProfile userProfile) {
    Builder builder = ImmutableUserProfileEntity.builder()
        .id(userProfile.getId().id())
        .firstName(userProfile.getFullName().firstName())
        .middleName(userProfile.getFullName().middleName())
        .lastName(userProfile.getFullName().lastName())
        .displayName(userProfile.getDisplayName().value());
    if (!Strings.isNullOrEmpty(userProfile.getPhotoUrl())) {
      builder.photoUrl(userProfile.getPhotoUrl());
    }
    return builder.build();
  }

}

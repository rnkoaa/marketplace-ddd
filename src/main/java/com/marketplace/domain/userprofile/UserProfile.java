package com.marketplace.domain.userprofile;

import com.marketplace.domain.shared.IdGenerator;
import com.marketplace.domain.shared.IdGeneratorImpl;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.event.ImmutableProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.ImmutableUserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.ImmutableUserFullNameUpdated;
import com.marketplace.domain.userprofile.event.ImmutableUserRegistered;
import com.marketplace.domain.userprofile.event.ProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.UserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.UserFullNameUpdated;
import com.marketplace.domain.userprofile.event.UserRegistered;
import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import java.util.Objects;

public class UserProfile extends AggregateRoot<EventId, VersionedEvent> {

  private static final IdGenerator idGenerator = new IdGeneratorImpl();
  private static final String AGGREGATE_NAME = UserProfile.class.getSimpleName();
  private UserId id;
  private FullName fullName;
  private DisplayName displayName;
  private String photoUrl;

  public UserId getId() {
    return id;
  }

  public FullName getFullName() {
    return fullName;
  }

  public DisplayName getDisplayName() {
    return displayName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public UserProfile(UserId id, FullName fullName, DisplayName displayName) {
    apply(ImmutableUserRegistered.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .userId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .firstName(fullName.firstName())
        .lastName(fullName.lastName())
        .middleName(fullName.middleName())
        .displayName(displayName.value())
        .build());
  }


  public void updateUserFullName(FullName fullName) {
    apply(ImmutableUserFullNameUpdated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .userId(id.id())
        .firstName(fullName.firstName())
        .middleName(fullName.middleName())
        .lastName(fullName.lastName())
        .build());
  }

  public void updateDisplayName(DisplayName displayName) {
    apply(ImmutableUserDisplayNameUpdated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .userId(id.id())
        .displayName(displayName.value())
        .build());
  }


  public void updatePhoto(String uri) {
    apply(ImmutableProfilePhotoUploaded.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .userId(id.id())
        .photoUrl(uri)
        .build());
  }

  @Override
  public void ensureValidState(VersionedEvent event) {
//        boolean valid = id != null && displayName != null && fullName != null;
//        if (!valid) {
//            throw new IllegalArgumentException("state is not valid while processing event " + event.getClass());
//        }
  }

  @Override
  public void when(VersionedEvent event) {
    if (event instanceof UserRegistered e) {
      this.id = new UserId(e.getAggregateId());
      this.displayName = new DisplayName(e.getDisplayName());
      this.fullName = new FullName(e.getFirstName(), e.getMiddleName().orElse(""), e.getLastName());
    } else if (event instanceof ProfilePhotoUploaded e) {
      this.photoUrl = e.getPhotoUrl();
    } else if (event instanceof UserFullNameUpdated e) {
      this.fullName = new FullName(e.getFirstName(), e.getMiddleName().orElse(""), e.getLastName());
    } else if (event instanceof UserDisplayNameUpdated e) {
      this.displayName = new DisplayName(e.getDisplayName());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProfile that = (UserProfile) o;
    return Objects.equals(id, that.id) && Objects.equals(fullName, that.fullName)
        && Objects.equals(displayName, that.displayName) && Objects.equals(photoUrl, that.photoUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, displayName, photoUrl);
  }

}

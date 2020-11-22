package com.marketplace.domain.userprofile.entity;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.framework.Strings;
import com.marketplace.mongo.entity.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileEntity implements MongoEntity {
    @Id
    private UUID id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;
    private String photoUrl;

    public UserProfileEntity(UserProfile entity) {
        this.id = entity.getId().id();
        this.firstName = entity.getFullName().firstName();
        this.middleName = entity.getFullName().middleName();
        this.lastName = entity.getFullName().lastName();
        this.displayName = entity.getDisplayName().value();
        this.photoUrl = entity.getPhotoUrl();
    }

    @BsonIgnore
    public FullName fullName() {
        return new FullName(firstName, middleName, lastName);
    }

    @BsonIgnore
    public DisplayName displayName() {
        return new DisplayName(displayName);
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
        return UserProfileEntity.builder()
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

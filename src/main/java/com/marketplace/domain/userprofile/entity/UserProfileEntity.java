package com.marketplace.domain.userprofile.entity;

import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.mongo.entity.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity implements MongoEntity {
    @Id
    private UUID id;

    private FullName fullName;
    private DisplayName displayName;
    private String photoUrl;

    public UserProfileEntity(UserProfile entity) {
        this.id = entity.getId().id();
        this.fullName = entity.getFullName();
        this.displayName = entity.getDisplayName();
        this.photoUrl = entity.getPhotoUrl();
    }

    public static UserProfile toUserProfile(UserProfileEntity userProfileEntity) {
        return null;
    }

    @Override
    public String getCollection() {
        return getClass().getSimpleName().toLowerCase();
    }
}

package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.evenstore.jooq.tables.records.UserProfileRecord;
import java.util.UUID;

public class UserProfileMapper {

    public static UserProfileEntity convert(UserProfileRecord userProfileRecord) {

        if (userProfileRecord == null) {
            return null;
        }
        return
            ImmutableUserProfileEntity.builder()
                .id(UUID.fromString(userProfileRecord.getId()))
                .firstName(userProfileRecord.getFirstname())
                .middleName(userProfileRecord.getMiddlename())
                .lastName(userProfileRecord.getLastname())
                .displayName(userProfileRecord.getDisplayname())
                .photoUrl(userProfileRecord.getPhotos())
                .build();
    }

    public static UserProfileRecord convert(UserProfileEntity userProfileEntity) {
        UserProfileRecord userProfileRecord = new UserProfileRecord()
            .setId(userProfileEntity.getId().toString())
            .setFirstname(userProfileEntity.getFirstName())
            .setMiddlename(userProfileEntity.getMiddleName())
            .setLastname(userProfileEntity.getLastName())
            .setDisplayname(userProfileEntity.getDisplayName())
            ;

        userProfileEntity.getPhotoUrl().ifPresentOrElse(userProfileRecord::setPhotos, () -> userProfileRecord.setPhotos(""));

        return userProfileRecord;
    }
}

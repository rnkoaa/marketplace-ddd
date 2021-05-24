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
                .firstName(userProfileRecord.getFirstName())
                .middleName(userProfileRecord.getMiddleName())
                .lastName(userProfileRecord.getLastName())
                .displayName(userProfileRecord.getDisplayName())
                .photoUrl(userProfileRecord.getPhotos())
                .build();
    }

    public static UserProfileRecord convert(UserProfileEntity userProfileEntity) {
        UserProfileRecord userProfileRecord = new UserProfileRecord()
            .setId(userProfileEntity.getId().toString())
            .setFirstName(userProfileEntity.getFirstName())
            .setMiddleName(userProfileEntity.getMiddleName())
            .setLastName(userProfileEntity.getLastName())
            .setDisplayName(userProfileEntity.getDisplayName());

        userProfileEntity.getPhotoUrl()
            .ifPresentOrElse(userProfileRecord::setPhotos, () -> userProfileRecord.setPhotos(""));

        return userProfileRecord;
    }
}

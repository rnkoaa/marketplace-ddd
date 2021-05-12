package com.marketplace.domain.userprofile.query;

import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
import java.util.Optional;
import java.util.UUID;

public class UserProfileMapper {

    public static Optional<UserProfileEntity> convert(UserProfileRecord userProfileRecord) {

        if (userProfileRecord == null) {
            return Optional.empty();
        }
        return Optional.of(
            ImmutableUserProfileEntity.builder()
                .internalId(userProfileRecord.getId())
                .id(UUID.fromString(userProfileRecord.getUserProfileId()))
                .firstName(userProfileRecord.getFirstname())
                .middleName(userProfileRecord.getMiddlename())
                .lastName(userProfileRecord.getLastname())
                .displayName(userProfileRecord.getDisplayname())
                .photoUrl(userProfileRecord.getPhotos())
                .build()
        );
    }

    public UserProfileRecord convert(UserProfileEntity userProfileEntity) {
        UserProfileRecord userProfileRecord = new UserProfileRecord();
        if (userProfileEntity.getInternalId() > 0) {
            userProfileRecord.setId(userProfileEntity.getInternalId());
        } else {
            // new record
            userProfileRecord.setId(null);
        }
        userProfileRecord
            .setUserProfileId(userProfileEntity.getId().toString())
            .setFirstname(userProfileEntity.getFirstName()).
            setMiddlename(userProfileEntity.getMiddleName())
            .setLastname(userProfileEntity.getLastName())
            .setDisplayname(userProfileRecord.getDisplayname());

        userProfileEntity.getPhotoUrl().ifPresent(userProfileRecord::setPhotos);

        return userProfileRecord;
    }

}

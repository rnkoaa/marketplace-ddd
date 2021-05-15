package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Named
@Singleton
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final DSLContext dslContext;

    @Inject
    public UserProfileRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public boolean exists(UserId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<UserProfile> load(UserId id) {

        Optional<UserProfileRecord> optionalUser = fetchRecord(id.toString());

        return optionalUser
            .map(UserProfileMapper::convert)
            .map(UserProfileEntity::toUserProfile);
    }

    @Override
    public UserProfile add(UserProfile entity) {
        var userProfileEntity = UserProfileEntity.create(entity);
        var userExists = fetchRecord(entity.getId().toString());

        UserProfileRecord userProfileRecord = UserProfileMapper.convert(userProfileEntity);

        userProfileRecord.setUpdated(Instant.now().toString());

        UserProfileRecord savedUserProfileRecord;
        if (userExists.isPresent()) {
            UserProfileRecord existingUserProfile = userExists.get();
            userProfileRecord.setCreated(existingUserProfile.getCreated());
            savedUserProfileRecord = dslContext.update(Tables.USER_PROFILE)
                .set(userProfileRecord)
                .returning(Tables.USER_PROFILE.ID)
                .fetchOne();
        } else {
            userProfileRecord.setCreated(Instant.now().toString());
            savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
                .set(userProfileRecord)
                .returning(Tables.USER_PROFILE.ID)
                .fetchOne();
        }

        if (savedUserProfileRecord == null || savedUserProfileRecord.getId() == null) {
            return null;
        }
        return entity;
    }

    private Optional<UserProfileRecord> fetchRecord(String id) {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq(id))
            .fetchOptionalInto(UserProfileRecord.class);
    }

    @Override
    public void deleteAll() {
        dslContext.delete(Tables.USER_PROFILE).execute();
    }
}

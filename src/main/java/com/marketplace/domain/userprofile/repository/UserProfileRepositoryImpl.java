package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
import java.time.Instant;
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
        Optional<UserProfileRecord> optionalUser = userExists.map(existingUser -> {
            userProfileRecord.setCreated(existingUser.getCreated());
            return dslContext.update(Tables.USER_PROFILE)
                .set(userProfileRecord)
                .returning(Tables.USER_PROFILE.ID)
                .fetchOne();
        }).or(() -> {
            userProfileRecord.setCreated(Instant.now().toString());
            return dslContext.insertInto(Tables.USER_PROFILE)
                .set(userProfileRecord)
                .returning(Tables.USER_PROFILE.ID)
                .fetchOptional();
        }).filter(it -> it.getId() != null);
        return optionalUser.isPresent() ? entity : null;
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

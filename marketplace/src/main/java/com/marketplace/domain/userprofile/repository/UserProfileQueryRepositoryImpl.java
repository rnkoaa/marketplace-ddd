package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.evenstore.jooq.Tables;
import com.marketplace.evenstore.jooq.tables.records.UserProfileRecord;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Named
@Singleton
public class UserProfileQueryRepositoryImpl implements UserProfileQueryRepository {

    private final DSLContext dslContext;

    @Inject
    public UserProfileQueryRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Optional<UserProfileEntity> findById(UUID id) {
        Optional<UserProfileRecord> optionalUser = fetchRecord(id.toString());

        return optionalUser
            .map(UserProfileMapper::convert);
    }

    @Override
    public Optional<UserProfileEntity> findByDisplayName(String displayName) {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.DISPLAY_NAME.eq(displayName))
            .fetchOptionalInto(UserProfileRecord.class)
            .map(UserProfileMapper::convert);
    }

    @Override
    public List<UserProfileEntity> findAll() {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .fetchInto(UserProfileRecord.class)
            .stream()
            .map(UserProfileMapper::convert)
            .toList();
    }

    @Override
    public Optional<UserProfileEntity> update(UserProfileEntity entity) {
        UserProfileRecord userProfileRecord = UserProfileMapper.convert(entity);
        userProfileRecord.setUpdated(Instant.now().toString());
        return dslContext.update(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOptional()
            .map(it -> entity);
    }

    @Override
    public Optional<UserProfileEntity> save(UserProfileEntity entity) {
        var userExists = fetchRecord(entity.getId().toString());

        UserProfileRecord userProfileRecord = UserProfileMapper.convert(entity);

        userProfileRecord.setUpdated(Instant.now().toString());
        return userExists.map(existingUser -> {
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
        }).filter(it -> it.getId() != null)
            .map(it -> entity);
    }

    @Override
    public void deleteAll() {
        dslContext.truncate(Tables.USER_PROFILE).execute();
        dslContext.delete(Tables.USER_PROFILE).execute();
    }

    private Optional<UserProfileRecord> fetchRecord(String id) {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq(id))
            .fetchOptionalInto(UserProfileRecord.class);
    }

}

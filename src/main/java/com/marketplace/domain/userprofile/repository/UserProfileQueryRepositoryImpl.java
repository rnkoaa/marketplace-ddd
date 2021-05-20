package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
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
    public List<UserProfileEntity> findAll() {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .fetchInto(UserProfileRecord.class)
            .stream()
            .map(UserProfileMapper::convert)
            .toList();
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

    private Optional<UserProfileRecord> fetchRecord(String id) {
        return dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq(id))
            .fetchOptionalInto(UserProfileRecord.class);
    }

}

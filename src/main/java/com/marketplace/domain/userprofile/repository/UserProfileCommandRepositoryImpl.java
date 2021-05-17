package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.repository.Repository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.eventstore.jdbc.Tables;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Named
@Singleton
public class UserProfileCommandRepositoryImpl implements UserProfileCommandRepository {

    private final DSLContext dslContext;

    @Inject
    public UserProfileCommandRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public boolean exists(UserId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<UserProfile> load(UserId id) {
//        fetchEvents

//        Optional<UserProfileRecord> optionalUser = fetchRecord(id.toString());
//
//        return optionalUser
//            .map(UserProfileMapper::convert)
//            .map(UserProfileEntity::toUserProfile);
        return Optional.empty();
    }

    @Override
    public Optional<UserProfile> add(UserProfile entity) {
//        var userProfileEntity = UserProfileEntity.create(entity);
//        var userExists = fetchRecord(entity.getId().toString());
//
//        UserProfileRecord userProfileRecord = UserProfileMapper.convert(userProfileEntity);
//
//        userProfileRecord.setUpdated(Instant.now().toString());
//        return userExists.map(existingUser -> {
//            userProfileRecord.setCreated(existingUser.getCreated());
//            return dslContext.update(Tables.USER_PROFILE)
//                .set(userProfileRecord)
//                .returning(Tables.USER_PROFILE.ID)
//                .fetchOne();
//        }).or(() -> {
//            userProfileRecord.setCreated(Instant.now().toString());
//            return dslContext.insertInto(Tables.USER_PROFILE)
//                .set(userProfileRecord)
//                .returning(Tables.USER_PROFILE.ID)
//                .fetchOptional();
//        }).filter(it -> it.getId() != null)
//            .map(it -> entity);
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        dslContext.delete(Tables.USER_PROFILE).execute();
    }
}

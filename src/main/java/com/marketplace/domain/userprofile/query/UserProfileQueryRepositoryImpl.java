package com.marketplace.domain.userprofile.query;

import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Named
//@Singleton
public class UserProfileQueryRepositoryImpl implements UserProfileQueryRepository {

//    private final DSLContext dslContext;
//
//    public UserProfileQueryRepositoryImpl(DSLContext dslContext) {
//        this.dslContext = dslContext;
//    }

    @Override
    public Optional<UserProfileEntity> findById(UUID id) {
//        UserProfileRecord userProfileRecord = dslContext.selectOne()
//            .from(Tables.USER_PROFILE)
//            .where(Tables.USER_PROFILE.USER_PROFILE_ID.eq(id.toString()))
//            .fetchOneInto(UserProfileRecord.class);
//
//        return UserProfileMapper.convert(userProfileRecord);
        return Optional.empty();
    }

    @Override
    public List<UserProfileEntity> findAll() {
//        List<UserProfileRecord> userProfileRecords = dslContext.selectOne()
//            .from(Tables.USER_PROFILE)
//            .fetchInto(UserProfileRecord.class);
//
//        return userProfileRecords.stream()
//            .map(UserProfileMapper::convert)
//            .filter(Optional::isPresent)
//            .map(Optional::get)
//            .toList();
        return List.of();
    }
}

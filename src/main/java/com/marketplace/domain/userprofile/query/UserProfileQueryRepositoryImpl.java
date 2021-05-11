package com.marketplace.domain.userprofile.query;

public class UserProfileQueryRepositoryImpl implements UserProfileQueryRepository{
/*
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
        UserProfileRecord userProfileRecord = dslContext.select().from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.USER_PROFILE_ID.eq(id.toString()))
            .fetchOneInto(UserProfileRecord.class);
        return Optional.empty();
    }

    @Override
    public UserProfile add(UserProfile entity) {
        return null;
    }

    @Override
    public void deleteAll() {
    }
 */
}

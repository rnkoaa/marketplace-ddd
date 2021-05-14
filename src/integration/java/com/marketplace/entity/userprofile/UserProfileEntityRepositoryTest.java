package com.marketplace.entity.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UserProfileEntityRepositoryTest extends BaseRepositoryTest {

    @Test
    void testSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = generateUserProfile();

        UserProfileRecord savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOne();

        assertThat(savedUserProfileRecord).isNotNull();

        assertThat(savedUserProfileRecord.getId()).isNotNull()
            .isEqualTo("hello-id");
    }

    @Test
    void testFindAllSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = generateUserProfile();

        UserProfileRecord savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOne();

        assertThat(savedUserProfileRecord).isNotNull();

        List<UserProfileRecord> savedProfileRecord = dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq("hello-id"))
            .fetchInto(UserProfileRecord.class);

        assertThat(savedProfileRecord).isNotNull().hasSize(0);
    }

    @Test
    void testFindSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = generateUserProfile();

        UserProfileRecord savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOne();

        assertThat(savedUserProfileRecord).isNotNull();

        UserProfileRecord savedProfileRecord = dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq("hello-id"))
            .fetchOneInto(UserProfileRecord.class);

        assertThat(savedProfileRecord).isNotNull().isEqualTo(userProfileRecord);
    }

    private UserProfileRecord generateUserProfile() {
        return new UserProfileRecord()
            .setFirstname("hello")
            .setId("hello-id")
            .setLastname("world")
            .setDisplayname("helloworld")
            .setCreated(Instant.now().toString())
            .setUpdated(Instant.now().toString())
            .setMiddlename("h.");
    }
}

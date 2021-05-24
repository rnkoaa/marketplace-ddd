package com.marketplace.entity.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.evenstore.jooq.Tables;
import com.marketplace.evenstore.jooq.tables.records.UserProfileRecord;
import java.time.Instant;
import java.util.List;
import org.jooq.DSLContext;
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
            .isEqualTo("ede8048e-2155-40d8-b9bf-49f96166d274");
    }

    @Test
    void testFindAllSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = generateUserProfile();

        saveUserProfileRecord(dslContext, userProfileRecord);

        List<UserProfileRecord> savedProfileRecord = dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq("ede8048e-2155-40d8-b9bf-49f96166d274"))
            .fetchInto(UserProfileRecord.class);

        assertThat(savedProfileRecord).isNotNull().hasSize(1);
    }

    @Test
    void testFindSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = generateUserProfile();

        saveUserProfileRecord(dslContext, userProfileRecord);

        UserProfileRecord savedProfileRecord = dslContext.select()
            .from(Tables.USER_PROFILE)
            .where(Tables.USER_PROFILE.ID.eq("ede8048e-2155-40d8-b9bf-49f96166d274"))
            .fetchOneInto(UserProfileRecord.class);

        assertThat(savedProfileRecord).isNotNull().isEqualTo(userProfileRecord);
    }

    public static UserProfileRecord generateUserProfile() {
        return new UserProfileRecord()
            .setFirstName("hello")
            .setId("ede8048e-2155-40d8-b9bf-49f96166d274")
            .setLastName("world")
            .setDisplayName("helloworld")
            .setCreated(Instant.now().toString())
            .setUpdated(Instant.now().toString())
            .setMiddleName("h.");
    }

    public static void saveUserProfileRecord(DSLContext dslContext, UserProfileRecord userProfileRecord) {

        UserProfileRecord savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOne();

        assertThat(savedUserProfileRecord).isNotNull();
    }
}

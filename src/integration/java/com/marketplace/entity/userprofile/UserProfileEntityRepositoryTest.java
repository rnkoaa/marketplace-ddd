package com.marketplace.entity.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.UserProfileRecord;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class UserProfileEntityRepositoryTest extends BaseRepositoryTest {

    @Test
    void testSaveUserProfileEntityRecord() {
        UserProfileRecord userProfileRecord = new UserProfileRecord();
        userProfileRecord.setFirstname("hello")
            .setId("hello-id")
            .setLastname("world")
            .setUserProfileId("uuid")
            .setDisplayname("helloworld")
            .setCreated(Instant.now().toString())
            .setUpdated(Instant.now().toString())
            .setMiddlename("h.");

        UserProfileRecord savedUserProfileRecord = dslContext.insertInto(Tables.USER_PROFILE)
            .set(userProfileRecord)
            .returning(Tables.USER_PROFILE.ID)
            .fetchOne();

        assertThat(savedUserProfileRecord).isNotNull();


        assertThat(savedUserProfileRecord.getId()).isNotNull()
        .isEqualTo("hello-id");

    }
}

package com.marketplace.entity.classifiedad;

import static com.marketplace.evenstore.jooq.Tables.CLASSIFIED_AD;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.ImmutablePriceDto;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepository;
import com.marketplace.domain.classifiedad.query.ImmutableClassifiedAdQueryEntity;
import com.marketplace.entity.userprofile.UserProfileEntityRepositoryTest;
import com.marketplace.evenstore.jooq.tables.records.ClassifiedAdRecord;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClassifiedAdQueryRepositoryTest extends BaseRepositoryTest {

    ClassifiedAdQueryRepository classifiedAdQueryRepository;

    @BeforeEach
    void setup() {
        classifiedAdQueryRepository = getApplicationContext().getClassifiedAdQueryRepository();
    }

    @Test
    void testClassifiedQueryEntityCanBeSaved() {
        var userProfileRecord = UserProfileEntityRepositoryTest.generateUserProfile();
        UserProfileEntityRepositoryTest.saveUserProfileRecord(dslContext, userProfileRecord);

        var queryEntity = createQueryEntity("278b1e1e-c411-4177-aee7-ef7f00759ecb", userProfileRecord.getId());
        Optional<ClassifiedAdQueryEntity> save = classifiedAdQueryRepository.save(queryEntity);
        assertThat(save).isPresent();
        ClassifiedAdQueryEntity classifiedAdQueryEntity = save.get();
        assertThat(classifiedAdQueryEntity.getId()).isEqualTo(queryEntity.getId());
    }

    @Test
    void testClassifiedQueryEntityCanBeUpdated() {
        var userProfileRecord = UserProfileEntityRepositoryTest.generateUserProfile();
        UserProfileEntityRepositoryTest.saveUserProfileRecord(dslContext, userProfileRecord);

        var queryEntity = createQueryEntity("278b1e1e-c411-4177-aee7-ef7f00759ecb", userProfileRecord.getId());
        Optional<ClassifiedAdQueryEntity> save = classifiedAdQueryRepository.save(queryEntity);
        assertThat(save).isPresent();
        ClassifiedAdQueryEntity classifiedAdQueryEntity = save.get();
        assertThat(classifiedAdQueryEntity.getId()).isEqualTo(queryEntity.getId());

        var existing = classifiedAdQueryRepository.findById(queryEntity.getId());
        assertThat(existing).isPresent();
        ClassifiedAdQueryEntity existingEntity = existing.get();
        assertThat(existingEntity.getTitle()).isPresent().isEqualTo(queryEntity.getTitle());

        var updatedEntity = ImmutableClassifiedAdQueryEntity
            .copyOf(existingEntity).withText("Updated, Price dropped");

        Optional<ClassifiedAdQueryEntity> updatedSave = classifiedAdQueryRepository.save(updatedEntity);
        assertThat(updatedSave).isPresent();

        var foundEntity = classifiedAdQueryRepository.findById(queryEntity.getId());
        assertThat(foundEntity).isPresent();
        ClassifiedAdQueryEntity updatedExisting = foundEntity.get();
        assertThat(updatedExisting.getTitle()).isPresent().isEqualTo(queryEntity.getTitle());
        assertThat(updatedExisting.getText()).isPresent().isEqualTo(updatedEntity.getText());
    }

    @Test
    void canBeSaved() {
        var userProfileRecord = UserProfileEntityRepositoryTest.generateUserProfile();

        UserProfileEntityRepositoryTest.saveUserProfileRecord(dslContext, userProfileRecord);

        var classifiedAdRecord = createClassifiedRecord("278b1e1e-c411-4177-aee7-ef7f00759ecb",
            "46f2f2a4-c49b-41ef-9138-fa9a64eeddbe", userProfileRecord.getId());

        Optional<ClassifiedAdRecord> saved = dslContext.insertInto(CLASSIFIED_AD)
            .set(classifiedAdRecord)
            .returning(CLASSIFIED_AD.ID)
            .fetchOptional();

        assertThat(saved).isPresent();
        ClassifiedAdRecord savedClassifiedAd = saved.get();
        assertThat(savedClassifiedAd.getId()).isEqualTo(classifiedAdRecord.getId());

        Optional<ClassifiedAdQueryEntity> findById = classifiedAdQueryRepository.findById(
            UUID.fromString(classifiedAdRecord.getId()));
        assertThat(findById).isPresent();

        ClassifiedAdQueryEntity classifiedAdQueryEntity = findById.get();

        assertThat(classifiedAdQueryEntity.getId())
            .isEqualTo(UUID.fromString(classifiedAdRecord.getId()));

        assertThat(classifiedAdQueryEntity.getApprover())
            .isPresent()
            .get()
            .isEqualTo(UUID.fromString(classifiedAdRecord.getApprover()));

        assertThat(classifiedAdQueryEntity.getOwner())
            .isEqualTo(UUID.fromString(classifiedAdRecord.getOwner()));
    }

    @Test
    void testClassifiedAdCanBeFoundByOwnerId() {
        var userProfileRecord = UserProfileEntityRepositoryTest.generateUserProfile();

        UserProfileEntityRepositoryTest.saveUserProfileRecord(dslContext, userProfileRecord);

        var classifiedAdRecord = createClassifiedRecord("278b1e1e-c411-4177-aee7-ef7f00759ecb",
            "46f2f2a4-c49b-41ef-9138-fa9a64eeddbe", userProfileRecord.getId());

        Optional<ClassifiedAdRecord> saved = dslContext.insertInto(CLASSIFIED_AD)
            .set(classifiedAdRecord)
            .returning(CLASSIFIED_AD.ID)
            .fetchOptional();

        assertThat(saved).isPresent();
        ClassifiedAdRecord savedClassifiedAd = saved.get();
        assertThat(savedClassifiedAd.getId()).isEqualTo(classifiedAdRecord.getId());

        List<ClassifiedAdQueryEntity> allByOwners = classifiedAdQueryRepository.findByOwner(
            UUID.fromString(userProfileRecord.getId()));
        assertThat(allByOwners).isNotNull().hasSize(1);

        ClassifiedAdQueryEntity classifiedAdQueryEntity = allByOwners.get(0);

        assertThat(classifiedAdQueryEntity.getId())
            .isEqualTo(UUID.fromString(classifiedAdRecord.getId()));

        assertThat(classifiedAdQueryEntity.getApprover())
            .isPresent()
            .get()
            .isEqualTo(UUID.fromString(classifiedAdRecord.getApprover()));

        assertThat(classifiedAdQueryEntity.getOwner())
            .isEqualTo(UUID.fromString(classifiedAdRecord.getOwner()));
    }

    public ClassifiedAdQueryEntity createQueryEntity(String id, String owner) {
        return ImmutableClassifiedAdQueryEntity.builder()
            .id(UUID.fromString(id))
            .owner(UUID.fromString(owner))
            .title("new ad for iphone")
            .text("iphone in mint condition")
            .price(ImmutablePriceDto.builder()
                .currencyCode("USD")
                .amount(new BigDecimal("1199.00"))
                .build())
            .state(ClassifiedAdState.PENDING_REVIEW)

            .build();
    }

    public ClassifiedAdRecord createClassifiedRecord(String id, String approver, String owner) {
        return new ClassifiedAdRecord()
            .setId(id)
            .setApprover(approver)
            .setOwner(owner)
            .setText("classified ad text")
            .setTitle("classified ad title")
            .setPrice("20.03")
            .setStatus("approved")
            .setCreated("created date")
            .setCreated(Instant.now().toString())
            .setUpdated(Instant.now().toString());
    }
}

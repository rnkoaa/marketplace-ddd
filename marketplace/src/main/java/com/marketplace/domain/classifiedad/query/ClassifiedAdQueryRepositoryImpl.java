package com.marketplace.domain.classifiedad.query;

import static com.marketplace.evenstore.jooq.Tables.CLASSIFIED_AD;

import com.marketplace.evenstore.jooq.tables.records.ClassifiedAdRecord;
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
public class ClassifiedAdQueryRepositoryImpl implements ClassifiedAdQueryRepository {

    private final DSLContext dslContext;

    @Inject
    public ClassifiedAdQueryRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Optional<ClassifiedAdQueryEntity> findById(UUID id) {
        return fetchRecord(id)
            .map(ClassifiedAdQueryEntityMapper::convert);
    }

    @Override
    public List<ClassifiedAdQueryEntity> findAll() {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.select()
            .from(CLASSIFIED_AD)
            .fetchInto(ClassifiedAdRecord.class);

        return convertRecords(classifiedAdRecords);
    }

    @Override
    public Optional<ClassifiedAdQueryEntity> save(ClassifiedAdQueryEntity entity) {
        var classifiedAdExists = fetchRecord(entity.getId());

        ClassifiedAdRecord classifiedAdRecord = ClassifiedAdQueryEntityMapper.convert(entity);
        classifiedAdRecord.setUpdated(Instant.now().toString());
        return classifiedAdExists
            .map(existingAd -> updateClassifiedAd(classifiedAdRecord, existingAd))
            .or(() -> insertClassifiedAd(classifiedAdRecord))
            .filter(it -> it.getId() != null)
            .map(ClassifiedAdQueryEntityMapper::convert);
    }


    @Override
    public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.select()
            .from(CLASSIFIED_AD)
            .where(CLASSIFIED_AD.OWNER.eq(ownerId.toString()))
            .fetchInto(ClassifiedAdRecord.class);

        return convertRecords(classifiedAdRecords);
    }

    private List<ClassifiedAdQueryEntity> convertRecords(List<ClassifiedAdRecord> classifiedAdRecords) {
        return classifiedAdRecords.stream()
            .map(ClassifiedAdQueryEntityMapper::convert)
            .toList();
    }

    @Override
    public List<ClassifiedAdQueryEntity> find() {
        return findAll();
    }

    private Optional<ClassifiedAdRecord> insertClassifiedAd(ClassifiedAdRecord classifiedAdRecord) {
        classifiedAdRecord.setCreated(Instant.now().toString());
        return dslContext.insertInto(CLASSIFIED_AD)
            .set(classifiedAdRecord)
            .returning(CLASSIFIED_AD.ID, CLASSIFIED_AD.OWNER, CLASSIFIED_AD.STATUS)
            .fetchOptional();
    }

    private ClassifiedAdRecord updateClassifiedAd(ClassifiedAdRecord classifiedAdRecord,
        ClassifiedAdRecord existingAd) {
        classifiedAdRecord.setCreated(existingAd.getCreated());
        return dslContext.update(CLASSIFIED_AD)
            .set(classifiedAdRecord)
            .returning(CLASSIFIED_AD.ID, CLASSIFIED_AD.OWNER, CLASSIFIED_AD.STATUS)
            .fetchOne();
    }

    private Optional<ClassifiedAdRecord> fetchRecord(UUID id) {
        return dslContext.select()
            .from(CLASSIFIED_AD)
            .where(CLASSIFIED_AD.ID.eq(id.toString()))
            .fetchOptionalInto(ClassifiedAdRecord.class);
    }
}

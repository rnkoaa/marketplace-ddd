package com.marketplace.domain.classifiedad.query;

import static com.marketplace.eventstore.jdbc.Tables.CLASSIFIED_AD;

import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;

import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.ClassifiedAdRecord;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;
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
        ClassifiedAdRecord classifiedAdRecord = dslContext.selectOne().from(CLASSIFIED_AD)
            .where(CLASSIFIED_AD.CLASSIFIED_AD_ID.eq(id.toString()))
            .fetchOneInto(ClassifiedAdRecord.class);

        ClassifiedAdQueryEntity entity = convertRecord(classifiedAdRecord);
        return Optional.ofNullable(entity);
    }

    private ClassifiedAdQueryEntity convertRecord(ClassifiedAdRecord classifiedAdRecord) {
        if (classifiedAdRecord == null || classifiedAdRecord.getId() == null || classifiedAdRecord.getId() == 0) {
            return null;
        }
        return null;
    }

    @Override
    public List<ClassifiedAdQueryEntity> findAll() {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.selectOne().from(CLASSIFIED_AD)
            .fetchInto(ClassifiedAdRecord.class);

        return classifiedAdRecords.stream()
            .filter(Objects::nonNull)
            .map(this::convertRecord)
            .toList();
    }

    @Override
    public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.selectOne().from(CLASSIFIED_AD)
            .where(CLASSIFIED_AD.OWNER.eq(ownerId.toString()))
            .fetchInto(ClassifiedAdRecord.class);

        return classifiedAdRecords.stream()
            .filter(Objects::nonNull)
            .map(this::convertRecord)
            .toList();
    }

    @Override
    public List<ClassifiedAdQueryEntity> findByApprover(UUID approverId) {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.selectOne().from(CLASSIFIED_AD)
            .where(CLASSIFIED_AD.APPROVER.eq(approverId.toString()))
            .fetchInto(ClassifiedAdRecord.class);

        return classifiedAdRecords.stream()
            .filter(Objects::nonNull)
            .map(this::convertRecord)
            .toList();
    }

    @Override
    public List<ClassifiedAdQueryEntity> find() {
        return findAll();
    }

    List<ClassifiedAdQueryEntity> convert(List<ClassifiedAdEntity> entities) {
        return entities.stream()
            .map(ClassifiedAdEntity::toClassifiedAdReadEntity)
            .collect(Collectors.toList());
    }
}

package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.entity.ClassifiedAdEntity;

import com.marketplace.eventstore.jdbc.Tables;
import com.marketplace.eventstore.jdbc.tables.records.ClassifiedAdRecord;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
        Optional<ClassifiedAdRecord> maybeClassifiedAdRecord = dslContext.selectOne()
            .from(Tables.CLASSIFIED_AD)
            .where(Tables.CLASSIFIED_AD.ID.eq(id.toString()))
            .fetchOptionalInto(ClassifiedAdRecord.class);

        return maybeClassifiedAdRecord.map(ClassifiedAdQueryEntityMapper::convert);
    }

    @Override
    public List<ClassifiedAdQueryEntity> findAll() {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.select()
            .from(Tables.CLASSIFIED_AD)
            .fetchInto(ClassifiedAdRecord.class);

        return convertRecords(classifiedAdRecords);
    }

    @Override
    public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
        List<ClassifiedAdRecord> classifiedAdRecords = dslContext.select()
            .from(Tables.CLASSIFIED_AD)
            .where(Tables.CLASSIFIED_AD.OWNER.eq(ownerId.toString()))
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

    List<ClassifiedAdQueryEntity> convert(List<ClassifiedAdEntity> entities) {
        return entities.stream()
            .map(ClassifiedAdEntity::toClassifiedAdReadEntity)
            .collect(Collectors.toList());
    }
}

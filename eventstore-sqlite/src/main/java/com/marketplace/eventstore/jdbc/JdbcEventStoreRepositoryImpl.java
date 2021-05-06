package com.marketplace.eventstore.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.jdbc.tables.records.EventRecordRecord;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class JdbcEventStoreRepositoryImpl implements JdbcEventStoreRepository {

    Map<String, Class<Event>> eventClassCache = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapperBuilder().build();
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEventStoreRepositoryImpl.class);
    private final DSLContext dslContext;

    public JdbcEventStoreRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Mono<List<Event>> load(UUID aggregateId, int fromVersion) {
        Result<EventRecordRecord> fetch = dslContext.selectFrom(Tables.EVENT_RECORD)
            .where(Tables.EVENT_RECORD.AGGREGATE_ID.eq(aggregateId.toString())
                .and(Tables.EVENT_RECORD.VERSION.ge(fromVersion)))
            .fetch();
        List<Event> events = fetch.stream()
            .map(eventRecordRecord -> {
                String data = eventRecordRecord.getData();
                String eventType = eventRecordRecord.getEventType();
                Class<Event> aClass = eventClassCache.get(eventType);
                if (aClass == null) {
                    return null;
                }
                try {
                    return objectMapper.readValue(data, aClass);
                } catch (JsonProcessingException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return Mono.justOrEmpty(events);
    }

    @Override
    public Mono<List<Event>> load(UUID aggregateId) {
        Result<EventRecordRecord> fetch = dslContext.selectFrom(Tables.EVENT_RECORD)
            .where(Tables.EVENT_RECORD.AGGREGATE_ID.eq(aggregateId.toString()))
            .fetch();
        List<Event> events = fetch.stream()
            .map(eventRecordRecord -> {
                String data = eventRecordRecord.getData();
                String eventType = eventRecordRecord.getEventType();
                Class<Event> aClass = eventClassCache.get(eventType);
                if (aClass == null) {
                    return null;
                }
                try {
                    return objectMapper.readValue(data, aClass);
                } catch (JsonProcessingException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return Mono.justOrEmpty(events);
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, Event event) {
        String eventData;
        try {
            eventData = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.error(e);
        }

        EventRecordRecord eventRecordRecord = new EventRecordRecord()
            .setEventId(event.getId().toString())
            .setAggregateId(event.getAggregateId().toString())
            .setVersion((int) event.getVersion())
            .setData(eventData)
            .setCreated(event.getCreatedAt().toString());

        EventRecordRecord saved = dslContext.insertInto(Tables.EVENT_RECORD)
            .set(eventRecordRecord)
            .returning()
            .fetchOne();
        if (saved != null && saved.getId() > 0) {
            return Mono.just(Optional.of(true));
        }
        return Mono.empty();
    }

    @Override
    public Mono<Optional<Boolean>> save(Event event) {
        return save(event.getAggregateId(), event);
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, List<Event> events, int version) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, Event event, int version) {
        Mono<Integer> versionMono = getVersion(aggregateId);
        Integer v = versionMono.block();
        if (v != null && v <= version) {
            return Mono.empty();
        }
        String eventData;
        try {
            eventData = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.error(e);
        }

        EventRecordRecord eventRecordRecord = new EventRecordRecord()
            .setEventId(event.getId().toString())
            .setAggregateId(event.getAggregateId().toString())
            .setVersion(version)
            .setData(eventData)
            .setCreated(event.getCreatedAt().toString());

        EventRecordRecord saved = dslContext.insertInto(Tables.EVENT_RECORD)
            .set(eventRecordRecord)
            .returning()
            .fetchOne();
        if (saved != null && saved.getId() > 0) {
            return Mono.just(Optional.of(true));
        }
        return Mono.empty();
    }

    @Override
    public Mono<Optional<Boolean>> save(Event event, int version) {
        return save(event.getAggregateId(), event, version);
    }

    @Override
    public Mono<Integer> getVersion(UUID aggregateId) {
        Record1<Integer> integerRecord = dslContext.select(DSL.max(Tables.EVENT_RECORD.VERSION))
            .where(Tables.EVENT_RECORD.AGGREGATE_ID.eq(aggregateId.toString()))
            .fetchOne();

        if (integerRecord != null) {
            return Mono.just(integerRecord.get(Tables.EVENT_RECORD.VERSION));
        }
        return Mono.empty();
    }

    @Override
    public Mono<Long> countEvents(UUID aggregateId) {
        return Mono.just(
            dslContext.fetchCount(
                dslContext.selectFrom(Tables.EVENT_RECORD)
                    .where(Tables.EVENT_RECORD.AGGREGATE_ID.eq(aggregateId.toString()))
            )
        ).map(Long::valueOf);
    }

    //    @Override
    //    public List<ControlAudit<Auditable>> findAll() {
    //        Result<ControlAuditRecord> results = dslContext.selectFrom(CONTROL_AUDIT)
    //            .fetch();
    //
    //        return results.stream()
    //            .map(controlAuditFromRecordMapping)
    //            .collect(Collectors.toList());
    //    }
    //
    //    long getNextId() {
    //        Field<Long> nextval = CONTROL_AUDIT_ID_SEQ.nextval();
    //        return dslContext.select(nextval).fetchOne(nextval);
    //    }
}

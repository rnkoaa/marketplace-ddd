package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.max;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.InvalidVersionException;
import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.util.List;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcEventStoreRepositoryImpl implements JdbcEventStoreRepository {

    EventClassCache eventClassCache = EventClassCache.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapperBuilder().build();
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEventStoreRepositoryImpl.class);
    private final DSLContext dslContext;

    public JdbcEventStoreRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Event> load(UUID aggregateId, int fromVersion) {
        org.jooq.Result<EventDataRecord> fetch = dslContext.selectFrom(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId.toString())
                .and(EVENT_DATA.EVENT_VERSION.ge(fromVersion)))
            .fetch();

        return List.of();
//        return fetch.stream()
//            .map(eventRecordRecord -> {
//                String data = eventRecordRecord.getData();
//                String eventType = eventRecordRecord.getEventType();
//                com.marketplace.eventstore.framework.Result<Class<?>> classResult = eventClassCache.get(eventType);
////                Class<?> aClass = (Event)eventClassCache.get(eventType);
//                if (aClass == null) {
//                    return null;
//                }
//                try {
//                    return objectMapper.readValue(data, aClass);
//                } catch (JsonProcessingException e) {
//                    return null;
//                }
//            })
//            .filter(Objects::nonNull)
//            .collect(Collectors.toList());
    }

    @Override
    public List<Event> load(UUID aggregateId) {
        org.jooq.Result<EventDataRecord> fetch = dslContext.selectFrom(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId.toString()))
            .fetch();

        return List.of();
//        return fetch.stream()
//            .map(eventRecordRecord -> {
//                String data = eventRecordRecord.getData();
//                String eventType = eventRecordRecord.getEventType();
//                Class<Event> aClass = eventClassCache.get(eventType);
//                if (aClass == null) {
//                    return null;
//                }
//                try {
//                    return objectMapper.readValue(data, aClass);
//                } catch (JsonProcessingException e) {
//                    return null;
//                }
//            })
//            .filter(Objects::nonNull)
//            .collect(Collectors.toList());
    }

    @Override
    public Result<Integer> save(UUID aggregateId, Event event) {
        long expectedVersion = event.getVersion();
        Integer latestVersion = getVersion(aggregateId);
        if (expectedVersion != 0 && expectedVersion <= latestVersion) {
            return Result.error(new InvalidVersionException("invalid exception"));
        }
        eventClassCache.put(event.getClass());

        Result<String> result = serializeJson(objectMapper, event);
        Result<EventDataRecord> savedResult = result.map(eventData -> new EventDataRecord()
            .setEventId(event.getId().toString())
            .setAggregateId(event.getAggregateId().toString())
            .setEventVersion((int) expectedVersion)
            .setEventType(event.getClass().getSimpleName())
            .setData(eventData)
            .setCreated(event.getCreatedAt().toString())
        ).map(eventDataRecord -> dslContext.insertInto(EVENT_DATA)
            .set(eventDataRecord)
            .returning()
            .fetchOne()
        );

        if (!savedResult.isPresent()) {
            return Result.error("error while saving event");
        }
        EventDataRecord eventDataRecord = savedResult.get();
        Integer id = eventDataRecord.getId();
        if (id == null) {
            return Result.error("id for saved event not found");
        }
        return Result.of(id);
    }

    @Override
    public Result<Integer> save(Event event) {
        return save(event.getAggregateId(), event);
    }

    @Override
    public Result<Integer> save(UUID aggregateId, List<Event> events, int version) {
        return null;
    }

    @Override
    public Result<Integer> save(UUID aggregateId, Event event, int expectedVersion) {
        Integer latestVersion = getVersion(aggregateId);
        if (expectedVersion != 0 && expectedVersion <= latestVersion) {
            return Result.error(new InvalidVersionException("invalid exception"));
        }
        eventClassCache.put(event.getClass());
        Result<String> result = serializeJson(objectMapper, event);
        Result<EventDataRecord> savedResult = result.map(eventData -> new EventDataRecord()
            .setEventId(event.getId().toString())
            .setAggregateId(event.getAggregateId().toString())
            .setEventVersion(expectedVersion)
            .setData(eventData)
            .setCreated(event.getCreatedAt().toString())
        ).map(eventDataRecord -> dslContext.insertInto(EVENT_DATA)
            .set(eventDataRecord)
            .returning()
            .fetchOne()
        );

        if (!savedResult.isPresent()) {
            return Result.error("error while saving event");
        }
        EventDataRecord eventDataRecord = savedResult.get();
        Integer id = eventDataRecord.getId();
        if (id == null) {
            return Result.error("id for saved event not found");
        }

        return Result.of(id);
    }

    @Override
    public Result<Integer> save(Event event, int version) {
        return save(event.getAggregateId(), event, version);
    }

    @Override
    public Integer getVersion(UUID aggregateId) {
        Record1<Integer> integerRecord1 = dslContext.select(
            max(EVENT_DATA.EVENT_VERSION).as("event_version")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId.toString()))
            .fetchOne();
        if (integerRecord1 == null) {
            return -1;
        }

        Integer version = integerRecord1.get("event_version", Integer.class);
        return (version != null) ? version : -1;
    }

    @Override
    public Long countEvents(UUID aggregateId) {
        Record1<Integer> countRecord = dslContext.select(
            countDistinct(EVENT_DATA.EVENT_ID).as("event_count")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId.toString()))
            .fetchOne();

        if (countRecord == null) {
            return 0L;
        }

        return countRecord.get("event_count", Long.class);
    }

    private static Result<String> serializeJson(ObjectMapper objectMapper, Object object) {
        try {
            return Result.of(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
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

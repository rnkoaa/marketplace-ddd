package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.max;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cqrs.event.Event;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.InvalidVersionException;
import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.util.List;
import java.util.stream.IntStream;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcEventStoreRepositoryImpl implements JdbcEventStoreRepository {

    record EventDataVersion(int version, VersionedEvent event) {

    }

    private static final EventClassCache eventClassCache = EventClassCache.getInstance();
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEventStoreRepositoryImpl.class);
    private final DSLContext dslContext;

    public JdbcEventStoreRepositoryImpl(ObjectMapper objectMapper, DSLContext dslContext) {
        this.objectMapper = objectMapper;
        this.dslContext = dslContext;
    }

    @Override
    public List<VersionedEvent> load(String streamId, int fromVersion) {
        org.jooq.Result<EventDataRecord> fetch = dslContext.selectFrom(EVENT_DATA)
            .where(EVENT_DATA.STREAM_ID.eq(streamId)
                .and(EVENT_DATA.EVENT_VERSION.ge(fromVersion)))
            .orderBy(EVENT_DATA.EVENT_VERSION.asc())
            .fetch();

        return fetch.stream()
            .map(eventDataRecord -> convertFromEventDataRecord(objectMapper, eventDataRecord))
            .filter(Result::isPresent)
            .map(Result::get)
            .toList();
    }

    @Override
    public List<VersionedEvent> load(String streamId) {
        org.jooq.Result<EventDataRecord> fetch = dslContext.selectFrom(EVENT_DATA)
            .where(EVENT_DATA.STREAM_ID.eq(streamId))
            .orderBy(EVENT_DATA.EVENT_VERSION.asc())
            .fetch();

        return fetch.stream()
            .map(eventDataRecord -> convertFromEventDataRecord(objectMapper, eventDataRecord))
            .filter(Result::isPresent)
            .map(Result::get)
            .toList();
    }

    @Override
    public Result<Boolean> save(String streamId, VersionedEvent event) {
        long expectedVersion = event.getVersion();
        int latestVersion = getVersion(streamId);
        int nextVersion = latestVersion + 1;

        if ((expectedVersion == 0) || (nextVersion != expectedVersion)) {
            String errMessage = "invalid expected version, latest version is %d, expected version is %d";
            return Result.error(new InvalidVersionException(String.format(errMessage, latestVersion, expectedVersion)));
        }

        eventClassCache.put(event.getClass());

        Result<String> result = serializeJson(objectMapper, event);
        Result<EventDataRecord> savedResult = createFromEvent(event, (int) expectedVersion, result)
            .map(eventDataRecord -> dslContext.insertInto(EVENT_DATA)
                .set(eventDataRecord)
                .returning(EVENT_DATA.ID)
                .fetchOne()
            );

        if (savedResult.isError()) {
            return Result.error(savedResult.getError());
        }

        EventDataRecord eventDataRecord = savedResult.get();
        String id = eventDataRecord.getId();
        if (id == null) {
            return Result.error("id for saved event not found");
        }
        return Result.of(true);
    }

    @Override
    public Result<Boolean> save(VersionedEvent event) {
        if(event.getStreamId() == null || event.getStreamId().isEmpty()) {
            return Result.error("required stream id missing");
        }
        return save(event.getStreamId(), event);
    }

    @Override
    public Result<Integer> save(String streamId, List<VersionedEvent> events, int expectedVersion) {
        int latestVersion = getVersion(streamId);
        int nextVersion = latestVersion + 1;
        if ((expectedVersion == 0) || (nextVersion != expectedVersion)) {
            String errMessage = "invalid expected version, latest version is %d, expected version is %d";
            return Result.error(new InvalidVersionException(String.format(errMessage, latestVersion, expectedVersion)));
        }

        List<EventDataRecord> eventDataRecords = IntStream.range(0, events.size())
            .mapToObj(index -> new EventDataVersion(nextVersion + index, events.get(index)))
            .peek(eventVersion -> eventClassCache.put(eventVersion.event.getClass()))
            .map(eventVersion -> {
                Result<String> result = serializeJson(objectMapper, eventVersion.event);
                return createFromEvent(eventVersion.event, eventVersion.version, result);
            })
            .filter(Result::isPresent)
            .map(Result::get)
//            .map(eventDataRecord -> eventDataRecord.setAggregateName(streamId))
            .toList();

        int[] execute = dslContext.batchStore(eventDataRecords).execute();
        return Result.of(execute.length);
    }

    @Override
    public Result<Boolean> save(String streamId, VersionedEvent event, int expectedVersion) {
        int latestVersion = getVersion(streamId);
        int nextVersion = latestVersion + 1;
        if ((expectedVersion == 0) || (nextVersion != expectedVersion)) {
            String errMessage = "invalid expected version, latest version is %d, expected version is %d";
            return Result.error(new InvalidVersionException(String.format(errMessage, latestVersion, expectedVersion)));
        }

        eventClassCache.put(event.getClass());
        Result<String> result = serializeJson(objectMapper, event);
        Result<EventDataRecord> savedResult = createFromEvent(event, expectedVersion, result)
            .map(eventDataRecord ->
                dslContext.insertInto(EVENT_DATA)
                    .set(eventDataRecord)
                    .returning()
                    .fetchOne()
            );

        if (!savedResult.isPresent()) {
            return Result.error("error while saving event");
        }
        EventDataRecord eventDataRecord = savedResult.get();
        String id = eventDataRecord.getId();
        if (id == null) {
            return Result.error("id for saved event not found");
        }

        return Result.of(true);
    }

    @Override
    public Result<Boolean> save(VersionedEvent event, int expectedVersion) {
        return save(event.getStreamId(), event, expectedVersion);
    }

    public int getVersion(String streamId) {
        Record1<Integer> integerRecord1 = dslContext.select(
            max(EVENT_DATA.EVENT_VERSION).as("event_version")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.STREAM_ID.eq(streamId))
            .fetchOne();
        if (integerRecord1 == null) {
            return 0;
        }

        Integer version = integerRecord1.get("event_version", Integer.class);
        return (version != null) ? version : 0;
    }

    @Override
    public long countEvents(String streamId) {
        Record1<Integer> countRecord = dslContext.select(
            countDistinct(EVENT_DATA.ID).as("event_count")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.STREAM_ID.eq(streamId))
            .fetchOne();

        if (countRecord == null) {
            return 0L;
        }

        return countRecord.get("event_count", Long.class);
    }

    @Override
    public int nextVersion(String streamId) {
        return 0;
    }

    private static Result<VersionedEvent> convertFromEventDataRecord(ObjectMapper objectMapper,
        EventDataRecord eventDataRecord) {
        String data = eventDataRecord.getData();
        String eventType = eventDataRecord.getEventType();

        if (eventType == null || eventType.isEmpty()) {
            return Result.error("unable to determine event type");
        }

        Result<Class<?>> classResult = eventClassCache.get(eventType);
        return classResult.flatmap(clzz -> deserializeJSON(objectMapper, data, clzz))
            .map(e -> (VersionedEvent) e);
    }

    private static Result<String> serializeJson(ObjectMapper objectMapper, Object object) {
        try {
            return Result.of(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
    }

    public static <T> Result<T> deserializeJSON(ObjectMapper objectMapper, String json, Class<T> clzz) {
        try {
            return Result.of(objectMapper.readValue(json, clzz));
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
    }

    private Result<EventDataRecord> createFromEvent(Event event, int expectedVersion, Result<String> eventDataResult) {
        Result<EventDataRecord> result = eventDataResult
            .map(eventData -> new EventDataRecord()
                .setId(event.getId().toString())
                .setStreamId(event.getStreamId())
                .setAggregateName(event.getAggregateName())
                .setAggregateId(event.getAggregateId().toString())
                .setEventVersion(expectedVersion)
                .setEventType(event.getClass().getSimpleName())
                .setData(eventData)
                .setCreated(event.getCreatedAt().toString()));

        if (result.isError()) {
            System.out.println(result.getError().getMessage());
        }

        return result;
    }

}

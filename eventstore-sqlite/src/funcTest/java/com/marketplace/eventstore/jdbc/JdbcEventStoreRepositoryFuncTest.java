package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.jooq.impl.DSL.max;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import com.marketplace.eventstore.test.data.TestEvents;
import com.marketplace.eventstore.test.events.ImmutableTestCreatedEvent;
import com.marketplace.eventstore.test.events.TestCreatedEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.jooq.Record1;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;

class JdbcEventStoreRepositoryFuncTest extends AbstractJdbcFuncTest {

    EventClassCache eventClassCache = EventClassCache.getInstance();
    private final JdbcEventStoreRepository jdbcEventStoreRepository;

    final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

    public JdbcEventStoreRepositoryFuncTest() throws SQLException {
        super();
        jdbcEventStoreRepository = new JdbcEventStoreRepositoryImpl(objectMapper, dslContext);
    }

    @Test
    void saveNewEventWithExpectedVersion() {
        Result<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent, 1);

        assertThat(save.isPresent()).isTrue();

        assertThat(save.get()).isTrue();
    }

    @Test
    void testSaveMultipleEventsAtExpectedVersion() {
        List<VersionedEvent> aggregateEvents = TestEvents.aggregateEvents;

        Result<Integer> save = jdbcEventStoreRepository.save(TestEvents.streamId, aggregateEvents, 1);

        assertThat(save.isPresent()).isTrue();

        assertThat(save.get()).isGreaterThan(0);

        Record1<Integer> integerRecord1 = dslContext.select(
            max(EVENT_DATA.EVENT_VERSION).as("event_version")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_NAME.eq(aggregateEvents.get(0).getAggregateName()))
            .fetchOne();

        assertThat(integerRecord1).isNotNull();
        int maxValue = integerRecord1.get("event_version", Integer.class);
        assertThat(maxValue).isEqualByComparingTo(3);
    }

    @Test
    void testSaveNewEvent() {

        Result<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save.isPresent()).isTrue();

        List<EventDataRecord> eventDataRecords = dslContext
            .select().from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID
                .eq(TestEvents.testCreatedEvent.getAggregateId().toString()))
            .fetchInto(EventDataRecord.class);

        assertThat(eventDataRecords).isNotNull().hasSize(1);

        EventDataRecord eventDataRecord = eventDataRecords.get(0);

        String eventData = eventDataRecord.get(EVENT_DATA.DATA, String.class);
        String eventType = eventDataRecord.get(EVENT_DATA.EVENT_TYPE, String.class);

        Result<Class<?>> classResult = eventClassCache.get(eventType);
        assertThat(classResult.isPresent()).isTrue();

        Result<Event> eventResult = classResult
            .flatmap(clzz -> deserializeJSON(objectMapper, eventData, clzz)
                .map(res -> (Event) res));

        assertThat(eventResult.isPresent()).isTrue();

        Event event = eventResult.get();
        assertThat(event.getAggregateId()).isEqualByComparingTo(TestEvents.testCreatedEvent.getAggregateId());
    }

    @Test
    void testCannotSaveTheSameEventTwice() {

        Result<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save.isPresent()).isTrue();

        // Duplicate key since the event id is the same
//        DataAccessException
        TestCreatedEvent testCreatedEventCopy = ImmutableTestCreatedEvent.copyOf(TestEvents.testCreatedEvent)
            .withVersion(2L);
        Result<Boolean> second = jdbcEventStoreRepository.save(testCreatedEventCopy);

        assertThat(second.isPresent()).isFalse();
        assertThat(second.isError()).isTrue();

        assertThat(second.getError()).isNotNull();
        assertThat(second.getError().getClass()).isEqualTo(DataAccessException.class);
    }

    @Test
    void testCannotSaveTwoEventsWithTheSameVersionForTheSameAggregate() {

        TestCreatedEvent testCreatedEventCopy = ImmutableTestCreatedEvent.copyOf(TestEvents.testCreatedEvent)
            .withId(UUID.fromString("c48e89e4-7219-44cf-9ae4-7df3d49fc9da"));

        assertThat(testCreatedEventCopy.getVersion()).isEqualByComparingTo(TestEvents.testCreatedEvent.getVersion());
        Result<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save.isPresent()).isTrue();
        assertThat(save.get()).isNotNull().isTrue();

        // Duplicate key since the event id is the same
//        DataAccessException
        Result<Boolean> save1 = jdbcEventStoreRepository.save(testCreatedEventCopy);
        assertThat(save1.isPresent()).isFalse();
    }

    @Test
    void testAllEventsForAggregateCanBeLoaded() {
        List<VersionedEvent> aggregateEvents = TestEvents.aggregateEvents;

        // when
        jdbcEventStoreRepository.save(TestEvents.streamId, aggregateEvents, 1);

        List<VersionedEvent> events = jdbcEventStoreRepository.load(TestEvents.streamId);

        // then
        assertThat(events).isNotNull().hasSize(3);
    }

    @Test
    void testLoadEventsUsingAggregateName() {
        List<VersionedEvent> aggregateEvents = TestEvents.aggregateEvents;

        // when
        jdbcEventStoreRepository.save(TestEvents.streamId, aggregateEvents, 1);

        List<VersionedEvent> events = jdbcEventStoreRepository.load(TestEvents.aggregateName);

        // then
        assertThat(events).isNotNull().hasSize(3);
    }

    @Test
    void testLoadMissingAggregates() {
        List<VersionedEvent> events = jdbcEventStoreRepository.load(TestEvents.streamId);

        // then
        assertThat(events).isNotNull().hasSize(0);

    }

    @Test
    void testLoadEventsFromVersion() {
        List<VersionedEvent> aggregateEvents = TestEvents.aggregateEvents;

        // when
        jdbcEventStoreRepository.save(TestEvents.streamId, aggregateEvents, 1);

        List<VersionedEvent> events = jdbcEventStoreRepository.load(TestEvents.streamId, 2);

        // then
        assertThat(events).isNotNull().hasSize(2);
    }

    @Test
    void testLoadEventsUsingAggregateNameFromVersion() {
        List<VersionedEvent> aggregateEvents = TestEvents.aggregateEvents;

        // when
        jdbcEventStoreRepository.save(TestEvents.streamId, aggregateEvents, 1);

        List<VersionedEvent> events = jdbcEventStoreRepository.load(TestEvents.aggregateName, 2);

        // then
        assertThat(events).isNotNull().hasSize(2);
    }

    public static <T> Result<T> deserializeJSON(ObjectMapper objectMapper, String json, Class<T> clzz) {
        try {
            return Result.of(objectMapper.readValue(json, clzz));
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
    }

}
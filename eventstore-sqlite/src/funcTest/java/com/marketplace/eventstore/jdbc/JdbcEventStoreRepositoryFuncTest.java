package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import com.marketplace.eventstore.test.data.TestEvents;
import com.marketplace.eventstore.test.events.ImmutableTestCreatedEvent;
import com.marketplace.eventstore.test.events.TestCreatedEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JdbcEventStoreRepositoryFuncTest extends AbstractJdbcFuncTest {

    EventClassCache eventClassCache = EventClassCache.getInstance();
    private JdbcEventStoreRepository jdbcEventStoreRepository;

    final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

    public JdbcEventStoreRepositoryFuncTest() throws SQLException {
        super();
        jdbcEventStoreRepository = new JdbcEventStoreRepositoryImpl(dslContext);
    }

    @Test
    void testSaveNewEvent() {

        Optional<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save).isPresent();

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

        Optional<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save).isPresent();

        // Duplicate key since the event id is the same
//        DataAccessException
//        Assertions.assertThrows(DataAccessException.class, () -> {
        Optional<Boolean> second = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(second).isNotPresent();
//        });
    }

    @Test
    void testCannotSaveTwoEventsWithTheSameVersionForTheSameAggregate() {

        TestCreatedEvent testCreatedEventCopy = ImmutableTestCreatedEvent.copyOf(TestEvents.testCreatedEvent)
            .withId(UUID.fromString("c48e89e4-7219-44cf-9ae4-7df3d49fc9da"));

        assertThat(testCreatedEventCopy.getVersion()).isEqualByComparingTo(TestEvents.testCreatedEvent.getVersion());
        Optional<Boolean> save = jdbcEventStoreRepository.save(TestEvents.testCreatedEvent);

        assertThat(save).isPresent();

        // Duplicate key since the event id is the same
//        DataAccessException
        Optional<Boolean> save1 = jdbcEventStoreRepository.save(testCreatedEventCopy);
        assertThat(save1).isNotPresent();
    }

    public static <T> Result<T> deserializeJSON(ObjectMapper objectMapper, String json, Class<T> clzz) {
        try {
            return Result.of(objectMapper.readValue(json, clzz));
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
    }

}
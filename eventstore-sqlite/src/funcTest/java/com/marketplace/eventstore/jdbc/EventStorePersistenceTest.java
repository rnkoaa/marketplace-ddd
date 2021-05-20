package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.max;

import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import org.jooq.InsertResultStep;
import org.jooq.Record1;
import org.junit.jupiter.api.Test;

public class EventStorePersistenceTest extends AbstractJdbcFuncTest {

    static String aggregateId = "f3864d92-9571-4212-8eb5-90b2f95c9739";
    static String aggregateType = "ClassifiedAd";

    static List<EventMetadata> eventMetadata = List.of(
        EventMetadata.of(aggregateType, "bf336e78-dc40-44f9-acec-57cf755987d9", aggregateId, "CreateClassifiedAd", 1),
        EventMetadata
            .of(aggregateType, "361b79f3-bfab-448e-8178-07ef679f34cd", aggregateId, "UpdateClassifiedAdTitle", 2),
        EventMetadata
            .of(aggregateType, "b975119e-e09e-42e7-9999-a443758f672e", aggregateId, "AddPictureToClassifiedAd", 3)
    );

    public EventStorePersistenceTest() throws SQLException {
        super();
    }

    static record EventMetadata(String aggregateType,
                                String eventId,
                                String aggregateId,
                                String eventType,
                                int version) {

        String aggregateName() {
            return String.format("%s:%s", aggregateType, aggregateId);
        }

        static EventMetadata of(String aggregateType, String eventId, String aggregateId, String eventType) {
            return new EventMetadata(aggregateType, eventId, aggregateId, eventType, 0);
        }

        static EventMetadata of(String aggregateType, String eventId, String aggregateId, String eventType,
            int version) {
            return new EventMetadata(aggregateType, eventId, aggregateId, eventType, version);
        }
    }

    @Test
    void testInsertRecord() {
        String eventId = "bf336e78-dc40-44f9-acec-57cf755987d9";

        var eventRecord = createForInsert(eventId, aggregateId);
        InsertResultStep<EventDataRecord> returning = dslContext.insertInto(EVENT_DATA)
            .set(eventRecord)
            .returning(EVENT_DATA.ID);

        EventDataRecord eventDataRecord = returning.fetchOne();
        assertThat(eventDataRecord).isNotNull();
        assertThat(eventDataRecord.getId()).isNotNull().isNotEmpty()
            .isEqualTo(eventId);
    }

    @Test
    void insertMultipleRecords() {
        var eventRecord = createMultipleEvents(eventMetadata, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);
    }

    @Test
    void testEventsQueryByAggregateId() {
        var eventRecord = createMultipleEvents(eventMetadata, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);

        List<EventDataRecord> eventDataRecords = dslContext.select().from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId))
            .fetchInto(EventDataRecord.class);

        assertThat(eventDataRecords).isNotEmpty().hasSize(3);
    }

    @Test
    void testEventsQueryByAggregateName() {
        var eventRecord = createMultipleEvents(eventMetadata, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);

        List<EventDataRecord> eventDataRecords = dslContext.select().from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_NAME.eq(eventMetadata.get(0).aggregateName()))
            .fetchInto(EventDataRecord.class);

        assertThat(eventDataRecords).isNotEmpty().hasSize(3);
    }

    @Test
    void testFindMaxVersionOfEventByAggregateName() {
        var eventRecord = createMultipleEvents(eventMetadata, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);

        Record1<Integer> integerRecord1 = dslContext.select(
            max(EVENT_DATA.EVENT_VERSION).as("event_version")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_NAME.eq(eventMetadata.get(0).aggregateName()))
            .fetchOne();

        assertThat(integerRecord1).isNotNull();
        int maxValue = integerRecord1.get("event_version", Integer.class);
        assertThat(maxValue).isEqualByComparingTo(3);
    }

    @Test
    void testCountEventsByAggregateName() {
        var eventRecord = createMultipleEvents(eventMetadata, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);

        Record1<Integer> integerRecord1 = dslContext.select(
            countDistinct(EVENT_DATA.ID).as("event_count")
        ).from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_NAME.eq(eventMetadata.get(0).aggregateName()))
            .fetchOne();

        assertThat(integerRecord1).isNotNull();
        int maxValue = integerRecord1.get("event_count", Integer.class);
        assertThat(maxValue).isEqualByComparingTo(3);
    }

    @Test
    void testInsertMultipleRecords() {
        String eventId = "bf336e78-dc40-44f9-acec-57cf755987d9";

        var eventRecord = createForInsert(eventId, aggregateId);
        InsertResultStep<EventDataRecord> returning = dslContext.insertInto(EVENT_DATA)
            .set(eventRecord)
            .returning(EVENT_DATA.ID);

        EventDataRecord eventDataRecord = returning.fetchOne();
        assertThat(eventDataRecord).isNotNull();

        List<EventDataRecord> eventDataRecords = dslContext.select().from(EVENT_DATA).fetchInto(EventDataRecord.class);
        assertThat(eventDataRecords).isNotNull().hasSize(1);
    }

    @Test
    void testQueryByAggregateId() {
        String eventId = "bf336e78-dc40-44f9-acec-57cf755987d9";

        var eventRecord = createForInsert(eventId, aggregateId);
        InsertResultStep<EventDataRecord> returning = dslContext.insertInto(EVENT_DATA)
            .set(eventRecord)
            .returning(EVENT_DATA.ID);

        EventDataRecord eventDataRecord = returning.fetchOne();
        assertThat(eventDataRecord).isNotNull();

        List<EventDataRecord> eventDataRecords = dslContext
            .select()
            .from(EVENT_DATA)
            .where(EVENT_DATA.AGGREGATE_ID.eq(aggregateId))
            .fetchInto(EventDataRecord.class);
        assertThat(eventDataRecords).isNotNull().hasSize(1);
    }

    List<EventDataRecord> createMultipleEvents(List<EventMetadata> events, String aggregateId) {
        String eventDataFormat = """
            {
            "event_type": "%s"
            }
            """;

        return events
            .stream()
            .map(entry -> {
                String eventData = String.format(eventDataFormat, entry.aggregateId());
                return new EventDataRecord(
                    entry.eventId(),
                    entry.aggregateName(),
                    entry.aggregateId,
                    entry.eventType,
                    entry.version,
                    eventData,
                    Instant.now().toString()
                );
            })
            .toList();
    }

    EventDataRecord createForInsert(String eventId, String aggregateId) {
        String eventData = """
            {
            "event_type": "ClassifiedAdCreated"
            }
            """;
        return new EventDataRecord(
            eventId,
            "ClassifiedAd:" + aggregateId,
            aggregateId,
            "ClassifiedAdCreated",
            1,
            eventData,
            Instant.now().toString()
        );
    }
}

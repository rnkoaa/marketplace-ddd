package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventStorePersistenceTest {

    private final static String CONNECTION_STRING = "jdbc:sqlite:src/funcTest/resources/db/eventstore.db";
    private DSLContext dslContext;
    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        connection = createConnection(CONNECTION_STRING);
        dslContext = createDslContext(connection);
    }

    @Test
    void testInsertRecord() {
        String aggregateId = "f3864d92-9571-4212-8eb5-90b2f95c9739";
        String eventId = "bf336e78-dc40-44f9-acec-57cf755987d9";

        var eventRecord = createForInsert(eventId, aggregateId);
        InsertResultStep<EventDataRecord> returning = dslContext.insertInto(EVENT_DATA)
            .set(eventRecord)
            .returning(EVENT_DATA.ID);

        EventDataRecord eventDataRecord = returning.fetchOne();
        assertThat(eventDataRecord).isNotNull();
        assertThat(eventDataRecord.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    void insertMultipleRecords() {
        String aggregateId = "f3864d92-9571-4212-8eb5-90b2f95c9739";
        Map<String, String> eventMap = Map.of(
            "bf336e78-dc40-44f9-acec-57cf755987d9", "CreateClassifiedAd",
            "361b79f3-bfab-448e-8178-07ef679f34cd", "UpdateClassifiedAdTitle",
            "b975119e-e09e-42e7-9999-a443758f672e", "AddPictureToClassifiedAd"
        );

        var eventRecord = createMultipleEvents(eventMap, aggregateId);
        int[] execute = dslContext.batchStore(eventRecord).execute();

        assertThat(execute).isNotEmpty().hasSize(3);
    }

    @Test
    void testInsertMultipleRecords() {
        String aggregateId = "f3864d92-9571-4212-8eb5-90b2f95c9739";
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

    @AfterEach
    void cleanup() throws SQLException {
        dslContext.delete(EVENT_DATA).execute();
        closeConnection(connection);
    }

    @BeforeAll
    static void setupAll() {
// use flyway to seed data and create tables
    }

    @AfterAll
    static void cleanupAll() {

    }

    private static Connection createConnection(final String connectionString) throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    private static DSLContext createDslContext(Connection connection) throws SQLException {
        return DSL.using(connection, SQLDialect.SQLITE);
    }

    private static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    List<EventDataRecord> createMultipleEvents(Map<String, String> events, String aggregateId) {
        String eventDataFormat = """
            {
            "event_type": "%s"
            }
            """;

        return events.entrySet()
            .stream()
            .map(entry -> {
                String eventData = String.format(eventDataFormat, entry.getValue());
                return new EventDataRecord(
                    null,
                    entry.getKey(),
                    "ClassifiedAd:" + aggregateId,
                    aggregateId,
                    "ClassifiedAdCreated",
                    1,
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
            null,
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

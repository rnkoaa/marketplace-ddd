package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
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
//        Event event =
        String aggregateId = "f3864d92-9571-4212-8eb5-90b2f95c9739";
        String eventId = "bf336e78-dc40-44f9-acec-57cf755987d9";
        String eventData = """
            {
            "event_type": "ClassifiedAdCreated"
            }
            """;
        var eventRecord = new EventDataRecord(
            null,
            eventId,
            "ClassifiedAd:" + aggregateId,
            aggregateId,
            "ClassifiedAdCreated",
            1,
            eventData,
            Instant.now().toString()
        );

        InsertResultStep<EventDataRecord> returning = dslContext.insertInto(EVENT_DATA)
            .set(eventRecord)
            .returning(EVENT_DATA.ID);

        EventDataRecord eventDataRecord = returning.fetchOne();
        assertThat(eventDataRecord).isNotNull();
        assertThat(eventDataRecord.getId()).isNotNull().isGreaterThan(0);
//        assertTrue(1, eventDataRecord.getId());
    }

    @AfterEach
    void cleanup() throws SQLException {
        closeConnection(connection);
    }

    @BeforeAll
    static void setupAll() {

    }

    @AfterAll
    static void cleanupAll() {

    }

    private static Connection createConnection(String connectionString) throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    private static DSLContext createDslContext(Connection connection) throws SQLException {
        return DSL.using(connection, SQLDialect.SQLITE);
    }

    private static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}

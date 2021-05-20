package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;

import com.marketplace.eventstore.jdbc.flyway.FlywayMigration;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJdbcFuncTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcFuncTest.class);

    private final static String CONNECTION_STRING = "jdbc:sqlite:src/funcTest/resources/db/eventstore.db";
    protected DSLContext dslContext;
    private final Connection connection;

    public AbstractJdbcFuncTest() throws SQLException {
        connection = createConnection(CONNECTION_STRING);
        dslContext = createDslContext(connection);
    }

    @BeforeEach
    void setup() throws SQLException {
        int count = dslContext.delete(EVENT_DATA).execute();
        LOGGER.info("cleaning event data {}", count);
    }

    @AfterEach
    void cleanup() throws SQLException {
        dslContext.delete(EVENT_DATA).execute();
        closeConnection(connection);
    }

    @BeforeAll
    static void setupAll() {
        FlywayMigration.migrate(CONNECTION_STRING);
    }

    @AfterAll
    static void cleanupAll() {
// delete db file
        File dbFile = new File("src/funcTest/resources/db/eventstore.db");
        if (!dbFile.delete()) {
            LOGGER.info("unable to delete db file");
        }
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
}

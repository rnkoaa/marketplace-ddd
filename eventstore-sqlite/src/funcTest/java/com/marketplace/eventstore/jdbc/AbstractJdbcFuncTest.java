package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;

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

public abstract class AbstractJdbcFuncTest {

    private final static String CONNECTION_STRING = "jdbc:sqlite:src/funcTest/resources/db/eventstore.db";
    protected DSLContext dslContext;
    private final Connection connection;

    public AbstractJdbcFuncTest() throws SQLException {
        connection = createConnection(CONNECTION_STRING);
        dslContext = createDslContext(connection);
    }

    @BeforeEach
    void setup() throws SQLException {
        dslContext.delete(EVENT_DATA).execute();
    }

    @AfterEach
    void cleanup() throws SQLException {
        dslContext.delete(EVENT_DATA).execute();
        closeConnection(connection);
    }

    @BeforeAll
    static void setupAll() {
        //  testCompile group: 'org.flywaydb', name:'flyway-core', version : '3.2.1'
        // resources/db/migration/V1__Initial_Setup.sql
//        create table event_data (
//            id INTEGER PRIMARY KEY,
//            event_id TEXT NOT NULL,
//            aggregate_name TEXT NULL,
//            aggregate_id TEXT NOT NULL,
//            event_type TEXT NOT NULL,
//            event_version integer,
//            data TEXT NOT NULL,
//            created TEXt NOT NULL
//        );

        // public static final String JDBC_URL = "jdbc:h2:mem:test_mem;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        //
        //    private static Server server;
        //    protected static DBI jdbi;
        //
        //    @BeforeClass
        //    public static void initDB() throws Exception {
        //        server = Server.createTcpServer();
        //
        //        initSchema();
        //        DataSource dataSource = JdbcConnectionPool.create(JDBC_URL, "sa", "");
        //        jdbi = new DBI(dataSource);
        //    }
        //
        //    private static void initSchema() {
        //        Flyway flyway = new Flyway();
        //        flyway.setDataSource(JDBC_URL, "sa", "");
        //        flyway.migrate();
        //    }
        //
        //    @AfterClass
        //    public static void closeDB() throws Exception {
        //        server.shutdown();
        //    }
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
}

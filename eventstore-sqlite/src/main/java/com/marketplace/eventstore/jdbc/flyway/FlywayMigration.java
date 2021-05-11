package com.marketplace.eventstore.jdbc.flyway;

import org.flywaydb.core.Flyway;
import org.sqlite.SQLiteDataSource;

public class FlywayMigration {

    public static void migrate(String connectionString) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(connectionString);
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }

}

package com.marketplace;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.AggregateStoreRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DaggerTest {

    ApplicationContext context;
    DSLContext dslContext;
    AggregateStoreRepository aggregateStoreRepository;

    //    @BeforeEach
    @Test
    void loadAppConfig() throws IOException {
        ApplicationConfig config =
            ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        Optional<String> dbPath = config.getDb().getDbPath();
        assertThat(dbPath).isPresent();
//        assertThat(d)
        String path = dbPath.get();
        assertThat(path).isEqualTo("~/marketplace");
        System.out.println(path);
    }

    @Test
    void loadDbUrl() throws IOException {
        ApplicationConfig config =
            ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        String dbPath = config.getDb().getDbUrl();
////        assertThat(dbPath).isPresent();
////        assertThat(d)
////        String path = dbPath.get();
        assertThat(dbPath).isNotEmpty(); //.isEqualTo("jdbc:sqlite:/tmp/marketplace/marketplace.db");
//        System.out.println(path);

        Optional<String> dbPath1 = config.getDb().getDbPath();
        assertThat(dbPath1).isPresent();

        String path = dbPath1.get();
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("failed to create directory");
            }
        }
    }

}

package com.marketplace.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.eventstore.jdbc.flyway.FlywayMigration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value.Immutable
@JsonSerialize(as = ImmutableDbConfig.class)
@JsonDeserialize(as = ImmutableDbConfig.class)
public abstract class DbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfig.class);

    @JsonProperty("url")
    public abstract Optional<String> getUrl();

    @JsonIgnore
    public String getDbUrl() {
        return getUrl()
            .orElseGet(() ->
                getDbPath()
                    .map(dbPath -> {
                        String dbName = getName().orElse("marketplace.db");
                        String absolutePath = normalizePath(dbPath);
                        if (!absolutePath.equals("")) {
                            Path filePath = Paths.get(absolutePath, dbName);
                            String dbFilePath = filePath.toString();
                            return String.format("jdbc:sqlite:%s", dbFilePath);
                        }
                        return dbName;
                    }).orElseGet(() -> "jdbc:sqlite:db/marketplace.db"));
//        return "jdbc:sqlite:src/main/resources/db/marketplace.db";
    }

    public abstract Optional<String> getName();

    @JsonProperty("db_path")
    public abstract Optional<String> getDbPath();

    static String normalizePath(String filePath) {
        Path path;
        if (filePath.startsWith("~")) {
            String userHome = System.getProperty("user.home");
            String dirName = filePath.replaceAll("~/", "");
            path = Paths.get(userHome, dirName);
        } else {
            path = Paths.get(filePath);
        }
        return path.normalize().toAbsolutePath().toString();
    }

    void setup() {
        String url = getDbUrl();
        String s = url.replaceAll("jdbc:sqlite:", "");

        int i = s.lastIndexOf("/");
        String localDirectory = s.substring(i, s.length() - 1);
//        String absolutePath = normalizePath(dbPath);
//        if (!absolutePath.equals("")) {
//            Path filePath = Paths.get(absolutePath, dbName);
//            String dbFilePath = filePath.toString();
//        }
        FlywayMigration.migrate(getDbUrl());
    }
}
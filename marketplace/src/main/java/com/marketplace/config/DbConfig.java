package com.marketplace.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.eventstore.jdbc.flyway.FlywayMigration;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDbConfig.class)
@JsonDeserialize(as = ImmutableDbConfig.class)
public abstract class DbConfig {

    @JsonProperty("url")
    public abstract Optional<String> getUrl();

    @JsonIgnore
    public String getDbUrl() {
        return getUrl().orElseGet(this::generateUrl);
    }

    private String generateUrl() {
        String dbPath = getDbPath().orElse("db");
        String dbName = getName().orElse("marketplace.db");
        return String.format("jdbc:sqlite:%s/%s", dbPath, dbName);
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

    private void ensureDbDirectoryExists(String url) {
        var urlWithoutParts = getFileLocation(url);
        var idx = urlWithoutParts.lastIndexOf("/");
        if (idx > 0) {
            var dir = urlWithoutParts.substring(0, idx);
            var filePath = Paths.get(dir);
            var file = filePath.toAbsolutePath().toFile();
            if (file.exists()) {
                System.out.println("Directory " + dir + " already exists");
            } else {
                if (!file.mkdirs()) {
                    System.out.println("error while creating directory " + file.getAbsolutePath());
                }
                System.out.println("Successfully created directory " + dir);
            }
        }
        FlywayMigration.migrate(getDbUrl());
    }

    private String getFileLocation(String url) {
        return url.replaceAll("jdbc:sqlite:", "");
    }

    public void postSetup() {
        String dbUrl = getDbUrl();
        String fileLoc = getFileLocation(dbUrl);
        if (!new File(fileLoc).exists()) {
            ensureDbDirectoryExists(dbUrl);
            FlywayMigration.migrate(dbUrl);
        }
    }
}
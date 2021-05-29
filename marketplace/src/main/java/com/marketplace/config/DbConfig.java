package com.marketplace.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Default;

@Value.Immutable
@JsonSerialize(as = ImmutableDbConfig.class)
@JsonDeserialize(as = ImmutableDbConfig.class)
public interface DbConfig {

    @Default
    default String getUrl() {
//        return "jdbc:sqlite:src/main/resources/db/marketplace.db";
        return "jdbc:sqlite:/Users/rnkoaa/workspace/marketplace-ddd/db/marketplace.db";
    }
}
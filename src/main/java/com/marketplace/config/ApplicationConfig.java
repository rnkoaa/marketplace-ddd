package com.marketplace.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableApplicationConfig.class)
@JsonDeserialize(as = ImmutableApplicationConfig.class)
public abstract class ApplicationConfig {

    @Value.Default
    public ServerConfig getServer() {
        return ImmutableServerConfig.builder().build();
    }

    @Value.Default
    public DbConfig getDb() {
        return ImmutableDbConfig.builder().build();
    }
}


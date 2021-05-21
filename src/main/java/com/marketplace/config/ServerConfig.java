package com.marketplace.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableServerConfig.class)
public interface ServerConfig {

    int DEFAULT_PORT = 8080;

    @Default
    default int getPort() {
        return DEFAULT_PORT;
    }

}

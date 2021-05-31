package com.marketplace.client.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableClientConfig.class)
public interface ClientConfig {

    @Value.Default
    default String getApplicationURL() {
        return "http://localhost:8080/";
    }

}

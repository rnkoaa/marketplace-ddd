package com.marketplace.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableServerConfig.class)
@JsonSerialize(as = ImmutableServerConfig.class)
public interface ServerConfig {

  int DEFAULT_PORT = 8080;

  @Default
  default int getPort() {
    return DEFAULT_PORT;
  }

}

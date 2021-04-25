package com.marketplace.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
public interface ServerConfig {

  int DEFAULT_PORT = 8080;

  @Default
  default int getPort() {
    return DEFAULT_PORT;
  }

}

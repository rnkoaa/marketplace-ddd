package com.marketplace.config;

import com.marketplace.common.config.MongoConfig;
import org.immutables.value.Value;

@Value.Immutable
public abstract class ApplicationConfig {

  @Value.Default
  public ServerConfig getServer() {
    return ImmutableServerConfig.builder().build();
  }

  @Value.Default
  public MongoConfig getMongo() {
    return new MongoConfig();
  }
}


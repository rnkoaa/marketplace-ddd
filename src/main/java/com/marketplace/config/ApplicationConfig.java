package com.marketplace.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.common.config.MongoConfig;
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
  public MongoConfig getMongo() {
    return new MongoConfig();
  }
}


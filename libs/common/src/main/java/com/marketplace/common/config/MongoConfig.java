package com.marketplace.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoConfig {
  private String hosts = "localhost";
  private String database = "test_db";
  int port = 27017;

  public String getConnectionString() {
    return String.format("mongodb://%s:%d", hosts, port);
  }
}

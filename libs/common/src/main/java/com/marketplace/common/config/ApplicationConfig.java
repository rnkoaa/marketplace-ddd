package com.marketplace.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig {
  private MongoConfig mongo = new MongoConfig();
}

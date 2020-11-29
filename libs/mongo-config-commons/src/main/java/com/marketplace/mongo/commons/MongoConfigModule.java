package com.marketplace.mongo.commons;

import com.marketplace.common.config.ApplicationConfig;
import com.marketplace.common.config.MongoConfig;
import com.marketplace.config.ConfigLoader;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.io.IOException;

@Module
public class MongoConfigModule {
  @Provides
  @Singleton
  public MongoConfig provideMongoConfig() {
    try {
      var config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
      return config.getMongo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new MongoConfig();
  }
}

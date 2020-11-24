package com.marketplace.client.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.client.ClassifiedAdRestService;
import com.marketplace.client.config.ClientConfig;
import com.marketplace.common.ObjectMapperBuilder;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public abstract class ClientModule {

  @Provides
  @Singleton
  static OkHttpClient okHttpClient() {
    return new OkHttpClient();
  }

  @Provides
  @Singleton
  static ObjectMapper provideObjectMapper() {
    return new ObjectMapperBuilder().build();
  }

  @Provides
  @Singleton
  static Retrofit provideRetrofit(ClientConfig config, ObjectMapper objectMapper, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl(config.getApplicationUrl())
        .client(okHttpClient)
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build();
  }

  @Provides
  @Singleton
  static ClassifiedAdRestService provideClassifiedAdRestService(Retrofit retrofit) {
    return retrofit.create(ClassifiedAdRestService.class);
  }
}

package com.marketplace.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ObjectMapperModule_ProvideObjectMapperFactory implements Factory<ObjectMapper> {
  @Override
  public ObjectMapper get() {
    return provideObjectMapper();
  }

  public static ObjectMapperModule_ProvideObjectMapperFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ObjectMapper provideObjectMapper() {
    return Preconditions.checkNotNull(ObjectMapperModule.provideObjectMapper(), "Cannot return null from a non-@Nullable @Provides method");
  }

  private static final class InstanceHolder {
    private static final ObjectMapperModule_ProvideObjectMapperFactory INSTANCE = new ObjectMapperModule_ProvideObjectMapperFactory();
  }
}

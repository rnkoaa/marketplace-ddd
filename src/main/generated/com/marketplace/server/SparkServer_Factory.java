package com.marketplace.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.controller.ClassifiedAdController;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SparkServer_Factory implements Factory<SparkServer> {
  private final Provider<Integer> portProvider;

  private final Provider<ObjectMapper> objectMapperProvider;

  private final Provider<ClassifiedAdController> controllerProvider;

  public SparkServer_Factory(Provider<Integer> portProvider,
      Provider<ObjectMapper> objectMapperProvider,
      Provider<ClassifiedAdController> controllerProvider) {
    this.portProvider = portProvider;
    this.objectMapperProvider = objectMapperProvider;
    this.controllerProvider = controllerProvider;
  }

  @Override
  public SparkServer get() {
    return newInstance(portProvider.get(), objectMapperProvider.get(), controllerProvider.get());
  }

  public static SparkServer_Factory create(Provider<Integer> portProvider,
      Provider<ObjectMapper> objectMapperProvider,
      Provider<ClassifiedAdController> controllerProvider) {
    return new SparkServer_Factory(portProvider, objectMapperProvider, controllerProvider);
  }

  public static SparkServer newInstance(int port, ObjectMapper objectMapper,
      ClassifiedAdController controller) {
    return new SparkServer(port, objectMapper, controller);
  }
}

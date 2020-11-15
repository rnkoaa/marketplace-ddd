package com.marketplace.controller;

import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
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
public final class ClassifiedAdController_Factory implements Factory<ClassifiedAdController> {
  private final Provider<ClassifiedAdService> classifiedAdServiceProvider;

  public ClassifiedAdController_Factory(Provider<ClassifiedAdService> classifiedAdServiceProvider) {
    this.classifiedAdServiceProvider = classifiedAdServiceProvider;
  }

  @Override
  public ClassifiedAdController get() {
    return newInstance(classifiedAdServiceProvider.get());
  }

  public static ClassifiedAdController_Factory create(
      Provider<ClassifiedAdService> classifiedAdServiceProvider) {
    return new ClassifiedAdController_Factory(classifiedAdServiceProvider);
  }

  public static ClassifiedAdController newInstance(ClassifiedAdService classifiedAdService) {
    return new ClassifiedAdController(classifiedAdService);
  }
}

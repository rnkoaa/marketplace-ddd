package com.marketplace.domain.classifiedad.command;

import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
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
public final class UpdateClassifiedAdCommandHandler_Factory implements Factory<UpdateClassifiedAdCommandHandler> {
  private final Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider;

  public UpdateClassifiedAdCommandHandler_Factory(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider) {
    this.classifiedAdRepositoryProvider = classifiedAdRepositoryProvider;
  }

  @Override
  public UpdateClassifiedAdCommandHandler get() {
    return newInstance(classifiedAdRepositoryProvider.get());
  }

  public static UpdateClassifiedAdCommandHandler_Factory create(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider) {
    return new UpdateClassifiedAdCommandHandler_Factory(classifiedAdRepositoryProvider);
  }

  public static UpdateClassifiedAdCommandHandler newInstance(
      ClassifiedAdRepository classifiedAdRepository) {
    return new UpdateClassifiedAdCommandHandler(classifiedAdRepository);
  }
}

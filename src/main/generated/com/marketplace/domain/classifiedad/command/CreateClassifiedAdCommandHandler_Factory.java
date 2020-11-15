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
public final class CreateClassifiedAdCommandHandler_Factory implements Factory<CreateClassifiedAdCommandHandler> {
  private final Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider;

  public CreateClassifiedAdCommandHandler_Factory(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider) {
    this.classifiedAdRepositoryProvider = classifiedAdRepositoryProvider;
  }

  @Override
  public CreateClassifiedAdCommandHandler get() {
    return newInstance(classifiedAdRepositoryProvider.get());
  }

  public static CreateClassifiedAdCommandHandler_Factory create(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider) {
    return new CreateClassifiedAdCommandHandler_Factory(classifiedAdRepositoryProvider);
  }

  public static CreateClassifiedAdCommandHandler newInstance(
      ClassifiedAdRepository classifiedAdRepository) {
    return new CreateClassifiedAdCommandHandler(classifiedAdRepository);
  }
}

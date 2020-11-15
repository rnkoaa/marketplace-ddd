package com.marketplace.domain.classifiedad.service;

import com.marketplace.domain.classifiedad.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdCommandHandler;
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
public final class ClassifiedAdService_Factory implements Factory<ClassifiedAdService> {
  private final Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider;

  private final Provider<CreateClassifiedAdCommandHandler> createCommandHandlerProvider;

  private final Provider<UpdateClassifiedAdCommandHandler> updateCommandHandlerProvider;

  public ClassifiedAdService_Factory(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider,
      Provider<CreateClassifiedAdCommandHandler> createCommandHandlerProvider,
      Provider<UpdateClassifiedAdCommandHandler> updateCommandHandlerProvider) {
    this.classifiedAdRepositoryProvider = classifiedAdRepositoryProvider;
    this.createCommandHandlerProvider = createCommandHandlerProvider;
    this.updateCommandHandlerProvider = updateCommandHandlerProvider;
  }

  @Override
  public ClassifiedAdService get() {
    return newInstance(classifiedAdRepositoryProvider.get(), createCommandHandlerProvider.get(), updateCommandHandlerProvider.get());
  }

  public static ClassifiedAdService_Factory create(
      Provider<ClassifiedAdRepository> classifiedAdRepositoryProvider,
      Provider<CreateClassifiedAdCommandHandler> createCommandHandlerProvider,
      Provider<UpdateClassifiedAdCommandHandler> updateCommandHandlerProvider) {
    return new ClassifiedAdService_Factory(classifiedAdRepositoryProvider, createCommandHandlerProvider, updateCommandHandlerProvider);
  }

  public static ClassifiedAdService newInstance(ClassifiedAdRepository classifiedAdRepository,
      CreateClassifiedAdCommandHandler createCommandHandler,
      UpdateClassifiedAdCommandHandler updateCommandHandler) {
    return new ClassifiedAdService(classifiedAdRepository, createCommandHandler, updateCommandHandler);
  }
}

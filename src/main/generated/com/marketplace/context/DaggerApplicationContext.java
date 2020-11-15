package com.marketplace.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.config.ApplicationConfig;
import com.marketplace.context.mongo.MongoConfig;
import com.marketplace.context.mongo.MongoConfigModule_ProvideMongoClientFactory;
import com.marketplace.context.mongo.MongoConfigModule_ProvideMongoConfigFactory;
import com.marketplace.context.server.SparkServerModule_ProvideServerPortFactory;
import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdCommandHandler;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdMongoRepositoryImpl;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdMongoRepositoryImpl_Factory;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.domain.repository.MongoTemplate;
import com.marketplace.domain.repository.MongoTemplate_Factory;
import com.marketplace.server.SparkServer;
import com.mongodb.client.MongoClient;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
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
public final class DaggerApplicationContext implements ApplicationContext {
  private Provider<ApplicationConfig> configProvider;

  private Provider<Integer> provideServerPortProvider;

  private Provider<ObjectMapper> provideObjectMapperProvider;

  private Provider<MongoConfig> provideMongoConfigProvider;

  private Provider<MongoClient> provideMongoClientProvider;

  private Provider<MongoTemplate> mongoTemplateProvider;

  private Provider<ClassifiedAdMongoRepositoryImpl> classifiedAdMongoRepositoryImplProvider;

  private Provider<ClassifiedAdRepository> bindClassifiedAdRepositoryProvider;

  private DaggerApplicationContext(ApplicationConfig configParam) {

    initialize(configParam);
  }

  public static ApplicationContext.Builder builder() {
    return new Builder();
  }

  private CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler() {
    return new CreateClassifiedAdCommandHandler(bindClassifiedAdRepositoryProvider.get());
  }

  private UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler() {
    return new UpdateClassifiedAdCommandHandler(bindClassifiedAdRepositoryProvider.get());
  }

  private ClassifiedAdService classifiedAdService() {
    return new ClassifiedAdService(bindClassifiedAdRepositoryProvider.get(), createClassifiedAdCommandHandler(), updateClassifiedAdCommandHandler());
  }

  private ClassifiedAdController classifiedAdController() {
    return new ClassifiedAdController(classifiedAdService());
  }

  @SuppressWarnings("unchecked")
  private void initialize(final ApplicationConfig configParam) {
    this.configProvider = InstanceFactory.create(configParam);
    this.provideServerPortProvider = DoubleCheck.provider(SparkServerModule_ProvideServerPortFactory.create(configProvider));
    this.provideObjectMapperProvider = DoubleCheck.provider(ObjectMapperModule_ProvideObjectMapperFactory.create());
    this.provideMongoConfigProvider = DoubleCheck.provider(MongoConfigModule_ProvideMongoConfigFactory.create(configProvider));
    this.provideMongoClientProvider = DoubleCheck.provider(MongoConfigModule_ProvideMongoClientFactory.create(provideMongoConfigProvider));
    this.mongoTemplateProvider = MongoTemplate_Factory.create(provideMongoConfigProvider, provideMongoClientProvider);
    this.classifiedAdMongoRepositoryImplProvider = ClassifiedAdMongoRepositoryImpl_Factory.create(mongoTemplateProvider);
    this.bindClassifiedAdRepositoryProvider = DoubleCheck.provider((Provider) classifiedAdMongoRepositoryImplProvider);
  }

  @Override
  public SparkServer getServer() {
    return new SparkServer(provideServerPortProvider.get(), provideObjectMapperProvider.get(), classifiedAdController());
  }

  private static final class Builder implements ApplicationContext.Builder {
    private ApplicationConfig config;

    @Override
    public Builder config(ApplicationConfig config) {
      this.config = Preconditions.checkNotNull(config);
      return this;
    }

    @Override
    public ApplicationContext build() {
      Preconditions.checkBuilderRequirement(config, ApplicationConfig.class);
      return new DaggerApplicationContext(config);
    }
  }
}

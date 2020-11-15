package com.marketplace.domain.classifiedad.repository;

import com.marketplace.domain.repository.MongoTemplate;
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
public final class ClassifiedAdMongoRepositoryImpl_Factory implements Factory<ClassifiedAdMongoRepositoryImpl> {
  private final Provider<MongoTemplate> mongoTemplateProvider;

  public ClassifiedAdMongoRepositoryImpl_Factory(Provider<MongoTemplate> mongoTemplateProvider) {
    this.mongoTemplateProvider = mongoTemplateProvider;
  }

  @Override
  public ClassifiedAdMongoRepositoryImpl get() {
    return newInstance(mongoTemplateProvider.get());
  }

  public static ClassifiedAdMongoRepositoryImpl_Factory create(
      Provider<MongoTemplate> mongoTemplateProvider) {
    return new ClassifiedAdMongoRepositoryImpl_Factory(mongoTemplateProvider);
  }

  public static ClassifiedAdMongoRepositoryImpl newInstance(MongoTemplate mongoTemplate) {
    return new ClassifiedAdMongoRepositoryImpl(mongoTemplate);
  }
}

package com.marketplace.domain.classifiedad.read;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdReadService {

  private final ClassifiedAdMongoReadRepositoryImpl classifiedAdMongoReadRepository;

  @Inject
  public ClassifiedAdReadService(
      ClassifiedAdMongoReadRepositoryImpl classifiedAdMongoReadRepository) {
    this.classifiedAdMongoReadRepository = classifiedAdMongoReadRepository;
  }

  public Optional<ClassifiedAdReadEntity> findById(UUID id) {
    return classifiedAdMongoReadRepository.findById(id);
  }
}

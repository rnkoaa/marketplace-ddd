package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAdState;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@Singleton
public class ClassifiedAdQueryController {

  private final ClassifiedAdQueryService classifiedAdQueryService;

  @Inject
  public ClassifiedAdQueryController(ClassifiedAdQueryService classifiedAdQueryService) {
    this.classifiedAdQueryService = classifiedAdQueryService;
  }

  public Optional<ClassifiedAdQueryEntity> findEntityById(UUID classifiedAdId) {
    return classifiedAdQueryService.findById(classifiedAdId);
  }

  public List<ClassifiedAdQueryEntity> findAll() {
    return classifiedAdQueryService.findAll();
  }

  public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
   return classifiedAdQueryService.findByOwner(ownerId);
  }

  public List<ClassifiedAdQueryEntity> findByOwnerPublished(UUID owner, ClassifiedAdState status) {
    return classifiedAdQueryService.find(owner, status);
  }
}

package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAdState;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdQueryService {

  private final ClassifiedAdQueryRepository classifiedAdQueryRepository;

  @Inject
  public ClassifiedAdQueryService(
      ClassifiedAdQueryRepository classifiedAdQueryRepository) {
    this.classifiedAdQueryRepository = classifiedAdQueryRepository;
  }

  public Optional<ClassifiedAdQueryEntity> findById(UUID id) {
    return classifiedAdQueryRepository.findById(id);
  }

  public List<ClassifiedAdQueryEntity> findAll() {
    return classifiedAdQueryRepository.findAll();
  }

  public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
    return classifiedAdQueryRepository.findByOwner(ownerId);
  }

  public List<ClassifiedAdQueryEntity> find(UUID owner, ClassifiedAdState status) {
//    Bson bson;
//    if (owner != null && status != null) {
//      bson = Filters.and(Filters.eq("owner", owner), Filters.eq("state", status.name()));
//    } else if (owner == null) {
//      bson = Filters.eq("state", status.name());
//    } else {
//      bson = Filters.eq("owner", owner);
//    }
//    return classifiedAdMongoQueryRepository.find(bson);
      return List.of();
  }
}

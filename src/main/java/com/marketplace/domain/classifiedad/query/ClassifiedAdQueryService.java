package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.cqrs.framework.Strings;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdQueryService {

  private final ClassifiedAdMongoQueryRepository classifiedAdMongoQueryRepository;

  @Inject
  public ClassifiedAdQueryService(
      ClassifiedAdMongoQueryRepository classifiedAdMongoQueryRepository) {
    this.classifiedAdMongoQueryRepository = classifiedAdMongoQueryRepository;
  }

  public Optional<ClassifiedAdQueryEntity> findById(UUID id) {
    return classifiedAdMongoQueryRepository.findById(id);
  }

  public List<ClassifiedAdQueryEntity> findAll() {
    return classifiedAdMongoQueryRepository.findAll();
  }

  public List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId) {
    return classifiedAdMongoQueryRepository.findByOwner(ownerId);
  }

  public List<ClassifiedAdQueryEntity> find(UUID owner, ClassifiedAdState status) {
    Bson bson;
    if (owner != null && status != null) {
      bson = Filters.and(Filters.eq("owner", owner), Filters.eq("state", status.name()));
    } else if (owner == null) {
      bson = Filters.eq("state", status.name());
    } else {
      bson = Filters.eq("owner", owner);
    }
    return classifiedAdMongoQueryRepository.find(bson);
  }
}

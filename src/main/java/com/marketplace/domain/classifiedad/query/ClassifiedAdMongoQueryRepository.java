package com.marketplace.domain.classifiedad.query;

import java.util.List;
import java.util.UUID;

public interface ClassifiedAdMongoQueryRepository
    extends QueryRepository<ClassifiedAdQueryEntity, UUID> {
  List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId);

  List<ClassifiedAdQueryEntity> find();
}

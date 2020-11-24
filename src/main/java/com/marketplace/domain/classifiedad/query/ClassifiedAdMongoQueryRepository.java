package com.marketplace.domain.classifiedad.query;

import org.bson.conversions.Bson;

import java.util.List;
import java.util.UUID;

public interface ClassifiedAdMongoQueryRepository
    extends QueryRepository<ClassifiedAdQueryEntity, UUID> {
  List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId);

  List<ClassifiedAdQueryEntity> find(Bson filter);
}

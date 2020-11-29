package com.marketplace.eventstore.mongodb;

import java.util.List;
import java.util.UUID;

public class MongoEventStoreRepositoryImpl implements EventStoreRepository<MongoEventEntity, UUID> {
  private final MongoEventEntityRepository mongoEventEntityRepository;

  public MongoEventStoreRepositoryImpl(MongoEventEntityRepository mongoEventEntityRepository) {
    this.mongoEventEntityRepository = mongoEventEntityRepository;
  }

  @Override
  public List<MongoEventEntity> load(UUID aggregateId, int fromVersion) {
    return null;
  }

  @Override
  public List<MongoEventEntity> load(UUID aggregateId) {
    return null;
  }

  @Override
  public MongoEventEntity save(UUID aggregateId, MongoEventEntity event) {
    return null;
  }

  @Override
  public List<MongoEventEntity> save(UUID aggregateId, List<MongoEventEntity> events, int version) {
    return null;
  }

  @Override
  public MongoEventEntity save(UUID aggregateId, MongoEventEntity event, int version) {
    return null;
  }

  @Override
  public int lastVersion(UUID aggregateId) {
    return 0;
  }
}

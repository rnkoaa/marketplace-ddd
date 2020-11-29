package com.marketplace.eventstore.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.criteria.Criteria;
import org.immutables.criteria.repository.reactive.ReactiveReadable;
import org.immutables.criteria.repository.reactive.ReactiveWritable;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@Criteria
@Criteria.Repository(facets = {ReactiveReadable.class, ReactiveWritable.class})
@JsonSerialize(as = ImmutableMongoEventEntity.class)
@JsonDeserialize(as = ImmutableMongoEventEntity.class)
public interface MongoEventEntity {
  @JsonProperty("_id")
  @Criteria.Id
  UUID getId();

  UUID getAggregateId();

  String getStreamName();

  String getEventType();

  String getEventBody();

  int getVersion();

  Instant getCreatedAt();
}

package com.marketplace.eventstore.test.data;

import static com.marketplace.eventstore.test.data.TestEvents.testCreatedEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.marketplace.eventstore.mongodb.ImmutableMongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import io.vavr.control.Try;
import java.util.List;

public class TestMongoEvents {

  static ObjectMapper objectMapper = new ObjectMapperBuilder()
      .build();

  public static TypedEvent typedEvent = ImmutableTypedEvent.builder()
      .sequenceId(0)
      .type(testCreatedEvent.getClass().getCanonicalName())
      .eventBody(serialize(testCreatedEvent))
      .build();

  public static MongoEventEntity testCreatedMongoEntity = ImmutableMongoEventEntity.builder()
      .aggregateId(testCreatedEvent.getAggregateId())
      .id(testCreatedEvent.getId())
      .createdAt(testCreatedEvent.createdAt())
      .events(List.of(typedEvent))
      .version(0)
      .streamName("ClassifiedAds:%s".formatted(testCreatedEvent.getId().toString()))
      .build();

  static String serialize(Object object) {
    return Try.of(() -> objectMapper.writeValueAsString(object))
        .getOrElse("");
  }
}

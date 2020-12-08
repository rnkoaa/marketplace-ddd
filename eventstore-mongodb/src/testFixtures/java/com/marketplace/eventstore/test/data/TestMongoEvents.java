package com.marketplace.eventstore.test.data;

import static com.marketplace.eventstore.test.data.TestEvents.testCreatedEvent;
import static com.marketplace.eventstore.test.data.TestEvents.testTextUpdatedEvent;
import static com.marketplace.eventstore.test.data.TestEvents.testTitleUpdatedEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.marketplace.eventstore.mongodb.ImmutableMongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import io.vavr.control.Try;
import java.util.List;

public class TestMongoEvents {

    public static ObjectMapper objectMapper = new ObjectMapperBuilder()
        .build();

    public static TypedEvent testCreatedTypedEvent = ImmutableTypedEvent.builder()
        .sequenceId(0)
        .type(testCreatedEvent.getClass().getCanonicalName())
        .eventBody(serialize(testCreatedEvent))
        .build();

    public static MongoEventEntity testCreatedMongoEntity = ImmutableMongoEventEntity.builder()
        .aggregateId(testCreatedEvent.getAggregateId())
        .id(testCreatedEvent.getId())
        .createdAt(testCreatedEvent.getCreatedAt())
        .events(List.of(testCreatedTypedEvent))
        .version(0)
        .streamName("ClassifiedAds:%s".formatted(testCreatedEvent.getAggregateId().toString()))
        .build();

    public static TypedEvent testTextUpdatedTypedEvent = ImmutableTypedEvent.builder()
        .sequenceId(0)
        .type(testTextUpdatedEvent.getClass().getCanonicalName())
        .eventBody(serialize(testTextUpdatedEvent))
        .build();

    public static MongoEventEntity testTextUpdatedMongoEntity = ImmutableMongoEventEntity.builder()
        .aggregateId(testTextUpdatedEvent.getAggregateId())
        .id(testTextUpdatedEvent.getId())
        .createdAt(testTextUpdatedEvent.getCreatedAt())
        .events(List.of(testTextUpdatedTypedEvent))
        .version(0)
        .streamName("ClassifiedAds:%s".formatted(testCreatedEvent.getAggregateId().toString()))
        .build();

    public static TypedEvent testTitleUpdatedTypedEvent = ImmutableTypedEvent.builder()
        .sequenceId(0)
        .type(testTitleUpdatedEvent.getClass().getCanonicalName())
        .eventBody(serialize(testTitleUpdatedEvent))
        .build();

    public static List<TypedEvent> sequencedEvents = List.of(
        testCreatedTypedEvent,
        ImmutableTypedEvent.builder()
            .sequenceId(1)
            .type(testTitleUpdatedEvent.getClass().getCanonicalName())
            .eventBody(serialize(testTitleUpdatedEvent))
            .build(),
        ImmutableTypedEvent.builder()
            .sequenceId(2)
            .type(testTextUpdatedEvent.getClass().getCanonicalName())
            .eventBody(serialize(testTextUpdatedEvent))
            .build()
    );

    public static List<MongoEventEntity> versionedEntityEvents = List.of(
        testCreatedMongoEntity,
        ImmutableMongoEventEntity.builder()
            .aggregateId(testTitleUpdatedEvent.getAggregateId())
            .id(testTitleUpdatedEvent.getId())
            .createdAt(testTitleUpdatedEvent.getCreatedAt())
            .events(List.of(testTitleUpdatedTypedEvent))
            .version(1)
            .streamName(
                "ClassifiedAds:%s".formatted(testTitleUpdatedEvent.getAggregateId().toString()))
            .build(),
        ImmutableMongoEventEntity.builder()
            .aggregateId(testTextUpdatedEvent.getAggregateId())
            .id(testTextUpdatedEvent.getId())
            .createdAt(testTextUpdatedEvent.getCreatedAt())
            .events(List.of(testTextUpdatedTypedEvent))
            .version(2)
            .streamName("ClassifiedAds:%s".formatted(testCreatedEvent.getAggregateId().toString()))
            .build()
    );

    static String serialize(Object object) {
        return Try.of(() -> objectMapper.writeValueAsString(object))
            .getOrElse("");
    }
}

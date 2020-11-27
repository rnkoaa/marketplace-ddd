package com.marketplace.eventstore.impl;

import com.google.common.eventbus.EventBus;
import com.marketplace.eventstore.framework.OperationResult.Success;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventListener;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.impl.fixtures.classifiedad.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("UnstableApiUsage")
class InMemoryEventStoreTest {

  private EventStore<Event> eventStore;
  private final ClassifiedAdEventProcessor eventProcessor = Mockito.mock(ClassifiedAdEventProcessor.class);
  private final EventListener classifiedAdEventListener = new ClassifiedAdEventListener(eventProcessor);

  InMemoryEventPublisher eventPublisher;

  @BeforeEach
  void setup() {
    var eventBus = new EventBus();
    eventPublisher = new InMemoryEventPublisher(eventBus);
    eventStore = new InMemoryEventStore(eventPublisher);
    eventPublisher.registerListener(classifiedAdEventListener);
  }

  @AfterEach
  void cleanup() {
    eventPublisher.close();
  }

  @Test
  void emptyEventStoreWithoutEvents() {
    assertThat(eventStore.size()).isEqualTo(0);
  }

  @Test
  void createAndUpdateTitleInEventStore() {
    String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
    var classifiedAdCreated = new ClassifiedAdCreated(ownerId1, classifiedAdId1);

    String streamId = String.format("%s:%s", classifiedAdCreated.aggregateName(), classifiedAdId1);
    eventStore.append(streamId, 0, classifiedAdCreated);

    assertThat(eventStore.size()).isEqualTo(1);

    var classifiedAdTextUpdated = new ClassifiedAdTitleUpdated(classifiedAdId1, "test title");

    EventStream<Event> eventStream = eventStore.load(streamId);
    assertThat(eventStream.getVersion()).isEqualTo(0);

    int expectedVersion = eventStream.getVersion() + 1;
    var appendResult = eventStore.append(streamId, expectedVersion, classifiedAdTextUpdated);
    assertThat(eventStore.size()).isEqualTo(1);
    assertThat(appendResult).isInstanceOf(Success.class);

    eventStream = eventStore.load(streamId);
    assertThat(eventStream.getVersion()).isEqualTo(1);
    assertThat(eventStream.size()).isEqualTo(2);
  }

  @Test
  void multipleEventsCanBeAdded() {
    String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
    var classifiedAdCreated = new ClassifiedAdCreated(ownerId1, classifiedAdId1);

    String streamId = String.format("%s:%s", classifiedAdCreated.aggregateName(), classifiedAdId1);

    var classifiedAdTitleUpdated = new ClassifiedAdTitleUpdated(classifiedAdId1, "test title");
    var result =
        eventStore.append(streamId, 0, List.of(classifiedAdCreated, classifiedAdTitleUpdated));
    assertThat(result).isInstanceOf(Success.class);
    assertThat(eventStore.size()).isEqualTo(1);

    EventStream<Event> eventStream = eventStore.load(streamId);
    assertThat(eventStream.size()).isEqualTo(2);
    assertThat(eventStream.getVersion()).isEqualTo(1);
  }

  @Test
  void canCreateEventStoreWithNewEvent() {
    String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
    var classifiedAdCreated = new ClassifiedAdCreated(ownerId1, classifiedAdId1);

    String streamId = String.format("%s:%s", classifiedAdCreated.aggregateName(), classifiedAdId1);
    eventStore.append(streamId, 0, classifiedAdCreated);

    assertThat(eventStore.size()).isEqualTo(1);
  }

  @Test
  void differentAggregatesWillCreateDifferentStreams() {
    String classifiedAdId2 = "582a7769-f08a-470d-af65-85ef7ff7969f";
    String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";

    String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId1);

    var result =
        eventStore.append(
            streamId,
            0,
            createEventsAggregate(UUID.fromString(classifiedAdId1), UUID.fromString(ownerId1)));

    String streamId2 = String.format("%s:%s", "ClassifiedAd", classifiedAdId2);
    result =
        eventStore.append(
            streamId2,
            0,
            createEventsAggregate(UUID.fromString(classifiedAdId2), UUID.fromString(ownerId1)));

    assertThat(result).isInstanceOf(Success.class);
    assertThat(eventStore.size()).isEqualTo(2);
  }

  @Test
  void eventsCanBeLoadedFromVersion() {
    String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";

    String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId1);

    var result =
        eventStore.append(
            streamId,
            0,
            createEventsAggregate(UUID.fromString(classifiedAdId1), UUID.fromString(ownerId1)));
    assertThat(result).isInstanceOf(Success.class);
    assertThat(eventStore.size()).isEqualTo(1);

    EventStream<Event> eventStream = eventStore.load(streamId);
    assertThat(eventStream.size()).isEqualTo(2);
    assertThat(eventStream.getVersion()).isEqualTo(1);
  }

  @Test
  void publishSavesEventAndPublishes() {
    String classifiedAdId = "9d5d69ee-eadd-4352-942e-47935e194d22";
    String ownerId = "89b69f4f-e36e-4f2b-baa0-d47057e02117";

    String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId);

    var classifiedAdCreated = new ClassifiedAdCreated(ownerId, classifiedAdId);

    var result = eventStore.publish(streamId, 0, classifiedAdCreated);
    assertThat(result).isInstanceOf(Success.class);
    assertThat(eventStore.size()).isEqualTo(1);

    EventStream<Event> eventStream = eventStore.load(streamId);
    assertThat(eventStream.size()).isEqualTo(1);
    assertThat(eventStream.getVersion()).isEqualTo(0);

    verify(eventProcessor, times(1)).create(classifiedAdCreated);
  }

  List<Event> createEventsAggregate(UUID aggregateId, UUID ownerId) {
    var classifiedAdCreated = new ClassifiedAdCreated(ownerId, aggregateId);
    var classifiedAdTextUpdated =
        new ClassifiedAdTextUpdated(aggregateId, "test classified ad text");
    return List.of(classifiedAdCreated, classifiedAdTextUpdated);
  }
}

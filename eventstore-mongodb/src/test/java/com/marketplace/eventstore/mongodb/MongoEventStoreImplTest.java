package com.marketplace.eventstore.mongodb;

import static com.marketplace.eventstore.test.data.TestEvents.aggregateId;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;

import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.test.data.TestEvents;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MongoEventStoreImplTest {

  private EventPublisher<Event> eventPublisher = mock(EventPublisher.class);
  private MongoEventStoreRepository eventStoreRepository = mock(MongoEventStoreRepository.class);

  private EventStore<Event> eventStore = new MongoEventStoreImpl(eventStoreRepository, eventPublisher);

  @Nested
  class Load {

    @Test
    void contextIsCreated() {
      assertThat(eventStore).isNotNull();
    }

    @Test
    void malformedStreamIdWillResultInAnError() {
      String streamId = UUID.randomUUID().toString();
      Mono<EventStream<Event>> load = eventStore.load(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void invalidStreamIdWithBadUUIDWillNotWork() {
      String streamId = "ClassifiedAd:hello-world";
      Mono<EventStream<Event>> load = eventStore.load(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void successfullyLoadEventsForValidStreamIdFromVersion() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      int fromVersion = 1;
      List<Event> testEvents = TestEvents.aggregateEvents.stream()
          .filter(it -> it.getVersion() >= fromVersion)
          .collect(Collectors.toList());

      Mockito.when(eventStoreRepository.load(aggregateId, fromVersion)).thenReturn(Mono.just(testEvents));

      Mono<EventStream<Event>> eventStreamMono = eventStore.load(validStreamId, fromVersion);
      StepVerifier.create(eventStreamMono)
          .assertNext(eventStream -> {
            assertThat(eventStream).isNotNull();
            assertThat(eventStream.size()).isEqualTo(2);
            assertThat(eventStream.getVersion()).isEqualTo(2);
            assertThat(eventStream.getId()).isEqualTo(validStreamId);
            assertThat(eventStream.getName()).isNotBlank().isEqualTo("ClassifiedAd");
          })
          .verifyComplete();
    }

    @Test
    void successfullyLoadEventsForValidStreamId() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.load(aggregateId)).thenReturn(Mono.just(TestEvents.aggregateEvents));

      Mono<EventStream<Event>> eventStreamMono = eventStore.load(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(eventStream -> {
            assertThat(eventStream).isNotNull();
            assertThat(eventStream.size()).isEqualTo(3);
            assertThat(eventStream.getVersion()).isEqualTo(2);
            assertThat(eventStream.getId()).isEqualTo(validStreamId);
            assertThat(eventStream.getName()).isNotBlank().isEqualTo("ClassifiedAd");
          })
          .verifyComplete();
    }
  }

}
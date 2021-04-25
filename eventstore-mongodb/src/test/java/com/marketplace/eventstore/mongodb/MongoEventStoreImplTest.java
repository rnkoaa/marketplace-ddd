package com.marketplace.eventstore.mongodb;

import static com.marketplace.eventstore.test.data.TestEvents.aggregateEvents;
import static com.marketplace.eventstore.test.data.TestEvents.aggregateId;
import static com.marketplace.eventstore.test.data.TestEvents.testCreatedEvent;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.test.data.TestEvents;
import java.util.List;
import java.util.Optional;
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

    @Test
    void successfullyLoadEmptyEventsForValidStreamId() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.load(aggregateId)).thenReturn(Mono.just(List.of()));

      Mono<EventStream<Event>> eventStreamMono = eventStore.load(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(eventStream -> {
            assertThat(eventStream.size()).isEqualTo(0);
            assertThat(eventStream.getVersion()).isEqualTo(0);
            assertThat(eventStream.getName()).isBlank();
          })
          .verifyComplete();
    }
  }

  @Nested
  class Append {

    @Test
    void saveMultipleEventsWillSucceed() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, TestEvents.aggregateEvents, 0))
          .thenReturn(Mono.just(Optional.of(true)));
      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.append(streamId, 0, aggregateEvents);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Success.class);
          })
          .verifyComplete();
    }

    @Test
    void failingToPersistEventReturnsFailureOperationResult() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, TestEvents.aggregateEvents, 0))
          .thenReturn(Mono.just(Optional.of(false)));
      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.append(streamId, 0, aggregateEvents);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Failure.class);
          })
          .verifyComplete();
    }

    @Test
    void saveOneEventIfEventStreamDoesNotExist() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, testCreatedEvent, 0)).thenReturn(Mono.just(Optional.of(true)));
      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.append(streamId, 0, testCreatedEvent);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Success.class);
          })
          .verifyComplete();
    }
  }

  @Nested
  class PublishEvents {

    @Test
    void multipleEventsCanBeAppendedThenPublished() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, TestEvents.aggregateEvents, 0))
          .thenReturn(Mono.just(Optional.of(true)));

      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.publish(streamId, 0, aggregateEvents);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Success.class);
          })
          .verifyComplete();
      verify(eventPublisher, times(3)).publish(Mockito.anyString(), Mockito.any(Event.class));
    }

    @Test
    void failingToAppendEventsPreventsEventsFromBeingPublished() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, TestEvents.aggregateEvents, 0))
          .thenReturn(Mono.just(Optional.of(false)));
      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.publish(streamId, 0, aggregateEvents);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Failure.class);
          })
          .verifyComplete();
      verify(eventPublisher, times(0)).publish(Mockito.anyString(), Mockito.any(Event.class));
    }

    @Test
    void saveOneEventIfEventStreamDoesNotExist() {
      Mockito.when(eventStoreRepository.save(TestEvents.aggregateId, testCreatedEvent, 0)).thenReturn(Mono.just(Optional.of(true)));
      String streamId = String.format("ClassifiedAd:%s", aggregateId.toString());
      Mono<OperationResult> append = eventStore.publish(streamId, 0, testCreatedEvent);

      StepVerifier.create(append)
          .assertNext(res -> {
            assertThat(res).isInstanceOf(OperationResult.Success.class);
          })
          .verifyComplete();
      verify(eventPublisher, times(1)).publish(Mockito.anyString(), Mockito.any(Event.class));
    }
  }

  @Nested
  class StreamVersion {

    @Test
    void malformedStreamIdWillResultInAnError() {
      String streamId = UUID.randomUUID().toString();
      Mono<Long> load = eventStore.getVersion(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void invalidStreamIdWithBadUUIDWillNotWork() {
      String streamId = "ClassifiedAd:hello-world";
      Mono<Long> load = eventStore.getVersion(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void canRetrieveStreamVersionBasedOnStreamId() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.getVersion(aggregateId)).thenReturn(Mono.just(10L));

      Mono<Long> eventStreamMono = eventStore.getVersion(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(version -> {
            assertThat(version).isEqualTo(10);
          })
          .verifyComplete();
    }

    @Test
    void nonExistentStreamShouldStillReturnANumber() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.getVersion(aggregateId)).thenReturn(Mono.just(0L));

      Mono<Long> eventStreamMono = eventStore.getVersion(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(version -> {
            assertThat(version).isEqualTo(0L);
          })
          .verifyComplete();
    }
  }

  @Nested
  class StreamSize {

    @Test
    void malformedStreamIdWillResultInAnError() {
      String streamId = UUID.randomUUID().toString();
      Mono<Long> load = eventStore.streamSize(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void invalidStreamIdWithBadUUIDWillNotWork() {
      String streamId = "ClassifiedAd:hello-world";
      Mono<Long> load = eventStore.streamSize(streamId);
      StepVerifier.create(load)
          .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
          .verify();
    }

    @Test
    void canRetrieveStreamSizeBasedOnStreamId() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.countEvents(aggregateId)).thenReturn(Mono.just(3L));

      Mono<Long> eventStreamMono = eventStore.streamSize(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(streamSize -> {
            assertThat(streamSize).isEqualTo(3L);
          })
          .verifyComplete();
    }

    @Test
    void nonExistentStreamShouldStillReturnANumber() {
      String validStreamId = "ClassifiedAd:%s".formatted(aggregateId);
      Mockito.when(eventStoreRepository.countEvents(aggregateId)).thenReturn(Mono.just(0L));

      Mono<Long> eventStreamMono = eventStore.streamSize(validStreamId);
      StepVerifier.create(eventStreamMono)
          .assertNext(streamSize -> {
            assertThat(streamSize).isEqualTo(0L);
          })
          .verifyComplete();
    }
  }
}
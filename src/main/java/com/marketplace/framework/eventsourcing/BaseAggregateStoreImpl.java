package com.marketplace.framework.eventsourcing;

import com.marketplace.event.VersionedEvent;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventStreamMetadata;
import com.marketplace.eventstore.framework.event.ImmutableEventStreamMetadata;
import com.marketplace.framework.AggregateRoot;
import io.vavr.control.Try;
import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@SuppressWarnings("unchecked")
public abstract class BaseAggregateStoreImpl implements AggregateStore {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseAggregateStoreImpl.class);
  public static final TypeCache typeCache = TypeCache.getInstance();

  public Mono<AggregateRoot<VersionedEvent>> generateAggregateRoot(List<Event> events, Constructor<?> constructor) {
    constructor.setAccessible(true);
    Try<Object> aggregateInstance = Try.of(constructor::newInstance);
    return aggregateInstance.map(instance -> {
      AggregateRoot<VersionedEvent> aggregateRoot = (AggregateRoot<VersionedEvent>) instance;
      events.stream().map(event -> (VersionedEvent) event).forEach(aggregateRoot::when);
      return Mono.just(aggregateRoot);
    }).getOrElseGet(Mono::error);
  }

  public Mono<? extends Constructor<?>> findDefaultConstructor(String aggregateType) {
    Optional<? extends Class<?>> aClass = typeCache.get(aggregateType);
    if (aClass.isEmpty()) {
      LOGGER.info("unable to retrieve class info for class '{}'", aggregateType);
      return Mono.error(new IllegalArgumentException("unable to find the class for type " + aggregateType));
    }
    Class<?> aggregateClass = aClass.get();
    Constructor<?>[] declaredConstructors = aggregateClass.getDeclaredConstructors();
    Optional<Constructor<?>> maybeDefaultConstructor = Arrays.stream(declaredConstructors)
        .filter(constructor -> constructor.getParameterCount() == 0)
        .findFirst();
//
    if (maybeDefaultConstructor.isEmpty()) {
      LOGGER.info("No default constructor found on class {}, please provide one ", aggregateType);
      return Mono.error(new IllegalArgumentException("AggregateRoot must have a default constructor"));
    }

    return maybeDefaultConstructor.map(Mono::just).orElse(Mono.error(new IllegalArgumentException(
        "constructor not found")));
  }

  public static EventStreamMetadata from(AggregateRoot<VersionedEvent> aggregateRoot) {
    return ImmutableEventStreamMetadata.builder()
        .createdAt(Instant.now())
        .streamId(getStreamId(aggregateRoot))
        .aggregateId(aggregateRoot.getAggregateId())
        .aggregateType(aggregateRoot.getClass().getCanonicalName())
        .build();
  }

  protected static String getStreamId(AggregateRoot<VersionedEvent> aggregateRoot) {
    return String.format("%s:%s", aggregateRoot.getClass().getSimpleName(), aggregateRoot.getAggregateId().toString());
  }
}

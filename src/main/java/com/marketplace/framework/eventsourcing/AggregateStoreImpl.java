package com.marketplace.framework.eventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.event.VersionedEvent;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.OperationResult.Success;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.framework.AggregateRoot;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import reactor.core.publisher.Mono;

public class AggregateStoreImpl implements AggregateStore<AggregateRoot<VersionedEvent>, UUID> {

  private final ObjectMapper objectMapper;
  private final EventStore<VersionedEvent> eventStore;

  @Inject
  public AggregateStoreImpl(ObjectMapper objectMapper, EventStore<VersionedEvent> eventStore) {
    this.objectMapper = objectMapper;
    this.eventStore = eventStore;
  }

  @Override
  public Mono<Optional<AggregateRoot<VersionedEvent>>> save(AggregateRoot<VersionedEvent> aggregate) {
    String streamId = getStreamId(aggregate);
    List<VersionedEvent> events = aggregate.getChanges();
    Mono<OperationResult> append = eventStore.append(streamId, aggregate.getVersion(), events);
    return append.map(res -> {
      if (res instanceof Success) {
        return Optional.of(aggregate);
      }
      return Optional.<AggregateRoot<VersionedEvent>>empty();
    }).switchIfEmpty(Mono.just(Optional.empty()));
  }

  private String getStreamId(String aggregateName, UUID aggregateId) {
    return String.format("%s:%s", aggregateName, aggregateId.toString());
  }

  private String getStreamId(AggregateRoot<VersionedEvent> aggregate) {
    return getStreamId(aggregate.getClass().getSimpleName(), aggregate.getAggregateId());
  }

  @Override
  public Mono<Optional<AggregateRoot<VersionedEvent>>> load(String aggregateName, UUID aggregateId) {
//    Mono<EventStream<VersionedEvent>> load = eventStore.load(getStreamId(aggregateName, aggregateId));
//    load.map(EventStream::getEvents)
//        .map(events -> {
//          String eventAggregateName = events.get(0).getAggregateName();
//          Class<?> aggregateClass = getClassFromName(eventAggregateName);
//          Constructor<?>[] declaredConstructors = aggregateClass.getDeclaredConstructors();
//
//          try {
//            Method apply = aggregateClass.getDeclaredMethod("apply");
//
//          } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//          }
          /*
           Class<?> clazz = MyClass.class;
        Constructor<?> ctor = clazz.getConstructor(List.class);

        ctor.newInstance(new Object[] { null });
           */

          /*
          Constructor<MyClass> constructor = MyClass.class.getConstructor(List.class);
or

Constructor constructor = MyClass.class.getConstructor(new Class[]{List.class});
           */
//          return null;
//        });
    return Mono.empty();
  }

  final static Map<String, Class<?>> classCache = new ConcurrentHashMap<>();

  private Class<?> getClassFromName(String type) {
    return classCache.computeIfAbsent(type, typeName -> {
      try {
        return Class.forName(typeName);
      } catch (ClassNotFoundException ignored) {
        return null;
      }
    });
  }
}

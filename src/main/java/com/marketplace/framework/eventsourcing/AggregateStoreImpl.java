package com.marketplace.framework.eventsourcing;

import com.marketplace.event.VersionedEvent;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStreamMetadata;
import com.marketplace.framework.AggregateRoot;
import java.lang.reflect.Constructor;
import java.util.UUID;
import javax.inject.Inject;
import reactor.core.publisher.Mono;

public class AggregateStoreImpl extends BaseAggregateStoreImpl {

  private final EventStore<VersionedEvent> eventStore;
  private final EventStreamMetadataRepository eventStreamMetadataRepository;

  @Inject
  public AggregateStoreImpl(
      EventStreamMetadataRepository eventStreamMetadataRepository,
      EventStore<VersionedEvent> eventStore) {
    this.eventStreamMetadataRepository = eventStreamMetadataRepository;
    this.eventStore = eventStore;
  }

  @Override
  public Mono<OperationResult> save(AggregateRoot<VersionedEvent> aggregate) {
    EventStreamMetadata eventStreamMetadata = from(aggregate);
    Mono<WriteResult> writeResult = eventStreamMetadataRepository.save(eventStreamMetadata);
    return writeResult.flatMap(result -> {
      String streamId = getStreamId(aggregate);
      return eventStore.append(streamId, aggregate.getVersion(), aggregate.getChanges());
    }).doOnNext(res -> {
      aggregate.clearChanges();
    });
  }

  @Override
  public Mono<AggregateRoot<VersionedEvent>> load(UUID aggregateId) {
    Mono<EventStreamMetadata> mayBeEventStream = eventStreamMetadataRepository.find(aggregateId);
    return mayBeEventStream.flatMap(eventStreamMetadata -> eventStore.load(eventStreamMetadata.getStreamId())
        .flatMap(eventStream -> {
          Mono<? extends Constructor<?>> defaultConstructor = findDefaultConstructor(eventStreamMetadata.getAggregateType());
          return defaultConstructor.flatMap(constructor -> generateAggregateRoot(eventStream.getEvents(), constructor));
        }));
  }

  @Override
  public Mono<Long> size() {
    return Mono.just(0L);
  }

  @Override
  public Mono<Long> countEvents(UUID aggregateId) {
    Mono<EventStreamMetadata> mayBeEventStream = eventStreamMetadataRepository.find(aggregateId);
    return mayBeEventStream.flatMap(eventStreamMetadata -> eventStore.streamSize(eventStreamMetadata.getStreamId()));
  }

//
//  @Override
//  public Mono<Optional<AggregateRoot<VersionedEvent>>> load(String aggregateName, UUID aggregateId) {
////    Mono<EventStream<VersionedEvent>> load = eventStore.load(getStreamId(aggregateName, aggregateId));
////    load.map(EventStream::getEvents)
////        .map(events -> {
////          String eventAggregateName = events.get(0).getAggregateName();
////          Class<?> aggregateClass = getClassFromName(eventAggregateName);
////          Constructor<?>[] declaredConstructors = aggregateClass.getDeclaredConstructors();
////
////          try {
////            Method apply = aggregateClass.getDeclaredMethod("apply");
////
////          } catch (NoSuchMethodException e) {
////            e.printStackTrace();
////          }
//          /*
//           Class<?> clazz = MyClass.class;
//        Constructor<?> ctor = clazz.getConstructor(List.class);
//
//        ctor.newInstance(new Object[] { null });
//           */
//
//          /*
//          Constructor<MyClass> constructor = MyClass.class.getConstructor(List.class);
//or
//
//Constructor constructor = MyClass.class.getConstructor(new Class[]{List.class});
//           */
////          return null;
////        });
//    return Mono.empty();
//  }
//
//  final static Map<String, Class<?>> classCache = new ConcurrentHashMap<>();
//
//  private Class<?> getClassFromName(String type) {
//    return classCache.computeIfAbsent(type, typeName -> {
//      try {
//        return Class.forName(typeName);
//      } catch (ClassNotFoundException ignored) {
//        return null;
//      }
//    });
//  }
}

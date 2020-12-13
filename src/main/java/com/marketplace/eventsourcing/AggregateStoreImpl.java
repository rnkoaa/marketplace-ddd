package com.marketplace.eventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.event.VersionedEvent;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.framework.AggregateRoot;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  public Mono<AggregateRoot<VersionedEvent>> save(AggregateRoot<VersionedEvent> aggregate) {
    String streamId = getStreamId(aggregate);
    List<VersionedEvent> events = aggregate.getChanges();
    eventStore.append(streamId, aggregate.getVersion(), events);
    return null;
  }

  private String getStreamId(AggregateRoot<VersionedEvent> aggregate) {
    UUID aggregateId = aggregate.getAggregateId();
    String simpleName = aggregate.getClass().getSimpleName();
    return String.format("%s:%s", simpleName, aggregateId.toString());
  }

  @Override
  public Mono<Optional<AggregateRoot<VersionedEvent>>> load(UUID aggregateId) {
    return null;
  }
}

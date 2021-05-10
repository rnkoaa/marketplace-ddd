package com.marketplace.framework.eventsourcing;

import com.marketplace.event.VersionedEvent;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.framework.AggregateRoot;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface AggregateStore {

  Mono<OperationResult> save(AggregateRoot<VersionedEvent> aggregate);

  Mono<AggregateRoot<VersionedEvent>> load(UUID aggregateId);

  Mono<Long> size();

  Mono<Long> countEvents(UUID aggregateId);

}

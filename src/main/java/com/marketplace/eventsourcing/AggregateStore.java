package com.marketplace.eventsourcing;

import com.marketplace.event.Event;
import com.marketplace.event.VersionedEvent;
import com.marketplace.framework.AggregateRoot;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface AggregateStore<T extends AggregateRoot<VersionedEvent>, U> {

  Mono<T> save(T aggregate);

  Mono<Optional<T>> load(U aggregateId);

}

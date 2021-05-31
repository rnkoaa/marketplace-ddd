package com.marketplace.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import com.marketplace.cqrs.event.Event;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStoreRepository;
import com.marketplace.eventstore.impl.InMemoryEventPublisher;
import com.marketplace.eventstore.jdbc.JdbcEventStoreImpl;
import com.marketplace.eventstore.jdbc.JdbcEventStoreRepository;
import com.marketplace.eventstore.jdbc.JdbcEventStoreRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Module
public abstract class EventStoreModule {

    @Provides
    @Singleton
    @SuppressWarnings("UnstableApiUsage")
    public static EventBus provideEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    @SuppressWarnings("UnstableApiUsage")
    public static EventPublisher<Event> provideEventPublisher(EventBus eventBus) {
        return new InMemoryEventPublisher(eventBus);
    }

    @Provides
    @Singleton
    public static JdbcEventStoreRepository eventStoreRepository(
        ObjectMapper objectMapper,
        DSLContext dslContext
    ) {
        return new JdbcEventStoreRepositoryImpl(objectMapper, dslContext);
    }

    @Provides
    @Singleton
    public static EventStore<VersionedEvent> provideVersionedEvent(
        EventPublisher<Event> eventPublisher,
        JdbcEventStoreRepository eventStoreRepository
    ) {
        return new JdbcEventStoreImpl(eventPublisher, eventStoreRepository);
    }
}

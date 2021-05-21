package com.marketplace.context;

import com.google.common.eventbus.EventBus;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.impl.InMemoryEventPublisher;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

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
}

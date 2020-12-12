package com.marketplace.context;

import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventPublisher;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

@Module
public abstract class GuavaEventPublisherModule {

  @Binds
  @Singleton
  abstract EventPublisher<Event> provideEventPublisher(GuavaEventPublisher eventPublisher);
}

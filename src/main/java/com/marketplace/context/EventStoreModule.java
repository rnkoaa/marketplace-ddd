package com.marketplace.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.config.MongoConfig;
import com.marketplace.context.mongo.MongoConfigModule;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventStoreImpl;
import com.marketplace.eventstore.mongodb.MongoEventStoreRepository;
import com.marketplace.eventstore.mongodb.MongoEventStoreRepositoryImpl;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

//@Module(includes = {
//    MongoConfigModule.class,
//    ObjectMapperModule.class,
//    GuavaEventPublisherModule.class
//})
public abstract class EventStoreModule {

//  @Provides
//  @Singleton
  public static MongoEventStoreRepository provideEventStoreRepository(
      ObjectMapper objectMapper,
      MongoConfig mongoConfig,
      MongoClient mongoClient) {
    MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
    MongoCollection<MongoEventEntity> eventstore = database.getCollection(mongoConfig.getEventStore(), MongoEventEntity.class);
    return new MongoEventStoreRepositoryImpl(objectMapper, eventstore);
  }

//  @Provides
//  @Singleton
  public static EventStore<Event> eventStore(MongoEventStoreRepository eventStoreRepository, EventPublisher<Event> eventPublisher) {
    return new MongoEventStoreImpl(eventStoreRepository, eventPublisher);
  }
}

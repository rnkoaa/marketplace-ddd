package com.marketplace.context;

import dagger.Module;

@Module
//@Module(includes = {
//    MongoConfigModule.class,
//    ObjectMapperModule.class,
//    GuavaEventPublisherModule.class
//})
public abstract class EventStoreModule {

//  @Provides
//  @Singleton
//  public static MongoEventStoreRepository provideEventStoreRepository(
//      ObjectMapper objectMapper,
//      MongoConfig mongoConfig,
//      MongoClient mongoClient) {
//    MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
//    MongoCollection<MongoEventEntity> eventstore = database.getCollection(mongoConfig.getEventStore(), MongoEventEntity.class);
//    return new MongoEventStoreRepositoryImpl(objectMapper, eventstore);
//  }
//
//  @Provides
//  @Singleton
//  public static EventStore<Event> eventStore(MongoEventStoreRepository eventStoreRepository, EventPublisher<Event> eventPublisher) {
//    return new MongoEventStoreImpl(eventStoreRepository, eventPublisher);
//  }
}

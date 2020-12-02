package com.marketplace.eventstore.mongodb.application;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.common.config.MongoConfig;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.marketplace.eventstore.mongodb.ImmutableMongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntityCodecProvider;
import com.marketplace.eventstore.mongodb.MongoEventEntitySerDe;
import com.marketplace.eventstore.mongodb.application.event.TestEvent;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.AggregatePublisher;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import java.util.List;
import java.util.UUID;
import javax.print.Doc;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@SuppressWarnings("UnstableApiUsage")
public class EventStoreApplication {

  private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

  public static void main(String[] args) throws InterruptedException {
    //
    MongoConfig mongoConfig = new MongoConfig("localhost", "eventstore", 27017);
    MongoClient mongoClient = createClient(mongoConfig, provideCodecRegistry());
    MongoDatabase db =
        mongoClient
            .getDatabase(mongoConfig.getDatabase())
            .withCodecRegistry(provideCodecRegistry());

    MongoCollection<MongoEventEntity> eventCollection = db.getCollection("event", MongoEventEntity.class);
//    MongoCollection<Document> eventCollection = db.getCollection("event");
    //
    //    Backend backend = new MongoBackend(MongoSetup.of(db));
    //    MongoEventEntityRepository eventEntityRepository = new
    // MongoEventEntityRepository(backend);
    //
    UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
    UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
    MongoEventEntitySerDe mongoEventEntitySerDe = new MongoEventEntitySerDe();
    var testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event");
    List<TypedEvent> typedEvent =
        List.of(
            ImmutableTypedEvent.builder()
                .type(TestEvent.class.getCanonicalName())
                .eventBody(serialize(testEvent))
                .sequenceId(0)
                .build());
    MongoEventEntity eventEntity =
        ImmutableMongoEventEntity.builder()
            .events(typedEvent)
            .streamName("ClassifiedAd:" + aggregateId)
            .aggregateId(testEvent.getAggregateId())
            .id(testEvent.getId())
            .createdAt(testEvent.createdAt())
            .version(0)
            .build();

//        Publisher<Success> successPublisher = eventCollection.insertOne(eventEntity);
//        Success block = Mono.from(successPublisher).block();
//        System.out.println(block);
//
//    FindPublisher<Document> findPublisher =
//        eventCollection.find(eq("_id", UUID.fromString("9be7ed23-8910-4e18-a775-704599c8b454")));
//    MongoEventEntity mongoEventEntity =
//        Mono.from(findPublisher)
//            .map(mongoEventEntitySerDe::decode)
//            //            .switchIfEmpty(Mono.just(ImmutableMongoEventEntity.builder().build()))
//            .block();

    FindPublisher<MongoEventEntity> findPublisher =
        eventCollection.find(eq("_id", UUID.fromString("9be7ed23-8910-4e18-a775-704599c8b454")));
    MongoEventEntity mongoEventEntity =
        Mono.from(findPublisher)
//            .map(mongoEventEntitySerDe::decode)
            //            .switchIfEmpty(Mono.just(ImmutableMongoEventEntity.builder().build()))
            .block();

    System.out.println("Found Results");
    System.out.println(mongoEventEntity);

    //
    //    Integer block = Mono.from(integerPublisher)
    //        .switchIfEmpty(Mono.just(0))
    //        .block();
    //    System.out.println(block);
    //    AggregatePublisher<Document> aggregatePublisher = findLatestVersion(
    //        db, aggregateId);

    //    AggregatePublisher<Document> aggregatePublisher = eventCollection.aggregate(List.of(
    //        match(
    //            eq("streamName", "ClassifiedAd:246219a4-4266-440b-964e-f292baadf133")
    ////            eq("aggregateId", aggregateId)
    //        ),
    //        group(null, Accumulators.max("version", "$version"))
    //    ), Document.class);
    //
    //    Document aggregateDocument =
    //        Mono.from(aggregatePublisher).switchIfEmpty(Mono.just(new Document())).block();
    //
    //    System.out.println(aggregateDocument);
    //    event.find()
    /*  var testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event");
    MongoEventEntity eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(testEvent.getId())
            .createdAt(testEvent.createdAt())
            .version(0)
            .build();

    //    MongoCollection<MongoEventEntity> collection = db.getCollection("test",
    // MongoEventEntity.class);
    //    Publisher<Success> insertResult = collection.insertOne(eventEntity);
    Publisher<WriteResult> insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();

    testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event 1");
    eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(UUID.randomUUID())
            .createdAt(testEvent.createdAt())
            .version(1)
            .build();
    insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();

    testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event 2");
    eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(UUID.randomUUID())
            .createdAt(testEvent.createdAt())
            .version(2)
            .build();
    insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();*/

    // db.mongoEventEntity.aggregate([{
    //  $match: { aggregateId: UUID('246219a4-4266-440b-964e-f292baadf133')}
    // },{
    //  $group: {
    //  _id: null,
    //  version: {
    //    $max: "$version"
    //  }
    // }}])

    //
    //    Mono.from(insertResult).block();
    //    insertResult.subscribe(
    //        new Subscriber<>() {
    //          @Override
    //          public void onSubscribe(Subscription s) {
    //            s.request(1);
    //          }
    //
    //          @Override
    //          public void onNext(WriteResult writeResult) {
    //            System.out.println(writeResult);
    //          }
    //
    //          @Override
    //          public void onError(Throwable t) {
    //            System.out.println(t.getMessage());
    //          }
    //
    //          @Override
    //          public void onComplete() {
    //            System.out.println("complete");
    //          }
    //        });
    //
    //    TimeUnit.SECONDS.sleep(2);
  }

  private static AggregatePublisher<Document> findLatestVersion(
      MongoDatabase db, UUID aggregateId) {
    MongoCollection<Document> eventCollection = db.getCollection("event");

    AggregatePublisher<Document> aggregatePublisher =
        eventCollection.aggregate(
            List.of(
                match(
                    //            eq("streamName",
                    // "ClassifiedAd:246219a4-4266-440b-964e-f292baadf133")
                    //            eq("_id", UUID.fromString("09398a4a-59ef-4fd7-90cf-15a861b1903c"))
                    eq("aggregateId", aggregateId)
                    //            eq("aggregateId", Filters"246219a4-4266-440b-964e-f292baadf133")
                    ),
                group(null, Accumulators.max("version", "$version"))),
            Document.class);
    return aggregatePublisher;
  }

  static MongoClient createClient(MongoConfig config, CodecRegistry codecRegistry) {
    var connectionString = new ConnectionString(config.getConnectionString());
    MongoClientSettings settings =
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .build();
    return MongoClients.create(settings);
  }

  public static CodecRegistry provideCodecRegistry() {

    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
//        fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
        fromProviders(new MongoEventEntityCodecProvider()));
  }

  private static String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}

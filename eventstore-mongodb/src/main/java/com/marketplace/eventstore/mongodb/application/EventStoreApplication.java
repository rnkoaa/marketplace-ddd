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
    MongoConfig mongoConfig = new MongoConfig("localhost", "marketplace_ddd", "eventstore", 27017);
    MongoClient mongoClient = createClient(mongoConfig, provideCodecRegistry());
    MongoDatabase db =
        mongoClient
            .getDatabase(mongoConfig.getDatabase())
            .withCodecRegistry(provideCodecRegistry());

    MongoCollection<MongoEventEntity> eventCollection =
        db.getCollection("event", MongoEventEntity.class);

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
            .createdAt(testEvent.getCreatedAt())
            .version(0)
            .build();

    /*Publisher<Success> successPublisher = eventCollection.insertOne(eventEntity);
    Success block = Mono.from(successPublisher).block();
    System.out.println(block);

    FindPublisher<MongoEventEntity> findPublisher =
        eventCollection.find(eq("_id", UUID.fromString("d2fbeb6f-8cf3-4f93-b24d-4786fcd9717c")));
    MongoEventEntity mongoEventEntity =
        Mono.from(findPublisher)
            //            .switchIfEmpty(Mono.just(ImmutableMongoEventEntity.builder().build()))
            .block();

    System.out.println("Found Results");
    System.out.println(mongoEventEntity);*/

    UUID bad = UUID.fromString("d2fbeb6f-8cf3-4f93-b24d-4786fcd9717c");

    AggregatePublisher<Document> latestVersion = findLatestVersion(db, bad);
    Integer version =
        Mono.from(latestVersion)
            .map(versionDocument -> versionDocument.getInteger("version"))
            .switchIfEmpty(Mono.just(0))
            .block();

    System.out.println("Version: " + version);
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

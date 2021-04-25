package com.marketplace.eventstore.mongodb;

import com.google.common.base.Strings;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.marketplace.cqrs.event.ImmutableTypedEvent;
import com.marketplace.cqrs.event.TypedEvent;
import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoEventEntityCodec implements CollectibleCodec<ImmutableMongoEventEntity> {

  public static final String AGGREGATE_ID = "aggregateId";
  public static final String VERSION = "version";
  public static final String STREAM_NAME = "streamName";
  public static final String CREATED_AT = "createdAt";
  public static final String EVENTS = "events";
  public static final String ID = "_id";
  private final CodecRegistry codecRegistry;

  public MongoEventEntityCodec(CodecRegistry codecRegistry) {
    this.codecRegistry = codecRegistry;
  }

  @Override
  public ImmutableMongoEventEntity decode(BsonReader reader, DecoderContext decoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = documentCodec.decode(reader, decoderContext);
    String streamName = document.getString(STREAM_NAME);
    List<Document> events = document.getList(EVENTS, Document.class);
    List<TypedEvent> typedEvents =
        events.stream().map(this::decodeTypeEvent).collect(Collectors.toList());

    Date createdAt = document.get(CREATED_AT, Date.class);

    return ImmutableMongoEventEntity.builder()
        .id(document.get(ID, UUID.class))
        .aggregateId(document.get(AGGREGATE_ID, UUID.class))
        .streamName((!Strings.isNullOrEmpty(streamName)) ? streamName : "")
        .version(document.getLong(VERSION))
        .events(typedEvents)
        .createdAt((createdAt != null) ? createdAt.toInstant() : Instant.now())
        .build();
  }

  @Override
  public void encode(
      BsonWriter writer, ImmutableMongoEventEntity value, EncoderContext encoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = new Document();
    document.put(ID, value.getId());
    document.put(AGGREGATE_ID, value.getAggregateId());
    document.put(VERSION, value.getVersion());
    document.put(STREAM_NAME, value.getStreamName());
    document.put(CREATED_AT, value.getCreatedAt());
    document.put(EVENTS, value.getEvents());
    documentCodec.encode(writer, document, encoderContext);
  }

  @Override
  public Class<ImmutableMongoEventEntity> getEncoderClass() {
    return ImmutableMongoEventEntity.class;
  }

  private ImmutableTypedEvent decodeTypeEvent(Document doc) {
    String eventBody = doc.getString("eventBody");
    String type = doc.getString("eventType");
    int sequenceId = doc.getInteger("sequenceId", 0);
    return ImmutableTypedEvent.builder()
        .eventBody(eventBody)
        .type(type)
        .sequenceId(sequenceId)
        .build();
  }

  @Override
  public ImmutableMongoEventEntity generateIdIfAbsentFromDocument(
      ImmutableMongoEventEntity document) {
    if (document.getId() == null) {
      document = document.withId(UUID.randomUUID());
    }
    return document;
  }

  @Override
  public boolean documentHasId(ImmutableMongoEventEntity document) {
    return document.getId() != null;
  }

  @Override
  public BsonValue getDocumentId(ImmutableMongoEventEntity document) {
    return new BsonBinary(document.getId());
  }
}

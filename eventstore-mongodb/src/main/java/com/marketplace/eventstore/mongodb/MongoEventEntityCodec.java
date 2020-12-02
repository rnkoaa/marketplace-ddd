package com.marketplace.eventstore.mongodb;

import com.google.common.base.Strings;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoEventEntityCodec implements Codec<ImmutableMongoEventEntity> {
  private final CodecRegistry codecRegistry;

  public MongoEventEntityCodec(CodecRegistry codecRegistry) {
    this.codecRegistry = codecRegistry;
  }

  @Override
  public ImmutableMongoEventEntity decode(BsonReader reader, DecoderContext decoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = documentCodec.decode(reader, decoderContext);
    UUID id = document.get("_id", UUID.class);
    UUID aggregateId = document.get("aggregateId", UUID.class);
    String streamName = document.getString("streamName");
    int version = document.getInteger("version", 0);
    //    List<ImmutableTypedEvent> events = document.getList("events", ImmutableTypedEvent.class);
    List<Document> events = document.getList("events", Document.class);
    List<TypedEvent> typedEvents =
        events.stream().map(this::decodeTypeEvent).collect(Collectors.toList());

    Date createdAt = document.get("createdAt", Date.class);

    return ImmutableMongoEventEntity.builder()
        .id(id)
        .aggregateId(aggregateId)
        .streamName((!Strings.isNullOrEmpty(streamName)) ? streamName : "")
        .version(version)
        //        .events(List.of())
        .events(typedEvents)
        .createdAt(createdAt.toInstant())
        .build();
  }

  @Override
  public void encode(
      BsonWriter writer, ImmutableMongoEventEntity value, EncoderContext encoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = new Document();
    document.put("_id", value.getId());
    document.put("aggregateId", value.getAggregateId());
    document.put("version", value.getVersion());
    document.put("streamName", value.getStreamName());
    document.put("createdAt", value.getCreatedAt());
    document.put("events", value.getEvents());
    documentCodec.encode(writer, document, encoderContext);
  }

  @Override
  public Class<ImmutableMongoEventEntity> getEncoderClass() {
    return ImmutableMongoEventEntity.class;
  }
  /*
   private final PriceConverter priceConverter;
  private final CodecRegistry codecRegistry;

  public PriceCodec(CodecRegistry codecRegistry, PriceConverter priceConverter) {
      this.priceConverter = priceConverter;
      this.codecRegistry = codecRegistry;
  }

  @Override
  public Price decode(BsonReader reader, DecoderContext decoderContext) {
      Codec<Document> documentCodec = codecRegistry.get(Document.class);
      Document document = documentCodec.decode(reader, decoderContext);
      return priceConverter.deserialize(document);
  }

  @Override
  public void encode(BsonWriter writer, Price value, EncoderContext encoderContext) {
      Codec<Document> documentCodec = codecRegistry.get(Document.class);
      Document document = priceConverter.serialize(value);
      documentCodec.encode(writer, document, encoderContext);
  }
   */
  private ImmutableTypedEvent decodeTypeEvent(Document doc) {
    String eventBody = doc.getString("eventBody");
    String type = doc.getString("type");
    int sequenceId = doc.getInteger("sequenceId", 0);
    return ImmutableTypedEvent.builder()
        .eventBody(eventBody)
        .type(type)
        .sequenceId(sequenceId)
        .build();
  }
}

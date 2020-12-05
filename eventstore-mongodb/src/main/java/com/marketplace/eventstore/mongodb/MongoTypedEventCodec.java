package com.marketplace.eventstore.mongodb;

import com.google.common.base.Strings;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoTypedEventCodec implements Codec<ImmutableTypedEvent> {

  private final CodecRegistry codecRegistry;

  public MongoTypedEventCodec(CodecRegistry codecRegistry) {
    this.codecRegistry = codecRegistry;
  }

  @Override
  public ImmutableTypedEvent decode(BsonReader reader, DecoderContext decoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = documentCodec.decode(reader, decoderContext);
    String eventBody = document.getString("eventBody");
    String eventType = document.getString("eventType");
    int sequenceId = document.getInteger("sequenceId", 0);

    return ImmutableTypedEvent.builder()
        .sequenceId(sequenceId)
        .eventBody(eventBody)
        .type(eventType)
        .build();
  }

  @Override
  public void encode(BsonWriter writer, ImmutableTypedEvent value, EncoderContext encoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = new Document();
    document.put("sequenceId", value.getSequenceId());
    document.put("eventBody", value.getEventBody());
    document.put("eventType", value.getType());
    documentCodec.encode(writer, document, encoderContext);
  }

  @Override
  public Class<ImmutableTypedEvent> getEncoderClass() {
    return ImmutableTypedEvent.class;
  }
}

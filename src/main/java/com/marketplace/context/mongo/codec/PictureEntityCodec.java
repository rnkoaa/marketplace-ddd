package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.entity.ImmutablePictureEntity;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import java.util.UUID;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class PictureEntityCodec implements Codec<ImmutablePictureEntity> {

  private final CodecRegistry codecRegistry;

  public PictureEntityCodec(CodecRegistry codecRegistry) {
    this.codecRegistry = codecRegistry;
  }


  @Override
  public ImmutablePictureEntity decode(BsonReader reader, DecoderContext decoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = documentCodec.decode(reader, decoderContext);
    UUID id = document.get("_id", UUID.class);
    UUID parentId = document.get("parentId", UUID.class);
    int height = document.getInteger("height");
    int width = document.getInteger("width");
    String uri = document.getString("uri");
    int order = document.getInteger("order", 0);

    return ImmutablePictureEntity.builder()
        .id(id)
        .parentId(parentId)
        .height(height)
        .width(width)
        .uri(uri)
        .order(order)
        .build();
  }

  @Override
  public void encode(BsonWriter writer, ImmutablePictureEntity value, EncoderContext encoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = new Document();
    document.put("_id", value.getId());
    document.put("parentId", value.getParentId());
    document.put("height", value.getHeight());
    document.put("width", value.getWidth());
    document.put("uri", value.getUri());
    document.put("order", value.getOrder());
    documentCodec.encode(writer, document, encoderContext);
  }

  @Override
  public Class<ImmutablePictureEntity> getEncoderClass() {
    return ImmutablePictureEntity.class;
  }

  /**
   *  Codec<Document> documentCodec = codecRegistry.get(Document.class);
   *     Document document = documentCodec.decode(reader, decoderContext);
   *     String eventBody = document.getString("eventBody");
   *     String eventType = document.getString("eventType");
   *     int sequenceId = document.getInteger("sequenceId", 0);
   *
   *     return ImmutableTypedEvent.builder()
   *         .sequenceId(sequenceId)
   *         .eventBody(eventBody)
   *         .type(eventType)
   *         .build();
   *   }
   *
   *   @Override
   *   public void encode(BsonWriter writer, ImmutableTypedEvent value, EncoderContext encoderContext) {
   *     Codec<Document> documentCodec = codecRegistry.get(Document.class);
   *     Document document = new Document();
   *     document.put("sequenceId", value.getSequenceId());
   *     document.put("eventBody", value.getEventBody());
   *     document.put("eventType", value.getType());
   *     documentCodec.encode(writer, document, encoderContext);
   *   }
   */
}

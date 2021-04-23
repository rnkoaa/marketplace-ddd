package com.marketplace.eventstore.mongodb;

import com.marketplace.cqrs.event.ImmutableTypedEvent;
import com.marketplace.cqrs.event.TypedEvent;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoEventEntityCodecProvider implements CodecProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
    if (clazz == ImmutableMongoEventEntity.class || clazz == MongoEventEntity.class) {
      return (Codec<T>) new MongoEventEntityCodec(registry);
    } else if (clazz == ImmutableTypedEvent.class || clazz == TypedEvent.class) {
      return (Codec<T>) new MongoTypedEventCodec(registry);
    }
    return null;
  }
}

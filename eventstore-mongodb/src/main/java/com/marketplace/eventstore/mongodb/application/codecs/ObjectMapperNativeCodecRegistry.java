package com.marketplace.eventstore.mongodb.application.codecs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.immutables.criteria.mongo.bson4jackson.Wrapper;

public class ObjectMapperNativeCodecRegistry implements CodecRegistry {

  private final ObjectMapper mapper;

  public ObjectMapperNativeCodecRegistry(ObjectMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper, "mapper");
  }

  @Override
  public <T> Codec<T> get(Class<T> clazz) {
    final JavaType javaType = mapper.getTypeFactory().constructType(clazz);

    try {
      JsonSerializer<?> ser = mapper.getSerializerProviderInstance().findValueSerializer(javaType);
      if (ser instanceof Wrapper) {
        @SuppressWarnings("unchecked")
        Codec<T> codec = ((Wrapper<Codec<T>>) ser).unwrap();
        return codec;
      }
    } catch (JsonMappingException e) {
      throw new CodecConfigurationException("Exception for " + javaType, e);
    }

    throw new CodecConfigurationException(javaType + " not supported");
  }
}

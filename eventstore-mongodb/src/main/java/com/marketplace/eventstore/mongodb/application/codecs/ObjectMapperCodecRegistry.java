package com.marketplace.eventstore.mongodb.application.codecs;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import org.bson.AbstractBsonReader;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;

public class ObjectMapperCodecRegistry implements CodecRegistry {

  private final ObjectMapper mapper;

  private ObjectMapperCodecRegistry(ObjectMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper, "mapper");
  }

  public static ObjectMapperCodecRegistry of(ObjectMapper mapper) {
    return new ObjectMapperCodecRegistry(mapper);
  }

  @Override
  public <T> Codec<T> get(Class<T> clazz) {
    final JavaType javaType = mapper.getTypeFactory().constructType(clazz);
    if (!(mapper.canSerialize(clazz) && mapper.canDeserialize(javaType))) {
      throw new CodecConfigurationException(
          String.format("%s (javaType: %s) not supported by Jackson Mapper", clazz, javaType));
    }

    return new ObjectMapperCodecRegistry.JacksonCodec<>(clazz, mapper);
  }

  private static class JacksonCodec<T> implements Codec<T> {

    private final Class<T> clazz;
    private final ObjectReader reader;
    private final ObjectWriter writer;
    private final IOContext ioContext;

    JacksonCodec(Class<T> clazz, ObjectMapper mapper) {
      this.clazz = Objects.requireNonNull(clazz, "clazz");
      Objects.requireNonNull(mapper, "mapper");
      this.reader = mapper.readerFor(clazz);
      this.writer = mapper.writerFor(clazz);
      this.ioContext = new IOContext(new BufferRecycler(), null, false);
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
      Preconditions.checkArgument(
          reader instanceof AbstractBsonReader,
          "Expected reader to be %s for %s but was %s",
          AbstractBsonReader.class.getName(),
          clazz,
          reader.getClass());
      final ObjectMapperBsonParser parser =
          new ObjectMapperBsonParser(ioContext, 0, (AbstractBsonReader) reader);
      try {
        return this.reader.readValue(parser);
      } catch (IOException e) {
        throw new UncheckedIOException("Error while decoding " + clazz, e);
      }
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
      final ObjectMapperBsonGenerator generator = new ObjectMapperBsonGenerator(0, writer);
      try {
        this.writer.writeValue(generator, value);
      } catch (IOException e) {
        throw new UncheckedIOException(
            "Couldn't serialize [" + value + "] as " + getEncoderClass(), e);
      }
    }

    @Override
    public Class<T> getEncoderClass() {
      return clazz;
    }
  }
}

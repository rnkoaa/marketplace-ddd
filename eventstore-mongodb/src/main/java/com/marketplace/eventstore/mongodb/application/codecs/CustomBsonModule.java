package com.marketplace.eventstore.mongodb.application.codecs;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import org.bson.UuidRepresentation;
import org.bson.codecs.BigDecimalCodec;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.ByteArrayCodec;
import org.bson.codecs.DateCodec;
import org.bson.codecs.Decimal128Codec;
import org.bson.codecs.ObjectIdCodec;
import org.bson.codecs.PatternCodec;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.immutables.criteria.mongo.bson4jackson.BsonModule;

public class CustomBsonModule extends Module {

  private final CodecRegistry registry;

  public CustomBsonModule() {
    this(defaultRegistry());
  }

  public CustomBsonModule(CodecRegistry registry) {
    this.registry = registry;
  }

  @Override
  public String getModuleName() {
    return BsonModule.class.getSimpleName();
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  private static CodecRegistry defaultRegistry() {
    CodecRegistry standard =
        CodecRegistries.fromProviders(
            new BsonValueCodecProvider(),
            new UuidCodecProvider(UuidRepresentation.STANDARD),
            new Jsr310CodecProvider());

    //    CodecRegistry

    // avoid codecs for String / Long / Boolean etc. They're already handled by jackson
    // choose the ones which need to be serialized in non-JSON format (BSON)
    CodecRegistry others =
        CodecRegistries.fromCodecs(
            new ObjectIdCodec(),
            new DateCodec(),
            new UuidCodec(),
            new Decimal128Codec(),
            new PatternCodec(),
            new BigDecimalCodec(),
            new ByteArrayCodec());

    return CodecRegistries.fromRegistries(standard, others);
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addSerializers(ObjectMapperCodecs.serializers(registry));
    context.addDeserializers(ObjectMapperCodecs.deserializers(registry));
  }
}

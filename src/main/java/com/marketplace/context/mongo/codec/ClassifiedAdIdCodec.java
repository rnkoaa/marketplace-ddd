package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.ClassifiedAdId;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ClassifiedAdIdCodec implements Codec<ClassifiedAdId> {
    @Override
    public ClassifiedAdId decode(BsonReader reader, DecoderContext decoderContext) {
        return ClassifiedAdId.fromString(reader.readString());
    }

    @Override
    public void encode(BsonWriter writer, ClassifiedAdId value, EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }

    @Override
    public Class<ClassifiedAdId> getEncoderClass() {
        return ClassifiedAdId.class;
    }
}

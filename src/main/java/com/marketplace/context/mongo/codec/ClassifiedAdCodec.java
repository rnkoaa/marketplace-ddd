package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class ClassifiedAdCodec implements Codec<ClassifiedAd> {
    private final CodecRegistry codecRegistry;

    public ClassifiedAdCodec(final CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ClassifiedAd decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        reader.readEndDocument();
        return null;
    }

    @Override
    public void encode(BsonWriter writer, ClassifiedAd value, EncoderContext encoderContext) {

    }

    @Override
    public Class<ClassifiedAd> getEncoderClass() {
        return ClassifiedAd.class;
    }
}

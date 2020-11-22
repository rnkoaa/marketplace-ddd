package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.ClassifiedAdTitle;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ClassifiedAdTitleCodec implements Codec<ClassifiedAdTitle> {
    @Override
    public ClassifiedAdTitle decode(BsonReader reader, DecoderContext decoderContext) {
        String s = reader.readString();
        return ClassifiedAdTitle.fromString(s);
    }

    @Override
    public void encode(BsonWriter writer, ClassifiedAdTitle value, EncoderContext encoderContext) {
        writer.writeString(value.value());
    }


    @Override
    public Class<ClassifiedAdTitle> getEncoderClass() {
        return ClassifiedAdTitle.class;
    }
}

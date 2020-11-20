package com.marketplace.context.mongo.codec;

import com.marketplace.domain.PictureId;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class PictureIdCodec implements Codec<PictureId> {
    public PictureIdCodec(CodecRegistry registry) {
    }

    @Override
    public PictureId decode(BsonReader reader, DecoderContext decoderContext) {
        String s = reader.readString();
        return PictureId.fromString(s);
    }

    @Override
    public void encode(BsonWriter writer, PictureId value, EncoderContext encoderContext) {
        writer.writeString(value.id().toString());
    }



    @Override
    public Class<PictureId> getEncoderClass() {
        return PictureId.class;
    }
}

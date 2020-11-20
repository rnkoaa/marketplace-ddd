package com.marketplace.context.mongo.codec;

import com.marketplace.domain.shared.UserId;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class UserIdCodec implements Codec<UserId> {
    public UserIdCodec(CodecRegistry registry) {
    }

    @Override
    public UserId decode(BsonReader reader, DecoderContext decoderContext) {
        String s = reader.readString();
        return UserId.fromString(s);
    }

    @Override
    public void encode(BsonWriter writer, UserId value, EncoderContext encoderContext) {
        writer.writeString(value.id().toString());
    }


    @Override
    public Class<UserId> getEncoderClass() {
        return UserId.class;
    }
}

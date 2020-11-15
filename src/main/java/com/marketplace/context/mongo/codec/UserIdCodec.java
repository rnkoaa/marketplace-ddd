package com.marketplace.context.mongo.codec;

import com.marketplace.domain.shared.UserId;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class UserIdCodec implements Codec<UserId> {
    @Override
    public UserId decode(BsonReader reader, DecoderContext decoderContext) {
        return UserId.fromString(reader.readString());
    }

    @Override
    public void encode(BsonWriter writer, UserId value, EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }

    @Override
    public Class<UserId> getEncoderClass() {
        return UserId.class;
    }
}

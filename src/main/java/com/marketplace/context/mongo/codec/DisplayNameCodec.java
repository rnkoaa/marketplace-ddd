package com.marketplace.context.mongo.codec;

import com.marketplace.domain.userprofile.DisplayName;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class DisplayNameCodec implements Codec<DisplayName> {
    @Override
    public DisplayName decode(BsonReader reader, DecoderContext decoderContext) {
        return new DisplayName(reader.readString());
    }

    @Override
    public void encode(BsonWriter writer, DisplayName displayName, EncoderContext encoderContext) {
        writer.writeString(displayName.value());

    }

    @Override
    public Class<DisplayName> getEncoderClass() {
        return DisplayName.class;
    }
}

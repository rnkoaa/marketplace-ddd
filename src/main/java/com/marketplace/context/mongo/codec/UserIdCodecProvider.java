package com.marketplace.context.mongo.codec;

import com.marketplace.domain.shared.UserId;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class UserIdCodecProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == UserId.class) {
            return (Codec<T>) new UserIdCodec();
        }

        return null;
    }
}

package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.ClassifiedAdId;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class ClassifiedAdIdCodecProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == ClassifiedAdId.class) {
            return (Codec<T>) new ClassifiedAdIdCodec();
        }

        return null;
    }
}

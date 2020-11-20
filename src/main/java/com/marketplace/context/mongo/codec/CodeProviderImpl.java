package com.marketplace.context.mongo.codec;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.shared.UserId;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class CodeProviderImpl implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == ClassifiedAdId.class) {
            return (Codec<T>) new ClassifiedAdIdCodec(registry);
        } else if (clazz == UserId.class) {
            return (Codec<T>) new UserIdCodec(registry);
        } else if (clazz == PictureId.class) {
            return (Codec<T>) new PictureIdCodec(registry);
        } else if (clazz == ClassifiedAd.class) {
            return (Codec<T>) new ClassifiedAdCodec(registry);
        }
        return null;
    }
}

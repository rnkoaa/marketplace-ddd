package com.marketplace.context.mongo.codec;

import com.marketplace.domain.userprofile.DisplayName;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class DisplayNameCodecProviderImpl implements CodecProvider {
    @SuppressWarnings("unchecked")
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if(clazz == DisplayName.class){
            return (Codec<T>) new DisplayNameCodec();
        }
        return null;
    }
}

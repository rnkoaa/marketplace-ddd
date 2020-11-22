package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.Price;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class PriceCodecProviderImpl implements CodecProvider {
    private final PriceConverter priceConverter;

    public PriceCodecProviderImpl(PriceConverter priceConverter) {
        this.priceConverter = priceConverter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Price.class) {
            return (Codec<T>) new PriceCodec(registry, priceConverter);
        }
        return null;
    }
}

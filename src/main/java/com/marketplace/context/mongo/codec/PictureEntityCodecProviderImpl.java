package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.entity.ImmutablePictureEntity;
import com.marketplace.domain.classifiedad.entity.PictureEntity;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class PictureEntityCodecProviderImpl implements CodecProvider {

  @SuppressWarnings("unchecked")
  @Override
  public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
    if (clazz == ImmutablePictureEntity.class || clazz == PictureEntity.class) {
      return (Codec<T>) new PictureEntityCodec(registry);
    }
    return null;
  }
}

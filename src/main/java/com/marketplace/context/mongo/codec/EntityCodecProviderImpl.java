package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.entity.ImmutablePictureEntity;
import com.marketplace.domain.classifiedad.entity.PictureEntity;
import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class EntityCodecProviderImpl implements CodecProvider {

  @SuppressWarnings("unchecked")
  @Override
  public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
    if (clazz == ImmutablePictureEntity.class || clazz == PictureEntity.class) {
      return (Codec<T>) new PictureEntityCodec(registry);
    } else if (clazz == ImmutableUserProfileEntity.class || clazz == UserProfileEntity.class) {
      return (Codec<T>) new UserProfileEntityCodec(registry);
    }
    return null;
  }
}

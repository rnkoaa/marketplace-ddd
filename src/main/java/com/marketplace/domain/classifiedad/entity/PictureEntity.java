package com.marketplace.domain.classifiedad.entity;

import com.marketplace.domain.classifiedad.Picture;

import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
public interface PictureEntity {

  UUID getId();

  UUID getParentId();

  int getHeight();

  int getWidth();

  String getUri();

  int getOrder();

  public static PictureEntity from(Picture picture) {
    return ImmutablePictureEntity.builder()
        .height(picture.getSize().height())
        .width(picture.getSize().width())
        .id(picture.getId().value())
        .parentId(picture.getParentId().value())
        .uri(picture.getUri())
        .order(picture.getOrder())
        .build();
  }
}

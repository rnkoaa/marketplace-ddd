package com.marketplace.domain.classifiedad.entity;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.ClassifiedAdText;
import com.marketplace.domain.classifiedad.ClassifiedAdTitle;
import com.marketplace.domain.classifiedad.Price;
import com.marketplace.domain.classifiedad.command.ImmutablePictureDto;
import com.marketplace.domain.classifiedad.command.ImmutablePriceDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.domain.classifiedad.query.ImmutableClassifiedAdQueryEntity;
import com.marketplace.domain.shared.UserId;
import com.marketplace.framework.Strings;
import com.marketplace.mongo.entity.MongoEntity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClassifiedAdEntity implements MongoEntity {

  @Id
  private UUID id;
  private UUID owner;
  private UUID approvedBy;
  private String text;
  private String title;
  private List<PictureEntity> pictures;
  private Price price;
  private ClassifiedAdState state;

  // for mongo entity
  public ClassifiedAdEntity() {
  }

  public ClassifiedAdEntity(ClassifiedAd classifiedAd) {
    this.id = classifiedAd.getId().id();
    this.owner = classifiedAd.getOwnerId().id();
    this.price = classifiedAd.getPrice();
    this.state = classifiedAd.getState();
    if (classifiedAd.getApprovedBy() != null) {
      this.approvedBy = classifiedAd.getApprovedBy().id();
    }
    if (classifiedAd.getTitle() != null) {
      this.title = classifiedAd.getTitle().value();
    }
    if (classifiedAd.getText() != null) {
      this.text = classifiedAd.getText().value();
    }

    if (classifiedAd.getPictures().size() > 0) {
      this.pictures = classifiedAd.getPictures()
          .stream()
          .map(PictureEntity::from)
          .collect(Collectors.toList());
    }
  }

  public ClassifiedAd toClassifiedAd() {
    ClassifiedAd classifiedAd = new ClassifiedAd(new ClassifiedAdId(id), new UserId(owner));
    if (!Strings.isNullOrEmpty(text)) {
      classifiedAd.updateText(new ClassifiedAdText(text));
    }
    if (!Strings.isNullOrEmpty(title)) {
      classifiedAd.updateTitle(new ClassifiedAdTitle(title));
    }
    if (price != null) {
      classifiedAd.updatePrice(price);
    }
    if (pictures != null && pictures.size() > 0) {
      pictures.forEach(pictureEntity ->
          classifiedAd.addPicture(
              new PictureId(pictureEntity.getId()),
              pictureEntity.getUri(),
              new PictureSize(pictureEntity.getWidth(), pictureEntity.getHeight()),
              pictureEntity.getOrder()));
    }
    if (approvedBy != null) {
      classifiedAd.approve(new UserId(approvedBy));
    }

    if (state == ClassifiedAdState.PENDING_REVIEW) {
      classifiedAd.requestToPublish();
    }
    return classifiedAd;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOwner() {
    return owner;
  }

  public UUID getApprovedBy() {
    return approvedBy;
  }

  public String getText() {
    return text;
  }

  public String getTitle() {
    return title;
  }

  public List<PictureEntity> getPictures() {
    return pictures;
  }

  public Price getPrice() {
    return price;
  }

  public ClassifiedAdState getState() {
    return state;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public void setApprovedBy(UUID approvedBy) {
    this.approvedBy = approvedBy;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPictures(List<PictureEntity> pictures) {
    this.pictures = pictures;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  public void setState(ClassifiedAdState state) {
    this.state = state;
  }

  @Override
  public String getCollection() {
    return ClassifiedAd.class.getSimpleName().toLowerCase();
  }

  public ClassifiedAdQueryEntity toClassifiedAdReadEntity() {
    var builder = ImmutableClassifiedAdQueryEntity.builder();
    builder
        .id(id)
        .text(text).title(title)
        .ownerId(owner)
        .state(state);

    if (price != null) {
      builder.price(
          ImmutablePriceDto.builder().currencyCode(price.money().currencyCode())
              .amount(price.money().amount())
              .build());
    }
    if (pictures != null && pictures.size() > 0) {
      List<PictureDto> pictureDtos = pictures.stream()
          .map(pic -> ImmutablePictureDto.builder()
              .height(pic.getHeight())
              .width(pic.getWidth())
              .uri(pic.getUri())
              .order(pic.getOrder())
              .id(pic.getId())
              .build())
          .collect(Collectors.toList());
      builder.addAllPrictures(pictureDtos);
    }
    if (approvedBy != null) {
      builder.approver(approvedBy);
    }

    return builder.build();
  }
}

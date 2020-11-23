package com.marketplace.domain.classifiedad.entity;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.shared.UserId;
import com.marketplace.event.Event;
import com.marketplace.framework.Strings;
import com.marketplace.mongo.entity.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @Override
  public String getCollection() {
    return ClassifiedAd.class.getSimpleName().toLowerCase();
  }
}

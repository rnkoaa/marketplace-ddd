package com.marketplace.domain.classifiedad;

import com.marketplace.domain.InvalidStateException;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.events.ClassifiedAdCreated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPriceUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdSentForReview;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTextUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTitleChanged;
import com.marketplace.domain.classifiedad.events.ClassifiedApproved;
import com.marketplace.domain.classifiedad.events.PictureAddedToAClassifiedAd;
import com.marketplace.domain.shared.UserId;
import com.marketplace.event.Event;
import com.marketplace.event.EventId;
import com.marketplace.framework.AggregateRoot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClassifiedAd extends AggregateRoot<EventId, Event> {

  private ClassifiedAdId id;
  private final List<Picture> pictures;
  private UserId ownerId;
  private ClassifiedAdTitle title;
  private ClassifiedAdText text;
  private Price price;
  private UserId approvedBy;
  private ClassifiedAdState state;


  /**
   * this is for jackson deserialization
   */
  public ClassifiedAd() {
    pictures = new ArrayList<>();
//        apply();
  }

  public ClassifiedAd(ClassifiedAdId id, UserId ownerId) {
    this.pictures = new ArrayList<>();
    apply(new ClassifiedAdCreated(id.id(), ownerId.id()));
  }

  public void updateTitle(ClassifiedAdTitle title) {
    apply(new ClassifiedAdTitleChanged(id.id(), title.toString()));
  }

  public void updateOwner(UserId userId) {
    this.ownerId = userId;
  }

  public void updateText(ClassifiedAdText text) {
    apply(new ClassifiedAdTextUpdated(id.id(), text.toString()));
  }

  public void approve(UserId approvedBy) {
    apply(ClassifiedApproved.builder()
        .id(id.id())
        .userId(approvedBy.id())
        .build());
  }

  public void updatePrice(Price price) {
    apply(new ClassifiedAdPriceUpdated(id.id(), price.money().amount(), price.money().currencyCode()));
  }

  public void requestToPublish() {
    apply(new ClassifiedAdSentForReview(id.id()));
  }

  public PictureId addPicture(PictureId id, String uri, PictureSize size, int order) {
    int newPictureOrder = (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
    var pictureId = (id != null) ? id : PictureId.newPictureId();
    apply(PictureAddedToAClassifiedAd.builder()
        .classifiedAdId(this.id.id())
        .pictureId(pictureId.id())
        .url(uri)
        .height(size.height())
        .width(size.width())
        .order(newPictureOrder)
        .build());
    return pictureId;
  }

  public PictureId addPicture(String uri, PictureSize size, int order) {
    int newPictureOrder = (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
    var pictureId = UUID.randomUUID();
    apply(PictureAddedToAClassifiedAd.builder()
        .classifiedAdId(this.id.id())
        .pictureId(pictureId)
        .url(uri)
        .height(size.height())
        .width(size.width())
        .order(newPictureOrder)
        .build());
    return new PictureId(pictureId);
  }

  public Picture createPicture(PictureSize pictureSize, String uri, int order) {
    var pictureItem = new Picture(this);
    var pictureId = addPicture(uri, pictureSize, order);
    Optional<Picture> picture = findPicture(pictureId);
    return picture.orElse(pictureItem);
  }

  public Picture createPicture(PictureId pictureId, PictureSize pictureSize, String uri, int order) {
    var pictureItem = new Picture(this);
    var result = addPicture(pictureId, uri, pictureSize, order);
    Optional<Picture> picture = findPicture(result);
    return picture.orElse(pictureItem);
  }

  public PictureId resizePicture(PictureId pictureId, PictureSize newSize) {
    var picture = findPicture(pictureId);
    var p = picture
        .orElseThrow(() -> new IllegalArgumentException("cannot resize a picture that I don't have"));
    p.resize(newSize);
    return pictureId;
  }

  private Optional<Picture> findPicture(PictureId pictureId) {
    return this.pictures.stream()
        .filter(p -> p.getId().equals(pictureId))
        .findFirst();
  }

  private Optional<Picture> first() {
    return this.pictures.stream().min(Comparator.comparingInt(Picture::getOrder));
  }

  @Override
  public void when(Event event) {
    if (event instanceof ClassifiedAdCreated e) {
      this.id = ClassifiedAdId.from(e.getId());
      this.ownerId = new UserId(e.getUserId());
      this.state = ClassifiedAdState.INACTIVE;
    } else if (event instanceof ClassifiedAdTextUpdated e) {
      this.text = new ClassifiedAdText(e.getText());
    } else if (event instanceof ClassifiedAdPriceUpdated e) {
      this.price = new Price(new Money(e.getPrice(), e.getCurrencyCode()));
    } else if (event instanceof ClassifiedAdTitleChanged e) {
      this.title = new ClassifiedAdTitle(e.getTitle());
    } else if (event instanceof ClassifiedAdSentForReview e) {
      this.state = ClassifiedAdState.PENDING_REVIEW;
    } else if (event instanceof ClassifiedApproved e) {
      this.state = ClassifiedAdState.APPROVED;
      this.approvedBy = UserId.from(e.getUserId());
    } else if (event instanceof PictureAddedToAClassifiedAd e) {
      var picture = new Picture(this);
      applyToEntity(picture, e);
      this.pictures.add(picture);
    } else if (event instanceof ClassifiedAdPictureResized e) {
      var optionalPicture = findPicture(new PictureId(e.getPictureId()));
      optionalPicture.ifPresent(picture -> applyToEntity(picture, e));
    }
  }

  @Override
  public void ensureValidState(Event event) {
    var valid = id != null && ownerId != null;

    boolean foundBadPictures = /*pictures.size() == 0 ||*/ pictures.stream().anyMatch(p -> !p.hasCorrectSize());

    valid = valid && switch (this.state) {
      case PENDING_REVIEW -> title != null && text != null
          && price != null
          && price.money() != null
          && price.money().amount().doubleValue() > 0
          && !foundBadPictures;
      case INACTIVE, MARKED_AS_SOLD -> !foundBadPictures;
      case ACTIVE, APPROVED -> title != null
          && text != null
          && price != null
          && price.money() != null
          && price.money().amount().doubleValue() > 0
          && approvedBy != null
          && !foundBadPictures;
    };
    if (!valid) {
      throw new InvalidStateException("post checks failed in state while processing event " + event.name());
    }
  }

}

package com.marketplace.domain.classifiedad;

import com.marketplace.domain.InvalidStateException;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.events.ClassifiedAdCreated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPriceUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdSentForReview;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTextUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTitleChanged;
import com.marketplace.domain.classifiedad.events.ClassifiedApproved;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdCreated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdPriceUpdated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdSentForReview;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdTextUpdated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdTitleChanged;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedApproved;
import com.marketplace.domain.classifiedad.events.ImmutablePictureAddedToAClassifiedAd;
import com.marketplace.domain.classifiedad.events.PictureAddedToAClassifiedAd;
import com.marketplace.domain.shared.IdGenerator;
import com.marketplace.domain.shared.IdGeneratorImpl;
import com.marketplace.domain.shared.UserId;
import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClassifiedAd extends AggregateRoot<EventId, VersionedEvent> {

  private final IdGenerator idGenerator = new IdGeneratorImpl();
  private static final String AGGREGATE_NAME = ClassifiedAd.class.getSimpleName();
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
  }

  public ClassifiedAd(CreateClassifiedAd createClassifiedAd) {
    this.pictures = new ArrayList<>();
    ClassifiedAdId classifiedAdId = createClassifiedAd.getClassifiedAdId()
        .map(ClassifiedAdId::from)
        .orElse(ClassifiedAdId.newClassifedAdId());
    apply(ImmutableClassifiedAdCreated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(classifiedAdId.id())
        .aggregateName(AGGREGATE_NAME)
        .ownerId(createClassifiedAd.getOwnerId())
        .build());
  }

  public ClassifiedAd(ClassifiedAdId id, UserId ownerId) {
    this.pictures = new ArrayList<>();
    apply(ImmutableClassifiedAdCreated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .ownerId(ownerId.id())
        .build());
  }

  public ClassifiedAdId getId() {
    return id;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public UserId getOwnerId() {
    return ownerId;
  }

  public ClassifiedAdTitle getTitle() {
    return title;
  }

  public ClassifiedAdText getText() {
    return text;
  }

  public Price getPrice() {
    return price;
  }

  public UserId getApprovedBy() {
    return approvedBy;
  }

  public ClassifiedAdState getState() {
    return state;
  }

  public void updateTitle(ClassifiedAdTitle title) {
    apply(ImmutableClassifiedAdTitleChanged.builder()
        .id(idGenerator.newUUID())
        .aggregateId(this.id.id())
        .aggregateName(AGGREGATE_NAME)
        .title(title.toString())
        .build());
  }

  public void updateOwner(UserId userId) {
    this.ownerId = userId;
  }

  public void updateText(ClassifiedAdText text) {
    apply(ImmutableClassifiedAdTextUpdated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .text(text.toString())
        .build());
  }

  public void approve(UserId approvedBy) {
    apply(ImmutableClassifiedApproved.builder()
        .id(idGenerator.newUUID())
        .aggregateName(AGGREGATE_NAME)
        .aggregateId(id.id())
        .userId(approvedBy.id())
        .build());
  }

  public void updatePrice(Price price) {
    apply(ImmutableClassifiedAdPriceUpdated.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .price(price.money().amount())
        .currency(price.money().currencyCode())
        .build());
  }

  public void requestToPublish() {
    apply(ImmutableClassifiedAdSentForReview.builder()
        .id(idGenerator.newUUID())
        .aggregateId(id.id())
        .aggregateName(AGGREGATE_NAME)
        .build());
  }

  public PictureId addPicture(PictureId id, String uri, PictureSize size, int order) {
    int newPictureOrder = (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
    var pictureId = (id != null) ? id : PictureId.newPictureId();
    apply(ImmutablePictureAddedToAClassifiedAd.builder()
        .id(idGenerator.newUUID())
        .aggregateName(AGGREGATE_NAME)
        .aggregateId(id.id())
        .pictureId(pictureId.id())
        .url(uri)
        .height(size.height())
        .width(size.width())
        .order(newPictureOrder)
        .classifiedAdId(id.id())
        .build());
    return pictureId;
  }

  public PictureId addPicture(String uri, PictureSize size, int order) {
    int newPictureOrder = (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
    var pictureId = idGenerator.newUUID();
    apply(ImmutablePictureAddedToAClassifiedAd.builder()
        .id(idGenerator.newUUID())
        .aggregateName(AGGREGATE_NAME)
        .aggregateId(id.id())
        .pictureId(pictureId)
        .url(uri)
        .height(size.height())
        .width(size.width())
        .order(newPictureOrder)
        .classifiedAdId(id.id())
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
  public void when(VersionedEvent event) {
    if (event instanceof ClassifiedAdCreated e) {
      this.id = ClassifiedAdId.from(e.getAggregateId());
      this.ownerId = new UserId(e.getOwnerId());
      this.state = ClassifiedAdState.INACTIVE;
    } else if (event instanceof ClassifiedAdTextUpdated e) {
      this.text = new ClassifiedAdText(e.getText());
    } else if (event instanceof ClassifiedAdPriceUpdated e) {
      this.price = new Price(new Money(e.getPrice(), e.getCurrency()));
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
  public void ensureValidState(VersionedEvent event) {
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
      throw new InvalidStateException("post checks failed in state while processing event " + event.getClass().getSimpleName());
    }
  }

  public void setState(ClassifiedAdState state) {
    this.state = state;
  }
}

package com.marketplace.domain.classifiedad;

import com.marketplace.domain.InvalidStateException;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureRules;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.events.ClassifiedAdCreated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPriceUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdSentForReview;
import com.marketplace.domain.classifiedad.events.ClassifiedAdSoldEvent;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTextUpdated;
import com.marketplace.domain.classifiedad.events.ClassifiedAdTitleChanged;
import com.marketplace.domain.classifiedad.events.ClassifiedApproved;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdCreated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdPriceUpdated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdSentForReview;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdSoldEvent;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdTextUpdated;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdTitleChanged;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedApproved;
import com.marketplace.domain.classifiedad.events.ImmutablePictureAddedToAClassifiedAd;
import com.marketplace.domain.classifiedad.events.ImmutableRejectedEvent;
import com.marketplace.domain.classifiedad.events.PictureAddedToAClassifiedAd;
import com.marketplace.domain.classifiedad.events.RejectedEvent;
import com.marketplace.domain.shared.IdGenerator;
import com.marketplace.domain.shared.IdGeneratorImpl;
import com.marketplace.domain.shared.UserId;
import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import com.marketplace.domain.shared.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private String rejectedMessage;

    /**
     * this is for jackson deserialization
     */
    public ClassifiedAd() {
        pictures = new ArrayList<>();
        when(ImmutableClassifiedAdCreated.builder()
            .id(idGenerator.newUUID())
            .ownerId(UserId.EMPTY_VALUE.id())
            .aggregateId(ClassifiedAdId.EMPTY_VALUE.id())
            .aggregateName(AGGREGATE_NAME)
            .build()
        );
    }

    public ClassifiedAd(CreateClassifiedAd createClassifiedAd) {
        this.pictures = new ArrayList<>();
        ClassifiedAdId classifiedAdId = createClassifiedAd.getClassifiedAdId()
            .map(ClassifiedAdId::from)
            .orElse(ClassifiedAdId.newClassifiedAdId());
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

    public String getRejectedMessage() {
        return rejectedMessage;
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

    public void reject(UUID approver, String reason) {
        if (this.state != ClassifiedAdState.PENDING_REVIEW) {
            throw new InvalidStateException("ClassifiedAd must be in an inactive state before it can be published");
        }
        apply(ImmutableRejectedEvent.builder()
            .id(idGenerator.newUUID())
            .aggregateId(id.id())
            .aggregateName(AGGREGATE_NAME)
            .approver(UserId.from(approver))
            .rejectedMessage(reason)
            .build());
    }

    public void approve(UserId approvedBy) {
        if (this.state != ClassifiedAdState.PENDING_REVIEW) {
            throw new InvalidStateException("ClassifiedAd must be in an inactive state before it can be published");
        }
        apply(ImmutableClassifiedApproved.builder()
            .id(idGenerator.newUUID())
            .aggregateName(AGGREGATE_NAME)
            .aggregateId(id.id())
            .userId(approvedBy.id())
            .build());


        // fake active
//        apply(ImmutableClassfiedAd)
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
        if (this.state != ClassifiedAdState.INACTIVE) {
            throw new InvalidStateException("ClassifiedAd must be in an inactive state before it can be published");
        }
        apply(ImmutableClassifiedAdSentForReview.builder()
            .id(idGenerator.newUUID())
            .aggregateId(id.id())
            .aggregateName(AGGREGATE_NAME)
            .build());
    }

    public void markItemAsSold() {
        if (this.state != ClassifiedAdState.ACTIVE) {
            throw new InvalidStateException("ClassifiedAd must be in an active state before it can be sold");
        }
        apply(ImmutableClassifiedAdSoldEvent.builder()
            .id(idGenerator.newUUID())
            .aggregateId(id.id())
            .aggregateName(AGGREGATE_NAME)
            .build());
    }

    public PictureId addPicture(PictureId id, String uri, PictureSize size, int order) {
        int newPictureOrder =
            (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
        var pictureId = (id != null) ? id : PictureId.newPictureId();
        apply(ImmutablePictureAddedToAClassifiedAd.builder()
            .id(idGenerator.newUUID())
            .aggregateName(AGGREGATE_NAME)
            .aggregateId(pictureId.id())
            .pictureId(pictureId.id())
            .url(uri)
            .height(size.height())
            .width(size.width())
            .order(newPictureOrder)
            .classifiedAdId(pictureId.id())
            .build());
        return pictureId;
    }

    public PictureId addPicture(String uri, PictureSize size, int order) {
        if (!PictureRules.hasCorrectSize(size)) {
            throw new InvalidStateException("Picture size is incorrect.");
        }
        int newPictureOrder =
            (order > 0) ? order : ((pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1);
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

    @Override
    public void when(VersionedEvent event) {
        if (event instanceof ClassifiedAdCreated e) {
            this.id = ClassifiedAdId.from(e.getAggregateId());
            this.ownerId = new UserId(e.getOwnerId());
            this.state = ClassifiedAdState.INACTIVE;
            this.price = Price.NEW_PRICE;
            this.text = ClassifiedAdText.DEFAULT;
            this.title = ClassifiedAdTitle.DEFAULT;
        } else if (event instanceof ClassifiedAdTextUpdated e) {
            this.text = new ClassifiedAdText(e.getText());
        } else if (event instanceof ClassifiedAdPriceUpdated e) {
            this.price = new Price(new Money(e.getPrice(), e.getCurrency()));
        } else if (event instanceof ClassifiedAdTitleChanged e) {
            this.title = new ClassifiedAdTitle(e.getTitle());
        } else if (event instanceof ClassifiedAdSentForReview) {
            this.state = ClassifiedAdState.PENDING_REVIEW;
        } else if (event instanceof RejectedEvent e) {
            this.state = ClassifiedAdState.REJECTED;
            this.approvedBy = e.getApprover();
            this.rejectedMessage = e.rejectedMessage();
        } else if (event instanceof ClassifiedApproved e) {
            this.state = ClassifiedAdState.ACTIVE;
            this.approvedBy = UserId.from(e.getUserId());
        } else if (event instanceof ClassifiedAdSoldEvent) {
            this.state = ClassifiedAdState.MARKED_AS_SOLD;
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
//
//        boolean foundBadPictures = /*pictures.size() == 0 ||*/ pictures.stream().anyMatch(p -> !p.hasCorrectSize());
//
//        valid = valid && switch (this.state) {
//            case PENDING_REVIEW -> title != null && text != null
//                && price != null
//                && price.money() != null
//                && price.money().amount().doubleValue() > 0
//                && !foundBadPictures;
//            case INACTIVE, MARKED_AS_SOLD -> !foundBadPictures;
//            case ACTIVE, APPROVED -> title != null
//                && text != null
//                && price != null
//                && price.money() != null
//                && price.money().amount().doubleValue() > 0
//                && approvedBy != null
//                && !foundBadPictures;
//        };
        boolean valid = id.isValid() && ownerId.isValid();
        var baseValidation = ValidationResult
            .of(valid, "ClassifiedAdId or OwnerId cannot be null for event " + event.getClass().getSimpleName());
        if (baseValidation.isNotValid()) {
            throw new InvalidStateException(baseValidation.message());
        }

        var validationResult = switch (state) {
            case PENDING_REVIEW -> {
                var validResult = title.isValid() && text.isValid() && price.valid();
                yield ValidationResult.of(validResult, "title, text and price are required to be published for event "
                    + event.getClass().getSimpleName());
            }
            case ACTIVE, APPROVED -> {
                var validResult = title.isValid() && text.isValid() && price.valid() && approvedBy.isValid();
                yield ValidationResult
                    .of(validResult, "title, text and approver are required to be approved for event " +
                        event.getClass().getSimpleName());
            }
            default -> ValidationResult.of(true, "");
        };

        if (validationResult.isNotValid()) {
            throw new InvalidStateException(validationResult.message());
        }
    }
//    }

    public void setState(ClassifiedAdState state) {
        this.state = state;
    }
}

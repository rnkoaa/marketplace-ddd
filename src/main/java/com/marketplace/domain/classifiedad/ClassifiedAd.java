package com.marketplace.domain.classifiedad;

import com.marketplace.domain.*;
import com.marketplace.domain.classifiedad.events.*;
import com.marketplace.domain.shared.UserId;
import com.marketplace.event.Event;
import com.marketplace.event.EventId;
import com.marketplace.framework.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.persistence.Id;
import java.util.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClassifiedAd extends AggregateRoot<EventId, Event> {
    @Id
    private ClassifiedAdId id;

    @BsonIgnore
    private final List<Picture> pictures;
    private UserId ownerId;

    private ClassifiedAdTitle title;
    private ClassifiedAdText text;
    @BsonIgnore
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

    public PictureId addPicture(String uri, PictureSize size) {
        int newPictureOrder = (pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1;
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

    public Picture createPicture(PictureSize pictureSize) {
        var pictureItem = new Picture(this);
        var pictureId = addPicture("", pictureSize);
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

    private Optional<Picture> first() {
        return this.pictures.stream().min(Comparator.comparingInt(Picture::getOrder));
    }

    @Override
    public void when(Event event) {
        if (event instanceof ClassifiedAdCreated e) {
            this.id = new ClassifiedAdId(e.getId());
            this.ownerId = new UserId(e.getUserId());
            this.state = ClassifiedAdState.inactive;
        } else if (event instanceof ClassifiedAdTextUpdated e) {
            this.id = new ClassifiedAdId(e.getId());
            this.text = new ClassifiedAdText(e.getAdText());
        } else if (event instanceof ClassifiedAdPriceUpdated e) {
            this.id = new ClassifiedAdId(e.getId());
            this.price = new Price(new Money(e.getPrice(), e.getCurrencyCode()));
        } else if (event instanceof ClassifiedAdTitleChanged e) {
            this.id = new ClassifiedAdId(e.getId());
            this.title = new ClassifiedAdTitle(e.getTitle());
        } else if (event instanceof ClassifiedAdSentForReview e) {
            this.id = new ClassifiedAdId(e.getId());
            this.state = ClassifiedAdState.pendingReview;
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
            case pendingReview -> title != null && text != null
                    && price != null
                    && price.money() != null
                    && price.money().amount().doubleValue() > 0
                    && !foundBadPictures;
            case inactive, markedAsSold -> !foundBadPictures;
            case active -> title != null
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

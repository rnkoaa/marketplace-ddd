package com.marketplace.domain;

import com.marketplace.domain.events.ClassifiedAdPictureResized;
import com.marketplace.domain.events.PictureAddedToAClassifiedAd;
import com.marketplace.event.*;
import com.marketplace.framework.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClassifiedAd extends AggregateRoot<EventId, Event> {
    private final List<Picture> pictures;
    private ClassifiedAdId id;
    private UserId ownerId;
    private ClassifiedAdTitle title;
    private ClassifiedAdText text;
    private Price price;
    private UserId approvedBy;
    private ClassifiedAdState state;

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
        apply(new ClassifiedApproved(id.id(), approvedBy.id()));
    }

    public void updatePrice(Price price) {
        apply(new ClassifiedAdPriceUpdated(id.id(), price.money().amount(), price.money().currencyCode()));
    }

    public void requestToPublish() {
        apply(new ClassifiedAdSentForReview(id.id()));
    }

    public void addPicture(String uri, PictureSize size) {
        int newPictureOrder = (pictures == null || pictures.size() <= 0) ? 0 : pictures.size() + 1;
        apply(new PictureAddedToAClassifiedAd(
                this.id.id(), UUID.randomUUID(), uri, size.height(), size.width(), newPictureOrder
        ));
    }

    public void resizePicture(PictureId pictureId, PictureSize newSize) {
        var picture = findPicture(pictureId);
        var p = picture
                .orElseThrow(() -> new IllegalArgumentException("cannot resize a picture that I don't have"));
        p.resize(newSize);
    }

    private Optional<Picture> findPicture(PictureId pictureId) {
        return this.pictures.stream().filter(p -> p.getId() == pictureId).findFirst();
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

//            new Picture(apply(e))
//            new Picture(new EventApplier<>() {
//                @Override
//                public void apply(Event event) {
//                    if (event instanceof PictureAddedToAClassifiedAd pictureAddedToAClassifiedAd) {
//
//                    }
//                }
//            });
//            this.id = new ClassifiedAdId(e.getId());
//            this.state = ClassifiedAdState.pendingReview;
        } else if (event instanceof ClassifiedAdPictureResized e) {
            var optionalPicture = findPicture(new PictureId(e.getPictureId()));
            optionalPicture.ifPresent(picture -> {
                applyToEntity(picture, e);
            });
        }
    }

    @Override
    public void ensureValidState() {
        var valid = id != null && ownerId != null;
        valid = valid && switch (this.state) {
            case pendingReview -> title != null && text != null && price != null && price.money() != null && price.money().amount().doubleValue() > 0;
            case inactive, markedAsSold -> true;
            case active -> title != null && text != null &&
                    price != null && price.money() != null &&
                    price.money().amount().doubleValue() > 0 && approvedBy != null;
        };
        if (!valid) {
            throw new InvalidStateException("post checks failed in state");
        }
    }

}

package com.marketplace.domain;

import com.marketplace.domain.events.ClassifiedAdPictureResized;
import com.marketplace.domain.events.PictureAddedToAClassifiedAd;
import com.marketplace.event.Event;
import com.marketplace.event.EventId;
import com.marketplace.framework.Entity;
import com.marketplace.framework.EventApplier;
import lombok.Getter;

@Getter
public class Picture extends Entity<EventId, Event> {
    private PictureId id;
    private ClassifiedAdId parentId;
    private PictureSize size;
    private String uri;
    private int order;

    public Picture(EventApplier eventApplier) {
        super(eventApplier);
    }

    @Override
    public void when(Event event) {
        if (event instanceof PictureAddedToAClassifiedAd e) {
            this.id = new PictureId(e.getPictureId());
            this.parentId = new ClassifiedAdId(e.getClassifiedAdId());
            this.size = new PictureSize(e.getWidth(), e.getHeight());
            this.uri = e.getUrl();
            this.order = e.getOrder();
        } else if (event instanceof ClassifiedAdPictureResized e) {
            this.size = new PictureSize(e.getWidth(), e.getHeight());
        }
    }

    public void resize(PictureSize newSize) {
        apply(new ClassifiedAdPictureResized(this.id.id(), this.parentId.id(), newSize.height(), newSize.width()));
    }

    public boolean hasCorrectSize() {
        return PictureRules.hasCorrectSize(this);
    }
}

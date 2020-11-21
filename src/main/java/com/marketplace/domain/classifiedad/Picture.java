package com.marketplace.domain.classifiedad;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureRules;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.PictureAddedToAClassifiedAd;
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

//    public Picture(){
//        super(null);
//    }
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
        apply(ClassifiedAdPictureResized.builder()
                .classifiedAdId(this.parentId.id())
                .pictureId(this.id.id())
                .height(newSize.height())
                .width(newSize.width())
                .build());
    }

    public boolean hasCorrectSize() {
        return PictureRules.hasCorrectSize(this);
    }
}

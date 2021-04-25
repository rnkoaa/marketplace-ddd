package com.marketplace.domain.classifiedad;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureRules;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.events.ClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdPictureResized;
import com.marketplace.domain.classifiedad.events.PictureAddedToAClassifiedAd;
import com.marketplace.domain.shared.IdGenerator;
import com.marketplace.domain.shared.IdGeneratorImpl;
import com.marketplace.cqrs.event.Event;
import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.Entity;
import com.marketplace.cqrs.framework.EventApplier;

public class Picture extends Entity<EventId, VersionedEvent> {

  private final IdGenerator idGenerator = new IdGeneratorImpl();
  private PictureId id;
  private ClassifiedAdId parentId;
  private PictureSize size;
  private String uri;
  private int order;

  public PictureId getId() {
    return id;
  }

  public PictureSize getSize() {
    return size;
  }

  public String getUri() {
    return uri;
  }

  public int getOrder() {
    return order;
  }

  public ClassifiedAdId getParentId() {
    return parentId;
  }

  public Picture(EventApplier eventApplier) {
    super(eventApplier);
  }

  @Override
  public void when(VersionedEvent event) {
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
    apply(ImmutableClassifiedAdPictureResized.builder()
        .id(idGenerator.newUUID())
        .aggregateId(this.parentId.id())
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

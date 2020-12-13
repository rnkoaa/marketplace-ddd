package com.marketplace.framework;

import com.marketplace.event.VersionedEvent;
import java.util.ArrayList;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonIgnore;

public abstract class AggregateRoot<T, U extends VersionedEvent> implements InternalEventHandler<U>, EventApplier {

  @BsonIgnore
  private final List<VersionedEvent> changes;
  private long version;

  protected AggregateRoot() {
    this.changes = new ArrayList<>();
    version = -1L;
  }

  public void apply(VersionedEvent event) {
    when(event);
    ensureValidState(event);
    changes.add(event);
    incrementVersion();
  }

  public void incrementVersion() {
    version++;
  }

  public abstract void ensureValidState(VersionedEvent event);

  public void clearChanges() {
    changes.clear();
  }

  public List<VersionedEvent> getChanges() {
    return changes;
  }

  public abstract void when(VersionedEvent event);

  protected void applyToEntity(InternalEventHandler<VersionedEvent> entity, VersionedEvent event) {
    entity.handle(event);
  }

  public long getVersion() {
    return version;
  }

  @Override
  public void handle(VersionedEvent event) {
    when(event);
  }
}

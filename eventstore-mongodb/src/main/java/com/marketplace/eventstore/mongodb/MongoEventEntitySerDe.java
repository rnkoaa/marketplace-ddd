package com.marketplace.eventstore.mongodb;

import com.google.common.base.Strings;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bson.Document;

public class MongoEventEntitySerDe {

  public Document encode(MongoEventEntity value) {
    Document document = new Document();
    document.put("_id", value.getId());
    document.put("aggregateId", value.getAggregateId());
    document.put("version", value.getVersion());
    document.put("streamName", value.getStreamName());
    document.put("createdAt", value.getCreatedAt());
    document.put("events", value.getEvents());
    return document;
  }

  public MongoEventEntity decode(Document document) {
    UUID id = document.get("_id", UUID.class);
    UUID aggregateId = document.get("aggregateId", UUID.class);
    String streamName = document.getString("streamName");
    int version = document.getInteger("version", 0);
    List<Document> events = document.getList("events", Document.class);
    List<TypedEvent> typedEvents =
        events.stream().map(this::decodeTypeEvent).collect(Collectors.toList());
    Date createdAt = document.get("createdAt", Date.class);

    return ImmutableMongoEventEntity.builder()
        .id(id)
        .aggregateId(aggregateId)
        .streamName((!Strings.isNullOrEmpty(streamName)) ? streamName : "")
        .version(version)
        .events(typedEvents)
        .createdAt(createdAt.toInstant())
        .build();
  }

  private ImmutableTypedEvent decodeTypeEvent(Document doc) {
    String eventBody = doc.getString("eventBody");
    String type = doc.getString("type");
    int sequenceId = doc.getInteger("sequenceId", 0);
    return ImmutableTypedEvent.builder()
        .eventBody(eventBody)
        .type(type)
        .sequenceId(sequenceId)
        .build();
  }
}

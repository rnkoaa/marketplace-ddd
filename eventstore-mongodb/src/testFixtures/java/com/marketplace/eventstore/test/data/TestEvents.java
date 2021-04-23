package com.marketplace.eventstore.test.data;

import com.marketplace.eventstore.test.events.ImmutableTestCreatedEvent;
import com.marketplace.eventstore.test.events.ImmutableTestTextUpdatedEvent;
import com.marketplace.eventstore.test.events.ImmutableTestTitleUpdatedEvent;
import com.marketplace.eventstore.test.events.TestCreatedEvent;
import com.marketplace.eventstore.test.events.TestTextUpdatedEvent;
import com.marketplace.eventstore.test.events.TestTitleUpdatedEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestEvents {

  public static final UUID aggregateId = UUID.fromString("0a4a2dfc-eb10-45ec-9f8c-4d05e9dab6fe");
  public static final UUID createdEventId = UUID
      .fromString("a7c92883-5406-4ea5-a1c4-8d9d6b7ee9bc");
  public static final UUID titleUpdatedEventId = UUID
      .fromString("5a5346eb-c9dd-476b-a83b-9cacb0625b7f");
  public static final UUID titleUpdatedEventId2 = UUID
      .fromString("73093141-cbdb-4692-98de-622d832b15f4");

  public static final UUID textUpdatedEventId = UUID
      .fromString("ad78b366-b892-4d9b-bc22-88adac00e9f3");
  public static final UUID textUpdatedEventId2 = UUID
      .fromString("40a89f32-5fb1-43d4-8bfe-36f7e42aaca8");

  public static TestCreatedEvent testCreatedEvent = ImmutableTestCreatedEvent.builder()
      .aggregateId(aggregateId)
      .id(createdEventId)
      .createdAt(Instant.now())
      .build();

  public static TestTitleUpdatedEvent testTitleUpdatedEvent = ImmutableTestTitleUpdatedEvent
      .builder()
      .aggregateId(aggregateId)
      .id(titleUpdatedEventId)
      .version(1)
      .title("first title")
      .build();

  public static TestTitleUpdatedEvent testTitleUpdatedEvent2 = ImmutableTestTitleUpdatedEvent
      .builder()
      .aggregateId(aggregateId)
      .id(titleUpdatedEventId2)
      .title("second title")
      .build();

  public static TestTextUpdatedEvent testTextUpdatedEvent = ImmutableTestTextUpdatedEvent
      .builder()
      .aggregateId(aggregateId)
      .id(textUpdatedEventId)
      .version(2)
      .text("item is being sold as is")
      .build();

  public static TestTextUpdatedEvent testTextUpdatedEvent2 = ImmutableTestTextUpdatedEvent
      .builder()
      .aggregateId(aggregateId)
      .id(textUpdatedEventId2)
      .text("item is being sold as is; seller has good deal")
      .build();

 public static List<Event> aggregateEvents = List.of(
      testCreatedEvent,
      testTitleUpdatedEvent,
      testTextUpdatedEvent
  );
}

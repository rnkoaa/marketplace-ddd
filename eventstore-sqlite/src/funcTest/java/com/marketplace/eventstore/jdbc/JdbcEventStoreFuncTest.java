package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.impl.InMemoryEventPublisher;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ClassifiedAdCreated;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ClassifiedAdEventListener;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ClassifiedAdEventProcessor;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ImmutableClassifiedAdCreated;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ImmutableClassifiedAdTextUpdated;
import com.marketplace.eventstore.impl.fixtures.classifiedad.ImmutableClassifiedAdTitleUpdated;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@SuppressWarnings("UnstableApiUsage")
class JdbcEventStoreFuncTest extends AbstractJdbcFuncTest {

    final ObjectMapper objectMapper = new ObjectMapperBuilder().build();
    private final EventStore<VersionedEvent> eventStore;
    private final ClassifiedAdEventProcessor eventProcessor = Mockito.mock(ClassifiedAdEventProcessor.class);
    InMemoryEventPublisher eventPublisher;

    public JdbcEventStoreFuncTest() throws SQLException {
        super();
        var jdbcEventStoreRepository = new JdbcEventStoreRepositoryImpl(objectMapper, dslContext);
        var eventBus = new EventBus();
        eventPublisher = new InMemoryEventPublisher(eventBus);
        eventStore = new JdbcEventStoreImpl(eventPublisher, jdbcEventStoreRepository);
        var classifiedAdEventListener = new ClassifiedAdEventListener(eventProcessor);
        eventPublisher.registerListener(classifiedAdEventListener);
    }

    @AfterEach
    void cleanup() {
        eventPublisher.close();
        dslContext.delete(EVENT_DATA).execute();
    }

    @Test
    void createAndUpdateTitleInEventStore() {
        String classifiedAdId = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
        String aggregateName = ClassifiedAdCreated.class.getSimpleName();
        String streamId = String.format("%s:%s", aggregateName, classifiedAdId);
        var classifiedAdCreated = ImmutableClassifiedAdCreated.builder()
            .owner(UUID.fromString(ownerId))
            .id(UUID.fromString(classifiedAdId))
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(aggregateName)
//            .aggregateId()
            .build();

        Result<Boolean> appendResult = eventStore.append(streamId, 1, classifiedAdCreated);
        assertThat(appendResult.isPresent()).isTrue();
        assertThat(appendResult.get()).isTrue();

        var classifiedAdTitleUpdated = ImmutableClassifiedAdTitleUpdated.builder()
            .title("test title")
            .id(UUID.randomUUID())
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(aggregateName)
            .streamId(streamId)
            .build();
        EventStream<VersionedEvent> eventStream = eventStore.load(streamId);
        assertThat(eventStream.size()).isEqualTo(1);
        assertThat(eventStream.getVersion()).isEqualTo(1);

        int expectedVersion = eventStream.getVersion() + 1;
        Result<Boolean> updateAppendResult = eventStore.append(streamId, expectedVersion, classifiedAdTitleUpdated);
        assertThat(updateAppendResult.isPresent()).isTrue();
        assertThat(updateAppendResult.get()).isTrue();

        EventStream<VersionedEvent> updatedEventStream = eventStore.load(streamId);
        assertThat(updatedEventStream.size()).isEqualTo(2);
    }

    @Test
    void multipleEventsCanBeAdded() {
        String classifiedAdId = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
        var classifiedAdCreated = ImmutableClassifiedAdCreated.builder()
            .owner(UUID.fromString(ownerId))
            .id(UUID.fromString(classifiedAdId))
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
            .build();

        String streamId = String.format("%s:%s", classifiedAdCreated.getAggregateName(), classifiedAdId);

        var classifiedAdTitleUpdated = ImmutableClassifiedAdTitleUpdated.builder()
            .title("test title")
            .id(UUID.randomUUID())
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
            .build();
        var appendResult =
            eventStore.append(streamId, 1, List.of(classifiedAdCreated, classifiedAdTitleUpdated));

        assertThat(appendResult.isPresent()).isTrue();
        EventStream<VersionedEvent> eventStream = eventStore.load(streamId);
        assertThat(eventStream.size()).isEqualTo(2);
        assertThat(eventStream.getVersion()).isEqualTo(2);
    }

    @Test
    void canCreateEventStoreWithNewEvent() {
        String classifiedAdId = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
        var classifiedAdCreated = ImmutableClassifiedAdCreated.builder()
            .owner(UUID.fromString(ownerId))
            .id(UUID.fromString(classifiedAdId))
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
            .build();

        String streamId = String.format("%s:%s", classifiedAdCreated.getAggregateName(), classifiedAdId);
        eventStore.append(streamId, 1, classifiedAdCreated);

        EventStream<VersionedEvent> eventStream = eventStore.load(streamId);
        assertThat(eventStream.size()).isEqualTo(1);
        assertThat(eventStream.getVersion()).isEqualTo(1);
    }

    //
    @Test
    void differentAggregatesWillCreateDifferentStreams() {
        String classifiedAdId2 = "582a7769-f08a-470d-af65-85ef7ff7969f";
        String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";

        String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId1);

        eventStore.append(
            streamId,
            1,
            createEventsAggregate(UUID.fromString(classifiedAdId1), UUID.fromString(ownerId1))
        );

        String streamId2 = String.format("%s:%s", "ClassifiedAd", classifiedAdId2);

        eventStore.append(
            streamId2,
            1,
            createEventsAggregate(UUID.fromString(classifiedAdId2), UUID.fromString(ownerId1))
        );
//        assertThat(eventStore.size()).isEqualTo(2);
        assertThat(eventStore.load(streamId).size()).isEqualTo(2);
        assertThat(eventStore.load(streamId2).size()).isEqualTo(2);
    }

    //
    @Test
    void eventsCanBeLoadedFromVersion() {
        String classifiedAdId1 = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId1 = "89b69f4f-e36e-4f2b-baa0-d47057e02117";
        String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId1);

        eventStore.append(
            streamId,
            1,
            createEventsAggregate(UUID.fromString(classifiedAdId1), UUID.fromString(ownerId1)))
        ;

        EventStream<VersionedEvent> eventStream = eventStore.load(streamId);
        assertThat(eventStream.size()).isEqualTo(2);
        assertThat(eventStream.getVersion()).isEqualTo(2);

        EventStream<VersionedEvent> nextVersionEventStream = eventStore.load(streamId, 2);
        assertThat(nextVersionEventStream.size()).isEqualTo(1);
        assertThat(nextVersionEventStream.getVersion()).isEqualTo(2);
    }

    //
    @Test
    void publishSavesEventAndPublishes() {
        String classifiedAdId = "9d5d69ee-eadd-4352-942e-47935e194d22";
        String ownerId = "89b69f4f-e36e-4f2b-baa0-d47057e02117";

        String streamId = String.format("%s:%s", "ClassifiedAd", classifiedAdId);

        var classifiedAdCreated = ImmutableClassifiedAdCreated.builder()
            .owner(UUID.fromString(ownerId))
            .id(UUID.fromString(classifiedAdId))
            .aggregateId(UUID.fromString(classifiedAdId))
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
//            .aggregateId()
            .build();

        eventStore.publish(streamId, 1, classifiedAdCreated);
        assertThat(eventStore.load(streamId).size()).isEqualTo(1);

        EventStream<VersionedEvent> eventStream = eventStore.load(streamId);
        assertThat(eventStream.size()).isEqualTo(1);
        assertThat(eventStream.getVersion()).isEqualTo(1);
        verify(eventProcessor, times(1)).create(classifiedAdCreated);
    }

    List<VersionedEvent> createEventsAggregate(UUID aggregateId, UUID ownerId) {
        var classifiedAdCreated = ImmutableClassifiedAdCreated.builder()
            .owner(ownerId)
            .id(UUID.randomUUID())
            .aggregateId(aggregateId)
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
            .build();

        var classifiedAdTextUpdated = ImmutableClassifiedAdTextUpdated.builder()
            .id(UUID.randomUUID())
            .aggregateId(aggregateId)
            .text("test classified ad text")
            .aggregateName(ClassifiedAdCreated.class.getSimpleName())
            .build();

        return List.of(classifiedAdCreated, classifiedAdTextUpdated);
    }
}
package com.marketplace.eventstore.mongodb;

import static com.marketplace.eventstore.test.data.TestMongoEvents.objectMapper;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.marketplace.eventstore.test.data.TestMongoEvents;
import com.marketplace.eventstore.test.events.ImmutableTestCreatedEvent;
import com.marketplace.eventstore.test.events.ImmutableTestTextUpdatedEvent;
import com.marketplace.eventstore.test.events.ImmutableTestTitleUpdatedEvent;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypedEventTest {

    private static final Logger logger = LoggerFactory.getLogger(TypedEventTest.class);

    @Test
    void testTypedEventDeserialization() {
        TypedEvent typedEvent = TestMongoEvents.testCreatedTypedEvent;

        assertThat(typedEvent.getEventBody()).isNotBlank();

        Try<Tuple2<Integer, Object>> o = deserializeTypedEvent(typedEvent);
        assertThat(o.isSuccess()).isNotNull();
        assertThat(o.get()._1).isEqualTo(0);
        assertThat(o.get()._2).isNotNull();
        System.out.println(o.get()._2);
    }

    @Test
    void testDeserializeCollectionOfTypedEvents() {
        List<TypedEvent> sequenceTypedEvents = TestMongoEvents.sequencedEvents;
        assertThat(sequenceTypedEvents).size().isEqualTo(3);

        List<Event> collect = sequenceTypedEvents.stream()
            .map(this::deserializeTypedEvent)
            .filter(Try::isSuccess)
            .map(Try::get)
            .sorted(Comparator.comparingInt(it -> it._1))
            .map(it -> (Event) it._2)
            .collect(Collectors.toUnmodifiableList());

        assertThat(collect).size().isEqualTo(3);
        assertThat(collect.get(0)).isInstanceOf(ImmutableTestCreatedEvent.class);
        assertThat(collect.get(1)).isInstanceOf(ImmutableTestTitleUpdatedEvent.class);
        assertThat(collect.get(2)).isInstanceOf(ImmutableTestTextUpdatedEvent.class);
    }

    Try<Tuple2<Integer, Object>> deserializeTypedEvent(TypedEvent typedEvent) {
        return Try.of(() -> Class.forName(typedEvent.getType()))
            .onFailure(ex -> logger.info("error retrieving class for {} with exception {}",
                typedEvent.getType(), ex.getCause()))
            .flatMap(clzz -> Try.of(() -> {
                Object object = objectMapper.readValue(typedEvent.getEventBody(), clzz);
                return Tuple.of(typedEvent.getSequenceId(), object);
            }));
    }

    /*
    // when
     // when
    A result = Try.of(this::bunchOfWork)
    .recover(x -> Match(x).of(
        Case($(instanceOf(Exception_1.class)), t -> somethingWithException(t)),
        Case($(instanceOf(Exception_2.class)), t -> somethingWithException(t)),
        Case($(instanceOf(Exception_n.class)), t -> somethingWithException(t))
    ))
    .getOrElse(other);
     */
}


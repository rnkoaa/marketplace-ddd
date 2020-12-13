package com.marketplace.domain.classifiedad;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.context.ObjectMapperModule;
import com.marketplace.eventstore.framework.event.Event;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

class CreateClassifiedAdFromEventsTest {

  static final ObjectMapper objectMapper = ObjectMapperModule.provideObjectMapper();

  JsonNode loadEvents() throws IOException {
    var resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fixtures/classified_ad_events.json");
    return objectMapper.readTree(resourceAsStream);
  }

  List<Event> readEvents(JsonNode jsonNode) {
    if (jsonNode.isArray()) {
      return StreamSupport.stream(jsonNode.spliterator(), false)
          .map(it -> {
            String type = it.get("type").asText();
            Class<?> clzz = getClassFromName(type);
            Object value = objectMapper.convertValue(it, clzz);
            return (Event) value;
          })
          .sorted(Comparator.comparing(Event::getCreatedAt))
          .collect(Collectors.toList());
    }
    return List.of();
  }

  @Test
  void classifiedAd_can_be_created_from_events() throws IOException {
    JsonNode jsonNode = loadEvents();

    List<Event> events = readEvents(jsonNode);

    ClassifiedAd classifiedAd = ClassifiedAd.of(events);
    assertThat(classifiedAd).isNotNull();
    assertThat(classifiedAd.getId().value()).isNotNull().isEqualByComparingTo(UUID.fromString("da31260e-b943-425f-8563-ddb6a911662d"));
    assertThat(classifiedAd.getOwnerId().value()).isNotNull().isEqualByComparingTo(UUID.fromString("ae48b592-9526-4952-81fc-584af8a46565"));
    assertThat(classifiedAd.getState()).isEqualByComparingTo(ClassifiedAdState.APPROVED);
    assertThat(classifiedAd.getPrice()).isNotNull();
    assertThat(classifiedAd.getPrice().money().amount()).isNotNull().isEqualTo(new BigDecimal("10.51"));
    assertThat(classifiedAd.getPrice().money().currencyCode()).isNotNull().isEqualTo("USD");
    assertThat(classifiedAd.getPictures()).isNotEmpty().hasSize(1);
    assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(900);
    assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(800);

    assertThat(classifiedAd.getVersion()).isEqualTo(6);
    assertThat(classifiedAd.getChanges()).isEmpty();

  }

  @Test
  void json_can_be_loaded() throws IOException {
    JsonNode jsonNode = loadEvents();
    assertThat(jsonNode).isNotNull();

    List<Event> events = readEvents(jsonNode);
    assertThat(events).isNotEmpty().hasSize(7);
  }

  static Map<String, Class<?>> classCache = new ConcurrentHashMap<>();

  private Class<?> getClassFromName(String type) {
    return classCache.computeIfAbsent(type, typeName -> {
      try {
        return Class.forName(typeName);
      } catch (ClassNotFoundException ignored) {
        return null;
      }
    });
  }
}

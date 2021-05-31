package com.marketplace.client.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Map;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableEventStreamResponse.class)
@JsonSerialize(as = ImmutableEventStreamResponse.class)
public interface EventStreamResponse {

    @JsonProperty("id")
    String getId();

    @JsonProperty("version")
    int getVersion();

    @JsonProperty("name")
    String getName();

    @JsonProperty("events")
    List<Map<String, Object>> getEvents();

}

package com.marketplace.domain.classifiedad;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface CommandHandlerResult<T> {

  Optional<T> getResult();

  @JsonProperty("successful")
  boolean isSuccessful();

  Optional<String> getMessage();
}

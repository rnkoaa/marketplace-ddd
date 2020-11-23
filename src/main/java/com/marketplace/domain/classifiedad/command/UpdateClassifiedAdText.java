package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.command.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = UpdateClassifiedAdText.UpdateClassifiedAdTextBuilder.class)
public class UpdateClassifiedAdText implements Command {

  private UUID id;
  private String text;

  @JsonPOJOBuilder(withPrefix = "")
  public static class UpdateClassifiedAdTextBuilder {

  }
}

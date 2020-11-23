package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassifiedAdOwner implements Command {
   private UUID id;
   private UUID ownerId;
}

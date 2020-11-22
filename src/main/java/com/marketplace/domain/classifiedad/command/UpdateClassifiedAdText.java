package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassifiedAdText implements Command {
    private UUID id;
    private String text;
}

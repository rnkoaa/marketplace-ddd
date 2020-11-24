package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateClassifiedAdTitle implements Command {
    private UUID id;
    private String title;
}

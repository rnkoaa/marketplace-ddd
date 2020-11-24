package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApproveClassifiedAd implements Command {
    private UUID id;
    private UUID approverId;
}

package com.marketplace.domain.userprofile.event;

import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDisplayNameUpdated implements Event {
    private UUID userId;
    private String displayName;

    @Override
    public UUID getId() {
        return userId;
    }
}

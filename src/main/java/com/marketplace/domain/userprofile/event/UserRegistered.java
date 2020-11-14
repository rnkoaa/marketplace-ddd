package com.marketplace.domain.userprofile.event;

import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistered implements Event {
    private UUID userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;

    @Override
    public UUID getId() {
       return userId;
    }
}

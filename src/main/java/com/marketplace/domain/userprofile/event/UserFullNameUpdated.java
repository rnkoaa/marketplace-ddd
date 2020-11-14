package com.marketplace.domain.userprofile.event;

import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFullNameUpdated implements Event {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String middleName;

    @Override
    public UUID getId() {
       return userId;
    }
}

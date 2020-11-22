package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.domain.userprofile.FullName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserFullNameCommand {
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;

    @JsonIgnore
    public FullName fullName(){
        return new FullName(firstName, middleName, lastName);
    }
}

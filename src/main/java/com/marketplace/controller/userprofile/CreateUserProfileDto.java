package com.marketplace.controller.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.domain.userprofile.FullName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserProfileDto {
   private String firstName;
   private String lastName;
   private String middleName;
   private String displayName;
   private String photoUrl;

   @JsonIgnore
    public FullName getFullName() {
        return new FullName(firstName, middleName, lastName);
    }
}

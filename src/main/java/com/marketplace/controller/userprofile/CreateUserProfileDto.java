package com.marketplace.controller.userprofile;

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
}

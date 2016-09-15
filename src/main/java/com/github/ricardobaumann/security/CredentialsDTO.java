package com.github.ricardobaumann.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {

   private String grantType;
   
   private String username;
   
   private String password;
   
   private String clientId;
}

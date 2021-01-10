package com.bayneno.backen_manage_property.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

  @Size(max = 50)
  private String firstName;
  @Size(max = 50)
  private String lastName;
  private String nickName;
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  private String zone;

}

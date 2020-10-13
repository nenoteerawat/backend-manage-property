package com.bayneno.backen_manage_property.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "action_log")
public class ActionLog {

  @Id
  private String id;

  @NotBlank
  private String status;

  private String comment;

  private ZonedDateTime actionDateTime;

  @DBRef
  private Lead lead;
  @DBRef
  private Listing listing;
  @DBRef
  private User sale;
  @DBRef
  private User createdBy;
  private ZonedDateTime createdDateTime;

}

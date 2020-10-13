package com.bayneno.backen_manage_property.payload.response;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
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
public class ActionLogResponse {

  private String id;

  private String status;
  private String done;

  private String comment;

  private String actionDateTime;

  private Lead lead;
  private Listing listing;
  private User sale;
  private User createdBy;
  private String createdDateTime;

}

package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLogRequest {

  private String id;

  private String status;
  private String done;

  private String comment;
  private String actionDateTime;

  private String leadId;
  private String listingId;

}

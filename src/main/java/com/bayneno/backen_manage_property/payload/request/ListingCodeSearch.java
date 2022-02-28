package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingCodeSearch {
  private String id;
  private String code;
}

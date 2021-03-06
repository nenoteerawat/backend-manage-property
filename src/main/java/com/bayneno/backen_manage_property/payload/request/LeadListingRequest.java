package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadListingRequest {

    private String value;
    private String text;
    private String building;
    private String propertyType;
    private String area = "0";
    private String floor;
    private String bed;
    private String toilet;
    private String direction;
    private String zone;
    private String notes;
}

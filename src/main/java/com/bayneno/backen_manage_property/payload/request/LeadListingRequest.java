package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeadListingRequest {

    private String value;
    private String text;
    private String propertyType;
    private String area;
    private String floor;
    private String direction;
    private String notes;
}

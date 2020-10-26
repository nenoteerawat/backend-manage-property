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
    private String building;
    private String propertyType;
    private String area = "0";
    private String floor;
    private String bed;
    private String toilet;
    private String direction;
    private String notes;
}

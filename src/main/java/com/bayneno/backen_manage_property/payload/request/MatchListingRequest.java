package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchListingRequest {
    private String building;
    private String propertyType;
    private String toilet;
    private String bed;
    private Double area;
    private String floor;
    private Double price;
    private String direction;
    private List<String> scenery = new ArrayList<>();
    private String priceMin;
    private String priceMax;
    private String zone;
}

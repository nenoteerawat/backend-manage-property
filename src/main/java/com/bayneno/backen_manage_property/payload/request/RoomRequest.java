package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    private String projectId;
    private String type;
    private String propertyType;
    private String level;
    private String standard;
    private String grade;
    private String toilet;
    private String bed;
    private Double area;
    private String floor;
    private Double price;
    private String priceRent;
    private String rentDetail;
    private String direction;
    private List<String> position;
    private List<String> scenery;
    private String feature;
    private List<String> tags;
    private String sellDetail;
    private String description;
    private String remark;
    private Boolean exclusive;
    private String exclusiveDate;
}

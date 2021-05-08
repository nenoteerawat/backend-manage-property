package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    private String projectId;
    private String building;
    private String type;
    private String propertyType;
    private String level;
    private String standard;
    private String grade;
    private String toilet;
    private String bed;
    private String roomType;
    private Double area;
    private String floor;
    private Double price;
    private Double priceDown;
    private Double priceRent;
    private String rentDetail;
    private String direction;
    @Builder.Default
    private List<String> position = new ArrayList<>();
    private String scenery;
    private String feature;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    private String sellDetail;
	private String tax;
	private String businessTax;
	private String transferFee;
    private String description;
    private String remark;
    private Boolean exclusive;
    private String exclusiveDate;
}

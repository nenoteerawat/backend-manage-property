package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    private String projectId;
    private String type;
    private String level;
    private String standard;
    private String grade;
    private String toilet;
    private String bed;
    private String area;
    private String floor;
    private String price;
    private String direction;
    private List<String> position;
    private List<String> scenery;
    private String feature;
    private List<String> tags;
    private String description;
    private String remark;
    private String exclusive;
    private String exclusiveDate;
}

package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.request.BuildingRequest;
import com.bayneno.backen_manage_property.payload.request.TransportRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "project")
public class Project {
    @Id
    private String id;
    private String type;
    private String name;
    private List<BuildingRequest> buildings;
    private String address;
    private String district;
    private String amphoe;
    private String province;
    private String zipcode;
    private String createdBy;
    private String createdDateTime;
    private String updatedBy;
    private String updatedDateTime;
    private List<String> facilities;
    private List<TransportRequest> transports;
}

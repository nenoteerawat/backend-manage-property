package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.request.BuildingRequest;
import com.bayneno.backen_manage_property.payload.request.TransportRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    @Builder.Default
    private List<BuildingRequest> buildings = new ArrayList<>();
    private String address;
    private String district;
    private String amphoe;
    private String province;
    private String zipcode;
    private String zone;
    private String team;
    @DBRef
    private User createdBy;
    private ZonedDateTime createdDateTime;
    @DBRef
    private User updatedBy;
    private ZonedDateTime updatedDateTime;
    @Builder.Default
    private List<String> facilities = new ArrayList<>();
    @Builder.Default
    private List<TransportRequest> transports = new ArrayList<>();
}

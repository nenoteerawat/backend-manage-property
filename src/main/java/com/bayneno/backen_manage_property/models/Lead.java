package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.request.OwnerRequest;
import com.bayneno.backen_manage_property.payload.request.RoomRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "lead")
public class Lead {
    @Id
    private String id;
    private OwnerRequest owner;
    private RoomRequest room;
    private List<String> fileIds;
    private String createdBy;
    private String createdDateTime;
    private String updatedBy;
    private String updatedDateTime;
}

package com.bayneno.backen_manage_property.payload.response;

import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.OwnerRequest;
import com.bayneno.backen_manage_property.payload.request.RoomRequest;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingResponse {
    @Id
    private String id;
    private OwnerRequest owner;
    private RoomRequest room;
    private List<FileResponse> files;
    private List<Project> projects;
    private String createdBy;
    private String createdDateTime;
    private String updatedBy;
    private String updatedDateTime;
}

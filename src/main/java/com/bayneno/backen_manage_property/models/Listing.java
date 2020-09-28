package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.request.OwnerRequest;
import com.bayneno.backen_manage_property.payload.request.RoomRequest;
import com.bayneno.backen_manage_property.payload.response.FileResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "listing")
public class Listing {
    @Id
    private String id;
    private OwnerRequest owner;
    private RoomRequest room;
    private List<FileResponse> files;
    @DBRef
    private User createdBy;
    private ZonedDateTime createdDateTime;
    @DBRef
    private User updatedBy;
    private ZonedDateTime updatedDateTime;

    private String saleUser;
}

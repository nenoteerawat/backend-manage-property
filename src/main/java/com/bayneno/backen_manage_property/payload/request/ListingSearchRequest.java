package com.bayneno.backen_manage_property.payload.request;

import com.bayneno.backen_manage_property.models.Role;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListingSearchRequest {

    private String id;

    private OwnerRequest ownerRequest;

    private RoomRequest roomRequest;

    private List<FileResponse> files;

    private User user;
}

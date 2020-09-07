package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeadSearchRequest {

    private String id;

    private OwnerRequest ownerRequest;

    private RoomRequest roomRequest;

    private List<String> fileIds;

    private String username;
}

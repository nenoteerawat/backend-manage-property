package com.bayneno.backen_manage_property.payload.response;

import com.bayneno.backen_manage_property.payload.request.BuildingRequest;
import com.bayneno.backen_manage_property.payload.request.TransportRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
	private String id;
	private String type;
	private String name;
	private String floor;
	private String building;
	private String develop;
	private String address;
	private String district;
	private String amphoe;
	private String province;
	private String zipcode;
	private List<String> facilities;
	private List<TransportRequest> transports;
	private List<BuildingRequest> buildings;

}

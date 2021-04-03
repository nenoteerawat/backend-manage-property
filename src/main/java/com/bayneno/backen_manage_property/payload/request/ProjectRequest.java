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
public class ProjectRequest {
	private String id;
	@NotBlank
	private String type;
	@NotBlank
	private String name;
	private List<BuildingRequest> buildings;
	@NotBlank
	private String address;
	private String district;
	private String amphoe;
	private String province;
	private String zipcode;
	private String zone;
	private String team;
	private List<String> facilities;
	private List<TransportRequest> transports;
	private String comment;
}

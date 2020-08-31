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
	@NotBlank
	private String floor;
	@NotBlank
	private String building;
	@NotBlank
	private String developer;
	@NotBlank
	private String address;
	@NotBlank
	private String district;
	@NotBlank
	private String amphoe;
	@NotBlank
	private String province;
	@NotBlank
	private String zipcode;
	@NotBlank
	private String username;
	private List<String> facilities;
	private List<TransportRequest> transports;
	
}

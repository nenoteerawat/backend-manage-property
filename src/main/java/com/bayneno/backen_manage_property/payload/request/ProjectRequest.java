package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
//	@NotBlank
	private String type;
//	@NotBlank
	private String name;
//	@NotBlank
	private String floor;
//	@NotBlank
	private String building;
//	@NotBlank
	private String developer;
}

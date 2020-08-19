package com.bayneno.backen_manage_property.payload.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
	private String token;
	@Builder.Default
	private String type = "Bearer";
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private List<String> roles;
}

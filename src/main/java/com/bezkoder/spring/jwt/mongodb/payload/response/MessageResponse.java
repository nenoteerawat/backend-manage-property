package com.bezkoder.spring.jwt.mongodb.payload.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
	private String message;
}

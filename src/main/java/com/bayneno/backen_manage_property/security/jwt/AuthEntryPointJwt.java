package com.bayneno.backen_manage_property.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		if (authException instanceof UsernameNotFoundException) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error: Username not found");
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
		}
	}

}

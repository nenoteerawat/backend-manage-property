package com.bayneno.backen_manage_property.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.bayneno.backen_manage_property.properties.AwsProp;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/test")
public class TestController {

	private AmazonS3 s3client;
	private AwsProp awsProp;

	public TestController(AmazonS3 s3client, AwsProp awsProp) {
		this.s3client = s3client;
		this.awsProp = awsProp;
	}

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@PostMapping("/upload")
	public String upload(@RequestParam("files") MultipartFile[] files) throws IOException {

		PutObjectResult putObjectResult = s3client.putObject(awsProp.getBucketName(), files[0].getOriginalFilename(), files[0].getInputStream(), new ObjectMetadata());

		return "SUCCESS";
	}
}

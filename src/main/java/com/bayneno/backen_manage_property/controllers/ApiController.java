package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/project/create")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest) {
        projectRequest.getBuilding();
        return ResponseEntity.ok("");
    }
}

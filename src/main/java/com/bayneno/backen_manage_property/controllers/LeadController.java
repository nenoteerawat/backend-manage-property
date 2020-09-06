package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LeadController {

    @Autowired
    LeadService leadService;

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest) {

        String projectId = leadService.createLead(leadRequest);
        return ResponseEntity.ok(projectId);
    }

//    @PostMapping("/project/edit")
//    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
//    public ResponseEntity<?> projectEdit(@Valid @RequestBody ProjectRequest projectRequest) {
//
//        //validate project name
//        String projectId = projectService.editProject(projectRequest);
//        return ResponseEntity.ok(projectId);
//    }

}

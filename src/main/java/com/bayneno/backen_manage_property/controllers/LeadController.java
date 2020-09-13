package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.response.LeadResponse;
import com.bayneno.backen_manage_property.payload.request.LeadSearchRequest;
import com.bayneno.backen_manage_property.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LeadController {

    @Autowired
    LeadService leadService;

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest) {

        String leadId = leadService.createLead(leadRequest);
        return ResponseEntity.ok(leadId);
    }

    @PostMapping("/lead/list")
    public ResponseEntity<?> leadList(@Valid @RequestBody LeadSearchRequest leadSearchRequest) {

        //validate project name
        List<LeadResponse> lead = leadService.getLead(leadSearchRequest);

        return ResponseEntity.ok(lead);
    }

    @PostMapping("/lead/edit")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> leadEdit(@Valid @RequestBody LeadRequest leadRequest) {

        //validate project name
        String leadId = leadService.editLead(leadRequest);
        return ResponseEntity.ok(leadId);
    }

}

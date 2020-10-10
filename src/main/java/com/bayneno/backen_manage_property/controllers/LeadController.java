package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class LeadController {

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeadService leadService;

    @GetMapping("/lead/selects")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadSelectList() {

            List<Lead> leads = leadRepository.findAll();

        return ResponseEntity.ok(leads);
    }

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest request, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);

        String leadId = leadService.createLead(leadRequest, createdByUser);

        return ResponseEntity.ok(leadId);
    }
}

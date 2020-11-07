package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.response.ActionLogResponse;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/lead/list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadList(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Lead> leads;
        if(user.getRoles().stream().iterator().next().getName().equals(ERole.ROLE_SALE)) {
            leads = leadRepository.findAllBySaleUserId(user.getId());
        } else {
            leads = leadRepository.findAll();
        }
        leads = leads.stream().sorted(Comparator.comparing(Lead::getUpdatedDateTime).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/lead/get")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadGet(@RequestParam String id) {

        Lead lead = (leadRepository.findById(id).orElse(null));

        return ResponseEntity.ok(lead);
    }

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest request, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);

        String leadId = leadService.createLead(leadRequest, createdByUser);

        return ResponseEntity.ok(leadId);
    }

    @PostMapping("/lead/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadEdit(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest request, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);

        String leadId = leadService.editLead(leadRequest, createdByUser);

        return ResponseEntity.ok(leadId);
    }

    @GetMapping("/lead/delete")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingDelete(@RequestParam String id, HttpServletRequest httpServletRequest, Principal principal) {
        Lead lead = leadRepository.findById(id).orElse(null);
        leadRepository.delete(lead);
        return ResponseEntity.ok("delete success");
    }
}

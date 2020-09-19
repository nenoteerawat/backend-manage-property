package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ChangeLogType;
import com.bayneno.backen_manage_property.enums.ChangeSubmitType;
import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.request.LeadSearchRequest;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.LeadResponse;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.ChangeServiceImpl;
import com.bayneno.backen_manage_property.services.LeadService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LeadController {

    private final LeadService leadService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    public LeadController(LeadService leadService
            , ChangeServiceImpl changeService
            , UserRepository userRepository) {
        this.leadService = leadService;
        this.changeService = changeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest request, Principal principal) {
        String leadId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.ADD.name())
                    .username(principal.getName())
                    .type(ChangeLogType.LEAD.name())
                    .toValue(Lead.builder()
                            .owner(leadRequest.getOwnerRequest())
                            .room(leadRequest.getRoomRequest())
                            .files(leadRequest.getFiles())
                            .createdBy(createdByUser)
                            .createdDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            leadId = leadService.createLead(leadRequest, createdByUser);
        }
        assert leadId != null;
        return ResponseEntity.ok(leadId);
    }

    @PostMapping("/lead/list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadList(@Valid @RequestBody LeadSearchRequest leadSearchRequest) {

        //validate project name
        List<LeadResponse> lead = leadService.getLead(leadSearchRequest);

        return ResponseEntity.ok(lead);
    }

    @PostMapping("/lead/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadEdit(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest request, Principal principal) {
        String leadId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.EDIT.name())
                    .username(principal.getName())
                    .type(ChangeLogType.LEAD.name())
                    .toValue(Lead.builder()
                            .owner(leadRequest.getOwnerRequest())
                            .room(leadRequest.getRoomRequest())
                            .files(leadRequest.getFiles())
                            .updatedBy(updatedByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            //validate project name
            leadId = leadService.editLead(leadRequest, updatedByUser);
        }
        assert leadId != null;
        return ResponseEntity.ok(leadId);
    }

}

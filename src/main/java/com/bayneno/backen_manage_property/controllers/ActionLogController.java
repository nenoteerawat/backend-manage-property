package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.ActionLog;
import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ActionLogRequest;
import com.bayneno.backen_manage_property.payload.response.ActionLogResponse;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.LeadService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ActionLogController {

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeadService leadService;

    @Autowired
    ActionLogRepository actionLogRepository;

    @Autowired
    ListingRepository listingRepository;

    @PostMapping("/actionLog/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> ActionLogCreate(@Valid @RequestBody ActionLogRequest actionLogRequest, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        Lead lead = null;
        Listing listing = null;
        if(actionLogRequest.getLeadId() != null && !actionLogRequest.getLeadId().isEmpty())
            lead = leadRepository.findById(actionLogRequest.getLeadId()).orElse(null);
        if(actionLogRequest.getListingId() != null && !actionLogRequest.getListingId().isEmpty())
            listing = listingRepository.findById(actionLogRequest.getListingId()).orElse(null);

        ZonedDateTime actionDateTime = ZonedDateTimeUtil.stringToZonedDateTime(actionLogRequest.getActionDateTime()
                , ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ
                , ZonedDateTimeUtil.bangkokAsiaZoneId);

        String done = actionLogRequest.getDone().equals("1")? "PENDING" : "COMPLETED";
        ActionLog actionLog = actionLogRepository.save(ActionLog.builder()
                .status(actionLogRequest.getStatus())
                .comment(actionLogRequest.getComment())
                .actionDateTime(actionDateTime)
                .done(done)
                .lead(lead)
                .sale(createdByUser)
                .listing(listing)
                .createdBy(createdByUser)
                .createdDateTime(ZonedDateTimeUtil.now())
                .build()
        );

        ActionLogResponse actionLogResponses =
                ActionLogResponse.builder()
                        .id(actionLog.getId())
                        .status(actionLog.getStatus())
                        .comment(actionLog.getComment())
                        .done(actionLog.getDone())
                        .actionDateTime(ZonedDateTimeUtil.zonedDateTimeToString(
                                actionLog.getActionDateTime()
                                , ZonedDateTimeUtil.DDMMYYHHMMSS
                                , ZonedDateTimeUtil.bangkokAsiaZoneId)
                        )
                        .build();

        return ResponseEntity.ok(actionLogResponses);
    }

    @PostMapping("/actionLog/updateStatus")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> ActionLogUpdateStatus(@Valid @RequestBody ActionLogRequest actionLogRequest, Principal principal) {
        ActionLog actionLog = actionLogRepository.findById(actionLogRequest.getId()).orElse(null);
        actionLog.setDone("COMPLETED");
        actionLogRepository.save(actionLog);
        return ResponseEntity.ok(actionLog);
    }

    @GetMapping("/actionLog/list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> actionLogList(Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);

        List<ActionLog> actionLogList = actionLogRepository.findAllBySaleIdOrderByActionDateTimeDesc(createdByUser.getId());

        List<ActionLogResponse> actionLogResponses = actionLogList.stream().map( actionLog ->
                ActionLogResponse.builder()
                .id(actionLog.getId())
                .status(actionLog.getStatus())
                .comment(actionLog.getComment())
                .done(actionLog.getDone())
                .actionDateTime(ZonedDateTimeUtil.zonedDateTimeToString(
                    actionLog.getActionDateTime()
                    , ZonedDateTimeUtil.DDMMYYHHMMSS
                    , ZonedDateTimeUtil.bangkokAsiaZoneId)
                )
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(actionLogResponses);
    }


}

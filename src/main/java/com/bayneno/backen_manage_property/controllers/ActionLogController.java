package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.ActionLog;
import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ActionLogRequest;
import com.bayneno.backen_manage_property.payload.response.ActionLogResponse;
import com.bayneno.backen_manage_property.payload.response.actionlog.ShowCalendarResponse;
import com.bayneno.backen_manage_property.properties.ActionLogCalendar;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.LeadService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ActionLogController {

    private final LeadRepository leadRepository;

    private final UserRepository userRepository;

    private final LeadService leadService;

    private final ActionLogRepository actionLogRepository;

    private final ListingRepository listingRepository;

    private ActionLogCalendar actionLogCalendar;

    public ActionLogController(LeadRepository leadRepository
            , UserRepository userRepository
            , LeadService leadService
            , ActionLogRepository actionLogRepository
            , ListingRepository listingRepository
            , ActionLogCalendar actionLogCalendar) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.leadService = leadService;
        this.actionLogRepository = actionLogRepository;
        this.listingRepository = listingRepository;
        this.actionLogCalendar = actionLogCalendar;
    }

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
                , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID);

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
                                , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID)
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
    public ResponseEntity<?> actionLogList(String leadId, String type, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);

        List<ActionLog> actionLogList;
        if("daily".equals(type)) {
//            actionLogList = actionLogRepository.findAllByActionDateTimeAndCreatedByIdOrderByActionDateTimeDesc(leadId, createdByUser.getId());
            actionLogList = actionLogRepository.findAll();
        } else {
            actionLogList = actionLogRepository.findAllByLeadIdAndCreatedByIdOrderByActionDateTimeDesc(leadId, createdByUser.getId());
        }

        List<ActionLogResponse> actionLogResponses = actionLogList.stream().map( actionLog ->
                ActionLogResponse.builder()
                .id(actionLog.getId())
                .status(actionLog.getStatus())
                .comment(actionLog.getComment())
                .done(actionLog.getDone())
                .actionDateTime(ZonedDateTimeUtil.zonedDateTimeToString(
                    actionLog.getActionDateTime()
                    , ZonedDateTimeUtil.DDMMYYHHMMSS
                    , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID)
                )
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(actionLogResponses);
    }

    @GetMapping("/actionLog/show/calendar")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> showCalendar(Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<ActionLog> actionLogList = actionLogRepository.findAllBySaleIdOrderByActionDateTimeDesc(createdByUser.getId());
        List<ShowCalendarResponse> showCalendarResponses = actionLogList.stream().map(actionLog ->
                ShowCalendarResponse.builder()
                        .id(actionLog.getId())
                        .title(actionLog.getComment())
                        .color(actionLogCalendar.getTypeMapEventColor().getOrDefault(actionLog.getStatus(), ""))
                        .done(actionLog.getDone().equals("PENDING") ? "1" : "2")
                        .start(ZonedDateTimeUtil.zonedDateTimeToString(actionLog.getActionDateTime()
                                , ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(showCalendarResponses);
    }

    @GetMapping("/actionLog/findById/{id}")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> showCalendar(@PathVariable("id") String id, Principal principal) {
        ActionLog actionLog = actionLogRepository.findById(id).orElse(ActionLog.builder().build());
        return ResponseEntity.ok(ActionLogResponse.builder()
                .comment(actionLog.getComment())
                .actionDateTime(ZonedDateTimeUtil.zonedDateTimeToString(actionLog.getActionDateTime()
                        , ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
                .createdBy(actionLog.getCreatedBy())
                .createdDateTime(ZonedDateTimeUtil.zonedDateTimeToString(actionLog.getCreatedDateTime()
                        , ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
                .done(actionLog.getDone().equals("PENDING") ? "1" : "2")
                .id(actionLog.getId())
                .lead(actionLog.getLead())
                .listing(actionLog.getListing())
                .sale(actionLog.getSale())
                .status(actionLog.getStatus())
                .build());
    }

    @PostMapping("/actionLog/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> editActionLog(@Valid @RequestBody ActionLogRequest actionLogRequest, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        Lead lead = null;
        Listing listing = null;
        if(actionLogRequest.getLeadId() != null && !actionLogRequest.getLeadId().isEmpty())
            lead = leadRepository.findById(actionLogRequest.getLeadId()).orElse(null);
        if(actionLogRequest.getListingId() != null && !actionLogRequest.getListingId().isEmpty())
            listing = listingRepository.findById(actionLogRequest.getListingId()).orElse(null);

        ZonedDateTime actionDateTime = ZonedDateTimeUtil.stringToZonedDateTime(actionLogRequest.getActionDateTime()
                , ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ
                , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID);

        String done = actionLogRequest.getDone().equals("1")? "PENDING" : "COMPLETED";
        ActionLog updateActionLog = actionLogRepository.findById(actionLogRequest.getId()).orElse(ActionLog.builder().build());
        updateActionLog.setStatus(actionLogRequest.getStatus());
        updateActionLog.setComment(actionLogRequest.getComment());
        updateActionLog.setActionDateTime(actionDateTime);
        updateActionLog.setDone(done);
        updateActionLog.setLead(lead);
        updateActionLog.setSale(createdByUser);
        updateActionLog.setListing(listing);
        updateActionLog.setCreatedBy(createdByUser);
        updateActionLog.setCreatedDateTime(ZonedDateTimeUtil.now());
        ActionLog actionLog = actionLogRepository.save(updateActionLog);

        ActionLogResponse actionLogResponses =
                ActionLogResponse.builder()
                        .id(actionLog.getId())
                        .status(actionLog.getStatus())
                        .comment(actionLog.getComment())
                        .done(actionLog.getDone())
                        .actionDateTime(ZonedDateTimeUtil.zonedDateTimeToString(
                                actionLog.getActionDateTime()
                                , ZonedDateTimeUtil.DDMMYYHHMMSS
                                , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID)
                        )
                        .build();

        return ResponseEntity.ok(actionLogResponses);
    }
}

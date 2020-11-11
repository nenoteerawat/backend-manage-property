package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.payload.request.*;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.payload.response.ProjectResponse;
import com.bayneno.backen_manage_property.repository.*;
import com.bayneno.backen_manage_property.services.ChangeServiceImpl;
import com.bayneno.backen_manage_property.services.ListingService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bayneno.backen_manage_property.enums.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/listing")
public class ListingController {

    private final ListingService listingService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    private final ListingRepository listingRepository;

    private final ActionLogRepository actionLogRepository;

    private final LeadRepository leadRepository;

    private final ProjectRepository projectRepository;

    public ListingController(ListingService listingService
            , ChangeServiceImpl changeService
            , UserRepository userRepository
            , ListingRepository listingRepository
            , ActionLogRepository actionLogRepository
            , LeadRepository leadRepository,
                             ProjectRepository projectRepository) {
        this.listingService = listingService;
        this.changeService = changeService;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.actionLogRepository = actionLogRepository;
        this.leadRepository = leadRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingCreate(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest httpServletRequest, Principal principal) {
        String listingId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment(listingRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.ADD)
                    .username(principal.getName())
                    .type(ETypeChangeLog.LISTING)
                    .toValue(Listing.builder()
                            .id(listingRequest.getId())
                            .owner(listingRequest.getOwnerRequest())
                            .room(listingRequest.getRoomRequest())
                            .files(listingRequest.getFiles())
                            .createdBy(createdByUser)
                            .createdDateTime(ZonedDateTimeUtil.now())
                            .saleUser(listingRequest.getSaleUser())
                            .build())
                    .build());
        } else {
            listingId = listingService.createListing(listingRequest, createdByUser);
        }
        assert listingId != null;
        return ResponseEntity.ok(listingId);
    }

    @PostMapping("list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingList(@Valid @RequestBody ListingSearchRequest listingSearchRequest, Principal principal) {
        List<ListingResponse> listing = new ArrayList<>();
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if(user != null) {
            listingSearchRequest.setUser(user);
                listing = listingService.getListing(listingSearchRequest).stream()
                        .sorted(Comparator.comparing(ListingResponse::getUpdatedDateTime).reversed())
                        .collect(Collectors.toList());
        }

        return ResponseEntity.ok(listing);
    }

    @GetMapping("getLastCode")
    public ResponseEntity<?> getListingCode(@RequestParam String saleUser) {

        List<Listing> listings = listingRepository.findAllBySaleUser(saleUser);
        String listingCode = "";
        int beforeListingCode = 0;
        if(listings.size() > 0) {
            for (Listing item:listings
            ) {
                String temp = item.getOwner().getListingCode();
                temp = temp.substring(2);
                if(Integer.parseInt(temp) > beforeListingCode) {
                    beforeListingCode = Integer.parseInt(temp);
                    listingCode = item.getOwner().getListingCode().substring(0,2);
                }
            }
            if(beforeListingCode >= 100) {
                listingCode += "0" + (beforeListingCode +1);
            } else if(beforeListingCode >= 10) {
                listingCode += "00" + (beforeListingCode +1);
            } else {
                listingCode += "000" + (beforeListingCode +1);
            }
        } else {
            Optional<User> user = userRepository.findByUsername(saleUser);
            if(user.isPresent()) {
                listingCode = user.get().getFirstName().substring(0,1) + user.get().getNickName().substring(0,1) + "0001";
            }
        }
        return ResponseEntity.ok(listingCode);
    }

    @PostMapping("edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingEdit(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest httpServletRequest, Principal principal) {
        String listingId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .id(listingRequest.getId())
                    .comment(listingRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.EDIT)
                    .username(principal.getName())
                    .type(ETypeChangeLog.LISTING)
                    .toValue(Listing.builder()
                            .id(listingRequest.getId())
                            .owner(listingRequest.getOwnerRequest())
                            .room(listingRequest.getRoomRequest())
                            .files(listingRequest.getFiles())
                            .updatedBy(updatedByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
                            .saleUser(listingRequest.getSaleUser())
                            .build())
                    .build());
        } else {
            //validate project name
            listingId = listingService.editListing(listingRequest, updatedByUser);
        }
        assert listingId != null;
        return ResponseEntity.ok(listingId);
    }

    @PostMapping("delete")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingDelete(@RequestBody ListingRequest listingRequest, HttpServletRequest httpServletRequest, Principal principal) {
        listingRepository.findById(listingRequest.getId()).ifPresent(listing -> {
            User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
            if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
                changeService.submit(SubmitReq.builder()
                        .id(listing.getId())
                        .comment(listingRequest.getComment())
                        .submitType(ESubmitTypeChangeLog.DELETE)
                        .username(principal.getName())
                        .type(ETypeChangeLog.LISTING)
                        .toValue(Listing.builder()
                                .id(listing.getId())
                                .updatedBy(updatedByUser)
                                .updatedDateTime(ZonedDateTimeUtil.now())
                                .build())
                        .build());
            } else {
                //validate project name
                listingRepository.delete(listing);
            }
        });
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/listing/booking")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingBooking(@Valid @RequestBody BookingRequest bookingRequest, HttpServletRequest httpServletRequest, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        Role role = user.getRoles().iterator().next();
        Listing listing = listingRepository.findById(bookingRequest.getListingId()).orElse(null);
        Lead lead = leadRepository.findById(bookingRequest.getLeadId()).orElse(null);
        User sale = user;

        if(role.getName().equals(ERole.ROLE_ADMIN)) {
            sale = userRepository.findByUsername(bookingRequest.getSaleUsername()).orElse(null);
        }
        actionLogRepository.save(
                ActionLog.builder()
                        .status(ETypeActionLog.BOOKING.name())
                        .sale(sale)
                        .lead(lead)
                        .comment("auto comment")
                        .listing(listing)
                        .createdBy(user)
                        .createdDateTime(ZonedDateTimeUtil.now())
                        .build()
        );
        return ResponseEntity.ok("");
    }

    @PostMapping("/match")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> getListingMatch(@RequestBody Listing listing, HttpServletRequest httpServletRequest, Principal principal) {
        List<Listing> listings = listingService.matchListing(listing);
        List<ListingResponse> listingResponses = listings.stream().map(l -> {
            final ListingResponse listingResponse = ListingResponse.builder()
                    .id(l.getId())
                    .room(l.getRoom())
                    .owner(l.getOwner())
                    .files(l.getFiles())
                    .saleUser(l.getSaleUser())
                    .build();

            // Set Create by
            Optional.of(l).map(Listing::getCreatedBy).ifPresent(createBy
                    -> listingResponse.setCreatedBy(createBy.getFirstName() + " " + createBy.getLastName()));
            Optional.of(l).map(Listing::getCreatedDateTime).ifPresent(createDateTime
                    -> listingResponse.setCreatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(createDateTime
                    , ZonedDateTimeUtil.DDMMYYHHMMSS
                    , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID)));

            // set Update by
            Optional.of(l).map(Listing::getUpdatedBy).ifPresent(updateBy
                    -> listingResponse.setUpdatedBy(updateBy.getFirstName() + " " + updateBy.getLastName()));
            Optional.of(l).map(Listing::getUpdatedDateTime).ifPresent(updateDateTime
                    -> listingResponse.setUpdatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(updateDateTime
                    , ZonedDateTimeUtil.DDMMYYHHMMSS
                    , ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID)));


            // Find project by id
            Optional.of(l).map(Listing::getRoom).map(RoomRequest::getProjectId)
                    .flatMap(projectRepository::findById).ifPresent(project
                    -> listingResponse.setProjects(Stream.of(project).collect(Collectors.toList())));
            return listingResponse;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(listingResponses);
    }
}

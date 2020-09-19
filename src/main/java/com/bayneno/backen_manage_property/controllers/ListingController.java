package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ChangeLogType;
import com.bayneno.backen_manage_property.enums.ChangeSubmitType;
import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ListingRequest;
import com.bayneno.backen_manage_property.payload.request.ListingSearchRequest;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.ChangeServiceImpl;
import com.bayneno.backen_manage_property.services.ListingService;
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
public class ListingController {

    private final ListingService listingService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    public ListingController(ListingService listingService
            , ChangeServiceImpl changeService
            , UserRepository userRepository) {
        this.listingService = listingService;
        this.changeService = changeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/listing/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingCreate(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest request, Principal principal) {
        String listingId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.ADD.name())
                    .username(principal.getName())
                    .type(ChangeLogType.LISTING.name())
                    .toValue(Listing.builder()
                            .owner(listingRequest.getOwnerRequest())
                            .room(listingRequest.getRoomRequest())
                            .files(listingRequest.getFiles())
                            .createdBy(createdByUser)
                            .createdDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            listingId = listingService.createListing(listingRequest, createdByUser);
        }
        assert listingId != null;
        return ResponseEntity.ok(listingId);
    }

    @PostMapping("/listing/list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingList(@Valid @RequestBody ListingSearchRequest listingSearchRequest) {

        //validate project name
        List<ListingResponse> listing = listingService.getListing(listingSearchRequest);

        return ResponseEntity.ok(listing);
    }

    @PostMapping("/listing/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingEdit(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest request, Principal principal) {
        String listingId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.EDIT.name())
                    .username(principal.getName())
                    .type(ChangeLogType.LISTING.name())
                    .toValue(Listing.builder()
                            .owner(listingRequest.getOwnerRequest())
                            .room(listingRequest.getRoomRequest())
                            .files(listingRequest.getFiles())
                            .updatedBy(updatedByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            //validate project name
            listingId = listingService.editListing(listingRequest, updatedByUser);
        }
        assert listingId != null;
        return ResponseEntity.ok(listingId);
    }

}

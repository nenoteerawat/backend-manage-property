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
import com.bayneno.backen_manage_property.repository.ListingRepository;
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
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ListingController {

    private final ListingService listingService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    private final ListingRepository listingRepository;

    public ListingController(ListingService listingService
            , ChangeServiceImpl changeService
            , UserRepository userRepository
            , ListingRepository listingRepository
    ) {
        this.listingService = listingService;
        this.changeService = changeService;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    @PostMapping("/listing/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingCreate(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest request, Principal principal) {
        String listingId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment(listingRequest.getComment())
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

    @GetMapping("/listing/getLastCode")
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

    @PostMapping("/listing/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingEdit(@Valid @RequestBody ListingRequest listingRequest, HttpServletRequest request, Principal principal) {
        String listingId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .comment(listingRequest.getComment())
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

    @PostMapping("/listing/delete")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> listingDelete(@RequestParam String id) {

        Optional<Listing> listing = listingRepository.findById(id);
        listingRepository.delete(listing.get());
        return ResponseEntity.ok("delete success");
    }
}

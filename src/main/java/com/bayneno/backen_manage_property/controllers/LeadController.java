package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.enums.ESubmitTypeChangeLog;
import com.bayneno.backen_manage_property.enums.ETypeChangeLog;
import com.bayneno.backen_manage_property.models.ActionLog;
import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadDocumentRequest;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.response.ActionLogResponse;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.FileResponse;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class LeadController {

    private final LeadRepository leadRepository;

    private final UserRepository userRepository;

    private final LeadService leadService;

    private final ChangeServiceImpl changeService;

    private final ListingRepository listingRepository;

    private final ActionLogRepository actionLogRepository;

    public LeadController(LeadRepository leadRepository
            , UserRepository userRepository
            , LeadService leadService
            , ChangeServiceImpl changeService
            , ListingRepository listingRepository
            , ActionLogRepository actionLogRepository) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.leadService = leadService;
        this.changeService = changeService;
        this.listingRepository = listingRepository;
        this.actionLogRepository = actionLogRepository;
    }

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

        for (Lead lead: leads
             ) {
            List<ActionLog> actionLogs = actionLogRepository.findAllByLeadIdAndDoneOrderByActionDateTimeDesc(lead.getId(), "COMPLETED");
            if(actionLogs.size() > 0)
                lead.setStatus(actionLogs.get(actionLogs.size()-1).getStatus());
            else
                lead.setStatus("0");
        }

        leads = leads.stream().sorted(Comparator.comparing(Lead::getUpdatedDateTime).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/lead/get")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadGet(@RequestParam String id) {

        Lead lead = (leadRepository.findById(id).orElse(null));

        if(lead != null) {
            int test = lead.hashCode();
            test = test;
        }

        return ResponseEntity.ok(lead);
    }

    @PostMapping("/lead/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadCreate(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest httpServletRequest, Principal principal) {
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        String leadId = "";
        Listing listingByAdmin = null;
        Listing listingByLead = null;
        Listing listingBySale = null;
        Listing listingLifeStyleBySale = null;
        if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            if(!"0".equals(leadRequest.getListingByAdmin().getValue()) && !"-1".equals(leadRequest.getListingByAdmin().getValue()))
                listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingByLead().getValue()) && !"-1".equals(leadRequest.getListingByLead().getValue()))
                listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingBySale().getValue()) && !"-1".equals(leadRequest.getListingBySale().getValue()))
                listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingLifeStyleBySale().getValue()) && !"-1".equals(leadRequest.getListingLifeStyleBySale().getValue()))
                listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);
            Lead lead = Lead.builder()
                    .painPoints(leadRequest.getPainPoints())
                    .painSales(leadRequest.getPaintSales())
                    .grade(leadRequest.getGrade().toUpperCase())
                    .priceMin(leadRequest.getPriceMin())
                    .priceMax(leadRequest.getPriceMax())
                    .typeBuy(leadRequest.isTypeBuy())
                    .typeRent(leadRequest.isTypeRent())
                    .firstName(leadRequest.getFirstName())
                    .lastName(leadRequest.getLastName())
                    .nickName(leadRequest.getNickName())
                    .email(leadRequest.getEmail())
                    .line(leadRequest.getLine())
                    .phone(leadRequest.getPhone())
                    .job(leadRequest.getJob())
                    .reason(leadRequest.getReason())
                    .address(leadRequest.getAddress())
                    .district(leadRequest.getDistrict())
                    .amphoe(leadRequest.getAmphoe())
                    .province(leadRequest.getProvince())
                    .zipcode(leadRequest.getZipcode())
                    .nationality(leadRequest.getNationality())
                    .career(leadRequest.getCareer())
                    .yearOfWork(leadRequest.getYearOfWork())
                    .incomeType(leadRequest.getIncomeType())
                    .averageIncome(leadRequest.getAverageIncome())
                    .liabilities(leadRequest.getLiabilities())
                    .billingAmount(leadRequest.getBillingAmount())
                    .preApprove(leadRequest.getPreApprove())
                    .status(leadRequest.getStatus())
                    .difficulty(leadRequest.getDifficulty())
                    .rapport(leadRequest.getRapport())
                    .listingByAdmin(listingByAdmin)
                    .buildingListingByAdmin(leadRequest.getListingByAdmin().getBuilding())
                    .propertyTypeListingByAdmin(leadRequest.getListingByAdmin().getPropertyType())
                    .toiletListingByAdmin(leadRequest.getListingByAdmin().getToilet())
                    .bedListingByAdmin(leadRequest.getListingByAdmin().getBed())
                    .areaListingByAdmin(Double.parseDouble(leadRequest.getListingByAdmin().getArea()))
                    .floorListingByAdmin(leadRequest.getListingByAdmin().getFloor())
                    .directionListingByAdmin(leadRequest.getListingByAdmin().getDirection())
                    .zoneListingByAdmin(leadRequest.getListingByAdmin().getZone())
                    .listingByAdminNotes(leadRequest.getListingByAdmin().getNotes())
                    .listingByLead(listingByLead)
                    .buildingListingByLead(leadRequest.getListingByLead().getBuilding())
                    .propertyTypeListingByLead(leadRequest.getListingByLead().getPropertyType())
                    .toiletListingByLead(leadRequest.getListingByLead().getToilet())
                    .bedListingByLead(leadRequest.getListingByLead().getBed())
                    .areaListingByLead(Double.parseDouble(leadRequest.getListingByLead().getArea()))
                    .floorListingByLead(leadRequest.getListingByLead().getFloor())
                    .directionListingByLead(leadRequest.getListingByLead().getDirection())
                    .zoneListingByLead(leadRequest.getListingByLead().getZone())
                    .listingByLeadNotes(leadRequest.getListingByLead().getNotes())
                    .listingBySale(listingBySale)
                    .buildingListingBySale(leadRequest.getListingBySale().getBuilding())
                    .propertyTypeListingBySale(leadRequest.getListingBySale().getPropertyType())
                    .toiletListingBySale(leadRequest.getListingBySale().getToilet())
                    .bedListingBySale(leadRequest.getListingBySale().getBed())
                    .areaListingBySale(Double.parseDouble(leadRequest.getListingBySale().getArea()))
                    .floorListingBySale(leadRequest.getListingBySale().getFloor())
                    .directionListingBySale(leadRequest.getListingBySale().getDirection())
                    .zoneListingBySale(leadRequest.getListingBySale().getZone())
                    .listingBySaleNotes(leadRequest.getListingBySale().getNotes())
                    .listingLifeStyleBySale(listingLifeStyleBySale)
                    .buildingListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBuilding())
                    .propertyTypeListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getPropertyType())
                    .toiletListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getToilet())
                    .bedListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBed())
                    .areaListingLifeStyleBySale(Double.parseDouble(leadRequest.getListingLifeStyleBySale().getArea()))
                    .floorListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getFloor())
                    .directionListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getDirection())
                    .zoneListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getZone())
                    .listingLifeStyleBySaleNotes(leadRequest.getListingLifeStyleBySale().getNotes())
                    .condition(leadRequest.getCondition())
                    .contract(leadRequest.getContract())
                    .typePay(leadRequest.getTypePay())
                    .createdBy(createdByUser)
                    .createdDateTime(ZonedDateTimeUtil.now())
                    .updatedBy(createdByUser)
                    .updatedDateTime(ZonedDateTimeUtil.now())
                    .saleUser(userRepository.findByUsername(leadRequest.getSaleUser()).orElse(null))
                    .build();
            lead.setInfo(leadService.calculateFillFieldValuePercentage(lead));
            changeService.submit(SubmitReq.builder()
                    .comment(leadRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.ADD)
                    .username(principal.getName())
                    .type(ETypeChangeLog.LEAD)
                    .toValue(lead)
                    .build());
        } else {
            leadId = leadService.createLead(leadRequest, createdByUser);
        }

        return ResponseEntity.ok(leadId);
    }

    @PostMapping("/lead/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadEdit(@Valid @RequestBody LeadRequest leadRequest, HttpServletRequest httpServletRequest, Principal principal) {
        User updateByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        String leadId = "";
        Listing listingByAdmin = null;
        Listing listingByLead = null;
        Listing listingBySale = null;
        Listing listingLifeStyleBySale = null;
        if (httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            if(!"0".equals(leadRequest.getListingByAdmin().getValue()) && !"-1".equals(leadRequest.getListingByAdmin().getValue()))
                listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingByLead().getValue()) && !"-1".equals(leadRequest.getListingByLead().getValue()))
                listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingBySale().getValue()) && !"-1".equals(leadRequest.getListingBySale().getValue()))
                listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
            if(!"0".equals(leadRequest.getListingLifeStyleBySale().getValue()) && !"-1".equals(leadRequest.getListingLifeStyleBySale().getValue()))
                listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);
            Lead lead = Lead.builder()
                    .id(leadRequest.getId())
                    .painPoints(leadRequest.getPainPoints())
                    .painSales(leadRequest.getPaintSales())
                    .grade(leadRequest.getGrade().toUpperCase())
                    .priceMin(leadRequest.getPriceMin())
                    .priceMax(leadRequest.getPriceMax())
                    .typeBuy(leadRequest.isTypeBuy())
                    .typeRent(leadRequest.isTypeRent())
                    .firstName(leadRequest.getFirstName())
                    .lastName(leadRequest.getLastName())
                    .nickName(leadRequest.getNickName())
                    .email(leadRequest.getEmail())
                    .line(leadRequest.getLine())
                    .phone(leadRequest.getPhone())
                    .job(leadRequest.getJob())
                    .reason(leadRequest.getReason())
                    .address(leadRequest.getAddress())
                    .district(leadRequest.getDistrict())
                    .amphoe(leadRequest.getAmphoe())
                    .province(leadRequest.getProvince())
                    .zipcode(leadRequest.getZipcode())
                    .nationality(leadRequest.getNationality())
                    .career(leadRequest.getCareer())
                    .yearOfWork(leadRequest.getYearOfWork())
                    .incomeType(leadRequest.getIncomeType())
                    .averageIncome(leadRequest.getAverageIncome())
                    .liabilities(leadRequest.getLiabilities())
                    .billingAmount(leadRequest.getBillingAmount())
                    .preApprove(leadRequest.getPreApprove())
                    .status(leadRequest.getStatus())
                    .difficulty(leadRequest.getDifficulty())
                    .rapport(leadRequest.getRapport())
                    .listingByAdmin(listingByAdmin)
                    .buildingListingByAdmin(leadRequest.getListingByAdmin().getBuilding())
                    .propertyTypeListingByAdmin(leadRequest.getListingByAdmin().getPropertyType())
                    .toiletListingByAdmin(leadRequest.getListingByAdmin().getToilet())
                    .bedListingByAdmin(leadRequest.getListingByAdmin().getBed())
                    .areaListingByAdmin(Double.parseDouble(leadRequest.getListingByAdmin().getArea()))
                    .floorListingByAdmin(leadRequest.getListingByAdmin().getFloor())
                    .directionListingByAdmin(leadRequest.getListingByAdmin().getDirection())
                    .listingByAdminNotes(leadRequest.getListingByAdmin().getNotes())
                    .listingByLead(listingByLead)
                    .buildingListingByLead(leadRequest.getListingByLead().getBuilding())
                    .propertyTypeListingByLead(leadRequest.getListingByLead().getPropertyType())
                    .toiletListingByLead(leadRequest.getListingByLead().getToilet())
                    .bedListingByLead(leadRequest.getListingByLead().getBed())
                    .areaListingByLead(Double.parseDouble(leadRequest.getListingByLead().getArea()))
                    .floorListingByLead(leadRequest.getListingByLead().getFloor())
                    .directionListingByLead(leadRequest.getListingByLead().getDirection())
                    .listingByLeadNotes(leadRequest.getListingByLead().getNotes())
                    .listingBySale(listingBySale)
                    .buildingListingBySale(leadRequest.getListingBySale().getBuilding())
                    .propertyTypeListingBySale(leadRequest.getListingBySale().getPropertyType())
                    .toiletListingBySale(leadRequest.getListingBySale().getToilet())
                    .bedListingBySale(leadRequest.getListingBySale().getBed())
                    .areaListingBySale(Double.parseDouble(leadRequest.getListingBySale().getArea()))
                    .floorListingBySale(leadRequest.getListingBySale().getFloor())
                    .directionListingBySale(leadRequest.getListingBySale().getDirection())
                    .listingBySaleNotes(leadRequest.getListingBySale().getNotes())
                    .listingLifeStyleBySale(listingLifeStyleBySale)
                    .buildingListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBuilding())
                    .propertyTypeListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getPropertyType())
                    .toiletListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getToilet())
                    .bedListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBed())
                    .areaListingLifeStyleBySale(Double.parseDouble(leadRequest.getListingLifeStyleBySale().getArea()))
                    .floorListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getFloor())
                    .directionListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getDirection())
                    .listingLifeStyleBySaleNotes(leadRequest.getListingLifeStyleBySale().getNotes())
                    .condition(leadRequest.getCondition())
                    .contract(leadRequest.getContract())
                    .typePay(leadRequest.getTypePay())
                    .updatedBy(updateByUser)
                    .updatedDateTime(ZonedDateTimeUtil.now())
                    .saleUser(userRepository.findByUsername(leadRequest.getSaleUser()).orElse(null))
                    .build();
            lead.setInfo(leadService.calculateFillFieldValuePercentage(lead));
            changeService.submit(SubmitReq.builder()
                    .id(leadRequest.getId())
                    .comment(leadRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.EDIT)
                    .username(principal.getName())
                    .type(ETypeChangeLog.LEAD)
                    .toValue(lead)
                    .build());
        } else {
            leadId = leadService.editLead(leadRequest, updateByUser);
        }

        return ResponseEntity.ok(leadId);
    }

    @PostMapping("/lead/delete")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadDelete(@RequestBody LeadRequest leadRequest, HttpServletRequest httpServletRequest, Principal principal) {
        Lead lead = leadRepository.findById(leadRequest.getId()).orElse(null);
        User updateByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if (httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .id(leadRequest.getId())
                    .comment(leadRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.DELETE)
                    .username(principal.getName())
                    .type(ETypeChangeLog.LEAD)
                    .toValue(Lead.builder()
                            .updatedBy(updateByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            leadRepository.delete(lead);
        }
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/lead/saveDocument")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadSaveDocument(@RequestBody LeadDocumentRequest leadDocumentRequest, Principal principal) {
        Lead lead = leadRepository.findById(leadDocumentRequest.getLeadId()).orElse(null);
        if(lead != null) {
            switch (leadDocumentRequest.getType()) {
                case "LEASE":
                    lead.setLeasePDF(leadDocumentRequest.getData());
                    break;
                case "SELL":
                    lead.setSellPDF(leadDocumentRequest.getData());
                    break;
                case "AGENT_AGREEMENT":
                    lead.setAgentAgreementPDF(leadDocumentRequest.getData());
                    break;
                case "EXCLUSIVE":
                    lead.setExclusivePDF(leadDocumentRequest.getData());
                    break;
                case "CO_BROKE":
                    lead.setCoBrokePDF(leadDocumentRequest.getData());
                    break;
            }
        }
        assert lead != null;
        leadRepository.save(lead);
        return ResponseEntity.ok("upload book success");
    }

    @PostMapping("/lead/saveBook")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadSaveBook(@RequestBody LeadRequest leadRequest, Principal principal) {
        Lead lead = leadRepository.findById(leadRequest.getId()).orElse(null);
        if(lead != null) {
            if(lead.getBooks() == null) {
                List<FileResponse> fileResponses = new ArrayList<>();
                fileResponses.add(leadRequest.getBook());
                lead.setBooks(fileResponses);
            } else {
                lead.getBooks().add(leadRequest.getBook());
            }
        }
        leadRepository.save(lead);
        return ResponseEntity.ok("upload book success");
    }

    @PostMapping("/lead/deleteBook")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> leadDeleteBook(@RequestBody LeadRequest leadRequest, Principal principal) {
        Lead lead = leadRepository.findById(leadRequest.getId()).orElse(null);
        if(lead != null) {
            List<FileResponse> fileResponses = lead.getBooks().stream()
                    .filter(book -> !book.getId().equals(leadRequest.getBook().getId()))
                    .collect(Collectors.toList());
            lead.setBooks(fileResponses);
            leadRepository.save(lead);
        }
        return ResponseEntity.ok("deleted book success");
    }
}

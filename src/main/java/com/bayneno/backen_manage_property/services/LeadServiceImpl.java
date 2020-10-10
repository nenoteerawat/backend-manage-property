package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.enums.EQuery;
import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.request.ListingRequest;
import com.bayneno.backen_manage_property.payload.request.ListingSearchRequest;
import com.bayneno.backen_manage_property.payload.request.RoomSearchRequest;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LeadServiceImpl implements LeadService {

	private final ListingRepository listingRepository;

	private final LeadRepository leadRepository;

	private final ActionLogRepository actionLogRepository;

	public LeadServiceImpl(ListingRepository listingRepository
			, ProjectRepository projectRepository
						   ,LeadRepository leadRepository
			, ActionLogRepository actionLogRepository) {
		this.listingRepository = listingRepository;
		this.leadRepository = leadRepository;
		this.actionLogRepository = actionLogRepository;
	}

	@Override
	public String createLead(LeadRequest leadRequest, User user) {

		Listing listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
		Listing listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
		Listing listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
		Lead lead = leadRepository.save(
				Lead
				.builder()
				.painPoints(leadRequest.getPainPoints())
				.grade(leadRequest.getGrade())
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
				.listingByAdmin(listingByAdmin)
				.listingByAdminNotes(leadRequest.getListingByAdmin().getNotes())
				.listingByLead(listingByLead)
				.listingByLeadNotes(leadRequest.getListingByLead().getNotes())
				.listingBySale(listingBySale)
				.listingBySaleNotes(leadRequest.getListingBySale().getNotes())
				.createdBy(user)
				.createdDateTime(ZonedDateTimeUtil.now())
				.updatedBy(null)
				.updatedDateTime(null)
				.build()
		);

		return lead.getId();
	}
}

package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

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
		Listing listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);
		Lead lead = leadRepository.save(
				Lead
				.builder()
				.painPoints(leadRequest.getPainPoints())
				.painSales(leadRequest.getPaintSales())
				.grade(leadRequest.getGrade().toUpperCase())
				.price(leadRequest.getPrice())
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
				.listingByAdminNotes(leadRequest.getListingByAdmin().getNotes())
				.listingByLead(listingByLead)
				.listingByLeadNotes(leadRequest.getListingByLead().getNotes())
				.listingBySale(listingBySale)
				.listingBySaleNotes(leadRequest.getListingBySale().getNotes())
				.listingLifeStyleBySale(listingLifeStyleBySale)
				.listingLifeStyleBySaleNotes(leadRequest.getListingLifeStyleBySale().getNotes())
				.condition(leadRequest.getCondition())
				.contract(leadRequest.getContract())
				.typePay(leadRequest.getTypePay())
				.createdBy(user)
				.createdDateTime(ZonedDateTimeUtil.now())
				.updatedBy(null)
				.updatedDateTime(null)
				.build()
		);

		return lead.getId();
	}

	@Override
	public String editLead(LeadRequest leadRequest, User user) {
		Listing listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
		Listing listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
		Listing listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
		Listing listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);

		Lead lead = leadRepository.findById(leadRequest.getId()).orElse(null);
		if(lead != null) {
			lead.setPainPoints(leadRequest.getPainPoints());
			lead.setPainSales(leadRequest.getPaintSales());
			lead.setGrade(leadRequest.getGrade().toUpperCase());
			lead.setPrice(leadRequest.getPrice());
			lead.setTypeBuy(leadRequest.isTypeBuy());
			lead.setTypeRent(leadRequest.isTypeRent());
			lead.setFirstName(leadRequest.getFirstName());
			lead.setLastName(leadRequest.getLastName());
			lead.setNickName(leadRequest.getNickName());
			lead.setEmail(leadRequest.getEmail());
			lead.setLine(leadRequest.getLine());
			lead.setPhone(leadRequest.getPhone());
			lead.setJob(leadRequest.getJob());
			lead.setReason(leadRequest.getReason());
			lead.setAddress(leadRequest.getAddress());
			lead.setDistrict(leadRequest.getDistrict());
			lead.setAmphoe(leadRequest.getAmphoe());
			lead.setProvince(leadRequest.getProvince());
			lead.setZipcode(leadRequest.getZipcode());
			lead.setNationality(leadRequest.getNationality());
			lead.setCareer(leadRequest.getCareer());
			lead.setYearOfWork(leadRequest.getYearOfWork());
			lead.setIncomeType(leadRequest.getIncomeType());
			lead.setAverageIncome(leadRequest.getAverageIncome());
			lead.setLiabilities(leadRequest.getLiabilities());
			lead.setBillingAmount(leadRequest.getBillingAmount());
			lead.setPreApprove(leadRequest.getPreApprove());
			lead.setStatus(leadRequest.getStatus());
			lead.setDifficulty(leadRequest.getDifficulty());
			lead.setRapport(leadRequest.getRapport());
			lead.setListingByAdmin(listingByAdmin);
			lead.setListingByAdminNotes(leadRequest.getListingByAdmin().getNotes());
			lead.setListingByLead(listingByLead);
			lead.setListingByLeadNotes(leadRequest.getListingByLead().getNotes());
			lead.setListingBySale(listingBySale);
			lead.setListingBySaleNotes(leadRequest.getListingBySale().getNotes());
			lead.setListingLifeStyleBySale(listingLifeStyleBySale);
			lead.setListingLifeStyleBySaleNotes(leadRequest.getListingLifeStyleBySale().getNotes());
			lead.setCondition(leadRequest.getCondition());
			lead.setContract(leadRequest.getContract());
			lead.setTypePay(leadRequest.getTypePay());
			lead.setUpdatedBy(user);
			lead.setUpdatedDateTime(ZonedDateTimeUtil.now());
			leadRepository.save(lead);
		}

		return lead.getId();
	}
}

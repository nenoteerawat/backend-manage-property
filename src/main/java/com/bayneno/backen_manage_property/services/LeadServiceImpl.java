package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.repository.*;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class LeadServiceImpl implements LeadService {

	private final ListingRepository listingRepository;

	private final LeadRepository leadRepository;

	private final ActionLogRepository actionLogRepository;

	private final UserRepository userRepository;

	public LeadServiceImpl(ListingRepository listingRepository
			, ProjectRepository projectRepository
		   ,LeadRepository leadRepository
			, ActionLogRepository actionLogRepository
			, UserRepository userRepository) {
		this.listingRepository = listingRepository;
		this.leadRepository = leadRepository;
		this.actionLogRepository = actionLogRepository;
		this.userRepository = userRepository;
	}

	@Override
	public String createLead(LeadRequest leadRequest, User user) {

		Listing listingByAdmin = null;
		Listing listingByLead = null;
		Listing listingBySale = null;
		Listing listingLifeStyleBySale = null;

		User sale = userRepository.findByUsername(leadRequest.getSaleUser()).orElse(null);

		if(!"0".equals(leadRequest.getListingByAdmin().getValue()) && !"-1".equals(leadRequest.getListingByAdmin().getValue()))
			listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingByLead().getValue()) && !"-1".equals(leadRequest.getListingByLead().getValue()))
			listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingBySale().getValue()) && !"-1".equals(leadRequest.getListingBySale().getValue()))
			listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingLifeStyleBySale().getValue()) && !"-1".equals(leadRequest.getListingLifeStyleBySale().getValue()))
			listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);
		Lead lead = Lead
				.builder()
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
				.createdBy(user)
				.createdDateTime(ZonedDateTimeUtil.now())
				.updatedBy(user)
				.updatedDateTime(ZonedDateTimeUtil.now())
				.saleUser(sale)
				.file(leadRequest.getFile())
				.build();
		lead.setInfo(calculateFillFieldValuePercentage(lead));
		Lead leadSaved = leadRepository.save(lead);

		return leadSaved.getId();
	}

	@Override
	public String editLead(LeadRequest leadRequest, User user) {

		Listing listingByAdmin = null;
		Listing listingByLead = null;
		Listing listingBySale = null;
		Listing listingLifeStyleBySale = null;

		User sale = userRepository.findByUsername(leadRequest.getSaleUser()).orElse(null);

		if(!"0".equals(leadRequest.getListingByAdmin().getValue()) && !"-1".equals(leadRequest.getListingByAdmin().getValue()))
			listingByAdmin = listingRepository.findById(leadRequest.getListingByAdmin().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingByLead().getValue()) && !"-1".equals(leadRequest.getListingByLead().getValue()))
			listingByLead = listingRepository.findById(leadRequest.getListingByLead().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingBySale().getValue()) && !"-1".equals(leadRequest.getListingBySale().getValue()))
			listingBySale = listingRepository.findById(leadRequest.getListingBySale().getValue()).orElse(null);
		if(!"0".equals(leadRequest.getListingLifeStyleBySale().getValue()) && !"-1".equals(leadRequest.getListingLifeStyleBySale().getValue()))
			listingLifeStyleBySale = listingRepository.findById(leadRequest.getListingLifeStyleBySale().getValue()).orElse(null);

		Lead lead = leadRepository.findById(leadRequest.getId()).orElse(null);
		if(lead != null) {
			lead.setPainPoints(leadRequest.getPainPoints());
			lead.setPainSales(leadRequest.getPaintSales());
			lead.setGrade(leadRequest.getGrade().toUpperCase());
			lead.setPriceMin(leadRequest.getPriceMin());
			lead.setPriceMax(leadRequest.getPriceMax());
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
			lead.setBuildingListingByAdmin(leadRequest.getListingByAdmin().getBuilding());
			lead.setPropertyTypeListingByAdmin(leadRequest.getListingByAdmin().getPropertyType());
			lead.setToiletListingByAdmin(leadRequest.getListingByAdmin().getToilet());
			lead.setBedListingByAdmin(leadRequest.getListingByAdmin().getBed());
			lead.setAreaListingByAdmin(Double.parseDouble(leadRequest.getListingByAdmin().getArea()));
			lead.setFloorListingByAdmin(leadRequest.getListingByAdmin().getFloor());
			lead.setDirectionListingByAdmin(leadRequest.getListingByAdmin().getDirection());
			lead.setListingByAdminNotes(leadRequest.getListingByAdmin().getNotes());
			lead.setListingByLead(listingByLead);
			lead.setBuildingListingByLead(leadRequest.getListingByLead().getBuilding());
			lead.setPropertyTypeListingByLead(leadRequest.getListingByLead().getPropertyType());
			lead.setToiletListingByLead(leadRequest.getListingByLead().getToilet());
			lead.setBedListingByLead(leadRequest.getListingByLead().getBed());
			lead.setAreaListingByLead(Double.parseDouble(leadRequest.getListingByLead().getArea()));
			lead.setFloorListingByLead(leadRequest.getListingByLead().getFloor());
			lead.setDirectionListingByLead(leadRequest.getListingByLead().getDirection());
			lead.setListingByLeadNotes(leadRequest.getListingByLead().getNotes());
			lead.setListingBySale(listingBySale);
			lead.setBuildingListingBySale(leadRequest.getListingBySale().getBuilding());
			lead.setPropertyTypeListingBySale(leadRequest.getListingBySale().getPropertyType());
			lead.setToiletListingBySale(leadRequest.getListingBySale().getToilet());
			lead.setBedListingBySale(leadRequest.getListingBySale().getBed());
			lead.setAreaListingBySale(Double.parseDouble(leadRequest.getListingBySale().getArea()));
			lead.setFloorListingBySale(leadRequest.getListingBySale().getFloor());
			lead.setDirectionListingBySale(leadRequest.getListingBySale().getDirection());
			lead.setListingBySaleNotes(leadRequest.getListingBySale().getNotes());
			lead.setListingLifeStyleBySale(listingLifeStyleBySale);
			lead.setBuildingListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBuilding());
			lead.setPropertyTypeListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getPropertyType());
			lead.setToiletListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getToilet());
			lead.setBedListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getBed());
			lead.setAreaListingLifeStyleBySale(Double.parseDouble(leadRequest.getListingLifeStyleBySale().getArea()));
			lead.setFloorListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getFloor());
			lead.setDirectionListingLifeStyleBySale(leadRequest.getListingLifeStyleBySale().getDirection());
			lead.setListingLifeStyleBySaleNotes(leadRequest.getListingLifeStyleBySale().getNotes());
			lead.setCondition(leadRequest.getCondition());
			lead.setContract(leadRequest.getContract());
			lead.setTypePay(leadRequest.getTypePay());
			lead.setUpdatedBy(user);
			lead.setUpdatedDateTime(ZonedDateTimeUtil.now());
			lead.setSaleUser(sale);
			lead.setFile(leadRequest.getFile());
			lead.setInfo(calculateFillFieldValuePercentage(lead));
			leadRepository.save(lead);
		}

		return lead.getId();
	}

	@Override
	public int calculateFillFieldValuePercentage(Lead lead) {
		Field[] fields = Lead.class.getDeclaredFields();
		int unFillField = 0;
		int totalField = fields.length;
		for(Field field : fields){
			try {
				if(field.isAnnotationPresent(NonInfo.class)) {
					field.setAccessible(true);
					Object fieldValue = field.get(lead);
					if (fieldValue instanceof String) {
						String fieldValueString = (String) fieldValue;
						if (fieldValueString.equalsIgnoreCase("")) {
							unFillField++;
						}
					} else if (fieldValue instanceof Integer) {
						int fieldValueInteger = (int) fieldValue;
						if (fieldValueInteger == 0) {
							unFillField++;
						}
					} else {
						if (fieldValue == null) {
							unFillField++;
						}
					}
					field.setAccessible(false);
				} else {
					totalField--;
				}
			} catch (IllegalAccessException e){

			}
		}
		return unFillField * 100 / totalField;
	}
}

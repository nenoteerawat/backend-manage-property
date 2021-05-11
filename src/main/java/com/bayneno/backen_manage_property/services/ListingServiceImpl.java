package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.enums.EQuery;
import com.bayneno.backen_manage_property.models.ActionLog;
import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.*;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.payload.response.xml.*;
import com.bayneno.backen_manage_property.properties.DDPropertiesParameters;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ListingServiceImpl implements ListingService {

	private final ListingRepository listingRepository;

	private final ProjectRepository projectRepository;

	private final MongoTemplate mongoTemplate;

	private final ActionLogRepository actionLogRepository;

	private final DDPropertiesParameters ddPropertiesParameters;

	public ListingServiceImpl(ListingRepository listingRepository
			, ProjectRepository projectRepository
			, MongoTemplate mongoTemplate
			, ActionLogRepository actionLogRepository
			, DDPropertiesParameters ddPropertiesParameters) {
		this.listingRepository = listingRepository;
		this.projectRepository = projectRepository;
		this.mongoTemplate = mongoTemplate;
		this.actionLogRepository = actionLogRepository;
		this.ddPropertiesParameters = ddPropertiesParameters;
	}

	@Override
	public String createListing(ListingRequest listingRequest, User user) {
		Listing listing = listingRepository.save(
				Listing
						.builder()
						.owner(listingRequest.getOwnerRequest())
						.room(listingRequest.getRoomRequest())
						.files(listingRequest.getFiles())
						.createdBy(user)
						.createdDateTime(ZonedDateTimeUtil.now())
						.updatedBy(user)
						.updatedDateTime(ZonedDateTimeUtil.now())
						.saleUser(listingRequest.getSaleUser())
						.build()
		);
		return listing.getId();
	}

	@Override
	public List<ListingResponse> getListing(ListingSearchRequest listingSearchRequest) {
		List<ListingResponse> listings = new ArrayList<>();

		if(!StringUtils.isEmpty(listingSearchRequest.getId())) {
			Optional<Listing> listing = listingRepository.findById(listingSearchRequest.getId());
			if(listing.isPresent()) {
				List<Project> projects = new ArrayList<>();
				Optional<Project> project = projectRepository.findById(listing.get().getRoom().getProjectId());
				project.ifPresent(projects::add);

				ListingResponse listingResponse = ListingResponse.builder()
						.owner(listing.map(Listing::getOwner).orElse(null))
						.room(listing.map(Listing::getRoom).orElse(null))
						.createdBy(listing.map(Listing::getCreatedBy).map(User::getFirstName).orElse("")
								+ " "
								+ listing.map(Listing::getCreatedBy).map(User::getLastName).orElse(""))
						.createdDateTime(ZonedDateTimeUtil.zonedDateTimeToString(listing.map(Listing::getCreatedDateTime).orElse(null)
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
						.updatedBy(listing.map(Listing::getUpdatedBy).map(User::getFirstName).orElse("")
								+ " "
								+ listing.map(Listing::getUpdatedBy).map(User::getLastName).orElse(""))
						.updatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(listing.map(Listing::getUpdatedDateTime).orElse(null)
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
						.projects(projects)
						.files(listing.map(Listing::getFiles).orElse(new ArrayList<>()))
						.id(listing.map(Listing::getId).orElse(null))
						.flag(listing.map(Listing::isFlag).orElse(null))
						.saleUser(listing.map(Listing::getSaleUser).orElse(""))
						.build();
				listings.add(listingResponse);
				return listings;
			}
			else {
				return new ArrayList<>();
			}
		}
		List<Listing> listingModels = queryListing(listingSearchRequest).stream()
				.sorted(Comparator.comparing(Listing::getUpdatedDateTime).reversed())
				.collect(Collectors.toList());
		if (listingModels.size() > 0) {
			List<String> projectIds = listingModels.stream().map(listing -> listing.getRoom().getProjectId()).collect(Collectors.toList());
			List<Project> projects = projectRepository.findByIdIn(projectIds);
			listings = listingModels
					.stream()
					.map(listing -> {
						List<ActionLog> actionLogs = actionLogRepository.findByListingId(listing.getId());
						String status = "";
						if(actionLogs.size() > 0)
							status = actionLogs.stream()
									.sorted(Comparator.comparing(ActionLog::getCreatedDateTime).reversed())
									.collect(Collectors.toList())
									.get(0)
									.getStatus();
						return ListingResponse
								.builder()
								.id(listing.getId())
								.owner(listing.getOwner())
								.room(listing.getRoom())
								.projects(projects
										.stream()
										.filter(project -> project.getId().equals(listing.getRoom().getProjectId()))
										.collect(Collectors.toList()))
								.files(listing.getFiles())
								.createdBy(listing.getCreatedBy().getFirstName() + " " + listing.getCreatedBy().getLastName())
								.createdDateTime(ZonedDateTimeUtil.zonedDateTimeToString(listing.getCreatedDateTime()
										, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
								.updatedBy(listing.getUpdatedBy().getFirstName() + " " + listing.getCreatedBy().getLastName())
								.updatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(listing.getUpdatedDateTime()
										, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID))
								.saleUser(listing.getSaleUser())
								.status(status)
								.flag(listing.isFlag())
								.build();
							}

					)
					.collect(Collectors.toList());
		}
		return listings;

	}

	@Override
	public String editListing(ListingRequest listingRequest, User user) {
		Optional<Listing> listing = listingRepository.findById(listingRequest.getId());
		if(listing.isPresent()) {
			listing.get().setOwner(listingRequest.getOwnerRequest());
			listing.get().setRoom(listingRequest.getRoomRequest());
			listing.get().setFiles(listingRequest.getFiles());
			listing.get().setUpdatedBy(user);
			listing.get().setUpdatedDateTime(ZonedDateTimeUtil.now());
			listing.get().setSaleUser(listingRequest.getSaleUser());
			listingRepository.save(listing.get());
			return listing.get().getId();
		}
		return "Not Found";
	}

	public List<Listing> queryListing(ListingSearchRequest criteria){
		Query query = new Query();

		// User criteria
//		Optional.of(criteria).map(ListingSearchRequest::getUser)
//				.filter(user -> {
//					boolean isAdmin = false;
//					for (Role role: user.getRoles()) {
//						if(role.getName() == ERole.ROLE_ADMIN
//								|| role.getName() == ERole.ROLE_SALE_MANAGER
//								|| role.getName() == ERole.ROLE_MANAGER) {
//							isAdmin = true;
//							break;
//						}
//					}
//					return !isAdmin;
//				})
//				.map(User::getUsername).ifPresent(username -> addQueryIsIfNotEmpty(query, "saleUser", username, EQuery.IS));

		// Sale user criteria
		Optional.of(criteria).map(ListingSearchRequest::getSaleUser).ifPresent(saleUser -> addQueryIsIfNotEmpty(query, "saleUser", saleUser, EQuery.IS));

		// Id criteria
		Optional.of(criteria).map(ListingSearchRequest::getId).filter(id -> !StringUtils.isEmpty(id))
				.ifPresent(id -> addQueryIsIfNotEmpty(query, "id", id, EQuery.IS));

		// Room criteria
		Optional.of(criteria).map(ListingSearchRequest::getRoomSearchRequest).ifPresent(room -> {
			Optional.of(room).map(RoomSearchRequest::getProjectId).ifPresent(projectId
					-> addQueryIsIfNotEmpty(query, "room.projectId", projectId, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getType).ifPresent(type
					-> addQueryIsIfNotEmpty(query, "room.type", type, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getBed).ifPresent(bed
					-> addQueryIsIfNotEmpty(query, "room.bed", bed, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getRoomType).ifPresent(roomType
					-> addQueryIsIfNotEmpty(query, "room.roomType", roomType, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getMateRoom).ifPresent(mateRoom
					-> addQueryIsIfNotEmpty(query, "room.mateRoom", mateRoom, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getStorageRoom).ifPresent(storageRoom
					-> addQueryIsIfNotEmpty(query, "room.storageRoom", storageRoom, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getToilet).ifPresent(toilet
					-> addQueryIsIfNotEmpty(query, "room.toilet", toilet, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getPrice).ifPresent(price
					-> addQueryIsIfNotEmpty(query, "room.price", price, EQuery.BETWEEN, 1));
			Optional.of(room).map(RoomSearchRequest::getArea).ifPresent(area
					-> addQueryIsIfNotEmpty(query, "room.area", area, EQuery.BETWEEN, 1));
		});

		// Search criteria
		Optional.of(criteria).map(ListingSearchRequest::getSearch).ifPresent(search
				-> addQueryIsIfNotEmpty(query, "owner.name", search, EQuery.LIKE));

		if(StringUtils.isEmpty(Optional.of(criteria).map(ListingSearchRequest::getRoomSearchRequest)
				.map(RoomSearchRequest::getProjectId).orElse(""))) {

			// search project
			Query projectQuery = new Query();
			Optional.of(criteria).map(ListingSearchRequest::getTransportType).ifPresent(tranType
					-> addQueryIsIfNotEmpty(projectQuery, "transports.type", tranType, EQuery.IS));
			Optional.of(criteria).map(ListingSearchRequest::getTransportName).ifPresent(tranName
					-> addQueryIsIfNotEmpty(projectQuery, "transports.name", tranName, EQuery.IS));
			List<Project> projects = mongoTemplate.find(projectQuery, Project.class);
			List<String> projectIds = projects.stream().map(Project::getId).collect(Collectors.toList());

			query.addCriteria(Criteria.where("room.projectId").in(projectIds));
		}

		return mongoTemplate.find(query, Listing.class);
	}

	public List<Listing> matchListing(MatchListingRequest matchListingRequest){
		final Query listingQuery = new Query();
		addQueryIsIfNotEmpty(listingQuery, "room.building", matchListingRequest.getBuilding(), EQuery.LIKE);
		addQueryIsIfNotEmpty(listingQuery, "room.propertyType", matchListingRequest.getPropertyType(), EQuery.LIKE);
		if(matchListingRequest.getArea() != null)
			listingQuery.addCriteria(Criteria.where("room.area").is(matchListingRequest.getArea()));
		addQueryIsIfNotEmpty(listingQuery, "room.floor", matchListingRequest.getPropertyType(), EQuery.IS);
		addQueryIsIfNotEmpty(listingQuery, "room.toilet", matchListingRequest.getToilet(), EQuery.IS);
		addQueryIsIfNotEmpty(listingQuery, "room.direction", matchListingRequest.getToilet(), EQuery.IS);
		if(null != matchListingRequest.getScenery() && matchListingRequest.getScenery().size() > 0)
			listingQuery.addCriteria(Criteria.where("room.scenery").in(matchListingRequest.getScenery()));
		if(null != matchListingRequest.getPriceMin() && null != matchListingRequest.getPriceMax())
			addQueryIsIfNotEmpty(listingQuery, "room.price", matchListingRequest.getPriceMin() + "," + matchListingRequest.getPriceMax(), EQuery.BETWEEN);
		List<Listing> listings = mongoTemplate.find(listingQuery, Listing.class);

		final Query projectQuery = new Query();
		addQueryIsIfNotEmpty(projectQuery, "zone", matchListingRequest.getZone(), EQuery.IS);
		List<Project> projects = mongoTemplate.find(projectQuery, Project.class);
		final Set<String> projectId = projects.stream().map(Project::getId).collect(Collectors.toSet());
		return listings.stream().filter(listing -> projectId.contains(Optional.ofNullable(listing.getRoom())
				.map(RoomRequest::getProjectId)
				.orElse(""))).collect(Collectors.toList());
	}

	public void addQueryIsIfNotEmpty(Query query, String searchKey, String searchValue, EQuery eQuery){
		this.addQueryIsIfNotEmpty(query, new String[]{searchKey}, new String[]{searchValue}, eQuery, 1);
	}
	public void addQueryIsIfNotEmpty(Query query, String searchKey, String searchValue, EQuery eQuery, double multipleValue){
		this.addQueryIsIfNotEmpty(query, new String[]{searchKey}, new String[]{searchValue}, eQuery, multipleValue);
	}
	public void addQueryIsIfNotEmpty(Query query, String[] searchKey, String[] searchValue, EQuery eQuery, double multipleValue){
		boolean haveValueSearch = false;
		int i = 0;
		int loopLength = Math.min(searchKey.length, searchValue.length);
		while(i < loopLength){
			if (!StringUtils.isEmpty(searchValue[i])) {
				haveValueSearch = true;
				break;
			}
			i++;
		}
		if(haveValueSearch) {
			switch (eQuery){
				case IS: query.addCriteria(Criteria.where(searchKey[0]).is(searchValue[0])); break;
				case BETWEEN:
					String[] split = searchValue[0].split(",");

					// For empty search case price and area with send to back end with ["", ""]
					if(split.length < 2) {
						break;
					}
					double upper = Double.MAX_VALUE;
					double lower = Double.MIN_VALUE;
					try {
						upper = Double.parseDouble(split[1]);
					} catch (Exception e) {

					}
					try {
						lower = Double.parseDouble(split[0]);
					} catch (Exception e){

					}
					if(upper <= lower)
						upper = Double.MAX_VALUE;
					if(upper > 0 && lower > 0 && upper > lower)
						query.addCriteria(Criteria.where(searchKey[0]).gte(lower * multipleValue).lte(upper * multipleValue));
					break;
				case LIKE:
					i = 1;
					Criteria criteria = Criteria.where(searchKey[0]).regex(".*" + searchValue[0] + ".*");
					while (i < loopLength ) {
						i++;
						criteria.orOperator(Criteria.where(searchKey[i]).regex(".*" + searchValue[i] + ".*"));
					}
					query.addCriteria(criteria);
					break;
			}
		}
	}
	@Override
	public List<ListingResponse> getListingByAppointment(String leadId, User user) {
		List<ListingResponse> listingResponses = new ArrayList<>();
		List<ActionLog> actionLogList = actionLogRepository.findByLeadIdAndStatus(leadId, "5");
		if(actionLogList.size() > 0) {
			for (ActionLog actionLog: actionLogList) {
				if(actionLog.getListing() != null) {
					Project project = projectRepository.findById(actionLog.getListing().getRoom().getProjectId()).orElse(null);
					List<Project> projectList = new ArrayList<>();
					projectList.add(project);
					listingResponses.add(ListingResponse.builder()
					.id(actionLog.getListing().getId())
					.owner(actionLog.getListing().getOwner())
					.projects(projectList)
					.build()
					);
				}
			}
//			listingResponses = listingRepository.findAllBySaleUser()
		}

		return listingResponses;
	}

	@Override
	public List<PropertyXml> findPublish() {

		List<Listing> listings = listingRepository.findAllByFlag(true);
		Map<String, String> facingMaster = ddPropertiesParameters.getFacing();
		Map<String, String> amenitiesMaster = ddPropertiesParameters.getAmenities();
		Map<String, String> furnishingMaster = ddPropertiesParameters.getFurnishing();

		return listings.stream().map(l ->
			{
				String listingType;
				String listingTypeTH;
				String listingTypeEN;
				Project project = projectRepository.findById(l.getRoom().getProjectId()).orElse(null);
				if(l.getRoom().getType().equals("2")) {
					listingType = "RENT";
					listingTypeTH = "เช่า";
					listingTypeEN = "Rent";
				} else {
					listingType = "SALE";
					listingTypeTH = "ขาย";
					listingTypeEN = "Sale";
				}
				List<BuildingRequest> buildingRequestList = project.getBuildings().stream().filter(p -> p.getBuilding().equals(l.getRoom().getBuilding())).collect(Collectors.toList());
				String facing = facingMaster.get(l.getRoom().getDirection());
				String amenities = project.getFacilities()
						.stream()
						.filter(f -> amenitiesMaster.containsKey(f.replace(" ","")))
						.map(f -> amenitiesMaster.get(f.replace(" ","")))
						.collect(Collectors.toList())
						.toString().replace("[", "").replace("]","");
				String furnishing = furnishingMaster.getOrDefault(l.getRoom().getFeature(), "");

			return PropertyXml.builder()
				.refNo(l.getOwner().getListingCode())
				.location(LocationXml.builder()
					.streetNumber("")
					.regionCode("TH10")
					.streetName("")
					.propertyTypeGroup("N")
					.propertyId(l.getId())
					.propertyName(project.getName() + " ( ตึก " + l.getRoom().getBuilding() + ")")
					.postCode(project.getZipcode())
					.longitude("")
					.latitude("")
					.districtCode("TH1006")
					.areaCode("TH100608")
					.build()
				)
				.details(DetailsXml.builder()
					.title(listingTypeTH+" "+project.getName())
					.titleEn(listingTypeEN+" "+project.getName())
					.description(l.getRoom().getDescription())
					.descriptionEn(l.getRoom().getDescription())
					.features("")
					.amenities(amenities)
					.priceDetails(PriceDetailsXml.builder()
						.priceUnit("")
						.price(Optional.of(l.getRoom()).map(RoomRequest::getPrice).map(price -> Double.toString(price)).orElse("")) // do not include comma “,”
						.priceType("BAH")
						.priceDescription("")
						.currencyCode("THB")
						.build()
					)
					.room(RoomXml.builder()
						.numBedrooms(l.getRoom().getBed())
						.numBathrooms(l.getRoom().getToilet())
						.extraRooms("")
						.build()
					)
					.furnishing(furnishing)
					.sizeDetails(SizeDetailsXml.builder()
						.floorArea(Optional.ofNullable(l.getRoom()).map(RoomRequest::getArea).map(area -> Double.toString(area)).orElse(""))
						.landArea("")
						.floorSizeY("")
						.floorSizeX("")
						.landSizeX("")
						.langSizeY("")
						.build()
					)
					.parkingSpaces("")
					.numberOfFloors(buildingRequestList.get(0).getFloor())
					.floorLevel(l.getRoom().getFloor())
					.facing(facing)
					.build()
				)
				.listingType(listingType) // SALE or RENT
				.agentId("")
				.customPhone("")
				.customName("")
				.customMobile("")
				.tenure("")
				.sold("NO")
				.status("ACTIVE")
				.photo(l.getFiles().stream().map(f -> PhotoXml.builder()
					.pictureUrl(f.getPath())
					.pictureCaption("")
					.build()).collect(Collectors.toList())
				)
				.build();
			}
		).collect(Collectors.toList());
	}
}

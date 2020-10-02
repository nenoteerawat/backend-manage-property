package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.enums.EQuery;
import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.payload.request.ListingRequest;
import com.bayneno.backen_manage_property.payload.request.ListingSearchRequest;
import com.bayneno.backen_manage_property.payload.request.RoomRequest;
import com.bayneno.backen_manage_property.payload.request.RoomSearchRequest;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.repository.ActionLogRepository;
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
public class ListingServiceImpl implements ListingService {

	private final ListingRepository listingRepository;

	private final ProjectRepository projectRepository;

	private final MongoTemplate mongoTemplate;

	private final ActionLogRepository actionLogRepository;

	public ListingServiceImpl(ListingRepository listingRepository
			, ProjectRepository projectRepository
			, MongoTemplate mongoTemplate
			, ActionLogRepository actionLogRepository) {
		this.listingRepository = listingRepository;
		this.projectRepository = projectRepository;
		this.mongoTemplate = mongoTemplate;
		this.actionLogRepository = actionLogRepository;
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
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
						.updatedBy(listing.map(Listing::getUpdatedBy).map(User::getFirstName).orElse("")
								+ " "
								+ listing.map(Listing::getUpdatedBy).map(User::getLastName).orElse(""))
						.updatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(listing.map(Listing::getUpdatedDateTime).orElse(null)
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
						.projects(projects)
						.files(listing.map(Listing::getFiles).orElse(new ArrayList<>()))
						.id(listing.map(Listing::getId).orElse(null))
						.saleUser(listing.map(Listing::getSaleUser).orElse(""))
						.build();
				listings.add(listingResponse);
				return listings;
			}
			else {
				return new ArrayList<>();
			}
		}
		List<Listing> listingModels = queryListing(listingSearchRequest);
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
										, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
								.saleUser(listing.getSaleUser())
								.status(status)
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
			Optional.of(room).map(RoomSearchRequest::getProjectId).ifPresent(projectId -> addQueryIsIfNotEmpty(query, "room.projectId", projectId, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getType).ifPresent(type -> addQueryIsIfNotEmpty(query, "room.type", type, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getBed).ifPresent(bed -> addQueryIsIfNotEmpty(query, "room.bed", bed, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getToilet).ifPresent(toilet -> addQueryIsIfNotEmpty(query, "room.toilet", toilet, EQuery.IS));
			Optional.of(room).map(RoomSearchRequest::getPrice).ifPresent(price -> addQueryIsIfNotEmpty(query, "room.price", price, EQuery.BETWEEN, 10000));
			Optional.of(room).map(RoomSearchRequest::getArea).ifPresent(area-> addQueryIsIfNotEmpty(query, "room.area", area, EQuery.BETWEEN, 3));
		});
		return mongoTemplate.find(query, Listing.class);
	}

	public void addQueryIsIfNotEmpty(Query query, String searchKey, String searchValue, EQuery eQuery){
		this.addQueryIsIfNotEmpty(query, searchKey, searchValue, eQuery, 0);
	}
	public void addQueryIsIfNotEmpty(Query query, String searchKey, String searchValue, EQuery eQuery, double multipleValue){
		if(!StringUtils.isEmpty(searchValue)) {
			switch (eQuery){
				case IS: query.addCriteria(Criteria.where(searchKey).is(searchValue)); break;
				case BETWEEN:
					String[] split = searchValue.split(",");
					double lower = Double.parseDouble(split[0]);
					double upper = Double.parseDouble(split[1]);
					if(upper > 0 && lower > 0 && upper > lower)
						query.addCriteria(Criteria.where(searchKey).gte(lower * multipleValue).lte(upper * multipleValue));
					break;
			}
		}
	}
}

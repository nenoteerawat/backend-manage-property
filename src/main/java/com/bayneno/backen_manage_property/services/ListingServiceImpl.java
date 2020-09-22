package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ListingRequest;
import com.bayneno.backen_manage_property.payload.request.ListingSearchRequest;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ListingServiceImpl implements ListingService {

	private final ListingRepository listingRepository;

	private final ProjectRepository projectRepository;

	public ListingServiceImpl(ListingRepository listingRepository, ProjectRepository projectRepository) {
		this.listingRepository = listingRepository;
		this.projectRepository = projectRepository;
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
						.build()
		);
		return listing.getId();
	}

	@Override
	public List<ListingResponse> getListing(ListingSearchRequest listingSearchRequest) {
		List<ListingResponse> listings = new ArrayList<>();

		if(listingSearchRequest.getId() != null && !"".equals(listingSearchRequest.getId())) {
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
						.build();
				listings.add(listingResponse);
				return listings;
			}
			else {
				return new ArrayList<>();
			}
		}

		List<Listing> listingModels = listingRepository.findAll();
		List<String> projectIds = listingModels.stream().map(listing -> listing.getRoom().getProjectId()).collect(Collectors.toList());
		List<Project> projects = projectRepository.findByIdIn(projectIds);
		listings = listingModels
				.stream()
				.map(listing -> ListingResponse
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
						.build()
				)
				.collect(Collectors.toList());

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
			listingRepository.save(listing.get());
			return listing.get().getId();
		}
		return "Not Found";
	}
}

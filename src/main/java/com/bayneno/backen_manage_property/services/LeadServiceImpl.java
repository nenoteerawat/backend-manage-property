package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.request.LeadSearchRequest;
import com.bayneno.backen_manage_property.payload.response.LeadResponse;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LeadServiceImpl implements LeadService  {

	private final LeadRepository leadRepository;

	private final ProjectRepository projectRepository;

	public LeadServiceImpl(LeadRepository leadRepository, ProjectRepository projectRepository) {
		this.leadRepository = leadRepository;
		this.projectRepository = projectRepository;
	}

	@Override
	public String createLead(LeadRequest leadRequest, User user) {
		Lead lead = leadRepository.save(
				Lead
						.builder()
						.owner(leadRequest.getOwnerRequest())
						.room(leadRequest.getRoomRequest())
						.files(leadRequest.getFiles())
						.createdBy(user)
						.createdDateTime(ZonedDateTimeUtil.now())
						.build()
		);
		return lead.getId();
	}

	@Override
	public List<LeadResponse> getLead(LeadSearchRequest leadSearchRequest) {
		List<LeadResponse> leads = new ArrayList<>();

		if(leadSearchRequest.getId() != null && !"".equals(leadSearchRequest.getId())) {
			Optional<Lead> lead = leadRepository.findById(leadSearchRequest.getId());
			if(lead.isPresent()) {
				List<Project> projects = new ArrayList<>();
				Optional<Project> project = projectRepository.findById(lead.get().getRoom().getProjectId());
				project.ifPresent(projects::add);

				LeadResponse leadResponse = LeadResponse.builder()
						.owner(lead.map(Lead::getOwner).orElse(null))
						.room(lead.map(Lead::getRoom).orElse(null))
						.createdBy(lead.map(Lead::getCreatedBy).map(User::getFirstName).orElse("")
								+ " "
								+ lead.map(Lead::getCreatedBy).map(User::getLastName).orElse(""))
						.createdDateTime(ZonedDateTimeUtil.zonedDateTimeToString(lead.map(Lead::getCreatedDateTime).orElse(null)
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
						.updatedBy(lead.map(Lead::getUpdatedBy).map(User::getFirstName).orElse("")
								+ " "
								+ lead.map(Lead::getUpdatedBy).map(User::getLastName).orElse(""))
						.updatedDateTime(ZonedDateTimeUtil.zonedDateTimeToString(lead.map(Lead::getUpdatedDateTime).orElse(null)
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
						.projects(projects)
						.files(lead.map(Lead::getFiles).orElse(new ArrayList<>()))
						.id(lead.map(Lead::getId).orElse(null))
						.build();
				leads.add(leadResponse);
				return leads;
			}
			else {
				return new ArrayList<>();
			}
		}

		List<Lead> leadModels = leadRepository.findAll();
		List<String> projectIds = leadModels.stream().map(lead -> lead.getRoom().getProjectId()).collect(Collectors.toList());
		List<Project> projects = projectRepository.findByIdIn(projectIds);
		leads = leadModels
				.stream()
				.map(lead -> LeadResponse
						.builder()
						.id(lead.getId())
						.owner(lead.getOwner())
						.room(lead.getRoom())
						.projects(projects
								.stream()
								.filter(project -> project.getId().equals(lead.getRoom().getProjectId()))
								.collect(Collectors.toList()))
						.files(lead.getFiles())
						.createdBy(lead.getCreatedBy().getFirstName() + " " +lead.getCreatedBy().getLastName())
						.createdDateTime(ZonedDateTimeUtil.zonedDateTimeToString(lead.getCreatedDateTime()
								, ZonedDateTimeUtil.DDMMYYHHMMSS, ZonedDateTimeUtil.bangkokAsiaZoneId))
						.build()
				)
				.collect(Collectors.toList());

		return leads;
	}

	@Override
	public String editLead(LeadRequest leadRequest, User user) {
		Optional<Lead> lead = leadRepository.findById(leadRequest.getId());
		if(lead.isPresent()) {
			lead.get().setOwner(leadRequest.getOwnerRequest());
			lead.get().setRoom(leadRequest.getRoomRequest());
			lead.get().setFiles(leadRequest.getFiles());
			lead.get().setUpdatedBy(user);
			lead.get().setUpdatedDateTime(ZonedDateTimeUtil.now());
			leadRepository.save(lead.get());
			return lead.get().getId();
		}
		return "Not Found";
	}
}

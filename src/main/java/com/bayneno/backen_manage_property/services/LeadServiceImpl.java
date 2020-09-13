package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.response.LeadResponse;
import com.bayneno.backen_manage_property.payload.request.LeadSearchRequest;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LeadServiceImpl implements LeadService  {

	@Autowired
	LeadRepository leadRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public String createLead(LeadRequest leadRequest) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		Lead lead = leadRepository.save(
				Lead
						.builder()
						.owner(leadRequest.getOwnerRequest())
						.room(leadRequest.getRoomRequest())
						.files(leadRequest.getFiles())
						.createdBy(leadRequest.getUsername())
						.createdDateTime(formatter.format(date))
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
						.owner(lead.get().getOwner())
						.room(lead.get().getRoom())
						.createdBy(lead.get().getCreatedBy())
						.createdDateTime(lead.get().getCreatedDateTime())
						.updatedBy(lead.get().getUpdatedBy())
						.updatedDateTime(lead.get().getUpdatedDateTime())
						.projects(projects)
						.files(lead.get().getFiles())
						.id(lead.get().getId())
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
						.createdBy(lead.getCreatedBy())
						.createdDateTime(lead.getCreatedDateTime())
						.build()
				)
				.collect(Collectors.toList());

		return leads;
	}

	@Override
	public String editLead(LeadRequest leadRequest) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		Optional<Lead> lead = leadRepository.findById(leadRequest.getId());
		if(lead.isPresent()) {
			lead.get().setOwner(leadRequest.getOwnerRequest());
			lead.get().setRoom(leadRequest.getRoomRequest());
			lead.get().setFiles(leadRequest.getFiles());
			lead.get().setUpdatedBy(leadRequest.getUsername());
			lead.get().setUpdatedDateTime(formatter.format(date));
			leadRepository.save(lead.get());
			return lead.get().getId();
		}
		return "Not Found";
	}
}

package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ProjectServiceImpl implements ProjectService  {

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public boolean validateProjectName (String projectName) {
			Optional<Project> project = projectRepository.findByName(projectName);
		return project.isPresent();
	}

	@Override
	public String createProject(ProjectRequest projectRequest) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		Project project = projectRepository.save(
				Project
						.builder()
						.type(projectRequest.getType())
						.name(projectRequest.getName())
						.floor(projectRequest.getFloor())
						.building(projectRequest.getBuilding())
						.developer(projectRequest.getDeveloper())
						.address(projectRequest.getAddress())
						.district(projectRequest.getDistrict())
						.amphoe(projectRequest.getAmphoe())
						.province(projectRequest.getProvince())
						.zipcode(projectRequest.getZipcode())
						.facilities(projectRequest.getFacilities())
						.transports(projectRequest.getTransports())
						.createdBy(projectRequest.getUsername())
						.createdDateTime(formatter.format(date))
						.build()
		);
		return project.getId();
	}

	@Override
	public List<Project> getProject(ProjectSearchRequest projectSearchRequest) {

		if(projectSearchRequest.getUserId() != null && !"".equals(projectSearchRequest.getUserId())) {
			Optional<Project> project = projectRepository.findById(projectSearchRequest.getUserId());
			if(project.isPresent()) {
				List<Project> projects = new ArrayList<>();
				projects.add(project.get());
				return projects;
			}
			else {
				return new ArrayList<>();
			}
		}
		return projectRepository.findAll();
	}

	@Override
	public String editProject(ProjectRequest projectRequest) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		Optional<Project> project = projectRepository.findById(projectRequest.getId());
		if(project.isPresent()) {
			project.get().setType(projectRequest.getType());
			project.get().setName(projectRequest.getName());
			project.get().setFloor(projectRequest.getFloor());
			project.get().setBuilding(projectRequest.getBuilding());
			project.get().setDeveloper(projectRequest.getDeveloper());
			project.get().setAddress(projectRequest.getAddress());
			project.get().setDistrict(projectRequest.getDistrict());
			project.get().setAmphoe(projectRequest.getAmphoe());
			project.get().setProvince(projectRequest.getProvince());
			project.get().setZipcode(projectRequest.getZipcode());
			project.get().setFacilities(projectRequest.getFacilities());
			project.get().setTransports(projectRequest.getTransports());
			project.get().setUpdatedBy(projectRequest.getUsername());
			project.get().setUpdatedDateTime(formatter.format(date));
			projectRepository.save(project.get());
			return project.get().getId();
		}
		return "Not Found";
	}

	@Override
	public void deletedProject(ProjectSearchRequest projectSearchRequest) {
		projectRepository.deleteById(projectSearchRequest.getId());
	}
}

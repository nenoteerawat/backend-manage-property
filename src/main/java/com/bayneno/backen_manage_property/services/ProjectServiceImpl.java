package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
	public String createProject(ProjectRequest projectRequest, User user) {
		Project project = projectRepository.save(
				Project
						.builder()
						.type(projectRequest.getType())
						.name(projectRequest.getName())
						.buildings(projectRequest.getBuildings())
						.address(projectRequest.getAddress())
						.district(projectRequest.getDistrict())
						.amphoe(projectRequest.getAmphoe())
						.province(projectRequest.getProvince())
						.zipcode(projectRequest.getZipcode())
						.facilities(projectRequest.getFacilities())
						.transports(projectRequest.getTransports())
						.createdBy(user)
						.createdDateTime(ZonedDateTimeUtil.now())
						.build()
		);
		return project.getId();
	}

	@Override
	public List<Project> getProject(ProjectSearchRequest projectSearchRequest) {

		if(projectSearchRequest.getId() != null && !"".equals(projectSearchRequest.getId())) {
			Optional<Project> project = projectRepository.findById(projectSearchRequest.getId());
			if(project.isPresent()) {
				List<Project> projects = new ArrayList<>();
				projects.add(project.get());
				return projects;
			}
			else {
				return new ArrayList<>();
			}
		}
        if(projectSearchRequest.getName() != null && !"".equals(projectSearchRequest.getName())) {
            Optional<Project> project = projectRepository.findByName(projectSearchRequest.getName());
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
	public String editProject(ProjectRequest projectRequest, User user) {
		Optional<Project> project = projectRepository.findById(projectRequest.getId());
		if(project.isPresent()) {
			project.get().setType(projectRequest.getType());
			project.get().setName(projectRequest.getName());
			project.get().setBuildings(projectRequest.getBuildings());
			project.get().setAddress(projectRequest.getAddress());
			project.get().setDistrict(projectRequest.getDistrict());
			project.get().setAmphoe(projectRequest.getAmphoe());
			project.get().setProvince(projectRequest.getProvince());
			project.get().setZipcode(projectRequest.getZipcode());
			project.get().setFacilities(projectRequest.getFacilities());
			project.get().setTransports(projectRequest.getTransports());
			project.get().setUpdatedBy(user);
			project.get().setUpdatedDateTime(ZonedDateTimeUtil.now());
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

package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.payload.response.ProjectResponse;
import com.bayneno.backen_manage_property.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/project/create")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest) {

        //validate project name
        if(projectService.validateProjectName(projectRequest.getName()))
        {
            return ResponseEntity.badRequest().body("Project Name Duplicate");
        }
        String projectId = projectService.createProject(projectRequest);
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/edit")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectEdit(@Valid @RequestBody ProjectRequest projectRequest) {

        //validate project name
        String projectId = projectService.editProject(projectRequest);
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/delete")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectDelete(@Valid @RequestBody ProjectSearchRequest projectSearchRequest) {

        projectService.deletedProject(projectSearchRequest);
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/project/list")
    public ResponseEntity<?> projectList(@Valid @RequestBody ProjectSearchRequest projectSearchRequest) {

        //validate project name
        List<Project> projects = projectService.getProject(projectSearchRequest);
        List<ProjectResponse> projectResponses = projects.stream().map(project -> new ProjectResponse(
                project.getId(),
                project.getType(),
                project.getName(),
                project.getFloor(),
                project.getBuilding(),
                project.getDeveloper(),
                project.getAddress(),
                project.getDistrict(),
                project.getAmphoe(),
                project.getProvince(),
                project.getZipcode(),
                project.getFacilities(),
                project.getTransports()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok(projectResponses);
    }

}

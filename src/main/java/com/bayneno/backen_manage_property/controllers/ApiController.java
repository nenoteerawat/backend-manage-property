package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ProjectService projectService;

//    @PostMapping("/project/create")
//    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
//    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest) {
//
//        //validate project name
//        if(projectService.validateProjectName(projectRequest.getName()))
//        {
//            return ResponseEntity.badRequest().body("Project Name Duplicate");
//        }
//        String projectId = projectService.createProject(projectRequest);
//        return ResponseEntity.ok(projectId);
//    }
//
//    @PostMapping("/project/list")
//    public ResponseEntity<?> projectList(@Valid @RequestBody ProjectSearchRequest projectSearchRequest) {
//
//        //validate project name
//        List<Project> projects = projectService.getProject();
//        List<ProjectResponse> projectResponses = projects.stream().map(project -> new ProjectResponse(
//                project.getId(),
//                project.getType(),
//                project.getName(),
//                project.getFloor(),
//                project.getBuilding(),
//                project.getDeveloper(),
//                project.getAddress(),
//                project.getDistrict(),
//                project.getAmphoe(),
//                project.getProvince(),
//                project.getZipcode(),
//                project.getFacilities(),
//                project.getTransports()
//                )
//        ).collect(Collectors.toList());
//        return ResponseEntity.ok(projectResponses);
//    }
}

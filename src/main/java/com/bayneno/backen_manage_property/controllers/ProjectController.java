package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.ChangeLogType;
import com.bayneno.backen_manage_property.models.ChangeSubmitType;
import com.bayneno.backen_manage_property.models.ERole;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.payload.response.ProjectResponse;
import com.bayneno.backen_manage_property.requests.change_log.SubmitReq;
import com.bayneno.backen_manage_property.security.services.ChangeServiceImpl;
import com.bayneno.backen_manage_property.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChangeServiceImpl changeService;


    @PostMapping("/project/create")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest request) {
        String projectId = null;
        if(request.isUserInRole(ERole.ROLE_SALE.name())){
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.ADD.name())
                    .type(ChangeLogType.PROJECT.name())
                    .toValue(Project
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
                            .createdBy(projectRequest.getUsername())
                            .createdDateTime(formatter.format(date))
                            .build())
                    .build());

        } else {
            //validate project name
            if (projectService.validateProjectName(projectRequest.getName())) {
                return ResponseEntity.badRequest().body("Project Name Duplicate");
            }
            projectId = projectService.createProject(projectRequest);
        }
        assert projectId != null;
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/edit")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectEdit(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest request) {
        String projectId = null;
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            changeService.submit(SubmitReq.builder()
                    .id(projectRequest.getId())
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.EDIT.name())
                    .type(ChangeLogType.PROJECT.name())
                    .toValue(Project
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
                            .updatedBy(projectRequest.getUsername())
                            .updatedDateTime(formatter.format(date))
                            .build())
                    .build());
        } else {
            //validate project name
            projectId = projectService.editProject(projectRequest);
        }
        assert projectId != null;
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/delete")
    @PreAuthorize("hasRole('USER') or hasRole('SALE') or hasRole('ADMIN')")
    public ResponseEntity<?> projectDelete(@Valid @RequestBody ProjectSearchRequest projectSearchRequest, Principal principal) {

        projectService.deletedProject(projectSearchRequest);
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/project/list")
    public ResponseEntity<?> projectList(@Valid @RequestBody ProjectSearchRequest projectSearchRequest) {

        List<Project> projects = projectService.getProject(projectSearchRequest);
        List<ProjectResponse> projectResponses = new ArrayList<>();
        if(!projectSearchRequest.isGroupBuilding())
        {
            for (Project p:projects
                 ) {
                projectResponses.addAll(
                        p.getBuildings().stream().map(project ->
                                new ProjectResponse(
                                        p.getId(),
                                        p.getType(),
                                        p.getName(),
                                        project.getFloor(),
                                        project.getBuilding(),
                                        project.getDevelop(),
                                        p.getAddress(),
                                        p.getDistrict(),
                                        p.getAmphoe(),
                                        p.getProvince(),
                                        p.getZipcode(),
                                        p.getFacilities(),
                                        p.getTransports(),
                                        p.getBuildings()
                                )
                        ).collect(Collectors.toList())
                );
            }
        } else {
            projectResponses = projects.stream().map(project -> new ProjectResponse(
                            project.getId(),
                            project.getType(),
                            project.getName(),
                            "",
                            "",
                            "",
                            project.getAddress(),
                            project.getDistrict(),
                            project.getAmphoe(),
                            project.getProvince(),
                            project.getZipcode(),
                            project.getFacilities(),
                            project.getTransports(),
                    project.getBuildings()
                    )
            ).collect(Collectors.toList());
        }

        return ResponseEntity.ok(projectResponses);
    }



}

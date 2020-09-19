package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ChangeLogType;
import com.bayneno.backen_manage_property.enums.ChangeSubmitType;
import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.ProjectResponse;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.services.ChangeServiceImpl;
import com.bayneno.backen_manage_property.services.ProjectService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    public ProjectController(ChangeServiceImpl changeService
            , ProjectService projectService
            , UserRepository userRepository) {
        this.changeService = changeService;
        this.projectService = projectService;
        this.userRepository = userRepository;
    }


    @PostMapping("/project/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest request, Principal principal) {
        String projectId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())){
            changeService.submit(SubmitReq.builder()
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.ADD.name())
                    .username(principal.getName())
                    .type(ChangeLogType.PROJECT.name())
                    .toValue(Project.builder()
                            .id(projectRequest.getId())
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
                            .createdBy(createdByUser)
                            .createdDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());

        } else {
            //validate project name
            if (projectService.validateProjectName(projectRequest.getName())) {
                return ResponseEntity.badRequest().body("Project Name Duplicate");
            }
            projectId = projectService.createProject(projectRequest, createdByUser);
        }
        assert projectId != null;
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/edit")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> projectEdit(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest request, Principal principal) {
        String projectId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(request.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .id(projectRequest.getId())
                    .comment("Auto Comment")
                    .submitType(ChangeSubmitType.EDIT.name())
                    .type(ChangeLogType.PROJECT.name())
                    .username(principal.getName())
                    .toValue(Project.builder()
                            .id(projectRequest.getId())
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
                            .updatedBy(updatedByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
                            .build())
                    .build());
        } else {
            //validate project name
            projectId = projectService.editProject(projectRequest, updatedByUser);
        }
        assert projectId != null;
        return ResponseEntity.ok(projectId);
    }

    @PostMapping("/project/delete")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> projectDelete(@Valid @RequestBody ProjectSearchRequest projectSearchRequest) {

        projectService.deletedProject(projectSearchRequest);
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/project/list")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
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

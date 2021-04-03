package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.enums.ESubmitTypeChangeLog;
import com.bayneno.backen_manage_property.enums.ETypeChangeLog;
import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.payload.response.ProjectResponse;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    private final ChangeServiceImpl changeService;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    public ProjectController(ChangeServiceImpl changeService
            , ProjectService projectService
            , UserRepository userRepository
            , ProjectRepository projectRepository) {
        this.changeService = changeService;
        this.projectService = projectService;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/project/create")
    @PreAuthorize("hasRole('SALE') or hasRole('ADMIN') or hasRole('SALE_MANAGER') or hasRole('MANAGER')")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest httpServletRequest, Principal principal) {
        String projectId = null;
        User createdByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())){
            changeService.submit(SubmitReq.builder()
                    .comment(projectRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.ADD)
                    .username(principal.getName())
                    .type(ETypeChangeLog.PROJECT)
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
                            .zone(projectRequest.getZone())
                            .team(projectRequest.getTeam())
                            .createdBy(createdByUser)
                            .createdDateTime(ZonedDateTimeUtil.now())
                            .updatedBy(createdByUser)
                            .updatedDateTime(ZonedDateTimeUtil.now())
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
    public ResponseEntity<?> projectEdit(@Valid @RequestBody ProjectRequest projectRequest, HttpServletRequest httpServletRequest, Principal principal) {
        String projectId = null;
        User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())) {
            changeService.submit(SubmitReq.builder()
                    .id(projectRequest.getId())
                    .comment(projectRequest.getComment())
                    .submitType(ESubmitTypeChangeLog.EDIT)
                    .type(ETypeChangeLog.PROJECT)
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
                            .zone(projectRequest.getZone())
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
    public ResponseEntity<?> projectDelete(@Valid @RequestBody ProjectSearchRequest projectSearchRequest, HttpServletRequest httpServletRequest, Principal principal) {

        projectRepository.findById(projectSearchRequest.getId()).ifPresent(project -> {
            User updatedByUser = userRepository.findByUsername(principal.getName()).orElse(null);
            if(httpServletRequest.isUserInRole(ERole.ROLE_SALE.name())){
                changeService.submit(SubmitReq.builder()
                        .id(projectSearchRequest.getId())
                        .comment(projectSearchRequest.getComment())
                        .submitType(ESubmitTypeChangeLog.DELETE)
                        .username(principal.getName())
                        .type(ETypeChangeLog.PROJECT)
                        .toValue(Project.builder()
                                .id(projectSearchRequest.getId())
                                .updatedBy(updatedByUser)
                                .updatedDateTime(ZonedDateTimeUtil.now())
                                .address("")
                                .amphoe("")
                                .district("")
                                .transports(new ArrayList<>())
                                .buildings(new ArrayList<>())
                                .province("")
                                .zipcode("")
                                .name("")
                                .type("")
                                .zone("")
                                .facilities(new ArrayList<>())
                                .build())
                        .build());
            } else {
                projectService.deletedProject(projectSearchRequest);
            }
        });
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
                                        p.getZone(),
                                        p.getTeam(),
                                        p.getFacilities(),
                                        p.getTransports(),
                                        p.getBuildings(),
                                        p.getUpdatedBy(),
                                        p.getUpdatedDateTime()
                                )
                        )
                        .collect(Collectors.toList())
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
                            project.getZone(),
                            project.getTeam(),
                            project.getFacilities(),
                            project.getTransports(),
                            project.getBuildings(),
                            project.getUpdatedBy(),
                            project.getUpdatedDateTime()
                    )
            )
            .collect(Collectors.toList());
        }

        projectResponses = projectResponses.stream()
                .sorted(Comparator.comparing(ProjectResponse::getUpdatedDateTime).reversed())
                .collect(Collectors.toList());

        return ResponseEntity.ok(projectResponses);
    }



}

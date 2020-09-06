package com.bayneno.backen_manage_property.services;


import com.bayneno.backen_manage_property.models.Project;
import com.bayneno.backen_manage_property.payload.request.ProjectRequest;
import com.bayneno.backen_manage_property.payload.request.ProjectSearchRequest;

import java.util.List;

public interface ProjectService {

    boolean validateProjectName (String projectName);

    String createProject(ProjectRequest projectRequest);

    List<Project> getProject(ProjectSearchRequest projectSearchRequest);

    String editProject(ProjectRequest projectRequest);

    void deletedProject(ProjectSearchRequest projectRequest);
}
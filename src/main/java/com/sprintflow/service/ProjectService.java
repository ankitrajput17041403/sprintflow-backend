package com.sprintflow.service;

import com.sprintflow.dto.CreateProjectRequest;
import com.sprintflow.dto.ProjectResponse;
import com.sprintflow.dto.UpdateProjectRequest;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request);
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(Long id);

    ProjectResponse updateProject(Long id, UpdateProjectRequest request);
    ProjectResponse deleteProject(Long id);
}
package com.sprintflow.service;

import com.sprintflow.dto.CreateProjectRequest;
import com.sprintflow.dto.ProjectResponse;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request);
}
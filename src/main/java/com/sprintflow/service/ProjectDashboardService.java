package com.sprintflow.service;

import com.sprintflow.dto.ProjectDashboardResponse;

public interface ProjectDashboardService {

    ProjectDashboardResponse getProjectDashboard(Long projectId);
}
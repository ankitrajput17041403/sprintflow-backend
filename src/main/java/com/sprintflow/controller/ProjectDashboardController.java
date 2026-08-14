package com.sprintflow.controller;

import com.sprintflow.dto.ProjectDashboardResponse;
import com.sprintflow.service.ProjectDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectDashboardController {

    private final ProjectDashboardService projectDashboardService;

    @GetMapping("/{projectId}/dashboard")
    public ResponseEntity<ProjectDashboardResponse> getProjectDashboard(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                projectDashboardService.getProjectDashboard(projectId)
        );
    }
}
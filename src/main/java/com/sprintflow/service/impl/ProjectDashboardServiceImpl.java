package com.sprintflow.service.impl;

import com.sprintflow.dto.ProjectDashboardResponse;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.entity.Project;
import com.sprintflow.enums.SprintStatus;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.OrganizationSecurityService;
import com.sprintflow.service.ProjectDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDashboardServiceImpl
        implements ProjectDashboardService {

    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;
    private final SprintRepository sprintRepository;
    private final OrganizationSecurityService organizationSecurityService;

    @Override
    public ProjectDashboardResponse getProjectDashboard(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        organizationSecurityService.validateProjectAccess(project);

        // Issue statistics
        Long totalIssues =
                issueRepository.countByProjectId(projectId);

        Long todoIssues =
                issueRepository.countByProjectIdAndStatus(
                        projectId,
                        IssueStatus.TODO
                );

        Long inProgressIssues =
                issueRepository.countByProjectIdAndStatus(
                        projectId,
                        IssueStatus.IN_PROGRESS
                );

        Long doneIssues =
                issueRepository.countByProjectIdAndStatus(
                        projectId,
                        IssueStatus.DONE
                );

        // Sprint statistics
        Long totalSprints =
                sprintRepository.countByProjectId(projectId);

        Long plannedSprints =
                sprintRepository.countByProjectIdAndStatus(
                        projectId,
                        SprintStatus.PLANNED
                );

        Long activeSprints =
                sprintRepository.countByProjectIdAndStatus(
                        projectId,
                        SprintStatus.ACTIVE
                );

        Long completedSprints =
                sprintRepository.countByProjectIdAndStatus(
                        projectId,
                        SprintStatus.COMPLETED
                );

        return new ProjectDashboardResponse(
                project.getName(),
                totalIssues,
                todoIssues,
                inProgressIssues,
                doneIssues,
                totalSprints,
                plannedSprints,
                activeSprints,
                completedSprints
        );
    }
}
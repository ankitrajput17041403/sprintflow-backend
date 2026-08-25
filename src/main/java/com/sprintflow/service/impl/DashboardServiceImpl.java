package com.sprintflow.service.impl;

import com.sprintflow.dto.DashboardResponse;
import com.sprintflow.dto.RecentIssueResponse;
import com.sprintflow.dto.SprintProgressResponse;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.entity.Sprint;
import com.sprintflow.entity.User;
import com.sprintflow.enums.Priority;
import com.sprintflow.enums.SprintStatus;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;
    private final SprintRepository sprintRepository;


    @Override
    public DashboardResponse getDashboard() {

        User currentUser =
                currentUserService.getCurrentUser();

        Long organizationId =
                currentUser.getOrganization().getId();


        // Sprint Progress
        SprintProgressResponse sprintProgress = null;

        Sprint activeSprint =
                sprintRepository
                        .findByProjectOrganizationIdAndStatus(
                                organizationId,
                                SprintStatus.COMPLETED
                        )
                        .orElse(null);

        if (activeSprint != null) {

            Long totalSprintIssues =
                    issueRepository.countBySprintId(
                            activeSprint.getId());

            Long completedSprintIssues =
                    issueRepository.countBySprintIdAndStatus(
                            activeSprint.getId(),
                            IssueStatus.DONE
                    );

            Long remainingSprintIssues =
                    totalSprintIssues - completedSprintIssues;

            Integer progressPercentage =
                    totalSprintIssues == 0
                            ? 0
                            : (int) (
                            (completedSprintIssues * 100.0)
                                    / totalSprintIssues
                    );

            sprintProgress =
                    SprintProgressResponse.builder()
                            .sprintName(activeSprint.getName())
                            .totalIssues(totalSprintIssues)
                            .completedIssues(completedSprintIssues)
                            .remainingIssues(remainingSprintIssues)
                            .progressPercentage(
                                    progressPercentage)
                            .build();
        }


        // Recent Issues
        List<RecentIssueResponse> recentIssues =
                issueRepository
                        .findTop5ByProjectOrganizationIdOrderByCreatedAtDesc(
                                organizationId)
                        .stream()
                        .map(issue ->
                                RecentIssueResponse.builder()
                                        .id(issue.getId())
                                        .title(issue.getTitle())
                                        .status(issue.getStatus())
                                        .priority(issue.getPriority())
                                        .projectName(
                                                issue.getProject()
                                                        .getName())
                                        .build())
                        .toList();


        return DashboardResponse.builder()

                .totalProjects(
                        projectRepository
                                .countProjectsByOrganization(
                                        organizationId)
                )

                .totalIssues(
                        issueRepository
                                .countIssuesByOrganization(
                                        organizationId)
                )

                .openIssues(
                        issueRepository.countIssuesByStatus(
                                organizationId,
                                IssueStatus.TODO
                        )
                )

                .inProgressIssues(
                        issueRepository.countIssuesByStatus(
                                organizationId,
                                IssueStatus.IN_PROGRESS
                        )
                )

                .doneIssues(
                        issueRepository.countIssuesByStatus(
                                organizationId,
                                IssueStatus.DONE
                        )
                )

                .activeSprints(
                        sprintRepository.countSprintsByStatus(
                                organizationId,
                                SprintStatus.ACTIVE
                        )
                )

                .completedSprints(
                        sprintRepository.countSprintsByStatus(
                                organizationId,
                                SprintStatus.COMPLETED
                        )
                )

                .lowPriorityIssues(
                        issueRepository.countIssuesByPriority(
                                organizationId,
                                Priority.LOW
                        )
                )

                .mediumPriorityIssues(
                        issueRepository.countIssuesByPriority(
                                organizationId,
                                Priority.MEDIUM
                        )
                )

                .highPriorityIssues(
                        issueRepository.countIssuesByPriority(
                                organizationId,
                                Priority.HIGH
                        )
                )

                .recentIssues(recentIssues)
                .sprintProgress(sprintProgress)
                .build();
    }
}
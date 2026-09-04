package com.sprintflow.service.impl;

import com.sprintflow.dto.*;
import com.sprintflow.entity.*;
import com.sprintflow.enums.SprintStatus;
import com.sprintflow.exception.BusinessException;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.NotificationService;
import com.sprintflow.service.OrganizationSecurityService;
import com.sprintflow.service.SprintService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final IssueRepository issueRepository;
    private final NotificationService notificationService;


    @Override
    public SprintResponse createSprint(CreateSprintRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        organizationSecurityService.validateProjectAccess(project);

        Sprint sprint = new Sprint();

        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setStatus(SprintStatus.PLANNED);
        sprint.setProject(project);
        sprint.setCreatedBy(currentUserService.getCurrentUser());
        sprint.setCreatedAt(LocalDateTime.now());

        sprintRepository.save(sprint);

        return mapToResponse(sprint);
    }


    @Override
    public List<SprintResponse> getAllSprints(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        organizationSecurityService.validateProjectAccess(project);

        List<Sprint> sprints =
                sprintRepository.findByProjectId(projectId);

        return sprints.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public SprintResponse getSprintById(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        return mapToResponse(sprint);
    }


    @Override
    public SprintResponse updateSprint(
            Long id,
            UpdateSprintRequest request) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setStatus(request.getStatus());

        sprintRepository.save(sprint);

        return mapToResponse(sprint);
    }


    @Override
    public SprintResponse deleteSprint(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        sprintRepository.delete(sprint);

        return mapToResponse(sprint);
    }


    @Override
    public SprintResponse startSprint(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        log.debug(
                "Current sprint status: {}",
                sprint.getStatus()
        );

        if (sprint.getStatus() != SprintStatus.PLANNED) {

            throw new BusinessException(
                    "Only planned sprint can be started"
            );
        }

        if (sprintRepository.existsByProjectIdAndStatus(
                sprint.getProject().getId(),
                SprintStatus.ACTIVE)) {

            throw new BusinessException(
                    "Project already has an active sprint"
            );
        }

        sprint.setStatus(SprintStatus.ACTIVE);

        sprintRepository.save(sprint);

        Set<Long> notifiedUserIds = new HashSet<>();

        List<Issue> issues =
                issueRepository.findBySprintId(sprint.getId());

        if (issues.isEmpty()) {

            throw new BusinessException(
                    "No issues are assigned to this sprint " +
                            "Then Notification Not Created !!!"
            );
        }

        log.debug(
                "Issues assigned to sprint {}: {}",
                sprint.getId(),
                issues.size()
        );

        for (Issue issue : issues) {

            User assignedUser = issue.getAssignedTo();

            if (assignedUser == null) {

                throw new BusinessException(
                        "Issue Not Assigned To anyone. " +
                                "Notification Not Created !!!"
                );
            }

            log.debug(
                    "Issue {} is assigned to user {}",
                    issue.getId(),
                    assignedUser.getId()
            );

            log.debug(
                    "Issue {} belongs to sprint {}",
                    issue.getId(),
                    issue.getSprint().getName()
            );

            if (assignedUser != null
                    && notifiedUserIds.add(
                    assignedUser.getId())) {

                notificationService.createNotification(
                        assignedUser,
                        sprint.getName() + " has started"
                );
            }
        }

        return mapToResponse(sprint);
    }


    @Override
    public SprintResponse completeSprint(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sprint not found"
                        ));

        organizationSecurityService.validateSprintAccess(sprint);

        if (sprint.getStatus() != SprintStatus.ACTIVE) {

            throw new BusinessException(
                    "Only active sprint can be completed"
            );
        }

        // Get all issues belonging to this sprint
        List<Issue> issues =
                issueRepository.findBySprintId(
                        sprint.getId()
                );

        // Move unfinished issues back to backlog
        for (Issue issue : issues) {

            if (issue.getStatus() != IssueStatus.DONE) {
                issue.setSprint(null);
            }
        }

        issueRepository.saveAll(issues);

        // Complete the sprint
        sprint.setStatus(SprintStatus.COMPLETED);

        sprintRepository.save(sprint);

        // Notify unique assigned users
        Set<Long> notifiedUserIds = new HashSet<>();

        for (Issue issue : issues) {

            User assignedUser = issue.getAssignedTo();

            if (assignedUser != null
                    && notifiedUserIds.add(
                    assignedUser.getId())) {

                notificationService.createNotification(
                        assignedUser,
                        sprint.getName() +
                                " has been completed"
                );
            }
        }

        return mapToResponse(sprint);
    }


    @Override
    public void planSprint(
            Long sprintId,
            SprintPlanningRequest request) {

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sprint not found"
                        ));

        organizationSecurityService.validateSprintAccess(sprint);

        for (Long issueId : request.getIssueIds()) {

            Issue issue = issueRepository.findById(issueId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Issue not found"
                            ));

            organizationSecurityService.validateIssueAccess(issue);

            if (!issue.getProject().getId()
                    .equals(sprint.getProject().getId())) {

                throw new BusinessException(
                        "Issue and Sprint must belong to the same project"
                );
            }

            List<Issue> issuesToUpdate =
                    new ArrayList<>();

            issuesToUpdate.add(issue);

            issue.setSprint(sprint);

            issueRepository.saveAll(issuesToUpdate);
        }
    }


    @Override
    public SprintBoardResponse getSprintBoard(
            Long sprintId) {

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sprint not found"
                        ));

        organizationSecurityService.validateSprintAccess(sprint);

        List<Issue> issues =
                issueRepository.findBySprintId(sprintId);

        List<BoardIssueResponse> todo =
                new ArrayList<>();

        List<BoardIssueResponse> inProgress =
                new ArrayList<>();

        List<BoardIssueResponse> done =
                new ArrayList<>();

        for (Issue issue : issues) {

            BoardIssueResponse boardIssue =
                    new BoardIssueResponse(
                            issue.getId(),
                            issue.getTitle(),
                            issue.getPriority(),
                            issue.getStatus()
                    );

            switch (issue.getStatus()) {

                case TODO:
                    todo.add(boardIssue);
                    break;

                case IN_PROGRESS:
                    inProgress.add(boardIssue);
                    break;

                case DONE:
                    done.add(boardIssue);
                    break;
            }
        }

        SprintBoardResponse response =
                new SprintBoardResponse();

        response.setTodo(todo);
        response.setInProgress(inProgress);
        response.setDone(done);

        return response;
    }


    private SprintResponse mapToResponse(
            Sprint sprint) {

        return new SprintResponse(
                sprint.getId(),
                sprint.getName(),
                sprint.getGoal(),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getStatus(),
                sprint.getCreatedAt(),
                sprint.getProject().getName()
        );
    }
}
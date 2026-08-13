package com.sprintflow.service.impl;

import com.sprintflow.dto.*;
import com.sprintflow.entity.Issue;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;

import com.sprintflow.enums.SprintStatus;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
import com.sprintflow.service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final IssueRepository issueRepository;

    @Override
    public SprintResponse createSprint(CreateSprintRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

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
                        new RuntimeException("Project not found"));

        organizationSecurityService.validateProjectAccess(project);

        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);


        return sprints.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public SprintResponse getSprintById(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sprint not found"));


        organizationSecurityService.validateSprintAccess(sprint);

        return mapToResponse(sprint);

    }


    @Override
    public SprintResponse updateSprint(Long id, UpdateSprintRequest request) {


        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sprint not found"));

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
                        new RuntimeException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        sprintRepository.delete(sprint);

        return mapToResponse(sprint);

    }

    @Override
    public SprintResponse startSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sprint not found"));
        organizationSecurityService.validateSprintAccess(sprint);

        if (sprint.getStatus() != SprintStatus.PLANNED) {
            throw new RuntimeException("Only planned sprint can be started");
        }
        if (sprintRepository.existsByProjectIdAndStatus(sprint.getProject().getId(), SprintStatus.ACTIVE)) {

            throw new RuntimeException(
                    "Project already has an active sprint");
        }
        sprint.setStatus(SprintStatus.ACTIVE);
        sprintRepository.save(sprint);
        return mapToResponse(sprint);

    }

    @Override
    public SprintResponse completeSprint(Long id) {

        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        if (sprint.getStatus() != SprintStatus.ACTIVE) {
            throw new RuntimeException(
                    "Only active sprint can be completed");
        }

        // Get all issues belonging to this sprint
        List<Issue> issues =
                issueRepository.findBySprintId(sprint.getId());

        // Move unfinished issues back to backlog
        for (Issue issue : issues) {

            if (issue.getStatus() != com.sprintflow.entity.IssueStatus.DONE) {
                issue.setSprint(null);
            }
        }

        issueRepository.saveAll(issues);

        // Complete the sprint
        sprint.setStatus(SprintStatus.COMPLETED);

        sprintRepository.save(sprint);

        return mapToResponse(sprint);
    }

    @Override
    public void planSprint(Long sprintId, SprintPlanningRequest request) {

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        for (Long issueId : request.getIssueIds()) {

            Issue issue = issueRepository.findById(issueId)
                    .orElseThrow(() -> new RuntimeException("Issue not found"));

            organizationSecurityService.validateIssueAccess(issue);

            if (!issue.getProject().getId().equals(sprint.getProject().getId())) {
                throw new RuntimeException("Issue and Sprint must belong to the same project");
            }

            List<Issue>  issuesToUpdate = new ArrayList<>();
            issuesToUpdate.add(issue);
            issue.setSprint(sprint);

            issueRepository.saveAll(issuesToUpdate);
        }
    }

    @Override
    public SprintBoardResponse getSprintBoard(Long sprintId) {

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        organizationSecurityService.validateSprintAccess(sprint);

        List<Issue> issues = issueRepository.findBySprintId(sprintId);

        List<BoardIssueResponse> todo = new ArrayList<>();
        List<BoardIssueResponse> inProgress = new ArrayList<>();
        List<BoardIssueResponse> done = new ArrayList<>();

        for (Issue issue : issues) {

            BoardIssueResponse boardIssue = new BoardIssueResponse(
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

        SprintBoardResponse response = new SprintBoardResponse();

        response.setTodo(todo);
        response.setInProgress(inProgress);
        response.setDone(done);

        return response;
    }

    private SprintResponse mapToResponse(Sprint sprint) {

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

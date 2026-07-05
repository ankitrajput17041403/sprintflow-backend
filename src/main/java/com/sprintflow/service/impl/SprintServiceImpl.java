package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;
import com.sprintflow.entity.User;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;

    @Override
    public SprintResponse createSprint(CreateSprintRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        Long organizationId =
                currentUser.getOrganization().getId();

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!organizationId.equals(project.getOrganization().getId())) {

            throw new RuntimeException("Access denied");
        }
        Sprint sprint = new Sprint();
        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setStatus(request.getStatus());
        sprint.setProject(project);
        sprint.setCreatedBy(currentUser);
        sprint.setCreatedAt(LocalDateTime.now());
        sprintRepository.save(sprint);


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

    @Override
    public List<SprintResponse> getAllSprints(Long projectId) {

        User currentUser = currentUserService.getCurrentUser();

        Long organizationId =
                currentUser.getOrganization().getId();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!organizationId.equals(project.getOrganization().getId())) {

            throw new RuntimeException("Access denied");
        }
               List<Sprint> sprints = sprintRepository.findByProjectId(projectId);

        return sprints.stream()
                .map(sprint -> new SprintResponse(
                        sprint.getId(),
                        sprint.getName(),
                        sprint.getGoal(),
                        sprint.getStartDate(),
                        sprint.getEndDate(),
                        sprint.getStatus(),
                        sprint.getCreatedAt(),
                        sprint.getProject().getName()
                ))
                .toList();    }

    @Override
    public SprintResponse getSprintById(Long id) {
        return null;
    }

    @Override
    public SprintResponse updateSprint(Long id, UpdateSprintRequest request) {
        return null;
    }

    @Override
    public SprintResponse deleteSprint(Long id) {
        return null;
    }
}

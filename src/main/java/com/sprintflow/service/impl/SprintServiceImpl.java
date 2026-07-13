package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;

import com.sprintflow.enums.SprintStatus;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
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
    private final OrganizationSecurityService organizationSecurityService;

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

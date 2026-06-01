package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateProjectRequest;
import com.sprintflow.dto.ProjectResponse;
import com.sprintflow.entity.Organization;
import com.sprintflow.entity.Project;
import com.sprintflow.repository.OrganizationRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.service.ProjectService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ProjectResponse createProject(CreateProjectRequest request) {

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new RuntimeException("Organization not found"));

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOrganization(organization);

        project = projectRepository.save(project);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                organization.getName()
        );
    }
}

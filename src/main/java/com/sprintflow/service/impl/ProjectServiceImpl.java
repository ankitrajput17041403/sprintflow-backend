package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateProjectRequest;
import com.sprintflow.dto.ProjectResponse;
import com.sprintflow.dto.UpdateProjectRequest;
import com.sprintflow.entity.Organization;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.User;
import com.sprintflow.repository.OrganizationRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;

    private final CurrentUserService currentUserService;

//   There is Secuirty Issue ..............
//    @Override
//    public ProjectResponse createProject(CreateProjectRequest request) {
//
//        Organization organization = organizationRepository
//                .findById(request.getOrganizationId())
//                .orElseThrow(() ->
//                        new RuntimeException("Organization not found"));
//
//        Project project = new Project();
//
//        project.setName(request.getName());
//        project.setDescription(request.getDescription());
//        project.setOrganization(organization);
//
//        project = projectRepository.save(project);
//
//        return new ProjectResponse(
//                project.getId(),
//                project.getName(),
//                project.getDescription(),
//                project.getCreatedAt(),
//                organization.getName()
//        );
//    }



    @Override
    public ProjectResponse createProject(CreateProjectRequest request) {

        User currentUser =
                currentUserService.getCurrentUser();

        Organization organization =
                currentUser.getOrganization();

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOrganization(organization);
        project.setCreatedBy(currentUser);

        project = projectRepository.save(project);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                organization.getName()
        );
    }

    @Override
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getCreatedAt(),
                        project.getOrganization().getName()
                ))
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getOrganization().getName()
        );
    }

    @Override
    public ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        project = projectRepository.save(project);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getOrganization().getName()
        );
    }

    @Override
    public ProjectResponse deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));
        projectRepository.delete(project);
        return  new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getOrganization().getName()
        );
    }


}
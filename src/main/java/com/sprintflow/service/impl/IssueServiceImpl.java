package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateIssueRequest;
import com.sprintflow.dto.IssueResponse;
import com.sprintflow.dto.UpdateIssueRequest;
import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Organization;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.User;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.UserRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;


    @Override
    public IssueResponse createIssue(CreateIssueRequest request) {


        User currentUser =
                currentUserService.getCurrentUser();

        Organization organization = currentUser.getOrganization();

        Project project = projectRepository.findById(
                        request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));
        if (!organization.getId().equals(project.getOrganization().getId())) {
            throw new RuntimeException("Access denied");
        }

        Issue issue = new Issue();
        issue.setTitle(request.getTitle());

        issue.setDescription(request.getDescription());

        issue.setStatus(request.getStatus());

        issue.setPriority(request.getPriority());

        issue.setProject(project);

        issue.setCreatedBy(currentUser);
        issue = issueRepository.save(issue);

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getProject().getName()
        );
    }


    @Override
    public List<IssueResponse> getAllIssues(Long projectId) {

        User currentUser =currentUserService.getCurrentUser();

        Organization organization =
                currentUser.getOrganization();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!organization.getId().equals(project.getOrganization().getId())) {
            throw new RuntimeException("Access denied");
        }

        List<Issue> issues =
                issueRepository.findByProjectId(projectId);

        return issues.stream()
                .map(issue -> new IssueResponse(
                        issue.getId(),
                        issue.getTitle(),
                        issue.getDescription(),
                        issue.getStatus(),
                        issue.getPriority(),
                        issue.getCreatedAt(),
                        issue.getProject().getName()
                ))
                .toList();
    }

    @Override
    public IssueResponse getIssueById(Long id) {
//        User currentUser = userRepository.findById(1L)
//                .orElseThrow(() ->
//                        new RuntimeException("User not found"));

        User currentUser =currentUserService.getCurrentUser();
        Organization organization =
                currentUser.getOrganization();

        Issue issue =
                issueRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Issue not found"));

//        Project project = projectRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Project not found"));

        if (!organization.getId().equals(issue.getProject().getOrganization().getId())) {
            throw new RuntimeException("Access denied");
        }

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getProject().getName()
        );
    }

    @Override
    public IssueResponse updateIssue(
            Long id,
            UpdateIssueRequest request) {

        User currentUser =currentUserService.getCurrentUser();

        Long organizationId =
                currentUser.getOrganization().getId();

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Issue not found"));

        if (!organizationId.equals(
                issue.getProject().getOrganization().getId())) {

            throw new RuntimeException("Access denied");
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setStatus(request.getStatus());
        issue.setPriority(request.getPriority());

        issue = issueRepository.save(issue);

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getProject().getName()
        );
    }

    @Override
    public IssueResponse deleteIssue(Long id) {
        User currentUser =currentUserService.getCurrentUser();

        Long organizationId =
                currentUser.getOrganization().getId();

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Issue not found"));

        if (!organizationId.equals(
                issue.getProject().getOrganization().getId())) {

            throw new RuntimeException("Access denied");
        }
        issueRepository.delete(issue);
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getProject().getName()
        );


    }
}

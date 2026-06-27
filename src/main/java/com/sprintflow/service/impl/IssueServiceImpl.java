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
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

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

        return List.of();
    }

    @Override
    public IssueResponse getIssueById(Long id) {
        return null;
    }

    @Override
    public IssueResponse updateIssue(Long id, UpdateIssueRequest request) {
        return null;
    }

    @Override
    public IssueResponse deleteIssue(Long id) {
        return null;
    }
}

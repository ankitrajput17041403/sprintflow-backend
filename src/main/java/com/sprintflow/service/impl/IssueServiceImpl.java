package com.sprintflow.service.impl;

import com.sprintflow.dto.*;
import com.sprintflow.entity.*;
import com.sprintflow.enums.ActivityAction;
import com.sprintflow.exception.AccessDeniedException;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.repository.ProjectRepository;
import com.sprintflow.repository.SprintRepository;
import com.sprintflow.repository.UserRepository;
import com.sprintflow.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final SprintRepository sprintRepository;
    private final ActivityService activityService;
    private final NotificationService notificationService;



    @Override
    public IssueResponse createIssue(CreateIssueRequest request) {


        User currentUser =
                currentUserService.getCurrentUser();

        Organization organization = currentUser.getOrganization();

        Project project = projectRepository.findById(
                        request.getProjectId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("ISSUE NOT FOUND"));
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

        issue.setStoryPoints(request.getStoryPoints());

        issue = issueRepository.save(issue);

        return mapToResponse(issue);
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
                .map(issue -> mapToResponse(issue)).toList();
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
                        .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

//        Project project = projectRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Project not found"));

        if (!organization.getId().equals(issue.getProject().getOrganization().getId())) {
            throw new AccessDeniedException("Access denied");
        }

        return mapToResponse(issue);
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

            throw new AccessDeniedException("Access denied");
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setStatus(request.getStatus());
        issue.setPriority(request.getPriority());
        issue.setStoryPoints(request.getStoryPoints());


        issue = issueRepository.save(issue);

        activityService.logActivity(issue,currentUser,ActivityAction.UPDATED,"Issue Updated");

        return mapToResponse(issue);
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
         return mapToResponse(issue);


    }

    @Override
    public IssueResponse assignIssueToSprint(Long issueId, Long sprintId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new RuntimeException("Issue not found"));

        organizationSecurityService.validateIssueAccess(issue);
         Sprint sprint = sprintRepository.findById(sprintId)
                 .orElseThrow(()->
                         new RuntimeException("Sprint Not Found"));

         organizationSecurityService.validateSprintAccess(sprint);




        if (!issue.getProject().getId().equals(sprint.getProject().getId())) {
            throw new RuntimeException("Issue and Sprint must belong to the same project");
        }

        issue.setSprint(sprint);
        issueRepository.save(issue);

        activityService.logActivity(
                issue,
                currentUserService.getCurrentUser(),
                ActivityAction.SPRINT_ASSIGNED,
                "Issue assigned to sprint " + sprint.getName()
        );

        return mapToResponse(issue);
    }

    @Override
    public List<IssueResponse> getIssuesBySprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(()->
                        new RuntimeException("Sprint Not Found"));

        organizationSecurityService.validateSprintAccess(sprint);

        List<Issue> issues = issueRepository.findBySprintId(sprintId);
        return issues.stream().map(issue -> mapToResponse(issue))
                .toList();
    }
    @Override
    public IssueResponse removeIssueFromSprint(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new RuntimeException("Issue Not Found"));

        organizationSecurityService.validateIssueAccess(issue);

        Sprint oldSprint = issue.getSprint();

        issue.setSprint(null);

        issueRepository.save(issue);

        activityService.logActivity(
                issue,
                currentUserService.getCurrentUser(),
                ActivityAction.SPRINT_REMOVED,
                "Issue removed from sprint " + oldSprint.getName()
        );

        return mapToResponse(issue);
    }

    @Override
    public List<IssueResponse> getBacklogIssues(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        organizationSecurityService.validateProjectAccess(project);

        List<Issue> issues =
                issueRepository.findByProjectIdAndSprintIsNull(projectId);

        return issues.stream()
                .map(this::mapToResponse)
                .toList();
    }

//    @Override
//    public void updateIssueStatus(Long issueId, UpdateIssueStatusRequest request) {
//
//        Issue issue = issueRepository.findById(issueId)
//                .orElseThrow(() -> new RuntimeException("Issue not found"));
//
//        organizationSecurityService.validateIssueAccess(issue);
//
//        issue.setStatus(request.getIssueStatus());
//
//        issueRepository.save(issue);
//    }


    @Override
    public void updateIssueStatus(Long issueId, UpdateIssueStatusRequest request) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        organizationSecurityService.validateIssueAccess(issue);

        IssueStatus oldStatus = issue.getStatus();

        issue.setStatus(request.getIssueStatus());

        issueRepository.save(issue);

        activityService.logActivity(
                issue,
                currentUserService.getCurrentUser(),
                ActivityAction.STATUS_CHANGED,
                "Status changed from " + oldStatus
                        + " to " + request.getIssueStatus()
        );
    }


    //Issue and Assigned
    @Override
    public IssueResponse assignIssueToUser(Long issueId, Long userId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        organizationSecurityService.validateIssueAccess(issue);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        organizationSecurityService.validateIssueAndUser(issue,user);

        issue.setAssignedTo(user);
        issueRepository.save(issue);

        notificationService.createNotification(user,
                 "You were asigned to "+issue.getTitle());

        activityService.logActivity(
                issue,
                currentUserService.getCurrentUser(),
                ActivityAction.ASSIGNED,
                "Assigned issue to " + user.getName()
        );

        return mapToResponse(issue);
    }



    // Search
//    @Override
//    public List<IssueResponse> searchIssues(IssueSearchRequest request) {
//
//        User currentUser = currentUserService.getCurrentUser();
//
//        Long organizationId = currentUser.getOrganization().getId();
//
//        List<Issue> issues = issueRepository.searchIssues(
//            organizationId
//                ,request.getStatus()
//        );
//
//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId,
//                request.getStatus(),
//                request.getPriority()
//        );
//
//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId,
//                request.getStatus(),
//                request.getPriority(),
//                request.getProjectId()
//        );
//
//
//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId,
//                request.getStatus(),
//                request.getPriority(),
//                request.getProjectId(),
//                request.getAssigneeId()
//        );
//
//        return issues.stream().map(issue -> mapToResponse(issue)).toList();
//    }


@Override
public Page<IssueResponse> searchIssues(IssueSearchRequest request,Pageable pageable) {

    User currentUser = currentUserService.getCurrentUser();

    Long organizationId = currentUser.getOrganization().getId();

//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId
//                ,request.getStatus()
//        );

//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId,
//                request.getStatus(),
//                request.getPriority()
//        );

//        List<Issue> issues = issueRepository.searchIssues(
//                organizationId,
//                request.getStatus(),
//                request.getPriority(),
//                request.getProjectId()
//        );


    Page<Issue> issues = issueRepository.searchIssues(
            organizationId,
            request.getStatus(),
            request.getPriority(),
            request.getProjectId(),
            request.getAssigneeId(),
            pageable
    );

    //return issues.stream().map(issue -> mapToResponse(issue)).toList();
    return issues.map(issue -> mapToResponse(issue));
}

    private IssueResponse mapToResponse(Issue issue) {

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getProject().getName(),
                issue.getSprint() != null
                        ? issue.getSprint().getName()
                        : null,
                issue.getStoryPoints(),

                issue.getAssignedTo() != null ? issue.getAssignedTo().getId() : null,

                issue.getAssignedTo() != null
                        ? issue.getAssignedTo().getName()
                        : null
        );
    }
}

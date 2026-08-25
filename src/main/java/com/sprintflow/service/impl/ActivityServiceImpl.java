package com.sprintflow.service.impl;

import com.sprintflow.dto.ActivityResponse;
import com.sprintflow.entity.Activity;
import com.sprintflow.entity.Issue;
import com.sprintflow.entity.User;
import com.sprintflow.enums.ActivityAction;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.ActivityRepository;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.service.ActivityService;
import com.sprintflow.service.OrganizationSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final IssueRepository issueRepository;
    private final OrganizationSecurityService organizationSecurityService;


    @Override
    public void logActivity(
            Issue issue,
            User performedBy,
            ActivityAction action,
            String description) {

        Activity activity = Activity.builder()
                .issue(issue)
                .performedBy(performedBy)
                .action(action)
                .description(description)
                .build();

        activityRepository.save(activity);
    }


    @Override
    public List<ActivityResponse> getActivitiesByIssue(
            Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Issue not found"));

        organizationSecurityService
                .validateIssueAccess(issue);

        return activityRepository
                .findByIssueIdOrderByCreatedAtDesc(issueId)
                .stream()
                .map(activity ->
                        ActivityResponse.builder()
                                .id(activity.getId())
                                .action(activity.getAction())
                                .description(
                                        activity.getDescription())
                                .performedBy(
                                        activity.getPerformedBy() != null
                                                ? activity.getPerformedBy().getName()
                                                : null)
                                .performedByEmail(
                                        activity.getPerformedBy() != null
                                                ? activity.getPerformedBy().getEmail()
                                                : null)
                                .createdAt(
                                        activity.getCreatedAt())
                                .build())
                .toList();
    }
}